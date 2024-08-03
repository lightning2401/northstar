package com.lightning.northstar.world.features;

import java.util.Optional;
import java.util.OptionalInt;

import com.lightning.northstar.world.features.configuration.StoneClusterConfiguration;
import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ClampedNormalFloat;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Column;
import net.minecraft.world.level.levelgen.feature.DripstoneUtils;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class StoneClusterFeature extends Feature<StoneClusterConfiguration> {

	public StoneClusterFeature(Codec<StoneClusterConfiguration> codec) {
		super(codec);
	}

	@Override
	public boolean place(FeaturePlaceContext<StoneClusterConfiguration> pContext) {
	      WorldGenLevel worldgenlevel = pContext.level();
	      BlockPos blockpos = pContext.origin();
	      StoneClusterConfiguration config = pContext.config();
	      RandomSource randomsource = pContext.random();
//	      if (!DripstoneUtils.isEmptyOrWater(worldgenlevel, blockpos)) {
//	         return false;
//	      } else 
	      
	         int i = config.height.sample(randomsource);
	         float f = config.wetness.sample(randomsource);
	         float f1 = config.density.sample(randomsource);
	         int j = config.radius.sample(randomsource);
	         int k = config.radius.sample(randomsource);
	         float lavaness = config.lavaness.sample(randomsource);
	         float thirdthingness = config.thirdthingness.sample(randomsource);

	         for(int l = -j; l <= j; ++l) {
	            for(int i1 = -k; i1 <= k; ++i1) {
	               double d0 = this.getChanceOfStalagmiteOrStalactite(j, k, l, i1, config);
	               BlockPos blockpos1 = blockpos.offset(l, 0, i1);
	               this.placeColumn(worldgenlevel, randomsource, blockpos1, l, i1, f, lavaness, thirdthingness, d0, i, f1, config);
	            }
	         }

	         return true;
	      
	}
	
	 private void placeColumn(WorldGenLevel pLevel, RandomSource pRandom, BlockPos pPos, int pX, int pZ, float pWetness, float lavaness, float thirdthingness, double pChance, int pHeight, float pDensity, StoneClusterConfiguration config) {
	      Optional<Column> optional = Column.scan(pLevel, pPos, config.floorToCeilingSearchRange, block ->{return !block.isSolidRender(pLevel, pPos);},  blockdeny ->{return blockdeny.isSolidRender(pLevel, pPos);});
	      if (optional.isPresent()) {
	         OptionalInt optionalint = optional.get().getCeiling();
	         OptionalInt optionalint1 = optional.get().getFloor();
	         if (optionalint.isPresent() || optionalint1.isPresent()) {
	            boolean water_flag = pRandom.nextFloat() < pWetness;
	            boolean lava_flag = pRandom.nextFloat() < lavaness;
	            boolean thirdthing_flag = pRandom.nextFloat() < thirdthingness;
	            Column column;
	            if (water_flag && optionalint1.isPresent()) {
	            	// && this.canPlacePool(pLevel, pPos.atY(optionalint1.getAsInt())
	               int i = optionalint1.getAsInt();
	               column = optional.get().withFloor(OptionalInt.of(i - 1));
	               pLevel.setBlock(pPos.atY(i), Blocks.WATER.defaultBlockState(), 2);
	               this.markAboveForPostProcessing(pLevel, pPos.atY(i).below());
	            } else {
	               column = optional.get();
	            } 
	            if (lava_flag && optionalint1.isPresent() && optionalint.isPresent()) {
	            	// && this.canPlacePool(pLevel, pPos.atY(optionalint1.getAsInt())
	               int i = optionalint1.getAsInt();
	               column = optional.get().withFloor(OptionalInt.of(i - 1));
	               pLevel.setBlock(pPos.atY(i), Blocks.LAVA.defaultBlockState(), 2);
	               this.markAboveForPostProcessing(pLevel, pPos.atY(i).below());
	            } 
	            if (thirdthing_flag && optionalint1.isPresent()) {
	            	// && this.canPlacePool(pLevel, pPos.atY(optionalint1.getAsInt())
	               int i = optionalint1.getAsInt();
	               column = optional.get().withFloor(OptionalInt.of(i - 1));
	               pLevel.setBlock(pPos.atY(i), config.fluid_provider.createLegacyBlock(), 2);
	               this.markAboveForPostProcessing(pLevel, pPos.atY(i).below());
	            } 

	            OptionalInt optionalint2 = column.getFloor();
	            boolean flag1 = pRandom.nextDouble() < pChance;
	            int j;
	            if (optionalint.isPresent() && flag1 && !this.isLava(pLevel, pPos.atY(optionalint.getAsInt()))) {
	               int k = config.dripstoneBlockLayerThickness.sample(pRandom);
	               this.replaceBlocksWithStoneBlocks(pLevel, pPos.atY(optionalint.getAsInt()), k, Direction.UP);
	               int l;
	               if (optionalint2.isPresent()) {
	                  l = Math.min(pHeight, optionalint.getAsInt() - optionalint2.getAsInt());
	               } else {
	                  l = pHeight;
	               }

	               j = this.getStoneHeight(pRandom, pX, pZ, pDensity, l, config);
	            } else {
	               j = 0;
	            }

	            
	            // not working :(((((
	            boolean flag2 = pRandom.nextDouble() < pChance;
	            int i3;
	            if (optionalint2.isPresent() && flag2 && !this.isLava(pLevel, pPos.atY(optionalint2.getAsInt()))) {
	               int i1 = config.dripstoneBlockLayerThickness.sample(pRandom);
	               this.replaceBlocksWithStoneBlocks(pLevel, pPos.atY(optionalint2.getAsInt()), i1, Direction.DOWN);
	               if (optionalint.isPresent()) {
	                  i3 = Math.max(0, j + Mth.randomBetweenInclusive(pRandom, -config.maxStalagmiteStalactiteHeightDiff, config.maxStalagmiteStalactiteHeightDiff));
	               } else {
	                  i3 = this.getStoneHeight(pRandom, pX, pZ, pDensity, pHeight, config);
	               }
	            } else {
	               i3 = 0;
	            }

	            int j1;
	            int j3;
	            if (optionalint.isPresent() && optionalint2.isPresent() && optionalint.getAsInt() - j <= optionalint2.getAsInt() + i3) {
	               int k1 = optionalint2.getAsInt();
	               int l1 = optionalint.getAsInt();
	               int i2 = Math.max(l1 - j, k1 + 1);
	               int j2 = Math.min(k1 + i3, l1 - 1);
	               int k2 = Mth.randomBetweenInclusive(pRandom, i2, j2 + 1);
	               int l2 = k2 - 1;
	               j3 = l1 - k2;
	               j1 = l2 - k1;
	            } else {
	               j3 = j;
	               j1 = i3;
	            }

	            boolean flag3 = pRandom.nextBoolean() && j3 > 0 && j1 > 0 && column.getHeight().isPresent() && j3 + j1 == column.getHeight().getAsInt();
	            if (optionalint.isPresent()) {
	            	growPointyStone(pLevel, pPos.atY(optionalint.getAsInt() - 1), Direction.DOWN, j3, flag3, config);
	            }

	            if (optionalint2.isPresent()) {
	            	growPointyStone(pLevel, pPos.atY(optionalint2.getAsInt() + 1), Direction.UP, j1, flag3, config);
	            }

	         }
	      }
	   }
	 
	 
	   private void replaceBlocksWithStoneBlocks(WorldGenLevel pLevel, BlockPos pPos, int pThickness, Direction pDirection) {
		      BlockPos.MutableBlockPos blockpos$mutableblockpos = pPos.mutable();

		      for(int i = 0; i < pThickness; ++i) {
		         blockpos$mutableblockpos.move(pDirection);}
		      }

//		   }
	   
	   
	   private double getChanceOfStalagmiteOrStalactite(int pXRadius, int pZRadius, int pX, int pZ, StoneClusterConfiguration config) {
		      int i = pXRadius - Math.abs(pX);
		      int j = pZRadius - Math.abs(pZ);
		      int k = Math.min(i, j);
		      return (double)Mth.clampedMap((float)k, 0.0F, (float)config.maxDistanceFromEdgeAffectingChanceOfDripstoneColumn, config.chanceOfDripstoneColumnAtMaxDistanceFromCenter, 1.0F);
		   }
	   private int getStoneHeight(RandomSource pRandom, int pX, int pZ, float pChance, int pHeight, StoneClusterConfiguration config) {
		      if (pRandom.nextFloat() > pChance) {
		         return 0;
		      } else {
		         int i = Math.abs(pX) + Math.abs(pZ);
		         float f = (float)Mth.clampedMap((double)i, 0.0D, (double)config.maxDistanceFromCenterAffectingHeightBias, (double)pHeight / 2.0D, 0.0D);
		         return (int)randomBetweenBiased(pRandom, 0.0F, (float)pHeight, f, (float)config.heightDeviation);
		      }
		   }
	   private static float randomBetweenBiased(RandomSource pRandom, float pMin, float pMax, float pMean, float pDeviation) {
		      return ClampedNormalFloat.sample(pRandom, pMean, pDeviation, pMin, pMax);
		   }
	   
	   private boolean isLava(LevelReader pLevel, BlockPos pPos) {
		      return pLevel.getBlockState(pPos).is(Blocks.LAVA);
		   }
	   
		protected BlockPos scan(Direction dir, BlockPos pos, WorldGenLevel level, int scanDist) {
			BlockPos.MutableBlockPos mutable = pos.mutable();
			if(level.getBlockState(pos).isAir() && level.getBlockState(mutable.relative(dir)).isSolidRender(level, pos)) 
			{return pos;}
			for(int i = 0; i < scanDist; i++) {
				mutable.move(Direction.UP);
				if(level.getBlockState(mutable).isAir() && level.getBlockState(mutable.relative(dir)).isSolidRender(level, pos)) {
					BlockPos newblockpos = new BlockPos(mutable.getX(),mutable.getY(),mutable.getZ());
					return newblockpos;
				}
			}
			mutable = pos.mutable();
			for(int i = 0; i < scanDist; i++) {
				mutable.move(dir);
				if(level.getBlockState(mutable.relative(dir)).isSolidRender(level, pos) && level.getBlockState(mutable.relative(dir.getOpposite())).isAir()) {
					BlockPos newblockpos = new BlockPos(mutable.getX(),mutable.getY(),mutable.getZ());
					return newblockpos;
				}
			}
			
			return pos;
		}
	   
	   protected static void growPointyStone(LevelAccessor pLevel, BlockPos pPos, Direction pDirection, int pHeight, boolean pMergeTip, StoneClusterConfiguration config) {
		         BlockPos.MutableBlockPos blockpos$mutableblockpos = pPos.mutable();
//		         buildBaseToTipColumn(pDirection, pPos, pHeight, pMergeTip, (p_190846_) -> {
//		               p_190846_ = (BlockState) (AllPaletteStoneTypes.CRIMSITE.baseBlock);
		         for(int l = 0; l == pHeight; ++l) {
		        	if(!pLevel.getBlockState(blockpos$mutableblockpos).isSolidRender(pLevel, blockpos$mutableblockpos))
		            {pLevel.setBlock(blockpos$mutableblockpos, config.stone_provider.getState(RandomSource.create(), blockpos$mutableblockpos), 2);}
		            blockpos$mutableblockpos.move(pDirection);}
//		         }, config);     
		   }
	   

}
