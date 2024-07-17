package com.lightning.northstar.world.features;

import com.lightning.northstar.block.NorthstarBlocks;
import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Heightmap;
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
		WorldGenLevel level = pContext.level();
		if(level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, blockpos).getY() <= blockpos.getY()) 
		{return false;}
		boolean flag = false;
		for(int i = 0; i < 10; i++) {
			if (rando.nextBoolean()) {
				BlockPos newpos = blockpos.offset(rando.nextInt(-7,7), rando.nextInt(-7,7),rando.nextInt(-7,7));
				if(level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, newpos).getY() - 5 > newpos.getY()) {
					if(placeShelf(level, rando, newpos.mutable())) {
						flag = true;
					}
				}
			}
		}
		return flag;
	}
	protected boolean placeShelf(WorldGenLevel level, RandomSource pRandom, BlockPos.MutableBlockPos pPos) {
		boolean flag = false;
		for(int x = -2; x < 3; x++) {
			for(int z = -2; z < 3; z++) {
				if(level.getBlockState(pPos.offset(x,0,z)).isSolidRender(level, pPos.offset(x,0,z))) {
					flag = true;
				}
			}
		}
		if(flag) {
			for(int x = -2; x < 3; x++) {
				for(int z = -2; z < 3; z++) {
					if(Mth.abs(x) + Mth.abs(z) != 4) {
						if(!level.getBlockState(pPos.offset(x,0,z)).isSolidRender(level, pPos.offset(x,0,z))) {
							level.setBlock(pPos.offset(x, 0, z), NorthstarBlocks.MERCURY_SHELF_FUNGUS_BLOCK.get().defaultBlockState(), 3);
						}
					}
				}
			}
		}
		return flag;
	}
}