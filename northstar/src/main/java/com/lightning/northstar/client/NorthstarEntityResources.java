package com.lightning.northstar.client;

import com.lightning.northstar.Northstar;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;

public class NorthstarEntityResources {
	
	// armors
	
	public static final ResourceLocation IRONSPACESUITARMOR_MODEL = new ResourceLocation(Northstar.MOD_ID,
			"geo/iron_space_suit.geo.json");
	public static final ResourceLocation IRONSPACESUITARMOR_TEXTURE = new ResourceLocation(Northstar.MOD_ID,
			"textures/armor/iron_space_suit_texture.png");
	public static final ResourceLocation BROKEN_IRONSPACESUITARMOR_TEXTURE = new ResourceLocation(Northstar.MOD_ID,
			"textures/armor/broken_iron_space_suit_texture.png");
	public static final ResourceLocation IRONSPACESUITARMOR_ANIMATIONS = new ResourceLocation(Northstar.MOD_ID,
			"animations/iron_space_suit.animation.json");
	public static final ResourceLocation IRONSPACESUIT_ARMOR_ARM = new ResourceLocation(Northstar.MOD_ID,
			"textures/armor/iron_space_suit_arm.png");
	
	public static final ResourceLocation BROKEN_IRONSPACESUIT_ARMOR_ARM = new ResourceLocation(Northstar.MOD_ID,
			"textures/armor/broken_iron_space_suit_arm.png");
	
	public static final ResourceLocation MARTIANSTEELSPACESUITARMOR_MODEL = new ResourceLocation(Northstar.MOD_ID,
			"geo/iron_space_suit.geo.json");
	public static final ResourceLocation MARTIANSTEELSPACESUITARMOR_TEXTURE = new ResourceLocation(Northstar.MOD_ID,
			"textures/armor/martian_steel_space_suit_texture.png");
	public static final ResourceLocation MARTIANSTEELSPACESUITARMOR_ANIMATIONS = new ResourceLocation(Northstar.MOD_ID,
			"animations/iron_space_suit.animation.json");
	public static final ResourceLocation MARTIANSTEELSPACESUITARMOR_ARM = new ResourceLocation(Northstar.MOD_ID,
			"textures/armor/martian_steel_space_suit_arm.png");
	
	// martian creatures
	
	public static final ResourceLocation WORM_MODEL = new ResourceLocation(Northstar.MOD_ID, "geo/mars_worm.geo.json");
	public static final ResourceLocation WORM_TEXTURE = new ResourceLocation(Northstar.MOD_ID, "textures/entity/mars_worm.png");
	public static final ResourceLocation WORM_ANIMATIONS = new ResourceLocation(Northstar.MOD_ID,
			"animations/mars_worm.animation.json");
	
	public static final ResourceLocation TOAD_MODEL = new ResourceLocation(Northstar.MOD_ID, "geo/mars_toad.geo.json");
	public static final ResourceLocation TOAD_TEXTURE = new ResourceLocation(Northstar.MOD_ID, "textures/entity/mars_toad.png");
	public static final ResourceLocation TOAD_ANIMATIONS = new ResourceLocation(Northstar.MOD_ID,
			"animations/mars_toad.animation.json");
	
	public static final ResourceLocation COBRA_MODEL = new ResourceLocation(Northstar.MOD_ID, "geo/mars_cobra.geo.json");
	public static final ResourceLocation COBRA_TEXTURE = new ResourceLocation(Northstar.MOD_ID, "textures/entity/mars_cobra.png");
	public static final ResourceLocation COBRA_ANIMATIONS = new ResourceLocation(Northstar.MOD_ID,
			"animations/mars_cobra.animation.json");
	
	public static final ResourceLocation MOTH_MODEL = new ResourceLocation(Northstar.MOD_ID, "geo/mars_moth.geo.json");
	public static final ResourceLocation MOTH_TEXTURE = new ResourceLocation(Northstar.MOD_ID, "textures/entity/mars_moth.png");
	public static final ResourceLocation MOTH_ANIMATIONS = new ResourceLocation(Northstar.MOD_ID,
			"animations/mars_moth.animation.json");
	
	// venusian creatures
	
	public static final ResourceLocation MIMIC_MODEL = new ResourceLocation(Northstar.MOD_ID, "geo/venus_mimic.geo.json");
	public static final ResourceLocation MIMIC_TEXTURE = new ResourceLocation(Northstar.MOD_ID, "textures/entity/venus_mimic.png");
	public static final ResourceLocation MIMIC_DEEP_TEXTURE = new ResourceLocation(Northstar.MOD_ID, "textures/entity/venus_deep_mimic.png");
	public static final ResourceLocation MIMIC_ANIMATIONS = new ResourceLocation(Northstar.MOD_ID,
			"animations/mimic.animation.json");
	
