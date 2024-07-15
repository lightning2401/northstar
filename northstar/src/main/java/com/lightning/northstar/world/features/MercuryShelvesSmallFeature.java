package com.lightning.northstar.world.features;

import com.lightning.northstar.block.MercuryShelfFungusBlock;
import com.lightning.northstar.block.NorthstarBlocks;
import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class MercuryShelvesSmallFeature extends Feature<NoneFeatureConfiguration> {
	public MercuryShelvesSmallFeature(Codec<NoneFeatureConfiguration> pCodec) {
		super(pCodec);
	}
	
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> pContext) {
	      WorldGenLevel level = pContext.level();
	      BlockPos pos = pContext.origin();
	      RandomSource rando = pContext.random();
	      if(level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos).getY() <= pos.getY()) 
	      {return false;}
	      BlockPos.MutableBlockPos mutable = pos.mutable();
	      int radius = rando.nextIntBetweenInclusive(3, 7);
	      Direction dir = Direction.Plane.HORIZONTAL.getRandomDirection(rando);
	      
	      for(BlockPos blockpos1 : BlockPos.betweenClosed(pos.offset(-radius, -radius / 2, -radius), pos.offset(radius, radius / 2, radius))) {
	    	  int difX = blockpos1.getX() - pos.getX();
				int difZ = blockpos1.getZ() - pos.getZ();
				if (difX * difX + difZ * difZ <= radius * radius - 0.1) {
					if(rando.nextInt(2) == 0) {
						BlockState blockstate = NorthstarBlocks.MERCURY_SHELF_FUNGUS.get().defaultBlockState()
								.setValue(MercuryShelfFungusBlock.FACING, dir).setValue(MercuryShelfFungusBlock.SHELVES, rando.nextInt(1,6));
						if(level.getBlockState(blockpos1.relative(dir.getOpposite())).isSolidRender(level, blockpos1.relative(dir.getOpposite()))) {
							if(level.getBlockState(blockpos1).isAir())
							{level.setBlock(blockpos1, blockstate, 8);}
						}
					}
		      			
				}
			}
	      return true;
	   }
}