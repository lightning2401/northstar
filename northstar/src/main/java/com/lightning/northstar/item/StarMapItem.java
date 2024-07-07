package com.lightning.northstar.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class StarMapItem extends Item {

	public StarMapItem(Properties pProperties) {
		super(pProperties);
	}

	public void fillItemCategory(CreativeModeTab pGroup, NonNullList<ItemStack> pItems) {
		if(this.allowedIn(pGroup)) {
			ItemStack earth = new ItemStack(NorthstarItems.STAR_MAP.get());
			earth.setHoverName(Component.translatable("item.northstar.star_map" + "_" + "earth").setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA).withItalic(false)));
	        CompoundTag earthTag = earth.getOrCreateTagElement("Planet");
	        earthTag.putString("name", "earth");
	        pItems.add(earth);
			ItemStack moon = new ItemStack(NorthstarItems.STAR_MAP.get());
			moon.setHoverName(Component.translatable("item.northstar.star_map" + "_" + "moon").setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA).withItalic(false)));
	        CompoundTag moonTag = moon.getOrCreateTagElement("Planet");
	        moonTag.putString("name", "moon");
	        pItems.add(moon);
			ItemStack mars = new ItemStack(NorthstarItems.STAR_MAP.get());
			mars.setHoverName(Component.translatable("item.northstar.star_map" + "_" + "mars").setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA).withItalic(false)));
	        CompoundTag marsTag = mars.getOrCreateTagElement("Planet");
	        marsTag.putString("name", "mars");
	        pItems.add(mars);
			ItemStack mercury = new ItemStack(NorthstarItems.STAR_MAP.get());
			mercury.setHoverName(Component.translatable("item.northstar.star_map" + "_" + "mercury").setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA).withItalic(false)));
	        CompoundTag mercuryTag = mercury.getOrCreateTagElement("Planet");
	        mercuryTag.putString("name", "mercury");
	        pItems.add(mercury);
			ItemStack venus = new ItemStack(NorthstarItems.STAR_MAP.get());
			venus.setHoverName(Component.translatable("item.northstar.star_map" + "_" + "venus").setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA).withItalic(false)));
	        CompoundTag venusTag = venus.getOrCreateTagElement("Planet");
	        venusTag.putString("name", "venus");
	        pItems.add(venus);
	        
			ItemStack jupiter = new ItemStack(NorthstarItems.STAR_MAP.get());
			jupiter.setHoverName(Component.translatable("item.northstar.star_map" + "_" + "jupiter").setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA).withItalic(false)));
	        CompoundTag jupiterTag = jupiter.getOrCreateTagElement("Planet");
	        jupiterTag.putString("name", "jupiter");
	        pItems.add(jupiter);
			ItemStack saturn = new ItemStack(NorthstarItems.STAR_MAP.get());
			saturn.setHoverName(Component.translatable("item.northstar.star_map" + "_" + "saturn").setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA).withItalic(false)));
	        CompoundTag saturnTag = saturn.getOrCreateTagElement("Planet");
	        saturnTag.putString("name", "saturn");
	        pItems.add(saturn);
			ItemStack uranus = new ItemStack(NorthstarItems.STAR_MAP.get());
			uranus.setHoverName(Component.translatable("item.northstar.star_map" + "_" + "uranus").setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA).withItalic(false)));
	        CompoundTag uranusTag = uranus.getOrCreateTagElement("Planet");
	        uranusTag.putString("name", "uranus");
	        pItems.add(uranus);
			ItemStack neptune = new ItemStack(NorthstarItems.STAR_MAP.get());
			neptune.setHoverName(Component.translatable("item.northstar.star_map" + "_" + "neptune").setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA).withItalic(false)));
	        CompoundTag neptuneTag = neptune.getOrCreateTagElement("Planet");
	        neptuneTag.putString("name", "neptune");
	        pItems.add(neptune);
		}
    }
}
