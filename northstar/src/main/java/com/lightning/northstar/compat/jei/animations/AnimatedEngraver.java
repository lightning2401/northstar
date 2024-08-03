package com.lightning.northstar.compat.jei.animations;

import com.lightning.northstar.block.NorthstarTechBlocks;
import com.lightning.northstar.block.tech.NorthstarPartialModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.foundation.utility.AnimationTickHolder;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;


public class AnimatedEngraver extends AnimatedKinetics {
	

	public AnimatedEngraver() {
	}

	@Override
	public void draw(GuiGraphics gui, int xOffset, int yOffset) {
		PoseStack ms = gui.pose();
		ms.pushPose();
		ms.translate(xOffset, yOffset - 16, 200);
		ms.mulPose(Axis.XP.rotationDegrees(-15.5f));
		ms.mulPose(Axis.YP.rotationDegrees(22.5f));
		int scale = 24;

		blockElement(shaft(Direction.Axis.Z))
				.rotateBlock(0, 0, getCurrentAngle())
				.scale(scale)
				.render(gui);
		blockElement(NorthstarTechBlocks.CIRCUIT_ENGRAVER.getDefaultState())
				.scale(scale)
				.render(gui);
		blockElement(NorthstarPartialModels.CIRCUIT_ENGRAVER_HEAD)
				.rotateBlock(0, getAnimatedHeadOffset(), 0)
				.scale(scale)
				.render(gui);
		blockElement(NorthstarPartialModels.CIRCUIT_ENGRAVER_LASER)
				.rotateBlock(0, getAnimatedHeadOffset(), 0)
				.scale(scale)
				.render(gui);
		blockElement(AllBlocks.DEPOT.getDefaultState())
				.atLocal(0, 1.65, 0)
				.scale(scale)
				.render(gui);

		ms.popPose();
	}

	private float getAnimatedHeadOffset() {
		return (AnimationTickHolder.getRenderTime() - offset * 8) % 90 * 4;

	}

}
