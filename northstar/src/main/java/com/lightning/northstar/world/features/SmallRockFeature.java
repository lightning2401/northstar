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
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;

public class SmallRockFeature extends Feature<BlockStateConfiguration> {

	public SmallRockFeature(Codec<BlockStateConfiguration> pCodec) {
		super(pCodec);
	}

	@Override
	public boolean place(FeaturePlaceContext<BlockStateConfiguration> pContext) {
		  BlockStateConfiguration config = pContext.config();
	      BlockPos blockpos = pContext.origin();
	      WorldGenLevel worldgenlevel = pContext.level();
	      RandomSource randomsource = pContext.random();
	      boolean flag = false;
	      
	      placeBlock(config, worldgenlevel, randomsource, blockpos);
	      placeBlock(config, worldgenlevel, randomsource, blockpos.offset(0,1,0));
	      placeBlock(config, worldgenlevel, randomsource, blockpos.offset(0,2,0));
	      
	      placeBlock(config, worldgenlevel, randomsource, blockpos.offset(1,0,0));
	      placeBlock(config, worldgenlevel, randomsource, blockpos.offset(-1,0,0));
	      placeBlock(config, worldgenlevel, randomsource, blockpos.offset(0,0,1));
	      placeBlock(config, worldgenlevel, randomsource, blockpos.offset(0,0,-1));
	      
	      placeBlock(config, worldgenlevel, randomsource, blockpos.offset(1,1,0));
	      placeBlock(config, worldgenlevel, randomsource, blockpos.offset(-1,1,0));
	      placeBlock(config, worldgenlevel, randomsource, blockpos.offset(0,1,1));
	      placeBlock(config, worldgenlevel, randomsource, blockpos.offset(0,1,-1));
	      
	      placeBlock(config, worldgenlevel, randomsource, blockpos.offset(1,1,0));
	      placeBlock(config, worldgenlevel, randomsource, blockpos.offset(1,1,1));
	      placeBlock(config, worldgenlevel, randomsource, blockpos.offset(1,1,-1));
	      placeBlock(config, worldgenlevel, randomsource, blockpos.offset(-1,1,0));
	      placeBlock(config, worldgenlevel, randomsource, blockpos.offset(-1,1,1));
	      placeBlock(config, worldgenlevel, randomsource, blockpos.offset(-1,1,-1));
	      placeBlock(config, worldgenlevel, randomsource, blockpos.offset(0,1,1));
	      placeBlock(config, worldgenlevel, randomsource, blockpos.offset(0,1,-1));
	      
	      placeBlock(config, worldgenlevel, randomsource, blockpos.offset(1,2,0));
	      placeBlock(config, worldgenlevel, randomsource, blockpos.offset(-1,2,0));
	      placeBlock(config, worldgenlevel, randomsource, blockpos.offset(0,2,1));
	      placeBlock(config, worldgenlevel, randomsource, blockpos.offset(0,2,-1));
	      
	      
	      placeBlock(config, worldgenlevel, randomsource, blockpos);

	      return flag;
	}

	protected boolean placeBlock(BlockStateConfiguration pConfig, WorldGenLevel pLevel, RandomSource pRandom, BlockPos pPos) {
	      boolean flag = false;
	      BlockState blockstate = pConfig.state;
	      pLevel.setBlock(pPos, blockstate, 2);
	      this.markAboveForPostProcessing(pLevel, pPos);
	      flag = true;

	      return flag;
	   }

}
