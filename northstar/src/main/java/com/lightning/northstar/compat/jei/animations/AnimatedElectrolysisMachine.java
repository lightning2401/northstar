package com.lightning.northstar.compat.jei.animations;

import com.lightning.northstar.block.NorthstarTechBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;

import net.minecraft.client.gui.GuiGraphics;

public class AnimatedElectrolysisMachine extends AnimatedKinetics {
	

	public AnimatedElectrolysisMachine() {
	}

	@Override
	public void draw(GuiGraphics matrixStack, int xOffset, int yOffset) {
		PoseStack ps = matrixStack.pose();
		ps.pushPose();
		ps.translate(xOffset, yOffset + 22, 200);
		ps.mulPose(Axis.XP.rotationDegrees(-15.5f));
		ps.mulPose(Axis.YP.rotationDegrees(22.5f));
		int scale = 24;

		blockElement(NorthstarTechBlocks.ELECTROLYSIS_MACHINE.getDefaultState())
				.scale(scale)
				.render(matrixStack);
		ps.popPose();
	}

}