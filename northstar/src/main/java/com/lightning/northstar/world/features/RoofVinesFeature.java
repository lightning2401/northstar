package com.lightning.northstar.world.features;

import com.lightning.northstar.block.crops.VenusVinesBlock;
import com.lightning.northstar.world.features.configuration.RoofVinesConfig;
import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class RoofVinesFeature extends Feature<RoofVinesConfig> {

	   public RoofVinesFeature(Codec<RoofVinesConfig> pCodec) {
	      super(pCodec);
	   }

	   /**
	    * Places the given feature at the given location.
	    * During world generation, features are provided with a 3x3 region of chunks, centered on the chunk being generated,
	    * that they can safely generate into.
	    * @param pContext A context object with a reference to the level and the position the feature is being placed at
	    */
	   public boolean place(FeaturePlaceContext<RoofVinesConfig> pContext) {
	      WorldGenLevel worldgenlevel = pContext.level();
	      BlockPos blockpos = pContext.origin();
	      RandomSource randomsource = pContext.random();
	      if (!worldgenlevel.isEmptyBlock(blockpos)) {
	         return false;
	      } else {
	    	  RoofVinesConfig config = pContext.config();
	    	  this.placeVines(worldgenlevel, randomsource, blockpos, config);
	    	  return true;
	         
	      }
	   }

	   private void placeVines(LevelAccessor pLevel, RandomSource pRandom, BlockPos pPos, RoofVinesConfig config) {
	      BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

	      for(int i = 0; i < 100; ++i) {
	         blockpos$mutableblockpos.setWithOffset(pPos, pRandom.nextInt(8) - pRandom.nextInt(8), pRandom.nextInt(2) - pRandom.nextInt(7), pRandom.nextInt(8) - pRandom.nextInt(8));
	         if (pLevel.isEmptyBlock(blockpos$mutableblockpos)) {
	            BlockState blockstate = pLevel.getBlockState(blockpos$mutableblockpos.above());
	            if (blockstate.isSolidRender(pLevel, blockpos$mutableblockpos.above())) {
	               int j = Mth.nextInt(pRandom, 1, config.size.sample(pRandom));
	               if (pRandom.nextInt(6) == 0) {
	                  j *= 2;
	               }

	               if (pRandom.nextInt(5) == 0) {
	                  j = 1;
	               }
	               placeWeepingVinesColumn(pLevel, pRandom, blockpos$mutableblockpos, j, config.blockProvider, config.glowProvider);
	            }
	         }
	      }

	   }

	   public static void placeWeepingVinesColumn(LevelAccessor pLevel, RandomSource pRandom, BlockPos.MutableBlockPos pPos, int pHeight, BlockStateProvider normalState, BlockStateProvider glowState) {
	      for(int i = 0; i <= pHeight; ++i) {
	         if (pLevel.isEmptyBlock(pPos)) {
	        	 BlockState newstate = normalState.getState(pRandom, pPos);
	        	 if(pRandom.nextInt(4) == 0) {newstate = glowState.getState(pRandom, pPos);}
	        	 if (i == pHeight || !pLevel.isEmptyBlock(pPos.below())) {
	        		 pLevel.setBlock(pPos, newstate.setValue(VenusVinesBlock.IS_TIP, true), 2);
	        		 break;
	        	 }
	
	        	 pLevel.setBlock(pPos, newstate.setValue(VenusVinesBlock.IS_TIP, false), 2);
	        	 
	         }

	         pPos.move(Direction.DOWN);
	      }

	   }
	}