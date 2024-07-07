package com.lightning.northstar.block.crops;

import com.lightning.northstar.item.NorthstarItems;

import net.minecraft.world.item.Item;

public class MarsTulipBlock extends MartianFlowerBlock {

	public MarsTulipBlock(Properties pProperties) {
		super(pProperties);
	}
	
	@Override
	public Item getSeedItem() {
		return NorthstarItems.MARS_TULIP_SEEDS.get();
	}
	

}
