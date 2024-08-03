package com.lightning.northstar.mixin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.mutable.MutableInt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.lightning.northstar.contraptions.RocketContraption;
import com.lightning.northstar.contraptions.RocketContraptionEntity;
import com.lightning.northstar.contraptions.RocketHandler;
import com.lightning.northstar.world.dimension.NorthstarDimensions;
import com.lightning.northstar.world.dimension.NorthstarPlanets;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.protocol.game.ClientboundChangeDifficultyPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.portal.PortalForcer;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.phys.Vec3;

@SuppressWarnings("unused")
@Mixin(LivingEntity.class)
public class GravityStuffMixin {
    @Unique
    private static final double CONSTANT = 0.08;
    private static final double EARTH_GRAV = 1;
    private static final double MOON_GRAV = 0.16;
    private static final double OUTER_MOON_GRAV = 0.06;
    private static final double SUPER_GRAV = 4;
    private static final double MARS_GRAV = 0.37;
    private static final double VENUS_GRAV = 0.89;
    private static final double MERCURY_GRAV = 0.38;
    

	private static final double GANYMEDE_GRAV = 0.14;
    private static final double TITAN_GRAV = 0.14;
    private static final double EUROPA_GRAV = 0.13;

    double PLANET_GRAV = 1;
    private int fall_disabled = 0;
    	//help
    
    @Inject(method = "travel", at = @At("TAIL"))
    public void northstar$travel(CallbackInfo ci) {
    	LivingEntity entity = (LivingEntity) (Object) this;
    	
    	if(fall_disabled > 0) {fall_disabled--; entity.fallDistance = 0;}
    	if(RocketHandler.isInRocket(entity) && entity.getY() > 1500) {
    		fall_disabled = 400;
    	}
        Vec3 velocity = entity.getDeltaMovement();
        boolean isInOrbit = NorthstarPlanets.isInOrbit(entity.level().dimension());
        if (entity.level().dimension() == NorthstarDimensions.MARS_DIM_KEY)
        {PLANET_GRAV = MARS_GRAV;}else 
        if (entity.level().dimension() == NorthstarDimensions.VENUS_DIM_KEY)
        {PLANET_GRAV = VENUS_GRAV;}else
        if (entity.level().dimension() == NorthstarDimensions.MOON_DIM_KEY)
        {PLANET_GRAV = MOON_GRAV;}else
        if (entity.level().dimension() == NorthstarDimensions.MERCURY_DIM_KEY)
        {PLANET_GRAV = MERCURY_GRAV;}else
        if(isInOrbit)
        {PLANET_GRAV = OUTER_MOON_GRAV;}else
        {PLANET_GRAV = EARTH_GRAV;}
        
        if(entity.isFallFlying() || entity.isInFluidType()){
        	PLANET_GRAV = EARTH_GRAV;
        }
        if (!entity.isNoGravity() && !entity.isInWater() && !entity.isInLava() && !entity.hasEffect(MobEffects.SLOW_FALLING)) {
        	float dust_push = 0;
        	if (entity.level().getRainLevel(0) > 0 && entity.level().getRawBrightness(entity.blockPosition(), -1) == 16 && !entity.isSpectator() && (entity.level().dimension() == NorthstarDimensions.MARS_DIM_KEY)
        			&& entity.level().isInWorldBounds(entity.blockPosition()) && !RocketHandler.isInRocket(entity))
        	{dust_push = 0.005f;}
        	if(entity instanceof Player ply) {
        		if(ply.isCreative()) {
        			dust_push = 0;
        		}
        	}
        	
        	double newGrav = CONSTANT * PLANET_GRAV;
        	float crouchPush = 0;
        	if(!isInOrbit) 
            {entity.setDeltaMovement(velocity.x() + dust_push, velocity.y() + (CONSTANT - newGrav), velocity.z() - dust_push);}
        	else
        	{if(entity.isCrouching()) {crouchPush = 0.05f;}
        	float vel_y = (float) Mth.clamp(velocity.y(), -0.3, 15);
        	entity.setDeltaMovement(velocity.x() + dust_push,vel_y + (CONSTANT - newGrav) -crouchPush, velocity.z() - dust_push);}
        }
        if(isInOrbit) {
        	if(entity.getY() < 0 && !entity.level().isClientSide) {
        		if(entity.level().dimension() == NorthstarDimensions.EARTH_ORBIT_DIM_KEY) {
        			ServerLevel destLevel = entity.level().getServer().getLevel(Level.OVERWORLD);
        			if(entity instanceof ServerPlayer plyer)
        			{changePlayerDimension(destLevel, plyer, new PortalForcer(destLevel));}else 
        			{changeDimensionCustom(destLevel, entity, new PortalForcer(destLevel));}
        		}
        	}
        }
    }
    @Inject(method = "calculateFallDamage", at = @At("HEAD"), cancellable = true)
    public void calculateFallDamage(float pFallDistance, float pDamageMultiplier, CallbackInfoReturnable<Integer> info) {
    	LivingEntity entity = (LivingEntity) (Object) this;

    	if(!NorthstarPlanets.hasNormalGrav(entity.level().dimension())) {
    	      MobEffectInstance mobeffectinstance = entity.getEffect(MobEffects.JUMP);
    	      double mult = getGravMultiplier(entity.level().dimension());
    	      float f = (float) (mobeffectinstance == null ? 0.0F : (float)(mobeffectinstance.getAmplifier() + 1) * mult);
    	      info.setReturnValue(Mth.ceil(((pFallDistance * mult) - 3.0F - f) * pDamageMultiplier));
    	}
    }
    
    
    
