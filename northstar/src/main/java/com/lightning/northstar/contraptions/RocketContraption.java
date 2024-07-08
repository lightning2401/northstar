package com.lightning.northstar.contraptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.lightning.northstar.NorthstarTags;
import com.lightning.northstar.block.NorthstarTechBlocks;
import com.lightning.northstar.block.tech.computer_rack.TargetingComputerRackBlockEntity;
import com.lightning.northstar.block.tech.oxygen_generator.OxygenGeneratorBlockEntity;
import com.lightning.northstar.block.tech.rocket_station.RocketStationBlockEntity;
import com.lightning.northstar.block.tech.temperature_regulator.TemperatureRegulatorBlockEntity;
import com.lightning.northstar.item.NorthstarItems;
import com.lightning.northstar.world.dimension.NorthstarPlanets;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.ContraptionType;
import com.simibubi.create.content.contraptions.MountedStorageManager;
import com.simibubi.create.content.contraptions.TranslatingContraption;
import com.simibubi.create.content.contraptions.actors.trainControls.ControlsBlock;
import com.simibubi.create.content.contraptions.minecart.TrainCargoManager;
import com.simibubi.create.content.contraptions.render.ContraptionLighter;
import com.simibubi.create.content.contraptions.render.NonStationaryLighter;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.content.trains.entity.ArrivalSoundQueue;
import com.simibubi.create.foundation.utility.Couple;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

public class RocketContraption extends TranslatingContraption{
	
	private Direction assemblyDirection = Direction.UP;

	public Map<BlockPos, Couple<Boolean>> conductorSeats;
	public ArrivalSoundQueue soundQueue;
	public int fuelCost = 0;
	public int weightCost = 0;
	public int heatShielding = 0;
	public int blockCount = 0;
	private List<BlockPos> jetEngines;
	private boolean rocket_station = false;
	public boolean hasControls = false;
	private boolean has_fuel = false;
	private boolean isUsingTicket = false;
	private int fuelAmount = 0;
	private int jet_engines = 0;
	public float computingPower = 0;
	private List<BlockPos> assembledJets;
	private BlockPos stationPos;
	public int fuelTicks;
	public String name = "Rocket";
	public Player owner;

	protected MountedStorageManager storageProxy;

	// render
	public int portalCutoffMin;
	public int portalCutoffMax;
	public ResourceKey<Level> dest = null;

	static final IItemHandlerModifiable fallbackItems = new ItemStackHandler();
	static final IFluidHandler fallbackFluids = new FluidTank(0);
	
		
	public float prevYaw;
	public float yaw;
	public float targetYaw;	
	
	public RocketContraption() {
		conductorSeats = new HashMap<>();
		jetEngines = new ArrayList<>();
		assembledJets = new ArrayList<>();
		soundQueue = new ArrivalSoundQueue();
		portalCutoffMin = Integer.MIN_VALUE;
		portalCutoffMax = Integer.MAX_VALUE;
		storage = new TrainCargoManager();
	}

	@Override
	public boolean assemble(Level world, BlockPos pos) throws AssemblyException {
		if (!searchMovedStructure(world, pos, null))
			return false;
		System.out.print(blocks.size());
		startMoving(world);
		
		return true;
	}
	public void burnFuel() {
		if(isUsingTicket) {
			IItemHandlerModifiable items = storage.getItems();
			
			if (items != null) {
				for (int slot = 0; slot < items.getSlots(); slot++) {
					ItemStack stack = items.extractItem(slot, 1, true);
					if(!stack.is(NorthstarItems.RETURN_TICKET.get()))
						continue;
	
					stack = items.extractItem(slot, 1, false);
					ItemStack containerItem = stack.getCraftingRemainingItem();
					if (!containerItem.isEmpty())
						ItemHandlerHelper.insertItemStacked(items, containerItem, false);
					return;
				}
			}
			
		}
		
		IFluidHandler rocketFuels = storage.getFluids();
		fuelCost = (int) ((int) weightCost + ((fuelCost - (fuelCost * computingPower))));
		if(owner != null)
		owner.displayClientMessage(Component.literal("fuel cost: " + fuelCost).withStyle(ChatFormatting.YELLOW), false);
		for (int slot = 0; slot < rocketFuels.getTanks(); slot++) {
			if(NorthstarTags.NorthstarFluidTags.TIER_1_ROCKET_FUEL.matches(rocketFuels.getFluidInTank(slot).getFluid()) && rocketFuels.getFluidInTank(slot).getAmount() > fuelCost) 
			{rocketFuels.drain(fuelCost, FluidAction.EXECUTE);
			return;}
			if(NorthstarTags.NorthstarFluidTags.TIER_2_ROCKET_FUEL.matches(rocketFuels.getFluidInTank(slot).getFluid()) && rocketFuels.getFluidInTank(slot).getAmount() > fuelCost) 
			{rocketFuels.drain(Math.round(fuelCost / 2), FluidAction.EXECUTE);
			return;}
			if(NorthstarTags.NorthstarFluidTags.TIER_3_ROCKET_FUEL.matches(rocketFuels.getFluidInTank(slot).getFluid()) && rocketFuels.getFluidInTank(slot).getAmount() > fuelCost) 
			{rocketFuels.drain(Math.round(fuelCost / 4), FluidAction.EXECUTE);
			return;}
		}
	}
	
