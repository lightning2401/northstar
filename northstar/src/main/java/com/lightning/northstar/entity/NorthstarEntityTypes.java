package com.lightning.northstar.entity;

import com.lightning.northstar.Northstar;
import com.lightning.northstar.contraptions.RocketContraptionEntity;
import com.lightning.northstar.entity.projectiles.LunargradeSpit;
import com.lightning.northstar.entity.projectiles.VenusScorpionSpit;
import com.lightning.northstar.entity.variants.FrozenZombieEntity;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.render.ContraptionEntityRenderer;
import com.simibubi.create.foundation.data.CreateEntityBuilder;
import com.simibubi.create.foundation.utility.Lang;
import com.tterrag.registrate.util.entry.EntityEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityType.EntityFactory;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class NorthstarEntityTypes {
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES,
			Northstar.MOD_ID);
	
	//actual animals / entities
	
	
	//venus
	public static final RegistryObject<EntityType<VenusMimicEntity>> VENUS_MIMIC = buildEntity(
			VenusMimicEntity::new, VenusMimicEntity.class, 1F, 1F, "venus_mimic", MobCategory.MONSTER, true, 8);
	
	public static final RegistryObject<EntityType<VenusScorpionEntity>> VENUS_SCORPION = buildEntity(
			VenusScorpionEntity::new, VenusScorpionEntity.class, 0.8F, 1.5F, "venus_scorpion", MobCategory.MONSTER, true, 8);
	
	public static final RegistryObject<EntityType<VenusStoneBullEntity>> VENUS_STONE_BULL = buildEntity(
			VenusStoneBullEntity::new, VenusStoneBullEntity.class, 1.85F, 1.5F, "venus_stone_bull", MobCategory.MONSTER, true, 8);
	
	public static final RegistryObject<EntityType<VenusVultureEntity>> VENUS_VULTURE = buildEntity(
			VenusVultureEntity::new, VenusVultureEntity.class, 0.8f, 0.9f, "venus_vulture", MobCategory.CREATURE, true, 16);
	
	//mars
	public static final RegistryObject<EntityType<MarsWormEntity>> MARS_WORM = buildEntity(
			MarsWormEntity::new, MarsWormEntity.class, 1.5f, 0.75f, "mars_worm", MobCategory.MONSTER, false, 8);
	
	public static final RegistryObject<EntityType<MarsToadEntity>> MARS_TOAD = buildEntity(
			MarsToadEntity::new, MarsToadEntity.class, 0.7f, 0.5f, "mars_toad", MobCategory.MONSTER, false, 8);
	
	public static final RegistryObject<EntityType<MarsCobraEntity>> MARS_COBRA = buildEntity(
			MarsCobraEntity::new, MarsCobraEntity.class, 1f, 0.7f, "mars_cobra", MobCategory.MONSTER, false, 8);
	
	public static final RegistryObject<EntityType<MarsMothEntity>> MARS_MOTH = buildEntity(
			MarsMothEntity::new, MarsMothEntity.class, 0.8f, 0.9f, "mars_moth", MobCategory.MONSTER, false, 8);
	
	//moon
	public static final RegistryObject<EntityType<MoonLunargradeEntity>> MOON_LUNARGRADE = buildEntity(
			MoonLunargradeEntity::new, MoonLunargradeEntity.class, 0.9f, 0.7f, "moon_lunargrade", MobCategory.MONSTER, false, 8);
	public static final RegistryObject<EntityType<MoonSnailEntity>> MOON_SNAIL = buildEntity(
			MoonSnailEntity::new, MoonSnailEntity.class, 0.5f, 0.5f, "moon_snail", MobCategory.MONSTER, false, 8);
	public static final RegistryObject<EntityType<MoonEelEntity>> MOON_EEL = buildEntity(
			MoonEelEntity::new, MoonEelEntity.class, 0.5f, 0.3f, "moon_eel", MobCategory.MONSTER, false, 8);
	

	public static final RegistryObject<EntityType<FrozenZombieEntity>> FROZEN_ZOMBIE = buildEntity(
			FrozenZombieEntity::new, FrozenZombieEntity.class, 0.6F, 1.95F, "frozen_zombie", MobCategory.MONSTER, false, 8);	
	
	//mercury
	public static final RegistryObject<EntityType<MercuryRaptorEntity>> MERCURY_RAPTOR = buildEntity(
			MercuryRaptorEntity::new, MercuryRaptorEntity.class, 0.7f, 1.4f, "mercury_raptor", MobCategory.MONSTER, false, 8);
	public static final RegistryObject<EntityType<MercuryRoachEntity>> MERCURY_ROACH = buildEntity(
			MercuryRoachEntity::new, MercuryRoachEntity.class, 0.6f, 0.5f, "mercury_roach", MobCategory.MONSTER, false, 8);
	public static final RegistryObject<EntityType<MercuryTortoiseEntity>> MERCURY_TORTOISE = buildEntity(
			MercuryTortoiseEntity::new, MercuryTortoiseEntity.class, 1f, 0.9f, "mercury_tortoise", MobCategory.MONSTER, false, 8);


	// misc
	
	public static final RegistryObject<EntityType<LunargradeSpit>> LUNARGRADE_SPIT = buildEntity(
			LunargradeSpit::new, LunargradeSpit.class, 0.25f, 0.25f, "lunargrade_spit", MobCategory.MISC, false, 8);	
	public static final RegistryObject<EntityType<VenusScorpionSpit>> VENUS_SCORPION_SPIT = buildEntity(
			VenusScorpionSpit::new, VenusScorpionSpit.class, 0.25f, 0.25f, "venus_scorpion_spit", MobCategory.MISC, false, 8);	
	
	// contraptions
	
	public static final EntityEntry<RocketContraptionEntity> ROCKET_CONTRAPTION =
			contraption("rocket_contraption", RocketContraptionEntity::new, () -> ContraptionEntityRenderer::new,
				200, 40, false).register();

	
