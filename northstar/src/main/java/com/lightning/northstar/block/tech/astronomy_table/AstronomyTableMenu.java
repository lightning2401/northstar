package com.lightning.northstar.block.tech.astronomy_table;

import javax.annotation.Nullable;

import com.lightning.northstar.NorthstarMenuTypes;
import com.lightning.northstar.item.NorthstarItems;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class AstronomyTableMenu extends AbstractContainerMenu  {
	 public Inventory inv;
	 public boolean dataTooFar;
	 public boolean differentPlanets;
	 
	   protected final ContainerLevelAccess access;
	   protected final Player player;
	   protected final ResultContainer resultSlots = new ResultContainer();
	   protected final Container inputSlots = new SimpleContainer(3) {
		      public void setChanged() {
		         super.setChanged();
		         AstronomyTableMenu.this.slotsChanged(this);
		      }
		   };
		   
		   
	 
    public AstronomyTableMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
    	this(NorthstarMenuTypes.ASTRONOMY_TABLE_MENU.get(), id, inv, ContainerLevelAccess.NULL);
    }
    public AstronomyTableMenu(int id, Inventory inv, ContainerData extraData) {
    	this(NorthstarMenuTypes.ASTRONOMY_TABLE_MENU.get(), id, inv, ContainerLevelAccess.NULL);
    }
	 public AstronomyTableMenu(@Nullable MenuType<?> pType, int pContainerId, Inventory pPlayerInventory, ContainerLevelAccess pAccess) {
	      super(pType, pContainerId);
	      this.access = pAccess;
	      this.player = pPlayerInventory.player;
	      this.addSlot(new Slot(this.inputSlots, 0, 24, 47));
	      this.addSlot(new Slot(this.inputSlots, 1, 80, 47));
	      this.addSlot(new Slot(this.inputSlots, 2, 52, 27));
	      this.addSlot(new Slot(this.resultSlots, 3, 134, 47) {
	         /**
	          * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
	          */
	         public boolean mayPlace(ItemStack p_39818_) {
	            return false;
	         }

	         /**
	          * Return whether this slot's stack can be taken from this slot.
	          */
	         public boolean mayPickup(Player p_39813_) {
	            return AstronomyTableMenu.this.mayPickup(p_39813_, this.hasItem());
	         }

	         public void onTake(Player p_150604_, ItemStack p_150605_) {
	        	 AstronomyTableMenu.this.onTake(p_150604_, p_150605_);
	         }
	      });

	      for(int i = 0; i < 3; ++i) {
	         for(int j = 0; j < 9; ++j) {
	            this.addSlot(new Slot(pPlayerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
	         }
	      }

	      for(int k = 0; k < 9; ++k) {
	         this.addSlot(new Slot(pPlayerInventory, k, 8 + k * 18, 142));
	      }

	   }
	protected boolean mayPickup(Player pPlayer, boolean pHasStack) {
		ItemStack item1 = this.inputSlots.getItem(0);
		ItemStack item2 = this.inputSlots.getItem(1);
		ItemStack item3 = this.inputSlots.getItem(2);
		if(item1.getItem() == NorthstarItems.ASTRONOMICAL_READING.get() 
				&& item2.getItem() == NorthstarItems.ASTRONOMICAL_READING.get() 
				&& item3.getItem() == NorthstarItems.ASTRONOMICAL_READING.get()) {
			return true;
		}
		else return false;
	}
	
	public void slotsChanged(Container pInventory) {
		super.slotsChanged(pInventory);
		if (pInventory == this.inputSlots) {
			this.createResult();
		}
		
	}

	 protected void onTake(Player p_150474_, ItemStack p_150475_) {
	      this.inputSlots.setItem(0, ItemStack.EMPTY);
	      this.inputSlots.setItem(1, ItemStack.EMPTY);
	      this.inputSlots.setItem(2, ItemStack.EMPTY);
	      access.execute((p_150479_, p_150480_) -> {
	    	  p_150479_.playSound((Player)null, p_150480_, SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F); });};

	protected boolean isValidBlock(BlockState pState) {
		return false;
	}

	public void createResult() {
		ItemStack item1 = this.inputSlots.getItem(0);
		ItemStack item2 = this.inputSlots.getItem(1);
		ItemStack item3 = this.inputSlots.getItem(2);
		dataTooFar = false;
		differentPlanets = false;
		if(item1 == null || item2 == null || item3 == null) {return;}
		boolean item_check = false;
		boolean name_check = false;
		CompoundTag item1_name =  item1.getTagElement("Planet");
		CompoundTag item2_name =  item2.getTagElement("Planet");
		CompoundTag item3_name =  item3.getTagElement("Planet");
		if(item1_name == null || item2_name == null || item3_name == null) {return;}
		String item1_final_name = "";
		String item2_final_name = "";
		String item3_final_name = "";
		item1_final_name = targetGetter(item1_name.toString());
		item2_final_name = targetGetter(item2_name.toString());
		item3_final_name = targetGetter(item3_name.toString());
		boolean flag = distanceFlag(item1,item2,item3);
		dataTooFar = !flag;
		if (!flag) {return;}
		if(item1.getItem() == NorthstarItems.ASTRONOMICAL_READING.get() 
				&& item2.getItem() == NorthstarItems.ASTRONOMICAL_READING.get() 
				&& item3.getItem() == NorthstarItems.ASTRONOMICAL_READING.get()) {
			item_check = true;
		}else {return;}

		if(item1_final_name == null || item2_final_name == null || item3_final_name == null) {
			return;
		}		
		if(item1_final_name.equals(item2_final_name) && item2_final_name.equals(item3_final_name)) {name_check = true;}
		else {differentPlanets = true;}
		
		if (item_check && name_check) {
			ItemStack result = new ItemStack(NorthstarItems.STAR_MAP.get());
			result.setHoverName(Component.translatable("item.northstar.star_map" + "_" + item1_final_name).setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA).withItalic(false)));
	        CompoundTag tag = result.getOrCreateTagElement("Planet");
	        tag.putString("name", item1_final_name);
	         this.resultSlots.setItem(0, result);
		}
		if (!item_check && !name_check) {
			this.resultSlots.clearContent();
		}
		
	}
	
	private String targetGetter(String thing) {
		String newthing = "";
		for(int i = 0;i < thing.length(); i++) {
			if(i > 6 && i < thing.length() - 2) {newthing += thing.charAt(i);}
		}
		return newthing;
	}
	private int valueGetter(String thing) {
		String newthing = "";
		for(int i = 0;i < thing.length(); i++) {
			if(i > 6 && i < thing.length() - 1) {newthing += thing.charAt(i);}
		}


		return Integer.parseInt(newthing);
	}
	
	private boolean distanceFlag(ItemStack item1, ItemStack item2, ItemStack item3) 
		{CompoundTag item1_x =  item1.getTagElement("planetX");
		CompoundTag item2_x =  item2.getTagElement("planetX");
		CompoundTag item3_x =  item3.getTagElement("planetX");
		CompoundTag item1_y =  item1.getTagElement("planetY");
		CompoundTag item2_y =  item2.getTagElement("planetY");
		CompoundTag item3_y =  item3.getTagElement("planetY");
		int i1X = valueGetter(item1_x.getAsString());
		int i1Y = valueGetter(item1_y.getAsString());
		int i2X = valueGetter(item2_x.getAsString());
		int i2Y = valueGetter(item2_y.getAsString());
		int i3X = valueGetter(item3_x.getAsString());
		int i3Y = valueGetter(item3_y.getAsString());
		double r1 = Math.sqrt(Math.pow((i2X - i1X), 2) + Math.pow((i2Y - i1Y), 2));
		double r2 = Math.sqrt(Math.pow((i3X - i2X), 2) + Math.pow((i3Y - i2Y), 2));
		double r3 = Math.sqrt(Math.pow((i1X - i3X), 2) + Math.pow((i1Y - i3Y), 2));
		double result = (r1 + r2 + r3) / 3;
		System.out.println(result);
		if (Math.abs(result) > 30) {return true;}
		return false;}
	
	
	@Override
	public void removed(Player pPlayer) {
		super.removed(pPlayer);
		this.resultSlots.removeItemNoUpdate(3);
		System.out.println("AAAAAAGGGGHHHH");
		this.clearContainer(pPlayer, this.inputSlots);
	}
	
	protected boolean shouldQuickMoveToAdditionalSlot(ItemStack pStack) {
		return false;
	}
	
	public boolean stillValid(Player pPlayer) {
		return this.access.evaluate((p_39785_, p_39786_) -> {
			return !this.isValidBlock(p_39785_.getBlockState(p_39786_)) ? false : pPlayer.distanceToSqr((double)p_39786_.getX() + 0.5D, (double)p_39786_.getY() + 0.5D, (double)p_39786_.getZ() + 0.5D) <= 64.0D;
		}, true);
	}
	@Override
	public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(pIndex);
		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (pIndex == 3) {
				if (!this.moveItemStackTo(itemstack1, 4, 40, true)) {
					return ItemStack.EMPTY;
				}

				slot.onQuickCraft(itemstack1, itemstack);
			} else if (pIndex != 0 && pIndex != 1 && pIndex != 2) {
				if (pIndex >= 4 && pIndex < 40) {
					int i = -1;
					if(this.slots.get(0).getItem() == ItemStack.EMPTY && i == -1) {i = 0;}
					if(this.slots.get(2).getItem() == ItemStack.EMPTY && i == -1) {i = 2;}
					if(this.slots.get(1).getItem() == ItemStack.EMPTY && i == -1) {i = 1;}
					if(i != -1) {
						if (!this.moveItemStackTo(itemstack1, i, 3, false)) {
							return ItemStack.EMPTY;
						}
					}
				}
	         } else if (!this.moveItemStackTo(itemstack1, 4, 40, false)) {
	            return ItemStack.EMPTY;
	         }

	         if (itemstack1.isEmpty()) {
	            slot.set(ItemStack.EMPTY);
	         } else {
	            slot.setChanged();
	         }

	         if (itemstack1.getCount() == itemstack.getCount()) {
	            return ItemStack.EMPTY;
	         }

	         slot.onTake(pPlayer, itemstack1);
	      }

	      return itemstack;
	   }

}
