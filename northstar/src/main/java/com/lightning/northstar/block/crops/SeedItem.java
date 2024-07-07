package com.lightning.northstar.block.crops;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class SeedItem extends ItemNameBlockItem {

	public SeedItem(Block pBlock, Properties pProperties) {
		super(pBlock, pProperties);
	}
	
	@Override
	public void fillItemCategory(CreativeModeTab pCategory, NonNullList<ItemStack> pItems) {
		if (this.allowedIn(pCategory)) {
			pItems.add(new ItemStack(this));
		}
	}

}
