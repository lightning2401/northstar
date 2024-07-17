package com.lightning.northstar.block.tech.ice_box;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.lightning.northstar.item.NorthstarRecipeTypes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder.ProcessingRecipeParams;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour.TankSegment;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.recipe.DummyCraftingContainer;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import com.simibubi.create.foundation.utility.Iterate;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

@SuppressWarnings("removal")
public class FreezingRecipe extends ProcessingRecipe<SmartInventory>  {
	
	//im gonna cry I just want this to work
	public static boolean match(IceBoxBlockEntity iceBox, Recipe<?> recipe) {
		FilteringBehaviour filter = iceBox.getFilter();
		if (filter == null)
			return false;

		boolean filterTest = filter.test(recipe.getResultItem());
		if (recipe instanceof FreezingRecipe) {
			FreezingRecipe FreezingRecipe = (FreezingRecipe) recipe;
			if (FreezingRecipe.getRollableResults()
				.isEmpty()
				&& !FreezingRecipe.getFluidResults()
					.isEmpty())
				filterTest = filter.test(FreezingRecipe.getFluidResults()
					.get(0));
		}

		if (!filterTest)
			return false;

		return apply(iceBox, recipe, true);
	}

	public static boolean apply(IceBoxBlockEntity iceBox, Recipe<?> recipe) {
		return apply(iceBox, recipe, false);
	}

	private static boolean apply(IceBoxBlockEntity icebox, Recipe<?> recipe, boolean test) {
		boolean isFreezingRecipe = recipe instanceof FreezingRecipe;
		IItemHandler availableItems = icebox.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			.orElse(null);
		IFluidHandler availableFluids = icebox.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			.orElse(null);

		if (availableItems == null || availableFluids == null)
			return false;

		List<ItemStack> recipeOutputItems = new ArrayList<>();
		List<FluidStack> recipeOutputFluids = new ArrayList<>();

		List<Ingredient> ingredients = new LinkedList<>(recipe.getIngredients());
		List<FluidIngredient> fluidIngredients =
			isFreezingRecipe ? ((FreezingRecipe) recipe).getFluidIngredients() : Collections.emptyList();

		for (boolean simulate : Iterate.trueAndFalse) {

			if (!simulate && test)
				return true;

			int[] extractedItemsFromSlot = new int[availableItems.getSlots()];
			int[] extractedFluidsFromTank = new int[availableFluids.getTanks()];

			Ingredients: for (int i = 0; i < ingredients.size(); i++) {
				Ingredient ingredient = ingredients.get(i);

				for (int slot = 0; slot < availableItems.getSlots(); slot++) {
					if (simulate && availableItems.getStackInSlot(slot)
						.getCount() <= extractedItemsFromSlot[slot])
						continue;
					ItemStack extracted = availableItems.extractItem(slot, 1, true);
					if (!ingredient.test(extracted))
						continue;
					if (!simulate)
						availableItems.extractItem(slot, 1, false);
					extractedItemsFromSlot[slot]++;
					continue Ingredients;
				}

				// something wasn't found
				return false;
			}

			boolean fluidsAffected = false;
			FluidIngredients: for (int i = 0; i < fluidIngredients.size(); i++) {
				FluidIngredient fluidIngredient = fluidIngredients.get(i);
				int amountRequired = fluidIngredient.getRequiredAmount();

				for (int tank = 0; tank < availableFluids.getTanks(); tank++) {
					FluidStack fluidStack = availableFluids.getFluidInTank(tank);
					if (simulate && fluidStack.getAmount() <= extractedFluidsFromTank[tank])
						continue;
					if (!fluidIngredient.test(fluidStack))
						continue;
					int drainedAmount = Math.min(amountRequired, fluidStack.getAmount());
					if (!simulate) {
						fluidStack.shrink(drainedAmount);
						fluidsAffected = true;
					}
					amountRequired -= drainedAmount;
					if (amountRequired != 0)
						continue;
					extractedFluidsFromTank[tank] += drainedAmount;
					continue FluidIngredients;
				}

				// something wasn't found
				return false;
			}

			if (fluidsAffected) {
				icebox.getBehaviour(SmartFluidTankBehaviour.INPUT)
					.forEach(TankSegment::onFluidStackChanged);
				icebox.getBehaviour(SmartFluidTankBehaviour.OUTPUT)
					.forEach(TankSegment::onFluidStackChanged);
			}

			if (simulate) {
				if (recipe instanceof FreezingRecipe FreezingRecipe) {
					recipeOutputItems.addAll(FreezingRecipe.rollResults());
					recipeOutputFluids.addAll(FreezingRecipe.getFluidResults());
					recipeOutputItems.addAll(FreezingRecipe.getRemainingItems(icebox.getInputInventory()));
				} else {
					recipeOutputItems.add(recipe.getResultItem());

					if (recipe instanceof CraftingRecipe craftingRecipe) {
						recipeOutputItems.addAll(craftingRecipe.getRemainingItems(new DummyCraftingContainer(availableItems, extractedItemsFromSlot)));
					}
				}
			}

			if (!icebox.acceptOutputs(recipeOutputItems, recipeOutputFluids, simulate))
				return false;
		}

		return true;
	}

	protected FreezingRecipe(IRecipeTypeInfo type, ProcessingRecipeParams params) {
		super(type, params);
	}

	public FreezingRecipe(ProcessingRecipeParams params) {
		this(NorthstarRecipeTypes.FREEZING, params);
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
	protected boolean canRequireHeat() {
		return true;
	}

	@Override
	public boolean matches(SmartInventory pContainer, Level pLevel) {
		// TODO Auto-generated method stub
		return false;
	}

}
