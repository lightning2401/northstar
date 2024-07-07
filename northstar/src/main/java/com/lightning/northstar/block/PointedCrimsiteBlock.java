package com.lightning.northstar.block;

import java.util.function.Consumer;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;
import net.minecraft.world.level.material.Fluids;

public class PointedCrimsiteBlock extends PointedDripstoneBlock implements Fallable, SimpleWaterloggedBlock {

	public PointedCrimsiteBlock(Properties pProperties) {
		super(pProperties);
	     this.registerDefaultState(this.stateDefinition.any().setValue(TIP_DIRECTION, Direction.UP).setValue(THICKNESS, DripstoneThickness.TIP).setValue(WATERLOGGED, Boolean.valueOf(false)));
	}
	
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		LevelAccessor levelaccessor = pContext.getLevel();
		BlockPos blockpos = pContext.getClickedPos();
		Direction direction = pContext.getNearestLookingVerticalDirection().getOpposite();
		Direction direction1 = calculateTipDirection(levelaccessor, blockpos, direction);
		if (direction1 == null) {
			return null;
		} else {
			boolean flag = !pContext.isSecondaryUseActive();
			DripstoneThickness dripstonethickness = calculateDripstoneThickness(levelaccessor, blockpos, direction1, flag);
			return dripstonethickness == null ? null : this.defaultBlockState().setValue(TIP_DIRECTION, direction1).setValue(THICKNESS, dripstonethickness).setValue(WATERLOGGED, Boolean.valueOf(levelaccessor.getFluidState(blockpos).getType() == Fluids.WATER));
		}
	}
	
	private static DripstoneThickness calculateDripstoneThickness(LevelReader pLevel, BlockPos pPos, Direction pDir, boolean pIsTipMerge) {
		Direction direction = pDir.getOpposite();
		BlockState blockstate = pLevel.getBlockState(pPos.relative(pDir));
		if (isPointedDripstoneWithDirection(blockstate, direction)) {
			return !pIsTipMerge && blockstate.getValue(THICKNESS) != DripstoneThickness.TIP_MERGE ? DripstoneThickness.TIP : DripstoneThickness.TIP_MERGE;
		} else if (!isPointedDripstoneWithDirection(blockstate, pDir)) {
			return DripstoneThickness.TIP;
		} else {
			DripstoneThickness dripstonethickness = blockstate.getValue(THICKNESS);
			if (dripstonethickness != DripstoneThickness.TIP && dripstonethickness != DripstoneThickness.TIP_MERGE) {
				BlockState blockstate1 = pLevel.getBlockState(pPos.relative(direction));
					return !isPointedDripstoneWithDirection(blockstate1, pDir) ? DripstoneThickness.BASE : DripstoneThickness.MIDDLE;
	         } else {
	        	 return DripstoneThickness.FRUSTUM;
	         }
		}
	}
	public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
	      if (isStalagmite(pState) && !this.canSurvive(pState, pLevel, pPos)) {
	         pLevel.destroyBlock(pPos, true);
	      } else {
	         spawnFallingStalactite(pState, pLevel, pPos);
	      }
	}
	private static void spawnFallingStalactite(BlockState pState, ServerLevel pLevel, BlockPos pPos) {
	      BlockPos.MutableBlockPos blockpos$mutableblockpos = pPos.mutable();

	      for(BlockState blockstate = pState; isStalactite(blockstate); blockstate = pLevel.getBlockState(blockpos$mutableblockpos)) {
	         FallingBlockEntity fallingblockentity = FallingBlockEntity.fall(pLevel, blockpos$mutableblockpos, blockstate);
	         if (isTip(blockstate, true)) {
	            int i = Math.max(1 + pPos.getY() - blockpos$mutableblockpos.getY(), 6);
	            float f = 1.0F * (float)i;
	            fallingblockentity.setHurtsEntities(f, 40);
	            break;
	         }

	         blockpos$mutableblockpos.move(Direction.DOWN);
	      }

	   }
	private static boolean isTip(BlockState pState, boolean pIsTipMerge) {
		if (!pState.is(NorthstarBlocks.POINTED_CRIMSITE.get())) {
	         return false;
		} else {
			DripstoneThickness dripstonethickness = pState.getValue(THICKNESS);
			return dripstonethickness == DripstoneThickness.TIP || pIsTipMerge && dripstonethickness == DripstoneThickness.TIP_MERGE;
		}
	}
	
	private static boolean isStalactite(BlockState pState) {
		return isPointedDripstoneWithDirection(pState, Direction.DOWN);
	}
	
	private static boolean isStalagmite(BlockState pState) {
		return isPointedDripstoneWithDirection(pState, Direction.UP);
	}
	
	@Nullable
	private static Direction calculateTipDirection(LevelReader pLevel, BlockPos pPos, Direction pDir) {
		Direction direction;
		if (isValidPointedDripstonePlacement(pLevel, pPos, pDir)) {
			direction = pDir;
		} else {
			if (!isValidPointedDripstonePlacement(pLevel, pPos, pDir.getOpposite())) {
				return null;
			}

			direction = pDir.getOpposite();
		}

		return direction;
	}
	
	private static boolean isValidPointedDripstonePlacement(LevelReader pLevel, BlockPos pPos, Direction pDir) {
		BlockPos blockpos = pPos.relative(pDir.getOpposite());
		BlockState blockstate = pLevel.getBlockState(blockpos);
		return blockstate.isFaceSturdy(pLevel, blockpos, pDir) || isPointedDripstoneWithDirection(blockstate, pDir);
	}
	
	@Override
	public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
		return isValidPointedDripstonePlacement(pLevel, pPos, pState.getValue(TIP_DIRECTION));
	}
	@Override
	public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {}
	
	private static boolean isPointedDripstoneWithDirection(BlockState pState, Direction pDir) {
	return pState.is(NorthstarBlocks.POINTED_CRIMSITE.get()) && pState.getValue(TIP_DIRECTION) == pDir;
	}
	
	public static void growPointedDripstone(LevelAccessor pLevel, BlockPos pPos, Direction pDirection, int pHeight, boolean pMergeTip, Block block) {
	      if (pLevel.getBlockState(pPos.relative(pDirection.getOpposite())).isSolidRender(pLevel, pPos.relative(pDirection.getOpposite()))) {
	         BlockPos.MutableBlockPos blockpos$mutableblockpos = pPos.mutable();
	         buildBaseToTipColumn(pDirection, pHeight, pMergeTip, (p_190846_) -> {
	            if (p_190846_.is(block)) {
	               p_190846_ = p_190846_.setValue(WATERLOGGED, Boolean.valueOf(pLevel.isWaterAt(blockpos$mutableblockpos)));
	            }
	            if(!pLevel.getBlockState(blockpos$mutableblockpos).isSolidRender(pLevel, blockpos$mutableblockpos))
	            {pLevel.setBlock(blockpos$mutableblockpos, p_190846_, 2);}
	            blockpos$mutableblockpos.move(pDirection);
	            if(!pLevel.getBlockState(blockpos$mutableblockpos).isAir()) {
	            	return;
	            }
	         }, block);
	      }
	}
	protected static void buildBaseToTipColumn(Direction pDirection, int pHeight, boolean pMergeTip, Consumer<BlockState> pBlockSetter, Block block) {
	      if (pHeight >= 3) {
	         pBlockSetter.accept(createPointedDripstone(pDirection, DripstoneThickness.BASE, block));

	         for(int i = 0; i < pHeight - 3; ++i) {
	            pBlockSetter.accept(createPointedDripstone(pDirection, DripstoneThickness.MIDDLE, block));
	         }
	      }

	      if (pHeight >= 2) {
	         pBlockSetter.accept(createPointedDripstone(pDirection, DripstoneThickness.FRUSTUM, block));
	      }

	      if (pHeight >= 1) {
	    	  pBlockSetter.accept(createPointedDripstone(pDirection, pMergeTip ? DripstoneThickness.TIP_MERGE : DripstoneThickness.TIP, block));
	      }
	}
	private static BlockState createPointedDripstone(Direction pDirection, DripstoneThickness pDripstoneThickness, Block block) {
	      return block.defaultBlockState().setValue(TIP_DIRECTION, pDirection).setValue(THICKNESS, pDripstoneThickness);
	   }
	
	@Override
	public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
	      if (pState.getValue(WATERLOGGED)) {
	         pLevel.scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
	      }

	      if (pDirection != Direction.UP && pDirection != Direction.DOWN) {
	         return pState;
	      } else {
	         Direction direction = pState.getValue(TIP_DIRECTION);
	         if (direction == Direction.DOWN && pLevel.getBlockTicks().hasScheduledTick(pCurrentPos, this)) {
	            return pState;
	         } else if (pDirection == direction.getOpposite() && !this.canSurvive(pState, pLevel, pCurrentPos)) {
	            if (direction == Direction.DOWN) {
	               pLevel.scheduleTick(pCurrentPos, this, 2);
	            } else {
	               pLevel.scheduleTick(pCurrentPos, this, 1);
	            }

	            return pState;
	         } else {
	            boolean flag = pState.getValue(THICKNESS) == DripstoneThickness.TIP_MERGE;
	            DripstoneThickness dripstonethickness = calculateDripstoneThickness(pLevel, pCurrentPos, direction, flag);
	            return pState.setValue(THICKNESS, dripstonethickness);
	         }
	      }
	   }
}
