package com.lightning.northstar.mixin.dimensionstuff;

import javax.annotation.Nullable;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.lightning.northstar.Northstar;
import com.lightning.northstar.particle.DustCloudParticleData;
import com.lightning.northstar.sound.NorthstarSounds;
import com.lightning.northstar.world.dimension.NorthstarDimensions;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;

import net.minecraft.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.ParticleStatus;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
	private static final ResourceLocation EARTH_CLOSE = new ResourceLocation(Northstar.MOD_ID,"textures/environment/earth_close.png");
	private static final ResourceLocation EARTH_FAR = new ResourceLocation(Northstar.MOD_ID,"textures/environment/earth_far.png");
	private static final ResourceLocation MOON_CLOSE = new ResourceLocation(Northstar.MOD_ID,"textures/environment/moon_close.png");
	private static final ResourceLocation MOON_FAR = new ResourceLocation(Northstar.MOD_ID,"textures/environment/moon_far.png");
	private static final ResourceLocation VENUS_FAR = new ResourceLocation(Northstar.MOD_ID,"textures/environment/venus_far_sky.png");
	private static final ResourceLocation VENUS_CLOSE = new ResourceLocation(Northstar.MOD_ID,"textures/environment/venus_close.png");
	private static final ResourceLocation BARE_SUN = new ResourceLocation(Northstar.MOD_ID,"textures/environment/baresun.png");
	private static final ResourceLocation BLURRED_SUN = new ResourceLocation(Northstar.MOD_ID,"textures/environment/sun_blurry.png");
	private static final ResourceLocation MARS_CLOSE = new ResourceLocation(Northstar.MOD_ID,"textures/environment/mars_close.png");
	//private static final ResourceLocation MARS_FAR = new ResourceLocation(Northstar.MOD_ID,"textures/environment/mars_far.png");
	private static final ResourceLocation MARS_VERY_FAR = new ResourceLocation(Northstar.MOD_ID,"textures/environment/mars_very_far_sky.png");
	private static final ResourceLocation MERCURY_CLOSE = new ResourceLocation(Northstar.MOD_ID,"textures/environment/mercury_close.png");
	private static final ResourceLocation PHOBOS_DEIMOS = new ResourceLocation(Northstar.MOD_ID,"textures/environment/phobos_and_deimos.png");
	private static final ResourceLocation NORTHERN_STAR = new ResourceLocation(Northstar.MOD_ID,"textures/environment/northernstar_sky.png");
	private static final ResourceLocation MARS_DUST = new ResourceLocation(Northstar.MOD_ID,"textures/environment/mars_dust.png");
	private static final ResourceLocation ACID_RAIN = new ResourceLocation(Northstar.MOD_ID,"textures/environment/acid_rain.png");
	private static final ResourceLocation MOON_LOC = new ResourceLocation("textures/environment/moon_phases.png");
	
	
	private static final ResourceLocation CLOUDS_LOCATION = new ResourceLocation("textures/environment/clouds.png");
	
	private static final ResourceLocation SNOW_LOCATION = new ResourceLocation("textures/environment/snow.png");
	
	
	@Nullable
	private VertexBuffer darkBuffer;
	@Shadow
	private VertexBuffer skyBuffer;
	@Shadow
	private VertexBuffer starBuffer;
	private VertexBuffer starBuffer2;
	private VertexBuffer starBuffer3;
	
	@Nullable
	@Shadow
	private VertexBuffer cloudBuffer;
	@Nullable
	private VertexBuffer cloudBuffer2;
	private boolean generateClouds = true;
	private int prevCloudX = Integer.MIN_VALUE;
	private int prevCloudY = Integer.MIN_VALUE;
	private int prevCloudZ = Integer.MIN_VALUE;
	private Vec3 prevCloudColor = Vec3.ZERO;
	@Nullable
	private CloudStatus prevCloudsType;
	
	@Nullable
	@Shadow
	private ClientLevel level;
	@Shadow
	private Minecraft minecraft;
	private float f_alpha = 1;
	private int ticks;
	private int rainSoundTime;
	private double dust_bounce = 0.01;
    float sc = 1;
    
    private static final Vector3f VENUS_DIFFUSE_1 = Util.make(new Vector3f(0.2F, -1.0F, -0.7F), Vector3f::normalize);
    private static final Vector3f VENUS_DIFFUSE_2 = Util.make(new Vector3f(-0.2F, -1.0F, 0.7F), Vector3f::normalize);
	
	
	private final float[] rainSizeX = new float[1024];
	private final float[] rainSizeZ = new float[1024];
	
