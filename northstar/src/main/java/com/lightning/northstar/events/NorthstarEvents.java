package com.lightning.northstar.events;

import com.lightning.northstar.block.tech.rocket_controls.RocketControlsServerHandler;

import net.minecraft.world.level.Level;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent.LevelTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class NorthstarEvents {

	@SubscribeEvent
	public static void onServerWorldTick(LevelTickEvent event) {
		if (event.phase == Phase.START)
			return;
		if (event.side == LogicalSide.CLIENT)
			return;
		Level world = event.level;
		RocketControlsServerHandler.tick(world);
	}
	@SubscribeEvent
	public static void registerCommands(RegisterCommandsEvent event) {
//		VenusWeather.register(event.getDispatcher());
//		MarsWeather.register(event.getDispatcher());
	}
}