	public static final ResourceLocation SCORPION_MODEL = new ResourceLocation(Northstar.MOD_ID, "geo/venus_scorpion.geo.json");
	public static final ResourceLocation SCORPION_TEXTURE = new ResourceLocation(Northstar.MOD_ID, "textures/entity/venus_scorpion.png");
	public static final ResourceLocation SCORPION_ANIMATIONS = new ResourceLocation(Northstar.MOD_ID,
			"animations/venus_scorpion.animation.json");
	
	public static final ResourceLocation STONE_BULL_MODEL = new ResourceLocation(Northstar.MOD_ID, "geo/venus_stone_bull.geo.json");
	public static final ResourceLocation STONE_BULL_TEXTURE = new ResourceLocation(Northstar.MOD_ID, "textures/entity/venus_stone_bull.png");
	public static final ResourceLocation STONE_BULL_ANIMATIONS = new ResourceLocation(Northstar.MOD_ID,
			"animations/venus_stone_bull.animation.json");
	
	public static final ResourceLocation VULTURE_MODEL = new ResourceLocation(Northstar.MOD_ID, "geo/venus_bird.geo.json");
	public static final ResourceLocation VULTURE_TEXTURE = new ResourceLocation(Northstar.MOD_ID, "textures/entity/venus_bird.png");
	public static final ResourceLocation VULTURE_ANIMATIONS = new ResourceLocation(Northstar.MOD_ID,
			"animations/venus_bird.animation.json");
	
	// moon creatures
	
	public static final ResourceLocation SNAIL_MODEL = new ResourceLocation(Northstar.MOD_ID, "geo/moon_snail.geo.json");
	public static final ResourceLocation SNAIL_TEXTURE = new ResourceLocation(Northstar.MOD_ID, "textures/entity/moon_snail.png");
	public static final ResourceLocation SNAIL_ANIMATIONS = new ResourceLocation(Northstar.MOD_ID,
			"animations/moon_snail.animation.json");
	
	public static final ResourceLocation LUNARGRADE_MODEL = new ResourceLocation(Northstar.MOD_ID, "geo/moon_lunargrade.geo.json");
	public static final ResourceLocation LUNARGRADE_TEXTURE = new ResourceLocation(Northstar.MOD_ID, "textures/entity/moon_lunargrade.png");
	public static final ResourceLocation LUNARGRADE_ANIMATIONS = new ResourceLocation(Northstar.MOD_ID,
			"animations/moon_lunargrade.animation.json");
	
	public static final ResourceLocation EEL_MODEL = new ResourceLocation(Northstar.MOD_ID, "geo/moon_lunar_hydra.geo.json");
	public static final ResourceLocation EEL_TEXTURE = new ResourceLocation(Northstar.MOD_ID, "textures/entity/moon_lunar_hydra.png");
	public static final ResourceLocation EEL_ANIMATIONS = new ResourceLocation(Northstar.MOD_ID,
			"animations/moon_lunar_worm.animation.json");
	
	//mercury creatures
	public static final ResourceLocation RAPTOR_MODEL = new ResourceLocation(Northstar.MOD_ID, "geo/mercury_raptor.geo.json");
	public static final ResourceLocation RAPTOR_TEXTURE = new ResourceLocation(Northstar.MOD_ID, "textures/entity/mercury_raptor.png");
	public static final ResourceLocation RAPTOR_ANIMATIONS = new ResourceLocation(Northstar.MOD_ID,
			"animations/mercury_raptor.animation.json");
	
	public static final ResourceLocation ROACH_MODEL = new ResourceLocation(Northstar.MOD_ID, "geo/mercury_roach.geo.json");
	public static final ResourceLocation ROACH_TEXTURE = new ResourceLocation(Northstar.MOD_ID, "textures/entity/mercury_roach.png");
	public static final ResourceLocation ROACH_ANIMATIONS = new ResourceLocation(Northstar.MOD_ID,
			"animations/mercury_roach.animation.json");
	
	public static final ResourceLocation TORTOISE_MODEL = new ResourceLocation(Northstar.MOD_ID, "geo/mercury_tortoise.geo.json");
	public static final ResourceLocation TORTOISE_TEXTURE = new ResourceLocation(Northstar.MOD_ID, "textures/entity/mercury_tortoise.png");
	public static final ResourceLocation TORTOISE_ANIMATIONS = new ResourceLocation(Northstar.MOD_ID,
			"animations/mercury_tortoise.animation.json");
    public static void register() {
    }
}
