package com.lightning.northstar.compat.jei.category;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang3.mutable.MutableInt;

import com.lightning.northstar.block.tech.ice_box.FreezingRecipe;
import com.lightning.northstar.compat.jei.animations.AnimatedIceBox;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.utility.Pair;

import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

@ParametersAreNonnullByDefault
public class FreezingCategory extends CreateRecipeCategory<FreezingRecipe>  {

	private final AnimatedIceBox iceBox = new AnimatedIceBox();

	public FreezingCategory(Info<FreezingRecipe> info) {
		super(info);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, FreezingRecipe recipe, IFocusGroup focuses) {
		List<Pair<Ingredient, MutableInt>> condensedIngredients = ItemHelper.condenseIngredients(recipe.getIngredients());

		int size = condensedIngredients.size() + recipe.getFluidIngredients().size();
		int xOffset = size < 3 ? (3 - size) * 19 / 2 : 0;
		int i = 0;

		for (Pair<Ingredient, MutableInt> pair : condensedIngredients) {
			List<ItemStack> stacks = new ArrayList<>();
			for (ItemStack itemStack : pair.getFirst().getItems()) {
				ItemStack copy = itemStack.copy();
				copy.setCount(pair.getSecond().getValue());
				stacks.add(copy);
			}

			builder
					.addSlot(RecipeIngredientRole.INPUT, 17 + xOffset + (i % 3) * 19, 51 - (i / 3) * 19)
					.setBackground(getRenderedSlot(), -1, -1)
					.addItemStacks(stacks);
			i++;
		}
		for (FluidIngredient fluidIngredient : recipe.getFluidIngredients()) {
			builder
					.addSlot(RecipeIngredientRole.INPUT, 17 + xOffset + (i % 3) * 19, 51 - (i / 3) * 19)
					.setBackground(getRenderedSlot(), -1, -1)
					.addIngredients(ForgeTypes.FLUID_STACK, withImprovedVisibility(fluidIngredient.getMatchingFluidStacks()))
					.addTooltipCallback(addFluidTooltip(fluidIngredient.getRequiredAmount()));
			i++;
		}

		size = recipe.getRollableResults().size() + recipe.getFluidResults().size();
		i = 0;

		for (ProcessingOutput result : recipe.getRollableResults()) {
			int xPosition = 142 - (size % 2 != 0 && i == size - 1 ? 0 : i % 2 == 0 ? 10 : -9);
			int yPosition = -19 * (i / 2) + 51;

			builder
					.addSlot(RecipeIngredientRole.OUTPUT, xPosition, yPosition)
					.setBackground(getRenderedSlot(result), -1, -1)
					.addItemStack(result.getStack())
					.addTooltipCallback(addStochasticTooltip(result));
			i++;
		}

		for (FluidStack fluidResult : recipe.getFluidResults()) {
			int xPosition = 142 - (size % 2 != 0 && i == size - 1 ? 0 : i % 2 == 0 ? 10 : -9);
			int yPosition = -19 * (i / 2) + 51;

			builder
					.addSlot(RecipeIngredientRole.OUTPUT, xPosition, yPosition)
					.setBackground(getRenderedSlot(), -1, -1)
					.addIngredient(ForgeTypes.FLUID_STACK, withImprovedVisibility(fluidResult))
					.addTooltipCallback(addFluidTooltip(fluidResult.getAmount()));
			i++;
		}
	}

	@Override
	public void draw(FreezingRecipe recipe, IRecipeSlotsView iRecipeSlotsView, PoseStack matrixStack,  double mouseX, double mouseY) {
		AllGuiTextures.JEI_SHADOW.render(matrixStack, 61, 41);
		AllGuiTextures.JEI_LONG_ARROW.render(matrixStack, 52, 54);
		

		iceBox.draw(matrixStack, getBackground().getWidth() / 2 - 17, 22);

		int vRows = (1 + recipe.getFluidResults().size() + recipe.getRollableResults().size()) / 2;

		String text = -recipe.getProcessingDuration() + " C°";
		Minecraft minecraft = Minecraft.getInstance();
		Font fontRenderer = minecraft.font;
		int stringCenter = fontRenderer.width(text) / 2;
		fontRenderer.drawShadow(matrixStack, text, (getBackground().getWidth() / 2) + 2 - stringCenter, 62, 0xFFFFFF);
		
		if (vRows <= 2)
			AllGuiTextures.JEI_DOWN_ARROW.render(matrixStack, 136, -19 * (vRows - 1) + 32);
	}

}