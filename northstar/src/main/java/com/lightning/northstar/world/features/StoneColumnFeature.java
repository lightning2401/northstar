package com.lightning.northstar.world.features;

import java.util.Optional;

import javax.annotation.Nullable;

import com.lightning.northstar.world.features.configuration.StoneColumnConfiguration;
import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Column;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.DripstoneUtils;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.phys.Vec3;

public class StoneColumnFeature extends Feature<StoneColumnConfiguration> {
	   public StoneColumnFeature(Codec<StoneColumnConfiguration> pCodec) {
		      super(pCodec);
		   }

		   /**
		    * Places the given feature at the given location.
		    * During world generation, features are provided with a 3x3 region of chunks, centered on the chunk being generated,
		    * that they can safely generate into.
		    * @param pContext A context object with a reference to the level and the position the feature is being placed at
		    */
		   public boolean place(FeaturePlaceContext<StoneColumnConfiguration> pContext) {
		      WorldGenLevel worldgenlevel = pContext.level();
		      BlockPos blockpos = pContext.origin();
		      StoneColumnConfiguration largedripstoneconfiguration = pContext.config();
		      RandomSource randomsource = pContext.random();
		      
		      if (!isEmptyOrWater(worldgenlevel, blockpos)) {
		         return false;
		      } else {
		         Optional<Column> optional = Column.scan(worldgenlevel, blockpos, largedripstoneconfiguration.floorToCeilingSearchRange, DripstoneUtils::isEmptyOrWater, DripstoneUtils::isDripstoneBaseOrLava);
		         if (optional.isPresent() && optional.get() instanceof Column.Range) {
		            Column.Range column$range = (Column.Range)optional.get();
		            if (column$range.height() < 4) {
		               return false;
		            } else {
		               int i = (int)((float)column$range.height() * largedripstoneconfiguration.maxColumnRadiusToCaveHeightRatio);
		               int j = Mth.clamp(i, largedripstoneconfiguration.columnRadius.getMinValue(), largedripstoneconfiguration.columnRadius.getMaxValue());
		               int k = Mth.randomBetweenInclusive(randomsource, largedripstoneconfiguration.columnRadius.getMinValue(), j);
		               StoneColumnFeature.LargeDripstone largedripstonefeature$largedripstone = makeDripstone(blockpos.atY(column$range.ceiling() - 1), false, randomsource, k, largedripstoneconfiguration.stalactiteBluntness, largedripstoneconfiguration.heightScale);
		               StoneColumnFeature.LargeDripstone largedripstonefeature$largedripstone1 = makeDripstone(blockpos.atY(column$range.floor() + 1), true, randomsource, k, largedripstoneconfiguration.stalagmiteBluntness, largedripstoneconfiguration.heightScale);
		               StoneColumnFeature.WindOffsetter largedripstonefeature$windoffsetter;
		               if (largedripstonefeature$largedripstone.isSuitableForWind(largedripstoneconfiguration) && largedripstonefeature$largedripstone1.isSuitableForWind(largedripstoneconfiguration)) {
		                  largedripstonefeature$windoffsetter = new StoneColumnFeature.WindOffsetter(blockpos.getY(), randomsource, largedripstoneconfiguration.windSpeed);
		               } else {
		                  largedripstonefeature$windoffsetter = StoneColumnFeature.WindOffsetter.noWind();
		               }

		               boolean flag = largedripstonefeature$largedripstone.moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(worldgenlevel, largedripstonefeature$windoffsetter);
		               boolean flag1 = largedripstonefeature$largedripstone1.moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(worldgenlevel, largedripstonefeature$windoffsetter);
		               if (flag) {
		                  largedripstonefeature$largedripstone.placeBlocks(worldgenlevel, randomsource, largedripstonefeature$windoffsetter, pContext.config());
		               }

		               if (flag1) {
		                  largedripstonefeature$largedripstone1.placeBlocks(worldgenlevel, randomsource, largedripstonefeature$windoffsetter, pContext.config());
		               }

		               return true;
		            }
		         } else {
		            return false;
		         }
		      }
		   }

		   private static StoneColumnFeature.LargeDripstone makeDripstone(BlockPos pRoot, boolean pPointingUp, RandomSource pRandom, int pRadius, FloatProvider pBluntnessBase, FloatProvider pScaleBase) {
		      return new StoneColumnFeature.LargeDripstone(pRoot, pPointingUp, pRadius, (double)pBluntnessBase.sample(pRandom), (double)pScaleBase.sample(pRandom));
		   }

