package com.lightning.northstar.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.lightning.northstar.Northstar;
import com.lightning.northstar.NorthstarTags;
import com.lightning.northstar.block.NorthstarTechBlocks;
import com.lightning.northstar.fluids.NorthstarFluids;
import com.lightning.northstar.world.dimension.NorthstarDimensions;
import com.lightning.northstar.world.dimension.NorthstarPlanets;
import com.simibubi.create.AllFluids;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = Northstar.MOD_ID, bus = Bus.FORGE)
public class TemperatureStuff {
	public static HashMap<HashMap<BlockPos, Integer>,ResourceKey<Level>> temperatureSources = new HashMap<HashMap<BlockPos, Integer>, ResourceKey<Level>>();
	public static int maxSize = 24;
	
//	@SubscribeEvent
	public static void onWorldTick(TickEvent.LevelTickEvent event){
    	long t = event.level.getGameTime();
    	if(t % 40 == 0 && event.level.isClientSide) {
    		for(Entry<HashMap<BlockPos, Integer>, ResourceKey<Level>> blocks:	temperatureSources.entrySet()) {
    			if(blocks == null)
    				continue;
    			if(blocks.getValue() == event.level.dimension()) {
    				for(BlockPos pos : blocks.getKey().keySet()) {
    					if(pos == null)
    						continue;
        				event.level.addParticle(ParticleTypes.FLAME, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, 0,0,0);
    				}
    			}
    			
    		}
    	}
    }
    
	public static void markTemp(BlockPos pos, Level level, HashMap<BlockPos, Integer> map, int temp, int sizeX, int sizeY, int sizeZ, int offsetX, int offsetY, int offsetZ) {
		if(level.getBlockState(pos).getBlock() != NorthstarTechBlocks.TEMPERATURE_REGULATOR.get()) {
			map.clear();
			return;
		}
		BlockPos temppos =  pos.immutable();
		temppos = temppos.offset(offsetX, offsetY, offsetZ);
		temppos.offset(-sizeX, -sizeY, -sizeZ);
		for(int x = 0; x < sizeX; x++) {
			for(int y = 0; y < sizeY; y++) {
				for(int z = 0; z < sizeZ; z++) {
					BlockPos newpos = new BlockPos(temppos.getX() + x - sizeX / 2, temppos.getY() + y - sizeY / 2, temppos.getZ() + z - sizeZ / 2);
					if(level.isClientSide)
						{return;}
					map.put(newpos, temp);
					if(level.getBlockState(newpos).is(NorthstarTechBlocks.TEMPERATURE_REGULATOR.get())) 
					{continue;}
					if(!level.getFluidState(newpos).isEmpty()) 
					{level.getBlockState(newpos).tick((ServerLevel) level, newpos, level.random);
					level.getFluidState(newpos).tick(level, newpos);}
				}
			}
		}
    }
	public static void removeSource(BlockPos pos, Level level, HashMap<BlockPos, Integer> map) {
		HashMap<BlockPos, Integer> tempmap = map;
		temperatureSources.remove(map);
		for(BlockPos newpos : tempmap.keySet()) {
			if(level.isClientSide)
			{return;}
			if(!level.getFluidState(newpos).isEmpty()) 
			{level.getBlockState(newpos).tick((ServerLevel) level, newpos, level.random);
			level.getFluidState(newpos).tick(level, newpos);}
		}
		
    }
	
    public static HashMap<BlockPos, Integer> spreadTemp(Level level, HashMap<BlockPos, Integer> list, int maxSize, int temp) {
    	List<BlockPos> newBlocks = new ArrayList<BlockPos>();
    	newBlocks.addAll(list.keySet());
		for(int i = 0; i < newBlocks.size() && i < maxSize;i++) {
		    	BlockPos pos = newBlocks.get(i);
		    	for(Direction direction : Direction.values()) {
		    		BlockPos blockpos = pos.relative(direction);
		    		if(list.containsKey(blockpos)) {
		    			continue;
		    		}
		    		if(canSpread(level.getBlockState(blockpos)) && list.size() < maxSize) 
		    		{list.put(blockpos, temp); newBlocks.add(blockpos);}
					if(level.isClientSide)
					{continue;}
					if(!level.getFluidState(blockpos).isEmpty()) 
					{level.getBlockState(blockpos).tick((ServerLevel) level, blockpos, level.random);
					level.getFluidState(blockpos).tick(level, blockpos);}
		    	}
		}
		return list;   
    }
	
	public static int getTemp(BlockPos pos, Level level) {
		if(!temperatureSources.containsValue(level.dimension()) && level.dimension() != NorthstarDimensions.MERCURY_DIM_KEY)
		{return NorthstarPlanets.getPlanetTemp(level.dimension());}
		for(Entry<HashMap<BlockPos, Integer>, ResourceKey<Level>> blocks:	temperatureSources.entrySet()) {
			if(blocks.getValue() == level.dimension()) {
				if (blocks.getKey().keySet().contains(pos)) {
					return blocks.getKey().get(pos);
				}
			}
			
		}
			
		if(level.dimension() == NorthstarDimensions.MERCURY_DIM_KEY) {
			if(level.canSeeSky(pos) && !level.isNight()) {
				return 434;}
			else {return -200;}
		}
		
		return NorthstarPlanets.getPlanetTemp(level.dimension());
		
	}
	
    public static boolean canSpread(BlockState state) {
        return state.is(NorthstarTags.NorthstarBlockTags.AIR_PASSES_THROUGH.tag) || !state.getFluidState().isEmpty();
     }
    
