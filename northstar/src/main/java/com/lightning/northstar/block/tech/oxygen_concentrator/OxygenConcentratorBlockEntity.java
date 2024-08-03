package com.lightning.northstar.block.tech.oxygen_concentrator;

import java.util.List;

import com.lightning.northstar.fluids.NorthstarFluids;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

@SuppressWarnings("removal")
public class OxygenConcentratorBlockEntity  extends KineticBlockEntity implements IHaveGoggleInformation {
	
	public int airLevel;
	public int airTimer;
	
	SmartFluidTankBehaviour tank;
//	public Container container = new SimpleContainer(1) {
//	      public void setChanged() {
//		         super.setChanged();
//		         OxygenConcentratorBlockEntity.this.slotsChanged(this);
////	      }
//	};
//	protected ItemStackHandler inventory;

	public OxygenConcentratorBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
		super(typeIn, pos, state);
	}
	
	
	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
		tank = SmartFluidTankBehaviour.single(this, 10000);
		behaviours.add(tank);
	}

//	 public void slotsChanged(Container pInventory) {
//		 if (pInventory == this.container) {
//			 ItemStack item = container.getItem(0);
//			 if (container.getItem(0).is(NorthstarItemTags.OXYGEN_SOURCES.tag)) {
//				 CompoundTag thing = item.getTag();
//				 ListTag lore = new ListTag();
//				 int currentOxy = thing.getInt("Oxygen");
//				 System.out.println(currentOxy);
//				 if(currentOxy < OxygenStuff.maximumOxy) {
//					 int oxytarget = OxygenStuff.maximumOxy - currentOxy;
//					 int newoxy = currentOxy;
//					 if(this.tank.getPrimaryHandler().getFluidAmount() > oxytarget) {
//						 newoxy += oxytarget;
//						 System.out.println("oxytarget " + oxytarget);
//						 thing.putInt("Oxygen", newoxy);
//						 lore.add(StringTag.valueOf(Component.Serializer.toJson(Component.literal( "Oxygen: " + newoxy + "mb").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY).withItalic(false))).toString())); 
//						 item.getOrCreateTagElement("display").put("Lore", lore);
//						 item.setTag(thing);
//						 System.out.println(thing);
//						 this.tank.getPrimaryHandler().drain(
//						 new FluidStack(NorthstarFluids.OXYGEN.get(), oxytarget), FluidAction.EXECUTE);
//						 return;
//					 }else
//					 {
//						 newoxy += this.tank.getPrimaryHandler().getFluidAmount();
//						 System.out.println("oxytarget " + oxytarget);
//						 thing.putInt("Oxygen", newoxy);
//						 lore.add(StringTag.valueOf(Component.Serializer.toJson(Component.literal( "Oxygen: " + newoxy + "mb").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY).withItalic(false))).toString())); 
//						 item.getOrCreateTagElement("display").put("Lore", lore);
//						 item.setTag(thing);
//						 System.out.println(thing);
//						 this.tank.getPrimaryHandler().drain(
//						 new FluidStack(NorthstarFluids.OXYGEN.get(), this.tank.getPrimaryHandler().getFluidAmount()), FluidAction.EXECUTE);
//						 return;
//					 }


//				 }
//			 }
//		 }
//	 }
	
	@Override
	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		Lang.translate("gui.goggles.kinetic_stats")
		.forGoggles(tooltip);
		addStressImpactStats(tooltip, calculateStressApplied());
	
		LangBuilder mb = Lang.translate("generic.unit.millibuckets");
		Lang.translate("gui.goggles.oxygen_concentrator")
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
		return true;
	}
	
	@Override
	public void tick() {
		super.tick();
		if (getSpeed() == 0)
			return;		
		float abs = Math.abs(getSpeed());
		int increment = Mth.clamp(((int) abs - 100) / 200, 1, 5);
		airLevel = Math.min(500, airLevel + increment);
		tank.getPrimaryHandler().fill(new FluidStack(NorthstarFluids.OXYGEN.get(), increment), FluidAction.EXECUTE);
	}
	@Override
	protected void write(CompoundTag compound, boolean clientPacket) {
		super.write(compound, clientPacket);
		compound.putInt("Air", airLevel);
		compound.putInt("Timer", airTimer);
	}
	@Override
	public void writeSafe(CompoundTag compound) {
		super.writeSafe(compound);
	}
	@Override
	protected void read(CompoundTag compound, boolean clientPacket) {
		super.read(compound, clientPacket);
		airLevel = compound.getInt("Air");
		airTimer = compound.getInt("Timer");
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == ForgeCapabilities.FLUID_HANDLER && side == getBlockState().getValue(OxygenConcentratorBlock.HORIZONTAL_FACING).getOpposite())
			return tank.getCapability()
				.cast();
		tank.getCapability().cast();
		return super.getCapability(cap, side);
	}
}
