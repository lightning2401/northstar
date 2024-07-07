package com.lightning.northstar.world.features;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class WormNestFeature extends Feature<StructureFeatureConfig>  {
	
	public WormNestFeature(Codec<StructureFeatureConfig> pCodec) {
		super(pCodec);
	}
	public boolean place(FeaturePlaceContext<StructureFeatureConfig> pContext) {
		RandomSource randomsource = pContext.random();
		WorldGenLevel worldgenlevel = pContext.level();
		BlockPos blockpos = pContext.origin();
		Rotation rotation = Rotation.getRandom(randomsource);
		StructureFeatureConfig config = pContext.config();

		int i = randomsource.nextInt(config.structures.size());
		int nestcount = randomsource.nextIntBetweenInclusive(2,8);
		for(int e = 0; e < nestcount;e++) {
//			System.out.println("e: " + e + "   nestcount: " + nestcount);
			i = randomsource.nextInt(config.structures.size());
			BlockPos newblockpos = scan(Direction.UP, blockpos.offset(randomsource.nextIntBetweenInclusive(-20, 20),0,randomsource.nextIntBetweenInclusive(-20, 20)), worldgenlevel, 60);
			StructureTemplateManager structuretemplatemanager = worldgenlevel.getLevel().getServer().getStructureManager();
			StructureTemplate structuretemplate = structuretemplatemanager.getOrCreate(config.structures.get(i));
			ChunkPos chunkpos = new ChunkPos(newblockpos);
			
			BoundingBox boundingbox = new BoundingBox(chunkpos.getMinBlockX() - 16, worldgenlevel.getMinBuildHeight(), chunkpos.getMinBlockZ() - 16, chunkpos.getMaxBlockX() + 16, worldgenlevel.getMaxBuildHeight(), chunkpos.getMaxBlockZ() + 16);
			StructurePlaceSettings structureplacesettings = (new StructurePlaceSettings()).setRotation(rotation).setBoundingBox(boundingbox).setRandom(randomsource);
			Vec3i vec3i = structuretemplate.getSize(rotation);
			BlockPos blockpos1 = newblockpos.offset(-vec3i.getX() / 2, 0, -vec3i.getZ() / 2);
			BlockPos blockpos2 = structuretemplate.getZeroPositionWithTransform(blockpos1.atY(blockpos1.getY() - config.verticalOffset), Mirror.NONE, rotation);

			if(pContext.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, blockpos2).getY() > blockpos1.getY() + 5) {
				structuretemplate.placeInWorld(worldgenlevel, blockpos2.offset(0,(-structuretemplate.getSize().getY() / 3) + 1,0), blockpos2.offset(0,(-structuretemplate.getSize().getY() / 3) + 1,0), structureplacesettings, randomsource, 4);
			}
//			System.out.println(structuretemplate.placeInWorld(worldgenlevel, blockpos2, blockpos2, structureplacesettings, randomsource, 4));
		}
		
		
		
		StructureTemplateManager structuretemplatemanager = worldgenlevel.getLevel().getServer().getStructureManager();
		StructureTemplate structuretemplate = structuretemplatemanager.getOrCreate(config.structures.get(i));
		ChunkPos chunkpos = new ChunkPos(blockpos);
		
		BoundingBox boundingbox = new BoundingBox(chunkpos.getMinBlockX() - 16, worldgenlevel.getMinBuildHeight(), chunkpos.getMinBlockZ() - 16, chunkpos.getMaxBlockX() + 16, worldgenlevel.getMaxBuildHeight(), chunkpos.getMaxBlockZ() + 16);
		StructurePlaceSettings structureplacesettings = (new StructurePlaceSettings()).setRotation(rotation).setBoundingBox(boundingbox).setRandom(randomsource);
		Vec3i vec3i = structuretemplate.getSize(rotation);
		BlockPos blockpos1 = blockpos.offset(-vec3i.getX() / 2, 0, -vec3i.getZ() / 2);
		BlockPos blockpos2 = structuretemplate.getZeroPositionWithTransform(blockpos1.atY(blockpos1.getY() - config.verticalOffset), Mirror.NONE, rotation);
		
		if(pContext.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, blockpos2).getY() > blockpos1.getY() + 5) {
			structuretemplate.placeInWorld(worldgenlevel, blockpos2, blockpos2, structureplacesettings, randomsource, 4);
		}
//		System.out.println(structuretemplate.placeInWorld(worldgenlevel, blockpos2, blockpos2, structureplacesettings, randomsource, 4));
		return true;
	      
	}
	
	protected BlockPos scan(Direction dir, BlockPos pos, WorldGenLevel level, int scanDist) {
		BlockPos.MutableBlockPos mutable = pos.mutable();
		if(level.getBlockState(pos).isAir() && !level.getBlockState(pos.below()).isAir()) 
		{return pos;}
		for(int i = 0; i < scanDist; i++) {
			mutable.move(Direction.UP);
			if(level.getBlockState(mutable).isAir() && !level.getBlockState(mutable.below()).isAir()) {
				System.out.println("WAAAAAAAAAAGGGGGGGGGHHHHHHH");
				BlockPos newblockpos = new BlockPos(mutable.getX(),mutable.getY(),mutable.getZ());
				return newblockpos;
			}
		}
		mutable = pos.mutable();
		for(int i = 0; i < scanDist; i++) {
			mutable.move(Direction.DOWN);
			if(!level.getBlockState(mutable).isAir() && level.getBlockState(mutable.above()).isAir()) {
				BlockPos newblockpos = new BlockPos(mutable.getX(),mutable.getY(),mutable.getZ());
				return newblockpos;
			}
		}
		
		return pos;
	}
	
}
