package com.lightning.northstar.block;

import static com.lightning.northstar.Northstar.REGISTRATE;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;
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
import com.lightning.northstar.item.NorthstarItems;
import com.lightning.northstar.world.features.NorthstarConfiguredFeatures;
import com.lightning.northstar.world.features.grower.ArgyreSaplingTreeGrower;
import com.lightning.northstar.world.features.grower.CoilerTreeGrower;
import com.lightning.northstar.world.features.grower.WilterTreeGrower;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
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
import net.minecraft.world.level.material.MapColor;
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
    		() -> new Block(BlockBehaviour.Properties.of().sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_GREEN).strength(30f,15f) .requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MARTIAN_STEEL_SHEETMETAL = registerBlock("martian_steel_sheetmetal",
    		() -> new Block(BlockBehaviour.Properties.of().sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_GREEN).strength(5f,15f) .requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MARTIAN_STEEL_SHEETMETAL_SLAB = registerBlock("martian_steel_sheetmetal_slab",
    		() -> new SlabBlock(BlockBehaviour.Properties.of() .sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_GREEN)  .strength(5f,15f) .requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MARTIAN_STEEL_SHEETMETAL_VERTICAL_SLAB = registerBlock("martian_steel_sheetmetal_vertical_slab",
    		() -> new VerticalSlabBlock(BlockBehaviour.Properties.of() .sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_GREEN)  .strength(5f,15f) .requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MARTIAN_STEEL_PLATING = registerBlock("martian_steel_plating",
    		() -> new Block(BlockBehaviour.Properties.of() .sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_GREEN)  .strength(5f,15f) .requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MARTIAN_STEEL_LARGE_PLATING = registerBlock("martian_steel_large_plating",
    		() -> new Block(BlockBehaviour.Properties.of() .sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_GREEN)  .strength(5f,15f) .requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MARTIAN_STEEL_PLATING_SLAB = registerBlock("martian_steel_plating_slab",
    		() -> new SlabBlock(BlockBehaviour.Properties.of() .sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_GREEN)  .strength(5f,15f) .requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MARTIAN_STEEL_PLATING_VERTICAL_SLAB = registerBlock("martian_steel_plating_vertical_slab",
    		() -> new VerticalSlabBlock(BlockBehaviour.Properties.of() .sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_GREEN)  .strength(5f,15f) .requiresCorrectToolForDrops()));
	@SuppressWarnings("deprecation")
	public static final RegistryObject<Block> MARTIAN_STEEL_PLATING_STAIRS = registerBlock("martian_steel_plating_stairs",
    		() -> new StairBlock(MARTIAN_STEEL_PLATING.get().defaultBlockState(), BlockBehaviour.Properties.of() .sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_GREEN)  .strength(5f,15f) .requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MARTIAN_STEEL_PILLAR = registerBlock("martian_steel_pillar",
    		() -> new RotatedPillarBlock(BlockBehaviour.Properties.of() .sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_GREEN)  .strength(5f,15f) .requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MARTIAN_STEEL_GRATE = registerBlock("martian_steel_grate",
    		() -> new GrateBlock(BlockBehaviour.Properties.of().sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_GREEN)  
    		.strength(4f,12f).requiresCorrectToolForDrops().noOcclusion().isSuffocating(NorthstarBlocks::never).isViewBlocking(NorthstarBlocks::never)));
    public static final RegistryObject<Block> MARTIAN_STEEL_LAMP = registerBlock("martian_steel_lamp",
    		() -> new Block(BlockBehaviour.Properties.of().lightLevel((p_50872_) -> {return 15;}) .sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_GREEN)  .strength(4f,12f) .requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MARTIAN_STEEL_BLUE_LAMP = registerBlock("martian_steel_blue_lamp",
    		() -> new Block(BlockBehaviour.Properties.of().lightLevel((p_50872_) -> {return 15;}) .sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_GREEN)  .strength(4f,12f) .requiresCorrectToolForDrops()));
    
    //iron stuff
    public static final RegistryObject<Block> IRON_SHEETMETAL = registerBlock("iron_sheetmetal",
    		() -> new Block(BlockBehaviour.Properties.of() .sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_GRAY)  .strength(4f,12f) .requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> IRON_SHEETMETAL_SLAB = registerBlock("iron_sheetmetal_slab",
    		() -> new SlabBlock(BlockBehaviour.Properties.of() .sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_GRAY) .strength(4f,12f) .requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> IRON_SHEETMETAL_VERTICAL_SLAB = registerBlock("iron_sheetmetal_vertical_slab",
    		() -> new VerticalSlabBlock(BlockBehaviour.Properties.of() .sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_GRAY) .strength(4f,12f) .requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> IRON_PLATING = registerBlock("iron_plating",
    		() -> new Block(BlockBehaviour.Properties.of() .sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_GRAY) .strength(4f,12f) .requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> IRON_PLATING_SLAB = registerBlock("iron_plating_slab",
    		() -> new SlabBlock(BlockBehaviour.Properties.of() .sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_GRAY) .strength(4f,12f) .requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> IRON_PLATING_VERTICAL_SLAB = registerBlock("iron_plating_vertical_slab",
    		() -> new VerticalSlabBlock(BlockBehaviour.Properties.of() .sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_GRAY) .strength(4f,12f) .requiresCorrectToolForDrops()));
    @SuppressWarnings("deprecation")
	public static final RegistryObject<Block> IRON_PLATING_STAIRS = registerBlock("iron_plating_stairs",
    		() -> new StairBlock(IRON_PLATING.get().defaultBlockState(), BlockBehaviour.Properties.of() .sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_GRAY) .strength(4f,12f) .requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> IRON_PILLAR = registerBlock("iron_pillar",
    		() -> new RotatedPillarBlock(BlockBehaviour.Properties.of() .sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_GRAY) .strength(4f,12f) .requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> IRON_GRATE = registerBlock("iron_grate",
    		() -> new GrateBlock(BlockBehaviour.Properties.of().sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_GRAY) 
    		.strength(4f,8f).requiresCorrectToolForDrops().noOcclusion().isSuffocating(NorthstarBlocks::never).isViewBlocking(NorthstarBlocks::never)));
    
    
    public static final RegistryObject<Block> VENT_BLOCK = registerBlock("vent_block",
    		() -> new GrateBlock(BlockBehaviour.Properties.of().sound(SoundType.NETHERITE_BLOCK) .mapColor(MapColor.COLOR_GRAY)
    		.strength(4f,8f).requiresCorrectToolForDrops().noOcclusion().isSuffocating(NorthstarBlocks::never).isViewBlocking(NorthstarBlocks::never)));
    
    
    //tungsten stuff
    public static final RegistryObject<Block> TUNGSTEN_BLOCK = registerBlock("tungsten_block",
    		() -> new Block(BlockBehaviour.Properties.of() .sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_BLACK) .strength(30f,16f) .requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> TUNGSTEN_SHEETMETAL = registerBlock("tungsten_sheetmetal",
    		() -> new Block(BlockBehaviour.Properties.of() .sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_BLACK) .strength(6f,16f) .requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> TUNGSTEN_SHEETMETAL_SLAB = registerBlock("tungsten_sheetmetal_slab",
    		() -> new SlabBlock(BlockBehaviour.Properties.of() .sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_BLACK) .strength(6f,16f) .requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> TUNGSTEN_SHEETMETAL_VERTICAL_SLAB = registerBlock("tungsten_sheetmetal_vertical_slab",
    		() -> new VerticalSlabBlock(BlockBehaviour.Properties.of() .sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_BLACK) .strength(6f,16f) .requiresCorrectToolForDrops()));
   public static final RegistryObject<Block> TUNGSTEN_PLATING = registerBlock("tungsten_plating",
    		() -> new Block(BlockBehaviour.Properties.of() .sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_BLACK) .strength(6f,16f) .requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> TUNGSTEN_PLATING_SLAB = registerBlock("tungsten_plating_slab",
    		() -> new SlabBlock(BlockBehaviour.Properties.of() .sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_BLACK) .strength(6f,16f) .requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> TUNGSTEN_PLATING_VERTICAL_SLAB = registerBlock("tungsten_plating_vertical_slab",
    		() -> new VerticalSlabBlock(BlockBehaviour.Properties.of() .sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_BLACK) .strength(6f,16f) .requiresCorrectToolForDrops()));
    @SuppressWarnings("deprecation")
	public static final RegistryObject<Block> TUNGSTEN_PLATING_STAIRS = registerBlock("tungsten_plating_stairs",
    		() -> new StairBlock(TUNGSTEN_PLATING.get().defaultBlockState(), BlockBehaviour.Properties.of() .sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_BLACK) .strength(6f,16f) .requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> TUNGSTEN_PILLAR = registerBlock("tungsten_pillar",
    		() -> new RotatedPillarBlock(BlockBehaviour.Properties.of() .sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_BLACK) .strength(6f,16f) .requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> TUNGSTEN_GRATE = registerBlock("tungsten_grate",
    		() -> new GrateBlock(BlockBehaviour.Properties.of().sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_BLACK) 
    		.strength(5f,16f).requiresCorrectToolForDrops().noOcclusion().isSuffocating(NorthstarBlocks::never).isViewBlocking(NorthstarBlocks::never)));    
    
    public static final RegistryObject<Block> GLOWSTONE_LAMP = registerBlock("glowstone_lamp",
    		() -> new Block(BlockBehaviour.Properties.of().lightLevel((p_50872_) -> {return 15;}) .sound(SoundType.GLASS).mapColor(MapColor.COLOR_YELLOW) .strength(2f,5f)));    
    
    public static final RegistryObject<Block> MARS_SAND = registerBlock("mars_sand",
            () -> new GravelBlock(BlockBehaviour.Properties.of().sound(SoundType.GRAVEL).mapColor(MapColor.COLOR_ORANGE)
                    .strength(0.5f, 1.6f)));
    public static final RegistryObject<Block> MARS_GRAVEL = registerBlock("mars_gravel",
    		() -> new GravelBlock(BlockBehaviour.Properties.of().sound(SoundType.GRAVEL).mapColor(MapColor.COLOR_BROWN)
                    .strength(0.65f, 2.0f)));
    public static final RegistryObject<Block> MARS_SOIL = registerBlock("mars_soil",
            () -> new MarsSoilBlock(BlockBehaviour.Properties.of().sound(SoundType.GRAVEL).mapColor(MapColor.TERRACOTTA_ORANGE)
                    .strength(0.5f, 8f)));
    public static final RegistryObject<Block> MARTIAN_GRASS = registerBlock("martian_grass",
            () -> new MartianGrassBlock(BlockBehaviour.Properties.of().sound(SoundType.GRASS).mapColor(MapColor.COLOR_PURPLE)
                    .strength(0.65f, 8f).randomTicks()));
    public static final RegistryObject<Block> MARTIAN_TALL_GRASS = registerBlock("martian_tall_grass",
            () -> new MartianTallGrassBlock(BlockBehaviour.Properties.of().sound(SoundType.GRASS).mapColor(MapColor.COLOR_PURPLE)
            		.noCollission().instabreak().randomTicks().offsetType(BlockBehaviour.OffsetType.XYZ)));
    public static final RegistryObject<Block> MARS_FARMLAND = registerBlock("mars_farmland",
            () -> new MarsFarmlandBlock(BlockBehaviour.Properties.of().randomTicks().sound(SoundType.GRAVEL).mapColor(MapColor.COLOR_ORANGE)
                    .strength(0.5f, 8f)));
    public static final RegistryObject<Block> MARS_WORM_NEST = registerBlock("mars_worm_nest",
            () -> new MarsWormNestBlock(BlockBehaviour.Properties.of().randomTicks().sound(SoundType.GRASS).mapColor(MapColor.COLOR_BROWN)
                    .strength(0.2f, 0.2f)));
    public static final RegistryObject<Block> MARS_ROOTS = registerBlock("mars_roots",
            () -> new MarsRootBlock(BlockBehaviour.Properties.of().sound(SoundType.VINE).mapColor(MapColor.COLOR_BROWN)
                    .noOcclusion() .noCollission() .strength(0.2f).randomTicks().isSuffocating(NorthstarBlocks::never).isViewBlocking(NorthstarBlocks::never)));
    public static final RegistryObject<Block> GLOWING_MARS_ROOTS = registerBlock("glowing_mars_roots",
            () -> new MarsRootBlock(BlockBehaviour.Properties.of().lightLevel((p_50872_) -> {return 10;}).sound(SoundType.VINE).mapColor(MapColor.COLOR_BROWN)
                    .noOcclusion() .noCollission() .strength(0.2f).randomTicks().isSuffocating(NorthstarBlocks::never).isViewBlocking(NorthstarBlocks::never)));
    

    public static final RegistryObject<Block> STRIPPED_COILER_LOG = registerBlock("stripped_coiler_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().sound(SoundType.WOOD).mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2f)));
    public static final RegistryObject<Block> COILER_LOG = registerBlock("coiler_log",
            () -> new LogBlock(BlockBehaviour.Properties.of().sound(SoundType.WOOD).mapColor(MapColor.COLOR_GREEN)
                    .strength(2f), STRIPPED_COILER_LOG.get()));
    public static final RegistryObject<Block> COILER_PLANKS = registerBlock("coiler_planks",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.WOOD).mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2f, 3f)));
    public static final RegistryObject<Block> COILER_SLAB = registerBlock("coiler_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of().sound(SoundType.WOOD).mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2f, 3f)));
    @SuppressWarnings("deprecation")
	public static final RegistryObject<Block> COILER_STAIRS = registerBlock("coiler_stairs",
            () -> new StairBlock(OAK_PLANKS.defaultBlockState(), BlockBehaviour.Properties.of().sound(SoundType.WOOD).mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .strength(2f, 3f)));
    
    
    public static final RegistryObject<Block> COILER_LEAVES = registerBlock("coiler_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.of().sound(SoundType.GRASS).mapColor(MapColor.COLOR_PINK)
                    .noOcclusion() .strength(0.5f).isSuffocating(NorthstarBlocks::never).isViewBlocking(NorthstarBlocks::never)));
    public static final RegistryObject<Block> COILER_VINES = registerBlock("coiler_vines",
            () -> new VineBlock(BlockBehaviour.Properties.of().sound(SoundType.VINE).mapColor(MapColor.COLOR_PINK)
                    .noOcclusion().noCollission().randomTicks().strength(0.2f).isSuffocating(NorthstarBlocks::never).isViewBlocking(NorthstarBlocks::never)));
    public static final RegistryObject<Block> COILER_SAPLING = registerBlock("coiler_sapling",
            () -> new SaplingBlock(new CoilerTreeGrower(), BlockBehaviour.Properties.of().sound(SoundType.GRASS).mapColor(MapColor.COLOR_PURPLE)
                    .randomTicks() .instabreak() .noCollission()));
    public static final RegistryObject<Block> MARTIAN_STRAWBERRY_BUSH = registerBlock("martian_strawberry_bush",
            () -> new MartianStrawberryBushBlock(BlockBehaviour.Properties.of().sound(SoundType.GRASS).mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .randomTicks() .instabreak() .noCollission()));
    
    

    public static final RegistryObject<Block> STRIPPED_WILTER_LOG = registerBlock("stripped_wilter_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().sound(SoundType.WOOD).mapColor(MapColor.COLOR_BLUE)
                    .strength(2f)));
    public static final RegistryObject<Block> WILTER_LOG = registerBlock("wilter_log",
            () -> new LogBlock(BlockBehaviour.Properties.of().sound(SoundType.WOOD).mapColor(MapColor.COLOR_BLACK)
                    .strength(2f), STRIPPED_WILTER_LOG.get()));
    public static final RegistryObject<Block> WILTER_PLANKS = registerBlock("wilter_planks",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.WOOD).mapColor(MapColor.COLOR_BLUE)
                    .strength(2f)));
    public static final RegistryObject<Block> WILTER_SLAB = registerBlock("wilter_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of().sound(SoundType.WOOD).mapColor(MapColor.COLOR_BLUE)
                    .strength(2f, 3f)));
    @SuppressWarnings("deprecation")
	public static final RegistryObject<Block> WILTER_STAIRS = registerBlock("wilter_stairs",
            () -> new StairBlock(OAK_PLANKS.defaultBlockState(), BlockBehaviour.Properties.of().sound(SoundType.WOOD).mapColor(MapColor.COLOR_BLUE)
                    .strength(2f, 3f)));
    

    public static final RegistryObject<Block> STRIPPED_ARGYRE_LOG = registerBlock("stripped_argyre_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().sound(SoundType.WOOD).mapColor(MapColor.COLOR_RED)
                    .strength(2f)));
    public static final RegistryObject<Block> ARGYRE_LOG = registerBlock("argyre_log",
            () -> new LogBlock(BlockBehaviour.Properties.of().sound(SoundType.WOOD).mapColor(MapColor.TERRACOTTA_RED)
                    .strength(2f), STRIPPED_ARGYRE_LOG.get()));
    public static final RegistryObject<Block> ARGYRE_LEAVES = registerBlock("argyre_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.of().sound(SoundType.GRASS).mapColor(MapColor.COLOR_RED)
                    .noOcclusion() .strength(0.5f).isSuffocating(NorthstarBlocks::never).isViewBlocking(NorthstarBlocks::never)));
    public static final RegistryObject<Block> ARGYRE_PLANKS = registerBlock("argyre_planks",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.WOOD).mapColor(MapColor.COLOR_RED)
                    .strength(2f, 3f)));
    public static final RegistryObject<Block> ARGYRE_SLAB = registerBlock("argyre_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of().sound(SoundType.WOOD).mapColor(MapColor.COLOR_RED)
                    .strength(2f, 3f)));
    @SuppressWarnings("deprecation")
	public static final RegistryObject<Block> ARGYRE_STAIRS = registerBlock("argyre_stairs",
            () -> new StairBlock(OAK_PLANKS.defaultBlockState(), BlockBehaviour.Properties.of().sound(SoundType.WOOD).mapColor(MapColor.COLOR_RED)
                    .strength(2f, 3f)));
    
    
    
    public static final RegistryObject<Block> ARGYRE_SAPLING = registerBlock("argyre_sapling",
            () -> new SaplingBlock(new ArgyreSaplingTreeGrower(), BlockBehaviour.Properties.of().sound(SoundType.GRASS).mapColor(MapColor.COLOR_RED)
                    .randomTicks() .instabreak() .noCollission()));
    
    
    
    public static final RegistryObject<Block> WILTER_LEAVES = registerBlock("wilter_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.of().sound(SoundType.GRASS).mapColor(MapColor.TERRACOTTA_PURPLE)
                    .noOcclusion() .strength(0.5f).isSuffocating(NorthstarBlocks::never).isViewBlocking(NorthstarBlocks::never)));
    public static final RegistryObject<Block> WILTER_SAPLING = registerBlock("wilter_sapling",
            () -> new SaplingBlock(new WilterTreeGrower(), BlockBehaviour.Properties.of().sound(SoundType.GRASS).mapColor(MapColor.COLOR_BLUE)
                    .randomTicks() .instabreak() .noCollission()));
    
    
    public static final RegistryObject<Block> MARS_TULIP = registerBlock("mars_tulip",
            () -> new MarsTulipBlock(BlockBehaviour.Properties.of().sound(SoundType.GRASS).mapColor(MapColor.COLOR_ORANGE)
                    .randomTicks() .instabreak() .noCollission().offsetType(BlockBehaviour.OffsetType.XZ)));
    public static final RegistryObject<Block> MARS_PALM = registerBlock("mars_palm",
            () -> new MarsPalmBlock(BlockBehaviour.Properties.of().sound(SoundType.GRASS).mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .randomTicks() .instabreak() .noCollission().offsetType(BlockBehaviour.OffsetType.XZ)));
    public static final RegistryObject<Block> MARS_SPROUT = registerBlock("mars_sprout",
            () -> new MarsSproutBlock(BlockBehaviour.Properties.of().sound(SoundType.GRASS).mapColor(MapColor.COLOR_PINK)
                    .randomTicks() .instabreak() .noCollission().offsetType(BlockBehaviour.OffsetType.XZ).lightLevel((l) -> {return 7;})));
    public static final RegistryObject<Block> MARS_SPROUT_BIG = registerBlock("mars_sprout_big",
            () -> new MartianTallFlowerBlock(BlockBehaviour.Properties.of().sound(SoundType.GRASS).mapColor(MapColor.COLOR_PINK)
                    .randomTicks() .instabreak() .noCollission().lightLevel((l) -> {return 14;})));
    
    public static final RegistryObject<Block> POINTED_CRIMSITE = registerBlock("pointed_crimsite",
            () -> new PointedCrimsiteBlock(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.COLOR_RED)
                    .strength(3.5f, 12f).noOcclusion().requiresCorrectToolForDrops().dynamicShape().offsetType(BlockBehaviour.OffsetType.XZ)));
    
    //mars stone stuff
    public static final RegistryObject<Block> MARS_STONE = registerBlock("mars_stone",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.COLOR_ORANGE)
                    .strength(3.5f, 8f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MARS_DEEP_STONE = registerBlock("mars_deep_stone",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.COLOR_BROWN)
                    .strength(4.5f, 8f).requiresCorrectToolForDrops()));
    //mars deco blocks
    public static final RegistryObject<Block> MARS_STONE_BRICKS = registerBlock("mars_stone_bricks",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE_BRICKS).mapColor(MapColor.COLOR_ORANGE)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MARS_STONE_BRICK_SLAB = registerBlock("mars_stone_brick_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE_BRICKS).mapColor(MapColor.COLOR_ORANGE)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MARS_STONE_BRICK_SLAB_VERTICAL = registerBlock("mars_stone_brick_slab_vertical",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE_BRICKS).mapColor(MapColor.COLOR_ORANGE)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()));
    @SuppressWarnings("deprecation")
	public static final RegistryObject<Block> MARS_STONE_BRICK_STAIRS = registerBlock("mars_stone_brick_stairs",
            () -> new StairBlock(STONE.defaultBlockState() ,BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE_BRICKS).mapColor(MapColor.COLOR_ORANGE)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MARS_STONE_BRICK_WALL = registerBlock("mars_stone_brick_wall",
            () -> new WallBlock(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE_BRICKS).mapColor(MapColor.COLOR_ORANGE)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MARS_STONE_PILLAR = registerBlock("mars_stone_pillar",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE_BRICKS).mapColor(MapColor.COLOR_ORANGE)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> CHISELED_MARS_STONE = registerBlock("chiseled_mars_stone",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE_BRICKS).mapColor(MapColor.COLOR_ORANGE)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> POLISHED_MARS_STONE = registerBlock("polished_mars_stone",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE_BRICKS).mapColor(MapColor.COLOR_ORANGE)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MARS_STONE_LAMP = registerBlock("mars_stone_lamp",
    		() -> new Block(BlockBehaviour.Properties.of().lightLevel((p_50872_) -> {return 15;}) .sound(SoundType.GLASS).mapColor(MapColor.COLOR_YELLOW) .strength(3f,6.5f) .requiresCorrectToolForDrops())); 

    //mars ores
    public static final RegistryObject<Block> MARS_IRON_ORE = registerBlock("mars_iron_ore",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.COLOR_ORANGE)
                    .strength(5f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MARS_COPPER_ORE = registerBlock("mars_copper_ore",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.COLOR_ORANGE)
                    .strength(5f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MARS_GOLD_ORE = registerBlock("mars_gold_ore",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.COLOR_ORANGE)
                    .strength(5f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MARS_DIAMOND_ORE = registerBlock("mars_diamond_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.COLOR_ORANGE)
                    .strength(6f, 12f).requiresCorrectToolForDrops(),UniformInt.of(3, 7)));
    public static final RegistryObject<Block> MARS_REDSTONE_ORE = registerBlock("mars_redstone_ore",
            () -> new RedStoneOreBlock(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.COLOR_ORANGE)
                    .strength(5f, 12f).requiresCorrectToolForDrops().randomTicks().lightLevel(litBlockEmission(9))));
    public static final RegistryObject<Block> MARS_QUARTZ_ORE = registerBlock("mars_quartz_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.COLOR_ORANGE)
                    .strength(5f, 12f).requiresCorrectToolForDrops(),  UniformInt.of(2, 5)));
    //mars deep ores
    public static final RegistryObject<Block> MARS_DEEP_IRON_ORE = registerBlock("mars_deep_iron_ore",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.COLOR_BROWN)
                    .strength(6f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MARS_DEEP_COPPER_ORE = registerBlock("mars_deep_copper_ore",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.COLOR_BROWN)
                    .strength(6f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MARS_DEEP_GOLD_ORE = registerBlock("mars_deep_gold_ore",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.COLOR_BROWN)
                    .strength(6f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MARS_DEEP_DIAMOND_ORE = registerBlock("mars_deep_diamond_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.COLOR_BROWN)
                    .strength(7f, 12f).requiresCorrectToolForDrops(),UniformInt.of(3, 7)));
    public static final RegistryObject<Block> MARS_DEEP_REDSTONE_ORE = registerBlock("mars_deep_redstone_ore",
            () -> new RedStoneOreBlock(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.COLOR_BROWN)
                    .strength(6f, 12f).requiresCorrectToolForDrops().randomTicks().lightLevel(litBlockEmission(9))));
    public static final RegistryObject<Block> MARS_DEEP_QUARTZ_ORE = registerBlock("mars_deep_quartz_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.COLOR_BROWN)
                    .strength(6f, 12f).requiresCorrectToolForDrops(),UniformInt.of(2, 5)));
    
    
    public static final RegistryObject<Block> VOLCANIC_ASH = registerBlock("volcanic_ash",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.TUFF).mapColor(MapColor.COLOR_BROWN)
                    .strength(0.4f, 2f)));
    public static final RegistryObject<Block> VOLCANIC_ROCK = registerBlock("volcanic_rock",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.TUFF).mapColor(MapColor.COLOR_BROWN)
                    .strength(2f, 4f).requiresCorrectToolForDrops()));
    
    
    //moon stone stuff
    public static final RegistryObject<Block> MOON_SAND = registerBlock("moon_sand",
            () -> new GravelBlock(BlockBehaviour.Properties.of().sound(SoundType.SAND).mapColor(MapColor.COLOR_LIGHT_GRAY)
                    .strength(0.5f, 8.0f)));
    public static final RegistryObject<Block> MOON_STONE = registerBlock("moon_stone",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.COLOR_GRAY)
                    .strength(3.5f, 8f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MOON_DEEP_STONE = registerBlock("moon_deep_stone",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.COLOR_GRAY)
                    .strength(4.5f, 8f).requiresCorrectToolForDrops()));
    
    public static final RegistryObject<Block> FROST = registerBlock("frost",
            () -> new FrostBlock(BlockBehaviour.Properties.of().sound(SoundType.CALCITE).mapColor(MapColor.COLOR_LIGHT_BLUE).friction(0.989F)
                    .noOcclusion() .noCollission() .strength(0.2f).isSuffocating(NorthstarBlocks::never).isViewBlocking(NorthstarBlocks::never)));
   
    
    //moon deco stuff ayy
    public static final RegistryObject<Block> MOON_STONE_BRICKS = registerBlock("moon_stone_bricks",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE_BRICKS).mapColor(MapColor.COLOR_GRAY)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MOON_STONE_BRICK_SLAB = registerBlock("moon_stone_brick_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE_BRICKS).mapColor(MapColor.COLOR_GRAY)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MOON_STONE_BRICK_SLAB_VERTICAL = registerBlock("moon_stone_brick_slab_vertical",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE_BRICKS).mapColor(MapColor.COLOR_GRAY)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()));
    @SuppressWarnings("deprecation")
	public static final RegistryObject<Block> MOON_STONE_BRICK_STAIRS = registerBlock("moon_stone_brick_stairs",
            () -> new StairBlock(STONE.defaultBlockState() ,BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE_BRICKS).mapColor(MapColor.COLOR_GRAY)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MOON_STONE_BRICK_WALL = registerBlock("moon_stone_brick_wall",
            () -> new WallBlock(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE_BRICKS).mapColor(MapColor.COLOR_GRAY)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MOON_STONE_PILLAR = registerBlock("moon_stone_pillar",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE_BRICKS).mapColor(MapColor.COLOR_GRAY)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MOON_STONE_LAMP = registerBlock("moon_stone_lamp",
    		() -> new Block(BlockBehaviour.Properties.of().lightLevel((p_50872_) -> {return 15;}) .sound(SoundType.GLASS).mapColor(MapColor.COLOR_YELLOW) .strength(3f,6.5f) .requiresCorrectToolForDrops())); 
    public static final RegistryObject<Block> CHISELED_MOON_STONE = registerBlock("chiseled_moon_stone",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE_BRICKS).mapColor(MapColor.COLOR_GRAY)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> POLISHED_MOON_STONE = registerBlock("polished_moon_stone",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE_BRICKS).mapColor(MapColor.COLOR_GRAY)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()));
    
    //moon ores
    public static final RegistryObject<Block> MOON_IRON_ORE = registerBlock("moon_iron_ore",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.COLOR_GRAY)
                    .strength(5f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MOON_COPPER_ORE = registerBlock("moon_copper_ore",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.COLOR_GRAY)
                    .strength(5f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MOON_GOLD_ORE = registerBlock("moon_gold_ore",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.COLOR_GRAY)
                    .strength(5f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MOON_DIAMOND_ORE = registerBlock("moon_diamond_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.COLOR_GRAY)
                    .strength(6f, 12f).requiresCorrectToolForDrops(),UniformInt.of(3, 7)));
    public static final RegistryObject<Block> MOON_REDSTONE_ORE = registerBlock("moon_redstone_ore",
            () -> new RedStoneOreBlock(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.COLOR_GRAY)
                    .strength(5f, 12f).requiresCorrectToolForDrops().randomTicks().lightLevel(litBlockEmission(9))));
    public static final RegistryObject<Block> MOON_LAPIS_ORE = registerBlock("moon_lapis_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.COLOR_GRAY)
                    .strength(5f, 12f).requiresCorrectToolForDrops(),  UniformInt.of(2, 5)));
    public static final RegistryObject<Block> MOON_ZINC_ORE = registerBlock("moon_zinc_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.COLOR_GRAY)
                    .strength(5f, 12f).requiresCorrectToolForDrops(),  UniformInt.of(2, 5)));
    public static final RegistryObject<Block> MOON_GLOWSTONE_ORE = registerBlock("moon_glowstone_ore",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.COLOR_YELLOW)
                    .strength(5f, 12f).requiresCorrectToolForDrops().lightLevel((p_50872_) -> {return 15;})));
    //moon deep ores
    public static final RegistryObject<Block> MOON_DEEP_IRON_ORE = registerBlock("moon_deep_iron_ore",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.COLOR_GRAY)
                    .strength(6f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MOON_DEEP_COPPER_ORE = registerBlock("moon_deep_copper_ore",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.COLOR_GRAY)
                    .strength(6f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MOON_DEEP_GOLD_ORE = registerBlock("moon_deep_gold_ore",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.COLOR_GRAY)
                    .strength(6f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MOON_DEEP_DIAMOND_ORE = registerBlock("moon_deep_diamond_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.COLOR_GRAY)
                    .strength(7f, 12f).requiresCorrectToolForDrops(),UniformInt.of(3, 7)));
    public static final RegistryObject<Block> MOON_DEEP_REDSTONE_ORE = registerBlock("moon_deep_redstone_ore",
            () -> new RedStoneOreBlock(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.COLOR_GRAY)
                    .strength(6f, 12f).requiresCorrectToolForDrops().randomTicks().lightLevel(litBlockEmission(9))));
    public static final RegistryObject<Block> MOON_DEEP_LAPIS_ORE = registerBlock("moon_deep_lapis_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.COLOR_GRAY)
                    .strength(6f, 12f).requiresCorrectToolForDrops(),UniformInt.of(2, 5)));
    public static final RegistryObject<Block> MOON_DEEP_ZINC_ORE = registerBlock("moon_deep_zinc_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.COLOR_GRAY)
                    .strength(6f, 12f).requiresCorrectToolForDrops(),UniformInt.of(2, 5)));
    public static final RegistryObject<Block> MOON_DEEP_GLOWSTONE_ORE = registerBlock("moon_deep_glowstone_ore",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.COLOR_YELLOW)
                    .strength(6f, 12f).requiresCorrectToolForDrops().lightLevel((p_50872_) -> {return 15;})));
    
    public static final RegistryObject<Block> LUNAR_SAPPHIRE_BLOCK = registerBlock("lunar_sapphire_block",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.AMETHYST).mapColor(MapColor.COLOR_BLUE)
                    .strength(2f, 5f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> LUNAR_SAPPHIRE_CLUSTER = registerBlock("lunar_sapphire_cluster",
            () -> new ClusterBlock(7, 3, BlockBehaviour.Properties.of().sound(SoundType.AMETHYST).mapColor(MapColor.COLOR_BLUE)
                    .strength(2f, 5f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> SMALL_LUNAR_SAPPHIRE_BUD = registerBlock("small_lunar_sapphire_bud",
            () -> new ClusterBlock(3, 4, BlockBehaviour.Properties.of().sound(SoundType.AMETHYST).mapColor(MapColor.COLOR_BLUE)
                    .strength(1.5f, 5f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MEDIUM_LUNAR_SAPPHIRE_BUD = registerBlock("medium_lunar_sapphire_bud",
            () -> new ClusterBlock(4, 3, BlockBehaviour.Properties.of().sound(SoundType.AMETHYST).mapColor(MapColor.COLOR_BLUE)
                    .strength(1.5f, 5f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> LARGE_LUNAR_SAPPHIRE_BUD = registerBlock("large_lunar_sapphire_bud",
            () -> new ClusterBlock(5, 3, BlockBehaviour.Properties.of().sound(SoundType.AMETHYST).mapColor(MapColor.COLOR_BLUE)
                    .strength(1.5f, 5f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> BUDDING_LUNAR_SAPPHIRE_BLOCK = registerBlock("budding_lunar_sapphire_block",
            () -> new BuddingCrystalBlock(BlockBehaviour.Properties.of().sound(SoundType.AMETHYST).mapColor(MapColor.COLOR_BLUE)
                    .strength(2f, 5f).randomTicks().requiresCorrectToolForDrops(), SMALL_LUNAR_SAPPHIRE_BUD.get(), MEDIUM_LUNAR_SAPPHIRE_BUD.get(),
                    LARGE_LUNAR_SAPPHIRE_BUD.get(), LUNAR_SAPPHIRE_CLUSTER.get()));
    
    
    
    
  //venus stone stuff
    public static final RegistryObject<Block> VENUS_STONE = registerBlock("venus_stone",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.TERRACOTTA_YELLOW)
                    .strength(3.5f, 8f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> VENUS_DEEP_STONE = registerBlock("venus_deep_stone",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.TERRACOTTA_YELLOW)
                    .strength(4.5f, 8f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> VENUS_GRAVEL = registerBlock("venus_gravel",
            () -> new GravelBlock(BlockBehaviour.Properties.of().sound(SoundType.GRAVEL).mapColor(MapColor.TERRACOTTA_GREEN)
                    .strength(1.1f, 2f)));
    public static final RegistryObject<Block> VENUS_PLUME = registerBlock("venus_plume",
            () -> new VenusExhaustBlock(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.TERRACOTTA_YELLOW)
                    .strength(3.5f, 8f).requiresCorrectToolForDrops()));
    
    public static final RegistryObject<Block> SPIKE_FUNGUS = registerBlock("spike_fungus",
            () -> new VenusMushroomBlock(BlockBehaviour.Properties.of().sound(SoundType.FUNGUS).mapColor(MapColor.TERRACOTTA_WHITE)
            		.strength(0f, 0.5f).noCollission().instabreak(), NorthstarConfiguredFeatures.SPIKE_FUNGUS, null));
    public static final RegistryObject<Block> BLOOM_FUNGUS = registerBlock("bloom_fungus",
            () -> new VenusMushroomBlock(BlockBehaviour.Properties.of().sound(SoundType.FUNGUS).mapColor(MapColor.COLOR_ORANGE)
            		.strength(0f, 0.5f).noCollission().instabreak().lightLevel((p_50872_) -> {return 7;}),NorthstarConfiguredFeatures.BLOOM_FUNGUS, NorthstarConfiguredFeatures.ROOF_BLOOM_FUNGUS));
    public static final RegistryObject<Block> PLATE_FUNGUS = registerBlock("plate_fungus",
            () -> new VenusMushroomBlock(BlockBehaviour.Properties.of().sound(SoundType.FUNGUS).mapColor(MapColor.COLOR_GREEN)
            		.strength(0f, 0.5f).noCollission().instabreak(), NorthstarConfiguredFeatures.PLATE_FUNGUS, NorthstarConfiguredFeatures.ROOF_PLATE_FUNGUS));
    public static final RegistryObject<Block> SPIKE_FUNGUS_BLOCK = registerBlock("spike_fungus_block",
            () -> new HugeMushroomBlock(BlockBehaviour.Properties.of().sound(SoundType.FUNGUS).mapColor(MapColor.TERRACOTTA_WHITE)
            		.strength(3f, 6f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> BLOOM_FUNGUS_BLOCK = registerBlock("bloom_fungus_block",
            () -> new HugeMushroomBlock(BlockBehaviour.Properties.of().sound(SoundType.FUNGUS).mapColor(MapColor.TERRACOTTA_RED)
            		.strength(3f, 6f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> BLOOM_FUNGUS_STEM_BLOCK = registerBlock("bloom_fungus_stem_block",
            () -> new HugeMushroomBlock(BlockBehaviour.Properties.of().sound(SoundType.FUNGUS).mapColor(MapColor.COLOR_ORANGE)
            		.strength(3f, 6f).requiresCorrectToolForDrops()));
    
    public static final RegistryObject<Block> PLATE_FUNGUS_STEM_BLOCK = registerBlock("plate_fungus_stem_block",
            () -> new HugeMushroomBlock(BlockBehaviour.Properties.of().sound(SoundType.FUNGUS).mapColor(MapColor.COLOR_GREEN)
            		.strength(3f, 6f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> PLATE_FUNGUS_CAP_BLOCK = registerBlock("plate_fungus_cap_block",
            () -> new HugeMushroomBlock(BlockBehaviour.Properties   .of().sound(SoundType.FUNGUS).mapColor(MapColor.COLOR_LIGHT_GREEN)
            		.strength(4f, 6f).requiresCorrectToolForDrops()));
    
    public static final RegistryObject<Block> TOWER_FUNGUS = registerBlock("tower_fungus",
            () -> new TallFungusBlock(BlockBehaviour.Properties.of().sound(SoundType.FUNGUS).mapColor(MapColor.TERRACOTTA_BLUE)
                    .randomTicks() .instabreak() .noCollission(),NorthstarConfiguredFeatures.TOWER_FUNGUS,NorthstarConfiguredFeatures.ROOF_TOWER_FUNGUS));
    public static final RegistryObject<Block> TOWER_FUNGUS_STEM_BLOCK = registerBlock("tower_fungus_stem_block",
            () -> new HugeMushroomBlock(BlockBehaviour.Properties.of().sound(SoundType.FUNGUS).mapColor(MapColor.TERRACOTTA_BLUE)
            		.strength(3f, 6f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> TOWER_FUNGUS_CAP_BLOCK = registerBlock("tower_fungus_cap_block",
            () -> new HugeMushroomBlock(BlockBehaviour.Properties.of().sound(SoundType.FUNGUS).mapColor(MapColor.TERRACOTTA_BLUE)
            		.strength(4f, 6f).requiresCorrectToolForDrops()));
    
    public static final RegistryObject<Block> VENUS_VINES = registerBlock("venus_vines",
            () -> new VenusVinesBlock(BlockBehaviour.Properties.of().sound(SoundType.GRASS).mapColor(MapColor.GRASS)
            		.strength(0.5f, 0.5f).randomTicks().noCollission().noOcclusion()));
    public static final RegistryObject<Block> GLOWING_VENUS_VINES = registerBlock("glowing_venus_vines",
            () -> new VenusVinesBlock(BlockBehaviour.Properties.of().sound(SoundType.GRASS).mapColor(MapColor.GRASS)
            		.strength(0.5f, 0.5f).randomTicks().noCollission().noOcclusion().lightLevel((light) -> {return 11;})));
    public static final RegistryObject<Block> TALL_VENUS_MYCELIUM = registerBlock("tall_venus_mycelium",
            () -> new VenusTallMyceliumBlock(BlockBehaviour.Properties.of().sound(SoundType.GRASS).mapColor(MapColor.RAW_IRON)
            		.noCollission().instabreak().randomTicks().offsetType(BlockBehaviour.OffsetType.XZ)));
    
  //venus deco blocks
    public static final RegistryObject<Block> VENUS_STONE_BRICKS = registerBlock("venus_stone_bricks",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE_BRICKS).mapColor(MapColor.TERRACOTTA_YELLOW)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> VENUS_STONE_BRICK_SLAB = registerBlock("venus_stone_brick_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE_BRICKS).mapColor(MapColor.TERRACOTTA_YELLOW)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> VENUS_STONE_BRICK_SLAB_VERTICAL = registerBlock("venus_stone_brick_slab_vertical",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE_BRICKS).mapColor(MapColor.TERRACOTTA_YELLOW)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()));
    @SuppressWarnings("deprecation")
	public static final RegistryObject<Block> VENUS_STONE_BRICK_STAIRS = registerBlock("venus_stone_brick_stairs",
            () -> new StairBlock(STONE.defaultBlockState() ,BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE_BRICKS).mapColor(MapColor.TERRACOTTA_YELLOW)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> VENUS_STONE_BRICK_WALL = registerBlock("venus_stone_brick_wall",
            () -> new WallBlock(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE_BRICKS).mapColor(MapColor.TERRACOTTA_YELLOW)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> VENUS_STONE_PILLAR = registerBlock("venus_stone_pillar",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE_BRICKS).mapColor(MapColor.TERRACOTTA_YELLOW)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> CHISELED_VENUS_STONE = registerBlock("chiseled_venus_stone",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE_BRICKS).mapColor(MapColor.TERRACOTTA_YELLOW)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> POLISHED_VENUS_STONE = registerBlock("polished_venus_stone",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE_BRICKS).mapColor(MapColor.TERRACOTTA_YELLOW)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> VENUS_STONE_LAMP = registerBlock("venus_stone_lamp",
    		() -> new Block(BlockBehaviour.Properties.of().lightLevel((p_50872_) -> {return 15;}) .sound(SoundType.GLASS).mapColor(MapColor.COLOR_YELLOW) .strength(3f,6.5f) .requiresCorrectToolForDrops())); 
    //venus ores
    public static final RegistryObject<Block> VENUS_COAL_ORE = registerBlock("venus_coal_ore",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.TERRACOTTA_YELLOW)
                    .strength(5f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> VENUS_IRON_ORE = registerBlock("venus_iron_ore",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.TERRACOTTA_YELLOW)
                    .strength(5f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> VENUS_COPPER_ORE = registerBlock("venus_copper_ore",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.TERRACOTTA_YELLOW)
                    .strength(5f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> VENUS_GOLD_ORE = registerBlock("venus_gold_ore",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.TERRACOTTA_YELLOW)
                    .strength(5f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> VENUS_DIAMOND_ORE = registerBlock("venus_diamond_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.TERRACOTTA_YELLOW)
                    .strength(6f, 12f).requiresCorrectToolForDrops(),UniformInt.of(3, 7)));
    public static final RegistryObject<Block> VENUS_REDSTONE_ORE = registerBlock("venus_redstone_ore",
            () -> new RedStoneOreBlock(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.TERRACOTTA_YELLOW)
                    .strength(5f, 12f).requiresCorrectToolForDrops().randomTicks().lightLevel(litBlockEmission(9))));
    public static final RegistryObject<Block> VENUS_QUARTZ_ORE = registerBlock("venus_quartz_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.TERRACOTTA_YELLOW)
                    .strength(5f, 12f).requiresCorrectToolForDrops(),  UniformInt.of(2, 5)));
    public static final RegistryObject<Block> VENUS_GLOWSTONE_ORE = registerBlock("venus_glowstone_ore",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.COLOR_YELLOW)
                    .strength(5f, 12f).requiresCorrectToolForDrops().lightLevel((p_50872_) -> {return 6;})));
    //venus deep ores
    public static final RegistryObject<Block> VENUS_DEEP_IRON_ORE = registerBlock("venus_deep_iron_ore",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.TERRACOTTA_YELLOW)
                    .strength(6f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> VENUS_DEEP_COPPER_ORE = registerBlock("venus_deep_copper_ore",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.TERRACOTTA_YELLOW)
                    .strength(6f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> VENUS_DEEP_GOLD_ORE = registerBlock("venus_deep_gold_ore",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.TERRACOTTA_YELLOW)
                    .strength(6f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> VENUS_DEEP_DIAMOND_ORE = registerBlock("venus_deep_diamond_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.TERRACOTTA_YELLOW)
                    .strength(7f, 12f).requiresCorrectToolForDrops(),UniformInt.of(3, 7)));
    public static final RegistryObject<Block> VENUS_DEEP_REDSTONE_ORE = registerBlock("venus_deep_redstone_ore",
            () -> new RedStoneOreBlock(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.TERRACOTTA_YELLOW)
                    .strength(6f, 12f).requiresCorrectToolForDrops().randomTicks().lightLevel(litBlockEmission(9))));
    public static final RegistryObject<Block> VENUS_DEEP_QUARTZ_ORE = registerBlock("venus_deep_quartz_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.TERRACOTTA_YELLOW)
                    .strength(6f, 12f).requiresCorrectToolForDrops(),UniformInt.of(2, 5)));
    public static final RegistryObject<Block> VENUS_DEEP_GLOWSTONE_ORE = registerBlock("venus_deep_glowstone_ore",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.TERRACOTTA_YELLOW)
                    .strength(6f, 12f).requiresCorrectToolForDrops().lightLevel((p_50872_) -> {return 6;})));
    
    //mercury stone stuff
    public static final RegistryObject<Block> MERCURY_STONE = registerBlock("mercury_stone",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.TERRACOTTA_WHITE)
                    .strength(3.5f, 8f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MERCURY_DEEP_STONE = registerBlock("mercury_deep_stone",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
                    .strength(4.5f, 8f).requiresCorrectToolForDrops()));
    //mercury deco blocks
    public static final RegistryObject<Block> MERCURY_STONE_BRICKS = registerBlock("mercury_stone_bricks",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE_BRICKS).mapColor(MapColor.TERRACOTTA_WHITE)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MERCURY_STONE_BRICK_SLAB = registerBlock("mercury_stone_brick_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE_BRICKS).mapColor(MapColor.TERRACOTTA_WHITE)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MERCURY_STONE_BRICK_SLAB_VERTICAL = registerBlock("mercury_stone_brick_slab_vertical",
            () -> new VerticalSlabBlock(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE_BRICKS).mapColor(MapColor.TERRACOTTA_WHITE)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()));
    @SuppressWarnings("deprecation")
	public static final RegistryObject<Block> MERCURY_STONE_BRICK_STAIRS = registerBlock("mercury_stone_brick_stairs",
            () -> new StairBlock(STONE.defaultBlockState() ,BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE_BRICKS).mapColor(MapColor.TERRACOTTA_WHITE)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MERCURY_STONE_BRICK_WALL = registerBlock("mercury_stone_brick_wall",
            () -> new WallBlock(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE_BRICKS).mapColor(MapColor.TERRACOTTA_WHITE)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MERCURY_STONE_PILLAR = registerBlock("mercury_stone_pillar",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE_BRICKS).mapColor(MapColor.TERRACOTTA_WHITE)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> CHISELED_MERCURY_STONE = registerBlock("chiseled_mercury_stone",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE_BRICKS).mapColor(MapColor.TERRACOTTA_WHITE)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> POLISHED_MERCURY_STONE = registerBlock("polished_mercury_stone",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE_BRICKS).mapColor(MapColor.TERRACOTTA_WHITE)
                    .strength(3.5f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MERCURY_STONE_LAMP = registerBlock("mercury_stone_lamp",
    		() -> new Block(BlockBehaviour.Properties.of().lightLevel((p_50872_) -> {return 15;}) .sound(SoundType.GLASS).mapColor(MapColor.COLOR_YELLOW).strength(3f,6.5f) .requiresCorrectToolForDrops())); 

    //mercury ores
    public static final RegistryObject<Block> MERCURY_IRON_ORE = registerBlock("mercury_iron_ore",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.TERRACOTTA_WHITE)
                    .strength(5f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MERCURY_COPPER_ORE = registerBlock("mercury_copper_ore",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.TERRACOTTA_WHITE)
                    .strength(5f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MERCURY_GOLD_ORE = registerBlock("mercury_gold_ore",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.TERRACOTTA_WHITE)
                    .strength(5f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MERCURY_DIAMOND_ORE = registerBlock("mercury_diamond_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.TERRACOTTA_WHITE)
                    .strength(6f, 12f).requiresCorrectToolForDrops(),UniformInt.of(3, 7)));
    public static final RegistryObject<Block> MERCURY_REDSTONE_ORE = registerBlock("mercury_redstone_ore",
            () -> new RedStoneOreBlock(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.TERRACOTTA_WHITE)
                    .strength(5f, 12f).requiresCorrectToolForDrops().randomTicks().lightLevel(litBlockEmission(9))));
    public static final RegistryObject<Block> MERCURY_LAPIS_ORE = registerBlock("mercury_lapis_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.TERRACOTTA_WHITE)
                    .strength(5f, 12f).requiresCorrectToolForDrops(),  UniformInt.of(2, 5)));
    public static final RegistryObject<Block> MERCURY_ZINC_ORE = registerBlock("mercury_zinc_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.TERRACOTTA_WHITE)
                    .strength(5f, 12f).requiresCorrectToolForDrops(),  UniformInt.of(2, 5)));
    public static final RegistryObject<Block> MERCURY_GLOWSTONE_ORE = registerBlock("mercury_glowstone_ore",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.COLOR_YELLOW)
                    .strength(5f, 12f).requiresCorrectToolForDrops().lightLevel((p_50872_) -> {return 15;})));
    public static final RegistryObject<Block> MERCURY_TUNGSTEN_ORE = registerBlock("mercury_tungsten_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.TERRACOTTA_WHITE)
                    .strength(6f, 20f).requiresCorrectToolForDrops(),  UniformInt.of(2, 5)));
    //mercury deep ores
    public static final RegistryObject<Block> MERCURY_DEEP_IRON_ORE = registerBlock("mercury_deep_iron_ore",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
                    .strength(6f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MERCURY_DEEP_COPPER_ORE = registerBlock("mercury_deep_copper_ore",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
                    .strength(6f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MERCURY_DEEP_GOLD_ORE = registerBlock("mercury_deep_gold_ore",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
                    .strength(6f, 12f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MERCURY_DEEP_DIAMOND_ORE = registerBlock("mercury_deep_diamond_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
                    .strength(7f, 12f).requiresCorrectToolForDrops(),UniformInt.of(3, 7)));
    public static final RegistryObject<Block> MERCURY_DEEP_REDSTONE_ORE = registerBlock("mercury_deep_redstone_ore",
            () -> new RedStoneOreBlock(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
                    .strength(6f, 12f).requiresCorrectToolForDrops().randomTicks().lightLevel(litBlockEmission(9))));
    public static final RegistryObject<Block> MERCURY_DEEP_LAPIS_ORE = registerBlock("mercury_deep_lapis_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
                    .strength(6f, 12f).requiresCorrectToolForDrops(),UniformInt.of(2, 5)));
    public static final RegistryObject<Block> MERCURY_DEEP_ZINC_ORE = registerBlock("mercury_deep_zinc_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
                    .strength(6f, 12f).requiresCorrectToolForDrops(),UniformInt.of(2, 5)));
    public static final RegistryObject<Block> MERCURY_DEEP_GLOWSTONE_ORE = registerBlock("mercury_deep_glowstone_ore",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.COLOR_YELLOW)
                    .strength(6f, 12f).requiresCorrectToolForDrops().lightLevel((p_50872_) -> {return 15;})));
    public static final RegistryObject<Block> MERCURY_DEEP_TUNGSTEN_ORE = registerBlock("mercury_deep_tungsten_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE).mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
                    .strength(7f, 20f).requiresCorrectToolForDrops(),UniformInt.of(2, 5)));
    
    public static final RegistryObject<Block> CALORIAN_LOG = registerBlock("calorian_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().sound(SoundType.BASALT).mapColor(MapColor.COLOR_GRAY)
                    .strength(4f, 4f)));
    public static final RegistryObject<Block> CALORIAN_PLANKS = registerBlock("calorian_planks",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.BASALT).mapColor(MapColor.COLOR_GRAY)
                    .strength(4f, 4f)));
    public static final RegistryObject<Block> CALORIAN_SLAB = registerBlock("calorian_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of().sound(SoundType.BASALT).mapColor(MapColor.COLOR_GRAY)
                    .strength(4f, 4f)));
    @SuppressWarnings("deprecation")
	public static final RegistryObject<Block> CALORIAN_STAIRS = registerBlock("calorian_stairs",
            () -> new StairBlock(CALORIAN_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.of().sound(SoundType.BASALT).mapColor(MapColor.COLOR_GRAY)
                    .strength(4f, 4f)));
    
    public static final RegistryObject<Block> MERCURY_SHELF_FUNGUS = registerBlock("mercury_shelf_fungus",
            () -> new MercuryShelfFungusBlock(BlockBehaviour.Properties.of().sound(SoundType.BASALT).mapColor(MapColor.COLOR_PURPLE)
                    .strength(1f, 1f).noCollission().noOcclusion().isSuffocating(NorthstarBlocks::never).isViewBlocking(NorthstarBlocks::never)));
    public static final RegistryObject<Block> MERCURY_SHELF_FUNGUS_BLOCK = registerBlock("mercury_shelf_fungus_block",
            () -> new HugeMushroomBlock(BlockBehaviour.Properties.of().sound(SoundType.BASALT).mapColor(MapColor.COLOR_PURPLE)
            		.strength(4f, 6f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> MERCURY_CACTUS = registerBlock("mercury_cactus",
            () -> new MercuryCactusBlock(BlockBehaviour.Properties.of().sound(SoundType.BASALT).mapColor(MapColor.COLOR_PURPLE)
            		.strength(4f, 6f).requiresCorrectToolForDrops()));
 
    
 //   public static final RegistryObject<Block> TEST = registerBlock("test_sapling",
 //           () -> new SaplingBlock(new TestTreeGrower(), BlockBehaviour.Properties.of().sound(SoundType.GRASS)
 //                   .randomTicks() .instabreak() .noCollission()));
    
 //   public static final RegistryObject<Block> CACTUS_TEST = registerBlock("cactus_test_sapling",
 //          () -> new SaplingBlock(new MercuryCactusGrower(), BlockBehaviour.Properties.of().sound(SoundType.GRASS)
 //                 .randomTicks() .instabreak() .noCollission()));
    
    
    public static final RegistryObject<Block> MONOLITHITE = registerBlock("monolithite",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.DEEPSLATE_BRICKS).mapColor(MapColor.COLOR_BLACK)
                    .strength(100f, 100f).requiresCorrectToolForDrops()));
    
    public static final RegistryObject<MethaneIceBlock> METHANE_ICE = registerBlock("methane_ice",
            () -> new MethaneIceBlock(BlockBehaviour.Properties.of().sound(SoundType.GLASS).friction(0.989F).mapColor(MapColor.COLOR_LIGHT_GREEN)
            		.randomTicks().strength(0.5F).noOcclusion().requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> ICICLE = registerBlock("icicle",
            () -> new IcicleBlock(BlockBehaviour.Properties.of().sound(SoundType.GLASS).mapColor(MapColor.COLOR_LIGHT_BLUE)
                    .strength(3.5f, 12f).noOcclusion().dynamicShape().offsetType(BlockBehaviour.OffsetType.XZ)));

    @SuppressWarnings("removal")
	public static final BlockEntry<TelescopeBlock> TELESCOPE = REGISTRATE.block("telescope", TelescopeBlock::new)
    		.initialProperties(SharedProperties::softMetal)
			.properties(p -> p.noOcclusion().isViewBlocking(NorthstarBlocks::never).strength(8f,8f))
			.properties(p -> p.sound(SoundType.COPPER).mapColor(MapColor.COLOR_BROWN))
    		.transform(pickaxeOnly())
    		.addLayer(() -> RenderType::cutoutMipped)
    		.simpleItem()
    		.register();
    
    public static final RegistryObject<Block> INTERPLANETARY_NAVIGATOR = registerBlock("interplanetary_navigator", 
    		() ->new InterplanetaryNavigatorBlock(BlockBehaviour.Properties.of().sound(SoundType.METAL).mapColor(MapColor.COLOR_GRAY)
                    .strength(8f, 8f).noOcclusion().isViewBlocking(NorthstarBlocks::never)));  
    
    public static final RegistryObject<Block> OXYGEN_BUBBLE_GENERATOR = registerBlock("oxygen_bubble_generator", 
    		() ->new OxygenBubbleGeneratorBlock(BlockBehaviour.Properties.of().sound(SoundType.COPPER).mapColor(MapColor.COLOR_GRAY)
                    .strength(8f, 8f).requiresCorrectToolForDrops()));
    
    
	
	
    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }
    private static ToIntFunction<BlockState> litBlockEmission(int pLightValue) {
        return (p_50763_) -> {
           return p_50763_.getValue(BlockStateProperties.LIT) ? pLightValue : 0;
        };
     }
	
    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
    	return NorthstarItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
}
	
	public static void register(IEventBus eventBus)
	{
		BLOCKS.register(eventBus);
		
	}
}
