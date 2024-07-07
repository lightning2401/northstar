package com.lightning.northstar.compat.jei.animations;

import com.lightning.northstar.block.NorthstarTechBlocks;
import com.lightning.northstar.block.tech.NorthstarPartialModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.foundation.utility.AnimationTickHolder;

import net.minecraft.core.Direction.Axis;

public class AnimatedEngraver extends AnimatedKinetics {
	

	public AnimatedEngraver() {
	}

	@Override
	public void draw(PoseStack matrixStack, int xOffset, int yOffset) {
		matrixStack.pushPose();
		matrixStack.translate(xOffset, yOffset - 16, 200);
		matrixStack.mulPose(Vector3f.XP.rotationDegrees(-15.5f));
		matrixStack.mulPose(Vector3f.YP.rotationDegrees(22.5f));
		int scale = 24;

		blockElement(shaft(Axis.Z))
				.rotateBlock(0, 0, getCurrentAngle())
				.scale(scale)
				.render(matrixStack);
		blockElement(NorthstarTechBlocks.CIRCUIT_ENGRAVER.getDefaultState())
				.scale(scale)
				.render(matrixStack);
		blockElement(NorthstarPartialModels.CIRCUIT_ENGRAVER_HEAD)
				.rotateBlock(0, getAnimatedHeadOffset(), 0)
				.scale(scale)
				.render(matrixStack);
		blockElement(NorthstarPartialModels.CIRCUIT_ENGRAVER_LASER)
				.rotateBlock(0, getAnimatedHeadOffset(), 0)
				.scale(scale)
				.render(matrixStack);
		blockElement(AllBlocks.DEPOT.getDefaultState())
				.atLocal(0, 1.65, 0)
				.scale(scale)
				.render(matrixStack);

		matrixStack.popPose();
	}

	private float getAnimatedHeadOffset() {
		return (AnimationTickHolder.getRenderTime() - offset * 8) % 90 * 4;

	}

}