		   private void placeDebugMarkers(WorldGenLevel pLevel, BlockPos pPos, Column.Range pRange, StoneColumnFeature.WindOffsetter pWindOffsetter) {
		      pLevel.setBlock(pWindOffsetter.offset(pPos.atY(pRange.ceiling() - 1)), Blocks.DIAMOND_BLOCK.defaultBlockState(), 2);
		      pLevel.setBlock(pWindOffsetter.offset(pPos.atY(pRange.floor() + 1)), Blocks.GOLD_BLOCK.defaultBlockState(), 2);

		      for(BlockPos.MutableBlockPos blockpos$mutableblockpos = pPos.atY(pRange.floor() + 2).mutable(); blockpos$mutableblockpos.getY() < pRange.ceiling() - 1; blockpos$mutableblockpos.move(Direction.UP)) {
		         BlockPos blockpos = pWindOffsetter.offset(blockpos$mutableblockpos);
		         if (isEmptyOrWater(pLevel, blockpos) || pLevel.getBlockState(blockpos).is(Blocks.DRIPSTONE_BLOCK)) {
		            pLevel.setBlock(blockpos, Blocks.CREEPER_HEAD.defaultBlockState(), 2);
		         }
		      }

		   }

		   static final class LargeDripstone {
		      private BlockPos root;
		      private final boolean pointingUp;
		      private int radius;
		      private final double bluntness;
		      private final double scale;

		      LargeDripstone(BlockPos pRoot, boolean pPointingUp, int pRadius, double pBluntness, double pScale) {
		         this.root = pRoot;
		         this.pointingUp = pPointingUp;
		         this.radius = pRadius;
		         this.bluntness = pBluntness;
		         this.scale = pScale;
		      }

		      private int getHeight() {
		         return this.getHeightAtRadius(0.0F);
		      }

		      private int getMinY() {
		         return this.pointingUp ? this.root.getY() : this.root.getY() - this.getHeight();
		      }

		      private int getMaxY() {
		         return !this.pointingUp ? this.root.getY() : this.root.getY() + this.getHeight();
		      }

		      boolean moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(WorldGenLevel pLevel, StoneColumnFeature.WindOffsetter pWindOffsetter) {
		         while(this.radius > 1) {
		            BlockPos.MutableBlockPos blockpos$mutableblockpos = this.root.mutable();
		            int i = Math.min(10, this.getHeight());

		            for(int j = 0; j < i; ++j) {
		               if (pLevel.getBlockState(blockpos$mutableblockpos).is(Blocks.LAVA)) {
		                  return false;
		               }

		               if (isCircleMostlyEmbeddedInStone(pLevel, pWindOffsetter.offset(blockpos$mutableblockpos), this.radius)) {
		                  this.root = blockpos$mutableblockpos;
		                  return true;
		               }

		               blockpos$mutableblockpos.move(this.pointingUp ? Direction.DOWN : Direction.UP);
		            }

		            this.radius /= 2;
		         }

		         return false;
		      }

		      private int getHeightAtRadius(float pRadius) {
		         return (int)getDripstoneHeight((double)pRadius, (double)this.radius, this.scale, this.bluntness);
		      }

		      void placeBlocks(WorldGenLevel pLevel, RandomSource pRandom, StoneColumnFeature.WindOffsetter pWindOffsetter, StoneColumnConfiguration config) {
		         for(int i = -this.radius; i <= this.radius; ++i) {
		            for(int j = -this.radius; j <= this.radius; ++j) {
		               float f = Mth.sqrt((float)(i * i + j * j));
		               if (!(f > (float)this.radius)) {
		                  int k = this.getHeightAtRadius(f);
		                  if (k > 0) {
		                     if ((double)pRandom.nextFloat() < 0.2D) {
		                        k = (int)((float)k * Mth.randomBetween(pRandom, 0.8F, 1.0F));
		                     }

		                     BlockPos.MutableBlockPos blockpos$mutableblockpos = this.root.offset(i, 0, j).mutable();
		                     boolean flag = false;
		                     int l = this.pointingUp ? pLevel.getHeight(Heightmap.Types.WORLD_SURFACE_WG, blockpos$mutableblockpos.getX(), blockpos$mutableblockpos.getZ()) : Integer.MAX_VALUE;

		                     for(int i1 = 0; i1 < k && blockpos$mutableblockpos.getY() < l; ++i1) {
		                        BlockPos blockpos = pWindOffsetter.offset(blockpos$mutableblockpos);
		                        if (isEmptyOrWaterOrLava(pLevel, blockpos)) {
		                           flag = true;

		                           pLevel.setBlock(blockpos, config.stone_provider.getState(pRandom, blockpos$mutableblockpos), 2);
		                        } else if (flag) {
		                           break;
		                        }

		                        blockpos$mutableblockpos.move(this.pointingUp ? Direction.UP : Direction.DOWN);
		                     }
		                  }
		               }
		            }
		         }

		      }

