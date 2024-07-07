package com.lightning.northstar.block;

import net.minecraft.world.level.block.Block;

public class LaunchPadBlock extends Block{

	public LaunchPadBlock(Properties pProperties) {
		super(pProperties);
		registerDefaultState(defaultBlockState());
	}

}
