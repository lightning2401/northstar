package com.lightning.northstar.sound;

import com.lightning.northstar.Northstar;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class NorthstarSounds {
	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Northstar.MOD_ID);
	protected RegistryObject<SoundEvent> event;
	
	public static final RegistryObject<SoundEvent> MARTIAN_DUST_STORM = registerSound("martian_dust_storm");
	public static final RegistryObject<SoundEvent> MARTIAN_DUST_STORM_ABOVE = registerSound("martian_dust_storm_above");
	
	//mars worm
	
	public static final RegistryObject<SoundEvent> MARS_WORM_CLICK_NOTICE = registerSound("mars_worm_click_notice");
	public static final RegistryObject<SoundEvent> MARS_WORM_CLICK = registerSound("mars_worm_click");
	public static final RegistryObject<SoundEvent> MARS_WORM_HURT = registerSound("mars_worm_hurt");
	public static final RegistryObject<SoundEvent> MARS_WORM_DEATH = registerSound("mars_worm_death");
	public static final RegistryObject<SoundEvent> MARS_WORM_ATTACK = registerSound("mars_worm_attack");
	
	//mars toad
	
	public static final RegistryObject<SoundEvent> MARS_TOAD_HURT = registerSound("mars_toad_hurt");
	public static final RegistryObject<SoundEvent> MARS_TOAD_DEATH = registerSound("mars_toad_death");
	public static final RegistryObject<SoundEvent> MARS_TOAD_IDLE = registerSound("mars_toad_idle");

	//mars cobra

	public static final RegistryObject<SoundEvent> MARS_COBRA_HURT = registerSound("mars_cobra_hurt");
	public static final RegistryObject<SoundEvent> MARS_COBRA_DEATH = registerSound("mars_cobra_death");
	public static final RegistryObject<SoundEvent> MARS_COBRA_IDLE = registerSound("mars_cobra_idle");
	public static final RegistryObject<SoundEvent> MARS_COBRA_HISS = registerSound("mars_cobra_hiss");
	
	//mars moth

	public static final RegistryObject<SoundEvent> MARS_MOTH_HURT = registerSound("mars_moth_hurt");
	public static final RegistryObject<SoundEvent> MARS_MOTH_DEATH = registerSound("mars_moth_death");
	public static final RegistryObject<SoundEvent> MARS_MOTH_IDLE = registerSound("mars_moth_idle");
	public static final RegistryObject<SoundEvent> MARS_MOTH_SNORE = registerSound("mars_moth_snore");
	
	//moon snail
	public static final RegistryObject<SoundEvent> MOON_SNAIL_HURT = registerSound("moon_snail_hurt");
	public static final RegistryObject<SoundEvent> MOON_SNAIL_DIE = registerSound("moon_snail_die");
	public static final RegistryObject<SoundEvent> MOON_SNAIL_IDLE = registerSound("moon_snail_idle");
	
	//moon eel
	public static final RegistryObject<SoundEvent> MOON_EEL_HURT = registerSound("moon_eel_hurt");
	public static final RegistryObject<SoundEvent> MOON_EEL_DIE = registerSound("moon_eel_die");
	public static final RegistryObject<SoundEvent> MOON_EEL_IDLE = registerSound("moon_eel_idle");
	
	//moon lunargrade
	public static final RegistryObject<SoundEvent> MOON_LUNARGRADE_HURT = registerSound("moon_lunargrade_hurt");
	public static final RegistryObject<SoundEvent> MOON_LUNARGRADE_DIE = registerSound("moon_lunargrade_die");
	public static final RegistryObject<SoundEvent> MOON_LUNARGRADE_IDLE = registerSound("moon_lunargrade_idle");
	
	//venus stone bull

	public static final RegistryObject<SoundEvent> VENUS_STONE_BULL_HURT = registerSound("venus_stone_bull_hurt");
	public static final RegistryObject<SoundEvent> VENUS_STONE_BULL_DEATH = registerSound("venus_stone_bull_death");
	public static final RegistryObject<SoundEvent> VENUS_STONE_BULL_IDLE = registerSound("venus_stone_bull_idle");
	public static final RegistryObject<SoundEvent> VENUS_STONE_BULL_ATTACK = registerSound("venus_stone_bull_attack");
	public static final RegistryObject<SoundEvent> VENUS_STONE_BULL_CHARGE = registerSound("venus_stone_bull_charge");
	
	//venus mimic stuff
	
	public static final RegistryObject<SoundEvent> VENUS_MIMIC_HURT = registerSound("venus_mimic_hurt");
	public static final RegistryObject<SoundEvent> VENUS_MIMIC_DEATH = registerSound("venus_mimic_die");
	public static final RegistryObject<SoundEvent> VENUS_MIMIC_IDLE = registerSound("venus_mimic_idle");
	
	//venus scorpion
	public static final RegistryObject<SoundEvent> VENUS_SCORPION_HURT = registerSound("venus_scorpion_hurt");
	public static final RegistryObject<SoundEvent> VENUS_SCORPION_DEATH = registerSound("venus_scorpion_die");
	public static final RegistryObject<SoundEvent> VENUS_SCORPION_IDLE = registerSound("venus_scorpion_idle");
	
	//venus vulture
	
	public static final RegistryObject<SoundEvent> VENUS_VULTURE_HURT = registerSound("venus_vulture_hurt");
	public static final RegistryObject<SoundEvent> VENUS_VULTURE_IDLE = registerSound("venus_vulture_idle");
	public static final RegistryObject<SoundEvent> VENUS_VULTURE_DIE = registerSound("venus_vulture_die");
	
	//mercury raptor stuff
	
	public static final RegistryObject<SoundEvent> MERCURY_RAPTOR_HURT = registerSound("mercury_raptor_hurt");
	public static final RegistryObject<SoundEvent> MERCURY_RAPTOR_DIE = registerSound("mercury_raptor_die");
	public static final RegistryObject<SoundEvent> MERCURY_RAPTOR_IDLE = registerSound("mercury_raptor_idle");
	public static final RegistryObject<SoundEvent> MERCURY_RAPTOR_ATTACK = registerSound("mercury_raptor_attack");
	
	//mercury roach stuff
	
	public static final RegistryObject<SoundEvent> MERCURY_ROACH_HURT = registerSound("mercury_roach_hurt");
	public static final RegistryObject<SoundEvent> MERCURY_ROACH_DIE = registerSound("mercury_roach_die");
	public static final RegistryObject<SoundEvent> MERCURY_ROACH_IDLE = registerSound("mercury_roach_idle");
	
	//mercury tortoise stuff
	
	public static final RegistryObject<SoundEvent> MERCURY_TORTOISE_HURT = registerSound("mercury_tortoise_hurt");
	public static final RegistryObject<SoundEvent> MERCURY_TORTOISE_DIE = registerSound("mercury_tortoise_die");
	public static final RegistryObject<SoundEvent> MERCURY_TORTOISE_IDLE = registerSound("mercury_tortoise_idle");
	
	
	
	public static final RegistryObject<SoundEvent> LASER_AMBIENT = registerSound("laser_ambient");
	public static final RegistryObject<SoundEvent> LASER_BURN = registerSound("laser_burn");
	public static final RegistryObject<SoundEvent> ROCKET_TAKEOFF = registerSound("rocket_takeoff");
	public static final RegistryObject<SoundEvent> ROCKET_BLAST = registerSound("rocket_blast");
	public static final RegistryObject<SoundEvent> ROCKET_LANDING = registerSound("rocket_landing");
	public static final RegistryObject<SoundEvent> AIRFLOW = registerSound("airflow");

	public static void register(IEventBus eventbus) {
		SOUNDS.register(eventbus);
	}
	
	public static RegistryObject<SoundEvent> registerSound(String name){
		ResourceLocation sound_loc = new ResourceLocation(Northstar.MOD_ID, name);
		return SOUNDS.register(name, () -> new SoundEvent(sound_loc));
	}
	
	

}
