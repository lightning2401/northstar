package com.lightning.northstar.mixin;

import java.util.List;
import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.lightning.northstar.world.dimension.NorthstarDimensions;
import com.lightning.northstar.world.dimension.NorthstarPlanets;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {
	
	
    @Inject(method = "tickChunk", at = @At("HEAD"))
    public void tickChunk(LevelChunk pChunk, int pRandomTickSpeed, CallbackInfo info) {
    	ServerLevel level = (ServerLevel)(Object) this;
    	if(level != null) {
	    	if(level.dimension() == NorthstarDimensions.MARS_DIM_KEY) {
	 //   		level.setRainLevel(15);
	    	}
	    	if(level.dimension() == NorthstarDimensions.VENUS_DIM_KEY) {
	  //  		level.setRainLevel(15);
		        ChunkPos chunkpos = pChunk.getPos();
		        boolean flag = level.isRaining();
		        int i = chunkpos.getMinBlockX();
		        int j = chunkpos.getMinBlockZ();
		        ProfilerFiller profilerfiller = level.getProfiler();
		        profilerfiller.push("thunder");
		        if (flag && level.random.nextInt(15000) == 0) {

			           // THUNDER TIME YEEHAW
		           BlockPos blockpos = this.findLightningTargetAround(level.getBlockRandomPos(i, 0, j, 15));
		           LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(level);
		           lightningbolt.moveTo(Vec3.atBottomCenterOf(blockpos));
		           level.addFreshEntity(lightningbolt);
		        }
	    	}
    	}
		   
    }
    protected BlockPos findLightningTargetAround(BlockPos pPos) {
    	ServerLevel level = (ServerLevel)(Object) this;
        BlockPos blockpos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pPos);
        Optional<BlockPos> optional = this.findLightningRod(blockpos);
        if (optional.isPresent()) {
           return optional.get();
        } else {
           AABB aabb = (new AABB(blockpos, new BlockPos(blockpos.getX(), level.getMaxBuildHeight(), blockpos.getZ()))).inflate(3.0D);
           List<LivingEntity> list = level.getEntitiesOfClass(LivingEntity.class, aabb, (p_184067_) -> {
              return p_184067_ != null && p_184067_.isAlive() && level.canSeeSky(p_184067_.blockPosition());
           });
           if (!list.isEmpty()) {
              return list.get(level.random.nextInt(list.size())).blockPosition();
           } else {
              if (blockpos.getY() == level.getMinBuildHeight() - 1) {
                 blockpos = blockpos.above(2);
              }

              return blockpos;
           }
        }
     }
    @SuppressWarnings("resource")
	private Optional<BlockPos> findLightningRod(BlockPos pPos) {
    	ServerLevel level = (ServerLevel)(Object) this;
        Optional<BlockPos> optional = level.getPoiManager().findClosest((p_215059_) -> {
           return p_215059_.is(PoiTypes.LIGHTNING_ROD);
        }, (p_184055_) -> {
           return p_184055_.getY() == level.getHeight(Heightmap.Types.WORLD_SURFACE, p_184055_.getX(), p_184055_.getZ()) - 1;
        }, pPos, 128, PoiManager.Occupancy.ANY);
        return optional.map((p_184053_) -> {
           return p_184053_.above(1);
        });
     }
    
    //yay :]
    @SuppressWarnings("resource")
	@Inject(method = "getSeed", at = @At("HEAD"), cancellable = true)
    public void getSeed(CallbackInfoReturnable<Long> info) {
    	ServerLevel level = (ServerLevel) (Object) this;
    	if(level != null) {
	        long seed = level.getServer().getWorldData().worldGenOptions().seed();
	        info.cancel();
	        info.setReturnValue(seed + NorthstarPlanets.getSeedOffset(level.dimension()));
    	}
    }
}
