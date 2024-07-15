package com.lightning.northstar.world.features;

import java.util.ArrayList;
import java.util.List;

import com.lightning.northstar.block.NorthstarBlocks;
import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class MercuryShelvesFeature extends Feature<NoneFeatureConfiguration> {
	public MercuryShelvesFeature(Codec<NoneFeatureConfiguration> p_65360_) {
		super(p_65360_);
	}
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> pContext) {
		BlockPos blockpos = pContext.origin();
		RandomSource rando = pContext.random();
		
		int i = blockpos.getY();
		int j = i + rando.nextInt(1,3);
		int k = i - rando.nextInt(1,3) - 1;
		int l = rando.nextInt(2,5);
		BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
		int pp = 0;
		for(int l_old = l;l > l_old / j; l = (int) (l / 1.5)) {
			if (pp < rando.nextInt(4,5)) {
				pp++; 
				blockpos = blockpos.below();
			}
			for(BlockPos blockpos1 : BlockPos.betweenClosed(blockpos.offset(-l, 0, -l), blockpos.offset(l, 0, l))) {
				int i1 = blockpos1.getX() - blockpos.getX();
				int j1 = blockpos1.getZ() - blockpos.getZ();
				if (i1 * i1 + j1 * j1 <= l * l - 0.1) {
					this.placeShelf(pContext.level(), rando, j, k, blockpos$mutableblockpos.set(blockpos1));
				}
			}
		}
		return true;
	}
	protected void placeShelf(WorldGenLevel pLevel, RandomSource pRandom, int pMaxY, int pMinY, BlockPos.MutableBlockPos pPos) {
		boolean flag = false;
		for(int x = -2; x < 3; x++) {
			for(int z = -2; z < 3; z++) {
				if(!(pLevel.getBlockState(pPos.offset(x,0,z)).isAir() || pLevel.getBlockState(pPos.offset(x,0,z)).getMaterial().isReplaceable())) {
					flag = true;
				}
			}
		}
		if(flag) {
			for(int x = -2; x < 3; x++) {
				for(int z = -2; z < 3; z++) {
					if(Mth.abs(x) + Mth.abs(z) != 4) {
						if(pLevel.getBlockState(pPos.offset(x,0,z)).isAir() || pLevel.getBlockState(pPos.offset(x,0,z)).getMaterial().isReplaceable()) {
							pLevel.setBlock(pPos.offset(x, 0, z), NorthstarBlocks.MERCURY_SHELF_FUNGUS_BLOCK.get().defaultBlockState(), 3);
						}
					}
				}
			}
		}
	}
}