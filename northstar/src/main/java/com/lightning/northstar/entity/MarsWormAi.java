package com.lightning.northstar.entity;

import com.google.common.collect.ImmutableList;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.schedule.Activity;

public class MarsWormAi {

	
	public static void updateActivity(MarsWormEntity pWarden) {
		pWarden.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.EMERGE, Activity.DIG, Activity.ROAR, Activity.FIGHT, Activity.INVESTIGATE, Activity.SNIFF, Activity.IDLE));
	}
	
	public static void setDisturbanceLocation(MarsWormEntity worm, BlockPos pDisturbanceLocation) {
		if (worm.level.getWorldBorder().isWithinBounds(pDisturbanceLocation)) {
		    	worm.getBrain().setMemoryWithExpiry(MemoryModuleType.SNIFF_COOLDOWN, Unit.INSTANCE, 100L);
		    	worm.getBrain().setMemoryWithExpiry(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(pDisturbanceLocation), 100L);
		    	worm.getBrain().setMemoryWithExpiry(MemoryModuleType.DISTURBANCE_LOCATION, pDisturbanceLocation, 100L);
		    	worm.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
		}
	}
}