	private static Entity changePlayerDimension(ServerLevel pDestination, ServerPlayer entity, net.minecraftforge.common.util.ITeleporter teleporter) {
	      if (!net.minecraftforge.common.ForgeHooks.onTravelToDimension(entity, pDestination.dimension())) return null;
	      ((ServerPlayer) entity).isChangingDimension();
	      ServerLevel serverlevel = entity.serverLevel();
	          LevelData leveldata = pDestination.getLevelData();
	          entity.connection.send(new ClientboundRespawnPacket(pDestination.dimensionTypeId(), pDestination.dimension(), BiomeManager.obfuscateSeed(pDestination.getSeed()), entity.gameMode.getGameModeForPlayer(), entity.gameMode.getPreviousGameModeForPlayer(), pDestination.isDebug(), pDestination.isFlat(), (byte)3, entity.getLastDeathLocation(), entity.getPortalCooldown()));
	          entity.connection.send(new ClientboundChangeDifficultyPacket(leveldata.getDifficulty(), leveldata.isDifficultyLocked()));
	          PlayerList playerlist = entity.server.getPlayerList();


	          playerlist.sendPlayerPermissionLevel(entity);
	          pDestination.removePlayerImmediately(entity, Entity.RemovalReason.CHANGED_DIMENSION);
	          entity.revive();
	          PortalInfo portalinfo = new PortalInfo(entity.position(), entity.getDeltaMovement(), entity.getYRot(), entity.getXRot());
	             Entity e = teleporter.placeEntity(entity, serverlevel, pDestination, entity.getYRot(), spawnPortal -> {//Forge: Start vanilla logic
	             serverlevel.getProfiler().push("moving");
	             serverlevel.getProfiler().pop();
	             serverlevel.getProfiler().push("placing");
	             entity.setServerLevel(pDestination);
	             pDestination.addDuringPortalTeleport(entity);
	             entity.setXRot(portalinfo.xRot);
	             entity.setYRot(portalinfo.yRot);
	             entity.moveTo(portalinfo.pos.x, 800, portalinfo.pos.z);
	             serverlevel.getProfiler().pop();
	             CriteriaTriggers.CHANGED_DIMENSION.trigger(entity, entity.level().dimension(), pDestination.dimension());
	             return entity;//forge: this is part of the ITeleporter patch
	             });//Forge: End vanilla logic
	             if (e != entity) throw new java.lang.IllegalArgumentException(String.format(java.util.Locale.ENGLISH, "Teleporter %s returned not the player entity but instead %s, expected PlayerEntity %s", teleporter, e, entity));
	             entity.connection.send(new ClientboundPlayerAbilitiesPacket(entity.getAbilities()));
	             playerlist.sendLevelInfo(entity, pDestination);
	             playerlist.sendAllPlayerInfo(entity);

	             for(MobEffectInstance mobeffectinstance : entity.getActiveEffects()) {
	                entity.connection.send(new ClientboundUpdateMobEffectPacket(entity.getId(), mobeffectinstance));
	             
	          }	          
	             
	          return entity;
	       
	}
	
	private static Entity changeDimensionCustom(ServerLevel pDestination, Entity entity, net.minecraftforge.common.util.ITeleporter teleporter) {
	      if (!net.minecraftforge.common.ForgeHooks.onTravelToDimension(entity, pDestination.dimension())) return null;
	      if (entity.level() instanceof ServerLevel && !entity.isRemoved()) {
	    	  entity.level().getProfiler().push("changeDimension");
	    	  int seatNumber = -12345;  	  
	    	  entity.level().getProfiler().push("reposition");

	    	  System.out.println(entity);

	    	  Entity transportedEntity = teleporter.placeEntity(entity, (ServerLevel) entity.level(), pDestination, entity.getYRot(), spawnPortal -> {
	            	
	            entity.level().getProfiler().popPush("reloading");
	            Entity newentity = entity.getType().create(pDestination);
	            System.out.println(newentity);
	            System.out.println(entity.getType());
	            
	            if (newentity != null) {
	            	newentity.restoreFrom(entity);
	            	newentity.moveTo(entity.position().x, 800, entity.position().z, entity.getYRot(), entity.getXRot());
	            	newentity.setDeltaMovement(entity.getDeltaMovement());
	               pDestination.addDuringTeleport(newentity);
	            }
	            
	            return newentity;
	            });    	  
	            
	            entity.setRemoved(Entity.RemovalReason.CHANGED_DIMENSION);
	            entity.level().getProfiler().pop();
	            ((ServerLevel)entity.level()).resetEmptyTime();
	            pDestination.resetEmptyTime();
	            entity.level().getProfiler().pop();
	            return transportedEntity;
	      } else {
	         return null;
	      }
	}
    
    public double getGravMultiplier(ResourceKey<Level> level) {
    	// I love spaghetti
    	if(level == NorthstarDimensions.MOON_DIM_KEY) {return MOON_GRAV;}
    	if(level == NorthstarDimensions.MARS_DIM_KEY) {return MARS_GRAV;}
    	if(level == NorthstarDimensions.MERCURY_DIM_KEY) {return MERCURY_GRAV;}
    	if(level == NorthstarDimensions.VENUS_DIM_KEY) {return VENUS_GRAV;}
    	if(level == NorthstarDimensions.EARTH_ORBIT_DIM_KEY) {return OUTER_MOON_GRAV;}
		return 1;
    }
}
