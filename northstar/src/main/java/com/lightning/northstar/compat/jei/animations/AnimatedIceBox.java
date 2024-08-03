package com.lightning.northstar.compat.jei.animations;

import com.lightning.northstar.block.NorthstarTechBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;

import net.minecraft.client.gui.GuiGraphics;

public class AnimatedIceBox extends AnimatedKinetics {
	

	public AnimatedIceBox() {
	}

	@Override
	public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
		PoseStack ms = graphics.pose();
		ms.pushPose();
		ms.translate(xOffset, yOffset + 22, 200);
		ms.mulPose(Axis.XP.rotationDegrees(-15.5f));
		ms.mulPose(Axis.YP.rotationDegrees(22.5f));
		int scale = 24;

		blockElement(NorthstarTechBlocks.ICE_BOX.getDefaultState())
				.scale(scale)
				.render(graphics);
		ms.popPose();
	}

}