package com.lightning.northstar.block;

import static net.minecraft.world.level.block.Blocks.OAK_PLANKS;
import static net.minecraft.world.level.block.Blocks.STONE;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import com.lightning.northstar.Northstar;
import com.lightning.northstar.block.crops.MarsPalmBlock;
import com.lightning.northstar.block.crops.MarsSproutBlock;
import com.lightning.northstar.block.crops.MarsTulipBlock;
import com.lightning.northstar.block.crops.MartianStrawberryBushBlock;
import com.lightning.northstar.block.crops.MartianTallFlowerBlock;
import com.lightning.northstar.block.crops.TallFungusBlock;
import com.lightning.northstar.block.crops.VenusVinesBlock;
import com.lightning.northstar.block.tech.telescope.TelescopeBlock;
import com.lightning.northstar.item.NorthstarCreativeModeTab;
import com.lightning.northstar.item.NorthstarItems;
import com.lightning.northstar.world.features.NorthstarConfiguredFeatures;
import com.lightning.northstar.world.features.grower.ArgyreSaplingTreeGrower;
import com.lightning.northstar.world.features.grower.CoilerTreeGrower;
import com.lightning.northstar.world.features.grower.MercuryCactusGrower;
import com.lightning.northstar.world.features.grower.TestTreeGrower;
import com.lightning.northstar.world.features.grower.WilterTreeGrower;

