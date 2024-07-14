package com.lightning.northstar.world.features;

import com.lightning.northstar.Northstar;
import com.lightning.northstar.world.features.configuration.AlienTreeConfig;
import com.lightning.northstar.world.features.configuration.BlockPileConfig;
import com.lightning.northstar.world.features.configuration.CraterConfig;
import com.lightning.northstar.world.features.configuration.GlowstoneBranchConfig;
import com.lightning.northstar.world.features.configuration.PointedStoneClusterConfiguration;
import com.lightning.northstar.world.features.configuration.RoofVinesConfig;
import com.lightning.northstar.world.features.configuration.SphereConfig;
import com.lightning.northstar.world.features.configuration.StoneClusterConfiguration;
import com.lightning.northstar.world.features.configuration.StoneColumnConfiguration;

import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FossilFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.MultifaceGrowthConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public abstract class NorthstarFeatures<FC extends FeatureConfiguration> {
		public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(Registry.FEATURE_REGISTRY, Northstar.MOD_ID);
	
//	    public static final Feature<MarsRootsConfig> MARS_ROOTS = register("mars_roots", new MarsRoots(MarsRootsConfig.CODEC));
		public static final RegistryObject<Feature<?>> NATURAL_ARGYRE = FEATURES.register("natural_argyre", () -> new ArgyreFeature(AlienTreeConfig.CODEC));
		public static final RegistryObject<Feature<?>> STONE_CLUSTER = FEATURES.register("stone_cluster", () -> new StoneClusterFeature(StoneClusterConfiguration.CODEC));
		public static final RegistryObject<Feature<?>> STONE_COLUMN = FEATURES.register("stone_column", () -> new StoneColumnFeature(StoneColumnConfiguration.CODEC));
		public static final RegistryObject<Feature<?>> POINTED_STONE_CLUSTER = FEATURES.register("pointed_stone_cluster", () -> new PointedStoneClusterFeature(PointedStoneClusterConfiguration.CODEC));
		public static final RegistryObject<Feature<?>> SPHERE = FEATURES.register("sphere", () -> new SphereFeature(SphereConfig.CODEC));
		public static final RegistryObject<Feature<?>> CRATER = FEATURES.register("crater", () -> new CraterFeature(CraterConfig.CODEC));
		public static final RegistryObject<Feature<?>> GLOWSTONE_BRANCH = FEATURES.register("glowstone_branch", () -> new GlowstoneBranchFeature(GlowstoneBranchConfig.CODEC));
		public static final RegistryObject<Feature<?>> GLOWSTONE_UPSIDE_DOWN_BRANCH = FEATURES.register("glowstone_upside_down_branch", () -> new GlowstoneUpsideDownBranchFeature(GlowstoneBranchConfig.CODEC));
		public static final RegistryObject<Feature<?>> GLOWING_ORE = FEATURES.register("glowing_ore", () -> new GlowingOreFeature(OreConfiguration.CODEC));
		public static final RegistryObject<Feature<?>> GIANT_SKELETON = FEATURES.register("giant_skeleton", () -> new GiantSkeletonFeature(FossilFeatureConfiguration.CODEC));
		public static final RegistryObject<Feature<?>> STRUCTURE_FEATURE = FEATURES.register("structure_feature", () -> new StructureFeature(StructureFeatureConfig.CODEC));
		public static final RegistryObject<Feature<?>> BLOCK_PILE = FEATURES.register("block_pile", () -> new BlockPileFeature(BlockPileConfig.CODEC));
		public static final RegistryObject<Feature<?>> SMALL_ROCK = FEATURES.register("small_rock", () -> new SmallRockFeature(BlockStateConfiguration.CODEC));
		public static final RegistryObject<Feature<?>> WORM_NEST = FEATURES.register("worm_nest", () -> new WormNestFeature(StructureFeatureConfig.CODEC));
		public static final RegistryObject<Feature<?>> MARS_ROOTS = FEATURES.register("mars_roots", () -> new MarsRootsFeature(MultifaceGrowthConfiguration.CODEC));
		public static final RegistryObject<Feature<?>> MULTIFACE_GROWTH_CUSTOM = FEATURES.register("multiface_growth_custom", () -> new MultifaceGrowthCustomFeature(MultifaceGrowthConfiguration.CODEC));
		public static final RegistryObject<Feature<?>> ROOF_VINES = FEATURES.register("roof_vines", () -> new RoofVinesFeature(RoofVinesConfig.CODEC));
		public static final RegistryObject<Feature<?>> MERCURY_CACTUS = FEATURES.register("mercury_cactus", () -> new MercuryCactusFeature(NoneFeatureConfiguration.CODEC));
		
	    public static void register(IEventBus eventBus) {
	        FEATURES.register(eventBus);
	    }
}
