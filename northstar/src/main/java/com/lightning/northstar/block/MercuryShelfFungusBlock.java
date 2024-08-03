package com.lightning.northstar.block;

import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MercuryShelfFungusBlock extends Block implements SimpleWaterloggedBlock{
	public static final int MIN_SHELVES = 1;
	public static final int MAX_SHELVES = 6;
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final IntegerProperty SHELVES = IntegerProperty.create("shelves", 1, 6);
	protected static final float AABB_OFFSET = 2.5F;
	private static final Map<Direction, VoxelShape> AABBS = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, Block.box(2D, 3.0D, 11.0D, 14D, 13.0D, 16.0D),
			Direction.SOUTH, Block.box(2D, 3.0D, 0.0D, 14D, 13.0D, 5.0D), Direction.WEST, Block.box(11.0D, 3.0D, 2D, 16.0D, 13.0D, 14D),
			Direction.EAST, Block.box(0.0D, 3.0D, 2D, 5.0D, 13.0D, 14D)));
	

	public MercuryShelfFungusBlock(BlockBehaviour.Properties pProperties) {
		super(pProperties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false).setValue(SHELVES, MIN_SHELVES));
	}
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return getShape(pState);
	}

	public static VoxelShape getShape(BlockState pState) {
		return AABBS.get(pState.getValue(FACING));
	}

	@Override
	public boolean propagatesSkylightDown(BlockState pState, BlockGetter pReader, BlockPos pPos) {
		return pState.getFluidState().isEmpty();
	}
	@SuppressWarnings("deprecation")
	@Override
	public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
		return pType == PathComputationType.AIR && !this.hasCollision ? true : super.isPathfindable(pState, pLevel, pPos, pType);
	}

	
	
	public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
		return pFacing.getOpposite() == pState.getValue(FACING) && !pState.canSurvive(pLevel, pCurrentPos) ? Blocks.AIR.defaultBlockState() : pState;
	}
	
	public boolean canBeReplaced(BlockState pState, BlockPlaceContext pUseContext) {
		return pUseContext.getItemInHand().is(this.asItem());
	}
	
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		BlockState blockstate = this.defaultBlockState();
		BlockState clickedState = pContext.getLevel().getBlockState(pContext.getClickedPos());
		System.out.println(clickedState.getBlock());
		if (clickedState.is(blockstate.getBlock())) {
			return clickedState.setValue(SHELVES, Integer.valueOf(Math.min(MAX_SHELVES, clickedState.getValue(SHELVES) + 1)));
		}
		FluidState fluidstate = pContext.getLevel().getFluidState(pContext.getClickedPos());
		LevelReader levelreader = pContext.getLevel();
		BlockPos blockpos = pContext.getClickedPos();
		Direction[] adirection = pContext.getNearestLookingDirections();
		      
		for(Direction direction : adirection) {
			if (direction.getAxis().isHorizontal()) {
				Direction direction1 = direction.getOpposite();
				blockstate = blockstate.setValue(FACING, direction1);
				if (blockstate.canSurvive(levelreader, blockpos)) {
					return blockstate.setValue(WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER));
	            }
			}
		}
		
		return null;
	}
	
	@SuppressWarnings("deprecation")
	public FluidState getFluidState(BlockState pState) {
		return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
	}
	

	public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
		Direction direction = pState.getValue(FACING);
		BlockPos blockpos = pPos.relative(direction.getOpposite());
		BlockState blockstate = pLevel.getBlockState(blockpos);
		return blockstate.isFaceSturdy(pLevel, blockpos, direction);
	}
	
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(FACING, WATERLOGGED, SHELVES);
	}
	public BlockState getStateForGeneration(BlockState state, WorldGenLevel level, BlockPos blockpos1, Direction direction) {
		BlockState blockstate = this.defaultBlockState();
		for(Direction dir : Direction.Plane.HORIZONTAL) {
			if(level.getBlockState(blockpos1.relative(dir)).isSolidRender(level, blockpos1.relative(dir))) {
				blockstate = blockstate.setValue(FACING, dir);
			}
		}
		if(blockstate != this.defaultBlockState())
			return blockstate;
		else
			return null;
	}
	
}
