package com.lightning.northstar.block.tech.rocket_station;

import javax.annotation.Nullable;

import com.lightning.northstar.NorthstarMenuTypes;
import com.lightning.northstar.item.NorthstarItems;
import com.lightning.northstar.world.dimension.NorthstarPlanets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class RocketStationMenu extends AbstractContainerMenu  {
	public RocketStationBlockEntity blockEntity;
	public Container container;
	public Inventory inv;
	public boolean dataTooFar;
	public boolean differentPlanets;
	public int fuelCost;
	public ResourceKey<Level> target;
	 
	protected final ContainerLevelAccess access;
	protected final Player player;
	   
		   
	public RocketStationMenu(int id, Inventory inv, FriendlyByteBuf thingo) {
		this(NorthstarMenuTypes.ROCKET_STATION.get(), id, inv.player.level.getBlockEntity(thingo.readBlockPos()), inv, ContainerLevelAccess.NULL, new SimpleContainer(1));
	}		   
		   
    public RocketStationMenu(int id, Inventory inv, RocketStationBlockEntity entity) {
    	this(NorthstarMenuTypes.ROCKET_STATION.get(), id, entity, inv, ContainerLevelAccess.NULL, entity.container);
    }
	 public RocketStationMenu(@Nullable MenuType<?> pType, int pContainerId,BlockEntity entity, Inventory pPlayerInventory, ContainerLevelAccess pAccess, Container container) {
	      super(pType, pContainerId);
	      this.access = pAccess;
	      this.player = pPlayerInventory.player;
	      this.blockEntity = (RocketStationBlockEntity) entity;
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
	protected boolean mayPickup(Player pPlayer, boolean pHasStack) {
		ItemStack item1 = this.blockEntity.container.getItem(0);
		if(item1.getItem() == NorthstarItems.STAR_MAP.get()) {
			return true;
		}
		else return false;
	}
	
	public int fuelCalc() {
		String home = NorthstarPlanets.getPlanetName(this.blockEntity.getLevel().dimension());
		String targ = NorthstarPlanets.getPlanetName(target);
		
		int home_x = (int) NorthstarPlanets.getPlanetX(home);
		int home_y = (int) NorthstarPlanets.getPlanetY(home);
		
		int targ_x = (int) NorthstarPlanets.getPlanetX(targ);
		int targ_y = (int) NorthstarPlanets.getPlanetY(targ);
		
		int dif = (int) (Math.pow(home_x - targ_x, 2) + Math.pow(home_y - targ_y, 2));
		dif = Mth.roundToward(dif, 100) / 20;
		int cost = dif + NorthstarPlanets.getPlanetAtmosphereCost(this.blockEntity.getLevel().dimension()) + 1000;
		if (dif != 0) {
//			System.out.println(dif);
		}
		return cost * 8;
	}
	
	public void slotsChanged(Container pInventory) {
		ItemStack item = container.getItem(0);
		if (container.getItem(0).getItem() == NorthstarItems.STAR_MAP.get()) {
			if(item.getTagElement("Planet") != null)
			target = NorthstarPlanets.getPlanetDimension(NorthstarPlanets.targetGetter(item.getTagElement("Planet").toString()));
		}
		fuelCost = fuelCalc();
//		System.out.println(fuelCost);
//		System.out.println(target);
	}
	public void assemble() {
		if (blockEntity != null)
		{blockEntity.queueAssembly(this.player);blockEntity.tick(); System.out.println("AYYYYY");}
		else {System.out.println("UGHHH");}
		
		
	}	

	 protected void onTake(Player p_150474_, ItemStack p_150475_) {
	      this.blockEntity.container.setItem(0, ItemStack.EMPTY);}

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
	@Override
	public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
		ItemStack itemstack = ItemStack.EMPTY;
		return itemstack;
	}

}
