package com.lightning.northstar.block.tech.rocket_controls;

import com.lightning.northstar.block.entity.NorthstarBlockEntityTypes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RocketControlsBlock extends HorizontalDirectionalBlock implements IWrenchable, IBE<RocketControlsBlockEntity> {
	protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 20.0D, 16.0D);
	
	public RocketControlsBlock(Properties properties) {
		super(properties);
		registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
	}
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> pBuilder) {
		super.createBlockStateDefinition(pBuilder.add(FACING));
	}
	
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return SHAPE;
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		BlockState state = defaultBlockState();
		Direction horizontalDirection = pContext.getHorizontalDirection();

		state = state.setValue(FACING, horizontalDirection.getOpposite());
		return state;
	}
	@Override
	public Class<RocketControlsBlockEntity> getBlockEntityClass() {
		return RocketControlsBlockEntity.class;
	}
	@Override
	public BlockEntityType<? extends RocketControlsBlockEntity> getBlockEntityType() {
		return NorthstarBlockEntityTypes.ROCKET_CONTROLS.get();
	}

	
}
