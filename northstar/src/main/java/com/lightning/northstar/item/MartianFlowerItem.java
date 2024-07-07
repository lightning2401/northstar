package com.lightning.northstar.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

public class MartianFlowerItem extends BlockItem{

	public MartianFlowerItem(Block pBlock, Properties pProperties) {
		super(pBlock, pProperties);
	}
	
	public String getDescriptionId() {
		return this.getOrCreateDescriptionId();
	}
}
