package com.lightning.northstar.item.armor;

import com.lightning.northstar.world.OxygenStuff;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.Components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class RemainingOxygenOverlay implements IGuiOverlay {
	public static final RemainingOxygenOverlay INSTANCE = new RemainingOxygenOverlay();
	
	@Override
	public void render(ForgeGui gui, GuiGraphics graphics, float partialTick, int width, int height) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.options.hideGui || mc.gameMode.getPlayerMode() == GameType.SPECTATOR)
			return;

		LocalPlayer player = mc.player;
		if (player == null)
			return;
		if (player.isCreative())
			return;
		PoseStack poseStack = graphics.pose();

		
		poseStack.pushPose();
		
		ItemStack oxytank = OxygenStuff.getOxy(player);
		
		if(oxytank.isEmpty()) {
			return;
		}
		
		int timeLeft = oxytank.getOrCreateTag().getInt("Oxygen");
		
		poseStack.translate(width / 2 + 90, height - 53 + (oxytank.getItem()
			.isFireResistant() ? 9 : 0), 0);
		
		Component text = Components.literal(net.minecraft.util.StringUtil.formatTickDuration(Math.max(0, timeLeft - 1) * 20));
		GuiGameElement.of(oxytank)
			.at(0, 0)
			.render(graphics);
		int color = 0xFF_FFFFFF;
		if (timeLeft < 60 && timeLeft % 2 == 0) {
			color = Color.mixColors(0xFF_FF0000, color, Math.max(timeLeft / 60f, .25f));
		}
		graphics.drawString(mc.font, text, 16, 5, color);
	
		poseStack.popPose();
	}
}








