package com.lightning.northstar.block.tech.oxygen_concentrator;

import javax.annotation.Nullable;

import com.lightning.northstar.NorthstarMenuTypes;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class OxygenConcentratorMenu extends AbstractContainerMenu {
	public OxygenConcentratorBlockEntity blockEntity;
	public Container container;
	 
	protected final ContainerLevelAccess access;
	protected final Player player;

	public OxygenConcentratorMenu(int id, Inventory inv, FriendlyByteBuf thingo) {
		this(NorthstarMenuTypes.OXYGEN_CONCENTRATOR.get(), id, inv.player.level.getBlockEntity(thingo.readBlockPos()), inv, ContainerLevelAccess.NULL, new SimpleContainer(1));
	}		   
		   
    public OxygenConcentratorMenu(int id, Inventory inv, OxygenConcentratorBlockEntity entity) {
    	this(NorthstarMenuTypes.OXYGEN_CONCENTRATOR.get(), id, entity, inv, ContainerLevelAccess.NULL, new SimpleContainer(1));

//    	this(NorthstarMenuTypes.OXYGEN_CONCENTRATOR.get(), id, entity, inv, ContainerLevelAccess.NULL, entity.container);
    }
	 public OxygenConcentratorMenu(@Nullable MenuType<?> pType, int pContainerId,BlockEntity entity, Inventory pPlayerInventory, ContainerLevelAccess pAccess, Container container) {
	      super(pType, pContainerId);
	      this.access = pAccess;
	      this.player = pPlayerInventory.player;
	      this.blockEntity = (OxygenConcentratorBlockEntity) entity;
	      this.container = container;

	      this.addSlot(new Slot(container, 0, 24, 47));

	      for(int i = 0; i < 3; ++i) {
	         for(int j = 0; j < 9; ++j) {
	            this.addSlot(new Slot(pPlayerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
	         }
	      }

	      for(int k = 0; k < 9; ++k) {
	         this.addSlot(new Slot(pPlayerInventory, k, 8 + k * 18, 142));
	      }
	 }


	 @Override
	 public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
		 ItemStack itemstack = ItemStack.EMPTY;
		 return itemstack;
	 }
//	 protected void onTake(Player p_150474_, ItemStack p_150475_) {
//	      this.blockEntity.container.setItem(0, ItemStack.EMPTY);}
	 protected boolean isValidBlock(BlockState pState) {
		 return false;
	 }
		
	 protected boolean shouldQuickMoveToAdditionalSlot(ItemStack pStack) {
		 return false;
	 }
		
	 public boolean stillValid(Player pPlayer) {
		 return this.access.evaluate((p_39785_, p_39786_) -> {
			 return !this.isValidBlock(p_39785_.getBlockState(p_39786_)) ? false : pPlayer.distanceToSqr((double)p_39786_.getX() + 0.5D, (double)p_39786_.getY() + 0.5D, (double)p_39786_.getZ() + 0.5D) <= 64.0D;
		 }, true);
	 }
	

}
