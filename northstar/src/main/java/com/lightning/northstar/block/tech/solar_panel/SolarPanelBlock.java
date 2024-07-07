package com.lightning.northstar.block.tech.solar_panel;

import com.lightning.northstar.block.entity.NorthstarBlockEntityTypes;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.Couple;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SolarPanelBlock extends HorizontalKineticBlock implements IBE<SolarPanelBlockEntity> {

	public SolarPanelBlock(Properties properties) {
		super(properties);
	}

	@Override
	public Axis getRotationAxis(BlockState state) {
		return state.getValue(HORIZONTAL_FACING)
				.getAxis();
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockState state = super.getStateForPlacement(context);
		state.setValue(HORIZONTAL_FACING, Direction.get(AxisDirection.POSITIVE, state.getValue(HORIZONTAL_FACING).getClockWise()
			.getAxis()));
		return state;
	}

	@Override
	public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
		withBlockEntityDo(pLevel, pPos, SolarPanelBlockEntity::determineAndApplySunlightScore);
	}


	@Override
	public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
		return state.getValue(HORIZONTAL_FACING)
			.getAxis() == face.getAxis();
	}


	@Override
	public float getParticleTargetRadius() {
		return 1.125f;
	}

	@Override
	public float getParticleInitialRadius() {
		return 1f;
	}

	@Override
	public boolean hideStressImpact() {
		return true;
	}

	@Override
	public Class<SolarPanelBlockEntity> getBlockEntityClass() {
		return SolarPanelBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends SolarPanelBlockEntity> getBlockEntityType() {
		return NorthstarBlockEntityTypes.SOLAR_PANEL.get();
	}
	
	public static Couple<Integer> getSpeedRange() {
		return Couple.create(8, 8);
	}

}
