package com.lightning.northstar.compat.jei.category;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import com.lightning.northstar.block.tech.circuit_engraver.EngravingRecipe;
import com.lightning.northstar.compat.jei.animations.AnimatedEngraver;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.gui.AllGuiTextures;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.GuiGraphics;

@ParametersAreNonnullByDefault
public class EngravingCategory extends CreateRecipeCategory<EngravingRecipe>  {

	private final AnimatedEngraver engraver = new AnimatedEngraver();

	public EngravingCategory(Info<EngravingRecipe> info) {
		super(info);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, EngravingRecipe recipe, IFocusGroup focuses) {
		builder
				.addSlot(RecipeIngredientRole.INPUT, 27, 51)
				.setBackground(getRenderedSlot(), -1, -1)
				.addIngredients(recipe.getIngredients().get(0));

		List<ProcessingOutput> results = recipe.getRollableResults();
		int i = 0;
		for (ProcessingOutput output : results) {
			builder.addSlot(RecipeIngredientRole.OUTPUT, 131 + 19 * i, 50)
					.setBackground(getRenderedSlot(output), -1, -1)
					.addItemStack(output.getStack())
					.addTooltipCallback(addStochasticTooltip(output));
			i++;
		}
	}

	@Override
	public void draw(EngravingRecipe recipe, IRecipeSlotsView iRecipeSlotsView, GuiGraphics matrixStack,  double mouseX, double mouseY) {
		AllGuiTextures.JEI_SHADOW.render(matrixStack, 61, 41);
		AllGuiTextures.JEI_LONG_ARROW.render(matrixStack, 52, 54);

		engraver.draw(matrixStack, getBackground().getWidth() / 2 - 17, 22);
	}

}
