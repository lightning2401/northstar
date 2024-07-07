package com.lightning.northstar.world.features.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class PointedStoneClusterConfiguration implements FeatureConfiguration {
	   public static final Codec<PointedStoneClusterConfiguration> CODEC = RecordCodecBuilder.create((p_191286_) -> {
		      return p_191286_.group(Codec.floatRange(0.0F, 1.0F).fieldOf("chance_of_taller_dripstone").orElse(0.2F).forGetter((p_191294_) -> {
		         return p_191294_.chanceOfTallerDripstone;
		      }), Codec.floatRange(0.0F, 1.0F).fieldOf("chance_of_directional_spread").orElse(0.7F).forGetter((p_191292_) -> {
		         return p_191292_.chanceOfDirectionalSpread;
		      }), Codec.floatRange(0.0F, 1.0F).fieldOf("chance_of_spread_radius2").orElse(0.5F).forGetter((p_191290_) -> {
		         return p_191290_.chanceOfSpreadRadius2;
		      }), Codec.floatRange(0.0F, 1.0F).fieldOf("chance_of_spread_radius3").orElse(0.5F).forGetter((p_191288_) -> {
		         return p_191288_.chanceOfSpreadRadius3;
		      }), BlockStateProvider.CODEC.fieldOf("stone_provider").forGetter((p_161135_) -> {
			         return p_161135_.stone_provider;
			  }), BlockStateProvider.CODEC.fieldOf("base_stone_provider").forGetter((p_161135_) -> {
			         return p_161135_.base_stone_provider;
			  })).apply(p_191286_, PointedStoneClusterConfiguration::new);
		   });
	   public final float chanceOfTallerDripstone;
	   public final float chanceOfDirectionalSpread;
	   public final float chanceOfSpreadRadius2;
	   public final float chanceOfSpreadRadius3;
	   public final BlockStateProvider stone_provider;
	   public final BlockStateProvider base_stone_provider;
		   
		   
	   public PointedStoneClusterConfiguration(float p_191281_, float p_191282_, float p_191283_, float p_191284_, BlockStateProvider block, BlockStateProvider block2)   {
		   this.chanceOfTallerDripstone = p_191281_;
		   this.chanceOfDirectionalSpread = p_191282_;
		   this.chanceOfSpreadRadius2 = p_191283_;
		   this.chanceOfSpreadRadius3 = p_191284_;
		   this.stone_provider = block;
		   this.base_stone_provider = block2;
	   }

}
