package com.lightning.northstar.block.tech.electrolysis_machine;

import java.util.List;

import com.lightning.northstar.fluids.NorthstarFluids;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour.TankSegment;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

@SuppressWarnings("removal")
public class ElectrolysisMachineBlockEntity extends KineticBlockEntity implements IHaveGoggleInformation {

	public int tickCounter;
	public SmartFluidTankBehaviour inputTank;
	protected SmartFluidTankBehaviour outputTankL;
	protected SmartFluidTankBehaviour outputTankR;

	public ElectrolysisMachineBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
		super(typeIn, pos, state);
	}
	
	
	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
		inputTank = new SmartFluidTankBehaviour(SmartFluidTankBehaviour.INPUT, this, 2, 1000, true);
		outputTankL = new SmartFluidTankBehaviour(SmartFluidTankBehaviour.OUTPUT, this, 2, 1000, true)
				.forbidInsertion();
		outputTankR = new SmartFluidTankBehaviour(SmartFluidTankBehaviour.OUTPUT, this, 2, 1000, true)
				.forbidInsertion();
		behaviours.add(inputTank);
		behaviours.add(outputTankL);
		behaviours.add(outputTankR);
	}	
	
	@Override
	public void tick() {
		super.tick();
		tickCounter++;
		float thing = 257 - Math.abs(speed);
		if(tickCounter % thing == 0 && speed != 0) {
			if(inputTank.getPrimaryHandler().getFluid().getFluid() == Fluids.WATER.getSource()) {
				if(outputTankR.getPrimaryHandler().getFluidAmount() <= 998 && outputTankR.getPrimaryHandler().getFluidAmount() <= 993 && inputTank.getPrimaryHandler().getFluidAmount() >= 10) {
					inputTank.getPrimaryHandler().drain(new FluidStack(Fluids.WATER.getSource(), 10), FluidAction.EXECUTE);
					outputTankL.getPrimaryHandler().fill(new FluidStack(NorthstarFluids.HYDROGEN.get(), 2), FluidAction.EXECUTE);
					outputTankR.getPrimaryHandler().fill(new FluidStack(NorthstarFluids.OXYGEN.get(), 7), FluidAction.EXECUTE);
				}
			}
		}
	}
	
	
	@Override
	protected void write(CompoundTag compound, boolean clientPacket) {
		super.write(compound, clientPacket);
		compound.putInt("tickCounter", tickCounter);
	}
	@Override
	protected void read(CompoundTag compound, boolean clientPacket) {
		super.read(compound, clientPacket);
		tickCounter = compound.getInt("tickCounter");
	}
	@Override
	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		LangBuilder mb = Lang.translate("generic.unit.millibuckets");
		Lang.translate("gui.goggles.electrolysis_machine")
			.forGoggles(tooltip);
		FluidStack fluidStack = inputTank.getPrimaryHandler().getFluidInTank(0);
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
			.add(Lang.number(inputTank.getPrimaryHandler().getTankCapacity(0))
				.add(mb)
				.style(ChatFormatting.DARK_GRAY))
			.forGoggles(tooltip, 1);

		LangBuilder mbR = Lang.translate("generic.unit.millibuckets");
		FluidStack fluidStackR = outputTankR.getPrimaryHandler().getFluidInTank(0);
		if(!fluidStackR.getFluid().getFluidType().isAir()) {
		Lang.translate("gui.goggles.electrolysis_blue_port")
			.add(Lang.fluidName(fluidStackR))
			.style(ChatFormatting.GRAY)
			.forGoggles(tooltip);
		}else {
			Lang.translate("gui.goggles.electrolysis_blue_port")
			.add(Lang.translate("gui.goggles.empty"))
			.style(ChatFormatting.GRAY)
			.forGoggles(tooltip);
		}
		Lang.builder()
			.add(Lang.number(fluidStackR.getAmount())
				.add(mbR)
				.style(ChatFormatting.GOLD))
			.text(ChatFormatting.GRAY, " / ")
			.add(Lang.number(outputTankR.getPrimaryHandler().getTankCapacity(0))
				.add(mbR)
				.style(ChatFormatting.DARK_GRAY))
			.forGoggles(tooltip, 1);
		
		LangBuilder mbL = Lang.translate("generic.unit.millibuckets");
		FluidStack fluidStackL = outputTankL.getPrimaryHandler().getFluidInTank(0);
		if(!fluidStackL.getFluid().getFluidType().isAir()) {
		Lang.translate("gui.goggles.electrolysis_orange_port")
			.add(Lang.fluidName(fluidStackL))
			.style(ChatFormatting.GRAY)
			.forGoggles(tooltip);
		}else {
			Lang.translate("gui.goggles.electrolysis_orange_port")
			.add(Lang.translate("gui.goggles.empty"))
			.style(ChatFormatting.GRAY)
			.forGoggles(tooltip);
		}
		Lang.builder()
			.add(Lang.number(fluidStackL.getAmount())
				.add(mbL)
				.style(ChatFormatting.GOLD))
			.text(ChatFormatting.GRAY, " / ")
			.add(Lang.number(outputTankL.getPrimaryHandler().getTankCapacity(0))
				.add(mbL)
				.style(ChatFormatting.DARK_GRAY))
			.forGoggles(tooltip, 1);
		return true;
	}
	public float getTotalFluidUnits(float partialTicks) {
		int renderedFluids = 0;
		float totalUnits = 0;

		SmartFluidTankBehaviour behaviour = inputTank;
			if (behaviour == null)
				return 0;
			for (TankSegment tankSegment : behaviour.getTanks()) {
				if (tankSegment.getRenderedFluid()
					.isEmpty())
					continue;
				float units = tankSegment.getTotalUnits(partialTicks);
				if (units < 1)
					continue;
				totalUnits += units;
				renderedFluids++;
			}
		

		if (renderedFluids == 0)
			return 0;
		if (totalUnits < 1)
			return 0;
		return totalUnits;
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == ForgeCapabilities.FLUID_HANDLER && side == Direction.UP)
			return inputTank.getCapability()
				.cast();
		if (cap == ForgeCapabilities.FLUID_HANDLER && side == getBlockState().getValue(ElectrolysisMachineBlock.HORIZONTAL_FACING).getClockWise())
			return outputTankL.getCapability()
				.cast();
		if (cap == ForgeCapabilities.FLUID_HANDLER && side == getBlockState().getValue(ElectrolysisMachineBlock.HORIZONTAL_FACING).getCounterClockWise())
			return outputTankR.getCapability()
				.cast();
		inputTank.getCapability().cast();
		outputTankL.getCapability().cast();
		outputTankR.getCapability().cast();
		return super.getCapability(cap, side);
	}

}