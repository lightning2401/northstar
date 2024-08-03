package com.lightning.northstar.block.tech.circuit_engraver;

import java.util.ArrayList;
import java.util.List;

import com.lightning.northstar.sound.NorthstarSounds;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.belt.behaviour.BeltProcessingBehaviour;
import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.NBTHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.phys.AABB;

public class EngravingBehaviour extends BeltProcessingBehaviour {
	
	public static final int CYCLE = 5280;
	public static final int ENTITY_SCAN = 10;

	public static List<ItemStack> particleItems = new ArrayList<>();

	public EngravingBehaviourSpecifics specifics;
	public int prevRunningTicks;
	public int runningTicks;
	public boolean running;
	public boolean finished;
	public Mode mode;

	int entityScanCooldown;
	
	public interface EngravingBehaviourSpecifics {
		public boolean tryProcessInBasin(boolean simulate);

		public boolean tryProcessOnBelt(TransportedItemStack input, List<ItemStack> outputList, boolean simulate);

		public boolean tryProcessInWorld(ItemEntity itemEntity, boolean simulate);

		public boolean canProcessInBulk();

		public void onPressingCompleted();

		public int getParticleAmount();

		public float getKineticSpeed();
	}


	public <T extends SmartBlockEntity & EngravingBehaviourSpecifics> EngravingBehaviour(T be) {
		super(be);
		this.specifics = (EngravingBehaviourSpecifics) be;
		mode = Mode.WORLD;
		entityScanCooldown = ENTITY_SCAN;
		whenItemEnters((s, i) -> BeltEngravingCallback.onItemReceived(s, i, this));
		whileItemHeld((s, i) -> BeltEngravingCallback.whenItemHeld(s, i, this));
	}
	
	@Override
	public void read(CompoundTag compound, boolean clientPacket) {
		running = compound.getBoolean("Running");
		mode = Mode.values()[compound.getInt("Mode")];
		finished = compound.getBoolean("Finished");
		prevRunningTicks = runningTicks = compound.getInt("Ticks");
		super.read(compound, clientPacket);

		if (clientPacket) {
			NBTHelper.iterateCompoundList(compound.getList("ParticleItems", Tag.TAG_COMPOUND),
				c -> particleItems.add(ItemStack.of(c)));
		}
	}

	@Override
	public void write(CompoundTag compound, boolean clientPacket) {
		compound.putBoolean("Running", running);
		compound.putInt("Mode", mode.ordinal());
		compound.putBoolean("Finished", finished);
		compound.putInt("Ticks", runningTicks);
		super.write(compound, clientPacket);

		if (clientPacket) {
			compound.put("ParticleItems", NBTHelper.writeCompoundList(particleItems, ItemStack::serializeNBT));
			particleItems.clear();
		}
	}
	
	public void start(Mode mode) {
		this.mode = mode;
		running = true;
		prevRunningTicks = 0;
		runningTicks = 0;
		particleItems.clear();
		blockEntity.sendData();
	}

	public boolean inWorld() {
		return mode == Mode.WORLD;
	}
	
	@Override
	public void tick() {
		super.tick();

		Level level = getWorld();
		BlockPos worldPosition = getPos();

		if (!running || level == null) {
			if (level != null && !level.isClientSide) {

				if (specifics.getKineticSpeed() == 0)
					return;
				if (entityScanCooldown > 0)
					entityScanCooldown--;
				if (entityScanCooldown <= 0) {
					entityScanCooldown = ENTITY_SCAN;

					if (BlockEntityBehaviour.get(level, worldPosition.below(2),
						TransportedItemStackHandlerBehaviour.TYPE) != null)
						return;
					if (AllBlocks.BASIN.has(level.getBlockState(worldPosition.below(2))))
						return;

					for (ItemEntity itemEntity : level.getEntitiesOfClass(ItemEntity.class,
						new AABB(worldPosition.below()).deflate(.125f))) {
						if (!itemEntity.isAlive() || !itemEntity.onGround())
							continue;
						if (!specifics.tryProcessInWorld(itemEntity, true))
							continue;
						start(Mode.WORLD);
						return;
					}
				}

			}
			return;
		}

		if (level.isClientSide && runningTicks == -CYCLE / 2) {
			prevRunningTicks = CYCLE / 2;
			return;
		}

		if (runningTicks == CYCLE / 2 && specifics.getKineticSpeed() != 0) {
			if (inWorld())
				applyInWorld();

			if (level.getBlockState(worldPosition.below(2))
				.getSoundType() == SoundType.WOOL)
				level.playLocalSound(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(),
						NorthstarSounds.LASER_BURN.get(), SoundSource.BLOCKS, 0.5f, 0.75f + (Math.abs(specifics.getKineticSpeed())), false);
			else
				level.playLocalSound(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(),
						NorthstarSounds.LASER_BURN.get(), SoundSource.BLOCKS, 0.5f, 0.75f + (Math.abs(specifics.getKineticSpeed())), false);

			if (!level.isClientSide)
				blockEntity.sendData();
		}

		if (!level.isClientSide && runningTicks > CYCLE) {
			finished = true;
			running = false;
			particleItems.clear();
			specifics.onPressingCompleted();
			blockEntity.sendData();
			return;
		}

		prevRunningTicks = runningTicks;
		runningTicks += getRunningTickSpeed();
		if (prevRunningTicks < CYCLE / 2 && runningTicks >= CYCLE / 2) {
			runningTicks = CYCLE / 2;
			// Pause the ticks until a packet is received
			if (level.isClientSide && !blockEntity.isVirtual())
				runningTicks = -(CYCLE / 2);
		}
	}
	
	protected void applyInWorld() {
		Level level = getWorld();
		BlockPos worldPosition = getPos();
		AABB bb = new AABB(worldPosition.below(1));
		boolean bulk = specifics.canProcessInBulk();

		particleItems.clear();

		if (level.isClientSide)
			return;

		for (Entity entity : level.getEntities(null, bb)) {
			if (!(entity instanceof ItemEntity itemEntity))
				continue;
			if (!entity.isAlive() || !entity.onGround())
				continue;

			entityScanCooldown = 0;
			if (specifics.tryProcessInWorld(itemEntity, false))
				blockEntity.sendData();
			if (!bulk)
				break;
		}
	}
	
	public int getRunningTickSpeed() {
		float speed = specifics.getKineticSpeed();
		if (speed == 0)
			return 0;
		return (int) Mth.lerp(Mth.clamp(Math.abs(speed) / 512f, 0, 1), 1, 60);
	}
	
	public enum Mode {
		WORLD(1), BELT(19f / 16f), BASIN(22f / 16f);

		public float headOffset;

		Mode(float headOffset) {
			this.headOffset = headOffset;
		}
	}


}
