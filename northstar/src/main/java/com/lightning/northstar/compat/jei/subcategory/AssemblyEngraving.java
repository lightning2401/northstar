package com.lightning.northstar.compat.jei.subcategory;

import com.lightning.northstar.compat.jei.animations.AnimatedEngraver;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.compat.jei.category.sequencedAssembly.SequencedAssemblySubCategory;
import com.simibubi.create.content.processing.sequenced.SequencedRecipe;

import net.minecraft.client.gui.GuiGraphics;

public class AssemblyEngraving extends SequencedAssemblySubCategory {

	AnimatedEngraver engraver;

	public AssemblyEngraving() {
		super(25);
		engraver = new AnimatedEngraver();
	}

	@Override
	public void draw(SequencedRecipe<?> recipe, GuiGraphics graphics, double mouseX, double mouseY, int index) {
		PoseStack ms = graphics.pose();
		ms.pushPose();
		ms.translate(-5.5, 46.3, 0);
		ms.scale(.625f, .625f, .625f);
		engraver.draw(graphics, getWidth() / 2, 30);
		ms.popPose();
	}

}
