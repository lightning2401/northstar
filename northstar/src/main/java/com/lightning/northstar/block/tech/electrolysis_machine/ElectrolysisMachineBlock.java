package com.lightning.northstar.block.tech.electrolysis_machine;

import com.lightning.northstar.block.entity.NorthstarBlockEntityTypes;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ElectrolysisMachineBlock extends HorizontalKineticBlock implements IBE<ElectrolysisMachineBlockEntity> {

	public ElectrolysisMachineBlock(Properties properties) {
		super(properties);
	}
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return IBE.super.newBlockEntity(pPos, pState);
	}

	@Override
	public Axis getRotationAxis(BlockState state) {
		return Axis.Y;
	}
	
	@Override
	public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
		return face == Direction.DOWN;
	}

	@Override
	public Class<ElectrolysisMachineBlockEntity> getBlockEntityClass() {
		return ElectrolysisMachineBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends ElectrolysisMachineBlockEntity> getBlockEntityType() {
		return NorthstarBlockEntityTypes.ELECTROLYSIS_MACHINE.get();
	}
}
