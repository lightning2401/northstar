package com.lightning.northstar.block.crops;

import com.lightning.northstar.item.NorthstarItems;

import net.minecraft.world.item.Item;

public class MarsPalmBlock extends MartianFlowerBlock {

	public MarsPalmBlock(Properties pProperties) {
		super(pProperties);
	}
	
	@Override
	public Item getSeedItem() {
		return NorthstarItems.MARS_PALM_SEEDS.get();
	}
	

}
