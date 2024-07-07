package com.lightning.northstar.world.features;

import java.util.List;
import java.util.function.Predicate;

import com.google.common.collect.Lists;
import com.lightning.northstar.world.features.configuration.SphereConfig;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BuddingAmethystBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.GeodeBlockSettings;
import net.minecraft.world.level.levelgen.GeodeCrackSettings;
import net.minecraft.world.level.levelgen.GeodeLayerSettings;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.GeodeConfiguration;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.world.level.material.FluidState;

public class SphereFeature extends Feature<SphereConfig>  {
	   private static final Direction[] DIRECTIONS = Direction.values();

	   public SphereFeature(Codec<SphereConfig> p_159834_) {
	      super(p_159834_);
	   }

	   /**
	    * Places the given feature at the given location.
	    * During world generation, features are provided with a 3x3 region of chunks, centered on the chunk being generated,
	    * that they can safely generate into.
	    * @param pContext A context object with a reference to the level and the position the feature is being placed at
	    */
	   public boolean place(FeaturePlaceContext<SphereConfig> context) {
		   SphereConfig config = context.config();
	      RandomSource randomsource = context.random();
	      BlockPos blockpos = context.origin();
	      WorldGenLevel worldgenlevel = context.level();
	      int i = config.minGenOffset;
	      int j = config.maxGenOffset;
	      List<Pair<BlockPos, Integer>> list = Lists.newLinkedList();
	      int k = config.distributionPoints.sample(randomsource);
	      WorldgenRandom worldgenrandom = new WorldgenRandom(new LegacyRandomSource(worldgenlevel.getSeed()));
	      NormalNoise normalnoise = NormalNoise.create(worldgenrandom, -4, 1.0D);
	      List<BlockPos> list1 = Lists.newLinkedList();
	      double d0 = (double)k / (double)config.outerWallDistance.getMaxValue();
	      GeodeLayerSettings geodelayersettings = config.geodeLayerSettings;
	      GeodeBlockSettings geodeblocksettings = config.geodeBlockSettings;
	      GeodeCrackSettings geodecracksettings = config.geodeCrackSettings;
	      double d1 = 1.0D / Math.sqrt(geodelayersettings.filling);
	      double d2 = 1.0D / Math.sqrt(geodelayersettings.innerLayer + d0);
	      double d3 = 1.0D / Math.sqrt(geodelayersettings.middleLayer + d0);
	      double d4 = 1.0D / Math.sqrt(geodelayersettings.outerLayer + d0);
	      double d5 = 1.0D / Math.sqrt(geodecracksettings.baseCrackSize + randomsource.nextDouble() / 2.0D + (k > 3 ? d0 : 0.0D));
	      boolean flag = (double)randomsource.nextFloat() < geodecracksettings.generateCrackChance;
	      int l = 0;

	      for(int i1 = 0; i1 < k; ++i1) {
	         int j1 = config.outerWallDistance.sample(randomsource);
	         int k1 = config.outerWallDistance.sample(randomsource);
	         int l1 = config.outerWallDistance.sample(randomsource);
	         BlockPos blockpos1 = blockpos.offset(j1, k1, l1);
	         BlockState blockstate = worldgenlevel.getBlockState(blockpos1);
	         if (blockstate.isAir() || blockstate.is(BlockTags.GEODE_INVALID_BLOCKS)) {
	            ++l;
	            if (l > config.invalidBlocksThreshold) {
	               return false;
	            }
	         }

	         list.add(Pair.of(blockpos1, config.pointOffset.sample(randomsource)));
	      }

	      if (flag) {
	         int i2 = randomsource.nextInt(4);
	         int j2 = k * 2 + 1;
	         if (i2 == 0) {
	            list1.add(blockpos.offset(j2, 7, 0));
	            list1.add(blockpos.offset(j2, 5, 0));
	            list1.add(blockpos.offset(j2, 1, 0));
	         } else if (i2 == 1) {
	            list1.add(blockpos.offset(0, 7, j2));
	            list1.add(blockpos.offset(0, 5, j2));
	            list1.add(blockpos.offset(0, 1, j2));
	         } else if (i2 == 2) {
	            list1.add(blockpos.offset(j2, 7, j2));
	            list1.add(blockpos.offset(j2, 5, j2));
	            list1.add(blockpos.offset(j2, 1, j2));
	         } else {
	            list1.add(blockpos.offset(0, 7, 0));
	            list1.add(blockpos.offset(0, 5, 0));
	            list1.add(blockpos.offset(0, 1, 0));
	         }
	      }

	      List<BlockPos> list2 = Lists.newArrayList();
	      Predicate<BlockState> predicate = isReplaceable(config.geodeBlockSettings.cannotReplace);

	      for(BlockPos blockpos3 : BlockPos.betweenClosed(blockpos.offset(i, i, i), blockpos.offset(j, j, j))) {
	         double d8 = normalnoise.getValue((double)blockpos3.getX(), (double)blockpos3.getY(), (double)blockpos3.getZ()) * config.noiseMultiplier;
	         double d6 = 0.0D;
	         double d7 = 0.0D;

	         for(Pair<BlockPos, Integer> pair : list) {
	            d6 += Mth.fastInvSqrt(blockpos3.distSqr(pair.getFirst()) + (double)pair.getSecond().intValue()) + d8;
	         }

	         for(BlockPos blockpos6 : list1) {
	            d7 += Mth.fastInvSqrt(blockpos3.distSqr(blockpos6) + (double)geodecracksettings.crackPointOffset) + d8;
	         }

	         if (!(d6 < d4)) {
	            if (flag && d7 >= d5 && d6 < d1) {
	               this.safeSetBlock(worldgenlevel, blockpos3, Blocks.AIR.defaultBlockState(), predicate);

	               for(Direction direction1 : DIRECTIONS) {
	                  BlockPos blockpos2 = blockpos3.relative(direction1);
	                  FluidState fluidstate = worldgenlevel.getFluidState(blockpos2);
	                  if (!fluidstate.isEmpty()) {
	                     worldgenlevel.scheduleTick(blockpos2, fluidstate.getType(), 0);
	                  }
	               }
	            } else if (d6 >= d1) {
	               this.safeSetBlock(worldgenlevel, blockpos3, geodeblocksettings.fillingProvider.getState(randomsource, blockpos3), predicate);
	            } else if (d6 >= d2) {
	               boolean flag1 = (double)randomsource.nextFloat() < config.useAlternateLayer0Chance;
	               if (flag1) {
	                  this.safeSetBlock(worldgenlevel, blockpos3, geodeblocksettings.alternateInnerLayerProvider.getState(randomsource, blockpos3), predicate);
	               } else {
	                  this.safeSetBlock(worldgenlevel, blockpos3, geodeblocksettings.innerLayerProvider.getState(randomsource, blockpos3), predicate);
	               }

	               if ((!config.placementsRequireLayer0Alternate || flag1) && (double)randomsource.nextFloat() < config.usePotentialPlacementsChance) {
	                  list2.add(blockpos3.immutable());
	               }
	            } else if (d6 >= d3) {
	               this.safeSetBlock(worldgenlevel, blockpos3, geodeblocksettings.middleLayerProvider.getState(randomsource, blockpos3), predicate);
	            } else if (d6 >= d4) {
	               this.safeSetBlock(worldgenlevel, blockpos3, geodeblocksettings.outerLayerProvider.getState(randomsource, blockpos3), predicate);
	            }
	         }
	      }

	      List<BlockState> list3 = geodeblocksettings.innerPlacements;

	      for(BlockPos blockpos4 : list2) {
	         BlockState blockstate1 = Util.getRandom(list3, randomsource);

	         for(Direction direction : DIRECTIONS) {
	            if (blockstate1.hasProperty(BlockStateProperties.FACING)) {
	               blockstate1 = blockstate1.setValue(BlockStateProperties.FACING, direction);
	            }

	            BlockPos blockpos5 = blockpos4.relative(direction);
	            BlockState blockstate2 = worldgenlevel.getBlockState(blockpos5);
	            if (blockstate1.hasProperty(BlockStateProperties.WATERLOGGED)) {
	               blockstate1 = blockstate1.setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(blockstate2.getFluidState().isSource()));
	            }
	         }
	      }

	      return true;
	   }
}
