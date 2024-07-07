package com.lightning.northstar.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.lightning.northstar.contraptions.RocketHandler;
import com.lightning.northstar.world.dimension.NorthstarDimensions;
import com.lightning.northstar.world.dimension.NorthstarPlanets;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
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
    	
    	if(fall_disabled > 0) {fall_disabled--; entity.fallDistance = 0; System.out.println("Fall Disabled: " + fall_disabled);}
    	if(RocketHandler.isInRocket(entity) && entity.getY() > 1500) {
    		fall_disabled = 400;
    	}
        Vec3 velocity = entity.getDeltaMovement();
        boolean isInOrbit = NorthstarPlanets.isInOrbit(entity.getLevel().dimension());
        if (entity.getLevel().dimension() == NorthstarDimensions.MARS_DIM_KEY)
        {PLANET_GRAV = MARS_GRAV;}else 
        if (entity.getLevel().dimension() == NorthstarDimensions.VENUS_DIM_KEY)
        {PLANET_GRAV = VENUS_GRAV;}else
        if (entity.getLevel().dimension() == NorthstarDimensions.MOON_DIM_KEY)
        {PLANET_GRAV = MOON_GRAV;}else
        if (entity.getLevel().dimension() == NorthstarDimensions.MERCURY_DIM_KEY)
        {PLANET_GRAV = MERCURY_GRAV;}else
        if(isInOrbit)
        {PLANET_GRAV = OUTER_MOON_GRAV;}else
        {PLANET_GRAV = EARTH_GRAV;}
        if (!entity.isNoGravity() && !entity.isInWater() && !entity.isInLava() && !entity.hasEffect(MobEffects.SLOW_FALLING)) {
        	float dust_push = 0;
        	if (entity.getLevel().getRainLevel(0) > 0 && entity.getLevel().getRawBrightness(entity.blockPosition(), -1) == 16 && !entity.isSpectator() && (entity.getLevel().dimension() == NorthstarDimensions.MARS_DIM_KEY)
        			&& entity.level.isInWorldBounds(entity.blockPosition()) && !RocketHandler.isInRocket(entity))
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
    }
    @Inject(method = "calculateFallDamage", at = @At("HEAD"), cancellable = true)
    public void calculateFallDamage(float pFallDistance, float pDamageMultiplier, CallbackInfoReturnable<Integer> info) {
    	LivingEntity entity = (LivingEntity) (Object) this;
    	if(entity instanceof ServerPlayer) 
    	{System.out.println("Is in rocket: " + RocketHandler.isInRocket(entity));}
    	if(!NorthstarPlanets.hasNormalGrav(entity.level.dimension())) {
    	      MobEffectInstance mobeffectinstance = entity.getEffect(MobEffects.JUMP);
    	      double mult = getGravMultiplier(entity.level.dimension());
    	      float f = (float) (mobeffectinstance == null ? 0.0F : (float)(mobeffectinstance.getAmplifier() + 1) * mult);
    	      info.setReturnValue(Mth.ceil(((pFallDistance * mult) - 3.0F - f) * pDamageMultiplier));
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
