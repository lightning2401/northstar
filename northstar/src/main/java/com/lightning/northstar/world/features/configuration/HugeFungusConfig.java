package com.lightning.northstar.world.features.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;

public class HugeFungusConfig implements FeatureConfiguration {
	public static final Codec<HugeFungusConfig> CODEC = RecordCodecBuilder.create((p_225468_) -> {
	      return p_225468_.group(BlockStateProvider.CODEC.fieldOf("trunk_provider").forGetter((p_161248_) -> {
	         return p_161248_.trunkProvider;
	      }), TrunkPlacer.CODEC.fieldOf("trunk_placer").forGetter((p_161246_) -> {
	         return p_161246_.trunkPlacer;
	      })).apply(p_225468_, HugeFungusConfig::new);
	   });
	   //TODO: Review this, see if we can hook in the sapling into the Codec
	   public final BlockStateProvider trunkProvider;
	   public final TrunkPlacer trunkPlacer;

	   protected HugeFungusConfig(BlockStateProvider p_225457_, TrunkPlacer p_225458_) {
	      this.trunkProvider = p_225457_;
	      this.trunkPlacer = p_225458_;
	   }

	   public static class HugeFungusConfigBuilder {
	      public final BlockStateProvider trunkProvider;
	      private final TrunkPlacer trunkPlacer;

	      public HugeFungusConfigBuilder(BlockStateProvider pTrunkProvider,TrunkPlacer pTrunkPlacer) {
	         this.trunkProvider = pTrunkProvider;
	         this.trunkPlacer = pTrunkPlacer;
	      }

	      public HugeFungusConfig build() {
	         return new HugeFungusConfig(this.trunkProvider,this.trunkPlacer);
	      }
	   }
}