	public static boolean combustable(FluidState state) {
		if(state.is(NorthstarFluids.HYDROCARBON.getSource().getSource())){
			return true;
		}
		return false;
		
	}
	public static int combustionTemp(FluidState state) {
		if(state.is(NorthstarFluids.HYDROCARBON.getSource().getSource()) || state.is(NorthstarFluids.HYDROCARBON.get())) {
			return 300;
		}
		return 1000;
		
	}
	
	
	public static int getBoilingPoint(FluidState state) {
		if(state.is(Fluids.WATER) || state.is(Fluids.FLOWING_WATER)) {
			return 100;
		}
		if(state.is(Fluids.LAVA) || state.is(Fluids.FLOWING_LAVA)) {
			return 1200;			
		}
		if(state.is(AllFluids.CHOCOLATE.get()) || state.is(AllFluids.HONEY.get())) {
			return 70;
		}
		if(state.is(NorthstarFluids.METHANE.get()) || state.is(NorthstarFluids.METHANE.getSource().getSource())) {
			return -80;
		}
		if(state.is(NorthstarFluids.HYDROCARBON.get()) || state.is(NorthstarFluids.HYDROCARBON.getSource().getSource())) {
			return 500;
		}
		if(state.is(NorthstarFluids.LIQUID_OXYGEN.get()) || state.is(NorthstarFluids.LIQUID_OXYGEN.getSource().getSource())) {
			return -180;
		}
		if(state.is(NorthstarFluids.LIQUID_HYDROGEN.get()) || state.is(NorthstarFluids.LIQUID_HYDROGEN.getSource().getSource())) {
			return -253;
		}
		if(state.is(NorthstarFluids.SULFURIC_ACID.get()) || state.is(NorthstarFluids.SULFURIC_ACID.getSource().getSource())) {
			return 1200;
		}
		return 100;
	}
	public static int getFreezingPoint(FluidState state) {
		if(state.is(Fluids.WATER) || state.is(Fluids.FLOWING_WATER)) {
			return 0;
		}
		if(state.is(Fluids.LAVA) || state.is(Fluids.FLOWING_LAVA)) {
			return -200;			
		}
		if(state.is(AllFluids.CHOCOLATE.get()) || state.is(AllFluids.HONEY.get())) {
			return 20;
		}
		if(state.is(NorthstarFluids.METHANE.get()) || state.is(NorthstarFluids.METHANE.getSource().getSource())) {
			return -200;
		}
		if(state.is(NorthstarFluids.LIQUID_OXYGEN.get()) || state.is(NorthstarFluids.LIQUID_OXYGEN.getSource().getSource())) {
			return -220;
		}
		if(state.is(NorthstarFluids.HYDROCARBON.get()) || state.is(NorthstarFluids.HYDROCARBON.getSource().getSource())) {
			return -60;
		}
		if(state.is(NorthstarFluids.LIQUID_HYDROGEN.get()) || state.is(NorthstarFluids.LIQUID_HYDROGEN.getSource().getSource())) {
			return -259;
		}
		if(state.is(NorthstarFluids.SULFURIC_ACID.get()) || state.is(NorthstarFluids.SULFURIC_ACID.getSource().getSource())) {
			return -200;
		}
		return 0;
	}
	public static boolean hasInsulation(LivingEntity entity) {
		return (entity.getItemBySlot(EquipmentSlot.HEAD).is(NorthstarTags.NorthstarItemTags.INSULATING.tag) &&
				entity.getItemBySlot(EquipmentSlot.CHEST).is(NorthstarTags.NorthstarItemTags.INSULATING.tag) &&
				entity.getItemBySlot(EquipmentSlot.LEGS).is(NorthstarTags.NorthstarItemTags.INSULATING.tag) && 
				entity.getItemBySlot(EquipmentSlot.FEET).is(NorthstarTags.NorthstarItemTags.INSULATING.tag)) 
				|| NorthstarTags.NorthstarEntityTags.CAN_SURVIVE_COLD.matches(entity);
		
	}
	public static boolean hasHeatProtection(LivingEntity entity) {
		return (entity.getItemBySlot(EquipmentSlot.HEAD).is(NorthstarTags.NorthstarItemTags.HEAT_RESISTANT.tag) &&
				entity.getItemBySlot(EquipmentSlot.CHEST).is(NorthstarTags.NorthstarItemTags.HEAT_RESISTANT.tag) &&
				entity.getItemBySlot(EquipmentSlot.LEGS).is(NorthstarTags.NorthstarItemTags.HEAT_RESISTANT.tag) && 
				entity.getItemBySlot(EquipmentSlot.FEET).is(NorthstarTags.NorthstarItemTags.HEAT_RESISTANT.tag));
		
	}
	
	
	

	public static void register() {
		System.out.println("Calculating temperatures for " + Northstar.MOD_ID);
	}
	
	
    public static double getHeatRating(ResourceKey<Level> level) {
    	// I love spaghetti (2)
    	if(level == NorthstarDimensions.MOON_DIM_KEY) {return 0;}
    	if(level == NorthstarDimensions.MARS_DIM_KEY) {return 0.05;}
    	if(level == NorthstarDimensions.MERCURY_DIM_KEY) {return 0;}
    	if(level == NorthstarDimensions.VENUS_DIM_KEY) {return 5;}
    	if(level == Level.OVERWORLD) {return 0.4;}
		return 1;
    }
    public static double getHeatConstant(ResourceKey<Level> level) {
    	// I love spaghetti (2)
    	if(level == NorthstarDimensions.MOON_DIM_KEY) {return 0;}
    	if(level == NorthstarDimensions.MARS_DIM_KEY) {return 50;}
    	if(level == NorthstarDimensions.MERCURY_DIM_KEY) {return 0;}
    	if(level == NorthstarDimensions.VENUS_DIM_KEY) {return 1000;}
    	if(level == Level.OVERWORLD) {return 100;}
		return 1;
    }
}
