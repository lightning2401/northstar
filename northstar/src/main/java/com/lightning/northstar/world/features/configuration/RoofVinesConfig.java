package com.lightning.northstar.world.features.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class RoofVinesConfig implements FeatureConfiguration {
	   public static final Codec<RoofVinesConfig> CODEC = RecordCodecBuilder.create((p_67849_) -> {
		   return p_67849_.group(BlockStateProvider.CODEC.fieldOf("block_provider").forGetter((p_161248_) -> {
			   return p_161248_.blockProvider;
		   }), BlockStateProvider.CODEC.fieldOf("glow_provider").forGetter((p_161136_) -> {
			   return p_161136_.glowProvider;
		   }), IntProvider.codec(1, 128).fieldOf("size").forGetter((p_160802_) -> {
		         return p_160802_.size;
		   })).apply(p_67849_, RoofVinesConfig::new);
	   });

	   public final BlockStateProvider blockProvider;
	   public final BlockStateProvider glowProvider;
	   public final IntProvider size;

	   public RoofVinesConfig(BlockStateProvider provider, BlockStateProvider glowprovider, IntProvider maxSize) {
	      this.size = maxSize;
	      this.blockProvider = provider;
	      this.glowProvider = provider;
	   }
}