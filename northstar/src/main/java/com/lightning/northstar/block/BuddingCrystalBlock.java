package com.lightning.northstar.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.AmethystBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;

public class BuddingCrystalBlock extends AmethystBlock {
		public static final int GROWTH_CHANCE = 5;
	   private static final Direction[] DIRECTIONS = Direction.values();
	   private Block clusterBlock;
	   private Block stage1Block;
	   private Block stage2Block;
	   private Block stage3Block;

	   public BuddingCrystalBlock(BlockBehaviour.Properties pProperties, Block stage1, Block stage2, Block stage3, Block cluster) {
	      super(pProperties);
	      clusterBlock = cluster;
	      stage1Block = stage1;
	      stage2Block = stage2;
	      stage3Block = stage3;
	   }

	   /**
	    * @deprecated call via {@link
	    * net.minecraft.world.level.block.state.BlockBehaviour.BlockStateBase#getPistonPushReaction} whenever possible.
	    * Implementing/overriding is fine.
	    */
	   public PushReaction getPistonPushReaction(BlockState pState) {
	      return PushReaction.DESTROY;
	   }

	   /**
	    * Performs a random tick on a block.
	    */
	   public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
	      if (pRandom.nextInt(5) == 0) {
	         Direction direction = DIRECTIONS[pRandom.nextInt(DIRECTIONS.length)];
	         BlockPos blockpos = pPos.relative(direction);
	         BlockState blockstate = pLevel.getBlockState(blockpos);
	         Block block = null;
	         if (canClusterGrowAtState(blockstate)) {
	            block = stage1Block;
	         } else if (blockstate.is(stage1Block) && blockstate.getValue(ClusterBlock.FACING) == direction) {
	            block = stage2Block;
	         } else if (blockstate.is(stage2Block) && blockstate.getValue(ClusterBlock.FACING) == direction) {
	            block = stage3Block;
	         } else if (blockstate.is(stage3Block) && blockstate.getValue(ClusterBlock.FACING) == direction) {
	            block = clusterBlock;
	         }

	         if (block != null) {
	            BlockState blockstate1 = block.defaultBlockState().setValue(ClusterBlock.FACING, direction).setValue(ClusterBlock.WATERLOGGED, Boolean.valueOf(blockstate.getFluidState().getType() == Fluids.WATER));
	            pLevel.setBlockAndUpdate(blockpos, blockstate1);
	         }

	      }
	   }

	   public static boolean canClusterGrowAtState(BlockState pState) {
	      return pState.isAir() || pState.is(Blocks.WATER) && pState.getFluidState().getAmount() == 8;
	   }

}
