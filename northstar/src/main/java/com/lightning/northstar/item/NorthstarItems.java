package com.lightning.northstar.item;

import static com.lightning.northstar.Northstar.REGISTRATE;

import com.lightning.northstar.Northstar;
import com.lightning.northstar.block.NorthstarBlocks;
import com.lightning.northstar.block.NorthstarTechBlocks;
import com.lightning.northstar.block.crops.SeedItem;
import com.lightning.northstar.entity.NorthstarEntityTypes;
import com.lightning.northstar.item.armor.BrokenIronSpaceSuitArmorItem;
import com.lightning.northstar.item.armor.IronSpaceSuitArmorItem;
import com.lightning.northstar.item.armor.MartianSteelSpaceSuitArmorItem;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.equipment.sandPaper.SandPaperItem;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.tterrag.registrate.util.entry.ItemEntry;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class NorthstarItems {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Northstar.MOD_ID);
	static {
		REGISTRATE.creativeModeTab(() -> NorthstarCreativeModeTab.NORTHSTAR_TAB);
	}

	public static final RegistryObject<Item> RAW_MARTIAN_IRON_ORE = ITEMS.register("raw_martian_iron_ore", () -> new Item(new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
	public static final RegistryObject<Item> RAW_TUNGSTEN_ORE = ITEMS.register("raw_tungsten_ore", () -> new Item(new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
	public static final RegistryObject<Item> RAW_GLOWSTONE_ORE = ITEMS.register("raw_glowstone_ore", () -> new Item(new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
	public static final RegistryObject<Item> ENRICHED_GLOWSTONE_ORE = ITEMS.register("enriched_glowstone_ore", () -> new Item(new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
	
	public static final RegistryObject<Item> MARTIAN_STEEL = ITEMS.register("martian_steel", () -> new Item(new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
	public static final RegistryObject<Item> MARTIAN_STEEL_SHEET = ITEMS.register("martian_steel_sheet", () -> new Item(new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
	public static final RegistryObject<Item> TUNGSTEN_INGOT = ITEMS.register("tungsten_ingot", () -> new Item(new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
	public static final RegistryObject<Item> TUNGSTEN_SHEET = ITEMS.register("tungsten_sheet", () -> new Item(new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
	
	
	public static final RegistryObject<StandingAndWallBlockItem> EXTINGUISHED_TORCH = ITEMS.register("extinguished_torch", () -> new StandingAndWallBlockItem(NorthstarTechBlocks.EXTINGUISHED_TORCH.get(), NorthstarTechBlocks.EXTINGUISHED_TORCH_WALL.get(), new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
	public static final RegistryObject<StandingAndWallBlockItem> GLOWSTONE_TORCH = ITEMS.register("glowstone_torch", () -> new StandingAndWallBlockItem(NorthstarTechBlocks.GLOWSTONE_TORCH.get(), NorthstarTechBlocks.GLOWSTONE_TORCH_WALL.get(), new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));

	public static final RegistryObject<Item> DORMANT_MARTIAN_SAPLING = ITEMS.register("dormant_martian_sapling", () -> new Item(new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
	public static final ItemEntry<SequencedAssemblyItem> DORMANT_MARTIAN_SAPLING_SEQUENCED = sequencedIngredient("dormant_martian_sapling_sequenced");
	
	public static final RegistryObject<Item> DORMANT_MARTIAN_SEED = ITEMS.register("dormant_martian_seed", () -> new Item(new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
	public static final ItemEntry<SequencedAssemblyItem> DORMANT_MARTIAN_SEED_SEQUENCED = sequencedIngredient("dormant_martian_seed_sequenced");
	
    public static final RegistryObject<Item> MARTIAN_SWORD = ITEMS.register("martian_sword",() -> new SwordItem(NorthstarToolTiers.MARTIAN_STEEL, 3, -2.4f,new Item.Properties().stacksTo(1).tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
    public static final RegistryObject<Item> MARTIAN_PICKAXE = ITEMS.register("martian_pickaxe",() -> new PickaxeItem(NorthstarToolTiers.MARTIAN_STEEL, 1, -2.8f,new Item.Properties().stacksTo(1).tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
    public static final RegistryObject<Item> MARTIAN_SHOVEL = ITEMS.register("martian_shovel",() -> new ShovelItem(NorthstarToolTiers.MARTIAN_STEEL, 1.5f, -3.0f,new Item.Properties().stacksTo(1).tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
    public static final RegistryObject<Item> MARTIAN_AXE = ITEMS.register("martian_axe",() -> new AxeItem(NorthstarToolTiers.MARTIAN_STEEL, 5, -3.0f,new Item.Properties().stacksTo(1).tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
    public static final RegistryObject<Item> MARTIAN_HOE = ITEMS.register("martian_hoe",() -> new HoeItem(NorthstarToolTiers.MARTIAN_STEEL, -2, 0.0f,new Item.Properties().stacksTo(1).tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
    
    public static final RegistryObject<Item> MARTIAN_STEEL_HELMET = ITEMS.register("martian_steel_helmet",() -> new ArmorItem(NorthstarArmorTiers.MARTIAN_STEEL_ARMOR, EquipmentSlot.HEAD, new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
    public static final RegistryObject<Item> MARTIAN_STEEL_CHESTPLATE = ITEMS.register("martian_steel_chestplate",() -> new ArmorItem(NorthstarArmorTiers.MARTIAN_STEEL_ARMOR, EquipmentSlot.CHEST, new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
    public static final RegistryObject<Item> MARTIAN_STEEL_LEGGINGS = ITEMS.register("martian_steel_leggings",() -> new ArmorItem(NorthstarArmorTiers.MARTIAN_STEEL_ARMOR, EquipmentSlot.LEGS, new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
    public static final RegistryObject<Item> MARTIAN_STEEL_BOOTS = ITEMS.register("martian_steel_boots",() -> new ArmorItem(NorthstarArmorTiers.MARTIAN_STEEL_ARMOR, EquipmentSlot.FEET, new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
    
    public static final RegistryObject<IronSpaceSuitArmorItem> IRON_SPACE_SUIT_HELMET = ITEMS.register("iron_space_suit_helmet",() -> new IronSpaceSuitArmorItem(NorthstarArmorTiers.IRON_SPACE_SUIT, EquipmentSlot.HEAD, new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
    public static final RegistryObject<IronSpaceSuitArmorItem> IRON_SPACE_SUIT_CHESTPIECE = ITEMS.register("iron_space_suit_chestpiece",() -> new IronSpaceSuitArmorItem(NorthstarArmorTiers.IRON_SPACE_SUIT, EquipmentSlot.CHEST, new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
    public static final RegistryObject<IronSpaceSuitArmorItem> IRON_SPACE_SUIT_LEGGINGS = ITEMS.register("iron_space_suit_leggings",() -> new IronSpaceSuitArmorItem(NorthstarArmorTiers.IRON_SPACE_SUIT, EquipmentSlot.LEGS, new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
    public static final RegistryObject<IronSpaceSuitArmorItem> IRON_SPACE_SUIT_BOOTS = ITEMS.register("iron_space_suit_boots",() -> new IronSpaceSuitArmorItem(NorthstarArmorTiers.IRON_SPACE_SUIT, EquipmentSlot.FEET, new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
    
    public static final RegistryObject<MartianSteelSpaceSuitArmorItem> MARTIAN_STEEL_SPACE_SUIT_HELMET = ITEMS.register("martian_steel_space_suit_helmet",() -> new MartianSteelSpaceSuitArmorItem(NorthstarArmorTiers.MARTIAN_STEEL_SPACE_SUIT, EquipmentSlot.HEAD, new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
    public static final RegistryObject<MartianSteelSpaceSuitArmorItem> MARTIAN_STEEL_SPACE_SUIT_CHESTPIECE = ITEMS.register("martian_steel_space_suit_chestpiece",() -> new MartianSteelSpaceSuitArmorItem(NorthstarArmorTiers.MARTIAN_STEEL_SPACE_SUIT, EquipmentSlot.CHEST, new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
    public static final RegistryObject<MartianSteelSpaceSuitArmorItem> MARTIAN_STEEL_SPACE_SUIT_LEGGINGS = ITEMS.register("martian_steel_space_suit_leggings",() -> new MartianSteelSpaceSuitArmorItem(NorthstarArmorTiers.MARTIAN_STEEL_SPACE_SUIT, EquipmentSlot.LEGS, new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
    public static final RegistryObject<MartianSteelSpaceSuitArmorItem> MARTIAN_STEEL_SPACE_SUIT_BOOTS = ITEMS.register("martian_steel_space_suit_boots",() -> new MartianSteelSpaceSuitArmorItem(NorthstarArmorTiers.MARTIAN_STEEL_SPACE_SUIT, EquipmentSlot.FEET, new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
    
    
    public static final RegistryObject<BrokenIronSpaceSuitArmorItem> BROKEN_IRON_SPACE_SUIT_HELMET = ITEMS.register("broken_iron_space_suit_helmet",() -> new BrokenIronSpaceSuitArmorItem(NorthstarArmorTiers.IRON_SPACE_SUIT, EquipmentSlot.HEAD, new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
    public static final RegistryObject<BrokenIronSpaceSuitArmorItem> BROKEN_IRON_SPACE_SUIT_CHESTPIECE = ITEMS.register("broken_iron_space_suit_chestpiece",() -> new BrokenIronSpaceSuitArmorItem(NorthstarArmorTiers.IRON_SPACE_SUIT, EquipmentSlot.CHEST, new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
    public static final RegistryObject<BrokenIronSpaceSuitArmorItem> BROKEN_IRON_SPACE_SUIT_LEGGINGS = ITEMS.register("broken_iron_space_suit_leggings",() -> new BrokenIronSpaceSuitArmorItem(NorthstarArmorTiers.IRON_SPACE_SUIT, EquipmentSlot.LEGS, new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
    public static final RegistryObject<BrokenIronSpaceSuitArmorItem> BROKEN_IRON_SPACE_SUIT_BOOTS = ITEMS.register("broken_iron_space_suit_boots",() -> new BrokenIronSpaceSuitArmorItem(NorthstarArmorTiers.IRON_SPACE_SUIT, EquipmentSlot.FEET, new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
    
    public static final RegistryObject<ForgeSpawnEggItem> MARS_WORM_SPAWN_EGG = ITEMS.register("mars_worm_spawn_egg",
            () -> new ForgeSpawnEggItem(NorthstarEntityTypes.MARS_WORM, 0xC3B1A9, 0x624234, new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
    public static final RegistryObject<ForgeSpawnEggItem> MARS_TOAD_SPAWN_EGG = ITEMS.register("mars_toad_spawn_egg",
            () -> new ForgeSpawnEggItem(NorthstarEntityTypes.MARS_TOAD, 0xa3907c, 0x716252, new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
    public static final RegistryObject<ForgeSpawnEggItem> MARS_COBRA_SPAWN_EGG = ITEMS.register("mars_cobra_spawn_egg",
            () -> new ForgeSpawnEggItem(NorthstarEntityTypes.MARS_COBRA, 0xc19c85, 0xccb086, new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
    public static final RegistryObject<ForgeSpawnEggItem> MARS_MOTH_SPAWN_EGG = ITEMS.register("mars_moth_spawn_egg",
            () -> new ForgeSpawnEggItem(NorthstarEntityTypes.MARS_MOTH, 0xb35525, 0x493124, new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
    
    public static final RegistryObject<ForgeSpawnEggItem> VENUS_MIMIC_SPAWN_EGG = ITEMS.register("venus_mimic_spawn_egg",
            () -> new ForgeSpawnEggItem(NorthstarEntityTypes.VENUS_MIMIC, 0x8e755b, 0x65553e, new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
    public static final RegistryObject<ForgeSpawnEggItem> VENUS_SCORPION_SPAWN_EGG = ITEMS.register("venus_scorpion_spawn_egg",
            () -> new ForgeSpawnEggItem(NorthstarEntityTypes.VENUS_SCORPION, 0x8f7450, 0x6bc18d, new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
    public static final RegistryObject<ForgeSpawnEggItem> VENUS_STONE_BULL_SPAWN_EGG = ITEMS.register("venus_stone_bull_spawn_egg",
            () -> new ForgeSpawnEggItem(NorthstarEntityTypes.VENUS_STONE_BULL, 0x79674f, 0x3a2417, new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
    public static final RegistryObject<ForgeSpawnEggItem> VENUS_VULTURE_SPAWN_EGG = ITEMS.register("venus_vulture_spawn_egg",
            () -> new ForgeSpawnEggItem(NorthstarEntityTypes.VENUS_VULTURE, 0x99826a, 0x813024, new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
    public static final RegistryObject<ForgeSpawnEggItem> FROZEN_ZOMBIE_SPAWN_EGG = ITEMS.register("frozen_zombie_spawn_egg",
            () -> new ForgeSpawnEggItem(NorthstarEntityTypes.FROZEN_ZOMBIE, 0x62a9bc, 0x4a695e, new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
    

    public static final RegistryObject<ForgeSpawnEggItem> MOON_LUNARGRADE_SPAWN_EGG = ITEMS.register("moon_lunargrade_spawn_egg",
            () -> new ForgeSpawnEggItem(NorthstarEntityTypes.MOON_LUNARGRADE, 0xa3afb4, 0x2b424c, new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
    public static final RegistryObject<ForgeSpawnEggItem> MOON_SNAIL_SPAWN_EGG = ITEMS.register("moon_snail_spawn_egg",
            () -> new ForgeSpawnEggItem(NorthstarEntityTypes.MOON_SNAIL, 0x7fab98, 0x3d676d, new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
    public static final RegistryObject<ForgeSpawnEggItem> MOON_EEL_SPAWN_EGG = ITEMS.register("moon_eel_spawn_egg",
            () -> new ForgeSpawnEggItem(NorthstarEntityTypes.MOON_EEL, 0xa3afb4, 0x58223e, new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
    
    public static final RegistryObject<ForgeSpawnEggItem> MERCURY_RAPTOR_SPAWN_EGG = ITEMS.register("mercury_raptor_spawn_egg",
            () -> new ForgeSpawnEggItem(NorthstarEntityTypes.MERCURY_RAPTOR, 0x88757f, 0x79636e, new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
    public static final RegistryObject<ForgeSpawnEggItem> MERCURY_ROACH_SPAWN_EGG = ITEMS.register("mercury_roach_spawn_egg",
            () -> new ForgeSpawnEggItem(NorthstarEntityTypes.MERCURY_ROACH, 0x8f7683, 0x53424a, new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
    public static final RegistryObject<ForgeSpawnEggItem> MERCURY_TORTOISE_SPAWN_EGG = ITEMS.register("mercury_tortoise_spawn_egg",
            () -> new ForgeSpawnEggItem(NorthstarEntityTypes.MERCURY_TORTOISE, 0x877b81, 0x6b5b64, new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
    
    public static final RegistryObject<ItemNameBlockItem> MARS_TULIP_SEEDS = ITEMS.register("mars_tulip_seeds",() -> new ItemNameBlockItem(NorthstarBlocks.MARS_TULIP.get(), new Item.Properties()));
    public static final RegistryObject<ItemNameBlockItem> MARS_PALM_SEEDS = ITEMS.register("mars_palm_seeds",() -> new ItemNameBlockItem(NorthstarBlocks.MARS_PALM.get(), new Item.Properties()));
    public static final RegistryObject<ItemNameBlockItem> MARS_SPROUT_SEEDS = ITEMS.register("mars_sprout_seeds",() -> new ItemNameBlockItem(NorthstarBlocks.MARS_SPROUT.get(), new Item.Properties()));
    
    public static final RegistryObject<MartianFlowerItem> MARS_TULIP_FLOWER = ITEMS.register("mars_tulip_flower",() -> new MartianFlowerItem(NorthstarBlocks.MARS_TULIP.get(), new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
    public static final RegistryObject<MartianFlowerItem> MARS_PALM_FLOWER = ITEMS.register("mars_palm_flower",() -> new MartianFlowerItem(NorthstarBlocks.MARS_PALM.get(), new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
    public static final RegistryObject<MartianFlowerItem> MARS_SPROUT_FLOWER = ITEMS.register("mars_sprout_flower",() -> new MartianFlowerItem(NorthstarBlocks.MARS_SPROUT.get(), new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
    
    //ice cream  :]
	public static final RegistryObject<Item> VANILLA_ICE_CREAM = ITEMS.register("vanilla_ice_cream", () -> new Item(new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB).food(Foods.COOKED_CHICKEN)));
	public static final RegistryObject<Item> CHOCOLATE_ICE_CREAM = ITEMS.register("chocolate_ice_cream", () -> new Item(new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB).food(Foods.COOKED_CHICKEN)));
	public static final RegistryObject<Item> STRAWBERRY_ICE_CREAM = ITEMS.register("strawberry_ice_cream", () -> new Item(new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB).food(Foods.COOKED_CHICKEN)));

	public static final RegistryObject<Item> FLATTENED_DOUGH = ITEMS.register("flattened_dough", () -> new Item(new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
	
	public static final RegistryObject<Item> RAW_ICE_CREAM_CONE = ITEMS.register("raw_ice_cream_cone", () -> new Item(new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
	public static final RegistryObject<Item> ICE_CREAM_CONE = ITEMS.register("ice_cream_cone", () -> new Item(new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB).food(Foods.MELON_SLICE)));

	
	public static final RegistryObject<Item> MARTIAN_STRAWBERRY = ITEMS.register("martian_strawberry", () -> new SeedItem(NorthstarBlocks.MARTIAN_STRAWBERRY_BUSH.get(), new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB).food(Foods.APPLE)));
	
	
	public static final RegistryObject<Item> ASTRONOMICAL_READING = ITEMS.register("astronomical_reading", () -> new Item(new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
	public static final RegistryObject<StarMapItem> STAR_MAP = ITEMS.register("star_map", () -> new StarMapItem(new Item.Properties().stacksTo(1).tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
	
	public static final RegistryObject<Item> LUNAR_SAPPHIRE_SHARD = ITEMS.register("lunar_sapphire_shard", () -> new Item(new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
	
	public static final RegistryObject<Item> POLISHED_LUNAR_SAPPHIRE = ITEMS.register("polished_lunar_sapphire", () -> new Item(new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
	public static final RegistryObject<Item> POLISHED_DIAMOND = ITEMS.register("polished_diamond", () -> new Item(new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
	public static final RegistryObject<Item> POLISHED_AMETHYST = ITEMS.register("polished_amethyst", () -> new Item(new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
	
	public static final RegistryObject<Item> TARGETING_COMPUTER = ITEMS.register("targeting_computer", () -> new Item(new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB).stacksTo(1)));
	public static final ItemEntry<SequencedAssemblyItem> UNFINISHED_TARGETING_COMPUTER = sequencedIngredient("unfinished_targeting_computer");
	
	public static final RegistryObject<Item> CIRCUIT = ITEMS.register("circuit", () -> new Item(new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
	public static final ItemEntry<SequencedAssemblyItem> UNFINISHED_CIRCUIT = sequencedIngredient("unfinished_circuit");
	
	public static final RegistryObject<Item> ADVANCED_CIRCUIT = ITEMS.register("advanced_circuit", () -> new Item(new Item.Properties().tab(NorthstarCreativeModeTab.NORTHSTAR_TAB)));
	public static final ItemEntry<SequencedAssemblyItem> UNFINISHED_ADVANCED_CIRCUIT = sequencedIngredient("unfinished_advanced_circuit");
	
	public static final ItemEntry<SandPaperItem> MOON_SAND_PAPER = REGISTRATE.item("moon_sand_paper", SandPaperItem::new)
			.tag(AllTags.AllItemTags.SANDPAPER.tag)
			.properties(p -> p.defaultDurability(512))
			.register();
    
	public static void register(IEventBus eventBus)
	{ITEMS.register(eventBus);}
    public ItemStack asStack(int count) {
        return new ItemStack(get(), count);
    }
    
	private static ItemEntry<SequencedAssemblyItem> sequencedIngredient(String name) {
		return REGISTRATE.item(name, SequencedAssemblyItem::new)
			.register();
	}
	public Holder<Item> get() {
		// TODO Auto-generated method stub
		return null;
	}
	public static void register() {}
}