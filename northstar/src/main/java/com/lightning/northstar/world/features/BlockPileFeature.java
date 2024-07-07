package com.lightning.northstar.world.features;

import com.lightning.northstar.world.features.configuration.BlockPileConfig;
import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class BlockPileFeature extends Feature<BlockPileConfig> {

	public BlockPileFeature(Codec<BlockPileConfig> pCodec) {
		super(pCodec);
	}

	@Override
	public boolean place(FeaturePlaceContext<BlockPileConfig> pContext) {
		  BlockPileConfig config = pContext.config();
	      BlockPos blockpos = pContext.origin();
	      WorldGenLevel worldgenlevel = pContext.level();
	      RandomSource randomsource = pContext.random();
	      boolean flag = false;
	      int i = blockpos.getY();
	      int j = i + config.half_height.sample(randomsource);
	      int k = i - config.half_height.sample(randomsource) - 1;
	      int radius = config.radius.sample(randomsource);
	      BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
	      int pp = 0;
	      int iteration = 0;
	      for(int old_radius = radius;radius > old_radius / j; radius = (int) (radius / 1.5)) {
	    	  if (pp < config.depth.sample(randomsource)) {
	    		  pp++; 
	    		  blockpos = blockpos.above();
	    		  iteration++;
	    	  }
	      	for(BlockPos blockpos1 : BlockPos.betweenClosed(blockpos.offset(-radius, 0, -radius), blockpos.offset(radius, 0, radius))) {
	      		int bruhX = blockpos1.getX() - blockpos.getX();
	      		int bruhZ = blockpos1.getZ() - blockpos.getZ();
	      		if (bruhX * bruhX + bruhZ * bruhZ <= radius * radius - 0.1) {
	      			if(iteration <= 3)
	      			flag |= this.placeColumn(config, worldgenlevel, randomsource, j, k, blockpos$mutableblockpos.set(blockpos1), iteration, radius, pContext.origin().offset(0, 0, 0));
	      		}
	      	}
	      }
	      return flag;
	}

	protected boolean placeColumn(BlockPileConfig pConfig, WorldGenLevel pLevel, RandomSource pRandom, int pMaxY, int pMinY, BlockPos.MutableBlockPos pPos, int iteration, float radius, BlockPos origin) {
	      boolean flag = false;
	      BlockState blockstate = pConfig.block_provider.getState(pRandom, pPos);
	      float dist = (float) Math.abs(Math.sqrt(Math.pow((pPos.getX() - origin.getX()), 2) + Math.pow((pPos.getZ() - origin.getZ()), 2)));
//	      System.out.println("pPos: " + pPos);
//	      System.out.println("origin: " + origin);
//	      System.out.println("dist: " + dist);
	      if(radius / 2 == 0){
		    System.out.println("What the scallop???");
	    	return false;  
	      }
	      if(iteration == 1) {
	      	  if (pRandom.nextIntBetweenInclusive(0, Math.round(Math.abs(((int)dist - 1) / (radius / 2)))) <= 0) {
			      pLevel.setBlock(pLevel.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pPos).below(), blockstate, 2);
			      pLevel.setBlock(pLevel.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pPos).below().below(), blockstate, 2);
	    	  }
	      }else if (pRandom.nextIntBetweenInclusive(0, Math.round(Math.abs(((int)dist - 1) / (radius / 2)))) <= 0) {
	    	  pLevel.setBlock(pLevel.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pPos), blockstate, 2);
	      }
	      this.markAboveForPostProcessing(pLevel, pPos);
	      flag = true;

	      return flag;
	   }
	 protected boolean clearAir(BlockPileConfig pConfig, WorldGenLevel pLevel, RandomSource pRandom, int pMaxY, int pMinY, BlockPos.MutableBlockPos pPos) {
	      boolean flag = false;
	      BlockState blockstate = pConfig.air_provider.getState(pRandom, pPos);
	      for(int i = 0; i < 12; i++) {
	      pLevel.setBlock(pPos.atY(pPos.getY() + i), blockstate, 2);
	      flag = true;}


	      return flag;
	   }

}
