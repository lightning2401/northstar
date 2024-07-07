package com.lightning.northstar.block.tech.computer_rack;

import java.util.List;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TargetingComputerRackBlockEntity extends SmartBlockEntity{

	
	public Container container = new SimpleContainer(6);
	public TargetingComputerRackBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
		
	}
	@Override
	protected void read(CompoundTag compound, boolean clientPacket) {
		super.read(compound, clientPacket);
	    if (compound.contains("slot1", 10)) {
		container.setItem(0, ItemStack.of(compound.getCompound("slot1")));}
	    if (compound.contains("slot2", 10)) {
		container.setItem(1, ItemStack.of(compound.getCompound("slot2")));}
		if (compound.contains("slot3", 10)) {
		container.setItem(2, ItemStack.of(compound.getCompound("slot3")));}
		if (compound.contains("slot4", 10)) {
		container.setItem(3, ItemStack.of(compound.getCompound("slot4")));}
		if (compound.contains("slot5", 10)) {
		container.setItem(4, ItemStack.of(compound.getCompound("slot5")));}
		if (compound.contains("slot6", 10)) {
		container.setItem(5, ItemStack.of(compound.getCompound("slot6")));}
	}
	
	@Override
	public void write(CompoundTag compound, boolean clientPacket) {
		super.write(compound, clientPacket);
		compound.put("slot1", container.getItem(0).serializeNBT());
		compound.put("slot2", container.getItem(1).serializeNBT());
		compound.put("slot3", container.getItem(2).serializeNBT());
		compound.put("slot4", container.getItem(3).serializeNBT());
		compound.put("slot5", container.getItem(4).serializeNBT());
		compound.put("slot6", container.getItem(5).serializeNBT());
	}

}