//	@Inject(method = "renderLevel", at = @At("HEAD"), cancellable = true)
//	public void renderLevel(PoseStack pPoseStack, float pPartialTick, long pFinishNanoTime, boolean pRenderBlockOutline, Camera pCamera, GameRenderer pGameRenderer, LightTexture pLightTexture, Matrix4f pProjectionMatrix, CallbackInfo info) {
//		 if(this.level.dimension() == NorthstarDimensions.VENUS_DIM_KEY)
//		 {RenderSystem.setupLevelDiffuseLighting(VENUS_DIFFUSE_1, VENUS_DIFFUSE_2, pPoseStack.last().pose());}
//	}
	
	@SuppressWarnings("resource")
	@Inject(method = "renderSnowAndRain", at = @At("HEAD"), cancellable = true)
	private void renderWeather(LightTexture pLightTexture, float pPartialTick, double pCamX, double pCamY, double pCamZ, CallbackInfo info) {
		if(this.minecraft != null) {
	        float playerEyeLevel = (float) this.minecraft.player.getEyePosition().y;
	        if (playerEyeLevel > 450) {
	        	info.cancel();
	        	return;
	        }
		}
		ResourceKey<Level> player_dim = Minecraft.getInstance().level.dimension();		
	 	if (player_dim == NorthstarDimensions.MARS_DIM_KEY) {info.cancel();
//		Minecraft.getInstance().level.setRainLevel(15);
	 		float rain_det = this.minecraft.level.getRainLevel(pPartialTick);
	 	      if (!(rain_det <= 0.0F)){
	 	         pLightTexture.turnOnLightLayer();
	 	    	 RenderSystem.setShader(GameRenderer::getPositionColorShader);
	 		 	 RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
	 		 	 RenderSystem.depthMask(false);
	 		 	 RenderSystem.enableBlend();
	 		 	 RenderSystem.defaultBlendFunc();
	 		 	 Camera pCamera = Minecraft.getInstance().gameRenderer.getMainCamera();
	 		 	 GameRenderer gRenderer = Minecraft.getInstance().gameRenderer;
				 Vec3 vec3 = pCamera.getPosition();
	 		 	 double fog_x = vec3.x();
	 		 	 double fog_y = vec3.y();
	 		     FogRenderer.setupColor(pCamera, pPartialTick, this.minecraft.level, this.minecraft.options.getEffectiveRenderDistance(), gRenderer.getDarkenWorldAmount(pPartialTick));
	 		     FogRenderer.levelFogColor();
	 		 	 boolean flag2 = this.minecraft.level.effects().isFoggyAt(Mth.floor(fog_x), Mth.floor(fog_y)) || this.minecraft.gui.getBossOverlay().shouldCreateWorldFog();
	 		 	 FogRenderer.setupFog(pCamera, FogRenderer.FogMode.FOG_TERRAIN, this.minecraft.options.getEffectiveRenderDistance() / 5, flag2, pPartialTick);
	 		 	 
	 		    
	 			 FogRenderer.setupNoFog();
	 			 RenderSystem.depthMask(true);
	 			 RenderSystem.disableBlend();
	 	    	 pLightTexture.turnOnLightLayer();
	 	         Level level = this.minecraft.level;
	 	         int i = Mth.floor(pCamX);
	 	         int j = Mth.floor(pCamY);
	 	         int k = Mth.floor(pCamZ);
	 	         Tesselator tesselator = Tesselator.getInstance();
	 	         BufferBuilder bufferbuilder = tesselator.getBuilder();
	 	         RenderSystem.disableCull();
	 	         RenderSystem.enableBlend();
	 	         RenderSystem.defaultBlendFunc();
	 	         RenderSystem.enableDepthTest();
	 	         int l = 2;
	 	         if (Minecraft.useFancyGraphics()) {
	 	            l = 3;
	 	         }
	 	         

	 	         RenderSystem.depthMask(Minecraft.useShaderTransparency());
	 	         int i1 = -1;
	 	         float f1 = (float)this.ticks + pPartialTick;
	 	         RenderSystem.setShader(GameRenderer::getParticleShader);
	 	         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
	 	         BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
	 	         
	 	         for(int j1 = k - l; j1 <= k + l; ++j1) {
	 	            for(int k1 = i - l; k1 <= i + l; ++k1) {
	 	               int l1 = (j1 - k + 8) * 32 + k1 - i + 8;
	 	               double d0 = (double)this.rainSizeX[l1] * 1.25D;
	 	               double d1 = (double)this.rainSizeZ[l1] * 1.25D;
	 	               blockpos$mutableblockpos.set((double)k1, pCamY, (double)j1);
	 	               Biome biome = level.getBiome(blockpos$mutableblockpos).value();
	 	               if (biome.getPrecipitationAt(blockpos$mutableblockpos) == Biome.Precipitation.NONE) {
	 	                  int i2 = level.getHeight(Heightmap.Types.MOTION_BLOCKING, k1, j1);
	 	                  int j2 = j - l;
	 	                  int k2 = j + l;
	 	                  if (j2 < i2) {
	 	                     j2 = i2;
	 	                  }

	 	                  if (k2 < i2) {
	 	                     k2 = i2;
	 	                  }

	 	                  int l2 = i2;
	 	                  if (i2 < j) {
	 	                     l2 = j;
	 	                  }
	 	                  if (j2 != k2) {
	 	                     RandomSource randomsource = RandomSource.create((long)(k1 * k1 * 3121 + k1 * 45238971 ^ j1 * j1 * 418711 + j1 * 13761));
	 	                     blockpos$mutableblockpos.set(k1, j2, j1);
	 	                     if ((biome.warmEnoughToRain(blockpos$mutableblockpos))) {
	 	                        if (i1 != 0) {
	 	                           if (i1 >= 0) {
	 	                              tesselator.end();
	 	                           }

	 	                           i1 = 0;    
	 	                           RenderSystem.setShaderTexture(0, MARS_DUST);
	 	                           bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
	 	                        }

	 	                        int i3 = this.ticks + k1 * k1 * 3121 + k1 * 45238971 + j1 * j1 * 418711 + j1 * 13761 & 31;
	 	                        float f2 = -((float)i3 + pPartialTick) / 32.0F * (0.75F);
	 	                        //double d2 = (double)k1 + 0.5D - pCamX;
	 	                        //double d4 = (double)j1 + 0.5D - pCamZ;
	 	                       // float f3 = (float)Math.sqrt(d2 * d2 + d4 * d4) / (float)l;
	 	                       // float f4 = ((1.0F - f3 * f3) * 0.5F + 0.5F) * rain_det;
	 	                        if (dust_bounce > 0)
	 	                        {dust_bounce -= 0.01;}
	 	                        else {dust_bounce += 0.01;}
	 	                        blockpos$mutableblockpos.set(k1, l2, j1);
	 	                        RenderSystem.setShaderColor(2.0F, 1.178F, 0.698f, 0.5f);
	 	                        int j3 = 0;
	 	                        bufferbuilder.vertex((double)k1 - pCamX - d0 + -10.5D, (double)k2 - pCamY, (double)j1 - pCamZ - d1 + 10.5D).uv(0.0F + f2, 0).color(1.0F, 1.0F, 1.0F, 0.2f).uv2(j3).endVertex();
	 	                        bufferbuilder.vertex((double)k1 - pCamX + d0 + 10.5D, (double)k2 - pCamY, (double)j1 - pCamZ + d1 + -10.5D).uv(1.0F + f2, (float) (0 + dust_bounce / 2)).color(1.0F, 1.0F, 1.0F, 0.2f).uv2(j3).endVertex();
	 	                        bufferbuilder.vertex((double)k1 - pCamX + d0 + 10.5D, (double)j2 - pCamY, (double)j1 - pCamZ + d1 + -10.5D).uv(1.0F + f2, (float) (1 + dust_bounce / 2)).color(1.0F, 1.0F, 1.0F, 0.2f).uv2(j3).endVertex();
	 	                        bufferbuilder.vertex((double)k1 - pCamX - d0 + -10.5D, (double)j2 - pCamY, (double)j1 - pCamZ - d1 + 10.5D).uv(0.0F + f2, 1).color(1.0F, 1.0F, 1.0F, 0.2f).uv2(j3).endVertex();
	 	                     }
	 	                  }
	 	               }

	 	            }
	 	         }

	 	         if (i1 >= 0) {
	 	            tesselator.end();
	 	         }

	 	         RenderSystem.enableCull();
	 	         RenderSystem.disableBlend();
	 	         pLightTexture.turnOffLightLayer();
	 	         RenderSystem.setShaderColor(1, 1, 1, 1);
	 	    	  
	 	      }
	 		
	 		
	 	}
	 	if (player_dim == NorthstarDimensions.VENUS_DIM_KEY) {info.cancel();
//		Minecraft.getInstance().level.setRainLevel(2);
 		float rain_det = this.minecraft.level.getRainLevel(pPartialTick);
 	      if (!(rain_det <= 0.0F)){
 	         pLightTexture.turnOnLightLayer();
 	         Level level = this.minecraft.level;
 	         int i = Mth.floor(pCamX);
 	         int j = Mth.floor(pCamY);
 	         int k = Mth.floor(pCamZ);
 	         Tesselator tesselator = Tesselator.getInstance();
 	         BufferBuilder bufferbuilder = tesselator.getBuilder();
 	         RenderSystem.disableCull();
 	         RenderSystem.enableBlend();
 	         RenderSystem.defaultBlendFunc();
 	         RenderSystem.enableDepthTest();
 	         int l = 5;
 	         if (Minecraft.useFancyGraphics()) {
 	            l = 10;
 	         }

 	         RenderSystem.depthMask(Minecraft.useShaderTransparency());
 	         int i1 = -1;
 	         RenderSystem.setShader(GameRenderer::getParticleShader);
 	         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
 	         BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

 	         for(int j1 = k - l; j1 <= k + l; ++j1) {
 	            for(int k1 = i - l; k1 <= i + l; ++k1) {
 	               int l1 = (j1 - k + 16) * 32 + k1 - i + 16;
 	               double d0 = (double)this.rainSizeX[l1] * 0.5D;
 	               double d1 = (double)this.rainSizeZ[l1] * 0.5D;
 	               blockpos$mutableblockpos.set((double)k1, pCamY, (double)j1);
 	               Biome biome = level.getBiome(blockpos$mutableblockpos).value();
 	               if (biome.getPrecipitationAt(blockpos$mutableblockpos) == Biome.Precipitation.NONE) {
 	                  int i2 = level.getHeight(Heightmap.Types.MOTION_BLOCKING, k1, j1);
 	                  int j2 = j - l;
 	                  int k2 = j + l;
 	                  if (j2 < i2) {
 	                     j2 = i2;
 	                  }

 	                  if (k2 < i2) {
 	                     k2 = i2;
 	                  }

 	                  int l2 = i2;
 	                  if (i2 < j) {
 	                     l2 = j;
 	                  }

 	                  if (j2 != k2) {
 	                     RandomSource randomsource = RandomSource.create((long)(k1 * k1 * 3121 + k1 * 45238971 ^ j1 * j1 * 418711 + j1 * 13761));
 	                     blockpos$mutableblockpos.set(k1, j2, j1);
 	                     if (biome.warmEnoughToRain(blockpos$mutableblockpos)) {
 	                        if (i1 != 0) {
 	                           if (i1 >= 0) {
 	                              tesselator.end();
 	                           }

 	                           i1 = 0;
 	                           RenderSystem.setShaderTexture(0, ACID_RAIN);
 	                           bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
 	                        }

 	                        int i3 = this.ticks + k1 * k1 * 3121 + k1 * 45238971 + j1 * j1 * 418711 + j1 * 13761 & 31;
 	                        float f2 = -((float)i3 + pPartialTick) / 32.0F * (3.0F + randomsource.nextFloat());
 	                        double d2 = (double)k1 + 0.5D - pCamX;
 	                        double d4 = (double)j1 + 0.5D - pCamZ;
 	                        float f3 = (float)Math.sqrt(d2 * d2 + d4 * d4) / (float)l;
 	                        float f4 = ((1.0F - f3 * f3) * 0.5F + 0.5F) * rain_det;
 	                        blockpos$mutableblockpos.set(k1, l2, j1);
 	                        int j3 = LevelRenderer.getLightColor(level, blockpos$mutableblockpos);
 	                        bufferbuilder.vertex((double)k1 - pCamX - d0 + 0.5D, (double)k2 - pCamY, (double)j1 - pCamZ - d1 + 0.5D).uv(0.0F, (float)j2 * 0.25F + f2).color(1.0F, 1.0F, 1.0F, f4).uv2(j3).endVertex();
 	                        bufferbuilder.vertex((double)k1 - pCamX + d0 + 0.5D, (double)k2 - pCamY, (double)j1 - pCamZ + d1 + 0.5D).uv(1.0F, (float)j2 * 0.25F + f2).color(1.0F, 1.0F, 1.0F, f4).uv2(j3).endVertex();
 	                        bufferbuilder.vertex((double)k1 - pCamX + d0 + 0.5D, (double)j2 - pCamY, (double)j1 - pCamZ + d1 + 0.5D).uv(1.0F, (float)k2 * 0.25F + f2).color(1.0F, 1.0F, 1.0F, f4).uv2(j3).endVertex();
 	                        bufferbuilder.vertex((double)k1 - pCamX - d0 + 0.5D, (double)j2 - pCamY, (double)j1 - pCamZ - d1 + 0.5D).uv(0.0F, (float)k2 * 0.25F + f2).color(1.0F, 1.0F, 1.0F, f4).uv2(j3).endVertex();
 	                     }
 	                  }
 	               }
 	            }
 	         }

 	         if (i1 >= 0) {
 	            tesselator.end();
 	         }

 	         RenderSystem.enableCull();
 	         RenderSystem.disableBlend();
 	         pLightTexture.turnOffLightLayer();
 	      }
 	      }
		
	}
	@SuppressWarnings("resource")
	@Inject(method = "tickRain", at = @At("HEAD"), cancellable = true)
	private void tickRain(Camera pCamera, CallbackInfo info) {
		if(this.minecraft != null) {
	        float playerEyeLevel = (float) this.minecraft.player.getEyePosition().y;
	        if (playerEyeLevel > 450) {
	        	info.cancel();
	        	return;
	        }
		}
		ResourceKey<Level> player_dim = Minecraft.getInstance().level.dimension();		
		
	 	if (player_dim == NorthstarDimensions.MARS_DIM_KEY) {info.cancel();
	 		float rain_det = this.minecraft.level.getRainLevel(3);
	        if (level.random.nextInt(2) == 0 && level.isDay()) {
//	        	System.out.println("tubble weed :)");
	        	BlockPos newpos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, 
	        			new BlockPos(pCamera.getBlockPosition().getX() + level.random.nextIntBetweenInclusive(-64, 64), 0, pCamera.getBlockPosition().getZ() + level.random.nextIntBetweenInclusive(-64, 64)));
	        	level.isClientSide();
	        	level.addAlwaysVisibleParticle(new DustCloudParticleData(), newpos.getX(), newpos.getY() + level.random.nextInt(3), newpos.getZ(), 0, 0, 0);
	        }
	 	      if (!(rain_det <= 0.0F)){	 	    	 
	      if (level.effects().tickRain(level, ticks, pCamera))
	         return;
	      float f = this.minecraft.level.getRainLevel(1.0F) / (Minecraft.useFancyGraphics() ? 1.0F : 2.0F);
	      if (!(f <= 0.0F)) {
	         RandomSource randomsource = RandomSource.create((long)this.ticks * 312987231L);
	         LevelReader levelreader = this.minecraft.level;
	         BlockPos blockpos = new BlockPos(pCamera.getBlockPosition());
	         BlockPos blockpos1 = null;
	         int i = (int)(100.0F * f * f) / (this.minecraft.options.particles().get() == ParticleStatus.DECREASED ? 2 : 1);

	         for(int j = 0; j < i; ++j) {
	            int k = randomsource.nextInt(21) - 10;
	            int l = randomsource.nextInt(21) - 10;
	            BlockPos blockpos2 = levelreader.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, blockpos.offset(k, 0, l));
	            Biome biome = levelreader.getBiome(blockpos2).value();
	            if (blockpos2.getY() > levelreader.getMinBuildHeight() && blockpos2.getY() <= blockpos.getY() + 10 && blockpos2.getY() >= blockpos.getY() - 10 && biome.getPrecipitationAt(blockpos2) == Biome.Precipitation.NONE && biome.warmEnoughToRain(blockpos2)) {
	               blockpos1 = blockpos2.below();
	               if (this.minecraft.options.particles().get() == ParticleStatus.MINIMAL) {
	                  break;
	               }
	               double d0 = randomsource.nextDouble();
	               double d1 = randomsource.nextDouble();
	               BlockState blockstate = levelreader.getBlockState(blockpos1);
	               FluidState fluidstate = levelreader.getFluidState(blockpos1);
	               VoxelShape voxelshape = blockstate.getCollisionShape(levelreader, blockpos1);
	               double d2 = voxelshape.max(Direction.Axis.Y, d0, d1);
	               double d3 = (double)fluidstate.getHeight(levelreader, blockpos1);
	               double d4 = Math.max(d2, d3);
	               if(level.random.nextInt(10) == 0) 
	               {ParticleOptions particleoptions = new DustCloudParticleData();
	               this.minecraft.level.addParticle(particleoptions, (double)blockpos1.getX() + d0, (double)blockpos1.getY() + d4 + level.random.nextInt(4), (double)blockpos1.getZ() + d1, 0.0D, 0.0D, 0.0D);}
	            }
	         }
	         if (blockpos1 != null && randomsource.nextInt(80) < this.rainSoundTime++) {
	            this.rainSoundTime = 0;
	            if (blockpos1.getY() > blockpos.getY() + 1 && levelreader.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, blockpos).getY() > Mth.floor((float)blockpos.getY())) {
	               this.minecraft.level.playLocalSound(blockpos1, NorthstarSounds.MARTIAN_DUST_STORM_ABOVE.get(), SoundSource.WEATHER, 0.5F, 0.5F, false);
	            } else {
	               this.minecraft.level.playLocalSound(blockpos1, NorthstarSounds.MARTIAN_DUST_STORM.get(), SoundSource.WEATHER, 1F, 1.0F, false);
	            }
	         }

	      }
	   }
	 }
	 	if (player_dim == NorthstarDimensions.VENUS_DIM_KEY) {info.cancel();
 		float rain_det = this.minecraft.level.getRainLevel(3);
 	      if (!(rain_det <= 0.0F)){
      if (level.effects().tickRain(level, ticks, pCamera))
         return;
      float f = this.minecraft.level.getRainLevel(1.0F) / (Minecraft.useFancyGraphics() ? 1.0F : 2.0F);
      if (!(f <= 0.0F)) {
         RandomSource randomsource = RandomSource.create((long)this.ticks * 312987231L);
         LevelReader levelreader = this.minecraft.level;
         BlockPos blockpos = new BlockPos(pCamera.getBlockPosition());
         BlockPos blockpos1 = null;
         int i = (int)(100.0F * f * f) / (this.minecraft.options.particles().get() == ParticleStatus.DECREASED ? 2 : 1);

         for(int j = 0; j < i; ++j) {
            int k = randomsource.nextInt(21) - 10;
            int l = randomsource.nextInt(21) - 10;
            BlockPos blockpos2 = levelreader.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, blockpos.offset(k, 0, l));
            Biome biome = levelreader.getBiome(blockpos2).value();
            if (blockpos2.getY() > levelreader.getMinBuildHeight() && blockpos2.getY() <= blockpos.getY() + 10 && blockpos2.getY() >= blockpos.getY() - 10 && biome.getPrecipitationAt(blockpos2) == Biome.Precipitation.NONE && biome.warmEnoughToRain(blockpos2)) {
               blockpos1 = blockpos2.below();
               if (this.minecraft.options.particles().get() == ParticleStatus.MINIMAL) {
                  break;
               }
               double d0 = randomsource.nextDouble();
               double d1 = randomsource.nextDouble();
               BlockState blockstate = levelreader.getBlockState(blockpos1);
               FluidState fluidstate = levelreader.getFluidState(blockpos1);
               VoxelShape voxelshape = blockstate.getCollisionShape(levelreader, blockpos1);
               double d2 = voxelshape.max(Direction.Axis.Y, d0, d1);
               double d3 = (double)fluidstate.getHeight(levelreader, blockpos1);
               double d4 = Math.max(d2, d3);
               ParticleOptions particleoptions = ParticleTypes.SMOKE;
               this.minecraft.level.addParticle(particleoptions, (double)blockpos1.getX() + d0, (double)blockpos1.getY() + d4, (double)blockpos1.getZ() + d1, 0.0D, 0.0D, 0.0D);
            }
         }
         if (blockpos1 != null && randomsource.nextInt(3) < this.rainSoundTime++) {
            this.rainSoundTime = 0;
            if (blockpos1.getY() > blockpos.getY() + 1 && levelreader.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, blockpos).getY() > Mth.floor((float)blockpos.getY())) {
               this.minecraft.level.playLocalSound(blockpos1, SoundEvents.WEATHER_RAIN_ABOVE, SoundSource.WEATHER, 0.1F, 0.5F, false);
            } else {
               this.minecraft.level.playLocalSound(blockpos1, SoundEvents.WEATHER_RAIN, SoundSource.WEATHER, 0.2F, 1.0F, false);
            }
         }

      }
   }
 }
	}
	@Inject(method = "createStars", at = @At("TAIL"), cancellable = true)
	private void createStars(CallbackInfo ci) {
			//stars 2
			Tesselator tesselator2 = Tesselator.getInstance();
			BufferBuilder bufferbuilder2 = tesselator2.getBuilder();
			RenderSystem.setShader(GameRenderer::getPositionShader);
			if (this.starBuffer2 != null) {
				this.starBuffer2.close();
			}

		    this.starBuffer2 = new VertexBuffer(VertexBuffer.Usage.STATIC);
			BufferBuilder.RenderedBuffer bufferbuilder$renderedbuffer2 = this.drawStars2(bufferbuilder2);
			this.starBuffer2.bind();
			this.starBuffer2.upload(bufferbuilder$renderedbuffer2);
			VertexBuffer.unbind();
			//stars 3
			Tesselator tesselator3 = Tesselator.getInstance();
			BufferBuilder bufferbuilder3 = tesselator3.getBuilder();
			RenderSystem.setShader(GameRenderer::getPositionShader);
			if (this.starBuffer3 != null) {
				this.starBuffer3.close();
			}

			this.starBuffer3 = new VertexBuffer(VertexBuffer.Usage.STATIC);
			BufferBuilder.RenderedBuffer bufferbuilder$renderedbuffer3 = this.drawStars3(bufferbuilder3);
			this.starBuffer3.bind();
			this.starBuffer3.upload(bufferbuilder$renderedbuffer3);
			VertexBuffer.unbind();
	}
	private BufferBuilder.RenderedBuffer drawStars2(BufferBuilder pBuilder) {
		RandomSource randomsource = RandomSource.create(92410L);
		pBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
//		System.out.println("big bingus");
		for(int i = 0; i < 2500; ++i) {
			double d0 = (double)(randomsource.nextFloat() * 2.0F - 1.0F);
			double d1 = (double)(randomsource.nextFloat() * 2.0F - 1.0F);
			double d2 = (double)(randomsource.nextFloat() * 2.0F - 1.0F);
			double d3 = (double)(0.15F + randomsource.nextFloat() * 0.1F);
			double d4 = d0 * d0 + d1 * d1 + d2 * d2;
			if (d4 < 1.0D && d4 > 0.01D) {
				d4 = 1.0D / Math.sqrt(d4);
				d0 *= d4;
				d1 *= d4;
				d2 *= d4;
				double d5 = d0 * 100.0D;
				double d6 = d1 * 100.0D;
				double d7 = d2 * 100.0D;
				double d8 = Math.atan2(d0, d2);
				double d9 = Math.sin(d8);
				double d10 = Math.cos(d8);
				double d11 = Math.atan2(Math.sqrt(d0 * d0 + d2 * d2), d1);
				double d12 = Math.sin(d11);
				double d13 = Math.cos(d11);
				double d14 = randomsource.nextDouble() * Math.PI * 2.0D;
				double d15 = Math.sin(d14);
				double d16 = Math.cos(d14);
				
				for(int j = 0; j < 4; ++j) {
					double d18 = (double)((j & 2) - 1) * d3;
					double d19 = (double)((j + 1 & 2) - 1) * d3;
					double d21 = d18 * d16 - d19 * d15;
					double d22 = d19 * d16 + d18 * d15;
					double d23 = d21 * d12 + 0.0D * d13;
					double d24 = 0.0D * d12 - d21 * d13;
					double d25 = d24 * d9 - d22 * d10;
					double d26 = d22 * d9 + d24 * d10;
					pBuilder.vertex(d5 + d25, d6 + d23, d7 + d26).endVertex();
				}
			}
		}
		
		return pBuilder.end();
	}
	private BufferBuilder.RenderedBuffer drawStars3(BufferBuilder pBuilder) {
		RandomSource randomsource = RandomSource.create(64094L);
		pBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
		for(int i = 0; i < 1800; ++i) {
			double d0 = (double)(randomsource.nextFloat() * 2.0F - 1.0F);
			double d1 = (double)(randomsource.nextFloat() * 2.0F - 1.0F);
			double d2 = (double)(randomsource.nextFloat() * 2.0F - 1.0F);
			double d3 = (double)(0.15F + randomsource.nextFloat() * 0.1F);
			double d4 = d0 * d0 + d1 * d1 + d2 * d2;
			if (d4 < 1.0D && d4 > 0.01D) {
				d4 = 1.0D / Math.sqrt(d4);
				d0 *= d4;
				d1 *= d4;
				d2 *= d4;
				double d5 = d0 * 100.0D;
				double d6 = d1 * 100.0D;
				double d7 = d2 * 100.0D;
				double d8 = Math.atan2(d0, d2);
				double d9 = Math.sin(d8);
				double d10 = Math.cos(d8);
				double d11 = Math.atan2(Math.sqrt(d0 * d0 + d2 * d2), d1);
				double d12 = Math.sin(d11);
				double d13 = Math.cos(d11);
				double d14 = randomsource.nextDouble() * Math.PI * 2.0D;
				double d15 = Math.sin(d14);
				double d16 = Math.cos(d14);
				
				for(int j = 0; j < 4; ++j) {
					double d18 = (double)((j & 2) - 1) * d3;
					double d19 = (double)((j + 1 & 2) - 1) * d3;
					double d21 = d18 * d16 - d19 * d15;
					double d22 = d19 * d16 + d18 * d15;
					double d23 = d21 * d12 + 0.0D * d13;
					double d24 = 0.0D * d12 - d21 * d13;
					double d25 = d24 * d9 - d22 * d10;
					double d26 = d22 * d9 + d24 * d10;
					pBuilder.vertex(d5 + d25, d6 + d23, d7 + d26).endVertex();
				}
			}
		}
		
		return pBuilder.end();
	}
	
	@SuppressWarnings("resource")
	@Inject(method = "renderSky", at = @At("HEAD"), cancellable = true)
    private void renderSky(PoseStack pPoseStack, Matrix4f pProjectionMatrix, float pPartialTick, Camera camera, boolean thing, Runnable runnable, CallbackInfo info) {
		ResourceKey<Level> player_dim = Minecraft.getInstance().level.dimension();
		if(this.minecraft != null) {
 		float rain_det = 0;
    	if (player_dim == NorthstarDimensions.MARS_DIM_KEY)
    	{info.cancel();
        runnable.run();
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        Vec3 vec3 = this.level.getSkyColor(this.minecraft.gameRenderer.getMainCamera().getPosition(), pPartialTick);    
        float playerEyeLevel = (float) this.minecraft.player.getEyePosition(pPartialTick).y;
        float f = (float)vec3.x;
        float f1 = (float)vec3.y;
        float f2 = (float)vec3.z;

        if (playerEyeLevel > 400)
        {f = (float) (vec3.x - ((playerEyeLevel - 400) / 300));
        f1 = (float) (vec3.y - ((playerEyeLevel - 400) / 300));
        f2 = (float) (vec3.z - ((playerEyeLevel - 400) / 300));
        f_alpha = 1 - ((playerEyeLevel - 400) / 300);
        if (f_alpha < 0) {f_alpha = 0;}}
        	//f_alpha = 700 - playerEyeLevel / 200;
        else {f_alpha = 1;}
        RenderSystem.depthMask(false);
        RenderSystem.setShaderColor(f, f1, f2, f_alpha);
        ShaderInstance shaderinstance = RenderSystem.getShader();
        this.skyBuffer.bind();
        this.skyBuffer.drawWithShader(pPoseStack.last().pose(), pProjectionMatrix, shaderinstance);
        VertexBuffer.unbind();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        float SUN = 20.0F;
        float PD = 15f;
        if (!(rain_det <= 0.0F))
        {if(sc > 0.45)
        {sc -=0.01;}}
        else 
        {if(!(sc<=1)) 
        {sc += 0.01;}}
        RenderSystem.setShaderColor(sc, sc, sc, 1);
        
        VertexBuffer.unbind();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        float[] afloat = this.level.effects().getSunriseColor(this.level.getTimeOfDay(pPartialTick), pPartialTick);
        if (afloat != null) {
           RenderSystem.setShader(GameRenderer::getPositionColorShader);
           RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
           pPoseStack.pushPose();
           pPoseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
           float f3 = Mth.sin(this.level.getSunAngle(pPartialTick)) < 0.0F ? 180.0F : 0.0F;
           pPoseStack.mulPose(Axis.ZP.rotationDegrees(f3));
           pPoseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
           float f4 = afloat[0];
           float f5 = afloat[1];
           float f6 = afloat[2];
           Matrix4f matrix4f = pPoseStack.last().pose();
           bufferbuilder.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
           bufferbuilder.vertex(matrix4f, 0.0F, 100.0F, 0.0F).color(f4, f5, f6, afloat[3]).endVertex();

           for(int j = 0; j <= 16; ++j) {
              float f7 = (float)j * ((float)Math.PI * 2F) / 16.0F;
              float f8 = Mth.sin(f7);
              float f9 = Mth.cos(f7);
              bufferbuilder.vertex(matrix4f, f8 * 120.0F, f9 * 120.0F, -f9 * 40.0F * afloat[3]).color(afloat[0], afloat[1], afloat[2], 0.0F).endVertex();
           }

           BufferUploader.drawWithShader(bufferbuilder.end());
           pPoseStack.popPose();
        }
        
        pPoseStack.pushPose();
        pPoseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
        pPoseStack.mulPose(Axis.XP.rotationDegrees(this.level.getTimeOfDay(pPartialTick) * 360.0F));
        
        Matrix4f matrix4f1 = pPoseStack.last().pose();
 		float rain_det2 = this.minecraft.level.getRainLevel(pPartialTick);
        float f10 = 2;
        if (!(rain_det2 <=0) && playerEyeLevel <= 450)
        {f10 = 0;}
        if (f10 > 0.0F) {
            RenderSystem.setShaderColor(f10, f10, f10, f10);
            FogRenderer.setupNoFog();
            this.starBuffer.bind();
            this.starBuffer.drawWithShader(pPoseStack.last().pose(), pProjectionMatrix, GameRenderer.getPositionShader());
            VertexBuffer.unbind();
            RenderSystem.setShaderColor(f10, f10, f10, 0.67F);
            this.starBuffer2.bind();
            this.starBuffer2.drawWithShader(pPoseStack.last().pose(), pProjectionMatrix, GameRenderer.getPositionShader());
            VertexBuffer.unbind();
            
            RenderSystem.setShaderColor(f10, f10, f10, 0.33F);
            this.starBuffer3.bind();
            this.starBuffer3.drawWithShader(pPoseStack.last().pose(), pProjectionMatrix, GameRenderer.getPositionShader());
            VertexBuffer.unbind();
            
            runnable.run();
        }
        

        float sky_brightness = (float) (this.level.getStarBrightness(pPartialTick) * 1.5) * (this.level.isRaining() && playerEyeLevel < 450 ? 0 : 1);

        float f11 = 1.0F - this.level.getRainLevel(pPartialTick);
        if(playerEyeLevel > 400) {
      	  f11 = (f11 + ((playerEyeLevel - 400) / 200));
      	  f11 = Mth.clamp(f11, 0, 1);
        }
        
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, f11);
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, PHOBOS_DEIMOS);
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(matrix4f1, 10 +  PD, -100.0F, 5 + -PD).uv(0.0F, 0.0F).endVertex();
        bufferbuilder.vertex(matrix4f1, 10 + -PD, -100.0F, 5 + -PD).uv(1.0F, 0.0F).endVertex();
        bufferbuilder.vertex(matrix4f1, 10 + -PD, -100.0F, 5 + PD).uv(1.0F, 1.0F).endVertex();
        bufferbuilder.vertex(matrix4f1, 10 +  PD, -100.0F, 5 + PD).uv(0.0F, 1.0F).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());	
        
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BARE_SUN);
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(matrix4f1,  SUN, 100.0F, SUN).uv(0, 0).endVertex();
        bufferbuilder.vertex(matrix4f1, -SUN, 100.0F, SUN).uv(1, 0).endVertex();
        bufferbuilder.vertex(matrix4f1, -SUN, 100.0F, -SUN).uv(1, 1).endVertex();
        bufferbuilder.vertex(matrix4f1,  SUN, 100.0F, -SUN).uv(0, 1).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());

        RenderSystem.setShaderColor(sky_brightness, sky_brightness, sky_brightness, 1);
   
        float EF = 3;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, EARTH_FAR);
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(matrix4f1, 100, -40f +  EF, 50 +  EF).uv(0, 0).endVertex();
        bufferbuilder.vertex(matrix4f1, 100,  -39 +  EF, 50 + -EF).uv(1, 0).endVertex();
        bufferbuilder.vertex(matrix4f1, 100,  -39 + -EF, 50 + -EF).uv(1, -1).endVertex();
        bufferbuilder.vertex(matrix4f1, 100, -40f + -EF, 50 +  EF).uv(0, -1).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
        
        float MF = 1.5F;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, MOON_FAR);
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(matrix4f1, 100, -40f +  MF, 55 +  MF).uv(0, 0).endVertex();
        bufferbuilder.vertex(matrix4f1, 100,  -39.5F +  MF, 55 + -MF).uv(1, 0).endVertex();
        bufferbuilder.vertex(matrix4f1, 100,  -39.5F + -MF, 55 + -MF).uv(1, -1).endVertex();
        bufferbuilder.vertex(matrix4f1, 100, -40f + -MF, 55 +  MF).uv(0, -1).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
        
        float NS = 2.0F;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, NORTHERN_STAR);
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(matrix4f1, -100,  -30 + NS, -NS).uv(0.0F, 0.0F).endVertex();
        bufferbuilder.vertex(matrix4f1, -100, -30 + NS,   NS).uv(1.0F, 0.0F).endVertex();
        bufferbuilder.vertex(matrix4f1, -100, -30 + -NS,   NS).uv(1.0F, 1.0F).endVertex();
        bufferbuilder.vertex(matrix4f1, -100,  -30 + -NS, -NS).uv(0.0F, 1.0F).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());	
        
        float VF = 2;
        RenderSystem.setShaderTexture(0, VENUS_FAR);
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(matrix4f1, -58.75F, -30f +  VF, -80 +  -VF).uv(0, 0).endVertex();
        bufferbuilder.vertex(matrix4f1, -60,  -29.25f +  VF, -80 +  VF).uv(1, 0).endVertex();
        bufferbuilder.vertex(matrix4f1, -60,  -29.25f + -VF, -80 +  VF).uv(1, -1).endVertex();
        bufferbuilder.vertex(matrix4f1, -58.75F, -30f + -VF, -80 +  -VF).uv(0, -1).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
	    RenderSystem.depthMask(true);
        pPoseStack.popPose();
        pPoseStack.pushPose();
        pPoseStack.mulPose(Axis.YP.rotationDegrees(0));
        pPoseStack.mulPose(Axis.XP.rotationDegrees(0));
        float mars_alpha = (playerEyeLevel - 400) / 100;
        float mars_dist = (playerEyeLevel - 400) / 10;
        if (playerEyeLevel > 400) {
            float MC = 1500;
            if (playerEyeLevel > 650) {RenderSystem.disableBlend();}else{RenderSystem.enableBlend();}
            Matrix4f matrix4f2 = pPoseStack.last().pose();
            RenderSystem.setShaderColor(1, 1, 1, mars_alpha);
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, MARS_CLOSE);
            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            bufferbuilder.vertex(matrix4f2, MC, -100.0F - mars_dist, -MC).uv(0.0F, 0.0F).endVertex();
            bufferbuilder.vertex(matrix4f2, -MC, -100.0F - mars_dist, -MC).uv(1.0F, 0.0F).endVertex();
            bufferbuilder.vertex(matrix4f2, -MC, -100.0F - mars_dist,  MC).uv(1.0F, 1.0F).endVertex();
            bufferbuilder.vertex(matrix4f2, MC, -100.0F - mars_dist,  MC).uv(0.0F, 1.0F).endVertex();
            BufferUploader.drawWithShader(bufferbuilder.end());
        }
        RenderSystem.setShaderColor(1, 1, 1, 1);
	    RenderSystem.depthMask(true);
        RenderSystem.enableBlend();
        pPoseStack.popPose();
      }
      if (player_dim == Level.OVERWORLD) {
      float playerEyeLevel = (float) this.minecraft.player.getEyePosition(pPartialTick).y;
      if (playerEyeLevel > 450){
    	  info.cancel();
          runnable.run();
          BufferBuilder bufferbuilder3 = Tesselator.getInstance().getBuilder();
          Vec3 vec3 = this.level.getSkyColor(this.minecraft.gameRenderer.getMainCamera().getPosition(), pPartialTick);    
          float f = (float)vec3.x;
          float f1 = (float)vec3.y;
          float f2 = (float)vec3.z;

          if (playerEyeLevel > 450)
          {f = (float) (vec3.x - ((playerEyeLevel - 450) / 300));
          f1 = (float) (vec3.y - ((playerEyeLevel - 450) / 300));
          f2 = (float) (vec3.z - ((playerEyeLevel - 450) / 300));
          f_alpha = 1 - ((playerEyeLevel - 450) / 300);
          if (f_alpha < 0) {f_alpha = 0;}}
          	//f_alpha = 700 - playerEyeLevel / 200;
          else {f_alpha = 1;}
          RenderSystem.depthMask(false);
          RenderSystem.setShaderColor(f, f1, f2, f_alpha);
          ShaderInstance shaderinstance = RenderSystem.getShader();
          RenderSystem.enableBlend();
          RenderSystem.defaultBlendFunc();
          this.skyBuffer.bind();
          this.skyBuffer.drawWithShader(pPoseStack.last().pose(), pProjectionMatrix, shaderinstance);
          VertexBuffer.unbind();
          
          pPoseStack.pushPose();
          pPoseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
          pPoseStack.mulPose(Axis.XP.rotationDegrees(this.level.getTimeOfDay(pPartialTick) * 360.0F));
          Matrix4f matrix4f3 = pPoseStack.last().pose();
          float starBrightness;
          float starBrightness2;
          float starBrightness3;
          float f10 = this.level.getStarBrightness(pPartialTick);
          if (f10 > 0) {starBrightness = -(f10 - (playerEyeLevel - 300) / 100);}
          else {starBrightness = (playerEyeLevel - 450) / 200;}
          if (f10 > 0) {starBrightness2 = ((playerEyeLevel - 450) / 200);}
          else {starBrightness2 = (playerEyeLevel - 600) / 200;}
          if (f10 > 0) {starBrightness3 = ((playerEyeLevel - 600) / 300);}
          else {starBrightness3 = (playerEyeLevel - 700) / 200;}
          starBrightness2 = Mth.clamp(starBrightness2, 0, 0.67F);
          starBrightness3 = Mth.clamp(starBrightness2, 0, 0.33F);
          
//          System.out.println(starBrightness);

          if (starBrightness > 0.0F) {
              RenderSystem.setShaderColor(starBrightness, starBrightness, starBrightness, starBrightness / 2);
              FogRenderer.setupNoFog();
              this.starBuffer.bind();
              this.starBuffer.drawWithShader(pPoseStack.last().pose(), pProjectionMatrix, GameRenderer.getPositionShader());
              VertexBuffer.unbind();
              
              RenderSystem.setShaderColor(starBrightness, starBrightness, starBrightness, starBrightness2);
              this.starBuffer2.bind();
              this.starBuffer2.drawWithShader(pPoseStack.last().pose(), pProjectionMatrix, GameRenderer.getPositionShader());
              VertexBuffer.unbind();
              
              RenderSystem.setShaderColor(starBrightness, starBrightness, starBrightness, starBrightness3);
              this.starBuffer3.bind();
              this.starBuffer3.drawWithShader(pPoseStack.last().pose(), pProjectionMatrix, GameRenderer.getPositionShader());
              VertexBuffer.unbind();
              runnable.run();
          }
          
          
          BufferBuilder bufferbuilder_earth_sky = Tesselator.getInstance().getBuilder();
          Matrix4f matrix4f_earth_sky = pPoseStack.last().pose();
          float earth_sky_planet_brightness = (float) (this.level.getStarBrightness(pPartialTick) * 1.5) * (this.level.isRaining() && playerEyeLevel < 450 ? 0 : 1);
          float northstar_brightness = earth_sky_planet_brightness * 2;
       
          RenderSystem.enableBlend();
          RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
          RenderSystem.depthMask(true);
          RenderSystem.setShader(GameRenderer::getPositionTexShader);
          RenderSystem.enableBlend();
          RenderSystem.defaultBlendFunc();
       
          float NS = 2.0F;
          RenderSystem.setShaderColor(northstar_brightness, northstar_brightness, northstar_brightness, northstar_brightness);
          RenderSystem.setShader(GameRenderer::getPositionTexShader);
          RenderSystem.setShaderTexture(0, NORTHERN_STAR);
          bufferbuilder_earth_sky.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
          bufferbuilder_earth_sky.vertex(matrix4f_earth_sky, -100,  -30 + NS, -NS).uv(0.0F, 0.0F).endVertex();
          bufferbuilder_earth_sky.vertex(matrix4f_earth_sky, -100, -30 + NS,   NS).uv(1.0F, 0.0F).endVertex();
          bufferbuilder_earth_sky.vertex(matrix4f_earth_sky, -100, -30 + -NS,   NS).uv(1.0F, 1.0F).endVertex();
          bufferbuilder_earth_sky.vertex(matrix4f_earth_sky, -100,  -30 + -NS, -NS).uv(0.0F, 1.0F).endVertex();
          BufferUploader.drawWithShader(bufferbuilder_earth_sky.end());	
    	 
          if (playerEyeLevel >= 450) {
        	  RenderSystem.setShaderColor(1, 1, 1, 1);//System.out.println("we gamin");
          }
          else {RenderSystem.setShaderColor(earth_sky_planet_brightness, earth_sky_planet_brightness, earth_sky_planet_brightness, earth_sky_planet_brightness);}

       
          float VF = 2;
          RenderSystem.setShaderTexture(0, VENUS_FAR);
          bufferbuilder_earth_sky.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
          bufferbuilder_earth_sky.vertex(matrix4f_earth_sky, 100, -40f +  VF, 50 +  VF).uv(0, 0).endVertex();
          bufferbuilder_earth_sky.vertex(matrix4f_earth_sky, 100,  -39.4f +  VF, 50 + -VF).uv(1, 0).endVertex();
          bufferbuilder_earth_sky.vertex(matrix4f_earth_sky, 100,  -39.4f + -VF, 50 + -VF).uv(1, -1).endVertex();
          bufferbuilder_earth_sky.vertex(matrix4f_earth_sky, 100, -40f + -VF, 50 +  VF).uv(0, -1).endVertex();
          BufferUploader.drawWithShader(bufferbuilder_earth_sky.end());
       
          float MVF = 1;
          RenderSystem.setShaderTexture(0, MARS_VERY_FAR);
          bufferbuilder_earth_sky.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
          bufferbuilder_earth_sky.vertex(matrix4f_earth_sky, -59.25f, -30f +  MVF, -80 +  -MVF).uv(0, 0).endVertex();
          bufferbuilder_earth_sky.vertex(matrix4f_earth_sky, -60,  -29.65f +  MVF, -80 +  MVF).uv(1, 0).endVertex();
          bufferbuilder_earth_sky.vertex(matrix4f_earth_sky, -60,  -29.65f + -MVF, -80 +  MVF).uv(1, -1).endVertex();
          bufferbuilder_earth_sky.vertex(matrix4f_earth_sky, -59.25f, -30f + -MVF, -80 +  -MVF).uv(0, -1).endVertex();
          BufferUploader.drawWithShader(bufferbuilder_earth_sky.end());
          RenderSystem.disableBlend();
          RenderSystem.depthMask(true);
          RenderSystem.enableBlend();
        
          
          RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
          float f11 = 1.0F - this.level.getRainLevel(pPartialTick);
          if(playerEyeLevel > 400) {
        	  f11 = (f11 + ((playerEyeLevel - 400) / 200));
        	  f11 = Mth.clamp(f11, 0, 1);
          }
          RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, f11);
          float f12 = 30.0F;
          RenderSystem.setShader(GameRenderer::getPositionTexShader);
          RenderSystem.setShaderTexture(0, BARE_SUN);
          bufferbuilder3.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
          bufferbuilder3.vertex(matrix4f3, -f12, 100.0F, -f12).uv(0.0F, 0.0F).endVertex();
          bufferbuilder3.vertex(matrix4f3, f12, 100.0F, -f12).uv(1.0F, 0.0F).endVertex();
          bufferbuilder3.vertex(matrix4f3, f12, 100.0F, f12).uv(1.0F, 1.0F).endVertex();
          bufferbuilder3.vertex(matrix4f3, -f12, 100.0F, f12).uv(0.0F, 1.0F).endVertex();
          BufferUploader.drawWithShader(bufferbuilder3.end());
          f12 = 20.0F;
          RenderSystem.setShaderTexture(0, MOON_LOC);
          int k = this.level.getMoonPhase();
          int l = k % 4;
          int i1 = k / 4 % 2;
          float f13 = (float)(l + 0) / 4.0F;
          float f14 = (float)(i1 + 0) / 2.0F;
          float f15 = (float)(l + 1) / 4.0F;
          float f16 = (float)(i1 + 1) / 2.0F;
          bufferbuilder3.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
          bufferbuilder3.vertex(matrix4f3, -f12, -100.0F, f12).uv(f15, f16).endVertex();
          bufferbuilder3.vertex(matrix4f3, f12, -100.0F, f12).uv(f13, f16).endVertex();
          bufferbuilder3.vertex(matrix4f3, f12, -100.0F, -f12).uv(f13, f14).endVertex();
          bufferbuilder3.vertex(matrix4f3, -f12, -100.0F, -f12).uv(f15, f14).endVertex();
          BufferUploader.drawWithShader(bufferbuilder3.end());
          RenderSystem.enableDepthTest();
          pPoseStack.popPose();
          float earth_alpha = (playerEyeLevel - 450) / 300;
          float earth_dist = (playerEyeLevel - 450) / 10;
          if (playerEyeLevel > 450) {
        	  if (earth_alpha >= 1) {RenderSystem.disableBlend();}else{RenderSystem.enableBlend();}	
        	  float EC = 2000;
        	  pPoseStack.pushPose();
        	  pPoseStack.mulPose(Axis.YP.rotationDegrees(0));
        	  pPoseStack.mulPose(Axis.XP.rotationDegrees(0));
        	  Matrix4f matrix4f2 = pPoseStack.last().pose();
        	  BufferBuilder bufferbuilder2 = Tesselator.getInstance().getBuilder();
        	  RenderSystem.setShaderColor(1, 1, 1, earth_alpha);
        	  RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        	  RenderSystem.setShader(GameRenderer::getPositionTexShader);
        	  RenderSystem.setShaderTexture(0, EARTH_CLOSE);
        	  bufferbuilder2.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        	  bufferbuilder2.vertex(matrix4f2, EC, -100.0F - earth_dist, -EC).uv(0.0F, 0.0F).endVertex();
        	  bufferbuilder2.vertex(matrix4f2, -EC, -100.0F - earth_dist,-EC).uv(1.0F, 0.0F).endVertex();
        	  bufferbuilder2.vertex(matrix4f2, -EC, -100.0F - earth_dist, EC).uv(1.0F, -1.0F).endVertex();
        	  bufferbuilder2.vertex(matrix4f2, EC, -100.0F - earth_dist,  EC).uv(0.0F, -1.0F).endVertex();
          	  BufferUploader.drawWithShader(bufferbuilder2.end());}
          RenderSystem.depthMask(true);
          RenderSystem.enableBlend();      
          RenderSystem.setShaderColor(1, 1, 1, 1);
          pPoseStack.popPose();
          }

        
      }
    	if (player_dim == NorthstarDimensions.VENUS_DIM_KEY)
    	{info.cancel();
        runnable.run();
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        float time = this.level.getTimeOfDay(pPartialTick);
        float skydarken = Mth.cos(time * ((float)Math.PI * 2F)) * 2.0F + 0.5F;
        Vec3 skycolor = new Vec3(1F, 0.874F, 0.336F);
        skydarken = Mth.clamp(skydarken, 0.125F, 1.0F);
        float playerEyeLevel = (float) this.minecraft.player.getEyePosition(pPartialTick).y;
        float f = (float)skycolor.x * skydarken;
        float f1 = (float)skycolor.y * skydarken;
        float f2 = (float)skycolor.z * skydarken;
 //       System.out.println(time);

        if (playerEyeLevel > 600)
        {f = (float) (skycolor.x * skydarken - ((playerEyeLevel - 600) / 300));
        f1 = (float) (skycolor.y * skydarken - ((playerEyeLevel - 600) / 300));
        f2 = (float) (skycolor.z * skydarken - ((playerEyeLevel - 600) / 300));
        f_alpha = 1 - ((playerEyeLevel - 600) / 300);
        if (f_alpha < 0) {f_alpha = 0;}}
        	//f_alpha = 700 - playerEyeLevel / 200;
        else {f_alpha = 1;}
        RenderSystem.depthMask(false);
        RenderSystem.setShaderColor(f, f1, f2, f_alpha);
        ShaderInstance shaderinstance = RenderSystem.getShader();
        this.skyBuffer.bind();
        this.skyBuffer.drawWithShader(pPoseStack.last().pose(), pProjectionMatrix, shaderinstance);
        VertexBuffer.unbind();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        float SUN = 30.0F;
        if (!(rain_det <= 0.0F))
        {if(sc > 0.45)
        {sc -=0.01;}}
        else 
        {if(!(sc<=1)) 
        {sc += 0.01;}}
        RenderSystem.setShaderColor(sc, sc, sc, 1);
        
        VertexBuffer.unbind();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        float[] afloat = this.level.effects().getSunriseColor(this.level.getTimeOfDay(pPartialTick), pPartialTick);
        if (afloat != null) {
           RenderSystem.setShader(GameRenderer::getPositionColorShader);
           RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
           pPoseStack.pushPose();
           pPoseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
           float f3 = Mth.sin(this.level.getSunAngle(pPartialTick)) < 0.0F ? 180.0F : 0.0F;
           pPoseStack.mulPose(Axis.ZP.rotationDegrees(f3));
           pPoseStack.mulPose(Axis.ZP.rotationDegrees(-90.0F));
           float f4 = afloat[0];
           float f5 = afloat[1];
           float f6 = afloat[2];
           Matrix4f matrix4f = pPoseStack.last().pose();
           bufferbuilder.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
           bufferbuilder.vertex(matrix4f, 0.0F, 100.0F, 0.0F).color(f4, f5, f6, afloat[3]).endVertex();

           for(int j = 0; j <= 16; ++j) {
              float f7 = (float)j * ((float)Math.PI * 2F) / 16.0F;
              float f8 = Mth.sin(f7);
              float f9 = Mth.cos(f7);
              bufferbuilder.vertex(matrix4f, f8 * 120.0F, f9 * 120.0F, -f9 * 40.0F * afloat[3]).color(afloat[0], afloat[1], afloat[2], 0.0F).endVertex();
           }
           BufferUploader.drawWithShader(bufferbuilder.end());
           pPoseStack.popPose();
        }
        
        pPoseStack.pushPose();
        pPoseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
        pPoseStack.mulPose(Axis.XP.rotationDegrees(this.level.getTimeOfDay(pPartialTick) * 360.0F));
        
        Matrix4f matrix4f1 = pPoseStack.last().pose();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
    	
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.25F);
        RenderSystem.setShaderTexture(0, BLURRED_SUN);
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(matrix4f1,  SUN, 100.0F, SUN).uv(0, 0).endVertex();
        bufferbuilder.vertex(matrix4f1, -SUN, 100.0F, SUN).uv(1, 0).endVertex();
        bufferbuilder.vertex(matrix4f1, -SUN, 100.0F, -SUN).uv(1, 1).endVertex();
        bufferbuilder.vertex(matrix4f1,  SUN, 100.0F, -SUN).uv(0, 1).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
        float sun_alpha = (playerEyeLevel - 600) / 300;
        sun_alpha = Mth.clamp(sun_alpha, 0, 1);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, sun_alpha);
        RenderSystem.setShaderTexture(0, BARE_SUN);
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(matrix4f1,  SUN, 100.0F, SUN).uv(0, 0).endVertex();
        bufferbuilder.vertex(matrix4f1, -SUN, 100.0F, SUN).uv(1, 0).endVertex();
        bufferbuilder.vertex(matrix4f1, -SUN, 100.0F, -SUN).uv(1, 1).endVertex();
        bufferbuilder.vertex(matrix4f1,  SUN, 100.0F, -SUN).uv(0, 1).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
        
        float f11 = 0.5F - this.level.getRainLevel(pPartialTick);
        float f10 = this.level.getStarBrightness(pPartialTick) * f11;
        float starHeight = (playerEyeLevel - 600) / 300;
 //       System.out.println(starHeight + "BRUH");
        float starBrightness;
        float starBrightness2;
        float starBrightness3;
        if (!(playerEyeLevel <= 600))
        {f10 += starHeight;} 
        f10 = Mth.clamp(f10, 0, 2);
        if (f10 > 0) {starBrightness = ((playerEyeLevel - 650) / 100);}
        else {starBrightness = (playerEyeLevel - 675) / 200;}
        if (f10 > 0) {starBrightness2 = ((playerEyeLevel - 750) / 200);}
        else {starBrightness2 = (playerEyeLevel - 750) / 200;}
        if (f10 > 0) {starBrightness3 = ((playerEyeLevel - 850) / 300);}
        else {starBrightness3 = (playerEyeLevel - 850) / 200;}
        starBrightness2 = Mth.clamp(starBrightness2, 0, 0.67F);
        starBrightness3 = Mth.clamp(starBrightness2, 0, 0.33F);
