package com.lightning.northstar.world.features;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class StructureFeature extends Feature<StructureFeatureConfig>  {
	public StructureFeature(Codec<StructureFeatureConfig> pCodec) {
		super(pCodec);
	}
	public boolean place(FeaturePlaceContext<StructureFeatureConfig> pContext) {
		System.out.println(pContext.origin());
		RandomSource randomsource = pContext.random();
		WorldGenLevel worldgenlevel = pContext.level();
		BlockPos blockpos = pContext.origin();
		Rotation rotation = Rotation.getRandom(randomsource);
		StructureFeatureConfig config = pContext.config();
		
		int i = randomsource.nextInt(config.structures.size());
		
		StructureTemplateManager structuretemplatemanager = worldgenlevel.getLevel().getServer().getStructureManager();
		StructureTemplate structuretemplate = structuretemplatemanager.getOrCreate(config.structures.get(i));
		ChunkPos chunkpos = new ChunkPos(blockpos);
		
		BoundingBox boundingbox = new BoundingBox(chunkpos.getMinBlockX() - 16, worldgenlevel.getMinBuildHeight(), chunkpos.getMinBlockZ() - 16, chunkpos.getMaxBlockX() + 16, worldgenlevel.getMaxBuildHeight(), chunkpos.getMaxBlockZ() + 16);
		StructurePlaceSettings structureplacesettings = (new StructurePlaceSettings()).setRotation(rotation).setBoundingBox(boundingbox).setRandom(randomsource);
		Vec3i vec3i = structuretemplate.getSize(rotation);
		BlockPos blockpos1 = blockpos.offset(-vec3i.getX() / 2, 0, -vec3i.getZ() / 2);
		BlockPos blockpos2 = structuretemplate.getZeroPositionWithTransform(blockpos1.atY(blockpos1.getY() - config.verticalOffset), Mirror.NONE, rotation);
		
		structuretemplate.placeInWorld(worldgenlevel, blockpos2, blockpos2, structureplacesettings, randomsource, 4);
		System.out.println(structuretemplate.placeInWorld(worldgenlevel, blockpos2, blockpos2, structureplacesettings, randomsource, 4));
		return true;
	      
	}

}