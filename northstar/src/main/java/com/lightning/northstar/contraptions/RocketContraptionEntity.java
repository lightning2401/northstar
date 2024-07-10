package com.lightning.northstar.contraptions;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.lightning.northstar.NorthstarPackets;
import com.lightning.northstar.entity.NorthstarEntityTypes;
import com.lightning.northstar.item.NorthstarItems;
import com.lightning.northstar.sound.NorthstarSounds;
import com.lightning.northstar.world.TemperatureStuff;
import com.lightning.northstar.world.dimension.NorthstarPlanets;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllMovementBehaviours;
import com.simibubi.create.AllPackets;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.ContraptionBlockChangedPacket;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.contraptions.actors.harvester.HarvesterMovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.gantry.GantryContraptionUpdatePacket;
import com.simibubi.create.content.kinetics.base.BlockBreakingMovementBehaviour;
import com.simibubi.create.foundation.utility.ServerSpeedProvider;
import com.simibubi.create.foundation.utility.VecHelper;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion.BlockInteraction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CocoaBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.PacketDistributor;

public class RocketContraptionEntity extends AbstractContraptionEntity {
	
	double clientOffsetDiff;
	double axisMotion;
	public boolean launched;
	public boolean landing;
	boolean fuelBurned = false;
	boolean printed = false;
	public boolean blasting = false;
	public boolean slowing = false;
	public boolean hasExploded = false;
	public boolean isUsingTicket = false;
	int i = 90;
	int soundTime = 0;
	int cooldown = 0;
	int cooldownLength = 100;
	private int maxSpeed = 5;
	public int launchtime = 0;
	public int visualEngineCount = 0;
	private boolean activeLaunch = false;
	public Player owner;
	public UUID ownerID;

	public double sequencedOffsetLimit;
	public float lift_vel = 0.5f;
	public float final_lift_vel = lift_vel - 0.5f;
	public ResourceKey<Level> home;
	public ResourceKey<Level> destination;
	CompoundTag serialisedEntity;
	Map<Integer, CompoundTag> serialisedPassengers;
	public WeakReference<RocketContraptionEntity> entity;
	@SuppressWarnings("unused")
	private Vec3 serverPrevPos;
	public List<Entity> entitiesInContraption = new ArrayList<Entity>();

	public RocketContraptionEntity(EntityType<?> entityTypeIn, Level worldIn) {
		super(entityTypeIn, worldIn);
		sequencedOffsetLimit = -1;
		lift_vel = 0.5f;
		launched = true;
		landing = false;
	}
	
	public static RocketContraptionEntity create(Level world, Contraption contraption) {
		RocketContraptionEntity entity = new RocketContraptionEntity(NorthstarEntityTypes.ROCKET_CONTRAPTION.get(), world);
		entity.setContraption(contraption);
		return entity;
	}
	
	public void limitMovement(double maxOffset) {
		sequencedOffsetLimit = maxOffset;
	}

	@OnlyIn(Dist.CLIENT)
	static RocketAirSound flyingSound = new RocketAirSound(SoundEvents.ELYTRA_FLYING, 0);
	
