package com.lightning.northstar.block.tech.oxygen_filler;

import java.util.List;

import com.lightning.northstar.NorthstarTags;
import com.lightning.northstar.NorthstarTags.NorthstarItemTags;
import com.lightning.northstar.fluids.NorthstarFluids;
import com.lightning.northstar.sound.NorthstarSounds;
import com.lightning.northstar.world.OxygenStuff;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.particle.AirParticleData;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import com.simibubi.create.foundation.utility.VecHelper;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

@SuppressWarnings("removal")
public class OxygenFillerBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {
	
	public int airLevel;
	public int airTimer;
	private boolean hasStopped = false;
	private int audioTick = 0;
	

	protected LazyOptional<IItemHandlerModifiable> itemCapability;
	SmartFluidTankBehaviour tank;
	public Container container = new SimpleContainer(1) {
	      public void setChanged() {
		         super.setChanged();
	//	         OxygenFillerBlockEntity.this.slotsChanged(this);
	      }
	};
	protected ItemStackHandler inventory;

	public OxygenFillerBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
		super(typeIn, pos, state);
		inventory = new ItemStackHandler();
		itemCapability = LazyOptional.of(() -> new CombinedInvWrapper(inventory));
	}
	
	
	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
		tank = SmartFluidTankBehaviour.single(this, 10000);
		behaviours.add(tank);
	}
	
	public ItemStackHandler getInventoryOfBlock() {
		return inventory;
	}
	
	
	 public void slotsChanged(Container pInventory) {
		 if (pInventory == this.container) {
			 ItemStack item = container.getItem(0);
			 if (container.getItem(0).is(NorthstarItemTags.OXYGEN_SOURCES.tag)) {
				 CompoundTag thing = item.getTag();
				 ListTag lore = new ListTag();
				 int currentOxy = thing.getInt("Oxygen");
				 System.out.println(currentOxy);
				 if(currentOxy < OxygenStuff.maximumOxy) {
					 int oxytarget = OxygenStuff.maximumOxy - currentOxy;
					 int newoxy = currentOxy;
					 if(this.tank.getPrimaryHandler().getFluidAmount() > oxytarget) {
						 newoxy += oxytarget;
						 System.out.println("oxytarget " + oxytarget);
						 thing.putInt("Oxygen", newoxy);
						 lore.add(StringTag.valueOf(Component.Serializer.toJson(Component.literal( "Oxygen: " + newoxy + "mb").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY).withItalic(false))).toString())); 
						 item.getOrCreateTagElement("display").put("Lore", lore);
						 item.setTag(thing);
						 System.out.println(thing);
						 this.tank.getPrimaryHandler().drain(
						 new FluidStack(NorthstarFluids.OXYGEN.get(), oxytarget), FluidAction.EXECUTE);
						 return;
					 }else
					 {
						 newoxy += this.tank.getPrimaryHandler().getFluidAmount();
						 System.out.println("oxytarget " + oxytarget);
						 thing.putInt("Oxygen", newoxy);
						 lore.add(StringTag.valueOf(Component.Serializer.toJson(Component.literal( "Oxygen: " + newoxy + "mb").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY).withItalic(false))).toString())); 
						 item.getOrCreateTagElement("display").put("Lore", lore);
						 item.setTag(thing);
						 System.out.println(thing);
						 this.tank.getPrimaryHandler().drain(
						 new FluidStack(NorthstarFluids.OXYGEN.get(), this.tank.getPrimaryHandler().getFluidAmount()), FluidAction.EXECUTE);
						 return;
					 }
				 }
			 }
		 }
	 }
	
	@Override
	@SuppressWarnings("deprecation")
	public void tick() {
		super.tick();
		ItemStack item = container.getItem(0);
		Fluid tankFluid = tank.getPrimaryHandler().getFluid().getFluid();
		int increment = 2;
		 if (item.is(NorthstarItemTags.OXYGEN_SOURCES.tag) && (tankFluid.is(NorthstarTags.NorthstarFluidTags.IS_OXY.tag) || tankFluid.isSame(NorthstarFluids.OXYGEN.get()))) {
			 System.out.println("I DONT KNOW IF THSI IS WORKING!!!");
			 CompoundTag thing = item.getTag();
			 if (thing == null)
				 return;
			 ListTag lore = new ListTag();
			 int currentOxy = thing.getInt("Oxygen");
			 while(currentOxy + increment > OxygenStuff.maximumOxy) {
				 increment--;
			 }
			 increment = increment > 0 ? increment : 0;
			 if(increment == 0 && !hasStopped) 
			 {	
				 AllSoundEvents.CONFIRM.playAt(level, worldPosition, 0.4f, 0, true);
				 System.out.println("Big Bazongos!!!!! Balls"); hasStopped = true;
			 }
			 else if(increment != 0) {
				hasStopped = false;
			 	audioTick++;
			 	System.out.println("audioTick: " +  audioTick);
				 if(level.isClientSide) {
					 if(audioTick % 13 == 0) {
						 System.out.println("Big Bazinga is now REal");
						 BlockPos pos = this.getBlockPos();
						 level.playLocalSound(pos.getX(),pos.getY(),pos.getZ(), NorthstarSounds.AIRFLOW.get(), SoundSource.BLOCKS, 0.5f, 0, false);
					 }					 
						Vec3 centerOf = VecHelper.getCenterOf(worldPosition);
						Vec3 v = VecHelper.offsetRandomly(centerOf, level.random, .65f);
						Vec3 m = centerOf.subtract(v);
						if(level.random.nextBoolean())
							level.addParticle(new AirParticleData(1, .05f), v.x, v.y, v.z, m.x, m.y, m.z);
					 }
			 }
			 int newOxyAmount =  Mth.clamp(increment, 0, tank.getPrimaryHandler().getFluidAmount());
			 int newoxy = currentOxy + newOxyAmount;
			 thing.putInt("Oxygen", newoxy);
			 lore.add(StringTag.valueOf(Component.Serializer.toJson(Component.literal( "Oxygen: " + newoxy + "mb").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY).withItalic(false))).toString())); 
			 item.getOrCreateTagElement("display").put("Lore", lore);
			 item.setTag(thing);
			 this.tank.getPrimaryHandler().drain(new FluidStack(tankFluid,newOxyAmount), FluidAction.EXECUTE);
		 }
		
