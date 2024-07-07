package com.lightning.northstar.world.features.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class NestConfig implements FeatureConfiguration{
	   public static final Codec<Double> CHANCE_RANGE = Codec.doubleRange(0.0D, 1.0D);
	   public static final Codec<NestConfig> CODEC = RecordCodecBuilder.create((p_160784_) -> {
		   return p_160784_.group(IntProvider.codec(1, 128).fieldOf("radius").forGetter((p_160802_) -> {
			   return p_160802_.radius;
		   }), BlockStateProvider.CODEC.fieldOf("block_provider").forGetter((p_161135_) -> {
			   return p_161135_.block_provider;
		   }), BlockStateProvider.CODEC.fieldOf("nest_provider").forGetter((p_161136_) -> {
			   return p_161136_.nest_provider;
		   }), BlockStateProvider.CODEC.fieldOf("ground_provider").forGetter((p_161136_) -> {
			   return p_161136_.ground_provider;
		   })).apply(p_160784_, NestConfig::new);
	   });
	   public final IntProvider radius;
	   public final BlockStateProvider block_provider;
	   public final BlockStateProvider nest_provider;
	   public final BlockStateProvider ground_provider;
	   
	   public NestConfig(IntProvider radius, BlockStateProvider block_provider, BlockStateProvider nest_provider, BlockStateProvider ground_provider) {
		   this.block_provider = block_provider;
		   this.nest_provider = nest_provider;
		   this.radius = radius;
		   this.ground_provider = ground_provider;
	   }

}
