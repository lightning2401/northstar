package com.lightning.northstar.block.tech.astronomy_table;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class AstronomyTableBlockEntity extends BlockEntity implements MenuProvider  {
	
	protected final ContainerData data;
	
    private final ItemStackHandler itemHandler = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };
    
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
	

	public AstronomyTableBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
		super(pType, pPos, pBlockState);
        this.data = new ContainerData() {

			@Override
			public int get(int pIndex) {
				return 0;
			}

			@Override
			public void set(int pIndex, int pValue) {
				return;
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return 0;
			}
			;};
	}

	@Override
	public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
		return new AstronomyTableMenu(pContainerId, pPlayerInventory,  this.data);
	}

	@Override
	public Component getDisplayName() {
		return Component.literal("Astronomy Table");
	}

}
