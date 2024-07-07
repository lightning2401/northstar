package com.lightning.northstar.block.tech.oxygen_generator;

import java.util.HashSet;

import com.lightning.northstar.block.entity.NorthstarBlockEntityTypes;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;

public class OxygenGeneratorBlock extends HorizontalKineticBlock implements IBE<OxygenGeneratorBlockEntity> {

	public OxygenGeneratorBlock(Properties pProperties) {
		super(pProperties);
	}
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return IBE.super.newBlockEntity(pPos, pState);
	}	
	@Override
	public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
		System.out.println("BIG STEVE IS REAL");
		if (!pState.is(pNewState.getBlock())) {
			BlockEntity blockentity = pLevel.getBlockEntity(pPos);
			if (blockentity instanceof OxygenGeneratorBlockEntity) {
				((OxygenGeneratorBlockEntity)blockentity).removeOxy((OxygenGeneratorBlockEntity) blockentity, new HashSet<BlockPos>());
			}

			super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
		}
	}
	@Override
	public Axis getRotationAxis(BlockState state) {
		return Axis.Y;
	}
	@Override
	public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
		return face == Direction.DOWN;
	}
	public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
	     return false;
	}
	public RenderShape getRenderShape(BlockState pState) {
		return RenderShape.MODEL;
	}
	@Override
	public Class<OxygenGeneratorBlockEntity> getBlockEntityClass() {
		return OxygenGeneratorBlockEntity.class;
	}
	@Override
	public BlockEntityType<? extends OxygenGeneratorBlockEntity> getBlockEntityType() {
		return NorthstarBlockEntityTypes.OXYGEN_GENERATOR.get();
	}
}