	@Override
	protected Pair<StructureBlockInfo, BlockEntity> capture(Level world, BlockPos pos) {
		BlockState blockState = world.getBlockState(pos);
		
		if (NorthstarTechBlocks.ROCKET_STATION.has(blockState)) {
			rocket_station = true;
			BlockEntity ent = world.getBlockEntity(pos);
			if(ent instanceof RocketStationBlockEntity rsbe) {
				name = rsbe.name;
				stationPos = toLocalPos(pos);
				System.out.println("rocket station: " + rsbe.container.getItem(0));
				if(rsbe.container.getItem(0).is(NorthstarItems.RETURN_TICKET.get())) {
					isUsingTicket = true;
					storage.addBlock(stationPos, rsbe);
				}
			}
		}
		if(NorthstarTechBlocks.COMPUTER_RACK.has(blockState)) {
			BlockEntity ent = world.getBlockEntity(pos);
			System.out.println("COMPUTER RACK DETECTED!");
			if(ent instanceof TargetingComputerRackBlockEntity crbe) {
				for(int b = 0; b < crbe.container.getContainerSize(); b++){
					if(crbe.container.getItem(b).is(NorthstarItems.TARGETING_COMPUTER.get())) {
						if(computingPower < 0.4)
							computingPower += 0.0025;
						System.out.println("CPU detected! " + computingPower);
					}
				}
			}
		}
		
		if (NorthstarTechBlocks.ROCKET_CONTROLS.has(blockState)) {
			hasControls = true;
		}
		if (NorthstarTechBlocks.JET_ENGINE.has(blockState)) {
			jet_engines += 1;
			assembledJets.add(toLocalPos(pos));
		}
		if (AllBlocks.FLUID_TANK.has(blockState)) {
			FluidTankBlockEntity tank = (FluidTankBlockEntity) world.getBlockEntity(pos);
			IFluidTank fluid = tank.getTankInventory();
			if (NorthstarTags.NorthstarFluidTags.TIER_1_ROCKET_FUEL.matches(fluid.getFluid().getFluid())) {
				if(fluid.getFluidAmount() != 0)
				fuelAmount += fluid.getFluidAmount();
				System.out.println("Fuel Amount: " + fuelAmount);
				has_fuel = true;
			}
			if (NorthstarTags.NorthstarFluidTags.TIER_2_ROCKET_FUEL.matches(fluid.getFluid().getFluid())) {
				if(fluid.getFluidAmount() != 0)
				fuelAmount += fluid.getFluidAmount() * 2;
				System.out.println("Fuel Amount: " + fuelAmount);
				has_fuel = true;
			}
			if (NorthstarTags.NorthstarFluidTags.TIER_3_ROCKET_FUEL.matches(fluid.getFluid().getFluid())) {
				if(fluid.getFluidAmount() != 0)
				fuelAmount += fluid.getFluidAmount() * 4;
				System.out.println("Fuel Amount: " + fuelAmount);
				has_fuel = true;
			}
		}
		if(!blockState.is(Blocks.AIR) && !blockState.is(Blocks.CAVE_AIR)) {
			blockCount++;
		}
		if(blockState.is(NorthstarTechBlocks.OXYGEN_GENERATOR.get())) {
			OxygenGeneratorBlockEntity ent = (OxygenGeneratorBlockEntity) world.getBlockEntity(pos);
			ent.removeOxy(ent, new HashSet<>());
		}else if(blockState.is(NorthstarTechBlocks.TEMPERATURE_REGULATOR.get())) {
			TemperatureRegulatorBlockEntity ent2 = (TemperatureRegulatorBlockEntity) world.getBlockEntity(pos);
			ent2.removeTemp(ent2);
		}
		
		if(blockState.is(NorthstarTags.NorthstarBlockTags.HEAVY_BLOCKS.tag) && !blockState.is(Blocks.AIR)) 
		{weightCost += 5;}else 
		if(blockState.is(NorthstarTags.NorthstarBlockTags.SUPER_HEAVY_BLOCKS.tag) && !blockState.is(Blocks.AIR))
		{weightCost += 10;} 
		else if(!blockState.is(Blocks.AIR)){weightCost+=1;}
		if(blockState.is(NorthstarTags.NorthstarBlockTags.TIER_1_HEAT_RESISTANCE.tag) && !blockState.is(Blocks.AIR)) 
		{heatShielding++;}
		if(blockState.is(NorthstarTags.NorthstarBlockTags.TIER_2_HEAT_RESISTANCE.tag) && !blockState.is(Blocks.AIR)) 
		{heatShielding += 8;}
		if(blockState.is(NorthstarTags.NorthstarBlockTags.TIER_3_HEAT_RESISTANCE.tag) && !blockState.is(Blocks.AIR))
		{heatShielding += 20;}
		return super.capture(world, pos);
	}
	
