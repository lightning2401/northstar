package com.lightning.northstar.block.tech.solar_panel;

import java.util.List;

import com.lightning.northstar.world.dimension.NorthstarPlanets;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class SolarPanelBlockEntity extends GeneratingKineticBlockEntity {
	
	public int sunlightScore;

	public SolarPanelBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		setLazyTickRate(60);
		updateGeneratedRotation();
		setChanged();
	}
	
	@Override
	public void tick() {
		super.tick();
		determineAndApplySunlightScore();
	}

	@Override
	public void lazyTick() {
		super.lazyTick();
		determineAndApplySunlightScore();
	}
	
	public void determineAndApplySunlightScore() {
		sunlightScore = getSunlightAtPosition(worldPosition);
		setSunlightScoreAndUpdate(sunlightScore);
	}
	
	public int getSunlightAtPosition(BlockPos pos) {
		if(level.isNight()){return 0;}
		if (level.getRawBrightness(pos.above(), -15) >= 16)
		return (int) (level.getRawBrightness(pos.above(), -15) * NorthstarPlanets.getSunMultiplier(level.dimension()));
		else return 0;
	}
	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
		super.addBehaviours(behaviours);
	}
	
	public void setSunlightScoreAndUpdate(int score) {
		sunlightScore = score;
		updateGeneratedRotation();
		setChanged();
	}
	
	@Override
	public float getGeneratedSpeed() {
		if(level == null)
		return Mth.clamp(sunlightScore, -1, 1) * 8 / getSize();
		else return (float) ((Mth.clamp(sunlightScore, -1, 1) * 8 / getSize()) * NorthstarPlanets.getSunMultiplier(level.dimension()));
	}
	@Override
	protected void read(CompoundTag compound, boolean clientPacket) {
		super.read(compound, clientPacket);
		sunlightScore = compound.getInt("sunlightScore");

	}

	@Override
	public void write(CompoundTag compound, boolean clientPacket) {
		super.write(compound, clientPacket);
		compound.putInt("sunlightScore", sunlightScore);
	}

	@Override
	protected AABB createRenderBoundingBox() {
		return new AABB(worldPosition).inflate(getSize());
	}
	
	protected int getSize() {
		return 1;
	}

}
