package com.lightning.northstar.world.features;

import java.util.ArrayList;
import java.util.List;

import com.lightning.northstar.block.NorthstarBlocks;
import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.MultifaceGrowthConfiguration;

public class MarsRootsFeature extends Feature<MultifaceGrowthConfiguration> {
	public MarsRootsFeature(Codec<MultifaceGrowthConfiguration> pCodec) {
		super(pCodec);
	}
	
	public boolean place(FeaturePlaceContext<MultifaceGrowthConfiguration> pContext) {
	      WorldGenLevel worldgenlevel = pContext.level();
	      BlockPos blockpos = pContext.origin();
	      RandomSource randomsource = pContext.random();
	      MultifaceGrowthConfiguration multifacegrowthconfiguration = pContext.config();
	      List<Direction> list = multifacegrowthconfiguration.getShuffledDirections(randomsource);
	      if (placeGrowthIfPossible(worldgenlevel, blockpos, worldgenlevel.getBlockState(blockpos), multifacegrowthconfiguration, randomsource, list)) {
	    	  return true;
	      } else {
	    	  BlockPos.MutableBlockPos blockpos$mutableblockpos = blockpos.mutable();

	    	  for(Direction direction : list) {
	    		  blockpos$mutableblockpos.set(blockpos);
	    		  List<Direction> list1 = multifacegrowthconfiguration.getShuffledDirectionsExcept(randomsource, direction.getOpposite());
	    		  
	    		  for(int i = 0; i < multifacegrowthconfiguration.searchRange; ++i) {
	    			  blockpos$mutableblockpos.setWithOffset(blockpos, direction);
	    			  BlockState blockstate = worldgenlevel.getBlockState(blockpos$mutableblockpos);
	    			  if (!blockstate.is(multifacegrowthconfiguration.placeBlock)) {
	    				  break;
	    			  }
	    			  
	    			  if (placeGrowthIfPossible(worldgenlevel, blockpos$mutableblockpos, blockstate, multifacegrowthconfiguration, randomsource, list1)) {
	    				  return true;
	    			  }
	    		  }
	    	  }
	    	  
	    	  return false;
	      }
	   }

	public static boolean placeGrowthIfPossible(WorldGenLevel level, BlockPos pos, BlockState state, MultifaceGrowthConfiguration config, RandomSource rando, List<Direction> directions) {
		BlockPos.MutableBlockPos mutable = pos.mutable();
		int radius = rando.nextIntBetweenInclusive(3, 7);

		for(BlockPos blockpos1 : BlockPos.betweenClosed(pos.offset(-radius, -radius / 2, -radius), pos.offset(radius, radius / 2, radius))) {
			int difX = blockpos1.getX() - pos.getX();
			int difZ = blockpos1.getZ() - pos.getZ();
			List<Direction> dirs = new ArrayList<Direction>();
			if (difX * difX + difZ * difZ <= radius * radius - 0.1) {
				for(Direction direction : Direction.values()) {
					BlockState blockstate = level.getBlockState(mutable.setWithOffset(blockpos1, direction));
					if (blockstate.is(config.canBePlacedOn)) {
						BlockState blockstate1 = config.placeBlock.getStateForPlacement(state, level, blockpos1, direction);
//						BlockState blockstate1 = NorthstarBlocks.MARS_ROOTS.get().defaultBlockState();
						
						if (blockstate1 == null) {
							continue;}

						dirs.add(direction);
						
//						blockstate1.randomTick(level.getLevel(), blockpos1, rando);
	    		         
					}
				}
				
				BlockState blockstate = config.placeBlock.defaultBlockState();
				if(rando.nextInt(4) == 0) 
				{blockstate = NorthstarBlocks.GLOWING_MARS_ROOTS.get().defaultBlockState();}
				for(Direction direction2 : dirs) {
					blockstate = blockstate.setValue(MultifaceBlock.getFaceProperty(direction2), true);
				}
				if(level.getBlockState(blockpos1).isAir())
				{level.setBlock(blockpos1, blockstate, 8);}
				level.getChunk(blockpos1).markPosForPostprocessing(blockpos1);
	      			
			}
		}


		return true;
	}
}