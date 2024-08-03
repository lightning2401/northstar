package com.lightning.northstar.world.features;

import java.util.Optional;

import com.lightning.northstar.Northstar;
import com.lightning.northstar.block.NorthstarBlocks;
import com.lightning.northstar.world.features.configuration.AlienTreeConfig;
import com.lightning.northstar.world.features.configuration.GlowstoneBranchConfig;
import com.lightning.northstar.world.features.configuration.StoneColumnConfiguration;
import com.lightning.northstar.world.features.foliageplacers.CoilerFoliagePlacer;
import com.lightning.northstar.world.features.trunkplacers.ArgyreCeilingTrunkPlacer;
import com.lightning.northstar.world.features.trunkplacers.ArgyreSaplingTrunkPlacer;
import com.lightning.northstar.world.features.trunkplacers.ArgyreTrunkPlacer;
import com.lightning.northstar.world.features.trunkplacers.BloomTrunkPlacer;
import com.lightning.northstar.world.features.trunkplacers.CoilerTrunkPlacer;
import com.lightning.northstar.world.features.trunkplacers.PlateTrunkPlacer;
import com.lightning.northstar.world.features.trunkplacers.RoofBloomTrunkPlacer;
import com.lightning.northstar.world.features.trunkplacers.RoofPlateTrunkPlacer;
import com.lightning.northstar.world.features.trunkplacers.RoofTowerTrunkPlacer;
import com.lightning.northstar.world.features.trunkplacers.SpikeTrunkPlacer;
import com.lightning.northstar.world.features.trunkplacers.TestSaplingTrunkPlacer;
import com.lightning.northstar.world.features.trunkplacers.TowerTrunkPlacer;
import com.lightning.northstar.world.features.trunkplacers.WilterTrunkPlacer;
import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.AcaciaFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.rootplacers.AboveRootPlacement;
import net.minecraft.world.level.levelgen.feature.rootplacers.MangroveRootPlacement;
import net.minecraft.world.level.levelgen.feature.rootplacers.MangroveRootPlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class NorthstarConfiguredFeatures {
    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES =
            DeferredRegister.create(Registries.CONFIGURED_FEATURE, Northstar.MOD_ID);
	

	public static final ResourceKey<ConfiguredFeature<?, ?>> COILER = registerCF("coiler");
	public static final ResourceKey<ConfiguredFeature<?, ?>> WILTER = registerCF("wilter");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ARGYRE = registerCF("argyre");
	public static final ResourceKey<ConfiguredFeature<?, ?>> SPIKE_FUNGUS = registerCF("spike_fungus");
	public static final ResourceKey<ConfiguredFeature<?, ?>> BLOOM_FUNGUS = registerCF("bloom_fungus");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ROOF_BLOOM_FUNGUS = registerCF("roof_bloom_fungus");
	public static final ResourceKey<ConfiguredFeature<?, ?>> PLATE_FUNGUS = registerCF("plate_fungus");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ROOF_PLATE_FUNGUS = registerCF("roof_plate_fungus");
	public static final ResourceKey<ConfiguredFeature<?, ?>> TOWER_FUNGUS = registerCF("tower_fungus");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ROOF_TOWER_FUNGUS = registerCF("roof_tower_fungus");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ARGYRE_CEILING = registerCF("argyre_ceiling");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ARGYRE_SMALL = registerCF("argyre_small");
	public static final ResourceKey<ConfiguredFeature<?, ?>> CALORIAN_VINES = registerCF("calorian_vines");
	public static final ResourceKey<ConfiguredFeature<?, ?>> MERCURY_CACTUS = registerCF("mercury_cactus");
	public static final ResourceKey<ConfiguredFeature<?, ?>> CRIMSITE_COLUMN = registerCF("crimsite_column");
	public static final ResourceKey<ConfiguredFeature<?, ?>> GLOWSTONE_BRANCH = registerCF("glowstone_branch");
	public static final ResourceKey<ConfiguredFeature<?, ?>> GLOWSTONE_UPSIDE_DOWN_BRANCH = registerCF("glowstone_upside_down_branch");

	public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
		
		HolderGetter<Block> blocks = context.lookup(Registries.BLOCK);
		
		context.register(COILER, new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder
        		(BlockStateProvider.simple(NorthstarBlocks.COILER_LOG.get()),
        		new CoilerTrunkPlacer(6, 1, 9, UniformInt.of(1, 4), 0.5F, UniformInt.of(0, 1),
        				blocks.getOrThrow(BlockTags.MANGROVE_LOGS_CAN_GROW_THROUGH), UniformInt.of(-1, 1)),
                BlockStateProvider.simple(NorthstarBlocks.COILER_LEAVES.get()),
                new CoilerFoliagePlacer(ConstantInt.of(4), ConstantInt.of(0)), Optional.of
        		(new MangroveRootPlacer(UniformInt.of(2,4), BlockStateProvider.simple(NorthstarBlocks.COILER_LOG.get()), Optional.of(new AboveRootPlacement(BlockStateProvider.simple(Blocks.AIR), 0.5F)),
        		new MangroveRootPlacement(blocks.getOrThrow(BlockTags.MANGROVE_ROOTS_CAN_GROW_THROUGH), HolderSet.direct(Block::builtInRegistryHolder, Blocks.MUD, Blocks.MUDDY_MANGROVE_ROOTS),
        		BlockStateProvider.simple(NorthstarBlocks.COILER_LOG.get()), 8, 15, 0.2F))),
                new TwoLayersFeatureSize(3, 0, 2)).build()));
		
		context.register(WILTER, new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder
	            		(BlockStateProvider.simple(NorthstarBlocks.WILTER_LOG.get()),
	            		new WilterTrunkPlacer(9, 1, 5, UniformInt.of(4, 4), 0.5F, UniformInt.of(0, 1),
	            			blocks.getOrThrow(BlockTags.MANGROVE_LOGS_CAN_GROW_THROUGH), UniformInt.of(-1, 1)),
	                    BlockStateProvider.simple(NorthstarBlocks.WILTER_LEAVES.get()),
	                    new AcaciaFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0)),
	                    new TwoLayersFeatureSize(2, 0, 2)).build()));
		
		context.register(ARGYRE, new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder
	            		(BlockStateProvider.simple(NorthstarBlocks.ARGYRE_LOG.get()),
	            		new ArgyreTrunkPlacer(32, 24, 20, UniformInt.of(4, 4), 0.5F, UniformInt.of(0, 1),
	            				blocks.getOrThrow(BlockTags.MANGROVE_LOGS_CAN_GROW_THROUGH), UniformInt.of(1, 1)),
	                    BlockStateProvider.simple(NorthstarBlocks.ARGYRE_LEAVES.get()),
	                    new AcaciaFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0)),
	                    new TwoLayersFeatureSize(2, 0, 2)).build()));	
		context.register(SPIKE_FUNGUS, new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder
	            		(BlockStateProvider.simple(NorthstarBlocks.SPIKE_FUNGUS_BLOCK.get()),
	            		new SpikeTrunkPlacer(8, 1, 4, UniformInt.of(4, 4), blocks.getOrThrow(BlockTags.MANGROVE_LOGS_CAN_GROW_THROUGH)),
	                    BlockStateProvider.simple(NorthstarBlocks.WILTER_LEAVES.get()),
	                    new AcaciaFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0)),
	                    new TwoLayersFeatureSize(2, 0, 2)).build()));	
		context.register(BLOOM_FUNGUS, new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder
	            		(BlockStateProvider.simple(NorthstarBlocks.BLOOM_FUNGUS_STEM_BLOCK.get()),
	            		new BloomTrunkPlacer(3, 1, 2, UniformInt.of(4, 4), blocks.getOrThrow(BlockTags.MANGROVE_LOGS_CAN_GROW_THROUGH), BlockStateProvider.simple(NorthstarBlocks.BLOOM_FUNGUS_BLOCK.get())),
	                    BlockStateProvider.simple(NorthstarBlocks.WILTER_LEAVES.get()),
	                    new AcaciaFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0)),
	                    new TwoLayersFeatureSize(2, 0, 2)).build()));	
		context.register(ROOF_BLOOM_FUNGUS, new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder
	            		(BlockStateProvider.simple(NorthstarBlocks.BLOOM_FUNGUS_STEM_BLOCK.get()),
	            		new RoofBloomTrunkPlacer(3, 1, 2, UniformInt.of(4, 4), blocks.getOrThrow(BlockTags.MANGROVE_LOGS_CAN_GROW_THROUGH), BlockStateProvider.simple(NorthstarBlocks.BLOOM_FUNGUS_BLOCK.get())),
	                    BlockStateProvider.simple(NorthstarBlocks.WILTER_LEAVES.get()),
	                    new AcaciaFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0)),
	                    new TwoLayersFeatureSize(2, 0, 2)).build()));	
	   context.register(PLATE_FUNGUS, new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder
	            		(BlockStateProvider.simple(NorthstarBlocks.PLATE_FUNGUS_STEM_BLOCK.get()),
	            		new PlateTrunkPlacer(3, 1, 2, UniformInt.of(4, 4), blocks.getOrThrow(BlockTags.MANGROVE_LOGS_CAN_GROW_THROUGH), BlockStateProvider.simple(NorthstarBlocks.PLATE_FUNGUS_CAP_BLOCK.get())),
	                    BlockStateProvider.simple(NorthstarBlocks.WILTER_LEAVES.get()),
	                    new AcaciaFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0)),
	                    new TwoLayersFeatureSize(2, 0, 2)).build()));	
		context.register(ROOF_PLATE_FUNGUS, new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder
	            		(BlockStateProvider.simple(NorthstarBlocks.PLATE_FUNGUS_STEM_BLOCK.get()),
	            		new RoofPlateTrunkPlacer(3, 1, 2, UniformInt.of(4, 4), blocks.getOrThrow(BlockTags.MANGROVE_LOGS_CAN_GROW_THROUGH), BlockStateProvider.simple(NorthstarBlocks.PLATE_FUNGUS_CAP_BLOCK.get())),
	                    BlockStateProvider.simple(NorthstarBlocks.WILTER_LEAVES.get()),
	                    new AcaciaFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0)),
	                    new TwoLayersFeatureSize(2, 0, 2)).build()));	
		context.register(TOWER_FUNGUS, new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder
	            		(BlockStateProvider.simple(NorthstarBlocks.TOWER_FUNGUS_STEM_BLOCK.get()),
	            		new TowerTrunkPlacer(8, 1, 4, UniformInt.of(4, 4), blocks.getOrThrow(BlockTags.MANGROVE_LOGS_CAN_GROW_THROUGH), BlockStateProvider.simple(NorthstarBlocks.TOWER_FUNGUS_CAP_BLOCK.get())),
	                    BlockStateProvider.simple(NorthstarBlocks.WILTER_LEAVES.get()),
	                    new AcaciaFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0)),
	                    new TwoLayersFeatureSize(2, 0, 2)).build()));	
		context.register(ROOF_TOWER_FUNGUS, new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder
	            		(BlockStateProvider.simple(NorthstarBlocks.TOWER_FUNGUS_STEM_BLOCK.get()),
	            		new RoofTowerTrunkPlacer(8, 1, 4, UniformInt.of(4, 4), blocks.getOrThrow(BlockTags.MANGROVE_LOGS_CAN_GROW_THROUGH), BlockStateProvider.simple(NorthstarBlocks.TOWER_FUNGUS_CAP_BLOCK.get())),
	                    BlockStateProvider.simple(NorthstarBlocks.WILTER_LEAVES.get()),
	                    new AcaciaFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0)),
	                    new TwoLayersFeatureSize(2, 0, 2)).build()));	
		context.register(ARGYRE_CEILING, new ConfiguredFeature<>((Feature<AlienTreeConfig>) NorthstarFeatures.NATURAL_ARGYRE.get(), new AlienTreeConfig.AlienTreeConfigBuilder
	            		(BlockStateProvider.simple(NorthstarBlocks.ARGYRE_LOG.get()),
	            		BlockStateProvider.simple(Blocks.GLOWSTONE), new ArgyreCeilingTrunkPlacer(32, 24, 20, UniformInt.of(4, 4), 0.5F, UniformInt.of(0, 1),
	            				blocks.getOrThrow(BlockTags.MANGROVE_LOGS_CAN_GROW_THROUGH), UniformInt.of(1, 1)),
	                    BlockStateProvider.simple(NorthstarBlocks.ARGYRE_LEAVES.get()),
	                    new AcaciaFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0)),
	                    new TwoLayersFeatureSize(2, 0, 2)).build()));
		context.register(ARGYRE_SMALL, new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder
	            		(BlockStateProvider.simple(NorthstarBlocks.ARGYRE_LOG.get()),
	            		new ArgyreSaplingTrunkPlacer(12, 1, 10, UniformInt.of(4, 4), 0.5F, UniformInt.of(0, 1),
	            				blocks.getOrThrow(BlockTags.MANGROVE_LOGS_CAN_GROW_THROUGH), UniformInt.of(1, 1)),
	                    BlockStateProvider.simple(NorthstarBlocks.ARGYRE_LEAVES.get()),
	                    new CoilerFoliagePlacer(ConstantInt.of(4), ConstantInt.of(0)),
	                    new TwoLayersFeatureSize(3, 0, 2)).build()));	
		context.register(CALORIAN_VINES, new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder
	            		(BlockStateProvider.simple(NorthstarBlocks.CALORIAN_LOG.get()),
	            		new TestSaplingTrunkPlacer(90, 2, 12, UniformInt.of(4, 4), 0.5F, UniformInt.of(0, 1),
	            				blocks.getOrThrow(BlockTags.MANGROVE_LOGS_CAN_GROW_THROUGH), UniformInt.of(-1, 1)),
	                    BlockStateProvider.simple(NorthstarBlocks.WILTER_LEAVES.get()),
	                    new AcaciaFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0)),
	                    new TwoLayersFeatureSize(2, 0, 2)).build()));
		
		context.register(MERCURY_CACTUS, new ConfiguredFeature<>((Feature<NoneFeatureConfiguration>)NorthstarFeatures.MERCURY_CACTUS.get(), new NoneFeatureConfiguration()));
	            
		context.register(CRIMSITE_COLUMN, new ConfiguredFeature<>((Feature<StoneColumnConfiguration>)NorthstarFeatures.STONE_COLUMN.get(), 
	            new StoneColumnConfiguration(36, UniformInt.of(3, 6), UniformFloat.of(0.4F, 2.0F), 0.45F, UniformFloat.of(0.3F, 0.9F),
	            UniformFloat.of(0.4F, 1.0F), UniformFloat.of(0.0F, 0.3F), 4, 0.6F, BlockStateProvider.simple(AllPaletteStoneTypes.CRIMSITE.baseBlock.get().defaultBlockState()))));
		context.register(GLOWSTONE_BRANCH, new ConfiguredFeature<>((Feature<GlowstoneBranchConfig>)NorthstarFeatures.GLOWSTONE_BRANCH.get(), 
	            new GlowstoneBranchConfig(BlockStateProvider.simple(Blocks.GLOWSTONE.defaultBlockState()))));
		context.register(GLOWSTONE_UPSIDE_DOWN_BRANCH, new ConfiguredFeature<>((Feature<GlowstoneBranchConfig>)NorthstarFeatures.GLOWSTONE_UPSIDE_DOWN_BRANCH.get(), 
	            new GlowstoneBranchConfig(BlockStateProvider.simple(Blocks.GLOWSTONE.defaultBlockState()))));
		
	}
	
	
	public static ResourceKey<ConfiguredFeature<?, ?>> registerCF(String name) {
		return ResourceKey.create(Registries.CONFIGURED_FEATURE, Northstar.asResource(name));
	}
    public static void register(IEventBus eventBus) {
        CONFIGURED_FEATURES.register(eventBus);

    }
}