package com.lightning.northstar.compat.jei.animations;

import com.lightning.northstar.block.NorthstarTechBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;

public class AnimatedElectrolysisMachine extends AnimatedKinetics {
	

	public AnimatedElectrolysisMachine() {
	}

	@Override
	public void draw(PoseStack matrixStack, int xOffset, int yOffset) {
		matrixStack.pushPose();
		matrixStack.translate(xOffset, yOffset + 22, 200);
		matrixStack.mulPose(Vector3f.XP.rotationDegrees(-15.5f));
		matrixStack.mulPose(Vector3f.YP.rotationDegrees(22.5f));
		int scale = 24;

		blockElement(NorthstarTechBlocks.ELECTROLYSIS_MACHINE.getDefaultState())
				.scale(scale)
				.render(matrixStack);
		matrixStack.popPose();
	}

}