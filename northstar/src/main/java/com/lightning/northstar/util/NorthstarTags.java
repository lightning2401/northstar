package com.lightning.northstar.util;

import com.lightning.northstar.Northstar;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class NorthstarTags {
	public static class Blocks {
        public static final TagKey<Block> NEEDS_MARTIAN_STEEL_TOOL
                = tag("needs_martian_steel_tool");
        public static final TagKey<Block> BASE_STONE_MARS
        		=tag("base_stone_mars");


        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(Northstar.MOD_ID, name));
        }
        @SuppressWarnings("unused")
		private static TagKey<Block> forgeTag(String name) {
            return BlockTags.create(new ResourceLocation("forge", name));
        }
    }
}