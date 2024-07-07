package com.lightning.northstar;

import com.lightning.northstar.block.tech.rocket_controls.RocketControlsHandler;
import com.lightning.northstar.item.armor.RemainingOxygenOverlay;
import com.lightning.northstar.particle.NorthstarParticles;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(Dist.CLIENT)
public class NorthstarClient {
	
	@SubscribeEvent
	public static void onTick(ClientTickEvent event) {
		
		if (event.phase == Phase.START) {
			RocketControlsHandler.tick();
		}
		
	}
	
	public static void onCtorClient(IEventBus modEventBus, IEventBus forgeEventBus) {
		modEventBus.addListener(NorthstarParticles::registerFactories);
	}

	@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
	public static class ModBusEvents {
		@SubscribeEvent
		public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
			event.registerAbove(VanillaGuiOverlay.AIR_LEVEL.id(), "remaining_oxygen", RemainingOxygenOverlay.INSTANCE);
		}
		
	}
}
