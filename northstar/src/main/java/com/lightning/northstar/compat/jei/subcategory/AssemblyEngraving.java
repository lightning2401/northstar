package com.lightning.northstar.compat.jei.subcategory;

import com.lightning.northstar.compat.jei.animations.AnimatedEngraver;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.compat.jei.category.sequencedAssembly.SequencedAssemblySubCategory;
import com.simibubi.create.content.processing.sequenced.SequencedRecipe;

public class AssemblyEngraving extends SequencedAssemblySubCategory {

	AnimatedEngraver engraver;

	public AssemblyEngraving() {
		super(25);
		engraver = new AnimatedEngraver();
	}

	@Override
	public void draw(SequencedRecipe<?> recipe, PoseStack ms, double mouseX, double mouseY, int index) {
		ms.pushPose();
		ms.translate(-5.5, 46.3, 0);
		ms.scale(.625f, .625f, .625f);
		engraver.draw(ms, getWidth() / 2, 30);
		ms.popPose();
	}

}
