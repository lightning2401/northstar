package com.lightning.northstar.world.features.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class GlowstoneBranchConfig implements FeatureConfiguration{
	public static final Codec<GlowstoneBranchConfig> CODEC = RecordCodecBuilder.create((p_225468_) -> {
	      return p_225468_.group(BlockStateProvider.CODEC.fieldOf("glow_provider").forGetter((p_161248_) -> {
	         return p_161248_.glowProvider;
	      })).apply(p_225468_, GlowstoneBranchConfig::new);
	   });
	
	   public final BlockStateProvider glowProvider;
	
	public GlowstoneBranchConfig(BlockStateProvider glow_provider) {
	this.glowProvider = glow_provider;
	}

     public GlowstoneBranchConfig build() {
        return new GlowstoneBranchConfig(this.glowProvider);
     }

}
