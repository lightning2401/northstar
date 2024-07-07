package com.lightning.northstar.world.features;

import com.lightning.northstar.world.features.configuration.GlowstoneBranchConfig;
import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class GlowstoneUpsideDownBranchFeature extends Feature<GlowstoneBranchConfig> {

	public GlowstoneUpsideDownBranchFeature(Codec<GlowstoneBranchConfig> codec) {
		super(codec);
	}

	@Override
	  public boolean place(FeaturePlaceContext<GlowstoneBranchConfig> context) {
	      WorldGenLevel worldgenlevel = context.level();
	      BlockPos blockpos = context.origin();
	      RandomSource randomsource = context.random();
	      BlockStateProvider block = context.config().glowProvider;
	      if (!worldgenlevel.isEmptyBlock(blockpos)) {
	         return false;
	      } else {
	         BlockState blockstate = worldgenlevel.getBlockState(blockpos.below());
	         if (blockstate.is(Blocks.AIR)) {
	            return false;
	         } else {
	            worldgenlevel.setBlock(blockpos, block.getState(randomsource, blockpos), 2);

	            for(int i = 0; i < 750; ++i) {
	               BlockPos blockpos1 = blockpos.offset(randomsource.nextInt(8) - randomsource.nextInt(8), randomsource.nextInt(12), randomsource.nextInt(8) - randomsource.nextInt(8));
	               if (worldgenlevel.getBlockState(blockpos1).isAir()) {
	                  int j = 0;

	                  for(Direction direction : Direction.values()) {
	                     if (worldgenlevel.getBlockState(blockpos1.relative(direction)).is(block.getState(randomsource, blockpos).getBlock())) {
	                        ++j;
	                     }

	                     if (j > 1) {
	                        break;
	                     }
	                  }

	                  if (j == 1) {
	                     worldgenlevel.setBlock(blockpos1, block.getState(randomsource, blockpos), 2);
	                  }
	               }
	            }

	            return true;
	         }
	      }
	   }

}
