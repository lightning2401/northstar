package com.lightning.northstar.mixin.blockstuff;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.lightning.northstar.NorthstarTags;
import com.lightning.northstar.fluids.NorthstarFluids;
import com.lightning.northstar.particle.ColdAirParticleData;
import com.lightning.northstar.world.TemperatureStuff;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

@Mixin(IceBlock.class)
public class IceBlockMixin {

    @Inject(method = "randomTick", at = @At("TAIL"), cancellable = true)
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom, CallbackInfo info) {
    	//just in case other mods use the iceblock class for some reason
    	if(!pState.is(Blocks.ICE)){
    		return;
    	}
    	
    	int highestTemp = checkTemp(pState, pLevel, pPos, pRandom);
    	if(pRandom.nextFloat() > 0.4) {
    		if(100 < highestTemp){
    			this.evaporate2(pState, pLevel, pPos);
    		}
    		else if(32 < highestTemp){
    			this.melt2(pState, pLevel, pPos);
    	    }
    	}
    	else if(32 < highestTemp){
    		coldAirParticles(pState, pLevel, pPos, pRandom);
    	}


    }
    

    public int checkTemp(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
    	int highestTemp = TemperatureStuff.getTemp(pPos, pLevel);
    	for(Direction dirs : Direction.values()) {
    		if(TemperatureStuff.getTemp(pPos.mutable().move(dirs), pLevel) > highestTemp && pLevel.getBlockState(pPos.mutable().move(dirs)).isAir()) {
    			highestTemp = TemperatureStuff.getTemp(pPos.mutable().move(dirs), pLevel);
    		}
    	}
    	return highestTemp;
    	
    }

 //   @Inject(method = "animateTick", at = @At("HEAD"), cancellable = true)
    public void coldAirParticles(BlockState state, Level level, BlockPos pos, RandomSource random) {
	      int x = pos.getX();
	      int y = pos.getY();
	      int z = pos.getZ();
	      for(Direction dir : Direction.values()) {
	    	  if(level.getBlockState(pos.relative(dir)).isAir() && random.nextInt(20) == 0) {
	    	      double d0 = (double)x + (dir.getStepX() / 2) + random.nextDouble();
	    	      double d1 = (double)y + 0.7D;
	    	      double d2 = (double)z + (dir.getStepZ() / 2) + random.nextDouble();
	    	      level.addParticle(new ColdAirParticleData(), d0, d1, d2, 0.0D, 0.0D, 0.0D);
	    	  }
	      }

	}
    
    public void melt2(BlockState pState, Level pLevel, BlockPos pPos) {
	    pLevel.setBlockAndUpdate(pPos, Fluids.WATER.getFluidType().getBlockForFluidState(pLevel, pPos, Fluids.WATER.defaultFluidState()));
	    pLevel.neighborChanged(pPos, Fluids.WATER.getFluidType().getBlockForFluidState(pLevel, pPos, Fluids.WATER.defaultFluidState()).getBlock(), pPos);
	}
	protected void evaporate2(BlockState pState, Level pLevel, BlockPos pPos) {
	    pLevel.setBlockAndUpdate(pPos, Blocks.AIR.defaultBlockState());
	    pLevel.neighborChanged(pPos, Blocks.AIR, pPos);
        int i = pPos.getX();
        int j = pPos.getY();
        int k = pPos.getZ();
        pLevel.playSound(null, pPos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (pLevel.random.nextFloat() - pLevel.random.nextFloat()) * 0.8F);
        for(int l = 0; l < 8; ++l) {
           pLevel.addParticle(ParticleTypes.LARGE_SMOKE, (double)i + Math.random(), (double)j + Math.random(), (double)k + Math.random(), 0.0D, 0.0D, 0.0D);
        }
	}
}