	@Override
	protected void tickContraption() {
		if (!(contraption instanceof RocketContraption))
			return;
		if(launchtime > 0 && activeLaunch) {launchtime--;}
		if(launchtime == 0 && activeLaunch)
		{blasting = true;}
		if(visualEngineCount == 0) {
			visualEngineCount = ((RocketContraption)this.contraption).getVisualJetEngines();
		}
		
		if(this.owner == null && ((RocketContraption)this.contraption).owner != null) {
			this.owner = ((RocketContraption)this.contraption).owner;
		}
		
		if(this.owner == null && this.ownerID != null) {
			this.owner = level.getPlayerByUUID(ownerID);
		}
		
		if(((RocketContraption)this.contraption).isUsingTicket) {
			this.isUsingTicket = true;
		}
		
		if(this.tickCount % 40 == 0 && !this.level.isClientSide) {
			System.out.println("syncing i guess!!!");
			NorthstarPackets.getChannel().send(PacketDistributor.TRACKING_ENTITY.with(() -> this),
					new RocketContraptionSyncPacket(this.position(), lift_vel, this.getId()));
		}
		
		
		
		RocketContraption contrap = ((RocketContraption)this.contraption);
		if(contrap.owner != null && printed == false)
		{
			
		double heatCost = (TemperatureStuff.getHeatRating(destination) * ((RocketContraption)contraption).blockCount) + TemperatureStuff.getHeatConstant(destination);
		double heatCostHome = (TemperatureStuff.getHeatRating(level.dimension()) * ((RocketContraption)contraption).blockCount) + TemperatureStuff.getHeatConstant(level.dimension());
		if(heatCostHome > heatCost) {
			heatCost = heatCostHome;
		}	
		int requiredJets = ((RocketContraption)this.contraption).fuelCost / 800;
		contrap.owner.displayClientMessage(Component.literal
		("Full Fuel Cost: " + (int)(contrap.weightCost + (contrap.fuelCost - (contrap.fuelCost * contrap.computingPower)))).withStyle(ChatFormatting.GOLD), false);
		contrap.owner.displayClientMessage(Component.literal
		("Current Fuel Supply: " + contrap.fuelAmount()).withStyle(ChatFormatting.GOLD), false);
		contrap.owner.displayClientMessage(Component.literal   
		("Required Heat Shielding: " + heatCost).withStyle(ChatFormatting.YELLOW), false);
		contrap.owner.displayClientMessage(Component.literal
		("Current Heat Shielding: " + contrap.heatShielding()).withStyle(ChatFormatting.YELLOW), false);
		contrap.owner.displayClientMessage(Component.literal
		("Required Engines: " + requiredJets).withStyle(ChatFormatting.BLUE), false);
		contrap.owner.displayClientMessage(Component.literal
		("Current Engine Count: " + contrap.hasJetEngine()).withStyle(ChatFormatting.BLUE), false);
		contrap.owner.displayClientMessage(Component.literal
		("All entities should remain seated for the duration of the flight!").withStyle(ChatFormatting.AQUA), false);
		printed = true;}
		if(destination == null) {
			//bruh :(
			System.out.println("well that didnt work bruh");
			destination = Level.OVERWORLD;
		}
		if(((RocketContraption)this.contraption).fuelAmount() < ((RocketContraption)this.contraption).fuelCost && fuelBurned == false){
			this.disassemble();
		}
		if(!fuelBurned && this.getY() > this.level.getMaxBuildHeight() + 100)
		{((RocketContraption)this.contraption).burnFuel(); fuelBurned = true;}
		
		if (soundTime % 40 == 0 && launchtime == 0 && blasting && !landing) 
		{this.level.playLocalSound(this.getX(), this.getY() - 20, this.getZ(), NorthstarSounds.ROCKET_BLAST.get(), SoundSource.BLOCKS, 5, 0, false); i = 0;
		soundTime = 0;}else if(!landing){soundTime++;}
		
		// this code feels really stupid but I don't care enough to clean it up
		//also this is the code for the air sound when soaring through the air
		if(Math.abs(final_lift_vel) > 0.5f)
		{int volume = NorthstarPlanets.getPlanetAtmosphereCost(level.dimension()) / 400;
		//this is so stupid
		int final_vol = volume < 1 ? 1 : volume;
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
		() -> () -> tickAirSound(final_vol));}
		else if(Math.abs(final_lift_vel) < 0.5f && flyingSound != null)
		{flyingSound.stopSound();}
		
		if (slowing && landing) 
		{this.level.playLocalSound(this.getX(), this.getY() - 8, this.getZ(), NorthstarSounds.ROCKET_LANDING.get(), SoundSource.BLOCKS, 4, 0, false); 
		i = 0; soundTime = 0; 
	//	System.out.println("AAAAAAAAA");
		}
		entitiesInContraption = this.level.getEntities(this, this.getBoundingBox());

