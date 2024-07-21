package com.lightning.northstar.world.features;

import com.lightning.northstar.world.features.configuration.CraterConfig;
import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class CraterFeature extends Feature<CraterConfig> {

	public CraterFeature(Codec<CraterConfig> pCodec) {
		super(pCodec);
	}

	@Override
	public boolean place(FeaturePlaceContext<CraterConfig> pContext) {
		  CraterConfig config = pContext.config();
	      BlockPos blockpos = pContext.origin();
	      WorldGenLevel worldgenlevel = pContext.level();
	      RandomSource randomsource = pContext.random();
	      boolean flag = false;
	      int i = blockpos.getY();
	      int j = i + config.half_height.sample(randomsource);
	      if(j == 0)
	    	  return false;
	      int k = i - config.half_height.sample(randomsource) - 1;
	      int l = config.radius.sample(randomsource);
	      BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
	      int pp = 0;
	      for(int l_old = l;l > l_old / j; l = (int) (l / 1.5)) {
	    	  if (pp < config.depth.sample(randomsource)) {
	    		  pp++; 
	    		  blockpos = blockpos.below();
	    	  }
	      	for(BlockPos blockpos1 : BlockPos.betweenClosed(blockpos.offset(-l, 0, -l), blockpos.offset(l, 0, l))) {
	      		int i1 = blockpos1.getX() - blockpos.getX();
	      		int j1 = blockpos1.getZ() - blockpos.getZ();
	      		if (i1 * i1 + j1 * j1 <= l * l - 0.1) {
	      			flag |= this.placeColumn(config, worldgenlevel, randomsource, j, k, blockpos$mutableblockpos.set(blockpos1));
	      			this.placeColumnBelow(config, worldgenlevel, randomsource, j, k, blockpos$mutableblockpos.set(blockpos1.below()));
	      			this.clearAir(config, worldgenlevel, randomsource, j, k, blockpos$mutableblockpos.set(blockpos1));
	      		}
	      	}
	      }

	      return flag;
	}

	 protected boolean placeColumn(CraterConfig pConfig, WorldGenLevel pLevel, RandomSource pRandom, int pMaxY, int pMinY, BlockPos.MutableBlockPos pPos) {
	      boolean flag = false;
	      BlockState blockstate = pConfig.air_provider.getState(pRandom, pPos);

	      if(pLevel.getBlockState(pPos).is(pConfig.canDelete)) {
		      pLevel.setBlock(pPos, blockstate, 2);
	      }
	      this.markAboveForPostProcessing(pLevel, pPos);
	      flag = true;

	      return flag;
	   }
	 protected boolean clearAir(CraterConfig pConfig, WorldGenLevel pLevel, RandomSource pRandom, int pMaxY, int pMinY, BlockPos.MutableBlockPos pPos) {
	      boolean flag = false;
	      BlockState blockstate = pConfig.air_provider.getState(pRandom, pPos);
	      for(int i = 0; i < 24; i++) {
	      if(pLevel.getBlockState(pPos.atY(pPos.getY() + i)).is(pConfig.canDelete)) {
	    		  pLevel.setBlock(pPos.atY(pPos.getY() + i), blockstate, 2);
	      }
	      flag = true;}


	      return flag;
	   }
	 
	 
	 protected boolean placeColumnBelow(CraterConfig pConfig, WorldGenLevel pLevel, RandomSource pRandom, int pMaxY, int pMinY, BlockPos.MutableBlockPos pPos) {
	      boolean flag = false;
	      BlockState blockstate = pConfig.block_provider.getState(pRandom, pPos);
	      if (pLevel.getBlockState(pPos) == Blocks.AIR.defaultBlockState()) {
	    	  return false;
	      }
	      if(pLevel.getBlockState(pPos).is(pConfig.canDelete)) {
		      pLevel.setBlock(pPos, blockstate, 2);
	      }
	      this.markAboveForPostProcessing(pLevel, pPos);
	      flag = true;

	      return flag;
	   }
}
