package com.lightning.northstar.item;

import java.util.List;

import com.lightning.northstar.Northstar;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.TierSortingRegistry;

public class NorthstarToolTiers {
	public static Tier MARTIAN_STEEL;
	
	static {
		MARTIAN_STEEL = TierSortingRegistry.registerTier(new ForgeTier(4, 4000, 9f, 5f, 30, Tags.Blocks.NEEDS_NETHERITE_TOOL, () -> Ingredient.of(NorthstarItems.MARTIAN_STEEL.get())),
		
		new ResourceLocation(Northstar.MOD_ID, "martian_steel"), List.of(Tiers.DIAMOND), List.of(Tiers.NETHERITE));
	}

}
