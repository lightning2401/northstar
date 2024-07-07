package com.lightning.northstar.item;

import java.util.function.Supplier;

import com.lightning.northstar.Northstar;


import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;


public record NorthstarArmorTiers(String name, int durability, int[] protection, int enchantability, 
	SoundEvent equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> repairMaterial) implements ArmorMaterial{
	
	private static final int[] DURABILITY_PER_SLOT = new int[] {15,18,20,15};
	
	@Override
	public int getDurabilityForSlot(EquipmentSlot slot) {
		return DURABILITY_PER_SLOT[slot.getIndex()] * this.durability;
	}
	@Override
	public int getDefenseForSlot(EquipmentSlot slot) {
		return this.protection[slot.getIndex()];
	}
	@Override
	public int getEnchantmentValue() {
		return this.enchantability;
	}
	@Override
	public SoundEvent getEquipSound() {
		return this.equipSound();
	}
	@Override
	public Ingredient getRepairIngredient() {
		return this.repairMaterial.get();
	}
	@Override
	public String getName() {
		return Northstar.MOD_ID + ":" + this.name;
	}
	@Override
	public float getToughness() {
		return this.toughness;
	}
	@Override
	public float getKnockbackResistance() {
		return this.knockbackResistance;
	}
	public static final ArmorMaterial MARTIAN_STEEL_ARMOR = new NorthstarArmorTiers(
			"martian_steel_armor",
			300,
			new int[] {3,6,8,3},
			25,
			SoundEvents.ARMOR_EQUIP_IRON,
			2.5f,
			0.05f,
			() -> Ingredient.of(NorthstarItems.MARTIAN_STEEL.get()));
	public static final ArmorMaterial IRON_SPACE_SUIT = new NorthstarArmorTiers(
			"iron_space_suit",
			300,
			new int[] {2,5,7,2},
			25,
			SoundEvents.ARMOR_EQUIP_IRON,
			0.5f,
			0,
			() -> Ingredient.of(Items.IRON_INGOT));
	public static final ArmorMaterial MARTIAN_STEEL_SPACE_SUIT = new NorthstarArmorTiers(
			"martian_steel_space_suit",
			300,
			new int[] {3,6,8,3},
			25,
			SoundEvents.ARMOR_EQUIP_IRON,
			2.5f,
			0.05f,
			() -> Ingredient.of(NorthstarItems.MARTIAN_STEEL.get()));
}
