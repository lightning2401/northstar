package com.lightning.northstar.mixin.dimensionstuff;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.lightning.northstar.world.dimension.NorthstarDimensions;
import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Vector3f;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.FogRenderer.FogMode;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.CubicSampler;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@Mixin(FogRenderer.class)
public abstract class FogRendererMixin {
	
	private static float fogRed;
	private static float fogGreen;
	private static float fogBlue;
	private static float p = 1;
	
	
	@SuppressWarnings("resource")
	@Inject(method = "setupColor", at = @At("HEAD"), cancellable = true)
	 private static void setupColor(Camera pActiveRenderInfo, float pPartialTicks, ClientLevel pLevel, int pRenderDistanceChunks, float pBossColorModifier, CallbackInfo info) {
		ResourceKey<Level> player_dim = Minecraft.getInstance().level.dimension();
		boolean isSubmerged = !Minecraft.getInstance().gameRenderer.getMainCamera().getBlockAtCamera().getFluidState().isEmpty();
		if(!isSubmerged) {
			if (player_dim == NorthstarDimensions.MARS_DIM_KEY)
			{float playerEyeLevel = (float) Minecraft.getInstance().player.getEyePosition(3).y;
			if (playerEyeLevel > 400) {info.cancel();
	    	      Vector3f fogColor = net.minecraftforge.client.ForgeHooksClient.getFogColor(pActiveRenderInfo, pPartialTicks, pLevel, pRenderDistanceChunks, pBossColorModifier, fogRed, fogGreen, fogBlue);
	    	      
	              fogRed = (float) (fogColor.x() - ((playerEyeLevel - 400) / 300));
	              fogGreen = (float) (fogColor.y() - ((playerEyeLevel - 400) / 300));
	              fogBlue = (float) (fogColor.z() - ((playerEyeLevel - 400) / 300));
	              if (fogRed<0) {fogRed=0;}
	              if (fogGreen<0) {fogGreen=0;}
	              if (fogBlue<0) {fogBlue=0;}
	
	    	      RenderSystem.clearColor(fogRed, fogGreen, fogBlue, 0.0F);}else {
	    	  info.cancel();
	  	      Vector3f fogColor = net.minecraftforge.client.ForgeHooksClient.getFogColor(pActiveRenderInfo, pPartialTicks, pLevel, pRenderDistanceChunks, pBossColorModifier, fogRed, fogGreen, fogBlue);
	  	      
	            fogRed = (float) fogColor.x();
	            fogGreen = (float) fogColor.y();
	            fogBlue = (float) fogColor.z();
	            if (fogRed<0) {fogRed=0;}
	            if (fogGreen<0) {fogGreen=0;}
	            if (fogBlue<0) {fogBlue=0;}
	
	  	      RenderSystem.clearColor(fogRed, fogGreen, fogBlue, 0.0F);}}
				
	    	if (player_dim == Level.OVERWORLD)
			{float playerEyeLevel = (float) Minecraft.getInstance().player.getEyePosition(3).y;
	    		if (playerEyeLevel > 450) {info.cancel();
			 	  ClientLevel level = Minecraft.getInstance().level;
			 	  Vec3 toad = level.getSkyColor(Minecraft.getInstance().player.getEyePosition(3), pPartialTicks);

	              fogRed = (float) (toad.x() - ((playerEyeLevel - 450) / 300));
	              fogGreen = (float) (toad.y() - ((playerEyeLevel - 450) / 300));
	              fogBlue = (float) (toad.z() - ((playerEyeLevel - 450) / 300));
	              if (fogRed<0) {fogRed=0;}
	              if (fogGreen<0) {fogGreen=0;}
	              if (fogBlue<0) {fogBlue=0;}
	
	    	      RenderSystem.clearColor(fogRed, fogGreen, fogBlue, 0.0F);}}
			if (player_dim == NorthstarDimensions.VENUS_DIM_KEY)
			{float playerEyeLevel = (float) Minecraft.getInstance().player.getEyePosition(3).y;
			if (playerEyeLevel > 600) {info.cancel();
			 	  ClientLevel level = Minecraft.getInstance().level;
			 	  Vec3 toad = level.getSkyColor(Minecraft.getInstance().player.getEyePosition(3), pPartialTicks);
	    	      Vector3f fogColor = net.minecraftforge.client.ForgeHooksClient.getFogColor(pActiveRenderInfo, pPartialTicks, pLevel, pRenderDistanceChunks, pBossColorModifier, fogRed, fogGreen, fogBlue);
	    	      
	              fogRed = (float) (toad.x() - ((playerEyeLevel - 600) / 300));
	              fogGreen = (float) (toad.y() - ((playerEyeLevel - 600) / 300));
	              fogBlue = (float) (toad.z() - ((playerEyeLevel - 600) / 300));
	              if (fogRed<0) {fogRed=0;}
	              if (fogGreen<0) {fogGreen=0;}
	              if (fogBlue<0) {fogBlue=0;}
	
	    	      RenderSystem.clearColor(fogRed, fogGreen, fogBlue, 0.0F);}else {
	    	  info.cancel();
		 	  ClientLevel level = Minecraft.getInstance().level;
		 	  Vec3 toad = level.getSkyColor(Minecraft.getInstance().player.getEyePosition(3), pPartialTicks);
	  	      
	            fogRed = (float) toad.x();
	            fogGreen = (float) toad.y();
	            fogBlue = (float) toad.z();
	            if (fogRed<0) {fogRed=0;}
	            if (fogGreen<0) {fogGreen=0;}
	            if (fogBlue<0) {fogBlue=0;}
	
	  	      RenderSystem.clearColor(fogRed, fogGreen, fogBlue, 0.0F);}}
		}
	 }
	@SuppressWarnings("resource")
	@Inject(method = "setupFog", at = @At("HEAD"), cancellable = true)
	private static void setupFog(Camera pCamera, FogRenderer.FogMode pFogMode, float pFarPlaneDistance, boolean p_234176_, float p_234177_, CallbackInfo info) {
		float pPartialTicks = 3;
	    FogType fogtype = pCamera.getFluidInCamera();
		ResourceKey<Level> player_dim = Minecraft.getInstance().level.dimension();
    	if (player_dim == NorthstarDimensions.MARS_DIM_KEY)
    	{float playerEyeLevel = (float) Minecraft.getInstance().player.getEyePosition(3).y;
    	ClientLevel level = Minecraft.getInstance().level;
 		float rain_det = Minecraft.getInstance().level.getRainLevel(pPartialTicks);
 		if(p<2 && level.getRawBrightness(pCamera.getBlockPosition(), -7) >= 16)
 		{p+=0.01;}
 		else if(p>0 && !(level.getRawBrightness(pCamera.getBlockPosition(), -7) >= 16))
 		{p-=0.01;}
 		else if(p<0)
 		{p=0;}
	      if (!(rain_det <= 0.0F) && fogtype == FogType.NONE && playerEyeLevel < 500 && p != 0){

	    	  
	    	  float fog_dist;
	    	  fog_dist = 48 / p;
	    
	    	  info.cancel();
	 		 	 RenderSystem.setShaderFogStart(-8.0F);
	 		     RenderSystem.setShaderFogEnd(fog_dist);
	 		     RenderSystem.setShaderFogShape(FogShape.SPHERE);
	 		     net.minecraftforge.client.ForgeHooksClient.onFogRender(FogMode.FOG_TERRAIN, FogType.WATER, pCamera, pPartialTicks, Minecraft.getInstance().options.getEffectiveRenderDistance(), -8, 96, FogShape.SPHERE);
	      }

    		Camera cam = Minecraft.getInstance().gameRenderer.getMainCamera();
    		float f4 = 0.25F + 0.75F * (float)pFarPlaneDistance / 32.0F;
            f4 = 1.0F - (float)Math.pow((double)f4, 0.25D);
            Vec3 vec3 = level.getSkyColor(cam.getPosition(), pPartialTicks);
            float f6 = (float)vec3.x;
            float f8 = (float)vec3.y;
            float f10 = (float)vec3.z;
            float f11 = Mth.clamp(Mth.cos(level.getTimeOfDay(pPartialTicks) * ((float)Math.PI * 2F)) * 2.0F + 0.5F, 0.0F, 1.0F);
            BiomeManager biomemanager = level.getBiomeManager();
            Vec3 vec31 = cam.getPosition().subtract(2.0D, 2.0D, 2.0D).scale(0.25D);
            Vec3 vec32 = CubicSampler.gaussianSampleVec3(vec31, (p_109033_, p_109034_, p_109035_) -> {
               return level.effects().getBrightnessDependentFogColor(Vec3.fromRGB24(biomemanager.getNoiseBiomeAtQuart(p_109033_, p_109034_, p_109035_).value().getFogColor()), f11);
            });
            fogRed = (float)vec32.x();
            fogGreen = (float)vec32.y();
            fogBlue = (float)vec32.z();
            if (pFarPlaneDistance >= 4) {
               float f12 = Mth.sin(level.getSunAngle(pPartialTicks)) > 0.0F ? -1.0F : 1.0F;
               Vector3f vector3f = new Vector3f(f12, 0.0F, 0.0F);
               float f16 = cam.getLookVector().dot(vector3f);
               if (f16 < 0.0F) {
                  f16 = 0.0F;
               }

               if (f16 > 0.0F) {
                  float[] afloat = level.effects().getSunriseColor(level.getTimeOfDay(pPartialTicks), pPartialTicks);
                  if (afloat != null) {
                     f16 *= afloat[3];
                     fogRed = fogRed * (1.0F - f16) + afloat[0] * f16;
                     fogGreen = fogGreen * (1.0F - f16) + afloat[1] * f16;
                     fogBlue = fogBlue * (1.0F - f16) + afloat[2] * f16;
                  }
               }
            }

            fogRed += (f6 - fogRed) * f4;
            fogGreen += (f8 - fogGreen) * f4;
            fogBlue += (f10 - fogBlue) * f4;

            
            Vec3 fogColor = Minecraft.getInstance().level.getSkyColor(Minecraft.getInstance().gameRenderer.getMainCamera().getPosition(), 3);
            Biome biome = level.getBiome(pCamera.getBlockPosition()).value();
  	        if (!(rain_det <= 0.0F) &&  biome.getPrecipitation() == Biome.Precipitation.NONE)
  	        {fogRed = (float) fogColor.x() * 1.35f;
            fogGreen = (float) fogColor.y() * 1.15f;
            fogBlue = (float) fogColor.z() * 1f;}
  	        else
            {fogRed = (float) fogColor.x();
            fogGreen = (float) fogColor.y();
            fogBlue = (float) fogColor.z();}
  	        
		     net.minecraftforge.client.ForgeHooksClient.onFogRender(FogMode.FOG_TERRAIN, FogType.NONE, pCamera, pPartialTicks, Minecraft.getInstance().options.getEffectiveRenderDistance(), -8, 96, FogShape.CYLINDER);

            RenderSystem.clearColor(fogRed, fogGreen, fogBlue, 0.0F);}
    	if (player_dim == Level.OVERWORLD)
    	{float playerEyeLevel = (float) Minecraft.getInstance().player.getEyePosition(3).y;
    	ClientLevel level = Minecraft.getInstance().level;
    		if (playerEyeLevel > 450) {info.cancel();
    		Camera cam = Minecraft.getInstance().gameRenderer.getMainCamera();
    		float f4 = 0.25F + 0.75F * (float)pFarPlaneDistance / 32.0F;
            f4 = 1.0F - (float)Math.pow((double)f4, 0.25D);
            Vec3 vec3 = level.getSkyColor(cam.getPosition(), pPartialTicks);
            float f6 = (float)vec3.x;
            float f8 = (float)vec3.y;
            float f10 = (float)vec3.z;
            float f11 = Mth.clamp(Mth.cos(level.getTimeOfDay(pPartialTicks) * ((float)Math.PI * 2F)) * 2.0F + 0.5F, 0.0F, 1.0F);
            BiomeManager biomemanager = level.getBiomeManager();
            Vec3 vec31 = cam.getPosition().subtract(2.0D, 2.0D, 2.0D).scale(0.25D);
            Vec3 vec32 = CubicSampler.gaussianSampleVec3(vec31, (p_109033_, p_109034_, p_109035_) -> {
               return level.effects().getBrightnessDependentFogColor(Vec3.fromRGB24(biomemanager.getNoiseBiomeAtQuart(p_109033_, p_109034_, p_109035_).value().getFogColor()), f11);
            });
            fogRed = (float)vec32.x();
            fogGreen = (float)vec32.y();
            fogBlue = (float)vec32.z();
            if (pFarPlaneDistance >= 4) {
               float f12 = Mth.sin(level.getSunAngle(pPartialTicks)) > 0.0F ? -1.0F : 1.0F;
               Vector3f vector3f = new Vector3f(f12, 0.0F, 0.0F);
               float f16 = cam.getLookVector().dot(vector3f);
               if (f16 < 0.0F) {
                  f16 = 0.0F;
               }

               if (f16 > 0.0F) {
                  float[] afloat = level.effects().getSunriseColor(level.getTimeOfDay(pPartialTicks), pPartialTicks);
                  if (afloat != null) {
                     f16 *= afloat[3];
                     fogRed = fogRed * (1.0F - f16) + afloat[0] * f16;
                     fogGreen = fogGreen * (1.0F - f16) + afloat[1] * f16;
                     fogBlue = fogBlue * (1.0F - f16) + afloat[2] * f16;
                  }
               }
            }

            fogRed += (f6 - fogRed) * f4;
            fogGreen += (f8 - fogGreen) * f4;
            fogBlue += (f10 - fogBlue) * f4;
            
            Vec3 fogColor = Minecraft.getInstance().level.getSkyColor(Minecraft.getInstance().gameRenderer.getMainCamera().getPosition(), 3);
            
            fogRed = (float) fogColor.x();
            fogGreen = (float) fogColor.y();
            fogBlue = (float) fogColor.z();
            
		 	 RenderSystem.setShaderFogStart(-8.0F);
 		     RenderSystem.setShaderFogShape(FogShape.CYLINDER);
 		     net.minecraftforge.client.ForgeHooksClient.onFogRender(FogMode.FOG_TERRAIN, FogType.NONE, pCamera, pPartialTicks, Minecraft.getInstance().options.getEffectiveRenderDistance(), -8, 96, FogShape.CYLINDER);
            
            

            RenderSystem.clearColor(fogRed, fogGreen, fogBlue, 0.0F);}}
    	if (player_dim == NorthstarDimensions.VENUS_DIM_KEY)
    	{float playerEyeLevel = (float) Minecraft.getInstance().player.getEyePosition(3).y;
    	ClientLevel level = Minecraft.getInstance().level;
 		float rain_det = Minecraft.getInstance().level.getRainLevel(pPartialTicks);
 		

    	
    	
    		if (playerEyeLevel > 600) {info.cancel();}
    		Camera cam = Minecraft.getInstance().gameRenderer.getMainCamera();
    		float f4 = 0.25F + 0.75F * (float)pFarPlaneDistance / 32.0F;
            f4 = 1.0F - (float)Math.pow((double)f4, 0.25D);
            Vec3 vec3 = level.getSkyColor(cam.getPosition(), pPartialTicks);
            float f6 = (float)vec3.x;
            float f8 = (float)vec3.y;
            float f10 = (float)vec3.z;
            float f11 = Mth.clamp(Mth.cos(level.getTimeOfDay(pPartialTicks) * ((float)Math.PI * 2F)) * 2.0F + 0.5F, 0.0F, 1.0F);
            BiomeManager biomemanager = level.getBiomeManager();
            Vec3 vec31 = cam.getPosition().subtract(2.0D, 2.0D, 2.0D).scale(0.25D);
            Vec3 vec32 = CubicSampler.gaussianSampleVec3(vec31, (p_109033_, p_109034_, p_109035_) -> {
               return level.effects().getBrightnessDependentFogColor(Vec3.fromRGB24(biomemanager.getNoiseBiomeAtQuart(p_109033_, p_109034_, p_109035_).value().getFogColor()), f11);
            });
            fogRed = (float)vec32.x();
            fogGreen = (float)vec32.y();
            fogBlue = (float)vec32.z();
            if (pFarPlaneDistance >= 4) {
               float f12 = Mth.sin(level.getSunAngle(pPartialTicks)) > 0.0F ? -1.0F : 1.0F;
               Vector3f vector3f = new Vector3f(f12, 0.0F, 0.0F);
               float f16 = cam.getLookVector().dot(vector3f);
               if (f16 < 0.0F) {
                  f16 = 0.0F;
               }

               if (f16 > 0.0F) {
                  float[] afloat = level.effects().getSunriseColor(level.getTimeOfDay(pPartialTicks), pPartialTicks);
                  if (afloat != null) {
                     f16 *= afloat[3];
                     fogRed = fogRed * (1.0F - f16) + afloat[0] * f16;
                     fogGreen = fogGreen * (1.0F - f16) + afloat[1] * f16;
                     fogBlue = fogBlue * (1.0F - f16) + afloat[2] * f16;
                  }
               }
            }

            fogRed += (f6 - fogRed) * f4;
            fogGreen += (f8 - fogGreen) * f4;
            fogBlue += (f10 - fogBlue) * f4;

            
            Vec3 fogColor = Minecraft.getInstance().level.getSkyColor(Minecraft.getInstance().gameRenderer.getMainCamera().getPosition(), 3);
            fogRed = (float) fogColor.x();
            fogGreen = (float) fogColor.y();
            fogBlue = (float) fogColor.z();
            
		 	 RenderSystem.setShaderFogStart(-8.0F);
 		     RenderSystem.setShaderFogShape(FogShape.CYLINDER);
		     net.minecraftforge.client.ForgeHooksClient.onFogRender(FogMode.FOG_TERRAIN, FogType.NONE, pCamera, pPartialTicks, Minecraft.getInstance().options.getEffectiveRenderDistance(), -8, 96, FogShape.CYLINDER);
            RenderSystem.clearColor(fogRed, fogGreen, fogBlue, 0.0F);}
	}
	