//		if (getSpeed() == 0)
//			return;		 
//		float abs = Math.abs(getSpeed());
//		int increment = Mth.clamp(((int) abs - 100) / 200, 1, 5);
//		airLevel = Math.min(500, airLevel + increment);
//		tank.getPrimaryHandler().fill(new FluidStack(NorthstarFluids.OXYGEN.get(), increment), FluidAction.EXECUTE);
	}
	
	
	@Override
	protected void write(CompoundTag compound, boolean clientPacket) {
		super.write(compound, clientPacket);
		compound.putInt("Air", airLevel);
		compound.putInt("Timer", airTimer);
		compound.put("item", this.container.getItem(0).save(new CompoundTag()));
	}
	@Override
	public void writeSafe(CompoundTag compound) {
		super.writeSafe(compound);
		compound.put("item", this.container.getItem(0).save(new CompoundTag()));
	}
	@Override
	protected void read(CompoundTag compound, boolean clientPacket) {
		super.read(compound, clientPacket);
		airLevel = compound.getInt("Air");
		airTimer = compound.getInt("Timer");
		this.container.setItem(0, ItemStack.of(compound.getCompound("item")));
	}
	@Override
	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		LangBuilder mb = Lang.translate("generic.unit.millibuckets");
		Lang.translate("gui.goggles.oxygen_filler")
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
		ItemStack itemStack = container.getItem(0);
		if(!itemStack.isEmpty()) {
			Lang.builder()
				.add(Lang.number(itemStack.getCount())
				.style(ChatFormatting.GRAY))
				.text(ChatFormatting.DARK_GRAY, "x ")
				.add(Lang.itemName(itemStack)
				.style(ChatFormatting.GRAY))
			.forGoggles(tooltip, 1);
			CompoundTag thing = itemStack.getTag();
			int currentOxy = thing.getInt("Oxygen");
			if(currentOxy != 0) {
				Lang.builder()
				.add(Lang.number(currentOxy)
				.style(ChatFormatting.GRAY))
				.add(mb)
				.style(ChatFormatting.GRAY)
				.forGoggles(tooltip, 2);
			}
		}
		return true;
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && side == getBlockState().getValue(OxygenFillerBlock.HORIZONTAL_FACING).getOpposite())
			return tank.getCapability()
				.cast();
		tank.getCapability().cast();
		return super.getCapability(cap, side);
	}

}
