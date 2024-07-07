package com.lightning.northstar.mixin.dimensionstuff;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.lightning.northstar.world.dimension.NorthstarDimensions;


@Mixin(DimensionSpecialEffects.class)
public class DimensionSpecialEffectsMixin {
	private final float[] sunCol = new float[4];
    
	@SuppressWarnings("resource")
	@Inject(method = "getSunriseColor", at = @At("RETURN"), cancellable = true)
    private float[] getSunriseColor(float Time, float tickDelta, CallbackInfoReturnable<float[]> info) {
		ResourceKey<Level> player_dim = Minecraft.getInstance().level.dimension();
		if (player_dim == NorthstarDimensions.MARS_DIM_KEY) {
	      float f1 = Mth.cos(Time * ((float)Math.PI * 2F)) - 0.0F;
	      if (f1 >= -0.4F && f1 <= 0.4F) {
	         float f3 = (f1 - -0.0F) / 0.4F * 0.5F + 0.5F;
	         float f4 = 1.0F - (1.0F - Mth.sin(f3 * (float)Math.PI)) * 0.99F;
	         f4 *= f4;
	         this.sunCol[0] = f3 * 0.2F + 0.5F;
	         this.sunCol[1] = f3 * f3 * 0.2F + 0.5F;
	         this.sunCol[2] = f3 * f3 * 0.8F + 0.5F;
	         this.sunCol[3] = f4;
	         return this.sunCol;
	      }

	   }
	    if (player_dim == Level.OVERWORLD)
	    {float f1 = Mth.cos(Time * ((float)Math.PI * 2F)) - 0.0F;
	      if (f1 >= -0.4F && f1 <= 0.4F) {
	         float f3 = (f1 - -0.0F) / 0.4F * 0.5F + 0.5F;
	         float f4 = 1.0F - (1.0F - Mth.sin(f3 * (float)Math.PI)) * 0.99F;
	         f4 *= f4;
	         this.sunCol[0] = f3 * 0.3F + 0.7F;
	         this.sunCol[1] = f3 * f3 * 0.7F + 0.4F;
	         this.sunCol[2] = f3 * f3 * 0.0F + 0.2F;
	         this.sunCol[3] = f4;
	         return this.sunCol;
	      } else {
	         return null;
	      }
	    }
	    
	    if (player_dim == NorthstarDimensions.VENUS_DIM_KEY)
	    {float f1 = Mth.cos(Time * ((float)Math.PI * 2F)) - 0.0F;
	      if (f1 >= -0.4F && f1 <= 0.4F) {
	         float f3 = (f1 - -0.0F) / 0.4F * 0.5F + 0.5F;
	         float f4 = 1.0F - (1.0F - Mth.sin(f3 * (float)Math.PI)) * 0.99F;
	         f4 *= f4;
	         this.sunCol[0] = f3 * 0.3F + 0.6F;
	         this.sunCol[1] = f3 * f3 * 0.1F + 0.4F;
	         this.sunCol[2] = f3 * f3 * 0.0F + 1.0F;
	         this.sunCol[3] = f4;
	         return this.sunCol;
	      } else {
	         return null;
	      }
	    }
	    else
	    {return null;}
	}
}