		//blasting is a thing because rocket controls needs it to work
		if(launched && blasting)
		{lift_vel += lift_vel / 200;lift_vel = Mth.clamp(lift_vel, 0.5f, maxSpeed); final_lift_vel = lift_vel - 0.5f;	
//		System.out.println(lift_vel);
		}	
		if (landing) {
			if(cooldown <= cooldownLength)
			{cooldown++;}
			if(cooldown >= cooldownLength) {
				if(!slowing)
				{lift_vel -= 0.02;}else {lift_vel -= lift_vel / 10;}
				lift_vel = Mth.clamp(lift_vel, -maxSpeed, -0.4f);
//				System.out.println(lift_vel);
				final_lift_vel = lift_vel;
			}
			
		}
		//starting landing
		if (this.getY() > 1750 && launched) {
			this.cooldown = 0;
			this.launched = false;
			this.landing = true;
			this.final_lift_vel = 0;
			this.lift_vel = 0;
			System.out.println("bruger");
		}

		double prevAxisMotion = axisMotion;
		if (level.isClientSide) {
			clientOffsetDiff *= .75f;
			updateClientMotion();   
		}
		alignEntity();
		tickActors();
		Vec3 movementVec = getDeltaMovement();
		Direction dir = landing ? Direction.DOWN : Direction.UP;
		if (customCollision(dir)) {
			level.playLocalSound(getX(), getY(), getZ(), AllSoundEvents.STEAM.getMainEvent(), SoundSource.BLOCKS, 3, 0, true);
			flyingSound.stopSound();
			if (!level.isClientSide && (Math.abs(final_lift_vel) < 3 || hasExploded)) {
				System.out.println("isUsingTicket: " + isUsingTicket);
				if(this.landing && !isUsingTicket) {
					ItemStack returnTicket = this.createReturnTicket(this);
					if(owner != null) {
					Player player = owner;
			        level.addFreshEntity(new ItemEntity(level, player.getX(), player.getY(), player.getZ(), returnTicket));}
				}
				disassemble();
				if(this.landing && isUsingTicket) {
					System.out.println("attempting to delete ticket");
					RocketHandler.deleteTicket(level, this.blockPosition());
				}
			}
			if(Math.abs(final_lift_vel) > 3 && !hasExploded) {
				level.explode(this, getX(), getY() - 1, getZ(), 30, NorthstarPlanets.getPlanetOxy(destination), BlockInteraction.DESTROY);
				hasExploded = true;
			}
		}
		if (!isStalled() && tickCount > 2) {
			if (sequencedOffsetLimit >= 0)
				movementVec = VecHelper.clampComponentWise(movementVec, (float) sequencedOffsetLimit);
			move(movementVec.x, movementVec.y + final_lift_vel, movementVec.z);
			if (sequencedOffsetLimit > 0)
				sequencedOffsetLimit = Math.max(0, sequencedOffsetLimit - movementVec.length());
		}
		if (Math.signum(prevAxisMotion) != Math.signum(axisMotion) && prevAxisMotion != 0)
			contraption.stop(level);
		if (!level.isClientSide && (prevAxisMotion != axisMotion || tickCount % 3 == 0))
			sendPacket();
		slowing = false;
	}
	
	@OnlyIn(Dist.CLIENT)
	private void tickAirSound(float maxVolume) {
		float pitch = (float) Mth.clamp(getDeltaMovement()
			.length(), .2f, 3f);
		if (flyingSound == null || flyingSound.isStopped()) {
			flyingSound = new RocketAirSound(SoundEvents.ELYTRA_FLYING, pitch);
			Minecraft.getInstance()
				.getSoundManager()
				.play(flyingSound);
		}
		flyingSound.setPitch(pitch);
		flyingSound.fadeIn(maxVolume);
	}
	@SuppressWarnings("resource")
	@OnlyIn(Dist.CLIENT)
	public static void handleSyncPacket(RocketContraptionSyncPacket packet) {
//		System.out.println("ALERTA!!! ALERTA!!! HANDLING!!!!");
		if (Minecraft.getInstance().level.getEntity(packet.contraptionEntityId) instanceof RocketContraptionEntity rce) {
			rce.lift_vel = packet.lift_vel;
			rce.setPos(packet.pos.x, packet.pos.y, packet.pos.z);
		}
	}
	
	public ItemStack createReturnTicket(RocketContraptionEntity entity) {
		ItemStack result = new ItemStack(NorthstarItems.RETURN_TICKET.get());
		result.setHoverName(Component.translatable("item.northstar.return_ticket" + "_" +  NorthstarPlanets.getPlanetName(entity.home)).setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA).withItalic(false)));
        CompoundTag tag = result.getOrCreateTagElement("Planet");
        tag.putString("name", NorthstarPlanets.getPlanetName(entity.home));
        return result;
	}
	
	
	public void startLanding() {
		this.launched = false;
		this.landing = true;
		this.lift_vel = 0;
	}
	@Override
	public Component getContraptionName() {
		if(this.contraption instanceof RocketContraption rc)
		return Component.literal(rc.name);
		else
		return getName();
	}
	
	@Override
	public boolean startControlling(BlockPos controlsLocalPos, Player player) {
		if (player == null || player.isSpectator())
			return false;
		return true;
	}
	
	public void updateClientMotion() {
		Direction dir = Direction.UP;
		float modifier = dir.getAxisDirection()
			.getStep();
//		System.out.println("axisMotion " + axisMotion);
//		System.out.println("clientOffsetDiff " + clientOffsetDiff);
		Vec3 motion = Vec3.atLowerCornerOf(Direction.UP.getNormal())
			.scale((axisMotion + clientOffsetDiff * modifier / 2f) * ServerSpeedProvider.get());
		if (sequencedOffsetLimit >= 0)
			motion = VecHelper.clampComponentWise(motion, (float) sequencedOffsetLimit);
		setDeltaMovement(motion);
	}	
	
	public boolean customCollision(Direction dir) {
		Level world = this.getCommandSenderWorld();
		AABB bounds = this.getBoundingBox();
		Vec3 position = this.position();
		BlockPos gridPos = new BlockPos(position);

		if (contraption == null)
			return false;
		if (bounds == null)
			return false;

		// Blocks in the world
		if (dir.getAxisDirection() == AxisDirection.POSITIVE)
			gridPos = gridPos.relative(dir);
		if (isCollidingWithWorld(world, (RocketContraption) this.getContraption(), gridPos, dir))
			return true;
		
		return false;
	}
	
	@Override
	public boolean control(BlockPos controlsLocalPos, Collection<Integer> heldControls, Player player) {
		if (player.isSpectator())
			return false;
		if (!toGlobalVector(VecHelper.getCenterOf(controlsLocalPos), 1).closerThan(player.position(), 8))
			return false;
		if (heldControls.contains(5))
			return false;
		
		boolean spaceDown = heldControls.contains(4);
		if(spaceDown && launched && launchtime == 0 && !blasting) {
			launchtime = 200;
			activeLaunch = true;
		}
		if(spaceDown && landing) {
			slowing = true;
		}
		return true;
	}
	public boolean clientControl(BlockPos controlsLocalPos, Collection<Integer> heldControls, Player player) {
		if (player == null)
			return false;
		if (player.isSpectator())
			return false;
		if(controlsLocalPos == null)
			return false;
		if (!toGlobalVector(VecHelper.getCenterOf(controlsLocalPos), 1).closerThan(player.position(), 8))
			return false;
		if (heldControls.contains(5))
			return false;
		boolean spaceDown = heldControls.contains(4);
		if(spaceDown && launched && launchtime == 0 && !blasting) {
			launchtime = 200;
			activeLaunch = true;
		}
		if(spaceDown && landing) {
			slowing = true;
		}
		return true;
	}

	public static boolean isCollidingWithWorld(Level world, RocketContraption contraption, BlockPos anchor,
			Direction movementDirection) {
			for (BlockPos pos : contraption.getOrCreateColliders(world, movementDirection)) {
				BlockPos colliderPos = pos.offset(anchor);

				BlockState collidedState = world.getBlockState(colliderPos);
				StructureBlockInfo blockInfo = contraption.getBlocks()
					.get(pos);
				boolean emptyCollider = collidedState.getCollisionShape(world, pos)
					.isEmpty();

				if (collidedState.getBlock() instanceof CocoaBlock)
					continue;

				MovementBehaviour movementBehaviour = AllMovementBehaviours.getBehaviour(blockInfo.state);
				if (movementBehaviour != null) {
					if (movementBehaviour instanceof BlockBreakingMovementBehaviour) {
						BlockBreakingMovementBehaviour behaviour = (BlockBreakingMovementBehaviour) movementBehaviour;
						if (!behaviour.canBreak(world, colliderPos, collidedState) && !emptyCollider)
							return true;
						continue;
					}
					if (movementBehaviour instanceof HarvesterMovementBehaviour) {
						HarvesterMovementBehaviour harvesterMovementBehaviour =
							(HarvesterMovementBehaviour) movementBehaviour;
						if (!harvesterMovementBehaviour.isValidCrop(world, colliderPos, collidedState)
							&& !harvesterMovementBehaviour.isValidOther(world, colliderPos, collidedState)
							&& !emptyCollider)
							return true;
						continue;
					}
				}
				
				if (!collidedState.getMaterial()
					.isReplaceable() && !emptyCollider) {
					return true;
				}

			}
			return false;
		}
	
	
	
	
	public void alignEntity() {
		if (!this.level.isClientSide()) {

			for (Entity e : this.getPassengers()) {
				if (!(e instanceof Player))
					continue;
				if (e.distanceToSqr(this) > 32 * 32)
					continue;
			}

			if (this.getPassengers()
				.stream()
				.anyMatch(p -> p instanceof Player)
				) {
			}
			
			this.setServerSidePrevPosition();
		}

		this.setPos(this.position());

		this.xo = this.getX();
		this.yo = this.getY();
		this.zo = this.getZ();
	
	}
	
	public void setServerSidePrevPosition() {
		serverPrevPos = position();
	}
	public RocketContraption getContraption() {
		return (RocketContraption) this.contraption;
	}
	
	
	@SuppressWarnings("unused")
	private void removeAndSaveEntity(RocketContraptionEntity entity, boolean portal) {
		Contraption contraption = entity.getContraption();
		if (contraption != null) {
			Map<UUID, Integer> mapping = contraption.getSeatMapping();
			for (Entity passenger : entity.getPassengers()) {
				if (!mapping.containsKey(passenger.getUUID()))
					continue;

				Integer seat = mapping.get(passenger.getUUID());

				if (passenger instanceof ServerPlayer sp) {
					continue;
				}

				serialisedPassengers.put(seat, passenger.serializeNBT());
			}
		}

		for (Entity passenger : entity.getPassengers())
			if (!(passenger instanceof Player))
				passenger.discard();

		serialize(entity);
		entity.discard();
		this.entity.clear();
	}
	
	private void serialize(Entity entity) {
		serialisedEntity = entity.serializeNBT();
		serialisedEntity.remove("Passengers");
		serialisedEntity.getCompound("Contraption")
			.remove("Passengers");
	}
	
	
	
	
	
	@Override
	public void setBlock(BlockPos localPos, StructureBlockInfo newInfo) {
			AllPackets.getChannel().send(PacketDistributor.TRACKING_ENTITY.with(() -> this),
				new ContraptionBlockChangedPacket(this.getId(), localPos, newInfo.state));
	}
	
	@Override
	public void disassemble() {
		RocketHandler.ROCKETS.remove(this);
		sequencedOffsetLimit = -1;
		super.disassemble();
	}
	
	@Override
	protected void writeAdditional(CompoundTag compound, boolean spawnPacket) {
		if (sequencedOffsetLimit >= 0)
			compound.putDouble("SequencedOffsetLimit", sequencedOffsetLimit);
		
		compound.putBoolean("blasting", this.blasting);
		compound.putBoolean("slowing", this.slowing);
		compound.putBoolean("isUsingTicket", this.isUsingTicket);
		compound.putBoolean("launched", this.launched);
		
		compound.putBoolean("landing", this.landing);
		compound.putBoolean("fuelBurned", this.fuelBurned);
		compound.putBoolean("printed", this.printed);
		compound.putBoolean("activeLaunch", this.activeLaunch);
		
		compound.putString("home", NorthstarPlanets.getPlanetName(home));
		compound.putString("destination", NorthstarPlanets.getPlanetName(destination));
		if(this.owner != null)
		{compound.putUUID("player", this.owner.getUUID());}

		compound.putInt("visualEngineCount", this.visualEngineCount);
		
		compound.putFloat("lift_vel", lift_vel);
		compound.putFloat("final_lift_vel", final_lift_vel);
		
		super.writeAdditional(compound, spawnPacket);
	}

	protected void readAdditional(CompoundTag compound, boolean spawnData) {
		sequencedOffsetLimit =
			compound.contains("SequencedOffsetLimit") ? compound.getDouble("SequencedOffsetLimit") : -1;
		
		this.blasting = compound.contains("blasting") ? compound.getBoolean("SequencedOffsetLimit") : false;
		this.slowing = compound.contains("slowing") ? compound.getBoolean("slowing") : false;
		this.isUsingTicket = compound.contains("isUsingTicket") ? compound.getBoolean("isUsingTicket") : false;
		this.launched = compound.contains("launched") ? compound.getBoolean("launched") : false;
		
		this.landing = compound.contains("landing") ? compound.getBoolean("landing") : false;
		this.fuelBurned = compound.contains("fuelBurned") ? compound.getBoolean("fuelBurned") : false;
		this.printed = compound.contains("printed") ? compound.getBoolean("printed") : false;
		this.activeLaunch = compound.contains("activeLaunch") ? compound.getBoolean("activeLaunch") : false;
		
		if(compound.contains("home")) {home = NorthstarPlanets.getPlanetDimension(compound.getString("home"));}
		if(compound.contains("destination")) {destination = NorthstarPlanets.getPlanetDimension(compound.getString("destination"));}
		
		if(compound.contains("player")){this.ownerID = compound.getUUID("player");}

		if(compound.contains("visualEngineCount")){this.visualEngineCount = compound.getInt("visualEngineCount");}
		if(compound.contains("lift_vel")){this.lift_vel = compound.getFloat("lift_vel");}
		if(compound.contains("final_lift_vel")){this.final_lift_vel = compound.getFloat("final_lift_vel");}
		
		super.readAdditional(compound, spawnData);
	}
	
	
	@Override
	protected boolean isActorActive(MovementContext context, MovementBehaviour actor) {
		if (!(contraption instanceof RocketContraption rc))
			return false;
		if (!super.isActorActive(context, actor))
			return false;
		return level.isClientSide();
	}

	@Override
	public Vec3 applyRotation(Vec3 localPos, float partialTicks) {
		return localPos;
	}

	@Override
	public Vec3 reverseRotation(Vec3 localPos, float partialTicks) {
		return localPos;
	}
	
	public void sendPacket() {
		AllPackets.getChannel()
		.send(PacketDistributor.TRACKING_ENTITY.with(() -> this),
			new GantryContraptionUpdatePacket(getId(), getY(), axisMotion, sequencedOffsetLimit));
	}
	
	public double getAxisCoord() {
		Vec3 anchorVec = getAnchorVec();
		return anchorVec.y;
	}
	

	@Override
	protected StructureTransform makeStructureTransform() {
		return new StructureTransform(new BlockPos(getAnchorVec().add(.5, .5, .5)), 0, 0, 0);
	}

	@Override
	protected float getStalledAngle() {
		return 0;
	}
	
	@Override
	public void teleportTo(double p_70634_1_, double p_70634_3_, double p_70634_5_) {}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void lerpTo(double x, double y, double z, float yw, float pt, int inc, boolean t) {}

	@Override
	protected void handleStallInformation(double x, double y, double z, float angle) {
		setPosRaw(x, y, z);
		clientOffsetDiff = 0;
	}

	@Override
	public ContraptionRotationState getRotationState() {
		return ContraptionRotationState.NONE;
	}

	@Override
	protected void outOfWorld() {
	}

	@Override
	public void applyLocalTransforms(PoseStack matrixStack, float partialTicks) {
		float angleInitialYaw = 0;
		float angleYaw = getViewYRot(partialTicks);
		float anglePitch = getViewXRot(partialTicks);

		matrixStack.translate(0, 0, 0);


		TransformStack.cast(matrixStack)
			.nudge(getId())
			.centre()
			.rotateY(angleYaw)
			.rotateZ(anglePitch)
			.rotateY(angleInitialYaw)
			.unCentre();
	}
	
	
	
	

}
