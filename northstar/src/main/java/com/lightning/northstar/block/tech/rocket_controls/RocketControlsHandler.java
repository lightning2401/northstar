package com.lightning.northstar.block.tech.rocket_controls;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.HashSet;
import java.util.Vector;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import com.lightning.northstar.NorthstarPackets;
import com.lightning.northstar.contraptions.RocketContraptionEntity;
import com.mojang.blaze3d.platform.InputConstants;
import com.simibubi.create.foundation.utility.ControlsUtil;
import com.simibubi.create.foundation.utility.Lang;

import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.LevelAccessor;

public class RocketControlsHandler {

	public static Collection<Integer> currentlyPressed = new HashSet<>();

	public static int PACKET_RATE = 5;
	private static int packetCooldown;
	private static int displaytime = 0;
	private static int launchtime = -20;

	private static WeakReference<RocketContraptionEntity> entityRef = new WeakReference<>(null);
	private static BlockPos controlsPos;

	public static void levelUnloaded(LevelAccessor level) {
		packetCooldown = 0;
		entityRef = new WeakReference<>(null);
		controlsPos = null;
		currentlyPressed.clear();
	}

	@SuppressWarnings("resource")
	public static void startControlling(RocketContraptionEntity entity, BlockPos controllerLocalPos) {
		entityRef = new WeakReference<RocketContraptionEntity>(entity);
		controlsPos = controllerLocalPos;
		displaytime = 0;
		Minecraft.getInstance().player.displayClientMessage(
			Lang.translateDirect("contraption.controls.start_controlling", entity.getContraptionName()), true);

	}

	@SuppressWarnings("resource")
	public static void stopControlling() {
		ControlsUtil.getControls()
			.forEach(kb -> kb.setDown(ControlsUtil.isActuallyPressed(kb)));
		RocketContraptionEntity contrapEntity = entityRef.get();

		if (!currentlyPressed.isEmpty() && contrapEntity != null)
			NorthstarPackets.getChannel().sendToServer(new RocketControlsInputPacket(currentlyPressed, false,
				contrapEntity.getId(), controlsPos, true));

		packetCooldown = 0;
		entityRef = new WeakReference<>(null);
		controlsPos = null;
		currentlyPressed.clear();

		Minecraft.getInstance().player.displayClientMessage(Lang.translateDirect("contraption.controls.stop_controlling"),
			true);
	}

	@SuppressWarnings("resource")
	public static void tick() {
		RocketContraptionEntity entity = entityRef.get();
		LocalPlayer player = Minecraft.getInstance().player;
		if(displaytime < 61)
		displaytime++;
		if(displaytime == 60 && player != null && entity != null && launchtime == -20 && !entity.blasting && !entity.landing && entity.launchtime == 0) {
			if(!entity.getControllingPlayer().isEmpty()) {
				if(entity.getControllingPlayer().get() == player.getUUID())
					{player.displayClientMessage(
							Lang.translateDirect("contraption.controls.rocket_tut").withStyle(ChatFormatting.AQUA), true);}
			}
		}
		if(launchtime % 20 == 0 && player != null && entity != null && launchtime != -20) {
			player.displayClientMessage(
			Component.literal(String.valueOf(launchtime / 20)).withStyle(ChatFormatting.AQUA), true);
			player.level().playSound(player, player.blockPosition(), SoundEvents.NOTE_BLOCK_PLING.get(), SoundSource.BLOCKS, 10, launchtime / 20 == 0 ? 10 : 1);
		}
		
		if(player != null && entity != null) {
			if(entity.landing && entity.getY() < entity.level().getMaxBuildHeight() + 500) 
			{if(!entity.getControllingPlayer().isEmpty()) {
				if(entity.getControllingPlayer().get() == player.getUUID())
				{player.displayClientMessage(
					Lang.translateDirect("contraption.controls.landing_warning").withStyle(ChatFormatting.RED), true);}
			}
			}
		}
		
		if(launchtime > -20)
		launchtime--;
		
		if (entity == null)
			return;
		if (packetCooldown > 0)
			packetCooldown--;

		if (entity.isRemoved() || InputConstants.isKeyDown(Minecraft.getInstance()
			.getWindow()
			.getWindow(), GLFW.GLFW_KEY_ESCAPE)) {
			BlockPos pos = controlsPos;
			stopControlling();
			NorthstarPackets.getChannel()
				.sendToServer(new RocketControlsInputPacket(currentlyPressed, false, entity.getId(), pos, true));
			return;
		}

		Vector<KeyMapping> controls = ControlsUtil.getControls();
		Collection<Integer> pressedKeys = new HashSet<>();
		for (int i = 0; i < controls.size(); i++) {
			if (ControlsUtil.isActuallyPressed(controls.get(i)))
				pressedKeys.add(i);
		}
		entity.clientControl(controlsPos, pressedKeys, player);
		
		
		
		Collection<Integer> newKeys = new HashSet<>(pressedKeys);
		Collection<Integer> releasedKeys = currentlyPressed;
		newKeys.removeAll(releasedKeys);
		releasedKeys.removeAll(pressedKeys);
		
		

		// Released Keys
		if (!releasedKeys.isEmpty()) {
			NorthstarPackets.getChannel()
				.sendToServer(new RocketControlsInputPacket(releasedKeys, false, entity.getId(), controlsPos, false));
//			AllSoundEvents.CONTROLLER_CLICK.playAt(player.level, player.blockPosition(), 1f, .5f, true);
		}

		// Newly Pressed Keys
		if (!newKeys.isEmpty()) {
			NorthstarPackets.getChannel().sendToServer(new RocketControlsInputPacket(newKeys, true, entity.getId(), controlsPos, false));
			packetCooldown = PACKET_RATE;
//			AllSoundEvents.CONTROLLER_CLICK.playAt(player.level, player.blockPosition(), 1f, .75f, true);
		}

		// Keepalive Pressed Keys
		if (packetCooldown == 0) {
//			if (!pressedKeys.isEmpty()) {
			NorthstarPackets.getChannel()
					.sendToServer(new RocketControlsInputPacket(pressedKeys, true, entity.getId(), controlsPos, false));
				packetCooldown = PACKET_RATE;
//			}
		}
		if(!currentlyPressed.isEmpty()) {
			if(currentlyPressed.contains(4) && launchtime == -20 && !entity.landing && !entity.blasting) {
				launchtime = 200;
			}
			if(currentlyPressed.contains(5)) {
				stopControlling();
			}
		}

		currentlyPressed = pressedKeys;
		controls.forEach(kb -> kb.setDown(false));
	}

	@Nullable
	public static RocketContraptionEntity getContraption() {
		return entityRef.get();
	}

	@Nullable
	public static BlockPos getControlsPos() {
		return controlsPos;
	}
}