	@SuppressWarnings("resource")
	@Inject(method = "levelFogColor", at = @At("HEAD"), cancellable = true)
	   private static void levelFogColor(CallbackInfo info) {
		boolean isSubmerged = !Minecraft.getInstance().gameRenderer.getMainCamera().getBlockAtCamera().getFluidState().isEmpty();
		if(!isSubmerged) {
			ResourceKey<Level> player_dim = Minecraft.getInstance().level.dimension();
	    	if (player_dim == NorthstarDimensions.MARS_DIM_KEY)
	    	{float playerEyeLevel = (float) Minecraft.getInstance().player.getEyePosition(3).y;
	        if (playerEyeLevel > 400) {
	        Vec3 vec3 = Minecraft.getInstance().level.getSkyColor(Minecraft.getInstance().gameRenderer.getMainCamera().getPosition(), 3);
	        float R = (float) (vec3.x - ((playerEyeLevel - 400) / 300));
	        float G = (float) (vec3.y - ((playerEyeLevel - 400) / 300));
	        float B = (float) (vec3.z - ((playerEyeLevel - 400) / 300));
	        if (R<0) {R=0;}
	        if (G<0) {G=0;}
	        if (B<0) {B=0;}
	        RenderSystem.setShaderFogColor(R, G, B);info.cancel();}}
	    	if (player_dim == Level.OVERWORLD)
	    	{float playerEyeLevel = (float) Minecraft.getInstance().player.getEyePosition(3).y;
	        if (playerEyeLevel > 450) {
	        Vec3 vec3 = Minecraft.getInstance().level.getSkyColor(Minecraft.getInstance().gameRenderer.getMainCamera().getPosition(), 3);
	        float R = (float) (vec3.x - ((playerEyeLevel - 450) / 300));
	        float G = (float) (vec3.y - ((playerEyeLevel - 450) / 300));
	        float B = (float) (vec3.z - ((playerEyeLevel - 450) / 300));
	        if (R<0) {R=0;}
	        if (G<0) {G=0;}
	        if (B<0) {B=0;}
	        RenderSystem.setShaderFogColor(R, G, B);info.cancel();}}
	    	if (player_dim == NorthstarDimensions.VENUS_DIM_KEY)
	    	{float playerEyeLevel = (float) Minecraft.getInstance().player.getEyePosition(3).y;
	        if (playerEyeLevel > 600) {
	        Vec3 vec3 = Minecraft.getInstance().level.getSkyColor(Minecraft.getInstance().gameRenderer.getMainCamera().getPosition(), 3);
	        float R = (float) (vec3.x - ((playerEyeLevel - 600) / 300));
	        float G = (float) (vec3.y - ((playerEyeLevel - 600) / 300));
	        float B = (float) (vec3.z - ((playerEyeLevel - 600) / 300));
	        if (R<0) {R=0;}
	        if (G<0) {G=0;}
	        if (B<0) {B=0;}
	        RenderSystem.setShaderFogColor(R, G, B);info.cancel();}}
	    	if (player_dim == NorthstarDimensions.MOON_DIM_KEY || player_dim == null || player_dim == NorthstarDimensions.MERCURY_DIM_KEY || player_dim == NorthstarDimensions.EARTH_ORBIT_DIM_KEY)
	    	{RenderSystem.setShaderFogColor(0, 0, 0);info.cancel();}
		}
    	
	}


		
}
