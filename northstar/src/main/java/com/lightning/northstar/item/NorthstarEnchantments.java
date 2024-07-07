package com.lightning.northstar.item;

import static com.lightning.northstar.Northstar.REGISTRATE;

import com.lightning.northstar.item.enchantments.FrostbiteEnchantment;
import com.tterrag.registrate.util.entry.RegistryEntry;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment.Rarity;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class NorthstarEnchantments {
	
	public static final RegistryEntry<FrostbiteEnchantment> FROSTBITE = REGISTRATE.object("frostbite")
			.enchantment(EnchantmentCategory.WEAPON, FrostbiteEnchantment::new)
			.addSlots(EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND)
			.lang("Frostbite")
			.rarity(Rarity.VERY_RARE)
			.register();

	public static void register() {}

}
