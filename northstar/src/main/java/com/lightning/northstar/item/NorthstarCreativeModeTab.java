package com.lightning.northstar.item;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.commons.lang3.mutable.MutableObject;

import com.lightning.northstar.Northstar;
import com.lightning.northstar.NorthstarTags.NorthstarItemTags;
import com.lightning.northstar.block.NorthstarBlocks;
import com.lightning.northstar.block.NorthstarTechBlocks;
import com.lightning.northstar.world.OxygenStuff;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.Create;
import com.simibubi.create.content.contraptions.actors.seat.SeatBlock;
import com.simibubi.create.content.equipment.armor.BacktankUtil;
import com.simibubi.create.content.equipment.toolbox.ToolboxBlock;
import com.simibubi.create.content.kinetics.crank.ValveHandleBlock;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.TagDependentIngredientItem;
import com.simibubi.create.foundation.utility.Components;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import it.unimi.dsi.fastutil.objects.ReferenceLinkedOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTab.DisplayItemsGenerator;
import net.minecraft.world.item.CreativeModeTab.ItemDisplayParameters;
import net.minecraft.world.item.CreativeModeTab.Output;
import net.minecraft.world.item.CreativeModeTab.TabVisibility;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class NorthstarCreativeModeTab {
	private static final DeferredRegister<CreativeModeTab> REGISTER =
			DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Northstar.MOD_ID);

	public static final RegistryObject<CreativeModeTab> NORTHSTAR_TAB = REGISTER.register("northstartab",
			() -> CreativeModeTab.builder()
				.title(Components.translatable("itemGroup.northstartab"))
				.icon(() -> new ItemStack(NorthstarItems.MARTIAN_STEEL.get()))
				.displayItems((pParameters, out) -> {
					addStarMaps(out);
					out.accept(NorthstarItems.MARTIAN_SWORD.get());
					out.accept(NorthstarItems.MARTIAN_PICKAXE.get());
					out.accept(NorthstarItems.MARTIAN_SHOVEL.get());
					out.accept(NorthstarItems.MARTIAN_AXE.get());
					out.accept(NorthstarItems.MARTIAN_HOE.get());
					out.accept(NorthstarItems.MARTIAN_STEEL_HELMET.get());
					out.accept(NorthstarItems.MARTIAN_STEEL_CHESTPLATE.get());
					out.accept(NorthstarItems.MARTIAN_STEEL_LEGGINGS.get());
					out.accept(NorthstarItems.MARTIAN_STEEL_BOOTS.get());
					out.accept(NorthstarItems.BROKEN_IRON_SPACE_SUIT_HELMET.get());
					out.accept(NorthstarItems.BROKEN_IRON_SPACE_SUIT_CHESTPIECE.get());
					out.accept(NorthstarItems.BROKEN_IRON_SPACE_SUIT_LEGGINGS.get());
					out.accept(NorthstarItems.BROKEN_IRON_SPACE_SUIT_BOOTS.get());
					out.accept(NorthstarItems.IRON_SPACE_SUIT_HELMET.get());
					addOxyItem(out, NorthstarItems.IRON_SPACE_SUIT_CHESTPIECE.get());
					out.accept(NorthstarItems.IRON_SPACE_SUIT_LEGGINGS.get());
					out.accept(NorthstarItems.IRON_SPACE_SUIT_BOOTS.get());
					out.accept(NorthstarItems.MARTIAN_STEEL_SPACE_SUIT_HELMET.get());
					addOxyItem(out, NorthstarItems.MARTIAN_STEEL_SPACE_SUIT_CHESTPIECE.get());
					out.accept(NorthstarItems.MARTIAN_STEEL_SPACE_SUIT_LEGGINGS.get());
					out.accept(NorthstarItems.MARTIAN_STEEL_SPACE_SUIT_BOOTS.get());
					out.accept(NorthstarItems.CIRCUIT.get());
					out.accept(NorthstarItems.ADVANCED_CIRCUIT.get());
					out.accept(NorthstarItems.TARGETING_COMPUTER.get());
					out.accept(NorthstarItems.RAW_GLOWSTONE_ORE.get());
					out.accept(NorthstarItems.ENRICHED_GLOWSTONE_ORE.get());
					out.accept(NorthstarItems.RAW_TUNGSTEN_ORE.get());
					out.accept(NorthstarItems.CRUSHED_RAW_TUNGSTEN.get());
					out.accept(NorthstarItems.TUNGSTEN_NUGGET.get());
					out.accept(NorthstarItems.RAW_MARTIAN_IRON_ORE.get());
					out.accept(NorthstarItems.MARTIAN_STEEL.get());
					out.accept(NorthstarItems.MARTIAN_STEEL_SHEET.get());
					out.accept(NorthstarItems.TUNGSTEN_INGOT.get());
					out.accept(NorthstarItems.TUNGSTEN_SHEET.get());

					out.accept(NorthstarItems.LUNAR_SAPPHIRE_SHARD.get());
					
					out.accept(NorthstarItems.POLISHED_LUNAR_SAPPHIRE.get());
					out.accept(NorthstarItems.POLISHED_DIAMOND.get());
					out.accept(NorthstarItems.POLISHED_AMETHYST.get());
					
					out.accept(NorthstarItems.MOON_SAND_PAPER.get());
					out.accept(NorthstarItems.DORMANT_MARTIAN_SAPLING.get());
					out.accept(NorthstarItems.DORMANT_MARTIAN_SEED.get());
					out.accept(NorthstarItems.MARS_SPROUT_SEEDS.get());
					out.accept(NorthstarItems.MARS_PALM_SEEDS.get());
					out.accept(NorthstarItems.MARS_TULIP_SEEDS.get());
					
					out.accept(NorthstarItems.FLATTENED_DOUGH.get());
					out.accept(NorthstarItems.RAW_ICE_CREAM_CONE.get());
					out.accept(NorthstarItems.ICE_CREAM_CONE.get());
					
					out.accept(NorthstarItems.VANILLA_ICE_CREAM.get());
					out.accept(NorthstarItems.CHOCOLATE_ICE_CREAM.get());
					out.accept(NorthstarItems.STRAWBERRY_ICE_CREAM.get());
					
					out.accept(NorthstarItems.MARTIAN_STRAWBERRY.get());
					
					out.accept(NorthstarItems.FROZEN_ZOMBIE_SPAWN_EGG.get());
					
					out.accept(NorthstarItems.MOON_EEL_SPAWN_EGG.get());
					out.accept(NorthstarItems.MOON_LUNARGRADE_SPAWN_EGG.get());
					out.accept(NorthstarItems.MOON_SNAIL_SPAWN_EGG.get());
					
					out.accept(NorthstarItems.MARS_COBRA_SPAWN_EGG.get());
					out.accept(NorthstarItems.MARS_MOTH_SPAWN_EGG.get());
					out.accept(NorthstarItems.MARS_WORM_SPAWN_EGG.get());
					out.accept(NorthstarItems.MARS_TOAD_SPAWN_EGG.get());
					
					out.accept(NorthstarItems.VENUS_MIMIC_SPAWN_EGG.get());
					out.accept(NorthstarItems.VENUS_SCORPION_SPAWN_EGG.get());
					out.accept(NorthstarItems.VENUS_STONE_BULL_SPAWN_EGG.get());
					out.accept(NorthstarItems.VENUS_VULTURE_SPAWN_EGG.get());
					
					out.accept(NorthstarItems.MERCURY_RAPTOR_SPAWN_EGG.get());
					out.accept(NorthstarItems.MERCURY_ROACH_SPAWN_EGG.get());
					out.accept(NorthstarItems.MERCURY_TORTOISE_SPAWN_EGG.get());
				})
				.build());
	public static final RegistryObject<CreativeModeTab> NORTHSTAR_BLOCKS = REGISTER.register("northstarblocks",
			() -> CreativeModeTab.builder()
				.title(Components.translatable("itemGroup.northstarblocks"))
				.icon(() -> new ItemStack(NorthstarBlocks.MARTIAN_STEEL_BLOCK.get()))
				.displayItems((pParameters, out) -> {
					out.accept(NorthstarBlocks.IRON_PLATING.get());
					out.accept(NorthstarBlocks.IRON_PLATING_SLAB.get());
					out.accept(NorthstarBlocks.IRON_PLATING_VERTICAL_SLAB.get());
					out.accept(NorthstarBlocks.IRON_PLATING_STAIRS.get());
					out.accept(NorthstarBlocks.IRON_SHEETMETAL.get());
					out.accept(NorthstarBlocks.IRON_SHEETMETAL_SLAB.get());
					out.accept(NorthstarBlocks.IRON_SHEETMETAL_VERTICAL_SLAB.get());
					out.accept(NorthstarBlocks.IRON_GRATE.get());
					out.accept(NorthstarBlocks.IRON_PILLAR.get());
					out.accept(NorthstarBlocks.VENT_BLOCK.get());

					out.accept(NorthstarBlocks.MARTIAN_STEEL_BLOCK.get());
					out.accept(NorthstarBlocks.MARTIAN_STEEL_PLATING.get());
					out.accept(NorthstarBlocks.MARTIAN_STEEL_PLATING_SLAB.get());
					out.accept(NorthstarBlocks.MARTIAN_STEEL_PLATING_VERTICAL_SLAB.get());
					out.accept(NorthstarBlocks.MARTIAN_STEEL_PLATING_STAIRS.get());
					out.accept(NorthstarBlocks.MARTIAN_STEEL_SHEETMETAL.get());
					out.accept(NorthstarBlocks.MARTIAN_STEEL_SHEETMETAL_SLAB.get());
					out.accept(NorthstarBlocks.MARTIAN_STEEL_SHEETMETAL_VERTICAL_SLAB.get());
					out.accept(NorthstarBlocks.MARTIAN_STEEL_GRATE.get());
					out.accept(NorthstarBlocks.MARTIAN_STEEL_PILLAR.get());
					out.accept(NorthstarBlocks.MARTIAN_STEEL_LARGE_PLATING.get());
					out.accept(NorthstarBlocks.MARTIAN_STEEL_LAMP.get());
					out.accept(NorthstarBlocks.MARTIAN_STEEL_BLUE_LAMP.get());
					
					out.accept(NorthstarBlocks.TUNGSTEN_BLOCK.get());
					out.accept(NorthstarBlocks.TUNGSTEN_PLATING.get());
					out.accept(NorthstarBlocks.TUNGSTEN_PLATING_SLAB.get());
					out.accept(NorthstarBlocks.TUNGSTEN_PLATING_VERTICAL_SLAB.get());
					out.accept(NorthstarBlocks.TUNGSTEN_PLATING_STAIRS.get());
					out.accept(NorthstarBlocks.TUNGSTEN_SHEETMETAL.get());
					out.accept(NorthstarBlocks.TUNGSTEN_SHEETMETAL_SLAB.get());
					out.accept(NorthstarBlocks.TUNGSTEN_SHEETMETAL_VERTICAL_SLAB.get());
					out.accept(NorthstarBlocks.TUNGSTEN_GRATE.get());
					out.accept(NorthstarBlocks.TUNGSTEN_PILLAR.get());
					
					out.accept(NorthstarBlocks.GLOWSTONE_LAMP.get());	
					
					out.accept(NorthstarItems.EXTINGUISHED_TORCH.get());
					out.accept(NorthstarTechBlocks.EXTINGUISHED_LANTERN.get());
					
					out.accept(NorthstarItems.GLOWSTONE_TORCH.get());
					out.accept(NorthstarTechBlocks.GLOWSTONE_LANTERN.get());

					out.accept(NorthstarBlocks.VOLCANIC_ASH.get());	
					out.accept(NorthstarBlocks.VOLCANIC_ROCK.get());	

					out.accept(NorthstarBlocks.FROST.get());
					out.accept(NorthstarBlocks.ICICLE.get());
					out.accept(NorthstarBlocks.METHANE_ICE.get());
					
					out.accept(NorthstarBlocks.MOON_SAND.get());	
					
					out.accept(NorthstarBlocks.MOON_STONE.get());			
					out.accept(NorthstarBlocks.MOON_COPPER_ORE.get());	
					out.accept(NorthstarBlocks.MOON_DIAMOND_ORE.get());
					out.accept(NorthstarBlocks.MOON_GLOWSTONE_ORE.get());
					out.accept(NorthstarBlocks.MOON_GOLD_ORE.get());
					out.accept(NorthstarBlocks.MOON_IRON_ORE.get());
					out.accept(NorthstarBlocks.MOON_LAPIS_ORE.get());
					out.accept(NorthstarBlocks.MOON_REDSTONE_ORE.get());
					out.accept(NorthstarBlocks.MOON_ZINC_ORE.get());
					
					out.accept(NorthstarBlocks.MOON_DEEP_STONE.get());			
					out.accept(NorthstarBlocks.MOON_DEEP_COPPER_ORE.get());	
					out.accept(NorthstarBlocks.MOON_DEEP_DIAMOND_ORE.get());
					out.accept(NorthstarBlocks.MOON_DEEP_GLOWSTONE_ORE.get());
					out.accept(NorthstarBlocks.MOON_DEEP_GOLD_ORE.get());
					out.accept(NorthstarBlocks.MOON_DEEP_IRON_ORE.get());
					out.accept(NorthstarBlocks.MOON_DEEP_LAPIS_ORE.get());
					out.accept(NorthstarBlocks.MOON_DEEP_REDSTONE_ORE.get());
					out.accept(NorthstarBlocks.MOON_DEEP_ZINC_ORE.get());
					
					out.accept(NorthstarBlocks.MOON_STONE_BRICKS.get());
					out.accept(NorthstarBlocks.MOON_STONE_BRICK_SLAB.get());
					out.accept(NorthstarBlocks.MOON_STONE_BRICK_SLAB_VERTICAL.get());
					out.accept(NorthstarBlocks.MOON_STONE_BRICK_STAIRS.get());
					out.accept(NorthstarBlocks.MOON_STONE_BRICK_WALL.get());
					out.accept(NorthstarBlocks.MOON_STONE_LAMP.get());
					out.accept(NorthstarBlocks.MOON_STONE_PILLAR.get());
					out.accept(NorthstarBlocks.POLISHED_MOON_STONE.get());
					out.accept(NorthstarBlocks.CHISELED_MOON_STONE.get());
					
					out.accept(NorthstarBlocks.LUNAR_SAPPHIRE_BLOCK.get());	
					out.accept(NorthstarBlocks.BUDDING_LUNAR_SAPPHIRE_BLOCK.get());	
					out.accept(NorthstarBlocks.SMALL_LUNAR_SAPPHIRE_BUD.get());	
					out.accept(NorthstarBlocks.MEDIUM_LUNAR_SAPPHIRE_BUD.get());	
					out.accept(NorthstarBlocks.LARGE_LUNAR_SAPPHIRE_BUD.get());	
					out.accept(NorthstarBlocks.LUNAR_SAPPHIRE_CLUSTER.get());	
					
					out.accept(NorthstarBlocks.MARS_SAND.get());	
					out.accept(NorthstarBlocks.MARS_SOIL.get());		
					out.accept(NorthstarBlocks.MARS_FARMLAND.get());	
					out.accept(NorthstarBlocks.MARS_GRAVEL.get());		
					out.accept(NorthstarBlocks.MARTIAN_GRASS.get());	
					out.accept(NorthstarBlocks.MARTIAN_TALL_GRASS.get());	
					out.accept(NorthstarItems.MARS_SPROUT_FLOWER.get());	
					out.accept(NorthstarBlocks.MARS_SPROUT_BIG.get());	
					out.accept(NorthstarItems.MARS_PALM_FLOWER.get());	
					out.accept(NorthstarItems.MARS_TULIP_FLOWER.get());	
					
					out.accept(NorthstarBlocks.MARS_STONE.get());			
					out.accept(NorthstarBlocks.MARS_COPPER_ORE.get());	
					out.accept(NorthstarBlocks.MARS_DIAMOND_ORE.get());
					out.accept(NorthstarBlocks.MARS_GOLD_ORE.get());
					out.accept(NorthstarBlocks.MARS_IRON_ORE.get());
					out.accept(NorthstarBlocks.MARS_REDSTONE_ORE.get());
					out.accept(NorthstarBlocks.MARS_QUARTZ_ORE.get());
					
					out.accept(NorthstarBlocks.MARS_DEEP_STONE.get());			
					out.accept(NorthstarBlocks.MARS_DEEP_COPPER_ORE.get());	
					out.accept(NorthstarBlocks.MARS_DEEP_DIAMOND_ORE.get());
					out.accept(NorthstarBlocks.MARS_DEEP_GOLD_ORE.get());
					out.accept(NorthstarBlocks.MARS_DEEP_IRON_ORE.get());
					out.accept(NorthstarBlocks.MARS_DEEP_REDSTONE_ORE.get());
					out.accept(NorthstarBlocks.MARS_DEEP_QUARTZ_ORE.get());
					
					out.accept(NorthstarBlocks.MARS_STONE_BRICKS.get());
					out.accept(NorthstarBlocks.MARS_STONE_BRICK_SLAB.get());
					out.accept(NorthstarBlocks.MARS_STONE_BRICK_SLAB_VERTICAL.get());
					out.accept(NorthstarBlocks.MARS_STONE_BRICK_STAIRS.get());
					out.accept(NorthstarBlocks.MARS_STONE_BRICK_WALL.get());
					out.accept(NorthstarBlocks.MARS_STONE_LAMP.get());
					out.accept(NorthstarBlocks.MARS_STONE_PILLAR.get());
					out.accept(NorthstarBlocks.POLISHED_MARS_STONE.get());
					out.accept(NorthstarBlocks.CHISELED_MARS_STONE.get());
					
					out.accept(NorthstarBlocks.MARS_ROOTS.get());	
					out.accept(NorthstarBlocks.GLOWING_MARS_ROOTS.get());	
					out.accept(NorthstarBlocks.MARS_WORM_NEST.get());	
					out.accept(NorthstarBlocks.POINTED_CRIMSITE.get());	
					
					out.accept(NorthstarBlocks.ARGYRE_LOG.get());	
					out.accept(NorthstarBlocks.STRIPPED_ARGYRE_LOG.get());	
					out.accept(NorthstarBlocks.ARGYRE_PLANKS.get());	
					out.accept(NorthstarBlocks.ARGYRE_SLAB.get());	
					out.accept(NorthstarBlocks.ARGYRE_STAIRS.get());	
					out.accept(NorthstarBlocks.ARGYRE_LEAVES.get());
					out.accept(NorthstarBlocks.ARGYRE_SAPLING.get());	

					out.accept(NorthstarBlocks.COILER_LOG.get());	
					out.accept(NorthstarBlocks.STRIPPED_COILER_LOG.get());	
					out.accept(NorthstarBlocks.COILER_PLANKS.get());	
					out.accept(NorthstarBlocks.COILER_SLAB.get());	
					out.accept(NorthstarBlocks.COILER_STAIRS.get());	
					out.accept(NorthstarBlocks.COILER_LEAVES.get());
					out.accept(NorthstarBlocks.COILER_SAPLING.get());		
					
					out.accept(NorthstarBlocks.WILTER_LOG.get());	
					out.accept(NorthstarBlocks.STRIPPED_WILTER_LOG.get());	
					out.accept(NorthstarBlocks.WILTER_PLANKS.get());	
					out.accept(NorthstarBlocks.WILTER_SLAB.get());	
					out.accept(NorthstarBlocks.WILTER_STAIRS.get());	
					out.accept(NorthstarBlocks.WILTER_LEAVES.get());
					out.accept(NorthstarBlocks.WILTER_SAPLING.get());	
					
					out.accept(NorthstarBlocks.VENUS_GRAVEL.get());			
					out.accept(NorthstarBlocks.VENUS_STONE.get());			
					out.accept(NorthstarBlocks.VENUS_COAL_ORE.get());		
					out.accept(NorthstarBlocks.VENUS_COPPER_ORE.get());	
					out.accept(NorthstarBlocks.VENUS_DIAMOND_ORE.get());
					out.accept(NorthstarBlocks.VENUS_GLOWSTONE_ORE.get());
					out.accept(NorthstarBlocks.VENUS_GOLD_ORE.get());
					out.accept(NorthstarBlocks.VENUS_IRON_ORE.get());
					out.accept(NorthstarBlocks.VENUS_REDSTONE_ORE.get());
					out.accept(NorthstarBlocks.VENUS_QUARTZ_ORE.get());
					
					out.accept(NorthstarBlocks.VENUS_DEEP_STONE.get());			
					out.accept(NorthstarBlocks.VENUS_DEEP_COPPER_ORE.get());	
					out.accept(NorthstarBlocks.VENUS_DEEP_DIAMOND_ORE.get());
					out.accept(NorthstarBlocks.VENUS_DEEP_GLOWSTONE_ORE.get());
					out.accept(NorthstarBlocks.VENUS_DEEP_GOLD_ORE.get());
					out.accept(NorthstarBlocks.VENUS_DEEP_IRON_ORE.get());
					out.accept(NorthstarBlocks.VENUS_DEEP_REDSTONE_ORE.get());
					out.accept(NorthstarBlocks.VENUS_DEEP_REDSTONE_ORE.get());
					out.accept(NorthstarBlocks.VENUS_DEEP_QUARTZ_ORE.get());
					
					out.accept(NorthstarBlocks.VENUS_STONE_BRICKS.get());
					out.accept(NorthstarBlocks.VENUS_STONE_BRICK_SLAB.get());
					out.accept(NorthstarBlocks.VENUS_STONE_BRICK_SLAB_VERTICAL.get());
					out.accept(NorthstarBlocks.VENUS_STONE_BRICK_STAIRS.get());
					out.accept(NorthstarBlocks.VENUS_STONE_BRICK_WALL.get());
					out.accept(NorthstarBlocks.VENUS_STONE_LAMP.get());
					out.accept(NorthstarBlocks.VENUS_STONE_PILLAR.get());
					out.accept(NorthstarBlocks.POLISHED_VENUS_STONE.get());
					out.accept(NorthstarBlocks.CHISELED_VENUS_STONE.get());	
					
					out.accept(NorthstarBlocks.VENUS_PLUME.get());	
					out.accept(NorthstarBlocks.VENUS_VINES.get());	
					out.accept(NorthstarBlocks.GLOWING_VENUS_VINES.get());	
					
					out.accept(NorthstarBlocks.TALL_VENUS_MYCELIUM.get());	
					out.accept(NorthstarBlocks.SPIKE_FUNGUS.get());	
					out.accept(NorthstarBlocks.SPIKE_FUNGUS_BLOCK.get());	
					out.accept(NorthstarBlocks.BLOOM_FUNGUS.get());	
					out.accept(NorthstarBlocks.BLOOM_FUNGUS_BLOCK.get());	
					out.accept(NorthstarBlocks.BLOOM_FUNGUS_STEM_BLOCK.get());	
					out.accept(NorthstarBlocks.PLATE_FUNGUS.get());	
					out.accept(NorthstarBlocks.PLATE_FUNGUS_CAP_BLOCK.get());	
					out.accept(NorthstarBlocks.PLATE_FUNGUS_STEM_BLOCK.get());	
					out.accept(NorthstarBlocks.TOWER_FUNGUS.get());	
					out.accept(NorthstarBlocks.TOWER_FUNGUS_CAP_BLOCK.get());	
					out.accept(NorthstarBlocks.TOWER_FUNGUS_STEM_BLOCK.get());	
						
					out.accept(NorthstarBlocks.MERCURY_STONE.get());			
					out.accept(NorthstarBlocks.MERCURY_COPPER_ORE.get());	
					out.accept(NorthstarBlocks.MERCURY_DIAMOND_ORE.get());
					out.accept(NorthstarBlocks.MERCURY_GLOWSTONE_ORE.get());
					out.accept(NorthstarBlocks.MERCURY_GOLD_ORE.get());
					out.accept(NorthstarBlocks.MERCURY_IRON_ORE.get());
					out.accept(NorthstarBlocks.MERCURY_REDSTONE_ORE.get());
					out.accept(NorthstarBlocks.MERCURY_LAPIS_ORE.get());
					out.accept(NorthstarBlocks.MERCURY_TUNGSTEN_ORE.get());
					out.accept(NorthstarBlocks.MERCURY_ZINC_ORE.get());
					
					out.accept(NorthstarBlocks.MERCURY_DEEP_STONE.get());			
					out.accept(NorthstarBlocks.MERCURY_DEEP_COPPER_ORE.get());	
					out.accept(NorthstarBlocks.MERCURY_DEEP_DIAMOND_ORE.get());
					out.accept(NorthstarBlocks.MERCURY_DEEP_GLOWSTONE_ORE.get());
					out.accept(NorthstarBlocks.MERCURY_DEEP_GOLD_ORE.get());
					out.accept(NorthstarBlocks.MERCURY_DEEP_IRON_ORE.get());
					out.accept(NorthstarBlocks.MERCURY_DEEP_REDSTONE_ORE.get());
					out.accept(NorthstarBlocks.MERCURY_DEEP_LAPIS_ORE.get());
					out.accept(NorthstarBlocks.MERCURY_DEEP_TUNGSTEN_ORE.get());
					out.accept(NorthstarBlocks.MERCURY_DEEP_ZINC_ORE.get());
					
					out.accept(NorthstarBlocks.MERCURY_STONE_BRICKS.get());
					out.accept(NorthstarBlocks.MERCURY_STONE_BRICK_SLAB.get());
					out.accept(NorthstarBlocks.MERCURY_STONE_BRICK_SLAB_VERTICAL.get());
					out.accept(NorthstarBlocks.MERCURY_STONE_BRICK_STAIRS.get());
					out.accept(NorthstarBlocks.MERCURY_STONE_BRICK_WALL.get());
					out.accept(NorthstarBlocks.MERCURY_STONE_LAMP.get());
					out.accept(NorthstarBlocks.MERCURY_STONE_PILLAR.get());
					out.accept(NorthstarBlocks.POLISHED_MERCURY_STONE.get());
					out.accept(NorthstarBlocks.CHISELED_MERCURY_STONE.get());
					
					out.accept(NorthstarBlocks.MERCURY_CACTUS.get());
					out.accept(NorthstarBlocks.MERCURY_SHELF_FUNGUS.get());
					out.accept(NorthstarBlocks.MERCURY_SHELF_FUNGUS_BLOCK.get());
					
					out.accept(NorthstarBlocks.CALORIAN_LOG.get());	
					out.accept(NorthstarBlocks.CALORIAN_PLANKS.get());	
					out.accept(NorthstarBlocks.CALORIAN_SLAB.get());	
					out.accept(NorthstarBlocks.CALORIAN_STAIRS.get());
					
					out.accept(NorthstarBlocks.MONOLITHITE.get());

				})
				.build());
	public static final RegistryObject<CreativeModeTab> NORTHSTAR_TECH = REGISTER.register("northstartech",
			() -> CreativeModeTab.builder()
				.title(Components.translatable("itemGroup.northstartech"))
				.icon(() -> new ItemStack(NorthstarBlocks.TELESCOPE.get()))
				.displayItems((pParameters, out) -> {
					out.accept(NorthstarTechBlocks.AMETHYST_CRYSTAL.get());
					out.accept(NorthstarTechBlocks.LUNAR_SAPPHIRE_CRYSTAL.get());
					out.accept(NorthstarTechBlocks.IRON_COGWHEEL.get());
					out.accept(NorthstarTechBlocks.LASER_LENSE.get());
					out.accept(NorthstarBlocks.TELESCOPE.get());
					out.accept(NorthstarTechBlocks.ASTRONOMY_TABLE.get());
					out.accept(NorthstarTechBlocks.OXYGEN_CONCENTRATOR.get());
					out.accept(NorthstarTechBlocks.SOLAR_PANEL.get());
					out.accept(NorthstarTechBlocks.CIRCUIT_ENGRAVER.get());
					out.accept(NorthstarTechBlocks.OXYGEN_CONCENTRATOR.get());
					out.accept(NorthstarTechBlocks.OXYGEN_FILLER.get());
					out.accept(NorthstarTechBlocks.OXYGEN_GENERATOR.get());
					out.accept(NorthstarTechBlocks.OXYGEN_DETECTOR.get());
					out.accept(NorthstarTechBlocks.COMBUSTION_ENGINE.get());
					out.accept(NorthstarTechBlocks.ELECTROLYSIS_MACHINE.get());
					out.accept(NorthstarTechBlocks.TEMPERATURE_REGULATOR.get());
					out.accept(NorthstarTechBlocks.ICE_BOX.get());
					out.accept(NorthstarTechBlocks.JET_ENGINE.get());
					out.accept(NorthstarTechBlocks.ROCKET_CONTROLS.get());
					out.accept(NorthstarTechBlocks.ROCKET_STATION.get());
					out.accept(NorthstarTechBlocks.IRON_SPACE_DOOR.get());
					out.accept(NorthstarTechBlocks.COMPUTER_RACK.get());
					out.accept(NorthstarBlocks.INTERPLANETARY_NAVIGATOR.get());
				})
				.build());
	
	
	public static void register(IEventBus modEventBus) {
		REGISTER.register(modEventBus);
	}

	
	private static class RegistrateDisplayItemsGenerator implements DisplayItemsGenerator {
		private static final Predicate<Item> IS_ITEM_3D_PREDICATE;

		static {
			MutableObject<Predicate<Item>> isItem3d = new MutableObject<>(item -> false);
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				isItem3d.setValue(item -> {
					ItemRenderer itemRenderer = Minecraft.getInstance()
						.getItemRenderer();
					BakedModel model = itemRenderer.getModel(new ItemStack(item), null, null, 0);
					return model.isGui3d();
				});
			});
			IS_ITEM_3D_PREDICATE = isItem3d.getValue();
		}

		@OnlyIn(Dist.CLIENT)
		private static Predicate<Item> makeClient3dItemPredicate() {
			return item -> {
				ItemRenderer itemRenderer = Minecraft.getInstance()
					.getItemRenderer();
				BakedModel model = itemRenderer.getModel(new ItemStack(item), null, null, 0);
				return model.isGui3d();
			};
		}

		private final boolean addItems;
		private final RegistryObject<CreativeModeTab> tabFilter;

		public RegistrateDisplayItemsGenerator(boolean addItems, RegistryObject<CreativeModeTab> tabFilter) {
			this.addItems = addItems;
			this.tabFilter = tabFilter;
		}

		private static Predicate<Item> makeExclusionPredicate() {
			Set<Item> exclusions = new ReferenceOpenHashSet<>();

			List<ItemProviderEntry<?>> simpleExclusions = List.of(
					AllItems.INCOMPLETE_PRECISION_MECHANISM,
					AllItems.INCOMPLETE_REINFORCED_SHEET,
					AllItems.INCOMPLETE_TRACK,
					AllItems.CHROMATIC_COMPOUND,
					AllItems.SHADOW_STEEL,
					AllItems.REFINED_RADIANCE,
					AllItems.COPPER_BACKTANK_PLACEABLE,
					AllItems.NETHERITE_BACKTANK_PLACEABLE,
					AllItems.MINECART_CONTRAPTION,
					AllItems.FURNACE_MINECART_CONTRAPTION,
					AllItems.CHEST_MINECART_CONTRAPTION,
					AllItems.SCHEMATIC,
					AllBlocks.ANDESITE_ENCASED_SHAFT,
					AllBlocks.BRASS_ENCASED_SHAFT,
					AllBlocks.ANDESITE_ENCASED_COGWHEEL,
					AllBlocks.BRASS_ENCASED_COGWHEEL,
					AllBlocks.ANDESITE_ENCASED_LARGE_COGWHEEL,
					AllBlocks.BRASS_ENCASED_LARGE_COGWHEEL,
					AllBlocks.MYSTERIOUS_CUCKOO_CLOCK,
					AllBlocks.ELEVATOR_CONTACT,
					AllBlocks.SHADOW_STEEL_CASING,
					AllBlocks.REFINED_RADIANCE_CASING
			);

			List<ItemEntry<TagDependentIngredientItem>> tagDependentExclusions = List.of(
					AllItems.CRUSHED_OSMIUM,
					AllItems.CRUSHED_PLATINUM,
					AllItems.CRUSHED_SILVER,
					AllItems.CRUSHED_TIN,
					AllItems.CRUSHED_LEAD,
					AllItems.CRUSHED_QUICKSILVER,
					AllItems.CRUSHED_BAUXITE,
					AllItems.CRUSHED_URANIUM,
					AllItems.CRUSHED_NICKEL
			);

			for (ItemProviderEntry<?> entry : simpleExclusions) {
				exclusions.add(entry.asItem());
			}

			for (ItemEntry<TagDependentIngredientItem> entry : tagDependentExclusions) {
				TagDependentIngredientItem item = entry.get();
				if (item.shouldHide()) {
					exclusions.add(entry.asItem());
				}
			}

			return exclusions::contains;
		}

		private static List<ItemOrdering> makeOrderings() {
			List<ItemOrdering> orderings = new ReferenceArrayList<>();

			Map<ItemProviderEntry<?>, ItemProviderEntry<?>> simpleBeforeOrderings = Map.of(
					AllItems.EMPTY_BLAZE_BURNER, AllBlocks.BLAZE_BURNER,
					AllItems.SCHEDULE, AllBlocks.TRACK_STATION
			);

			Map<ItemProviderEntry<?>, ItemProviderEntry<?>> simpleAfterOrderings = Map.of(
					AllItems.VERTICAL_GEARBOX, AllBlocks.GEARBOX
			);

			simpleBeforeOrderings.forEach((entry, otherEntry) -> {
				orderings.add(ItemOrdering.before(entry.asItem(), otherEntry.asItem()));
			});

			simpleAfterOrderings.forEach((entry, otherEntry) -> {
				orderings.add(ItemOrdering.after(entry.asItem(), otherEntry.asItem()));
			});

			return orderings;
		}

		private static Function<Item, ItemStack> makeStackFunc() {
			Map<Item, Function<Item, ItemStack>> factories = new Reference2ReferenceOpenHashMap<>();

			Map<ItemProviderEntry<?>, Function<Item, ItemStack>> simpleFactories = Map.of(
					AllItems.COPPER_BACKTANK, item -> {
						ItemStack stack = new ItemStack(item);
						stack.getOrCreateTag().putInt("Air", BacktankUtil.maxAirWithoutEnchants());
						return stack;
					},
					AllItems.NETHERITE_BACKTANK, item -> {
						ItemStack stack = new ItemStack(item);
						stack.getOrCreateTag().putInt("Air", BacktankUtil.maxAirWithoutEnchants());
						return stack;
					}
			);

			simpleFactories.forEach((entry, factory) -> {
				factories.put(entry.asItem(), factory);
			});

			return item -> {
				Function<Item, ItemStack> factory = factories.get(item);
				if (factory != null) {
					return factory.apply(item);
				}
				return new ItemStack(item);
			};
		}

		private static Function<Item, TabVisibility> makeVisibilityFunc() {
			Map<Item, TabVisibility> visibilities = new Reference2ObjectOpenHashMap<>();

			Map<ItemProviderEntry<?>, TabVisibility> simpleVisibilities = Map.of(
					AllItems.BLAZE_CAKE_BASE, TabVisibility.SEARCH_TAB_ONLY
			);

			simpleVisibilities.forEach((entry, factory) -> {
				visibilities.put(entry.asItem(), factory);
			});

			for (BlockEntry<ValveHandleBlock> entry : AllBlocks.DYED_VALVE_HANDLES) {
				visibilities.put(entry.asItem(), TabVisibility.SEARCH_TAB_ONLY);
			}

			for (BlockEntry<SeatBlock> entry : AllBlocks.SEATS) {
				SeatBlock block = entry.get();
				if (block.getColor() != DyeColor.RED) {
					visibilities.put(entry.asItem(), TabVisibility.SEARCH_TAB_ONLY);
				}
			}

			for (BlockEntry<ToolboxBlock> entry : AllBlocks.TOOLBOXES) {
				ToolboxBlock block = entry.get();
				if (block.getColor() != DyeColor.BROWN) {
					visibilities.put(entry.asItem(), TabVisibility.SEARCH_TAB_ONLY);
				}
			}

			return item -> {
				TabVisibility visibility = visibilities.get(item);
				if (visibility != null) {
					return visibility;
				}
				return TabVisibility.PARENT_AND_SEARCH_TABS;
			};
		}

		@Override
		public void accept(ItemDisplayParameters parameters, Output output) {
			Predicate<Item> exclusionPredicate = makeExclusionPredicate();
			List<ItemOrdering> orderings = makeOrderings();
			Function<Item, ItemStack> stackFunc = makeStackFunc();
			Function<Item, TabVisibility> visibilityFunc = makeVisibilityFunc();

			List<Item> items = new LinkedList<>();
			if (addItems) {
				items.addAll(collectItems(exclusionPredicate.or(IS_ITEM_3D_PREDICATE.negate())));
			}
			items.addAll(collectBlocks(exclusionPredicate));
			if (addItems) {
				items.addAll(collectItems(exclusionPredicate.or(IS_ITEM_3D_PREDICATE)));
			}

			applyOrderings(items, orderings);
			outputAll(output, items, stackFunc, visibilityFunc);
		}

		private List<Item> collectBlocks(Predicate<Item> exclusionPredicate) {
			List<Item> items = new ReferenceArrayList<>();
			for (RegistryEntry<Block> entry : Northstar.REGISTRATE.getAll(Registries.BLOCK)) {
				if (!CreateRegistrate.isInCreativeTab(entry, tabFilter))
					continue;
				Item item = entry.get()
					.asItem();
				if (item == Items.AIR)
					continue;
				if (!exclusionPredicate.test(item))
					items.add(item);
			}
			items = new ReferenceArrayList<>(new ReferenceLinkedOpenHashSet<>(items));
			return items;
		}

		private List<Item> collectItems(Predicate<Item> exclusionPredicate) {
			List<Item> items = new ReferenceArrayList<>();
			for (RegistryEntry<Item> entry : Northstar.REGISTRATE.getAll(Registries.ITEM)) {
				if (!CreateRegistrate.isInCreativeTab(entry, tabFilter))
					continue;
				Item item = entry.get();
				if (item instanceof BlockItem)
					continue;
				if (!exclusionPredicate.test(item))
					items.add(item);
			}
			return items;
		}

		private static void applyOrderings(List<Item> items, List<ItemOrdering> orderings) {
			for (ItemOrdering ordering : orderings) {
				int anchorIndex = items.indexOf(ordering.anchor());
				if (anchorIndex != -1) {
					Item item = ordering.item();
					int itemIndex = items.indexOf(item);
					if (itemIndex != -1) {
						items.remove(itemIndex);
						if (itemIndex < anchorIndex) {
							anchorIndex--;
						}
					}
					if (ordering.type() == ItemOrdering.Type.AFTER) {
						items.add(anchorIndex + 1, item);
					} else {
						items.add(anchorIndex, item);
					}
				}
			}
		}

		private static void outputAll(Output output, List<Item> items, Function<Item, ItemStack> stackFunc, Function<Item, TabVisibility> visibilityFunc) {
			for (Item item : items) {
				output.accept(stackFunc.apply(item), visibilityFunc.apply(item));
			}
		}

		private record ItemOrdering(Item item, Item anchor, Type type) {
			public static ItemOrdering before(Item item, Item anchor) {
				return new ItemOrdering(item, anchor, Type.BEFORE);
			}

			public static ItemOrdering after(Item item, Item anchor) {
				return new ItemOrdering(item, anchor, Type.AFTER);
			}

			public enum Type {
				BEFORE,
				AFTER;
			}
		}
	}
	
	public static void addStarMaps(CreativeModeTab.Output pGroup) {
			ItemStack earth = new ItemStack(NorthstarItems.STAR_MAP.get());
			earth.setHoverName(Component.translatable("item.northstar.star_map" + "_" + "earth").setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA).withItalic(false)));
	        CompoundTag earthTag = earth.getOrCreateTagElement("Planet");
	        earthTag.putString("name", "earth");
	        pGroup.accept(earth);
			ItemStack moon = new ItemStack(NorthstarItems.STAR_MAP.get());
			moon.setHoverName(Component.translatable("item.northstar.star_map" + "_" + "moon").setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA).withItalic(false)));
	        CompoundTag moonTag = moon.getOrCreateTagElement("Planet");
	        moonTag.putString("name", "moon");
	        pGroup.accept(moon);
			ItemStack mars = new ItemStack(NorthstarItems.STAR_MAP.get());
			mars.setHoverName(Component.translatable("item.northstar.star_map" + "_" + "mars").setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA).withItalic(false)));
	        CompoundTag marsTag = mars.getOrCreateTagElement("Planet");
	        marsTag.putString("name", "mars");
	        pGroup.accept(mars);
			ItemStack mercury = new ItemStack(NorthstarItems.STAR_MAP.get());
			mercury.setHoverName(Component.translatable("item.northstar.star_map" + "_" + "mercury").setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA).withItalic(false)));
	        CompoundTag mercuryTag = mercury.getOrCreateTagElement("Planet");
	        mercuryTag.putString("name", "mercury");
	        pGroup.accept(mercury);
			ItemStack venus = new ItemStack(NorthstarItems.STAR_MAP.get());
			venus.setHoverName(Component.translatable("item.northstar.star_map" + "_" + "venus").setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA).withItalic(false)));
	        CompoundTag venusTag = venus.getOrCreateTagElement("Planet");
	        venusTag.putString("name", "venus");
	        pGroup.accept(venus);
    }

	public static void addOxyItem(CreativeModeTab.Output pGroup, Item item) {
		ItemStack stack = new ItemStack(item);
		CompoundTag nbt = new CompoundTag();
		nbt.putInt("Oxygen", OxygenStuff.maximumOxy);
		stack.setTag(nbt);
		if(stack.is(NorthstarItemTags.OXYGEN_SOURCES.tag)) {
			ListTag lore = new ListTag();
			lore.add(StringTag.valueOf(Component.Serializer.toJson(Component.literal( "Oxygen: " + OxygenStuff.maximumOxy + "mb").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY).withItalic(false))).toString())); 
			stack.getOrCreateTagElement("display").put("Lore", lore);
		}
		pGroup.accept(stack);
	}
}