package com.lightning.northstar.block.tech.combustion_engine;

import com.lightning.northstar.block.entity.NorthstarBlockEntityTypes;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.Couple;

import net.minecraft.core.BlockPos; 
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CombustionEngineBlock extends HorizontalKineticBlock implements IBE<CombustionEngineBlockEntity>  {
	protected static final VoxelShape SHAPE_NORTH_SOUTH = Block.box(3.0D, 0.0D, 0.0D, 13.0D, 13.0D, 16.0D);

	protected static final VoxelShape SHAPE_EAST_WEST = Block.box(0.0D, 0.0D, 3.0D, 16.0D, 13.0D, 13.0D);
	
	public CombustionEngineBlock(Properties properties) {
		super(properties);
	}

	@Override
	public Axis getRotationAxis(BlockState state) {
		return state.getValue(HORIZONTAL_FACING).getAxis();
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return IBE.super.newBlockEntity(pPos, pState);
	}
	
	@Override
	public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
		return state.getValue(HORIZONTAL_FACING).getOpposite() == face;
	}

	@Override
	public Class<CombustionEngineBlockEntity> getBlockEntityClass() {
		return CombustionEngineBlockEntity.class;
	}
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		switch((Direction)pState.getValue(HORIZONTAL_FACING)) {
		case NORTH:
			return SHAPE_NORTH_SOUTH;
		case SOUTH:
			return SHAPE_NORTH_SOUTH;
		case EAST:
			return SHAPE_EAST_WEST;
		case WEST:
			return SHAPE_EAST_WEST;
		default:
			return SHAPE_NORTH_SOUTH;
		}
	}

	@Override
	public BlockEntityType<? extends CombustionEngineBlockEntity> getBlockEntityType() {
		return NorthstarBlockEntityTypes.COMBUSTION_ENGINE.get();
	}
	public static Couple<Integer> getSpeedRange() {
		return Couple.create(16, 32);
	}

}
