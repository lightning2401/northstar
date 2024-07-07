package com.lightning.northstar.world.features.configuration;

import java.util.List;
import java.util.Optional;

import com.google.common.collect.ImmutableList;
import com.lightning.northstar.world.features.trunkplacers.ArgyreTrunkPlacer;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.FeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;

public class AlienTreeConfig implements FeatureConfiguration {
	public static final Codec<AlienTreeConfig> CODEC = RecordCodecBuilder.create((p_225468_) -> {
	      return p_225468_.group(BlockStateProvider.CODEC.fieldOf("trunk_provider").forGetter((p_161248_) -> {
	         return p_161248_.trunkProvider;
	      }), BlockStateProvider.CODEC.fieldOf("glow_provider").forGetter((p_161244_) -> {
	    	  return p_161244_.glowProvider;
	      }), ArgyreTrunkPlacer.CODEC.fieldOf("trunk_placer").forGetter((p_161246_) -> {
	         return p_161246_.trunkPlacer;
	      }), BlockStateProvider.CODEC.fieldOf("foliage_provider").forGetter((p_161244_) -> {
	         return p_161244_.foliageProvider;
	      }), FoliagePlacer.CODEC.fieldOf("foliage_placer").forGetter((p_191357_) -> {
	         return p_191357_.foliagePlacer;
	      }), RootPlacer.CODEC.optionalFieldOf("root_placer").forGetter((p_225478_) -> {
	         return p_225478_.rootPlacer;
	      }), BlockStateProvider.CODEC.fieldOf("dirt_provider").forGetter((p_225476_) -> {
	         return p_225476_.dirtProvider;
	      }), FeatureSize.CODEC.fieldOf("minimum_size").forGetter((p_225474_) -> {
	         return p_225474_.minimumSize;
	      }), TreeDecorator.CODEC.listOf().fieldOf("decorators").forGetter((p_225472_) -> {
	         return p_225472_.decorators;
	      }), Codec.BOOL.fieldOf("ignore_vines").orElse(false).forGetter((p_161232_) -> {
	         return p_161232_.ignoreVines;
	      }), Codec.BOOL.fieldOf("force_dirt").orElse(false).forGetter((p_225470_) -> {
	         return p_225470_.forceDirt;
	      })).apply(p_225468_, AlienTreeConfig::new);
	   });
	   //TODO: Review this, see if we can hook in the sapling into the Codec
	   public final BlockStateProvider trunkProvider;
	   public final BlockStateProvider glowProvider;
	   public final BlockStateProvider dirtProvider;
	   public final ArgyreTrunkPlacer trunkPlacer;
	   public final BlockStateProvider foliageProvider;
	   public final FoliagePlacer foliagePlacer;
	   public final Optional<RootPlacer> rootPlacer;
	   public final FeatureSize minimumSize;
	   public final List<TreeDecorator> decorators;
	   public final boolean ignoreVines;
	   public final boolean forceDirt;

	   protected AlienTreeConfig(BlockStateProvider p_225457_, BlockStateProvider glow, ArgyreTrunkPlacer trunkPlacer2, BlockStateProvider p_225459_, FoliagePlacer p_225460_, Optional<RootPlacer> p_225461_, BlockStateProvider p_225462_, FeatureSize p_225463_, List<TreeDecorator> p_225464_, boolean p_225465_, boolean p_225466_) {
	      this.trunkProvider = p_225457_;
	      this.glowProvider = glow;
	      this.trunkPlacer = trunkPlacer2;
	      this.foliageProvider = p_225459_;
	      this.foliagePlacer = p_225460_;
	      this.rootPlacer = p_225461_;
	      this.dirtProvider = p_225462_;
	      this.minimumSize = p_225463_;
	      this.decorators = p_225464_;
	      this.ignoreVines = p_225465_;
	      this.forceDirt = p_225466_;
	   }

	   public static class AlienTreeConfigBuilder {
	      public final BlockStateProvider trunkProvider;
	      public final BlockStateProvider glowProvider;
	      private final ArgyreTrunkPlacer trunkPlacer;
	      public final BlockStateProvider foliageProvider;
	      private final FoliagePlacer foliagePlacer;
	      private final Optional<RootPlacer> rootPlacer;
	      private BlockStateProvider dirtProvider;
	      private final FeatureSize minimumSize;
	      private List<TreeDecorator> decorators = ImmutableList.of();
	      private boolean ignoreVines;
	      private boolean forceDirt;

	      public AlienTreeConfigBuilder(BlockStateProvider pTrunkProvider, BlockStateProvider glow, ArgyreTrunkPlacer pTrunkPlacer, BlockStateProvider pFoliageProvider, FoliagePlacer pFoliagePlacer, Optional<RootPlacer> pRootPlacer, FeatureSize pMinimumSize) {
	         this.trunkProvider = pTrunkProvider;
	         this.glowProvider = glow;
	         this.trunkPlacer = pTrunkPlacer;
	         this.foliageProvider = pFoliageProvider;
	         this.dirtProvider = BlockStateProvider.simple(Blocks.DIRT);
	         this.foliagePlacer = pFoliagePlacer;
	         this.rootPlacer = pRootPlacer;
	         this.minimumSize = pMinimumSize;
	      }

	      public AlienTreeConfigBuilder(BlockStateProvider pTrunkProvider, BlockStateProvider glow, ArgyreTrunkPlacer pTrunkPlacer, BlockStateProvider pFoliageProvider, FoliagePlacer pFoliagePlacer, FeatureSize pMinimumSize) {
	         this(pTrunkProvider, glow, pTrunkPlacer, pFoliageProvider, pFoliagePlacer, Optional.empty(), pMinimumSize);
	      }

	      public AlienTreeConfig.AlienTreeConfigBuilder dirt(BlockStateProvider pDirtProvider) {
	         this.dirtProvider = pDirtProvider;
	         return this;
	      }

	      public AlienTreeConfig.AlienTreeConfigBuilder decorators(List<TreeDecorator> pDecorators) {
	         this.decorators = pDecorators;
	         return this;
	      }

	      public AlienTreeConfig.AlienTreeConfigBuilder ignoreVines() {
	         this.ignoreVines = true;
	         return this;
	      }

	      public AlienTreeConfig.AlienTreeConfigBuilder forceDirt() {
	         this.forceDirt = true;
	         return this;
	      }

	      public AlienTreeConfig build() {
	         return new AlienTreeConfig(this.trunkProvider, this.glowProvider, this.trunkPlacer, this.foliageProvider, this.foliagePlacer, this.rootPlacer, this.dirtProvider, this.minimumSize, this.decorators, this.ignoreVines, this.forceDirt);
	      }
	   }
}
