package com.lightning.northstar.mixin.gravitystuff;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.lightning.northstar.world.dimension.NorthstarDimensions;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.phys.Vec3;

@Mixin(AbstractArrow.class)
public class AbstractArrowGravityMixin {
    @Unique
    private static final double CONSTANT = 0.08;
    private static final double EARTH_GRAV = 1;
    private static final double MOON_GRAV = 0.5;
    private static final double MARS_GRAV = 0.5;
    double PLANET_GRAV = 1;
    

    @Inject(method = "tick", at = @At("TAIL"))
    public void northstar$tick(CallbackInfo ci) {
            Entity entity = (Entity) (Object) this;
            Vec3 velocity = entity.getDeltaMovement();
            if (entity.getLevel().dimension() == NorthstarDimensions.MARS_DIM_KEY)
            {PLANET_GRAV = MARS_GRAV;}else 
            if (entity.getLevel().dimension() == NorthstarDimensions.MOON_DIM_KEY)
            {PLANET_GRAV = MOON_GRAV;}else {PLANET_GRAV = EARTH_GRAV;}
            if (!entity.isNoGravity()) {
                double newGrav = CONSTANT * PLANET_GRAV;
                entity.setDeltaMovement(velocity.x(), velocity.y() + CONSTANT - newGrav, velocity.z());
            }
    }

}
