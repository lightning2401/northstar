package com.lightning.northstar.world.features;

import java.util.ArrayList;
import java.util.List;

import com.lightning.northstar.block.NorthstarBlocks;
import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

//

public class MercuryCactusFeature extends Feature<NoneFeatureConfiguration> {
	public MercuryCactusFeature(Codec<NoneFeatureConfiguration> p_65360_) {
		super(p_65360_);
	}
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> pContext) {
		WorldGenLevel level = pContext.level();
		BlockPos pos = pContext.origin();
		RandomSource rando = pContext.random();
		int height = rando.nextInt(5, 10);
		List<Direction> usedDirections = new ArrayList<>();
		
		for(int i = 0; i < height; ++i) {
			level.setBlock(pos.offset(0, i, 0), NorthstarBlocks.MERCURY_CACTUS.get().defaultBlockState(), 3);
			if(rando.nextInt(4) == 0 || i == height - 1) {
				Direction dir = Direction.Plane.HORIZONTAL.getRandomDirection(rando);
				if(!usedDirections.contains(dir)) {
					placeBranch(level, pos.offset(0, i, 0), rando, height, dir);
					usedDirections.add(dir);
				}
			}
		}
		return true;
	}
	public void placeBranch(WorldGenLevel level, BlockPos pos, RandomSource rando, int height, Direction dir) {
		BlockPos.MutableBlockPos newpos = pos.mutable();
		for(int i = 0; i < height; ++i) {
			if(i == 0) {
				newpos.move(dir);
			}
			if(rando.nextInt(3) == 0) {
				if(rando.nextBoolean())
				newpos.move(dir.getClockWise());
				else
				newpos.move(dir.getCounterClockWise());
			}else if(rando.nextInt(3) == 0) {
				newpos.move(Direction.UP);
			}
			else{newpos.move(dir);}
			level.setBlock(newpos, NorthstarBlocks.MERCURY_CACTUS.get().defaultBlockState(), 3);
		}
	}
}