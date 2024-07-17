package com.lightning.northstar.mixin;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.lightning.northstar.NorthstarTags.NorthstarBlockTags;
import com.lightning.northstar.NorthstarTags.NorthstarEntityTags;
import com.lightning.northstar.advancements.NorthstarAdvancements;
import com.lightning.northstar.block.NorthstarBlocks;
import com.lightning.northstar.fluids.NorthstarFluids;
import com.lightning.northstar.world.OxygenStuff;
import com.lightning.northstar.world.TemperatureStuff;
import com.lightning.northstar.world.dimension.NorthstarDimensions;
import com.lightning.northstar.world.dimension.NorthstarPlanets;

import it.unimi.dsi.fastutil.objects.Object2DoubleArrayMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;

@Mixin(LivingEntity.class)
public class OxygenAndTempEntityMixin {
	int oxyHurtBuffer = 70;
	int tempHurtBuffer = 70;
	protected Object2DoubleMap<net.minecraftforge.fluids.FluidType> forgeFluidTypeHeight = new Object2DoubleArrayMap<>(net.minecraftforge.fluids.FluidType.SIZE.get());

    @Inject(method = "tick", at = @At("TAIL"))
    public void northstar$tick(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if(entity instanceof ZombifiedPiglin)
        	return;
        
        if(entity.level.isClientSide)
        	return;
        ResourceKey<Level> dim = entity.level.dimension();
        boolean posHasAir = OxygenStuff.checkForAir(entity);
        if(entity.canBreatheUnderwater()) {posHasAir = true;}
        boolean oxygenated = OxygenStuff.oxygenatedEntities.contains(entity);
        int temp = checkTemp(entity);
        boolean hasInsulation = TemperatureStuff.hasInsulation(entity);
        boolean hasHeatProtection = TemperatureStuff.hasHeatProtection(entity);
        boolean creativeCheck = false;
        if(entity instanceof ServerPlayer svp) {
        	creativeCheck = svp.isCreative();
        }
        
        if(posHasAir)
        {oxyHurtBuffer = 40;}
        if(temp > -32 && temp < 300)
        {tempHurtBuffer = 40;}
        
        if(oxyHurtBuffer > 0 ){oxyHurtBuffer--;}
        if(tempHurtBuffer > 0 ){tempHurtBuffer--;}
        
        if (!NorthstarPlanets.getPlanetOxy(dim) && !posHasAir && !oxygenated && !NorthstarEntityTags.DOESNT_REQUIRE_OXYGEN.matches(entity) && oxyHurtBuffer <= 0 && !creativeCheck)
        {entity.hurt(DamageSource.DROWN, 2f);}
        if (temp < -32 && !entity.isSpectator() && !hasInsulation && tempHurtBuffer <= 0 && !creativeCheck && !NorthstarEntityTags.CAN_SURVIVE_COLD.matches(entity))
        {boolean flag = entity.getType().is(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES);
        int j = flag ? 7 : 2;
        entity.hurt(DamageSource.FREEZE, (float)j);
        entity.setTicksFrozen(entity.getTicksRequiredToFreeze());}
        if (temp > 300 && !entity.isOnFire() && !entity.fireImmune() && !hasHeatProtection && tempHurtBuffer <= 0)
        {entity.setSecondsOnFire(5);}
        if(entity.level instanceof ServerLevel) {
        	if(dim == NorthstarDimensions.EARTH_ORBIT_DIM_KEY && entity.getY() < -10) {
                ServerLevel newLevel = entity.level.getServer().getLevel(Level.END);
//                if(entity.level instanceof ServerLevel)
//                entity = (LivingEntity) changeDimensionCustom(newLevel);
        	}else if (entity instanceof Player plyer) {
        		if (dim == NorthstarDimensions.MOON_DIM_KEY && !NorthstarAdvancements.ONE_SMALL_STEP.isAlreadyAwardedTo(plyer)) {
        			if(plyer.level.getBlockState(plyer.blockPosition().below()).is(NorthstarBlockTags.MOON_BLOCKS.tag)) {
        				NorthstarAdvancements.ONE_SMALL_STEP.awardTo(plyer);
        			}
            		
            	} else if (dim == NorthstarDimensions.MARS_DIM_KEY && !NorthstarAdvancements.ONE_GIANT_LEAP.isAlreadyAwardedTo(plyer)) {
        			if(plyer.level.getBlockState(plyer.blockPosition().below()).is(NorthstarBlockTags.MARS_BLOCKS.tag)) {
        				NorthstarAdvancements.ONE_GIANT_LEAP.awardTo(plyer);
        			}
            	}
        	}
        }
        if(getFluidAtPos(entity, entity.level) == NorthstarFluids.SULFURIC_ACID.get() || getFluidAtPos(entity, entity.level) == NorthstarFluids.SULFURIC_ACID.getSource().getSource()) {
        	sulfurBurn(entity, entity.getRandom());
        }
    	if(entity.getY() > 800) {
    		if(!oxygenated) {
    			entity.hurt(DamageSource.DROWN, 2f);
    		}
    		if(!hasInsulation) {
    			boolean flag = entity.getType().is(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES);
    	        int j = flag ? 7 : 2;
    	        entity.hurt(DamageSource.FREEZE, (float)j);
    		}
    	}
        
        // Test code to figure out how changing dimensions works
//        if(entity instanceof ServerPlayer && entity.getItemInHand(InteractionHand.MAIN_HAND).getItem() == Items.FLINT_AND_STEEL && !entity.level.isClientSide && entity.level.dimension() == Level.OVERWORLD) {
//    		ResourceKey<Level> dest = NorthstarDimensions.MOON_DIM_KEY;
//    		ServerLevel destLevel = entity.getLevel().getServer().getLevel(dest);
//    		RocketHandler.changePlayerDimension(destLevel, (ServerPlayer) entity, new PortalForcer(destLevel));
//        }
    }
	@Nullable
	public Entity changeDimensionCustom(ServerLevel pDestination) {
		Entity entity = (Entity) (Object) this;
		if (!net.minecraftforge.common.ForgeHooks.onTravelToDimension(entity, pDestination.dimension())) return null;
        entity.level.getProfiler().push("changeDimension");
        entity.level.getProfiler().push("reposition");
        BlockPos blockpos = entity.blockPosition();
        PortalInfo portalinfo = new PortalInfo(new Vec3((double)blockpos.getX() + 0.5D, (double)blockpos.getY(), (double)blockpos.getZ() + 0.5D),
        		entity.getDeltaMovement(), entity.getYRot(), entity.getXRot());
        Entity transportedEntity = pDestination.getPortalForcer().placeEntity(entity, (ServerLevel) entity.level, pDestination, entity.getYRot(), spawnPortal -> { //Forge: Start vanilla logic
            entity.level.getProfiler().popPush("reloading");
            Entity newentity = entity.getType().create(pDestination);
            if (newentity != null) {
               newentity.restoreFrom(newentity);
               newentity.moveTo(portalinfo.pos.x, portalinfo.pos.y, portalinfo.pos.z, portalinfo.yRot, newentity.getXRot());
               newentity.setDeltaMovement(portalinfo.speed);
               pDestination.addDuringTeleport(newentity);
               if (spawnPortal && pDestination.dimension() == Level.END) {
                  ServerLevel.makeObsidianPlatform(pDestination);
               }
            }
            return newentity;
            });
        entity.setRemoved(Entity.RemovalReason.CHANGED_DIMENSION);
        entity.level.getProfiler().pop();
        ((ServerLevel)entity.level).resetEmptyTime();
        pDestination.resetEmptyTime();
        entity.level.getProfiler().pop();
        return transportedEntity;
	}
	
	
	public void sulfurBurn(Entity entity, RandomSource rando) {
        if (entity.hurt(DamageSource.LAVA, 6.0F)) {
        	entity.playSound(SoundEvents.GENERIC_BURN, 0.4F, 2.0F + rando.nextFloat() * 0.4F);
         }
	}
	public Fluid getFluidAtPos(Entity entity, Level level) {
		float height = level.getBlockState(entity.blockPosition()).getFluidState().getType().getHeight(level.getFluidState(entity.blockPosition()), level, entity.blockPosition());
		if(height + entity.blockPosition().getY() > entity.position().y)
		{return level.getBlockState(entity.blockPosition()).getFluidState().getType();}
		else{return null;}
	}
   
    
    private static int checkTemp(LivingEntity entity) {
    	return TemperatureStuff.getTemp(entity.blockPosition(), entity.level);    	
    }
    
    
}
