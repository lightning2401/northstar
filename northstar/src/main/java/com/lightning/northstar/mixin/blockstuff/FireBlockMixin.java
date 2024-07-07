package com.lightning.northstar.mixin.blockstuff;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.lightning.northstar.world.OxygenStuff;
import com.lightning.northstar.world.TemperatureStuff;
import com.lightning.northstar.world.dimension.NorthstarPlanets;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(FireBlock.class)
public class FireBlockMixin {

    @Inject(method = "canSurvive", at = @At("HEAD"), cancellable = true)
    public void canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos, CallbackInfoReturnable<Boolean> info) {
    	try {
	    	if (TemperatureStuff.getTemp(pPos, (Level) pLevel) < -100 || (!OxygenStuff.hasOxygen(pPos, ((Level)pLevel).dimension()) && !NorthstarPlanets.getPlanetOxy(((Level)pLevel).dimension()))) {
	    		info.setReturnValue(false);
	    	}
		} catch (Exception e) {
			//oops
		}
    	
//		System.out.println("TEMP: " + TemperatureStuff.getTemp(pPos, (Level) pLevel));
//		System.out.println("OXY: " + OxygenStuff.hasOxygen(pPos,((Level)pLevel).dimension()));
//		System.out.println("PLANET OXY: " + NorthstarPlanets.getPlanetOxy(((Level)pLevel).dimension()));		   
    }


}
