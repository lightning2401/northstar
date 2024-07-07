package com.lightning.northstar;

import com.lightning.northstar.entity.NorthstarEntityTypes;
import com.lightning.northstar.entity.projectiles.renderers.LunargradeSpitRenderer;
import com.lightning.northstar.entity.projectiles.renderers.VenusScorpionSpitRenderer;
import com.lightning.northstar.entity.renderers.MarsCobraRenderer;
import com.lightning.northstar.entity.renderers.MarsMothRenderer;
import com.lightning.northstar.entity.renderers.MarsToadRenderer;
import com.lightning.northstar.entity.renderers.MarsWormRenderer;
import com.lightning.northstar.entity.renderers.MercuryRaptorRenderer;
import com.lightning.northstar.entity.renderers.MercuryRoachRenderer;
import com.lightning.northstar.entity.renderers.MercuryTortoiseRenderer;
import com.lightning.northstar.entity.renderers.MoonEelRenderer;
import com.lightning.northstar.entity.renderers.MoonLunargradeRenderer;
import com.lightning.northstar.entity.renderers.MoonSnailRenderer;
import com.lightning.northstar.entity.renderers.VenusMimicRenderer;
import com.lightning.northstar.entity.renderers.VenusScorpionRenderer;
import com.lightning.northstar.entity.renderers.VenusStoneBullRenderer;
import com.lightning.northstar.entity.renderers.VenusVultureRenderer;
import com.lightning.northstar.entity.variants.renderers.FrozenZombieRenderer;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Northstar.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class NorthstarRenderers {

	@SubscribeEvent
	public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(NorthstarEntityTypes.VENUS_MIMIC.get(), VenusMimicRenderer::new);
		event.registerEntityRenderer(NorthstarEntityTypes.VENUS_SCORPION.get(), VenusScorpionRenderer::new);
		event.registerEntityRenderer(NorthstarEntityTypes.VENUS_STONE_BULL.get(), VenusStoneBullRenderer::new);
		event.registerEntityRenderer(NorthstarEntityTypes.VENUS_VULTURE.get(), VenusVultureRenderer::new);
		
		event.registerEntityRenderer(NorthstarEntityTypes.MARS_WORM.get(), MarsWormRenderer::new);
		event.registerEntityRenderer(NorthstarEntityTypes.MARS_TOAD.get(), MarsToadRenderer::new);
		event.registerEntityRenderer(NorthstarEntityTypes.MARS_COBRA.get(), MarsCobraRenderer::new);
		event.registerEntityRenderer(NorthstarEntityTypes.MARS_MOTH.get(), MarsMothRenderer::new);

		event.registerEntityRenderer(NorthstarEntityTypes.MOON_LUNARGRADE.get(), MoonLunargradeRenderer::new);
		event.registerEntityRenderer(NorthstarEntityTypes.MOON_SNAIL.get(), MoonSnailRenderer::new);
		event.registerEntityRenderer(NorthstarEntityTypes.MOON_EEL.get(), MoonEelRenderer::new);

		event.registerEntityRenderer(NorthstarEntityTypes.MERCURY_RAPTOR.get(), MercuryRaptorRenderer::new);
		event.registerEntityRenderer(NorthstarEntityTypes.MERCURY_ROACH.get(), MercuryRoachRenderer::new);
		event.registerEntityRenderer(NorthstarEntityTypes.MERCURY_TORTOISE.get(), MercuryTortoiseRenderer::new);
		
		event.registerEntityRenderer(NorthstarEntityTypes.LUNARGRADE_SPIT.get(), LunargradeSpitRenderer::new);
		event.registerEntityRenderer(NorthstarEntityTypes.VENUS_SCORPION_SPIT.get(), VenusScorpionSpitRenderer::new);
		
		event.registerEntityRenderer(NorthstarEntityTypes.FROZEN_ZOMBIE.get(), FrozenZombieRenderer::new);
	}
	
}
