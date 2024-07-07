package com.lightning.northstar.client;

import com.lightning.northstar.contraptions.RocketContraptionEntity;
import com.lightning.northstar.item.client.SpaceSuitFirstPersonRenderer;
import com.simibubi.create.content.trains.CameraDistanceModifier;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(Dist.CLIENT)
public class ClientEvents {

	@SubscribeEvent
	public static void onTick(ClientTickEvent event) {
		if (!isGameActive())
			return;
		SpaceSuitFirstPersonRenderer.clientTick();
	}
	@SuppressWarnings("resource")
	@SubscribeEvent
	public static void onMount(EntityMountEvent event) {
		if (event.getEntityMounting() != Minecraft.getInstance().player)
			return;

		if (event.isDismounting()) {
			CameraDistanceModifier.reset();
			return;
		}

		if (!event.isMounting() || !(event.getEntityBeingMounted() instanceof RocketContraptionEntity rocket)) {
			return;
		}

		CameraDistanceModifier.zoomOut(6);
	}
	

	@SuppressWarnings("resource")
	protected static boolean isGameActive() {
		return !(Minecraft.getInstance().level == null || Minecraft.getInstance().player == null);
	}
}