		      boolean isSuitableForWind(StoneColumnConfiguration pConfig) {
		         return this.radius >= pConfig.minRadiusForWind && this.bluntness >= (double)pConfig.minBluntnessForWind;
		      }
		   }

		   static final class WindOffsetter {
		      private final int originY;
		      @Nullable
		      private final Vec3 windSpeed;

		      WindOffsetter(int pOriginY, RandomSource pRandom, FloatProvider pMagnitude) {
		         this.originY = pOriginY;
		         float f = pMagnitude.sample(pRandom);
		         float f1 = Mth.randomBetween(pRandom, 0.0F, (float)Math.PI);
		         this.windSpeed = new Vec3((double)(Mth.cos(f1) * f), 0.0D, (double)(Mth.sin(f1) * f));
		      }

		      private WindOffsetter() {
		         this.originY = 0;
		         this.windSpeed = null;
		      }

		      static StoneColumnFeature.WindOffsetter noWind() {
		         return new StoneColumnFeature.WindOffsetter();
		      }

		      BlockPos offset(BlockPos pPos) {
		         if (this.windSpeed == null) {
		            return pPos;
		         } else {
		            int i = this.originY - pPos.getY();
		            Vec3 vec3 = this.windSpeed.scale((double)i);
		            return pPos.offset(vec3.x, 0.0D, vec3.z);
		         }
		      }
		   }
		   protected static boolean isEmptyOrWater(LevelAccessor pLevel, BlockPos pPos) {
			      return pLevel.isStateAtPosition(pPos, StoneColumnFeature::isEmptyOrWater);
			   }
		   public static boolean isEmptyOrWater(BlockState p_159665_) {
			      return p_159665_.isAir() || p_159665_.is(Blocks.WATER);
			   }
		   protected static boolean isCircleMostlyEmbeddedInStone(WorldGenLevel pLevel, BlockPos pPos, int pRadius) {
			      if (isEmptyOrWaterOrLava(pLevel, pPos)) {
			         return false;
			      } else {
			         float f = 6.0F;
			         float f1 = 6.0F / (float)pRadius;

			         for(float f2 = 0.0F; f2 < ((float)Math.PI * 2F); f2 += f1) {
			            int i = (int)(Mth.cos(f2) * (float)pRadius);
			            int j = (int)(Mth.sin(f2) * (float)pRadius);
			            if (isEmptyOrWaterOrLava(pLevel, pPos.offset(i, 0, j))) {
			               return false;
			            }
			         }

			         return true;
			      }
			   }
		   protected static boolean isEmptyOrWaterOrLava(LevelAccessor pLevel, BlockPos pPos) {
			      return pLevel.isStateAtPosition(pPos, StoneColumnFeature::isEmptyOrWaterOrLava);
			   }
		   public static boolean isEmptyOrWaterOrLava(BlockState p_159667_) {
			      return p_159667_.isAir() || p_159667_.is(Blocks.WATER) || p_159667_.is(Blocks.LAVA);
			   }
		   protected static double getDripstoneHeight(double pRadius, double pMaxRadius, double pScale, double pMinRadius) {
			      if (pRadius < pMinRadius) {
			         pRadius = pMinRadius;
			      }

			      double d0 = 0.384D;
			      double d1 = pRadius / pMaxRadius * 0.384D;
			      double d2 = 0.75D * Math.pow(d1, 1.3333333333333333D);
			      double d3 = Math.pow(d1, 0.6666666666666666D);
			      double d4 = 0.3333333333333333D * Math.log(d1);
			      double d5 = pScale * (d2 - d3 - d4);
			      d5 = Math.max(d5, 0.0D);
			      return d5 / 0.384D * pMaxRadius;
			   }
}
