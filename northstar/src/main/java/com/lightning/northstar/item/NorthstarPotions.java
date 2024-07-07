package com.lightning.northstar.item;

import com.lightning.northstar.Northstar;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class NorthstarPotions {
	public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, Northstar.MOD_ID);
	
	public static final RegistryObject<Potion> ENHANCED_STRENGTH = POTIONS.register("enhanced_strength", () -> new Potion("strength", new MobEffectInstance(MobEffects.DAMAGE_BOOST, 900, 2)));
	public static final RegistryObject<Potion> ENHANCED_HEALING = POTIONS.register("enhanced_healing", () -> new Potion("healing", new MobEffectInstance(MobEffects.HEAL, 1, 2)));
	public static final RegistryObject<Potion> ENHANCED_REGENERATION = POTIONS.register("enhanced_regeneration", () -> new Potion("regeneration", new MobEffectInstance(MobEffects.REGENERATION, 225, 2)));
	
	public static void register(IEventBus eventBus)
	{POTIONS.register(eventBus);}

	public static void register() {}
}
