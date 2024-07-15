package com.lightning.northstar.world.features;

import com.lightning.northstar.block.NorthstarBlocks;
import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class MercuryCactusFeature extends Feature<NoneFeatureConfiguration> {
	public MercuryCactusFeature(Codec<NoneFeatureConfiguration> p_65360_) {
		super(p_65360_);
	}
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> pContext) {
		WorldGenLevel level = pContext.level();
		BlockPos pos = pContext.origin();
		BlockPos.MutableBlockPos newpos = pos.mutable();
		
		if(level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, newpos).getY() <= newpos.getY()) 
		{return false;}
		
		RandomSource rando = pContext.random();
		int height = rando.nextInt(5, 10);
		
		for(int i = 0; i < height;) {
			if(level.getBlockState(newpos).isAir() || level.getBlockState(newpos).getMaterial().isReplaceable())
			{level.setBlock(newpos, NorthstarBlocks.MERCURY_CACTUS.get().defaultBlockState(), 3);}else 
			{i+=99999;}
			if(i == height - 1) {
				placeBranch(level, newpos, rando, (int)(height * 2.2f), Direction.NORTH, rando.nextInt(1, 3));
				placeBranch(level, newpos, rando, (int)(height * 2.2f), Direction.SOUTH, rando.nextInt(1, 3));
				placeBranch(level, newpos, rando, (int)(height * 2.2f), Direction.EAST, rando.nextInt(1, 3));
				placeBranch(level, newpos, rando, (int)(height * 2.2f), Direction.WEST, rando.nextInt(1, 3));
			}
			if(rando.nextInt(4) == 0 && i > 2) {
				newpos.move(Direction.Plane.HORIZONTAL.getRandomDirection(rando));
			}else {
				newpos.move(Direction.UP);
				i++;
			}
		}
		return true;
	}
	public void placeBranch(WorldGenLevel level, BlockPos pos, RandomSource rando, int height, Direction dir, int dist) {
		BlockPos.MutableBlockPos newpos = pos.mutable();
		for(int i = 0; i < height; ++i) {
			if(i <= dist) {
				newpos.move(dir);
			}else if(rando.nextInt(4) != 0) {
				newpos.move(Direction.UP);
			}else{newpos.move(dir);}
			if(level.getBlockState(newpos).isAir() || level.getBlockState(newpos).getMaterial().isReplaceable())
			{level.setBlock(newpos, NorthstarBlocks.MERCURY_CACTUS.get().defaultBlockState(), 3);
			level.scheduleTick(newpos, NorthstarBlocks.MERCURY_CACTUS.get(), 2);}else 
			{i+= 99999;}
		}
	}
}