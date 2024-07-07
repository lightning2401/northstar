package com.lightning.northstar.item;

import com.lightning.northstar.block.NorthstarBlocks;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class NorthstarCreativeModeTab {
		public static final CreativeModeTab NORTHSTAR_TAB = new CreativeModeTab("northstartab") {
			
			@Override
			public ItemStack makeIcon() {
				// TODO Auto-generated method stub
				return new ItemStack(NorthstarItems.MARTIAN_STEEL.get());
			}
		};
		public static final CreativeModeTab NORTHSTAR_BLOCKS = new CreativeModeTab("northstarblocks") {
			
			@Override
			public ItemStack makeIcon() {
				// TODO Auto-generated method stub
				return new ItemStack(NorthstarBlocks.MARTIAN_STEEL_BLOCK.get());
			}
		};
		public static final CreativeModeTab NORTHSTAR_TECH = new CreativeModeTab("northstartech") {
			
			@Override
			public ItemStack makeIcon() {
				// TODO Auto-generated method stub
				return new ItemStack(NorthstarBlocks.TELESCOPE.get());
			}
		};
}
