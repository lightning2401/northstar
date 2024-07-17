package com.lightning.northstar.block.tech.circuit_engraver;

import com.lightning.northstar.block.LaserBlock;
import com.lightning.northstar.block.NorthstarTechBlocks;
import com.lightning.northstar.block.entity.NorthstarBlockEntityTypes;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;

public class CircuitEngraverBlock extends HorizontalKineticBlock implements IBE<CircuitEngraverBlockEntity> {
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

	public CircuitEngraverBlock(Properties properties) {
		super(properties);
		registerDefaultState(defaultBlockState().setValue(POWERED, false).setValue(HORIZONTAL_FACING, Direction.NORTH));
	}
	
	@Override
	public Class<CircuitEngraverBlockEntity> getBlockEntityClass() {
		return CircuitEngraverBlockEntity.class;
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(POWERED);
		super.createBlockStateDefinition(builder);
	}

	@Override
	public BlockEntityType<? extends CircuitEngraverBlockEntity> getBlockEntityType() {
		return NorthstarBlockEntityTypes.CIRCUIT_ENGRAVER.get();
	}
	@Override
	public Axis getRotationAxis(BlockState state) {
		return state.getValue(HORIZONTAL_FACING)
			.getAxis();
	}

	@Override
	public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
		return face.getAxis() == state.getValue(HORIZONTAL_FACING)
			.getAxis();
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction prefferedSide = getPreferredHorizontalFacing(context);
		if (prefferedSide != null)
			return defaultBlockState().setValue(HORIZONTAL_FACING, prefferedSide);
		return super.getStateForPlacement(context);
	}
	
	@Override
	public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
		return false;
	}
	@Override
	public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
//		System.out.println("GREEN GOBLN");
//		System.out.println(pLevel.getBlockState(pCurrentPos.above()));
		if (pLevel.getBlockState(pCurrentPos.above()) == NorthstarTechBlocks.LASER.getDefaultState().setValue(LaserBlock.DIRECTION, Direction.DOWN)) {
			return pState.setValue(POWERED, true);
		}else
		{return pState.setValue(POWERED, false);}
	}

	
}
