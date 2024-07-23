package com.lightning.northstar.entity;

import com.lightning.northstar.Northstar;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Northstar.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NorthstarCommonListener {

	@SubscribeEvent
	public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
		event.put(NorthstarEntityTypes.VENUS_MIMIC.get(),
				Monster.createMobAttributes().add(Attributes.FOLLOW_RANGE, 16.0D).add(Attributes.MAX_HEALTH, 20.0D).add(Attributes.ATTACK_DAMAGE, 5).add(Attributes.MOVEMENT_SPEED, 0.2f).build());
		event.put(NorthstarEntityTypes.VENUS_SCORPION.get(),
				Monster.createMobAttributes().add(Attributes.FOLLOW_RANGE, 16.0D).add(Attributes.MAX_HEALTH, 20.0D).add(Attributes.ATTACK_DAMAGE, 5).add(Attributes.MOVEMENT_SPEED, 0.2f).build());
		event.put(NorthstarEntityTypes.VENUS_STONE_BULL.get(),
				Monster.createMobAttributes().add(Attributes.FOLLOW_RANGE, 16.0D).add(Attributes.MAX_HEALTH, 60.0D).add(Attributes.ATTACK_DAMAGE, 10).add(Attributes.MOVEMENT_SPEED, 0.25f).build());
		event.put(NorthstarEntityTypes.VENUS_VULTURE.get(),
				Monster.createMobAttributes().add(Attributes.FOLLOW_RANGE, 16.0D).add(Attributes.MAX_HEALTH, 40.0D).add(Attributes.ATTACK_DAMAGE, 4).add(Attributes.MOVEMENT_SPEED, 0.2f).build());
		
		event.put(NorthstarEntityTypes.MARS_WORM.get(),
				Monster.createMobAttributes().add(Attributes.FOLLOW_RANGE, 16.0D).add(Attributes.MAX_HEALTH, 40.0D).add(Attributes.ATTACK_DAMAGE, 5).add(Attributes.MOVEMENT_SPEED, 0.2f).build());
		event.put(NorthstarEntityTypes.MARS_TOAD.get(),
				Monster.createMobAttributes().add(Attributes.FOLLOW_RANGE, 16.0D).add(Attributes.MAX_HEALTH, 16.0D).add(Attributes.ATTACK_DAMAGE, 3).add(Attributes.MOVEMENT_SPEED, 0.25f).build());
		event.put(NorthstarEntityTypes.MARS_COBRA.get(),
				Monster.createMobAttributes().add(Attributes.FOLLOW_RANGE, 16.0D).add(Attributes.MAX_HEALTH, 16.0D).add(Attributes.ATTACK_DAMAGE, 3).add(Attributes.MOVEMENT_SPEED, 0.3f).build());
		event.put(NorthstarEntityTypes.MARS_MOTH.get(),
				Monster.createMobAttributes().add(Attributes.FOLLOW_RANGE, 16.0D).add(Attributes.MAX_HEALTH, 8.0D).add(Attributes.ATTACK_DAMAGE, 1).add(Attributes.MOVEMENT_SPEED, 0.25f).build());

		event.put(NorthstarEntityTypes.MOON_LUNARGRADE.get(),
				Monster.createMobAttributes().add(Attributes.FOLLOW_RANGE, 16.0D).add(Attributes.MAX_HEALTH, 24.0D).add(Attributes.ATTACK_DAMAGE, 3).add(Attributes.MOVEMENT_SPEED, 0.25f).build());
		event.put(NorthstarEntityTypes.MOON_SNAIL.get(),
				Monster.createMobAttributes().add(Attributes.FOLLOW_RANGE, 16.0D).add(Attributes.MAX_HEALTH, 8.0D).add(Attributes.ATTACK_DAMAGE, 0.5).add(Attributes.MOVEMENT_SPEED, 0.15f).build());
		event.put(NorthstarEntityTypes.MOON_EEL.get(),
				Monster.createMobAttributes().add(Attributes.FOLLOW_RANGE, 16.0D).add(Attributes.MAX_HEALTH, 4.0D).add(Attributes.ATTACK_DAMAGE, 0.25).add(Attributes.MOVEMENT_SPEED, 0.2f).build());
		
		event.put(NorthstarEntityTypes.MERCURY_RAPTOR.get(),
				Monster.createMobAttributes().add(Attributes.FOLLOW_RANGE, 16.0D).add(Attributes.MAX_HEALTH, 36.0D).add(Attributes.ATTACK_DAMAGE, 6).add(Attributes.MOVEMENT_SPEED, 0.25f).build());
		event.put(NorthstarEntityTypes.MERCURY_ROACH.get(),
				Monster.createMobAttributes().add(Attributes.FOLLOW_RANGE, 16.0D).add(Attributes.MAX_HEALTH, 8.0D).add(Attributes.ATTACK_DAMAGE, 2).add(Attributes.ARMOR, 20).add(Attributes.MOVEMENT_SPEED, 0.2f).build());
		event.put(NorthstarEntityTypes.MERCURY_TORTOISE.get(),
				Monster.createMobAttributes().add(Attributes.FOLLOW_RANGE, 16.0D).add(Attributes.MAX_HEALTH, 30.0D).add(Attributes.ARMOR, 16).add(Attributes.MOVEMENT_SPEED, 0.15f).build());
		
		
		event.put(NorthstarEntityTypes.FROZEN_ZOMBIE.get(),
				Monster.createMobAttributes().add(Attributes.FOLLOW_RANGE, 35.0D).add(Attributes.MOVEMENT_SPEED, (double)0.23F).add(Attributes.ATTACK_DAMAGE, 3.0D).add(Attributes.ARMOR, 2.0D)
				.add(Attributes.SPAWN_REINFORCEMENTS_CHANCE).build());
	}
}
