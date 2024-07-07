package com.lightning.northstar.block.tech.jet_engine;

import java.util.List;

import com.lightning.northstar.particle.ColdAirParticleData;
import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.IMultiBlockEntityContainer;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.VecHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class JetEngineBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation, IMultiBlockEntityContainer{
	
	private static final int MAX_SIZE = 5;

	protected LazyOptional<IFluidHandler> fluidCapability;
	protected boolean forceFluidLevelUpdate;
	protected FluidTank tankInventory;
	protected BlockPos controller;
	protected BlockPos lastKnownPos;
	protected boolean window;
	protected int luminosity;
	protected int width;
	protected int height;

	protected int syncCooldown;
	protected boolean queuedSync;

	// For rendering purposes only
	
	protected boolean updateConnectivity;

	public JetEngineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		updateConnectivity = false;
		height = 1;
		width = 1;
	}
	
	protected void updateConnectivity() {
		updateConnectivity = false;
		if (level.isClientSide)
			return;
		ConnectivityHandler.formMulti(this);
	}

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
		// TODO Auto-generated method stub
		
	}
	
	public Boolean getBottom() {
		return this.getBlockState().getValue(JetEngineBlock.BOTTOM);
	}
	
	@Override
	public void tick() {
		if (updateConnectivity)
			updateConnectivity();
		if (level.isClientSide) {
			if (!isVirtual() && this.getBlockState().getValue(JetEngineBlock.BOTTOM))
				spawnParticles(1);
			return;
		}
		if (lastKnownPos == null)
			lastKnownPos = getBlockPos();
		else if (!lastKnownPos.equals(worldPosition) && worldPosition != null) {
			onPositionChanged();
			return;
		}
	}
	protected void spawnParticles(double burstMult) {
		if (level == null)
			return;
		boolean empty = level.getBlockState(worldPosition.above())
				.getCollisionShape(level, worldPosition.above())
				.isEmpty();
		RandomSource r = level.getRandom();

		Vec3 c = VecHelper.getCenterOf(worldPosition);
		Vec3 v = c.add(VecHelper.offsetRandomly(Vec3.ZERO, r, .125f)
			.multiply(1, 0, 1));
			

		double yMotion = empty ? .0125f : r.nextDouble() * .0125f;
		if (r.nextInt(2) == 0)
		level.addParticle(new ColdAirParticleData(), v.x, v.y, v.z, 0, yMotion, 0);
		
	}

	@Override
	public BlockPos getController() {
		return isController() ? worldPosition : controller;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JetEngineBlockEntity getControllerBE() {
		if (isController())
			return this;
		BlockEntity blockEntity = level.getBlockEntity(controller);
		if (blockEntity instanceof JetEngineBlockEntity)
			return (JetEngineBlockEntity) blockEntity;
		return null;
	}

	@Override
	public boolean isController() {
		return controller == null || worldPosition.getX() == controller.getX()
			&& worldPosition.getY() == controller.getY() && worldPosition.getZ() == controller.getZ();
	}

	@Override
	public void setController(BlockPos controller) {
		if (level.isClientSide && !isVirtual())
			return;
		if (controller.equals(this.controller))
			return;
		this.controller = controller;
		setChanged();
		sendData();
	}

	public void removeController(boolean keepFluids) {
		if (level.isClientSide)
			return;
		updateConnectivity = true;
		controller = null;
		width = 1;
		height = 1;

		BlockState state = getBlockState();
		if (JetEngineBlock.isTank(state)) {
			state = state.setValue(JetEngineBlock.BOTTOM, true);
			state = state.setValue(JetEngineBlock.TOP, true);
			getLevel().setBlock(worldPosition, state, 22);
		}
		setChanged();
		sendData();
	}

	@Override
	public BlockPos getLastKnownPos() {
		return lastKnownPos;
	}

	@Override
	public void preventConnectivityUpdate() {
		updateConnectivity = false;
	}
	
	private void onPositionChanged() {
		removeController(true);
		lastKnownPos = worldPosition;
	}

	@Override
	public void notifyMultiUpdated() {
		BlockState state = this.getBlockState();
		if (JetEngineBlock.isTank(state)) { // safety
			state = state.setValue(JetEngineBlock.BOTTOM, getController().getY() == getBlockPos().getY());
			state = state.setValue(JetEngineBlock.TOP, getController().getY() + height - 1 == getBlockPos().getY());
			level.setBlock(getBlockPos(), state, 6);
		}
		setChanged();
	}
	@Override
	public int getMaxLength(Direction.Axis longAxis, int width) {
		if (longAxis == Direction.Axis.Y)
			return getMaxHeight();
		return getMaxWidth();
	}
	
	public static int getMaxHeight() {
		return 32;
	}

	@Override
	public int getMaxWidth() {
		return MAX_SIZE;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public void setWidth(int width) {
		this.width = width;
	}

	public boolean hasTank() {
		return true;
	}

	@Override
	public Direction.Axis getMainConnectionAxis() {
		return Direction.Axis.Y;
	}


}
