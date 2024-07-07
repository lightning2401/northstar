package com.lightning.northstar.block;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CrystalBlock extends Block implements SimpleWaterloggedBlock {
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final DirectionProperty FACING = BlockStateProperties.FACING;
	protected final VoxelShape northAabb;
	protected final VoxelShape southAabb;
	protected final VoxelShape eastAabb;
	protected final VoxelShape westAabb;
	protected final VoxelShape upAabb;
	protected final VoxelShape downAabb;

	public CrystalBlock(BlockBehaviour.Properties pProperties) {
		super(pProperties);
		int pOffset = 4;
		int pSize = 7;
		this.registerDefaultState(this.defaultBlockState().setValue(WATERLOGGED, Boolean.valueOf(false)).setValue(FACING, Direction.UP));
		this.upAabb = Block.box((double)pOffset, 0.0D, (double)pOffset, (double)(16 - pOffset), (double)pSize, (double)(16 - pOffset));
		this.downAabb = Block.box((double)pOffset, (double)(16 - pSize), (double)pOffset, (double)(16 - pOffset), 16.0D, (double)(16 - pOffset));
		this.northAabb = Block.box((double)pOffset, (double)pOffset, (double)(16 - pSize), (double)(16 - pOffset), (double)(16 - pOffset), 16.0D);
		this.southAabb = Block.box((double)pOffset, (double)pOffset, 0.0D, (double)(16 - pOffset), (double)(16 - pOffset), (double)pSize);
		this.eastAabb = Block.box(0.0D, (double)pOffset, (double)pOffset, (double)pSize, (double)(16 - pOffset), (double)(16 - pOffset));
		this.westAabb = Block.box((double)(16 - pSize), (double)pOffset, (double)pOffset, 16.0D, (double)(16 - pOffset), (double)(16 - pOffset));
	}
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		Direction direction = pState.getValue(FACING);
		switch (direction) {
		case NORTH:
			return this.northAabb;
		case SOUTH:
			return this.southAabb;
		case EAST:
			return this.eastAabb;
		case WEST:
			return this.westAabb;
		case DOWN:
			return this.downAabb;
		case UP:
		default:
			return this.upAabb;
		}
	}

	public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
		Direction direction = pState.getValue(FACING);
		BlockPos blockpos = pPos.relative(direction.getOpposite());
		return pLevel.getBlockState(blockpos).isFaceSturdy(pLevel, blockpos, direction);
	}

	   /**
	    * Update the provided state given the provided neighbor direction and neighbor state, returning a new state.
	    * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately
	    * returns its solidified counterpart.
	    * Note that this method should ideally consider only the specific direction passed in.
	    */
	@SuppressWarnings("deprecation")
	public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        BlockPos.MutableBlockPos blockpos = pCurrentPos.mutable();
        for(Direction direction : Direction.values()) {
        	blockpos.setWithOffset(pCurrentPos, direction);
        	pLevel.scheduleTick(blockpos, pLevel.getBlockState(blockpos).getBlock(), 3);
        }

		
		if (pState.getValue(WATERLOGGED)) {
			pLevel.scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
		}

		return pDirection == pState.getValue(FACING).getOpposite() && !pState.canSurvive(pLevel, pCurrentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(pState, pDirection, pNeighborState, pLevel, pCurrentPos, pNeighborPos);
	}

	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		LevelAccessor levelaccessor = pContext.getLevel();
		BlockPos blockpos = pContext.getClickedPos();
		return this.defaultBlockState().setValue(WATERLOGGED, Boolean.valueOf(levelaccessor.getFluidState(blockpos).getType() == Fluids.WATER)).setValue(FACING, pContext.getClickedFace());
	}

	/**
	 * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
	 * blockstate.
	 * @deprecated call via {@link net.minecraft.world.level.block.state.BlockBehaviour.BlockStateBase#rotate} whenever
	 * possible. Implementing/overriding is fine.
	 */
	   
	public BlockState rotate(BlockState pState, Rotation pRotation) {
		return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
	}

	/**
	 * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
	 * blockstate.
	 * @deprecated call via {@link net.minecraft.world.level.block.state.BlockBehaviour.BlockStateBase#mirror} whenever
	 * possible. Implementing/overriding is fine.
	 */
	public BlockState mirror(BlockState pState, Mirror pMirror) {
		return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
	}

	@SuppressWarnings("deprecation")
	public FluidState getFluidState(BlockState pState) {
		return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(WATERLOGGED, FACING);
	}

	/**
	 * @deprecated call via {@link
	 * net.minecraft.world.level.block.state.BlockBehaviour.BlockStateBase#getPistonPushReaction} whenever possible.
	 * Implementing/overriding is fine.
	 */
	public PushReaction getPistonPushReaction(BlockState pState) {
		return PushReaction.DESTROY;
	}

}