	public void changeDim(ResourceKey<Level> targetDim, RocketContraptionEntity entity) {
		entity.level.getProfiler().push("changeDimension");
		ServerLevel serverlevel = (ServerLevel)entity.level;
        MinecraftServer minecraftserver = serverlevel.getServer();
        ServerLevel target = minecraftserver.getLevel(targetDim);
        target.setChunkForced((int)entity.getX(), (int)entity.getZ(), true);
		System.out.println(target.isLoaded(entity.blockPosition()));
		for (Entity e : entity.getPassengers()) {
			System.out.println(e);
			e.changeDimension(target);
		}
		entity.changeDimension(target);
		entity.getContraption().getContraptionWorld();
		System.out.println("AAAAAAAA WE ARE IN SPACE!!!!!! PANIC PANIC PANIC PANIC");
		entity.level.getProfiler().pop();
	}
	
	
	public boolean inControl(BlockPos pos, Direction direction) {
		BlockPos controlsPos = pos.relative(direction);
		if (!blocks.containsKey(controlsPos))
			return false;
		StructureBlockInfo info = blocks.get(controlsPos);
		if (!AllBlocks.TRAIN_CONTROLS.has(info.state))
			return false;
		return info.state.getValue(ControlsBlock.FACING) == direction.getOpposite();
	}
	

	@Override
	public boolean canBeStabilized(Direction facing, BlockPos localPos) {
		return false;
	}

	@Override
	public ContraptionType getType() {
		return NorthstarContraptionTypes.ROCKET;
	}
	
	@Override
	public CompoundTag writeNBT(boolean spawnPacket) {
		CompoundTag tag = super.writeNBT(spawnPacket);
		return tag;
	}
	
	public boolean hasRocketStation() {
		return rocket_station;
	}

	public int hasJetEngine() {
		return jet_engines;
	}
	public boolean hasFuel() {
		return has_fuel;
	}
	public int fuelAmount() {
		return fuelAmount;
	}
	public int heatShielding() {
		return heatShielding;
	}

	@Override
	public void readNBT(Level world, CompoundTag tag, boolean spawnData) {
		super.readNBT(world, tag, spawnData);
	}
	
	@Override
	protected boolean isAnchoringBlockAt(BlockPos pos) {
		return super.isAnchoringBlockAt(pos.relative(Direction.DOWN));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public ContraptionLighter<?> makeLighter() {
		return new NonStationaryLighter<>(this);
	}
}