//        System.out.println(starBrightness);
        if (f10 > 0.0F) {
            RenderSystem.setShaderColor(starBrightness, starBrightness, starBrightness, starBrightness);
            FogRenderer.setupNoFog();
            this.starBuffer.bind();
            this.starBuffer.drawWithShader(pPoseStack.last().pose(), pProjectionMatrix, GameRenderer.getPositionShader());
            VertexBuffer.unbind();
            
            RenderSystem.setShaderColor(starBrightness, starBrightness, starBrightness, starBrightness2);
            this.starBuffer2.bind();
            this.starBuffer2.drawWithShader(pPoseStack.last().pose(), pProjectionMatrix, GameRenderer.getPositionShader());
            VertexBuffer.unbind();
            
            RenderSystem.setShaderColor(starBrightness, starBrightness, starBrightness, starBrightness3);
            this.starBuffer3.bind();
            this.starBuffer3.drawWithShader(pPoseStack.last().pose(), pProjectionMatrix, GameRenderer.getPositionShader());
            VertexBuffer.unbind();
            runnable.run();
            runnable.run();
        }
        float planetBrightness = Mth.clamp(starBrightness, 0, 1);
        RenderSystem.setShaderColor(planetBrightness, planetBrightness, planetBrightness, planetBrightness);
        float EF = 3;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, EARTH_FAR);
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(matrix4f1, 100, -40f +  EF, 50 +  EF).uv(0, 0).endVertex();
        bufferbuilder.vertex(matrix4f1, 100,  -39 +  EF, 50 + -EF).uv(1, 0).endVertex();
        bufferbuilder.vertex(matrix4f1, 100,  -39 + -EF, 50 + -EF).uv(1, -1).endVertex();
        bufferbuilder.vertex(matrix4f1, 100, -40f + -EF, 50 +  EF).uv(0, -1).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
        
        float NS = 2.0F;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, NORTHERN_STAR);
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(matrix4f1, -100,  -30 + NS, -NS).uv(0.0F, 0.0F).endVertex();
        bufferbuilder.vertex(matrix4f1, -100, -30 + NS,   NS).uv(1.0F, 0.0F).endVertex();
        bufferbuilder.vertex(matrix4f1, -100, -30 + -NS,   NS).uv(1.0F, 1.0F).endVertex();
        bufferbuilder.vertex(matrix4f1, -100,  -30 + -NS, -NS).uv(0.0F, 1.0F).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());	
        
        float MVF = 1;
        RenderSystem.setShaderTexture(0, MARS_VERY_FAR);
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(matrix4f1, -59.25f, -30f +  MVF, -80 +  -MVF).uv(0, 0).endVertex();
        bufferbuilder.vertex(matrix4f1, -60,  -29.65f +  MVF, -80 +  MVF).uv(1, 0).endVertex();
        bufferbuilder.vertex(matrix4f1, -60,  -29.65f + -MVF, -80 +  MVF).uv(1, -1).endVertex();
        bufferbuilder.vertex(matrix4f1, -59.25f, -30f + -MVF, -80 +  -MVF).uv(0, -1).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
	    RenderSystem.depthMask(true);
        pPoseStack.popPose();
        pPoseStack.pushPose();
        pPoseStack.mulPose(Axis.YP.rotationDegrees(0));
        pPoseStack.mulPose(Axis.XP.rotationDegrees(0));
        float venus_alpha = (playerEyeLevel - 600) / 150;
        float venus_dist = (playerEyeLevel - 600) / 10;
        if (playerEyeLevel > 600) {
            float VC = 2000;
            if (playerEyeLevel > 750) {RenderSystem.disableBlend();}else{RenderSystem.enableBlend();}
            Matrix4f matrix4f2 = pPoseStack.last().pose();
            RenderSystem.setShaderColor(1, 1, 1, venus_alpha);
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, VENUS_CLOSE);
            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            bufferbuilder.vertex(matrix4f2, VC, -100.0F - venus_dist, -VC).uv(0.0F, 0.0F).endVertex();
            bufferbuilder.vertex(matrix4f2, -VC, -100.0F - venus_dist, -VC).uv(1.0F, 0.0F).endVertex();
            bufferbuilder.vertex(matrix4f2, -VC, -100.0F - venus_dist,  VC).uv(1.0F, 1.0F).endVertex();
            bufferbuilder.vertex(matrix4f2, VC, -100.0F - venus_dist,  VC).uv(0.0F, 1.0F).endVertex();
            BufferUploader.drawWithShader(bufferbuilder.end());
        }
        RenderSystem.setShaderColor(1, 1, 1, 1);
	    RenderSystem.depthMask(true);
        RenderSystem.enableBlend();
        pPoseStack.popPose();
      }
    	if (player_dim == NorthstarDimensions.MOON_DIM_KEY) {
    	      float playerEyeLevel = (float) this.minecraft.player.getEyePosition(pPartialTick).y;
    	    	  info.cancel();
    	          runnable.run();
    	          BufferBuilder bufferbuilder3 = Tesselator.getInstance().getBuilder();
    	          float f = 0;
    	          float f1 = 0;
    	          float f2 = 0;

    	          RenderSystem.depthMask(false);
    	          RenderSystem.setShaderColor(f, f1, f2, f_alpha);
    	          ShaderInstance shaderinstance = RenderSystem.getShader();
    	          RenderSystem.enableBlend();
    	          RenderSystem.defaultBlendFunc();
    	          this.skyBuffer.bind();
    	          this.skyBuffer.drawWithShader(pPoseStack.last().pose(), pProjectionMatrix, shaderinstance);
    	          VertexBuffer.unbind();
    	          
    	          pPoseStack.pushPose();
    	          pPoseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
    	          pPoseStack.mulPose(Axis.XP.rotationDegrees(this.level.getTimeOfDay(pPartialTick) * 360.0F));
    	          Matrix4f matrix4f3 = pPoseStack.last().pose();
    	          float starBrightness;
    	          float f10 = 2;
    	          if (f10 > 0) {starBrightness = -(f10 - playerEyeLevel - 300) / 100;}
    	          else {starBrightness = (playerEyeLevel - 450) / 200;}

    	          if (starBrightness > 0.0F) {
    	              RenderSystem.setShaderColor(starBrightness, starBrightness, starBrightness, starBrightness);
    	              FogRenderer.setupNoFog();
    	              this.starBuffer.bind();
    	              this.starBuffer.drawWithShader(pPoseStack.last().pose(), pProjectionMatrix, GameRenderer.getPositionShader());
    	              VertexBuffer.unbind();
    	              RenderSystem.setShaderColor(starBrightness, starBrightness, starBrightness, 0.67F);
    	              this.starBuffer2.bind();
    	              this.starBuffer2.drawWithShader(pPoseStack.last().pose(), pProjectionMatrix, GameRenderer.getPositionShader());
    	              VertexBuffer.unbind();
    	              RenderSystem.setShaderColor(starBrightness, starBrightness, starBrightness, 0.33F);
    	              this.starBuffer3.bind();
    	              this.starBuffer3.drawWithShader(pPoseStack.last().pose(), pProjectionMatrix, GameRenderer.getPositionShader());
    	              VertexBuffer.unbind();
    	              runnable.run();
    	          }
      	          float NS = 2.0F;
      	          RenderSystem.setShaderColor(1, 1, 1, 1);
      	          RenderSystem.setShader(GameRenderer::getPositionTexShader);
      	          RenderSystem.setShaderTexture(0, NORTHERN_STAR);
      	          bufferbuilder3.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
      	          bufferbuilder3.vertex(matrix4f3, -100,  -30 + NS, -NS).uv(0.0F, 0.0F).endVertex();
      	          bufferbuilder3.vertex(matrix4f3, -100, -30 + NS,   NS).uv(1.0F, 0.0F).endVertex();
      	          bufferbuilder3.vertex(matrix4f3, -100, -30 + -NS,   NS).uv(1.0F, 1.0F).endVertex();
      	          bufferbuilder3.vertex(matrix4f3, -100,  -30 + -NS, -NS).uv(0.0F, 1.0F).endVertex();
      	          BufferUploader.drawWithShader(bufferbuilder3.end());	
      	          
    	          RenderSystem.setShaderColor(1, 1, 1, 1);
    	          int VF = 2;
    	          int MVF = 1;
    	          RenderSystem.setShader(GameRenderer::getPositionTexShader);
    	          RenderSystem.enableBlend();
    	          RenderSystem.defaultBlendFunc();
    	          RenderSystem.setShaderTexture(0, VENUS_FAR);
    	          bufferbuilder3.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
    	          bufferbuilder3.vertex(matrix4f3, 100, -40f +  VF, 50 +  VF).uv(0, 0).endVertex();
    	          bufferbuilder3.vertex(matrix4f3, 100,  -39.4f +  VF, 50 + -VF).uv(1, 0).endVertex();
    	          bufferbuilder3.vertex(matrix4f3, 100,  -39.4f + -VF, 50 + -VF).uv(1, -1).endVertex();
    	          bufferbuilder3.vertex(matrix4f3, 100, -40f + -VF, 50 +  VF).uv(0, -1).endVertex();
    	          BufferUploader.drawWithShader(bufferbuilder3.end());
    	          RenderSystem.setShaderTexture(0, MARS_VERY_FAR);
    	          bufferbuilder3.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
    	          bufferbuilder3.vertex(matrix4f3, -59.25f, -30f +  MVF, -80 +  -MVF).uv(0, 0).endVertex();
    	          bufferbuilder3.vertex(matrix4f3, -60,  -29.65f +  MVF, -80 +  MVF).uv(1, 0).endVertex();
    	          bufferbuilder3.vertex(matrix4f3, -60,  -29.65f + -MVF, -80 +  MVF).uv(1, -1).endVertex();
    	          bufferbuilder3.vertex(matrix4f3, -59.25f, -30f + -MVF, -80 +  -MVF).uv(0, -1).endVertex();
    	          BufferUploader.drawWithShader(bufferbuilder3.end());
    	          pPoseStack.popPose();
    	       	 RenderSystem.depthMask(true);
    	        
    	          
    	          RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
    	          float f11 = 1.0F - this.level.getRainLevel(pPartialTick);
    	          RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0f);
    	          float f12 = 30.0F;
    	          RenderSystem.setShader(GameRenderer::getPositionTexShader);
    	          RenderSystem.setShaderTexture(0, BARE_SUN);
    	          bufferbuilder3.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
    	          bufferbuilder3.vertex(matrix4f3, -f12, 100.0F, -f12).uv(0.0F, 0.0F).endVertex();
    	          bufferbuilder3.vertex(matrix4f3, f12, 100.0F, -f12).uv(1.0F, 0.0F).endVertex();
    	          bufferbuilder3.vertex(matrix4f3, f12, 100.0F, f12).uv(1.0F, 1.0F).endVertex();
    	          bufferbuilder3.vertex(matrix4f3, -f12, 100.0F, f12).uv(0.0F, 1.0F).endVertex();
    	          BufferUploader.drawWithShader(bufferbuilder3.end());
    	          RenderSystem.disableBlend();
    	          
    	  		  pPoseStack.pushPose();
    	          pPoseStack.mulPose(Axis.YP.rotationDegrees(0));
    	          pPoseStack.mulPose(Axis.XP.rotationDegrees(-135));
    	          Matrix4f matrix4f2 = pPoseStack.last().pose();
    	          RenderSystem.setShaderColor(1, 1, 1, 1);
    	          RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
    	          RenderSystem.setShader(GameRenderer::getPositionTexShader);
    	          float earth_sky_dist = 35;
    	  		  float ECS = 45;
    	          RenderSystem.setShaderTexture(0, EARTH_CLOSE);
    	          bufferbuilder3.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
    	          bufferbuilder3.vertex(matrix4f2, ECS, -100.0F - earth_sky_dist, -ECS).uv(0.0F, 0.0F).endVertex();
    	          bufferbuilder3.vertex(matrix4f2, -ECS, -100.0F - earth_sky_dist,-ECS).uv(1.0F, 0.0F).endVertex();
    	          bufferbuilder3.vertex(matrix4f2, -ECS, -100.0F - earth_sky_dist, ECS).uv(1.0F, -1.0F).endVertex();
    	          bufferbuilder3.vertex(matrix4f2, ECS, -100.0F - earth_sky_dist,  ECS).uv(0.0F, -1.0F).endVertex();
    	          BufferUploader.drawWithShader(bufferbuilder3.end());pPoseStack.popPose();RenderSystem.depthMask(true);  
    	          
    	          f12 = 20.0F;
    	          float earth_alpha = (playerEyeLevel - 450) / 300;
    	          float earth_dist = (playerEyeLevel - 450) / 10;
    	          if (playerEyeLevel > 450) {
    	          if (earth_alpha >= 1) {RenderSystem.disableBlend();}else{RenderSystem.enableBlend();}	
    	  		  float EC = 2000;
    	  		  pPoseStack.pushPose();
    	          pPoseStack.mulPose(Axis.YP.rotationDegrees(0));
    	          pPoseStack.mulPose(Axis.XP.rotationDegrees(0));
    	          matrix4f2 = pPoseStack.last().pose();
    	          BufferBuilder bufferbuilder2 = Tesselator.getInstance().getBuilder();
    	          RenderSystem.setShaderColor(1, 1, 1, earth_alpha);
    	          RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
    	          RenderSystem.setShader(GameRenderer::getPositionTexShader);
    	          RenderSystem.setShaderTexture(0, MOON_CLOSE);
    	          bufferbuilder2.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
    	          bufferbuilder2.vertex(matrix4f2, EC, -100.0F - earth_dist, -EC).uv(0.0F, 0.0F).endVertex();
    	          bufferbuilder2.vertex(matrix4f2, -EC, -100.0F - earth_dist,-EC).uv(1.0F, 0.0F).endVertex();
    	          bufferbuilder2.vertex(matrix4f2, -EC, -100.0F - earth_dist, EC).uv(1.0F, -1.0F).endVertex();
    	          bufferbuilder2.vertex(matrix4f2, EC, -100.0F - earth_dist,  EC).uv(0.0F, -1.0F).endVertex();
    	          BufferUploader.drawWithShader(bufferbuilder2.end());pPoseStack.popPose();RenderSystem.depthMask(true);  
    	          }RenderSystem.depthMask(true);  
    	          RenderSystem.setShaderColor(1, 1, 1, 1);
    	      }
    	if (player_dim == null) {
  	      float playerEyeLevel = (float) this.minecraft.player.getEyePosition(pPartialTick).y;
  	    	  info.cancel();
  	          runnable.run();
	          float f = 0;
	          float f1 = 0;
	          float f2 = 0;
  	          RenderSystem.depthMask(false);
  	          RenderSystem.setShaderColor(f, f1, f2, f_alpha);
  	          ShaderInstance shaderinstance = RenderSystem.getShader();
  	          RenderSystem.enableBlend();
  	          RenderSystem.defaultBlendFunc();
  	          this.skyBuffer.bind();
  	          this.skyBuffer.drawWithShader(pPoseStack.last().pose(), pProjectionMatrix, shaderinstance);
  	          VertexBuffer.unbind();
  	          
  	          pPoseStack.pushPose();
  	          pPoseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
  	          pPoseStack.mulPose(Axis.XP.rotationDegrees(this.level.getTimeOfDay(pPartialTick) * 360.0F));
  	          float starBrightness;
  	          float f10 = 2;
  	          if (f10 > 0) {starBrightness = -(f10 - playerEyeLevel - 300) / 100;}
  	          else {starBrightness = (playerEyeLevel - 450) / 200;}

  	          if (starBrightness > 0.0F) {
  	              RenderSystem.setShaderColor(starBrightness, starBrightness, starBrightness, starBrightness);
  	              FogRenderer.setupNoFog();
  	              this.starBuffer.bind();
  	              this.starBuffer.drawWithShader(pPoseStack.last().pose(), pProjectionMatrix, GameRenderer.getPositionShader());
  	              VertexBuffer.unbind();
  	              RenderSystem.setShaderColor(starBrightness, starBrightness, starBrightness, 0.67F);
  	              this.starBuffer2.bind();
  	              this.starBuffer2.drawWithShader(pPoseStack.last().pose(), pProjectionMatrix, GameRenderer.getPositionShader());
  	              VertexBuffer.unbind();
  	              RenderSystem.setShaderColor(starBrightness, starBrightness, starBrightness, 0.33F);
  	              this.starBuffer3.bind();
  	              this.starBuffer3.drawWithShader(pPoseStack.last().pose(), pProjectionMatrix, GameRenderer.getPositionShader());
  	              VertexBuffer.unbind();
  	              runnable.run();
  	          }
  	            RenderSystem.setShaderColor(1, 1, 1, 1);
  	      }
    	if (player_dim == NorthstarDimensions.EARTH_ORBIT_DIM_KEY) {
  	      float playerEyeLevel = (float) this.minecraft.player.getEyePosition(pPartialTick).y;
  	    	  info.cancel();
  	          runnable.run();
  	          BufferBuilder bufferbuilder3 = Tesselator.getInstance().getBuilder();
	          float f = 0;
	          float f1 = 0;
	          float f2 = 0;
  	          RenderSystem.depthMask(false);
  	          RenderSystem.setShaderColor(f, f1, f2, f_alpha);
  	          ShaderInstance shaderinstance = RenderSystem.getShader();
  	          RenderSystem.enableBlend();
  	          RenderSystem.defaultBlendFunc();
  	          this.skyBuffer.bind();
  	          this.skyBuffer.drawWithShader(pPoseStack.last().pose(), pProjectionMatrix, shaderinstance);
  	          VertexBuffer.unbind();
  	          
  	          pPoseStack.pushPose();
  	          pPoseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
  	          pPoseStack.mulPose(Axis.XP.rotationDegrees(this.level.getTimeOfDay(pPartialTick) * 360.0F));
  	          Matrix4f matrix4f3 = pPoseStack.last().pose();
  	          float starBrightness;
  	          float f10 = 2;
  	          if (f10 > 0) {starBrightness = -(f10 - playerEyeLevel - 300) / 100;}
  	          else {starBrightness = (playerEyeLevel - 450) / 200;}

  	          if (starBrightness > 0.0F) {
  	              RenderSystem.setShaderColor(starBrightness, starBrightness, starBrightness, starBrightness);
  	              FogRenderer.setupNoFog();
  	              this.starBuffer.bind();
  	              this.starBuffer.drawWithShader(pPoseStack.last().pose(), pProjectionMatrix, GameRenderer.getPositionShader());
  	              VertexBuffer.unbind();
  	              RenderSystem.setShaderColor(starBrightness, starBrightness, starBrightness, 0.67F);
  	              this.starBuffer2.bind();
  	              this.starBuffer2.drawWithShader(pPoseStack.last().pose(), pProjectionMatrix, GameRenderer.getPositionShader());
  	              VertexBuffer.unbind();
  	              RenderSystem.setShaderColor(starBrightness, starBrightness, starBrightness, 0.33F);
  	              this.starBuffer3.bind();
  	              this.starBuffer3.drawWithShader(pPoseStack.last().pose(), pProjectionMatrix, GameRenderer.getPositionShader());
  	              VertexBuffer.unbind();
  	              runnable.run();
  	          }
  	          
  	          float NS = 2.0F;
  	          RenderSystem.setShaderColor(1, 1, 1, 1);
  	          RenderSystem.setShader(GameRenderer::getPositionTexShader);
  	          RenderSystem.setShaderTexture(0, NORTHERN_STAR);
  	          bufferbuilder3.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
  	          bufferbuilder3.vertex(matrix4f3, -100,  -30 + NS, -NS).uv(0.0F, 0.0F).endVertex();
  	          bufferbuilder3.vertex(matrix4f3, -100, -30 + NS,   NS).uv(1.0F, 0.0F).endVertex();
  	          bufferbuilder3.vertex(matrix4f3, -100, -30 + -NS,   NS).uv(1.0F, 1.0F).endVertex();
  	          bufferbuilder3.vertex(matrix4f3, -100,  -30 + -NS, -NS).uv(0.0F, 1.0F).endVertex();
  	          BufferUploader.drawWithShader(bufferbuilder3.end());	
  	          
  	          RenderSystem.setShaderColor(1, 1, 1, 1);
  	          int VF = 2;
  	          int MVF = 1;
  	          RenderSystem.setShader(GameRenderer::getPositionTexShader);
  	          RenderSystem.enableBlend();
  	          RenderSystem.defaultBlendFunc();
  	          RenderSystem.setShaderTexture(0, VENUS_FAR);
  	          bufferbuilder3.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
  	          bufferbuilder3.vertex(matrix4f3, 100, -40f +  VF, 50 +  VF).uv(0, 0).endVertex();
  	          bufferbuilder3.vertex(matrix4f3, 100,  -39.4f +  VF, 50 + -VF).uv(1, 0).endVertex();
  	          bufferbuilder3.vertex(matrix4f3, 100,  -39.4f + -VF, 50 + -VF).uv(1, -1).endVertex();
  	          bufferbuilder3.vertex(matrix4f3, 100, -40f + -VF, 50 +  VF).uv(0, -1).endVertex();
  	          BufferUploader.drawWithShader(bufferbuilder3.end());
  	          RenderSystem.setShaderTexture(0, MARS_VERY_FAR);
  	          bufferbuilder3.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
  	          bufferbuilder3.vertex(matrix4f3, -59.25f, -30f +  MVF, -80 +  -MVF).uv(0, 0).endVertex();
  	          bufferbuilder3.vertex(matrix4f3, -60,  -29.65f +  MVF, -80 +  MVF).uv(1, 0).endVertex();
  	          bufferbuilder3.vertex(matrix4f3, -60,  -29.65f + -MVF, -80 +  MVF).uv(1, -1).endVertex();
  	          bufferbuilder3.vertex(matrix4f3, -59.25f, -30f + -MVF, -80 +  -MVF).uv(0, -1).endVertex();
  	          BufferUploader.drawWithShader(bufferbuilder3.end());
  	          pPoseStack.popPose();
  	       	 RenderSystem.depthMask(true);
  	        
  	          
  	          RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
  	          float f11 = 1.0F - this.level.getRainLevel(pPartialTick);
  	          RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, f11);
  	          float f12 = 30.0F;
  	          RenderSystem.setShader(GameRenderer::getPositionTexShader);
  	          RenderSystem.setShaderTexture(0, BARE_SUN);
  	          bufferbuilder3.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
  	          bufferbuilder3.vertex(matrix4f3, -f12, 100.0F, -f12).uv(0.0F, 0.0F).endVertex();
  	          bufferbuilder3.vertex(matrix4f3, f12, 100.0F, -f12).uv(1.0F, 0.0F).endVertex();
  	          bufferbuilder3.vertex(matrix4f3, f12, 100.0F, f12).uv(1.0F, 1.0F).endVertex();
  	          bufferbuilder3.vertex(matrix4f3, -f12, 100.0F, f12).uv(0.0F, 1.0F).endVertex();
  	          BufferUploader.drawWithShader(bufferbuilder3.end());
  	          RenderSystem.disableBlend();
  	          
  	          f12 = 20.0F;
  	          float earth_dist = 175;

  	  		  float EC = 2000;
  	          Matrix4f matrix4f2 = pPoseStack.last().pose();
  	  		  pPoseStack.pushPose();
  	          pPoseStack.mulPose(Axis.YP.rotationDegrees(0));
  	          pPoseStack.mulPose(Axis.XP.rotationDegrees(0));
  	          matrix4f2 = pPoseStack.last().pose();
  	          BufferBuilder bufferbuilder2 = Tesselator.getInstance().getBuilder();
  	          RenderSystem.setShaderColor(1, 1, 1, 1);
  	          RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
  	          RenderSystem.setShader(GameRenderer::getPositionTexShader);
  	          RenderSystem.setShaderTexture(0, EARTH_CLOSE);
  	          bufferbuilder2.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
  	          bufferbuilder2.vertex(matrix4f2, EC, -100.0F - earth_dist, -EC).uv(0.0F, 0.0F).endVertex();
  	          bufferbuilder2.vertex(matrix4f2, -EC, -100.0F - earth_dist,-EC).uv(1.0F, 0.0F).endVertex();
  	          bufferbuilder2.vertex(matrix4f2, -EC, -100.0F - earth_dist, EC).uv(1.0F, -1.0F).endVertex();
  	          bufferbuilder2.vertex(matrix4f2, EC, -100.0F - earth_dist,  EC).uv(0.0F, -1.0F).endVertex();
  	          BufferUploader.drawWithShader(bufferbuilder2.end());pPoseStack.popPose();RenderSystem.depthMask(true);  
  	          RenderSystem.depthMask(true);  
  	      }
    	if (player_dim == NorthstarDimensions.MERCURY_DIM_KEY) {
  	      float playerEyeLevel = (float) this.minecraft.player.getEyePosition(pPartialTick).y;
  	    	  info.cancel();
  	          runnable.run();
  	          BufferBuilder bufferbuilder3 = Tesselator.getInstance().getBuilder();
	          float f = 0;
	          float f1 = 0;
	          float f2 = 0;
  	          RenderSystem.depthMask(false);
  	          RenderSystem.setShaderColor(f, f1, f2, f_alpha);
  	          ShaderInstance shaderinstance = RenderSystem.getShader();
  	          RenderSystem.enableBlend();
  	          RenderSystem.defaultBlendFunc();
  	          this.skyBuffer.bind();
  	          this.skyBuffer.drawWithShader(pPoseStack.last().pose(), pProjectionMatrix, shaderinstance);
  	          VertexBuffer.unbind();
  	          
  	          pPoseStack.pushPose();
  	          pPoseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
  	          pPoseStack.mulPose(Axis.XP.rotationDegrees(this.level.getTimeOfDay(pPartialTick) * 360.0F));
  	          Matrix4f matrix4f3 = pPoseStack.last().pose();
  	          float starBrightness;
  	          float f10 = 2;
  	          if (f10 > 0) {starBrightness = -(f10 - playerEyeLevel - 300) / 100;}
  	          else {starBrightness = (playerEyeLevel - 450) / 200;}

  	          if (starBrightness > 0.0F) {
  	              RenderSystem.setShaderColor(starBrightness, starBrightness, starBrightness, starBrightness);
  	              FogRenderer.setupNoFog();
  	              this.starBuffer.bind();
  	              this.starBuffer.drawWithShader(pPoseStack.last().pose(), pProjectionMatrix, GameRenderer.getPositionShader());
  	              VertexBuffer.unbind();
  	              RenderSystem.setShaderColor(starBrightness, starBrightness, starBrightness, 0.67F);
  	              this.starBuffer2.bind();
  	              this.starBuffer2.drawWithShader(pPoseStack.last().pose(), pProjectionMatrix, GameRenderer.getPositionShader());
  	              VertexBuffer.unbind();
  	              RenderSystem.setShaderColor(starBrightness, starBrightness, starBrightness, 0.33F);
  	              this.starBuffer3.bind();
  	              this.starBuffer3.drawWithShader(pPoseStack.last().pose(), pProjectionMatrix, GameRenderer.getPositionShader());
  	              VertexBuffer.unbind();
  	              runnable.run();
  	          }
  	          
  	          float NS = 2.0F;
  	          RenderSystem.setShaderColor(1, 1, 1, 1);
  	          RenderSystem.setShader(GameRenderer::getPositionTexShader);
  	          RenderSystem.setShaderTexture(0, NORTHERN_STAR);
  	          bufferbuilder3.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
  	          bufferbuilder3.vertex(matrix4f3, -100,  -30 + NS, -NS).uv(0.0F, 0.0F).endVertex();
  	          bufferbuilder3.vertex(matrix4f3, -100, -30 + NS,   NS).uv(1.0F, 0.0F).endVertex();
  	          bufferbuilder3.vertex(matrix4f3, -100, -30 + -NS,   NS).uv(1.0F, 1.0F).endVertex();
  	          bufferbuilder3.vertex(matrix4f3, -100,  -30 + -NS, -NS).uv(0.0F, 1.0F).endVertex();
  	          BufferUploader.drawWithShader(bufferbuilder3.end());	
  	          
  	          
  	            RenderSystem.setShaderColor(1, 1, 1, 1);
  	          int VF = 2;
  	          int EF = 2;
  	          int SUN = 80;
  	          RenderSystem.setShader(GameRenderer::getPositionTexShader);
  	          RenderSystem.enableBlend();
  	          RenderSystem.defaultBlendFunc();
  	          RenderSystem.setShaderTexture(0, VENUS_FAR);
  	          bufferbuilder3.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
  	          bufferbuilder3.vertex(matrix4f3, 100, -40f +  VF, 50 +  VF).uv(0, 0).endVertex();
  	          bufferbuilder3.vertex(matrix4f3, 100,  -39.4f +  VF, 50 + -VF).uv(1, 0).endVertex();
  	          bufferbuilder3.vertex(matrix4f3, 100,  -39.4f + -VF, 50 + -VF).uv(1, -1).endVertex();
  	          bufferbuilder3.vertex(matrix4f3, 100, -40f + -VF, 50 +  VF).uv(0, -1).endVertex();
  	          BufferUploader.drawWithShader(bufferbuilder3.end());
  	          RenderSystem.setShaderTexture(0, EARTH_FAR);
  	          bufferbuilder3.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
  	          bufferbuilder3.vertex(matrix4f3, -58f, -30f +  EF, -80 +  -EF).uv(0, 0).endVertex();
  	          bufferbuilder3.vertex(matrix4f3, -60,  -29.65f +  EF, -80 +  EF).uv(1, 0).endVertex();
  	          bufferbuilder3.vertex(matrix4f3, -60,  -29.65f + -EF, -80 +  EF).uv(1, -1).endVertex();
  	          bufferbuilder3.vertex(matrix4f3, -58f, -30f + -EF, -80 +  -EF).uv(0, -1).endVertex();
  	          BufferUploader.drawWithShader(bufferbuilder3.end());
  	          RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
  	          RenderSystem.setShader(GameRenderer::getPositionTexShader);
  	          RenderSystem.setShaderTexture(0, BARE_SUN);
  	          bufferbuilder3.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
  	          bufferbuilder3.vertex(matrix4f3,  SUN, 100.0F, SUN).uv(0, 0).endVertex();
  	          bufferbuilder3.vertex(matrix4f3, -SUN, 100.0F, SUN).uv(1, 0).endVertex();
  	    	  bufferbuilder3.vertex(matrix4f3, -SUN, 100.0F, -SUN).uv(1, 1).endVertex();
  	    	  bufferbuilder3.vertex(matrix4f3,  SUN, 100.0F, -SUN).uv(0, 1).endVertex();
  	    	  BufferUploader.drawWithShader(bufferbuilder3.end());
  	          pPoseStack.popPose();
  	       	 RenderSystem.depthMask(true);
  	        RenderSystem.setShaderColor(1, 1, 1, 1);
  	        
  	          
  	          float f11 = 1.0F - this.level.getRainLevel(pPartialTick);
  	          RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, f11);
  	          float f12 = 30.0F;
  	          RenderSystem.setShader(GameRenderer::getPositionTexShader);
  	          RenderSystem.setShaderTexture(0, BARE_SUN);
  	          bufferbuilder3.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
  	          bufferbuilder3.vertex(matrix4f3, -f12, 100.0F, -f12).uv(0.0F, 0.0F).endVertex();
  	          bufferbuilder3.vertex(matrix4f3, f12, 100.0F, -f12).uv(1.0F, 0.0F).endVertex();
  	          bufferbuilder3.vertex(matrix4f3, f12, 100.0F, f12).uv(1.0F, 1.0F).endVertex();
  	          bufferbuilder3.vertex(matrix4f3, -f12, 100.0F, f12).uv(0.0F, 1.0F).endVertex();
  	          BufferUploader.drawWithShader(bufferbuilder3.end());
  	          RenderSystem.disableBlend();

  	          Matrix4f matrix4f2 = pPoseStack.last().pose();

  	          
  	          f12 = 20.0F;
  	          float earth_alpha = (playerEyeLevel - 450) / 300;
  	          float earth_dist = (playerEyeLevel - 450) / 10;
  	          if (playerEyeLevel > 450) {
  	          if (earth_alpha >= 1) {RenderSystem.disableBlend();}else{RenderSystem.enableBlend();}	
  	  		  float EC = 2000;
  	  		  pPoseStack.pushPose();
  	          pPoseStack.mulPose(Axis.YP.rotationDegrees(0));
  	          pPoseStack.mulPose(Axis.XP.rotationDegrees(0));
  	          matrix4f2 = pPoseStack.last().pose();
  	          BufferBuilder bufferbuilder2 = Tesselator.getInstance().getBuilder();
  	          RenderSystem.setShaderColor(1, 1, 1, earth_alpha);
  	          RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
  	          RenderSystem.setShader(GameRenderer::getPositionTexShader);
  	          RenderSystem.setShaderTexture(0, MERCURY_CLOSE);
  	          bufferbuilder2.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
  	          bufferbuilder2.vertex(matrix4f2, EC, -100.0F - earth_dist, -EC).uv(0.0F, 0.0F).endVertex();
  	          bufferbuilder2.vertex(matrix4f2, -EC, -100.0F - earth_dist,-EC).uv(1.0F, 0.0F).endVertex();
  	          bufferbuilder2.vertex(matrix4f2, -EC, -100.0F - earth_dist, EC).uv(1.0F, -1.0F).endVertex();
  	          bufferbuilder2.vertex(matrix4f2, EC, -100.0F - earth_dist,  EC).uv(0.0F, -1.0F).endVertex();
  	          BufferUploader.drawWithShader(bufferbuilder2.end());pPoseStack.popPose();RenderSystem.depthMask(true);  
  	          }RenderSystem.depthMask(true);  
  	      }
		}
      
   }
	
	//THIS IS FOR THE OVERWORLD ONLY, OTHERWISE IT (probably) WONT BE CALLED
	// THIS IS FOR WHEN THE PLAYER IS **NOT** LEAVING THE PLANET
	@SuppressWarnings("resource")
	@Inject(method = "renderSky", at = @At("TAIL"), cancellable = true)
    private void renderSky2(PoseStack pPoseStack, Matrix4f pProjectionMatrix, float pPartialTick, Camera camera, boolean thing, Runnable runnable, CallbackInfo info) {
		if(this.minecraft != null) {
		ResourceKey<Level> player_dim = Minecraft.getInstance().level.dimension();
    	if (player_dim == Level.OVERWORLD)
    	{float playerEyeLevel = (float) this.minecraft.player.getEyePosition(pPartialTick).y;
            //venus off in the distance (cool)
         pPoseStack.pushPose();
         pPoseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
         pPoseStack.mulPose(Axis.XP.rotationDegrees(this.level.getTimeOfDay(pPartialTick) * 360.0F));
            
            
            
         BufferBuilder bufferbuilder_earth_sky = Tesselator.getInstance().getBuilder();
         Matrix4f matrix4f_earth_sky = pPoseStack.last().pose();
         float earth_sky_planet_brightness = (float) (this.level.getStarBrightness(pPartialTick) * 1.5) * (this.level.isRaining() && playerEyeLevel < 450 ? 0 : 1);
         float northstar_brightness = earth_sky_planet_brightness * 2;
         
         RenderSystem.enableBlend();
         RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
      	 RenderSystem.depthMask(true);
         RenderSystem.setShader(GameRenderer::getPositionTexShader);
         RenderSystem.enableBlend();
         RenderSystem.defaultBlendFunc();
         
         float NS = 2.0F;
         RenderSystem.setShaderColor(northstar_brightness, northstar_brightness, northstar_brightness, northstar_brightness);
         RenderSystem.setShader(GameRenderer::getPositionTexShader);
         RenderSystem.setShaderTexture(0, NORTHERN_STAR);
         bufferbuilder_earth_sky.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
         bufferbuilder_earth_sky.vertex(matrix4f_earth_sky, -100,  -30 + NS, -NS).uv(0.0F, 0.0F).endVertex();
         bufferbuilder_earth_sky.vertex(matrix4f_earth_sky, -100, -30 + NS,   NS).uv(1.0F, 0.0F).endVertex();
         bufferbuilder_earth_sky.vertex(matrix4f_earth_sky, -100, -30 + -NS,   NS).uv(1.0F, 1.0F).endVertex();
         bufferbuilder_earth_sky.vertex(matrix4f_earth_sky, -100,  -30 + -NS, -NS).uv(0.0F, 1.0F).endVertex();
         BufferUploader.drawWithShader(bufferbuilder_earth_sky.end());	
      	 
      	 if (playerEyeLevel >= 450) {
      	   RenderSystem.setShaderColor(1, 1, 1, 1);//System.out.println("we gamin");
      	   }
      	   else {RenderSystem.setShaderColor(earth_sky_planet_brightness, earth_sky_planet_brightness, earth_sky_planet_brightness, earth_sky_planet_brightness);}

         
         float VF = 2;
         RenderSystem.setShaderTexture(0, VENUS_FAR);
         bufferbuilder_earth_sky.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
         bufferbuilder_earth_sky.vertex(matrix4f_earth_sky, 100, -40f +  VF, 50 +  VF).uv(0, 0).endVertex();
         bufferbuilder_earth_sky.vertex(matrix4f_earth_sky, 100,  -39.4f +  VF, 50 + -VF).uv(1, 0).endVertex();
         bufferbuilder_earth_sky.vertex(matrix4f_earth_sky, 100,  -39.4f + -VF, 50 + -VF).uv(1, -1).endVertex();
         bufferbuilder_earth_sky.vertex(matrix4f_earth_sky, 100, -40f + -VF, 50 +  VF).uv(0, -1).endVertex();
         BufferUploader.drawWithShader(bufferbuilder_earth_sky.end());
         
         float MVF = 1;
         RenderSystem.setShaderTexture(0, MARS_VERY_FAR);
         bufferbuilder_earth_sky.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
         bufferbuilder_earth_sky.vertex(matrix4f_earth_sky, -59.25f, -30f +  MVF, -80 +  -MVF).uv(0, 0).endVertex();
         bufferbuilder_earth_sky.vertex(matrix4f_earth_sky, -60,  -29.65f +  MVF, -80 +  MVF).uv(1, 0).endVertex();
         bufferbuilder_earth_sky.vertex(matrix4f_earth_sky, -60,  -29.65f + -MVF, -80 +  MVF).uv(1, -1).endVertex();
         bufferbuilder_earth_sky.vertex(matrix4f_earth_sky, -59.25f, -30f + -MVF, -80 +  -MVF).uv(0, -1).endVertex();
         BufferUploader.drawWithShader(bufferbuilder_earth_sky.end());
         RenderSystem.disableBlend();
         RenderSystem.depthMask(true);
         RenderSystem.enableBlend();      
         pPoseStack.popPose();
    	}
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.depthMask(true);
		}
		
	}
	
	
	@SuppressWarnings("resource")
	@Inject(method = "renderClouds", at = @At("HEAD"), cancellable = true)
   public void renderClouds(PoseStack pPoseStack, Matrix4f pProjectionMatrix, float pPartialTick, double pCamX, double pCamY, double pCamZ, CallbackInfo info) {
		ResourceKey<Level> player_dim = Minecraft.getInstance().level.dimension();
		if(this.minecraft != null) {
	    float playerEyeLevel = (float) this.minecraft.player.getEyePosition(pPartialTick).y;
		if (player_dim == NorthstarDimensions.MARS_DIM_KEY)
    	{info.cancel();}
		if (player_dim == NorthstarDimensions.MOON_DIM_KEY)
    	{info.cancel();}
		if (player_dim == NorthstarDimensions.MERCURY_DIM_KEY)
    	{info.cancel();}
		if (player_dim == NorthstarDimensions.EARTH_ORBIT_DIM_KEY)
    	{info.cancel();}
    	if (player_dim == Level.OVERWORLD && playerEyeLevel > 500)
    	{info.cancel();}
		if (player_dim == NorthstarDimensions.VENUS_DIM_KEY && !(playerEyeLevel > 500)) {
			info.cancel();
			if (level.effects().renderClouds(level, ticks, pPartialTick, pPoseStack, pCamX, pCamY, pCamZ, pProjectionMatrix))
		         return;
		      float f = this.level.effects().getCloudHeight();
		      if (!Float.isNaN(f)) {
		         RenderSystem.disableCull();
		         RenderSystem.enableBlend();
		         RenderSystem.enableDepthTest();
		         RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		         RenderSystem.depthMask(true);
		         double d1 = (double)(((float)this.ticks + pPartialTick) * 0.03F);
		         double d2 = (pCamX + d1) / 12.0D;
		         double d3 = (double)(f - (float)pCamY + 0.33F);
		         double d4 = pCamZ / 12.0D + (double)0.33F;
		         d2 -= (double)(Mth.floor(d2 / 2048.0D) * 2048);
		         d4 -= (double)(Mth.floor(d4 / 2048.0D) * 2048);
		         float f3 = (float)(d2 - (double)Mth.floor(d2));
		         float f4 = (float)(d3 / 4.0D - (double)Mth.floor(d3 / 4.0D)) * 4.0F;
		         float f5 = (float)(d4 - (double)Mth.floor(d4));
		         float time = level.getTimeOfDay(pPartialTick);
		         float cloudcolor = Mth.cos(time * ((float)Math.PI * 2F)) * 2.0F + 0.5F;
		         cloudcolor = Mth.clamp(cloudcolor, 0.25F, 1.0F);
		         Vec3 vec3 = new Vec3(0.5058F * cloudcolor, 0.5176F * cloudcolor, 0.0352F * cloudcolor);
		         int i = (int)Math.floor(d2);
		         int j = (int)Math.floor(d3 / 4.0D);
		         int k = (int)Math.floor(d4);
		         if (i != this.prevCloudX || j != this.prevCloudY || k != this.prevCloudZ || this.minecraft.options.getCloudsType() != this.prevCloudsType || this.prevCloudColor.distanceToSqr(vec3) > 2.0E-4D) {
		            this.prevCloudX = i;
		            this.prevCloudY = j;
		            this.prevCloudZ = k;
		            this.prevCloudColor = vec3;
		            this.prevCloudsType = this.minecraft.options.getCloudsType();
		            this.generateClouds = true;
		         }
		         if (this.generateClouds) {
		            this.generateClouds = false;
		            BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
		            if (this.cloudBuffer != null) {
		               this.cloudBuffer.close();
		            }
		            this.cloudBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
		            BufferBuilder.RenderedBuffer bufferbuilder$renderedbuffer = this.buildClouds(bufferbuilder, d2, d3, d4, vec3, 1);
		            this.cloudBuffer.bind();
		            this.cloudBuffer.upload(bufferbuilder$renderedbuffer);
		            VertexBuffer.unbind();
		            
//		            this.cloudBuffer2 = new VertexBuffer();
//		            BufferBuilder.RenderedBuffer bufferbuilder$renderedbuffer2 = this.buildClouds(bufferbuilder, d2 + 50, d3 + 50, d4 + 50, vec3,   2);
//		            this.cloudBuffer2.bind();
//		            this.cloudBuffer2.upload(bufferbuilder$renderedbuffer2);
//		            VertexBuffer.unbind();
		         }

		         RenderSystem.setShader(GameRenderer::getPositionTexColorNormalShader);
		         RenderSystem.setShaderTexture(0, CLOUDS_LOCATION);
		         FogRenderer.levelFogColor();
		         pPoseStack.pushPose();
		         pPoseStack.scale(12.0F, 1.0F, 12.0F);
		         pPoseStack.translate((double)(-f3), (double)f4, (double)(-f5));
		         if (this.cloudBuffer != null) {
		            this.cloudBuffer.bind();
		            int l = this.prevCloudsType == CloudStatus.FANCY ? 0 : 1;

		            for(int i1 = l; i1 < 2; ++i1) {
		               if (i1 == 0) {
		                  RenderSystem.colorMask(false, false, false, false);
		               } else {
		                  RenderSystem.colorMask(true, true, true, true);
		               }

		               ShaderInstance shaderinstance = RenderSystem.getShader();
		               this.cloudBuffer.drawWithShader(pPoseStack.last().pose(), pProjectionMatrix, shaderinstance);
//		               this.cloudBuffer2.drawWithShader(pPoseStack.last().pose(), pProjectionMatrix, shaderinstance);
		            }

		            VertexBuffer.unbind();
		         }

		         pPoseStack.popPose();
		         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		         RenderSystem.enableCull();
		         RenderSystem.disableBlend();
		      }
		}else if(player_dim == NorthstarDimensions.VENUS_DIM_KEY && playerEyeLevel > 500) {
			info.cancel();
		}
		}
	}
	
	   private BufferBuilder.RenderedBuffer buildClouds(BufferBuilder pBuilder, double pX, double pY, double pZ, Vec3 pCloudColor, float offset) {
		      float f3 = (float)Mth.floor(pX) * 0.00390625F;
		      float f4 = (float)Mth.floor(pZ) * 0.00390625F;
		      float f5 = (float)pCloudColor.x;
		      float f6 = (float)pCloudColor.y;
		      float f7 = (float)pCloudColor.z;
		      float f8 = f5 * 0.9F;
		      float f9 = f6 * 0.9F;
		      float f10 = f7 * 0.9F;
		      float f11 = f5 * 0.7F;
		      float f12 = f6 * 0.7F;
		      float f13 = f7 * 0.7F;
		      float f14 = f5 * 0.8F;
		      float f15 = f6 * 0.8F;
		      float f16 = f7 * 0.8F;
		      RenderSystem.setShader(GameRenderer::getPositionTexColorNormalShader);
		      pBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL);
		      float f17 = (float)Math.floor(pY / 4.0D) * 4.0F;
		      if (this.prevCloudsType == CloudStatus.FANCY) {
		         for(int k = -3; k <= 4; ++k) {
		            for(int l = -3; l <= 4; ++l) {
		               float f18 = (float)(k * 8);
		               float f19 = (float)(l * 8);
		               if (f17 > -5.0F) {
		                  pBuilder.vertex((double)(f18 + 0.0F), (double)(f17 + 0.0F), (double)(f19 + 8.0F)).uv((f18 + 0.0F) * 0.00390625F + f3, (f19 + 8.0F) * 0.00390625F + f4).color(f11, f12, f13, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
		                  pBuilder.vertex((double)(f18 + 8.0F), (double)(f17 + 0.0F), (double)(f19 + 8.0F)).uv((f18 + 8.0F) * 0.00390625F + f3, (f19 + 8.0F) * 0.00390625F + f4).color(f11, f12, f13, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
		                  pBuilder.vertex((double)(f18 + 8.0F), (double)(f17 + 0.0F), (double)(f19 + 0.0F)).uv((f18 + 8.0F) * 0.00390625F + f3, (f19 + 0.0F) * 0.00390625F + f4).color(f11, f12, f13, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
		                  pBuilder.vertex((double)(f18 + 0.0F), (double)(f17 + 0.0F), (double)(f19 + 0.0F)).uv((f18 + 0.0F) * 0.00390625F + f3, (f19 + 0.0F) * 0.00390625F + f4).color(f11, f12, f13, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
		               }

		               if (f17 <= 5.0F) {
		                  pBuilder.vertex((double)(f18 + 0.0F), (double)(f17 + 4.0F - 9.765625E-4F), (double)(f19 + 8.0F)).uv((f18 + 0.0F) * 0.00390625F + f3, (f19 + 8.0F) * 0.00390625F + f4).color(f5, f6, f7, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
		                  pBuilder.vertex((double)(f18 + 8.0F), (double)(f17 + 4.0F - 9.765625E-4F), (double)(f19 + 8.0F)).uv((f18 + 8.0F) * 0.00390625F + f3, (f19 + 8.0F) * 0.00390625F + f4).color(f5, f6, f7, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
		                  pBuilder.vertex((double)(f18 + 8.0F), (double)(f17 + 4.0F - 9.765625E-4F), (double)(f19 + 0.0F)).uv((f18 + 8.0F) * 0.00390625F + f3, (f19 + 0.0F) * 0.00390625F + f4).color(f5, f6, f7, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
		                  pBuilder.vertex((double)(f18 + 0.0F), (double)(f17 + 4.0F - 9.765625E-4F), (double)(f19 + 0.0F)).uv((f18 + 0.0F) * 0.00390625F + f3, (f19 + 0.0F) * 0.00390625F + f4).color(f5, f6, f7, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
		               }

		               if (k > -1) {
		                  for(int i1 = 0; i1 < 8; ++i1) {
		                     pBuilder.vertex((double)(f18 + (float)i1 + 0.0F), (double)(f17 + 0.0F), (double)(f19 + 8.0F)).uv((f18 + (float)i1 + 0.5F) * 0.00390625F + f3, (f19 + 8.0F) * 0.00390625F + f4).color(f8, f9, f10, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
		                     pBuilder.vertex((double)(f18 + (float)i1 + 0.0F), (double)(f17 + 4.0F), (double)(f19 + 8.0F)).uv((f18 + (float)i1 + 0.5F) * 0.00390625F + f3, (f19 + 8.0F) * 0.00390625F + f4).color(f8, f9, f10, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
		                     pBuilder.vertex((double)(f18 + (float)i1 + 0.0F), (double)(f17 + 4.0F), (double)(f19 + 0.0F)).uv((f18 + (float)i1 + 0.5F) * 0.00390625F + f3, (f19 + 0.0F) * 0.00390625F + f4).color(f8, f9, f10, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
		                     pBuilder.vertex((double)(f18 + (float)i1 + 0.0F), (double)(f17 + 0.0F), (double)(f19 + 0.0F)).uv((f18 + (float)i1 + 0.5F) * 0.00390625F + f3, (f19 + 0.0F) * 0.00390625F + f4).color(f8, f9, f10, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
		                  }
		               }

		               if (k <= 1) {
		                  for(int j2 = 0; j2 < 8; ++j2) {
		                     pBuilder.vertex((double)(f18 + (float)j2 + 1.0F - 9.765625E-4F), (double)(f17 + 0.0F), (double)(f19 + 8.0F)).uv((f18 + (float)j2 + 0.5F) * 0.00390625F + f3, (f19 + 8.0F) * 0.00390625F + f4).color(f8, f9, f10, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
		                     pBuilder.vertex((double)(f18 + (float)j2 + 1.0F - 9.765625E-4F), (double)(f17 + 4.0F), (double)(f19 + 8.0F)).uv((f18 + (float)j2 + 0.5F) * 0.00390625F + f3, (f19 + 8.0F) * 0.00390625F + f4).color(f8, f9, f10, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
		                     pBuilder.vertex((double)(f18 + (float)j2 + 1.0F - 9.765625E-4F), (double)(f17 + 4.0F), (double)(f19 + 0.0F)).uv((f18 + (float)j2 + 0.5F) * 0.00390625F + f3, (f19 + 0.0F) * 0.00390625F + f4).color(f8, f9, f10, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
		                     pBuilder.vertex((double)(f18 + (float)j2 + 1.0F - 9.765625E-4F), (double)(f17 + 0.0F), (double)(f19 + 0.0F)).uv((f18 + (float)j2 + 0.5F) * 0.00390625F + f3, (f19 + 0.0F) * 0.00390625F + f4).color(f8, f9, f10, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
		                  }
		               }

		               if (l > -1) {
		                  for(int k2 = 0; k2 < 8; ++k2) {
		                     pBuilder.vertex((double)(f18 + 0.0F), (double)(f17 + 4.0F), (double)(f19 + (float)k2 + 0.0F)).uv((f18 + 0.0F) * 0.00390625F + f3, (f19 + (float)k2 + 0.5F) * 0.00390625F + f4).color(f14, f15, f16, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
		                     pBuilder.vertex((double)(f18 + 8.0F), (double)(f17 + 4.0F), (double)(f19 + (float)k2 + 0.0F)).uv((f18 + 8.0F) * 0.00390625F + f3, (f19 + (float)k2 + 0.5F) * 0.00390625F + f4).color(f14, f15, f16, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
		                     pBuilder.vertex((double)(f18 + 8.0F), (double)(f17 + 0.0F), (double)(f19 + (float)k2 + 0.0F)).uv((f18 + 8.0F) * 0.00390625F + f3, (f19 + (float)k2 + 0.5F) * 0.00390625F + f4).color(f14, f15, f16, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
		                     pBuilder.vertex((double)(f18 + 0.0F), (double)(f17 + 0.0F), (double)(f19 + (float)k2 + 0.0F)).uv((f18 + 0.0F) * 0.00390625F + f3, (f19 + (float)k2 + 0.5F) * 0.00390625F + f4).color(f14, f15, f16, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
		                  }
		               }

		               if (l <= 1) {
		                  for(int l2 = 0; l2 < 8; ++l2) {
		                     pBuilder.vertex((double)(f18 + 0.0F), (double)(f17 + 4.0F), (double)(f19 + (float)l2 + 1.0F - 9.765625E-4F)).uv((f18 + 0.0F) * 0.00390625F + f3, (f19 + (float)l2 + 0.5F) * 0.00390625F + f4).color(f14, f15, f16, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
		                     pBuilder.vertex((double)(f18 + 8.0F), (double)(f17 + 4.0F), (double)(f19 + (float)l2 + 1.0F - 9.765625E-4F)).uv((f18 + 8.0F) * 0.00390625F + f3, (f19 + (float)l2 + 0.5F) * 0.00390625F + f4).color(f14, f15, f16, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
		                     pBuilder.vertex((double)(f18 + 8.0F), (double)(f17 + 0.0F), (double)(f19 + (float)l2 + 1.0F - 9.765625E-4F)).uv((f18 + 8.0F) * 0.00390625F + f3, (f19 + (float)l2 + 0.5F) * 0.00390625F + f4).color(f14, f15, f16, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
		                     pBuilder.vertex((double)(f18 + 0.0F), (double)(f17 + 0.0F), (double)(f19 + (float)l2 + 1.0F - 9.765625E-4F)).uv((f18 + 0.0F) * 0.00390625F + f3, (f19 + (float)l2 + 0.5F) * 0.00390625F + f4).color(f14, f15, f16, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
		                  }
		               }
		            }
		         }
		      } else {
		         for(int l1 = -32; l1 < 32; l1 += 32) {
		            for(int i2 = -32; i2 < 32; i2 += 32) {
		               pBuilder.vertex((double)(l1 + 0), (double)f17, (double)(i2 + 32)).uv((float)(l1 + 0) * 0.00390625F + f3, (float)(i2 + 32) * 0.00390625F + f4).color(f5, f6, f7, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
		               pBuilder.vertex((double)(l1 + 32), (double)f17, (double)(i2 + 32)).uv((float)(l1 + 32) * 0.00390625F + f3, (float)(i2 + 32) * 0.00390625F + f4).color(f5, f6, f7, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
		               pBuilder.vertex((double)(l1 + 32), (double)f17, (double)(i2 + 0)).uv((float)(l1 + 32) * 0.00390625F + f3, (float)(i2 + 0) * 0.00390625F + f4).color(f5, f6, f7, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
		               pBuilder.vertex((double)(l1 + 0), (double)f17, (double)(i2 + 0)).uv((float)(l1 + 0) * 0.00390625F + f3, (float)(i2 + 0) * 0.00390625F + f4).color(f5, f6, f7, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
		            }
		         }
		      }
		      
		      return pBuilder.end();
		   }
	   
	
}
	
	
	