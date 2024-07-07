package com.lightning.northstar.block.tech.electrolysis_machine;

import com.lightning.northstar.Northstar;
import com.lightning.northstar.item.NorthstarRecipeTypes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder.ProcessingRecipeParams;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class ElectrolysisRecipe extends ProcessingRecipe<RecipeWrapper>  {

	static int counter = 0;
	
	public static ElectrolysisRecipe create(FluidIngredient from, FluidStack toL, FluidStack toR, String name) {
		ResourceLocation recipeId = Northstar.asResource(name);
		return new ProcessingRecipeBuilder<>(ElectrolysisRecipe::new, recipeId)
			.withFluidIngredients(from)
			.withFluidOutputs(toL, toR)
			.build();
	}	

	protected ElectrolysisRecipe(IRecipeTypeInfo type, ProcessingRecipeParams params) {
		super(type, params);
	}

	public ElectrolysisRecipe(ProcessingRecipeParams params) {
		this(NorthstarRecipeTypes.ELECTROLYSIS, params);
	}

	@Override
	protected int getMaxInputCount() {
		return 9;
	}

	@Override
	protected int getMaxOutputCount() {
		return 4;
	}

	@Override
	protected int getMaxFluidInputCount() {
		return 2;
	}

	@Override
	protected int getMaxFluidOutputCount() {
		return 2;
	}
	
	@Override
	public boolean matches(RecipeWrapper pContainer, Level pLevel) {
		return true;
	}

}
