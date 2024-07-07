package com.lightning.northstar.world.features;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class StructureFeatureConfig implements FeatureConfiguration  {
	public static final Codec<StructureFeatureConfig> CODEC = RecordCodecBuilder.create((p_159816_) -> {
		return p_159816_.group(ResourceLocation.CODEC.listOf().fieldOf("structures").forGetter((p_159830_) -> {
			return p_159830_.structures;
		}), Codec.intRange(-100, 100).fieldOf("vertical_offset").forGetter((p_160970_) -> {
	         return p_160970_.verticalOffset;
	    })).apply(p_159816_, StructureFeatureConfig::new);	
	});
	public final List<ResourceLocation> structures;
	public final int verticalOffset;
	
	public StructureFeatureConfig(List<ResourceLocation> structure, int offset) {
		if (structure.isEmpty()) {
			throw new IllegalArgumentException("Structure lists need at least one entry");
		} else {
			this.structures = structure;
			this.verticalOffset = offset;
		}
	}
}
