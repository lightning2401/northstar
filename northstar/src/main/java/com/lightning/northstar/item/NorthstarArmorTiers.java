package com.lightning.northstar.item;

import java.util.function.Supplier;

import com.lightning.northstar.Northstar;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem.Type;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;


public record NorthstarArmorTiers(String name, int durability, int[] protection, int enchantability, 
	SoundEvent equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> repairMaterial) implements ArmorMaterial{
	
	private static final int[] DURABILITY_PER_SLOT = new int[] {15,18,20,15};
	private static final int[] MAX_DAMAGE_ARRAY = new int[] { 11, 16, 15, 13 };
	
	@Override
	public int getDurabilityForType(Type pType) {
		return DURABILITY_PER_SLOT[pType.ordinal()] * this.durability;
	}

	@Override
	public int getDefenseForType(Type pType) {
		return this.protection[pType.ordinal()];
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
