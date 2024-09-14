package com.lightning.northstar.contraptions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.lightning.northstar.NorthstarTags;
import com.lightning.northstar.block.NorthstarBlocks;
import com.lightning.northstar.block.NorthstarTechBlocks;
import com.lightning.northstar.block.tech.computer_rack.TargetingComputerRackBlockEntity;
import com.lightning.northstar.block.tech.jet_engine.JetEngineBlock;
import com.lightning.northstar.block.tech.oxygen_generator.OxygenGeneratorBlockEntity;
import com.lightning.northstar.block.tech.rocket_station.RocketStationBlockEntity;
import com.lightning.northstar.block.tech.temperature_regulator.TemperatureRegulatorBlockEntity;
import com.lightning.northstar.item.NorthstarItems;
import com.lightning.northstar.world.dimension.NorthstarPlanets;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.ContraptionType;
import com.simibubi.create.content.contraptions.ContraptionWorld;
import com.simibubi.create.content.contraptions.MountedStorageManager;
import com.simibubi.create.content.contraptions.TranslatingContraption;
import com.simibubi.create.content.contraptions.minecart.TrainCargoManager;
import com.simibubi.create.content.contraptions.render.ContraptionLighter;
import com.simibubi.create.content.contraptions.render.NonStationaryLighter;
import com.simibubi.create.content.fluids.tank.CreativeFluidTankBlockEntity;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
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
import net.minecraftforge.items.ItemStackHandler;

public class RocketContraption extends TranslatingContraption{

	public int fuelCost = 0;
	public int fuelReturnCost = 0;
	public int weightCost = 0;
	public int heatShielding = 0;
	public int blockCount = 0;
	private boolean rocket_station = false;
	public boolean hasControls = false;
	public boolean hasInterplanetaryNavigation = false;
	private boolean has_fuel = false;
	public boolean isUsingTicket;
	private int fuelAmount = 0;
	private int jet_engines = 0;
	private int visual_jet_engines = 0;
	public float computingPower = 0;
	private List<BlockPos> assembledJets;
	public int fuelTicks;
	public String name = "Rocket";
	public Player owner;
	public BlockPos localControlsPos;

	protected MountedStorageManager storageProxy;

	public ResourceKey<Level> dest = null;

	static final IItemHandlerModifiable fallbackItems = new ItemStackHandler();
	static final IFluidHandler fallbackFluids = new FluidTank(0);
	
		
	public float prevYaw;
	public float yaw;
	public float targetYaw;	
	
	public RocketContraption() {
		assembledJets = new ArrayList<>();
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
		IFluidHandler rocketFuels = storage.getFluids();
		fuelCost = (int) ((int) weightCost + ((fuelCost - (fuelCost * computingPower))));
		if(owner != null)
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
				
				// this is a bit sketchy in game, it should delete the ticket after it's
				// actually been used, not when assembling the rocket
				// though I can't figure that out so this may have to do
				
				if(rsbe.container.getItem(0).is(NorthstarItems.RETURN_TICKET.get())) {
					if(rsbe.container.getItem(0).getTagElement("Planet") != null) {
						if(NorthstarPlanets.getPlanetDimension(NorthstarPlanets.targetGetter(rsbe.container.getItem(0).getTagElement("Planet").toString())) == dest)
						this.isUsingTicket = true;
						this.isUsingTicket = true;
					}
				}
			}
		}
		if (blockState.is(NorthstarBlocks.INTERPLANETARY_NAVIGATOR.get())) {
			this.hasInterplanetaryNavigation = true;
		}
		if(NorthstarTechBlocks.COMPUTER_RACK.has(blockState)) {
			BlockEntity ent = world.getBlockEntity(pos);
			if(ent instanceof TargetingComputerRackBlockEntity crbe) {
				for(int b = 0; b < crbe.container.getContainerSize(); b++){
					if(crbe.container.getItem(b).is(NorthstarItems.TARGETING_COMPUTER.get())) {
						if(computingPower < 0.4)
							computingPower += 0.0025;
					}
				}
			}
		}
		
		if (NorthstarTechBlocks.ROCKET_CONTROLS.has(blockState)) {
			hasControls = true;
			if(this.localControlsPos == null) {
				this.localControlsPos = this.toLocalPos(pos);
			}
		}
		if (NorthstarTechBlocks.JET_ENGINE.has(blockState)) {
			jet_engines += 1;
			if(blockState.getValue(JetEngineBlock.BOTTOM)) {
				visual_jet_engines++;
			}
			assembledJets.add(toLocalPos(pos));
		}
		if (AllBlocks.FLUID_TANK.has(blockState)) {
			FluidTankBlockEntity tank = (FluidTankBlockEntity) world.getBlockEntity(pos);
			IFluidTank fluid = tank.getTankInventory();
			if (NorthstarTags.NorthstarFluidTags.TIER_1_ROCKET_FUEL.matches(fluid.getFluid().getFluid())) {
				if(fluid.getFluidAmount() != 0)
				fuelAmount += fluid.getFluidAmount();
				has_fuel = true;
			}
			if (NorthstarTags.NorthstarFluidTags.TIER_2_ROCKET_FUEL.matches(fluid.getFluid().getFluid())) {
				if(fluid.getFluidAmount() != 0)
				fuelAmount += fluid.getFluidAmount() * 2;
				has_fuel = true;
			}
			if (NorthstarTags.NorthstarFluidTags.TIER_3_ROCKET_FUEL.matches(fluid.getFluid().getFluid())) {
				if(fluid.getFluidAmount() != 0)
				fuelAmount += fluid.getFluidAmount() * 4;
				has_fuel = true;
			}
		}
		if(blockState.is(AllBlocks.CREATIVE_FLUID_TANK.get())) {
			CreativeFluidTankBlockEntity tank = (CreativeFluidTankBlockEntity) world.getBlockEntity(pos);
			IFluidTank fluid = tank.getTankInventory();
			if (NorthstarTags.NorthstarFluidTags.TIER_1_ROCKET_FUEL.matches(fluid.getFluid().getFluid())) {
				fuelAmount = Integer.MAX_VALUE;
				has_fuel = true;
			}
			if (NorthstarTags.NorthstarFluidTags.TIER_2_ROCKET_FUEL.matches(fluid.getFluid().getFluid())) {
				fuelAmount = Integer.MAX_VALUE;
				has_fuel = true;
			}
			if (NorthstarTags.NorthstarFluidTags.TIER_3_ROCKET_FUEL.matches(fluid.getFluid().getFluid())) {
				fuelAmount = Integer.MAX_VALUE;
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
		{heatShielding += 3;}
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
		entity.level.getProfiler().pop();
	}	

	@Override
	public boolean canBeStabilized(Direction facing, BlockPos localPos) {
		return false;
	}

	@Override
	public ContraptionType getType() {
		return NorthstarContraptionTypes.ROCKET;
	}
	
	public boolean hasRocketStation() {
		return rocket_station;
	}

	public int hasJetEngine() {
		return jet_engines;
	}
	public int getVisualJetEngines() {
		return visual_jet_engines;
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
	protected boolean isAnchoringBlockAt(BlockPos pos) {
		return super.isAnchoringBlockAt(pos.relative(Direction.DOWN));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public ContraptionLighter<?> makeLighter() {
		return new NonStationaryLighter<>(this);
	}
}
