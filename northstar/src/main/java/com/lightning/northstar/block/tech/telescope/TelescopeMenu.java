package com.lightning.northstar.block.tech.telescope;

import com.lightning.northstar.NorthstarMenuTypes;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

@SuppressWarnings({"unused", "removal"})
public class TelescopeMenu extends AbstractContainerMenu {
	 public final TelescopeBlockEntity blockEntity;
	 private final Level level;
	private ContainerData data = null;
	 public Inventory inv;
	 
	 public TelescopeMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
	    this(id, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(2));
	 }
	 
	public TelescopeMenu(int id, Inventory inv, BlockEntity entity, ContainerData data) {
		 super(NorthstarMenuTypes.TELESCOPE_MENU.get(), id);
	     checkContainerSize(inv, 3);
	     blockEntity = (TelescopeBlockEntity) entity;
	     this.level = inv.player.level();
	     this.data = data;
	     this.inv = inv;

         this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
         this.addSlot(new SlotItemHandler(handler, 0, 12, 15));
         this.addSlot(new SlotItemHandler(handler, 1, 86, 15));
         this.addSlot(new SlotItemHandler(handler, 2, 86, 60));
         });
         addDataSlots(data);
   }

	@Override
	public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean stillValid(Player pPlayer) {
		// TODO Auto-generated method stub
		return true;
	}


}
