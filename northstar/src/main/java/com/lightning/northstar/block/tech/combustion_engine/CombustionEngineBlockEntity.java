package com.lightning.northstar.block.tech.combustion_engine;

import java.util.List;

import com.lightning.northstar.NorthstarTags;
import com.lightning.northstar.block.tech.oxygen_concentrator.OxygenConcentratorBlock;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class CombustionEngineBlockEntity extends GeneratingKineticBlockEntity implements IHaveGoggleInformation  {
	
	SmartFluidTankBehaviour tank;
	boolean powered = false;
	int powerLevel = 0;

	public CombustionEngineBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
		super(typeIn, pos, state);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void tick() {
		super.tick();
		int increment = 0;
		powered = false;
		Fluid fluid = tank.getPrimaryHandler().getFluid().getFluid();
		int fluidamount = tank.getPrimaryHandler().getFluidAmount();
		if(fluid.is(NorthstarTags.NorthstarFluidTags.TIER_1_ROCKET_FUEL.tag)) {
			if(fluidamount > 4) {
				increment = 4;
			}else {increment = fluidamount;}
			powered = true;
			powerLevel = 4;
		}else if(fluid.is(NorthstarTags.NorthstarFluidTags.TIER_2_ROCKET_FUEL.tag)){
			if(fluidamount > 3) {
				increment = 3;
			}else {increment = fluidamount;}
			powered = true;
			powerLevel = 6;
		}else if(fluid.is(NorthstarTags.NorthstarFluidTags.TIER_3_ROCKET_FUEL.tag)) {
			if(fluidamount > 2) {
				increment = 2;
			}else {increment = fluidamount;}
			powered = true;
			powerLevel = 8;
		}
		tank.getPrimaryHandler().drain(increment, FluidAction.EXECUTE);
		this.updateGeneratedRotation();
	}
	
	@Override
	public float getGeneratedSpeed() {
		return (powered ? 1 : 0) * 128 * powerLevel / 8;
	}
	
	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
		tank = SmartFluidTankBehaviour.single(this, 10000);
		behaviours.add(tank);
	}
	
	@SuppressWarnings("removal")
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && side == getBlockState().getValue(OxygenConcentratorBlock.HORIZONTAL_FACING))
			return tank.getCapability()
				.cast();
		tank.getCapability().cast();
		return super.getCapability(cap, side);
	}
	
	@Override
	public void write(CompoundTag tag, boolean clientPacket) {
		super.write(tag, clientPacket);
	}
	
	@Override
	protected void read(CompoundTag tag, boolean clientPacket) {		
		super.read(tag, clientPacket);
	}
	@Override
	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		LangBuilder mb = Lang.translate("generic.unit.millibuckets");
		Lang.translate("gui.goggles.combustion_engine")
			.forGoggles(tooltip);
		FluidStack fluidStack = tank.getPrimaryHandler().getFluidInTank(0);
		if(!fluidStack.getFluid().getFluidType().isAir()) {
		Lang.fluidName(fluidStack)
			.style(ChatFormatting.GRAY)
			.forGoggles(tooltip);
		}else {
			Lang.translate("gui.goggles.empty")
			.style(ChatFormatting.GRAY)
			.forGoggles(tooltip);
		}
		Lang.builder()
			.add(Lang.number(fluidStack.getAmount())
				.add(mb)
				.style(ChatFormatting.GOLD))
			.text(ChatFormatting.GRAY, " / ")
			.add(Lang.number(tank.getPrimaryHandler().getTankCapacity(0))
				.add(mb)
				.style(ChatFormatting.DARK_GRAY))
			.forGoggles(tooltip, 1);
		
		float stressBase = calculateAddedStressCapacity();
		float speed = getTheoreticalSpeed();
		if (speed != getGeneratedSpeed() && speed != 0)
			stressBase *= getGeneratedSpeed() / speed;
		speed = Math.abs(speed);

		float stressTotal = stressBase * speed;
		
		Lang.translate("tooltip.capacityProvided")
		.style(ChatFormatting.GRAY)
		.forGoggles(tooltip);

		Lang.number(stressTotal)
			.translate("generic.unit.stress")
			.style(ChatFormatting.AQUA)
			.space()
			.add(Lang.translate("gui.goggles.at_current_speed")
				.style(ChatFormatting.DARK_GRAY))
			.forGoggles(tooltip, 1);
		return true;
	}
}
