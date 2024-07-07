package com.lightning.northstar.world.features;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.FossilFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class GiantSkeletonFeature extends Feature<FossilFeatureConfiguration>  {
	public GiantSkeletonFeature(Codec<FossilFeatureConfiguration> pCodec) {
		super(pCodec);
	}
	public boolean place(FeaturePlaceContext<FossilFeatureConfiguration> pContext) {
	      RandomSource randomsource = pContext.random();
	      WorldGenLevel worldgenlevel = pContext.level();
	      BlockPos blockpos = pContext.origin();
	      Rotation rotation = Rotation.getRandom(randomsource);
	      FossilFeatureConfiguration fossilfeatureconfiguration = pContext.config();
	      int i = randomsource.nextInt(fossilfeatureconfiguration.fossilStructures.size());
	      StructureTemplateManager structuretemplatemanager = worldgenlevel.getLevel().getServer().getStructureManager();
	      StructureTemplate structuretemplate = structuretemplatemanager.getOrCreate(fossilfeatureconfiguration.fossilStructures.get(i));
	      StructureTemplate structuretemplate1 = structuretemplatemanager.getOrCreate(fossilfeatureconfiguration.overlayStructures.get(i));
	      ChunkPos chunkpos = new ChunkPos(blockpos);
	      BoundingBox boundingbox = new BoundingBox(chunkpos.getMinBlockX() - 16, worldgenlevel.getMinBuildHeight(), chunkpos.getMinBlockZ() - 16, chunkpos.getMaxBlockX() + 16, worldgenlevel.getMaxBuildHeight(), chunkpos.getMaxBlockZ() + 16);
	      StructurePlaceSettings structureplacesettings = (new StructurePlaceSettings()).setRotation(rotation).setBoundingBox(boundingbox).setRandom(randomsource);
	      BlockPos blockpos2 = structuretemplate.getZeroPositionWithTransform(blockpos.atY(blockpos.getY() - 2), Mirror.NONE, rotation);
	         structureplacesettings.clearProcessors();
	         fossilfeatureconfiguration.fossilProcessors.value().list().forEach(structureplacesettings::addProcessor);
	         structuretemplate.placeInWorld(worldgenlevel, blockpos2, blockpos2, structureplacesettings, randomsource, 4);
	         structureplacesettings.clearProcessors();
	         fossilfeatureconfiguration.overlayProcessors.value().list().forEach(structureplacesettings::addProcessor);
	         structuretemplate1.placeInWorld(worldgenlevel, blockpos2, blockpos2, structureplacesettings, randomsource, 4);
	         return true;
	      
	   }

}
