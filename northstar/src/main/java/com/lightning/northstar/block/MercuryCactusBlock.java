package com.lightning.northstar.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.pathfinder.PathComputationType;

public class MercuryCactusBlock extends PipeBlock {

	public MercuryCactusBlock(BlockBehaviour.Properties pProperties) {
		super(0.3125F, pProperties);
		this.registerDefaultState(this.stateDefinition.any().setValue(NORTH, Boolean.valueOf(false)).setValue(EAST, Boolean.valueOf(false)).setValue(SOUTH, Boolean.valueOf(false)).setValue(WEST, Boolean.valueOf(false)).setValue(UP, Boolean.valueOf(false)).setValue(DOWN, Boolean.valueOf(false)));      
	}
	
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		return this.getStateForPlacement(pContext.getLevel(), pContext.getClickedPos());
	}

	public BlockState getStateForPlacement(BlockGetter pLevel, BlockPos pPos) {
		BlockState blockstate = pLevel.getBlockState(pPos.below());
		BlockState blockstate1 = pLevel.getBlockState(pPos.above());
		BlockState blockstate2 = pLevel.getBlockState(pPos.north());
		BlockState blockstate3 = pLevel.getBlockState(pPos.east());
		BlockState blockstate4 = pLevel.getBlockState(pPos.south());
		BlockState blockstate5 = pLevel.getBlockState(pPos.west());
		return this.defaultBlockState()
				.setValue(DOWN, Boolean.valueOf(blockstate.is(this) || blockstate.isSolidRender(pLevel, pPos.below())))
				.setValue(UP, Boolean.valueOf(blockstate1.is(this) || blockstate1.isSolidRender(pLevel, pPos.above())))
				.setValue(NORTH, Boolean.valueOf(blockstate2.is(this) || blockstate2.isSolidRender(pLevel, pPos.north())))
				.setValue(EAST, Boolean.valueOf(blockstate3.is(this) || blockstate3.isSolidRender(pLevel, pPos.east())))
				.setValue(SOUTH, Boolean.valueOf(blockstate4.is(this) || blockstate4.isSolidRender(pLevel, pPos.south())))
				.setValue(WEST, Boolean.valueOf(blockstate5.is(this) || blockstate5.isSolidRender(pLevel, pPos.west())));
	}
	
	@SuppressWarnings("deprecation")
	public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
		if (!pState.canSurvive(pLevel, pCurrentPos)) {
			pLevel.scheduleTick(pCurrentPos, this, 1);
			return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
		} else {
			BlockState newState = this.defaultBlockState();
			for(Direction direction : Direction.values()) {
				BlockPos blockpos = pCurrentPos.relative(direction);
				BlockState blockstate1 = pLevel.getBlockState(blockpos);
				if (blockstate1.is(this) || blockstate1.isSolidRender(pLevel, blockpos)) {
					newState = newState.setValue(PROPERTY_BY_DIRECTION.get(direction), true);
				}
			}
			return newState;
		}
	}

	public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
		if (!pState.canSurvive(pLevel, pPos)) {
// kind of debating whether i should keep this or not
//			pLevel.destroyBlock(pPos, true);
		}
	}
	
	public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
		pEntity.hurt(DamageSource.CACTUS, 1.0F);
	}
	
	public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
		BlockState blockstate = pLevel.getBlockState(pPos.below());		
		for(Direction direction : Direction.values()) {
			BlockPos blockpos = pPos.relative(direction);
			BlockState blockstate1 = pLevel.getBlockState(blockpos);
			if (blockstate1.is(this) || blockstate1.isSolidRender(pLevel, blockpos)) {
				return true;
			}
		}

		return blockstate.is(this) || blockstate.isSolidRender(pLevel, pPos);
	}
	
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
	}

	public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
		return false;
	}

}
