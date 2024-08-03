package com.lightning.northstar.mixin.blockstuff;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.lightning.northstar.world.TemperatureStuff;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

@Mixin(FluidState.class)
public class FluidStateMixin {
	
    @Inject(method = "tick", at = @At("TAIL"))
	public void tick$fluid(Level pLevel, BlockPos pPos, CallbackInfo info) {
 //   	System.out.println("loadBuffer: " + TemperatureStuff.loadBuffer);
 //   	System.out.println("TemperatureStuff.loadBuffer <= 70: " +  String.valueOf( TemperatureStuff.loadBuffer <= 70));
		if(pLevel.isClientSide || TemperatureStuff.loadBuffer <= 70)
			return;
		FluidState state = pLevel.getFluidState(pPos);
		BlockState block = pLevel.getBlockState(pPos);
		int temp = TemperatureStuff.getTemp(pPos, pLevel);
		if(temp > TemperatureStuff.getBoilingPoint(state)) 
		{if(block.hasProperty(BlockStateProperties.WATERLOGGED) && !state.isEmpty()){pLevel.setBlockAndUpdate(pPos, block.setValue(BlockStateProperties.WATERLOGGED, false)); removeFluid(pLevel, pPos, state);}
		else {pLevel.setBlock(pPos, Blocks.AIR.defaultBlockState(), 3); removeFluid(pLevel, pPos, state);}
		}
		if(temp < TemperatureStuff.getFreezingPoint(state) && state.is(Fluids.WATER)) 
		{if(block.hasProperty(BlockStateProperties.WATERLOGGED)){{pLevel.setBlockAndUpdate(pPos, block.setValue(BlockStateProperties.WATERLOGGED, false)); removeFluid(pLevel, pPos, state);}}
		else {pLevel.setBlockAndUpdate(pPos, Blocks.ICE.defaultBlockState());}
		}
		if(TemperatureStuff.combustable(state)){
			if(TemperatureStuff.combustionTemp(state) <= temp) {
				combust(pLevel, pPos, state);
			}
			
			
		}
	}
    
    public void removeFluid(Level level, BlockPos pos, FluidState fluid) {
    	for(Direction dir : Direction.values()) {
    		BlockPos newpos = pos.mutable().move(dir);
    		if(level.getFluidState(newpos).is(fluid.getType()) && level.getBlockState(newpos).canBeReplaced(fluid.getType()) && !level.getBlockState(newpos).hasProperty(BlockStateProperties.WATERLOGGED)){level.setBlock(newpos, Blocks.AIR.defaultBlockState(), 3);}
    		else if(level.getBlockState(newpos).hasProperty(BlockStateProperties.WATERLOGGED) && fluid.is(Fluids.WATER)) 
    		{level.setBlockAndUpdate(newpos, level.getBlockState(newpos).setValue(BlockStateProperties.WATERLOGGED, false));}
    	}
    }
    //EXPLOSION!!!!!! YEAH!!!!!!! I LOVE DEATH AND DESTRUCTION!!!!!!!!!!!!!!!!!!!
    public void combust(Level level, BlockPos pos, FluidState fluid) {
    	level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
    	level.explode(null, (double) pos.getX(), (double)  pos.getY(), (double) pos.getZ(), 2.5F, true, Level.ExplosionInteraction.BLOCK);
    	
    }
}