import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.GravelBlock;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RedStoneOreBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class NorthstarBlocks {

	
	public static final DeferredRegister<Block> BLOCKS =
			DeferredRegister.create(ForgeRegistries.BLOCKS, Northstar.MOD_ID);
	
	private static boolean never(BlockState blockstate, BlockGetter blockgetter, BlockPos blockpos) {
	 	return false;
 	}
	
	// martian steel stuff yay :]
    public static final RegistryObject<Block> MARTIAN_STEEL_BLOCK = registerBlock("martian_steel_block",
    		() -> new Block(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_BLACK) .sound(SoundType.NETHERITE_BLOCK) .strength(30f,15f) .requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MARTIAN_STEEL_SHEETMETAL = registerBlock("martian_steel_sheetmetal",
    		() -> new Block(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_BLACK) .sound(SoundType.NETHERITE_BLOCK) .strength(5f,15f) .requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MARTIAN_STEEL_SHEETMETAL_SLAB = registerBlock("martian_steel_sheetmetal_slab",
    		() -> new SlabBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_BLACK) .sound(SoundType.NETHERITE_BLOCK) .strength(5f,15f) .requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MARTIAN_STEEL_SHEETMETAL_VERTICAL_SLAB = registerBlock("martian_steel_sheetmetal_vertical_slab",
    		() -> new VerticalSlabBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_BLACK) .sound(SoundType.NETHERITE_BLOCK) .strength(5f,15f) .requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MARTIAN_STEEL_PLATING = registerBlock("martian_steel_plating",
    		() -> new Block(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_BLACK) .sound(SoundType.NETHERITE_BLOCK) .strength(5f,15f) .requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MARTIAN_STEEL_LARGE_PLATING = registerBlock("martian_steel_large_plating",
    		() -> new Block(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_BLACK) .sound(SoundType.NETHERITE_BLOCK) .strength(5f,15f) .requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MARTIAN_STEEL_PLATING_SLAB = registerBlock("martian_steel_plating_slab",
    		() -> new SlabBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_BLACK) .sound(SoundType.NETHERITE_BLOCK) .strength(5f,15f) .requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MARTIAN_STEEL_PLATING_VERTICAL_SLAB = registerBlock("martian_steel_plating_vertical_slab",
    		() -> new VerticalSlabBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_BLACK) .sound(SoundType.NETHERITE_BLOCK) .strength(5f,15f) .requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
	@SuppressWarnings("deprecation")
	public static final RegistryObject<Block> MARTIAN_STEEL_PLATING_STAIRS = registerBlock("martian_steel_plating_stairs",
    		() -> new StairBlock(MARTIAN_STEEL_PLATING.get().defaultBlockState(), BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_BLACK) .sound(SoundType.NETHERITE_BLOCK) .strength(5f,15f) .requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MARTIAN_STEEL_PILLAR = registerBlock("martian_steel_pillar",
    		() -> new RotatedPillarBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_BLACK) .sound(SoundType.NETHERITE_BLOCK) .strength(5f,15f) .requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MARTIAN_STEEL_GRATE = registerBlock("martian_steel_grate",
    		() -> new GrateBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK) 
    		.strength(4f,12f).requiresCorrectToolForDrops().noOcclusion().isSuffocating(NorthstarBlocks::never).isViewBlocking(NorthstarBlocks::never)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MARTIAN_STEEL_LAMP = registerBlock("martian_steel_lamp",
    		() -> new Block(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_BLACK).lightLevel((p_50872_) -> {return 15;}) .sound(SoundType.NETHERITE_BLOCK) .strength(4f,12f) .requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MARTIAN_STEEL_BLUE_LAMP = registerBlock("martian_steel_blue_lamp",
    		() -> new Block(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_BLACK).lightLevel((p_50872_) -> {return 15;}) .sound(SoundType.NETHERITE_BLOCK) .strength(4f,12f) .requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    
    //iron stuff
    public static final RegistryObject<Block> IRON_SHEETMETAL = registerBlock("iron_sheetmetal",
    		() -> new Block(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_GRAY) .sound(SoundType.NETHERITE_BLOCK) .strength(4f,12f) .requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> IRON_SHEETMETAL_SLAB = registerBlock("iron_sheetmetal_slab",
    		() -> new SlabBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_GRAY) .sound(SoundType.NETHERITE_BLOCK) .strength(4f,12f) .requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> IRON_SHEETMETAL_VERTICAL_SLAB = registerBlock("iron_sheetmetal_vertical_slab",
    		() -> new VerticalSlabBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_GRAY) .sound(SoundType.NETHERITE_BLOCK) .strength(4f,12f) .requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> IRON_PLATING = registerBlock("iron_plating",
    		() -> new Block(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_GRAY) .sound(SoundType.NETHERITE_BLOCK) .strength(4f,12f) .requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> IRON_PLATING_SLAB = registerBlock("iron_plating_slab",
    		() -> new SlabBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_GRAY) .sound(SoundType.NETHERITE_BLOCK) .strength(4f,12f) .requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> IRON_PLATING_VERTICAL_SLAB = registerBlock("iron_plating_vertical_slab",
    		() -> new VerticalSlabBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_GRAY) .sound(SoundType.NETHERITE_BLOCK) .strength(4f,12f) .requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    @SuppressWarnings("deprecation")
	public static final RegistryObject<Block> IRON_PLATING_STAIRS = registerBlock("iron_plating_stairs",
    		() -> new StairBlock(IRON_PLATING.get().defaultBlockState(), BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_GRAY) .sound(SoundType.NETHERITE_BLOCK) .strength(4f,12f) .requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> IRON_PILLAR = registerBlock("iron_pillar",
    		() -> new RotatedPillarBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_GRAY) .sound(SoundType.NETHERITE_BLOCK) .strength(4f,12f) .requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> IRON_GRATE = registerBlock("iron_grate",
    		() -> new GrateBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK) 
    		.strength(4f,8f).requiresCorrectToolForDrops().noOcclusion().isSuffocating(NorthstarBlocks::never).isViewBlocking(NorthstarBlocks::never)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    
    
    public static final RegistryObject<Block> VENT_BLOCK = registerBlock("vent_block",
    		() -> new GrateBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK) 
    		.strength(4f,8f).requiresCorrectToolForDrops().noOcclusion().isSuffocating(NorthstarBlocks::never).isViewBlocking(NorthstarBlocks::never)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    
    
    //tungsten stuff
    public static final RegistryObject<Block> TUNGSTEN_BLOCK = registerBlock("tungsten_block",
    		() -> new Block(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_BLACK) .sound(SoundType.NETHERITE_BLOCK) .strength(30f,16f) .requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> TUNGSTEN_SHEETMETAL = registerBlock("tungsten_sheetmetal",
    		() -> new Block(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_GRAY) .sound(SoundType.NETHERITE_BLOCK) .strength(6f,16f) .requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> TUNGSTEN_SHEETMETAL_SLAB = registerBlock("tungsten_sheetmetal_slab",
    		() -> new SlabBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_GRAY) .sound(SoundType.NETHERITE_BLOCK) .strength(6f,16f) .requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> TUNGSTEN_SHEETMETAL_VERTICAL_SLAB = registerBlock("tungsten_sheetmetal_vertical_slab",
    		() -> new VerticalSlabBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_GRAY) .sound(SoundType.NETHERITE_BLOCK) .strength(6f,16f) .requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
   public static final RegistryObject<Block> TUNGSTEN_PLATING = registerBlock("tungsten_plating",
    		() -> new Block(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_GRAY) .sound(SoundType.NETHERITE_BLOCK) .strength(6f,16f) .requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> TUNGSTEN_PLATING_SLAB = registerBlock("tungsten_plating_slab",
    		() -> new SlabBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_GRAY) .sound(SoundType.NETHERITE_BLOCK) .strength(6f,16f) .requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> TUNGSTEN_PLATING_VERTICAL_SLAB = registerBlock("tungsten_plating_vertical_slab",
    		() -> new VerticalSlabBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_GRAY) .sound(SoundType.NETHERITE_BLOCK) .strength(6f,16f) .requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    @SuppressWarnings("deprecation")
	public static final RegistryObject<Block> TUNGSTEN_PLATING_STAIRS = registerBlock("tungsten_plating_stairs",
    		() -> new StairBlock(TUNGSTEN_PLATING.get().defaultBlockState(), BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_GRAY) .sound(SoundType.NETHERITE_BLOCK) .strength(6f,16f) .requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> TUNGSTEN_PILLAR = registerBlock("tungsten_pillar",
    		() -> new RotatedPillarBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_GRAY) .sound(SoundType.NETHERITE_BLOCK) .strength(6f,16f) .requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> TUNGSTEN_GRATE = registerBlock("tungsten_grate",
    		() -> new GrateBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK) 
    		.strength(5f,16f).requiresCorrectToolForDrops().noOcclusion().isSuffocating(NorthstarBlocks::never).isViewBlocking(NorthstarBlocks::never)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);    
    
    public static final RegistryObject<Block> GLOWSTONE_LAMP = registerBlock("glowstone_lamp",
    		() -> new Block(BlockBehaviour.Properties.of(Material.DECORATION, MaterialColor.COLOR_YELLOW).lightLevel((p_50872_) -> {return 15;}) .sound(SoundType.GLASS) .strength(2f,5f)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);    
    
    public static final RegistryObject<Block> MARS_SAND = registerBlock("mars_sand",
            () -> new GravelBlock(BlockBehaviour.Properties.of(Material.SAND, MaterialColor.COLOR_ORANGE).sound(SoundType.GRAVEL)
                    .strength(0.5f, 1.6f)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MARS_GRAVEL = registerBlock("mars_gravel",
    		() -> new GravelBlock(BlockBehaviour.Properties.of(Material.SAND, MaterialColor.COLOR_ORANGE).sound(SoundType.GRAVEL)
                    .strength(0.65f, 2.0f)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MARS_SOIL = registerBlock("mars_soil",
            () -> new MarsSoilBlock(BlockBehaviour.Properties.of(Material.DIRT, MaterialColor.COLOR_ORANGE).sound(SoundType.GRAVEL)
                    .strength(0.5f, 8f)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MARTIAN_GRASS = registerBlock("martian_grass",
            () -> new MartianGrassBlock(BlockBehaviour.Properties.of(Material.DIRT, MaterialColor.COLOR_PURPLE).sound(SoundType.GRASS)
                    .strength(0.65f, 8f).randomTicks()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MARTIAN_TALL_GRASS = registerBlock("martian_tall_grass",
            () -> new MartianTallGrassBlock(BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT, MaterialColor.COLOR_PURPLE).sound(SoundType.GRASS)
            		.noCollission().instabreak().randomTicks().offsetType(BlockBehaviour.OffsetType.XYZ)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MARS_FARMLAND = registerBlock("mars_farmland",
            () -> new MarsFarmlandBlock(BlockBehaviour.Properties.of(Material.DIRT, MaterialColor.COLOR_ORANGE).randomTicks().sound(SoundType.GRAVEL)
                    .strength(0.5f, 8f)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MARS_WORM_NEST = registerBlock("mars_worm_nest",
            () -> new MarsWormNestBlock(BlockBehaviour.Properties.of(Material.LEAVES, MaterialColor.COLOR_ORANGE).randomTicks().sound(SoundType.GRASS)
                    .strength(0.2f, 0.2f)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MARS_ROOTS = registerBlock("mars_roots",
            () -> new MarsRootBlock(BlockBehaviour.Properties.of(Material.LEAVES, MaterialColor.TERRACOTTA_GRAY).sound(SoundType.VINE)
                    .noOcclusion() .noCollission() .strength(0.2f).randomTicks().isSuffocating(NorthstarBlocks::never).isViewBlocking(NorthstarBlocks::never)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> GLOWING_MARS_ROOTS = registerBlock("glowing_mars_roots",
            () -> new MarsRootBlock(BlockBehaviour.Properties.of(Material.LEAVES, MaterialColor.TERRACOTTA_GRAY).lightLevel((p_50872_) -> {return 10;}).sound(SoundType.VINE)
                    .noOcclusion() .noCollission() .strength(0.2f).randomTicks().isSuffocating(NorthstarBlocks::never).isViewBlocking(NorthstarBlocks::never)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    

    public static final RegistryObject<Block> STRIPPED_COILER_LOG = registerBlock("stripped_coiler_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_LIGHT_GREEN).sound(SoundType.WOOD)
                    .strength(2f)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> COILER_LOG = registerBlock("coiler_log",
            () -> new LogBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_LIGHT_GREEN).sound(SoundType.WOOD)
                    .strength(2f), STRIPPED_COILER_LOG.get()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> COILER_PLANKS = registerBlock("coiler_planks",
            () -> new Block(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_LIGHT_GREEN).sound(SoundType.WOOD)
                    .strength(2f, 3f)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> COILER_SLAB = registerBlock("coiler_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_LIGHT_GREEN).sound(SoundType.WOOD)
                    .strength(2f, 3f)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    @SuppressWarnings("deprecation")
	public static final RegistryObject<Block> COILER_STAIRS = registerBlock("coiler_stairs",
            () -> new StairBlock(OAK_PLANKS.defaultBlockState(), BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_LIGHT_GREEN).sound(SoundType.WOOD)
                    .strength(2f, 3f)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    
    
    public static final RegistryObject<Block> COILER_LEAVES = registerBlock("coiler_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.of(Material.LEAVES, MaterialColor.COLOR_MAGENTA).sound(SoundType.GRASS)
                    .noOcclusion() .strength(0.5f).isSuffocating(NorthstarBlocks::never).isViewBlocking(NorthstarBlocks::never)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> COILER_VINES = registerBlock("coiler_vines",
            () -> new VineBlock(BlockBehaviour.Properties.of(Material.LEAVES, MaterialColor.COLOR_MAGENTA).sound(SoundType.VINE)
                    .noOcclusion().noCollission().randomTicks().strength(0.2f).isSuffocating(NorthstarBlocks::never).isViewBlocking(NorthstarBlocks::never)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> COILER_SAPLING = registerBlock("coiler_sapling",
            () -> new SaplingBlock(new CoilerTreeGrower(), BlockBehaviour.Properties.of(Material.PLANT).sound(SoundType.GRASS)
                    .randomTicks() .instabreak() .noCollission()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MARTIAN_STRAWBERRY_BUSH = registerBlock("martian_strawberry_bush",
            () -> new MartianStrawberryBushBlock(BlockBehaviour.Properties.of(Material.PLANT).sound(SoundType.GRASS)
                    .randomTicks() .instabreak() .noCollission()), null);
    
    

    public static final RegistryObject<Block> STRIPPED_WILTER_LOG = registerBlock("stripped_wilter_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_BLUE).sound(SoundType.WOOD)
                    .strength(2f)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> WILTER_LOG = registerBlock("wilter_log",
            () -> new LogBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_BLUE).sound(SoundType.WOOD)
                    .strength(2f), STRIPPED_WILTER_LOG.get()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> WILTER_PLANKS = registerBlock("wilter_planks",
            () -> new Block(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_BLUE).sound(SoundType.WOOD)
                    .strength(2f)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> WILTER_SLAB = registerBlock("wilter_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_BLUE).sound(SoundType.WOOD)
                    .strength(2f, 3f)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    @SuppressWarnings("deprecation")
	public static final RegistryObject<Block> WILTER_STAIRS = registerBlock("wilter_stairs",
            () -> new StairBlock(OAK_PLANKS.defaultBlockState(), BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_BLUE).sound(SoundType.WOOD)
                    .strength(2f, 3f)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    

    public static final RegistryObject<Block> STRIPPED_ARGYRE_LOG = registerBlock("stripped_argyre_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_RED).sound(SoundType.WOOD)
                    .strength(2f)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> ARGYRE_LOG = registerBlock("argyre_log",
            () -> new LogBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_RED).sound(SoundType.WOOD)
                    .strength(2f), STRIPPED_ARGYRE_LOG.get()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> ARGYRE_LEAVES = registerBlock("argyre_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.of(Material.LEAVES, MaterialColor.COLOR_ORANGE).sound(SoundType.GRASS)
                    .noOcclusion() .strength(0.5f).isSuffocating(NorthstarBlocks::never).isViewBlocking(NorthstarBlocks::never)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> ARGYRE_PLANKS = registerBlock("argyre_planks",
            () -> new Block(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_RED).sound(SoundType.WOOD)
                    .strength(2f, 3f)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> ARGYRE_SLAB = registerBlock("argyre_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_RED).sound(SoundType.WOOD)
                    .strength(2f, 3f)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    @SuppressWarnings("deprecation")
	public static final RegistryObject<Block> ARGYRE_STAIRS = registerBlock("argyre_stairs",
            () -> new StairBlock(OAK_PLANKS.defaultBlockState(), BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_RED).sound(SoundType.WOOD)
                    .strength(2f, 3f)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    
    
    
    public static final RegistryObject<Block> ARGYRE_SAPLING = registerBlock("argyre_sapling",
            () -> new SaplingBlock(new ArgyreSaplingTreeGrower(), BlockBehaviour.Properties.of(Material.PLANT).sound(SoundType.GRASS)
                    .randomTicks() .instabreak() .noCollission()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    
    
    
    public static final RegistryObject<Block> WILTER_LEAVES = registerBlock("wilter_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.of(Material.LEAVES, MaterialColor.COLOR_BLUE).sound(SoundType.GRASS)
                    .noOcclusion() .strength(0.5f).isSuffocating(NorthstarBlocks::never).isViewBlocking(NorthstarBlocks::never)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> WILTER_SAPLING = registerBlock("wilter_sapling",
            () -> new SaplingBlock(new WilterTreeGrower(), BlockBehaviour.Properties.of(Material.PLANT).sound(SoundType.GRASS)
                    .randomTicks() .instabreak() .noCollission()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    
    
    public static final RegistryObject<Block> MARS_TULIP = registerBlock("mars_tulip",
            () -> new MarsTulipBlock(BlockBehaviour.Properties.of(Material.PLANT).sound(SoundType.GRASS).color(MaterialColor.COLOR_ORANGE)
                    .randomTicks() .instabreak() .noCollission().offsetType(BlockBehaviour.OffsetType.XZ)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MARS_PALM = registerBlock("mars_palm",
            () -> new MarsPalmBlock(BlockBehaviour.Properties.of(Material.PLANT).sound(SoundType.GRASS).color(MaterialColor.COLOR_BLUE)
                    .randomTicks() .instabreak() .noCollission().offsetType(BlockBehaviour.OffsetType.XZ)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MARS_SPROUT = registerBlock("mars_sprout",
            () -> new MarsSproutBlock(BlockBehaviour.Properties.of(Material.PLANT).sound(SoundType.GRASS).color(MaterialColor.COLOR_PINK)
                    .randomTicks() .instabreak() .noCollission().offsetType(BlockBehaviour.OffsetType.XZ).lightLevel((l) -> {return 7;})), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MARS_SPROUT_BIG = registerBlock("mars_sprout_big",
            () -> new MartianTallFlowerBlock(BlockBehaviour.Properties.of(Material.PLANT).sound(SoundType.GRASS)
                    .randomTicks() .instabreak() .noCollission().lightLevel((l) -> {return 14;})), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    
    public static final RegistryObject<Block> POINTED_CRIMSITE = registerBlock("pointed_crimsite",
            () -> new PointedCrimsiteBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_RED).sound(SoundType.DEEPSLATE)
                    .strength(3.5f, 12f).noOcclusion().requiresCorrectToolForDrops().dynamicShape().offsetType(BlockBehaviour.OffsetType.XZ)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    
    //mars stone stuff
    public static final RegistryObject<Block> MARS_STONE = registerBlock("mars_stone",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).sound(SoundType.DEEPSLATE)
                    .strength(3.5f, 8f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MARS_DEEP_STONE = registerBlock("mars_deep_stone",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).sound(SoundType.DEEPSLATE)
                    .strength(4.5f, 8f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    //mars deco blocks
    public static final RegistryObject<Block> MARS_STONE_BRICKS = registerBlock("mars_stone_bricks",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).sound(SoundType.DEEPSLATE_BRICKS)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MARS_STONE_BRICK_SLAB = registerBlock("mars_stone_brick_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).sound(SoundType.DEEPSLATE_BRICKS)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MARS_STONE_BRICK_SLAB_VERTICAL = registerBlock("mars_stone_brick_slab_vertical",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).sound(SoundType.DEEPSLATE_BRICKS)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    @SuppressWarnings("deprecation")
	public static final RegistryObject<Block> MARS_STONE_BRICK_STAIRS = registerBlock("mars_stone_brick_stairs",
            () -> new StairBlock(STONE.defaultBlockState() ,BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).sound(SoundType.DEEPSLATE_BRICKS)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MARS_STONE_BRICK_WALL = registerBlock("mars_stone_brick_wall",
            () -> new WallBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).sound(SoundType.DEEPSLATE_BRICKS)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MARS_STONE_PILLAR = registerBlock("mars_stone_pillar",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).sound(SoundType.DEEPSLATE_BRICKS)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> CHISELED_MARS_STONE = registerBlock("chiseled_mars_stone",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).sound(SoundType.DEEPSLATE_BRICKS)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> POLISHED_MARS_STONE = registerBlock("polished_mars_stone",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_LIGHT_GRAY).sound(SoundType.DEEPSLATE_BRICKS)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MARS_STONE_LAMP = registerBlock("mars_stone_lamp",
    		() -> new Block(BlockBehaviour.Properties.of(Material.DECORATION, MaterialColor.COLOR_YELLOW).lightLevel((p_50872_) -> {return 15;}) .sound(SoundType.GLASS) .strength(3f,6.5f) .requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS); 

    //mars ores
    public static final RegistryObject<Block> MARS_IRON_ORE = registerBlock("mars_iron_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).sound(SoundType.DEEPSLATE)
                    .strength(5f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MARS_COPPER_ORE = registerBlock("mars_copper_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).sound(SoundType.DEEPSLATE)
                    .strength(5f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MARS_GOLD_ORE = registerBlock("mars_gold_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).sound(SoundType.DEEPSLATE)
                    .strength(5f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MARS_DIAMOND_ORE = registerBlock("mars_diamond_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).sound(SoundType.DEEPSLATE)
                    .strength(6f, 12f).requiresCorrectToolForDrops(),UniformInt.of(3, 7)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MARS_REDSTONE_ORE = registerBlock("mars_redstone_ore",
            () -> new RedStoneOreBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).sound(SoundType.DEEPSLATE)
                    .strength(5f, 12f).requiresCorrectToolForDrops().randomTicks().lightLevel(litBlockEmission(9))), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MARS_QUARTZ_ORE = registerBlock("mars_quartz_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).sound(SoundType.DEEPSLATE)
                    .strength(5f, 12f).requiresCorrectToolForDrops(),  UniformInt.of(2, 5)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    //mars deep ores
    public static final RegistryObject<Block> MARS_DEEP_IRON_ORE = registerBlock("mars_deep_iron_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).sound(SoundType.DEEPSLATE)
                    .strength(6f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MARS_DEEP_COPPER_ORE = registerBlock("mars_deep_copper_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).sound(SoundType.DEEPSLATE)
                    .strength(6f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MARS_DEEP_GOLD_ORE = registerBlock("mars_deep_gold_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).sound(SoundType.DEEPSLATE)
                    .strength(6f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MARS_DEEP_DIAMOND_ORE = registerBlock("mars_deep_diamond_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).sound(SoundType.DEEPSLATE)
                    .strength(7f, 12f).requiresCorrectToolForDrops(),UniformInt.of(3, 7)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MARS_DEEP_REDSTONE_ORE = registerBlock("mars_deep_redstone_ore",
            () -> new RedStoneOreBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).sound(SoundType.DEEPSLATE)
                    .strength(6f, 12f).requiresCorrectToolForDrops().randomTicks().lightLevel(litBlockEmission(9))), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MARS_DEEP_QUARTZ_ORE = registerBlock("mars_deep_quartz_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).sound(SoundType.DEEPSLATE)
                    .strength(6f, 12f).requiresCorrectToolForDrops(),UniformInt.of(2, 5)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    
    
    public static final RegistryObject<Block> VOLCANIC_ASH = registerBlock("volcanic_ash",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK).sound(SoundType.TUFF)
                    .strength(0.4f, 2f)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> VOLCANIC_ROCK = registerBlock("volcanic_rock",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_BROWN).sound(SoundType.TUFF)
                    .strength(2f, 4f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    
    
    //moon stone stuff
    public static final RegistryObject<Block> MOON_SAND = registerBlock("moon_sand",
            () -> new GravelBlock(BlockBehaviour.Properties.of(Material.SAND, MaterialColor.COLOR_LIGHT_GRAY).sound(SoundType.SAND)
                    .strength(0.5f, 8.0f)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MOON_STONE = registerBlock("moon_stone",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_LIGHT_GRAY).sound(SoundType.DEEPSLATE)
                    .strength(3.5f, 8f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MOON_DEEP_STONE = registerBlock("moon_deep_stone",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_LIGHT_GRAY).sound(SoundType.DEEPSLATE)
                    .strength(4.5f, 8f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    
    public static final RegistryObject<Block> FROST = registerBlock("frost",
            () -> new FrostBlock(BlockBehaviour.Properties.of(Material.ICE, MaterialColor.COLOR_LIGHT_BLUE).sound(SoundType.CALCITE).friction(0.989F)
                    .noOcclusion() .noCollission() .strength(0.2f).isSuffocating(NorthstarBlocks::never).isViewBlocking(NorthstarBlocks::never)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
   
    
    //moon deco stuff ayy
    public static final RegistryObject<Block> MOON_STONE_BRICKS = registerBlock("moon_stone_bricks",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_LIGHT_GRAY).sound(SoundType.DEEPSLATE_BRICKS)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MOON_STONE_BRICK_SLAB = registerBlock("moon_stone_brick_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_LIGHT_GRAY).sound(SoundType.DEEPSLATE_BRICKS)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MOON_STONE_BRICK_SLAB_VERTICAL = registerBlock("moon_stone_brick_slab_vertical",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_LIGHT_GRAY).sound(SoundType.DEEPSLATE_BRICKS)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    @SuppressWarnings("deprecation")
	public static final RegistryObject<Block> MOON_STONE_BRICK_STAIRS = registerBlock("moon_stone_brick_stairs",
            () -> new StairBlock(STONE.defaultBlockState() ,BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_LIGHT_GRAY).sound(SoundType.DEEPSLATE_BRICKS)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MOON_STONE_BRICK_WALL = registerBlock("moon_stone_brick_wall",
            () -> new WallBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_LIGHT_GRAY).sound(SoundType.DEEPSLATE_BRICKS)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MOON_STONE_PILLAR = registerBlock("moon_stone_pillar",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_LIGHT_GRAY).sound(SoundType.DEEPSLATE_BRICKS)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MOON_STONE_LAMP = registerBlock("moon_stone_lamp",
    		() -> new Block(BlockBehaviour.Properties.of(Material.DECORATION, MaterialColor.COLOR_YELLOW).lightLevel((p_50872_) -> {return 15;}) .sound(SoundType.GLASS) .strength(3f,6.5f) .requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS); 
    public static final RegistryObject<Block> CHISELED_MOON_STONE = registerBlock("chiseled_moon_stone",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_LIGHT_GRAY).sound(SoundType.DEEPSLATE_BRICKS)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> POLISHED_MOON_STONE = registerBlock("polished_moon_stone",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_LIGHT_GRAY).sound(SoundType.DEEPSLATE_BRICKS)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    
    //moon ores
    public static final RegistryObject<Block> MOON_IRON_ORE = registerBlock("moon_iron_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_LIGHT_GRAY).sound(SoundType.DEEPSLATE)
                    .strength(5f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MOON_COPPER_ORE = registerBlock("moon_copper_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_LIGHT_GRAY).sound(SoundType.DEEPSLATE)
                    .strength(5f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MOON_GOLD_ORE = registerBlock("moon_gold_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_LIGHT_GRAY).sound(SoundType.DEEPSLATE)
                    .strength(5f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MOON_DIAMOND_ORE = registerBlock("moon_diamond_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_LIGHT_GRAY).sound(SoundType.DEEPSLATE)
                    .strength(6f, 12f).requiresCorrectToolForDrops(),UniformInt.of(3, 7)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MOON_REDSTONE_ORE = registerBlock("moon_redstone_ore",
            () -> new RedStoneOreBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_LIGHT_GRAY).sound(SoundType.DEEPSLATE)
                    .strength(5f, 12f).requiresCorrectToolForDrops().randomTicks().lightLevel(litBlockEmission(9))), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MOON_LAPIS_ORE = registerBlock("moon_lapis_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_LIGHT_GRAY).sound(SoundType.DEEPSLATE)
                    .strength(5f, 12f).requiresCorrectToolForDrops(),  UniformInt.of(2, 5)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MOON_ZINC_ORE = registerBlock("moon_zinc_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_LIGHT_GRAY).sound(SoundType.DEEPSLATE)
                    .strength(5f, 12f).requiresCorrectToolForDrops(),  UniformInt.of(2, 5)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MOON_GLOWSTONE_ORE = registerBlock("moon_glowstone_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).sound(SoundType.DEEPSLATE)
                    .strength(5f, 12f).requiresCorrectToolForDrops().lightLevel((p_50872_) -> {return 15;})), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    //moon deep ores
    public static final RegistryObject<Block> MOON_DEEP_IRON_ORE = registerBlock("moon_deep_iron_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).sound(SoundType.DEEPSLATE)
                    .strength(6f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MOON_DEEP_COPPER_ORE = registerBlock("moon_deep_copper_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).sound(SoundType.DEEPSLATE)
                    .strength(6f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MOON_DEEP_GOLD_ORE = registerBlock("moon_deep_gold_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).sound(SoundType.DEEPSLATE)
                    .strength(6f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MOON_DEEP_DIAMOND_ORE = registerBlock("moon_deep_diamond_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).sound(SoundType.DEEPSLATE)
                    .strength(7f, 12f).requiresCorrectToolForDrops(),UniformInt.of(3, 7)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MOON_DEEP_REDSTONE_ORE = registerBlock("moon_deep_redstone_ore",
            () -> new RedStoneOreBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).sound(SoundType.DEEPSLATE)
                    .strength(6f, 12f).requiresCorrectToolForDrops().randomTicks().lightLevel(litBlockEmission(9))), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MOON_DEEP_LAPIS_ORE = registerBlock("moon_deep_lapis_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).sound(SoundType.DEEPSLATE)
                    .strength(6f, 12f).requiresCorrectToolForDrops(),UniformInt.of(2, 5)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MOON_DEEP_ZINC_ORE = registerBlock("moon_deep_zinc_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).sound(SoundType.DEEPSLATE)
                    .strength(6f, 12f).requiresCorrectToolForDrops(),UniformInt.of(2, 5)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MOON_DEEP_GLOWSTONE_ORE = registerBlock("moon_deep_glowstone_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).sound(SoundType.DEEPSLATE)
                    .strength(6f, 12f).requiresCorrectToolForDrops().lightLevel((p_50872_) -> {return 15;})), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    
    public static final RegistryObject<Block> LUNAR_SAPPHIRE_BLOCK = registerBlock("lunar_sapphire_block",
            () -> new Block(BlockBehaviour.Properties.of(Material.AMETHYST, MaterialColor.COLOR_BLUE).sound(SoundType.AMETHYST)
                    .strength(2f, 5f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> LUNAR_SAPPHIRE_CLUSTER = registerBlock("lunar_sapphire_cluster",
            () -> new ClusterBlock(7, 3, BlockBehaviour.Properties.of(Material.AMETHYST, MaterialColor.COLOR_BLUE).sound(SoundType.AMETHYST)
                    .strength(2f, 5f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> SMALL_LUNAR_SAPPHIRE_BUD = registerBlock("small_lunar_sapphire_bud",
            () -> new ClusterBlock(3, 4, BlockBehaviour.Properties.of(Material.AMETHYST, MaterialColor.COLOR_BLUE).sound(SoundType.AMETHYST)
                    .strength(1.5f, 5f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MEDIUM_LUNAR_SAPPHIRE_BUD = registerBlock("medium_lunar_sapphire_bud",
            () -> new ClusterBlock(4, 3, BlockBehaviour.Properties.of(Material.AMETHYST, MaterialColor.COLOR_BLUE).sound(SoundType.AMETHYST)
                    .strength(1.5f, 5f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> LARGE_LUNAR_SAPPHIRE_BUD = registerBlock("large_lunar_sapphire_bud",
            () -> new ClusterBlock(5, 3, BlockBehaviour.Properties.of(Material.AMETHYST, MaterialColor.COLOR_BLUE).sound(SoundType.AMETHYST)
                    .strength(1.5f, 5f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> BUDDING_LUNAR_SAPPHIRE_BLOCK = registerBlock("budding_lunar_sapphire_block",
            () -> new BuddingCrystalBlock(BlockBehaviour.Properties.of(Material.AMETHYST, MaterialColor.COLOR_BLUE).sound(SoundType.AMETHYST)
                    .strength(2f, 5f).randomTicks().requiresCorrectToolForDrops(), SMALL_LUNAR_SAPPHIRE_BUD.get(), MEDIUM_LUNAR_SAPPHIRE_BUD.get(),
                    LARGE_LUNAR_SAPPHIRE_BUD.get(), LUNAR_SAPPHIRE_CLUSTER.get()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    
    
    
    
  //venus stone stuff
    public static final RegistryObject<Block> VENUS_STONE = registerBlock("venus_stone",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_YELLOW).sound(SoundType.DEEPSLATE)
                    .strength(3.5f, 8f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> VENUS_DEEP_STONE = registerBlock("venus_deep_stone",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_YELLOW).sound(SoundType.DEEPSLATE)
                    .strength(4.5f, 8f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> VENUS_GRAVEL = registerBlock("venus_gravel",
            () -> new GravelBlock(BlockBehaviour.Properties.of(Material.SAND, MaterialColor.COLOR_YELLOW).sound(SoundType.GRAVEL)
                    .strength(1.1f, 2f)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> VENUS_PLUME = registerBlock("venus_plume",
            () -> new VenusExhaustBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_YELLOW).sound(SoundType.DEEPSLATE)
                    .strength(3.5f, 8f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    
    public static final RegistryObject<Block> SPIKE_FUNGUS = registerBlock("spike_fungus",
            () -> new VenusMushroomBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_LIGHT_GRAY).sound(SoundType.FUNGUS)
            		.strength(0f, 0.5f).noCollission().instabreak(), () -> {return NorthstarConfiguredFeatures.SPIKE_FUNGUS.getHolder().get();}, () -> {return null;}),   NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> BLOOM_FUNGUS = registerBlock("bloom_fungus",
            () -> new VenusMushroomBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).sound(SoundType.FUNGUS)
            		.strength(0f, 0.5f).noCollission().instabreak().lightLevel((p_50872_) -> {return 7;}), () -> {return NorthstarConfiguredFeatures.BLOOM_FUNGUS.getHolder().get();}, () -> {return NorthstarConfiguredFeatures.ROOF_BLOOM_FUNGUS.getHolder().get();}), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> PLATE_FUNGUS = registerBlock("plate_fungus",
            () -> new VenusMushroomBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_GREEN).sound(SoundType.FUNGUS)
            		.strength(0f, 0.5f).noCollission().instabreak(), () -> {return NorthstarConfiguredFeatures.PLATE_FUNGUS.getHolder().get();}, () -> {return NorthstarConfiguredFeatures.ROOF_PLATE_FUNGUS.getHolder().get();}),   NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> SPIKE_FUNGUS_BLOCK = registerBlock("spike_fungus_block",
            () -> new HugeMushroomBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_LIGHT_GRAY).sound(SoundType.FUNGUS)
            		.strength(3f, 6f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> BLOOM_FUNGUS_BLOCK = registerBlock("bloom_fungus_block",
            () -> new HugeMushroomBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).sound(SoundType.FUNGUS)
            		.strength(3f, 6f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> BLOOM_FUNGUS_STEM_BLOCK = registerBlock("bloom_fungus_stem_block",
            () -> new HugeMushroomBlock(BlockBehaviour.Properties   .of(Material.STONE, MaterialColor.TERRACOTTA_ORANGE).sound(SoundType.FUNGUS)
            		.strength(3f, 6f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    
    public static final RegistryObject<Block> PLATE_FUNGUS_STEM_BLOCK = registerBlock("plate_fungus_stem_block",
            () -> new HugeMushroomBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_LIGHT_GREEN).sound(SoundType.FUNGUS)
            		.strength(3f, 6f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> PLATE_FUNGUS_CAP_BLOCK = registerBlock("plate_fungus_cap_block",
            () -> new HugeMushroomBlock(BlockBehaviour.Properties   .of(Material.STONE, MaterialColor.COLOR_GREEN).sound(SoundType.FUNGUS)
            		.strength(4f, 6f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    
    public static final RegistryObject<Block> TOWER_FUNGUS = registerBlock("tower_fungus",
            () -> new TallFungusBlock(BlockBehaviour.Properties.of(Material.PLANT, MaterialColor.COLOR_BLUE).sound(SoundType.FUNGUS)
                    .randomTicks() .instabreak() .noCollission(), () -> {return NorthstarConfiguredFeatures.TOWER_FUNGUS.getHolder().get();}, () -> {return NorthstarConfiguredFeatures.ROOF_TOWER_FUNGUS.getHolder().get();}),  NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> TOWER_FUNGUS_STEM_BLOCK = registerBlock("tower_fungus_stem_block",
            () -> new HugeMushroomBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_BLUE).sound(SoundType.FUNGUS)
            		.strength(3f, 6f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> TOWER_FUNGUS_CAP_BLOCK = registerBlock("tower_fungus_cap_block",
            () -> new HugeMushroomBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_BLUE).sound(SoundType.FUNGUS)
            		.strength(4f, 6f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    
    public static final RegistryObject<Block> VENUS_VINES = registerBlock("venus_vines",
            () -> new VenusVinesBlock(BlockBehaviour.Properties.of(Material.LEAVES, MaterialColor.COLOR_GREEN).sound(SoundType.GRASS)
            		.strength(0.5f, 0.5f).randomTicks().noCollission().noOcclusion()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> GLOWING_VENUS_VINES = registerBlock("glowing_venus_vines",
            () -> new VenusVinesBlock(BlockBehaviour.Properties.of(Material.LEAVES, MaterialColor.COLOR_GREEN).sound(SoundType.GRASS)
            		.strength(0.5f, 0.5f).randomTicks().noCollission().noOcclusion().lightLevel((light) -> {return 11;})), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> TALL_VENUS_MYCELIUM = registerBlock("tall_venus_mycelium",
            () -> new VenusTallMyceliumBlock(BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT, MaterialColor.COLOR_YELLOW).sound(SoundType.GRASS)
            		.noCollission().instabreak().randomTicks().offsetType(BlockBehaviour.OffsetType.XZ)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    
  //venus deco blocks
    public static final RegistryObject<Block> VENUS_STONE_BRICKS = registerBlock("venus_stone_bricks",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_YELLOW).sound(SoundType.DEEPSLATE_BRICKS)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> VENUS_STONE_BRICK_SLAB = registerBlock("venus_stone_brick_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).sound(SoundType.DEEPSLATE_BRICKS)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> VENUS_STONE_BRICK_SLAB_VERTICAL = registerBlock("venus_stone_brick_slab_vertical",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).sound(SoundType.DEEPSLATE_BRICKS)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    @SuppressWarnings("deprecation")
	public static final RegistryObject<Block> VENUS_STONE_BRICK_STAIRS = registerBlock("venus_stone_brick_stairs",
            () -> new StairBlock(STONE.defaultBlockState() ,BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).sound(SoundType.DEEPSLATE_BRICKS)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> VENUS_STONE_BRICK_WALL = registerBlock("venus_stone_brick_wall",
            () -> new WallBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).sound(SoundType.DEEPSLATE_BRICKS)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> VENUS_STONE_PILLAR = registerBlock("venus_stone_pillar",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).sound(SoundType.DEEPSLATE_BRICKS)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> CHISELED_VENUS_STONE = registerBlock("chiseled_venus_stone",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).sound(SoundType.DEEPSLATE_BRICKS)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> POLISHED_VENUS_STONE = registerBlock("polished_venus_stone",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_LIGHT_GRAY).sound(SoundType.DEEPSLATE_BRICKS)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> VENUS_STONE_LAMP = registerBlock("venus_stone_lamp",
    		() -> new Block(BlockBehaviour.Properties.of(Material.DECORATION, MaterialColor.COLOR_YELLOW).lightLevel((p_50872_) -> {return 15;}) .sound(SoundType.GLASS) .strength(3f,6.5f) .requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS); 
    //venus ores
    public static final RegistryObject<Block> VENUS_COAL_ORE = registerBlock("venus_coal_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_YELLOW).sound(SoundType.DEEPSLATE)
                    .strength(5f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> VENUS_IRON_ORE = registerBlock("venus_iron_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_YELLOW).sound(SoundType.DEEPSLATE)
                    .strength(5f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> VENUS_COPPER_ORE = registerBlock("venus_copper_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_YELLOW).sound(SoundType.DEEPSLATE)
                    .strength(5f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> VENUS_GOLD_ORE = registerBlock("venus_gold_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_YELLOW).sound(SoundType.DEEPSLATE)
                    .strength(5f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> VENUS_DIAMOND_ORE = registerBlock("venus_diamond_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_YELLOW).sound(SoundType.DEEPSLATE)
                    .strength(6f, 12f).requiresCorrectToolForDrops(),UniformInt.of(3, 7)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> VENUS_REDSTONE_ORE = registerBlock("venus_redstone_ore",
            () -> new RedStoneOreBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_YELLOW).sound(SoundType.DEEPSLATE)
                    .strength(5f, 12f).requiresCorrectToolForDrops().randomTicks().lightLevel(litBlockEmission(9))), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> VENUS_QUARTZ_ORE = registerBlock("venus_quartz_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_YELLOW).sound(SoundType.DEEPSLATE)
                    .strength(5f, 12f).requiresCorrectToolForDrops(),  UniformInt.of(2, 5)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> VENUS_GLOWSTONE_ORE = registerBlock("venus_glowstone_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_YELLOW).sound(SoundType.DEEPSLATE)
                    .strength(5f, 12f).requiresCorrectToolForDrops().lightLevel((p_50872_) -> {return 6;})), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    //venus deep ores
    public static final RegistryObject<Block> VENUS_DEEP_IRON_ORE = registerBlock("venus_deep_iron_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).sound(SoundType.DEEPSLATE)
                    .strength(6f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> VENUS_DEEP_COPPER_ORE = registerBlock("venus_deep_copper_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).sound(SoundType.DEEPSLATE)
                    .strength(6f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> VENUS_DEEP_GOLD_ORE = registerBlock("venus_deep_gold_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).sound(SoundType.DEEPSLATE)
                    .strength(6f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> VENUS_DEEP_DIAMOND_ORE = registerBlock("venus_deep_diamond_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).sound(SoundType.DEEPSLATE)
                    .strength(7f, 12f).requiresCorrectToolForDrops(),UniformInt.of(3, 7)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> VENUS_DEEP_REDSTONE_ORE = registerBlock("venus_deep_redstone_ore",
            () -> new RedStoneOreBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).sound(SoundType.DEEPSLATE)
                    .strength(6f, 12f).requiresCorrectToolForDrops().randomTicks().lightLevel(litBlockEmission(9))), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> VENUS_DEEP_QUARTZ_ORE = registerBlock("venus_deep_quartz_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).sound(SoundType.DEEPSLATE)
                    .strength(6f, 12f).requiresCorrectToolForDrops(),UniformInt.of(2, 5)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> VENUS_DEEP_GLOWSTONE_ORE = registerBlock("venus_deep_glowstone_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_YELLOW).sound(SoundType.DEEPSLATE)
                    .strength(6f, 12f).requiresCorrectToolForDrops().lightLevel((p_50872_) -> {return 6;})), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    
    //mercury stone stuff
    public static final RegistryObject<Block> MERCURY_STONE = registerBlock("mercury_stone",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).sound(SoundType.DEEPSLATE)
                    .strength(3.5f, 8f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MERCURY_DEEP_STONE = registerBlock("mercury_deep_stone",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).sound(SoundType.DEEPSLATE)
                    .strength(4.5f, 8f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    //mercury deco blocks
    public static final RegistryObject<Block> MERCURY_STONE_BRICKS = registerBlock("mercury_stone_bricks",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).sound(SoundType.DEEPSLATE_BRICKS)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MERCURY_STONE_BRICK_SLAB = registerBlock("mercury_stone_brick_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).sound(SoundType.DEEPSLATE_BRICKS)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MERCURY_STONE_BRICK_SLAB_VERTICAL = registerBlock("mercury_stone_brick_slab_vertical",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).sound(SoundType.DEEPSLATE_BRICKS)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    @SuppressWarnings("deprecation")
	public static final RegistryObject<Block> MERCURY_STONE_BRICK_STAIRS = registerBlock("mercury_stone_brick_stairs",
            () -> new StairBlock(STONE.defaultBlockState() ,BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).sound(SoundType.DEEPSLATE_BRICKS)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MERCURY_STONE_BRICK_WALL = registerBlock("mercury_stone_brick_wall",
            () -> new WallBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).sound(SoundType.DEEPSLATE_BRICKS)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MERCURY_STONE_PILLAR = registerBlock("mercury_stone_pillar",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).sound(SoundType.DEEPSLATE_BRICKS)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> CHISELED_MERCURY_STONE = registerBlock("chiseled_mercury_stone",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).sound(SoundType.DEEPSLATE_BRICKS)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> POLISHED_MERCURY_STONE = registerBlock("polished_mercury_stone",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_LIGHT_GRAY).sound(SoundType.DEEPSLATE_BRICKS)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MERCURY_STONE_LAMP = registerBlock("mercury_stone_lamp",
    		() -> new Block(BlockBehaviour.Properties.of(Material.DECORATION, MaterialColor.COLOR_YELLOW).lightLevel((p_50872_) -> {return 15;}) .sound(SoundType.GLASS) .strength(3f,6.5f) .requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS); 

    //mercury ores
    public static final RegistryObject<Block> MERCURY_IRON_ORE = registerBlock("mercury_iron_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_LIGHT_GRAY).sound(SoundType.DEEPSLATE)
                    .strength(5f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MERCURY_COPPER_ORE = registerBlock("mercury_copper_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_LIGHT_GRAY).sound(SoundType.DEEPSLATE)
                    .strength(5f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MERCURY_GOLD_ORE = registerBlock("mercury_gold_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_LIGHT_GRAY).sound(SoundType.DEEPSLATE)
                    .strength(5f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MERCURY_DIAMOND_ORE = registerBlock("mercury_diamond_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_LIGHT_GRAY).sound(SoundType.DEEPSLATE)
                    .strength(6f, 12f).requiresCorrectToolForDrops(),UniformInt.of(3, 7)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MERCURY_REDSTONE_ORE = registerBlock("mercury_redstone_ore",
            () -> new RedStoneOreBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_LIGHT_GRAY).sound(SoundType.DEEPSLATE)
                    .strength(5f, 12f).requiresCorrectToolForDrops().randomTicks().lightLevel(litBlockEmission(9))), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MERCURY_LAPIS_ORE = registerBlock("mercury_lapis_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_LIGHT_GRAY).sound(SoundType.DEEPSLATE)
                    .strength(5f, 12f).requiresCorrectToolForDrops(),  UniformInt.of(2, 5)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MERCURY_ZINC_ORE = registerBlock("mercury_zinc_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_LIGHT_GRAY).sound(SoundType.DEEPSLATE)
                    .strength(5f, 12f).requiresCorrectToolForDrops(),  UniformInt.of(2, 5)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MERCURY_GLOWSTONE_ORE = registerBlock("mercury_glowstone_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).sound(SoundType.DEEPSLATE)
                    .strength(5f, 12f).requiresCorrectToolForDrops().lightLevel((p_50872_) -> {return 15;})), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MERCURY_TUNGSTEN_ORE = registerBlock("mercury_tungsten_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_LIGHT_GRAY).sound(SoundType.DEEPSLATE)
                    .strength(6f, 20f).requiresCorrectToolForDrops(),  UniformInt.of(2, 5)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    //mercury deep ores
    public static final RegistryObject<Block> MERCURY_DEEP_IRON_ORE = registerBlock("mercury_deep_iron_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).sound(SoundType.DEEPSLATE)
                    .strength(6f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MERCURY_DEEP_COPPER_ORE = registerBlock("mercury_deep_copper_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).sound(SoundType.DEEPSLATE)
                    .strength(6f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MERCURY_DEEP_GOLD_ORE = registerBlock("mercury_deep_gold_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).sound(SoundType.DEEPSLATE)
                    .strength(6f, 12f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MERCURY_DEEP_DIAMOND_ORE = registerBlock("mercury_deep_diamond_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).sound(SoundType.DEEPSLATE)
                    .strength(7f, 12f).requiresCorrectToolForDrops(),UniformInt.of(3, 7)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MERCURY_DEEP_REDSTONE_ORE = registerBlock("mercury_deep_redstone_ore",
            () -> new RedStoneOreBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).sound(SoundType.DEEPSLATE)
                    .strength(6f, 12f).requiresCorrectToolForDrops().randomTicks().lightLevel(litBlockEmission(9))), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MERCURY_DEEP_LAPIS_ORE = registerBlock("mercury_deep_lapis_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).sound(SoundType.DEEPSLATE)
                    .strength(6f, 12f).requiresCorrectToolForDrops(),UniformInt.of(2, 5)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MERCURY_DEEP_ZINC_ORE = registerBlock("mercury_deep_zinc_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).sound(SoundType.DEEPSLATE)
                    .strength(6f, 12f).requiresCorrectToolForDrops(),UniformInt.of(2, 5)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MERCURY_DEEP_GLOWSTONE_ORE = registerBlock("mercury_deep_glowstone_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).sound(SoundType.DEEPSLATE)
                    .strength(6f, 12f).requiresCorrectToolForDrops().lightLevel((p_50872_) -> {return 15;})), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MERCURY_DEEP_TUNGSTEN_ORE = registerBlock("mercury_deep_tungsten_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).sound(SoundType.DEEPSLATE)
                    .strength(7f, 20f).requiresCorrectToolForDrops(),UniformInt.of(2, 5)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    
    public static final RegistryObject<Block> CALORIAN_LOG = registerBlock("calorian_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).sound(SoundType.BASALT)
                    .strength(4f, 4f)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> CALORIAN_PLANKS = registerBlock("calorian_planks",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).sound(SoundType.BASALT)
                    .strength(4f, 4f)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> CALORIAN_SLAB = registerBlock("calorian_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).sound(SoundType.BASALT)
                    .strength(4f, 4f)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    @SuppressWarnings("deprecation")
	public static final RegistryObject<Block> CALORIAN_STAIRS = registerBlock("calorian_stairs",
            () -> new StairBlock(CALORIAN_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).sound(SoundType.BASALT)
                    .strength(4f, 4f)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    
    public static final RegistryObject<Block> MERCURY_SHELF_FUNGUS = registerBlock("mercury_shelf_fungus",
            () -> new MercuryShelfFungusBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).sound(SoundType.BASALT)
                    .strength(1f, 1f).noCollission().noOcclusion().isSuffocating(NorthstarBlocks::never).isViewBlocking(NorthstarBlocks::never)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MERCURY_SHELF_FUNGUS_BLOCK = registerBlock("mercury_shelf_fungus_block",
            () -> new HugeMushroomBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).sound(SoundType.BASALT)
            		.strength(4f, 6f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    public static final RegistryObject<Block> MERCURY_CACTUS = registerBlock("mercury_cactus",
            () -> new MercuryCactusBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).sound(SoundType.BASALT)
            		.strength(4f, 6f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
 
    
    public static final RegistryObject<Block> TEST = registerBlock("test_sapling",
            () -> new SaplingBlock(new TestTreeGrower(), BlockBehaviour.Properties.of(Material.PLANT).sound(SoundType.GRASS)
                    .randomTicks() .instabreak() .noCollission()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    
    public static final RegistryObject<Block> CACTUS_TEST = registerBlock("cactus_test_sapling",
            () -> new SaplingBlock(new MercuryCactusGrower(), BlockBehaviour.Properties.of(Material.PLANT).sound(SoundType.GRASS)
                    .randomTicks() .instabreak() .noCollission()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    
    
    public static final RegistryObject<Block> MONOLITHITE = registerBlock("monolithite",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK).sound(SoundType.DEEPSLATE_BRICKS)
                    .strength(100f, 100f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    
    public static final RegistryObject<MethaneIceBlock> METHANE_ICE = registerBlock("methane_ice",
            () -> new MethaneIceBlock(BlockBehaviour.Properties.of(Material.ICE, MaterialColor.COLOR_LIGHT_GREEN).sound(SoundType.GLASS).friction(0.989F)
            		.randomTicks().strength(0.5F).noOcclusion().requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);

    public static final RegistryObject<Block> ICICLE = registerBlock("icicle",
            () -> new IcicleBlock(BlockBehaviour.Properties.of(Material.ICE, MaterialColor.COLOR_LIGHT_BLUE).sound(SoundType.GLASS)
                    .strength(3.5f, 12f).noOcclusion().dynamicShape().offsetType(BlockBehaviour.OffsetType.XZ)), NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
    

    

    public static final RegistryObject<Block> TELESCOPE = registerBlock("telescope", 
    		() ->new TelescopeBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_BROWN).sound(SoundType.COPPER)
                    .strength(8f, 8f).noOcclusion().isViewBlocking(NorthstarBlocks::never)), NorthstarCreativeModeTab.NORTHSTAR_TECH);  
    
    public static final RegistryObject<Block> INTERPLANETARY_NAVIGATOR = registerBlock("interplanetary_navigator", 
    		() ->new InterplanetaryNavigatorBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_GRAY).sound(SoundType.METAL)
                    .strength(8f, 8f).noOcclusion().isViewBlocking(NorthstarBlocks::never)), NorthstarCreativeModeTab.NORTHSTAR_TECH);  
    
    public static final RegistryObject<Block> OXYGEN_BUBBLE_GENERATOR = registerBlock("oxygen_bubble_generator", 
    		() ->new OxygenBubbleGeneratorBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_GRAY).sound(SoundType.COPPER)
                    .strength(8f, 8f).requiresCorrectToolForDrops()), NorthstarCreativeModeTab.NORTHSTAR_TECH);
    
    
	
	
    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);
        return toReturn;
    }
    private static ToIntFunction<BlockState> litBlockEmission(int pLightValue) {
        return (p_50763_) -> {
           return p_50763_.getValue(BlockStateProperties.LIT) ? pLightValue : 0;
        };
     }
	
    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block,
            CreativeModeTab tab) {
return NorthstarItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
}
	
	public static void register(IEventBus eventBus)
	{
		BLOCKS.register(eventBus);
		
	}
}