//	public static final EntityEntry<RocketEntity> ROCKET =
//			register("rocket", RocketEntity::new, () -> null, MobCategory.MISC, 10,
//				Integer.MAX_VALUE, false, true, RocketEntity::build).register();
	
	
	
	
	public static <T extends Entity> RegistryObject<EntityType<T>> buildEntity(EntityType.EntityFactory<T> entity,
			Class<T> entityClass, float width, float height, String name, MobCategory category, boolean fireImmune, int clientRange) {
		if(fireImmune) {
		return ENTITIES.register(name,
				() -> EntityType.Builder.of(entity, category).fireImmune().sized(width, height).clientTrackingRange(clientRange).build(name));}
		else {
			return ENTITIES.register(name,
					() -> EntityType.Builder.of(entity, category).sized(width, height).build(name));
		}
	}
	
	private static <T extends Entity> CreateEntityBuilder<T, ?> contraption(String name, EntityFactory<T> factory,
			NonNullSupplier<NonNullFunction<EntityRendererProvider.Context, EntityRenderer<? super T>>> renderer, int range,
			int updateFrequency, boolean sendVelocity) {
			return register(name, factory, renderer, MobCategory.MISC, range, updateFrequency, sendVelocity, true,
				AbstractContraptionEntity::build);
		}

		private static <T extends Entity> CreateEntityBuilder<T, ?> register(String name, EntityFactory<T> factory,
			NonNullSupplier<NonNullFunction<EntityRendererProvider.Context, EntityRenderer<? super T>>> renderer,
			MobCategory group, int range, int updateFrequency, boolean sendVelocity, boolean immuneToFire,
			NonNullConsumer<EntityType.Builder<T>> propertyBuilder) {
			String id = Lang.asId(name);
			return (CreateEntityBuilder<T, ?>) Northstar.REGISTRATE
				.entity(id, factory, group)
				.properties(b -> b.setTrackingRange(range)
					.setUpdateInterval(updateFrequency)
					.setShouldReceiveVelocityUpdates(sendVelocity))
				.properties(propertyBuilder)
				.properties(b -> {
					if (immuneToFire)
						b.fireImmune();
				})
				.renderer(renderer);
		}

		public static void register() {}

}
