package com.lightning.northstar.particle;

import java.util.Locale;
import java.util.function.Supplier;

import com.lightning.northstar.Northstar;
import com.simibubi.create.foundation.particle.ICustomParticleData;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public enum NorthstarParticles {
	
	GLOWSTONE_PARTICLE(GlowstoneParticleData::new),
	ROCKET_FLAME(RocketFlameParticleData::new),
	ROCKET_FLAME_LANDING(RocketFlameLandingParticleData::new),
	ROCKET_SMOKE(RocketSmokeParticleData::new),
	ROCKET_SMOKE_LANDING(RocketSmokeLandingParticleData::new),
	COLD_AIR(ColdAirParticleData::new),
	OXY_FLOW(OxyFlowParticleData::new),
	SNOWFLAKE(SnowflakeParticleData::new),
	SNAIL_SLIME(SnailSlimeParticleData::new),
	SULFUR_POOF(SulfurPoofParticleData::new),
	DUST_CLOUD(DustCloudParticleData::new);
	
	private final ParticleEntry<?> entry;

	<D extends ParticleOptions> NorthstarParticles(Supplier<? extends ICustomParticleData<D>> typeFactory) {
		String name = name().toLowerCase(Locale.ROOT);
		entry = new ParticleEntry<>(name, typeFactory);
	}

	public static void register(IEventBus modEventBus) {
		ParticleEntry.REGISTER.register(modEventBus);
	}

	@OnlyIn(Dist.CLIENT)
	public static void registerFactories(RegisterParticleProvidersEvent event) {
		for (NorthstarParticles particle : values())
			particle.entry.registerFactory(event);
	}

	public ParticleType<?> get() {
		return entry.object.get();
	}

	public String parameter() {
		return entry.name;
	}

	private static class ParticleEntry<D extends ParticleOptions> {
		private static final DeferredRegister<ParticleType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Northstar.MOD_ID);

		private final String name;
		private final Supplier<? extends ICustomParticleData<D>> typeFactory;
		private final RegistryObject<ParticleType<D>> object;

		public ParticleEntry(String name, Supplier<? extends ICustomParticleData<D>> typeFactory) {
			this.name = name;			
			this.typeFactory = typeFactory;
			object = REGISTER.register(name, () -> this.typeFactory.get().createType());
		}

		@OnlyIn(Dist.CLIENT)
		public void registerFactory(RegisterParticleProvidersEvent event) {
			typeFactory.get()
				.register(object.get(), event);
		}

	}

}
