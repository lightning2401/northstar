package com.lightning.northstar.world.features.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class CraterConfig implements FeatureConfiguration{
	   public static final Codec<Double> CHANCE_RANGE = Codec.doubleRange(0.0D, 1.0D);
	   public static final Codec<CraterConfig> CODEC = RecordCodecBuilder.create((p_160784_) -> {
		   return p_160784_.group(IntProvider.codec(1, 128).fieldOf("radius").forGetter((p_160802_) -> {
			   return p_160802_.radius;
		   }), IntProvider.codec(1, 128).fieldOf("half_height").forGetter((p_160802_) -> {
			   return p_160802_.half_height;
		   }), IntProvider.codec(1, 128).fieldOf("depth").forGetter((p_160802_) -> {
			   return p_160802_.depth;
		   }), BlockStateProvider.CODEC.fieldOf("block_provider").forGetter((p_161135_) -> {
			   return p_161135_.block_provider;
		   }), BlockStateProvider.CODEC.fieldOf("air_provider").forGetter((p_161136_) -> {
			   return p_161136_.air_provider;
		   }), RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("can_delete").forGetter((p_226234_) -> {
		         return p_226234_.canDelete;
		   })).apply(p_160784_, CraterConfig::new);
	   });
	   public final IntProvider radius;
	   public final IntProvider half_height;
	   public final IntProvider depth;
	   public final HolderSet<Block> canDelete;
	   public final BlockStateProvider block_provider;
	   public final BlockStateProvider air_provider;
	   
	   public CraterConfig(IntProvider radius, IntProvider half_height, IntProvider depth, BlockStateProvider block_provider, BlockStateProvider air_provider, HolderSet<Block> canDelete) {
		   this.half_height = half_height;
		   this.depth = depth;
		   this.block_provider = block_provider;
		   this.air_provider = air_provider;
		   this.radius = radius;
		   this.canDelete = canDelete;
	   }

}
