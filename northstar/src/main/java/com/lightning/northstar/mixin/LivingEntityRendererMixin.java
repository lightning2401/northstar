package com.lightning.northstar.mixin;

import java.util.List;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.Lists;
import com.lightning.northstar.world.dimension.NorthstarPlanets;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements RenderLayerParent<T, M> {
	   
    @Shadow
    @Final
    protected M model;
    @Shadow
    @Final
    protected final List<RenderLayer<T, M>> layers = Lists.newArrayList();
	
    private LivingEntityRendererMixin(Context pContext) {
		super(pContext);
	}

	@SuppressWarnings("resource")
	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	private void render(T pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, CallbackInfo info) {		
		if(!NorthstarPlanets.hasNormalGrav(pEntity.level.dimension()) && !pEntity.isVisuallySwimming() && !pEntity.isFallFlying() && pEntity instanceof Player) {
			if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Pre<T, M>(pEntity, (LivingEntityRenderer)(Object) this, pPartialTicks, pMatrixStack, pBuffer, pPackedLight))) return;
			pMatrixStack.pushPose();
			this.model.attackTime = this.getAttackAnim2(pEntity, pPartialTicks);
			
			boolean shouldSit = pEntity.isPassenger() && (pEntity.getVehicle() != null && pEntity.getVehicle().shouldRiderSit());
			this.model.riding = shouldSit;
			this.model.young = pEntity.isBaby();
			float f = Mth.rotLerp(pPartialTicks, pEntity.yBodyRotO, pEntity.yBodyRot);
			float f1 = Mth.rotLerp(pPartialTicks, pEntity.yHeadRotO, pEntity.yHeadRot);
			float f2 = f1 - f;
			if (shouldSit && pEntity.getVehicle() instanceof LivingEntity) {
				LivingEntity livingentity = (LivingEntity)pEntity.getVehicle();
				f = Mth.rotLerp(pPartialTicks, livingentity.yBodyRotO, livingentity.yBodyRot);
				f2 = f1 - f;
				float f3 = Mth.wrapDegrees(f2);
				if (f3 < -85.0F) {
					f3 = -85.0F;
				}
				
				if (f3 >= 85.0F) {
					f3 = 85.0F;
				}
	
				f = f1 - f3;
				if (f3 * f3 > 2500.0F) {
					f += f3 * 0.2F;
				}
	
				f2 = f1 - f;
			}
	
			float f6 = Mth.lerp(pPartialTicks, pEntity.xRotO, pEntity.getXRot());
			if (LivingEntityRenderer.isEntityUpsideDown(pEntity)) {
				f6 *= -1.0F;
				f2 *= -1.0F;
			}
	
			if (pEntity.hasPose(Pose.SLEEPING)) {
				Direction direction = pEntity.getBedOrientation();
				if (direction != null) {
					float f4 = pEntity.getEyeHeight(Pose.STANDING) - 0.1F;
					pMatrixStack.translate((double)((float)(-direction.getStepX()) * f4), 0.0D, (double)((float)(-direction.getStepZ()) * f4));
				}
			}
			
			float f7 = this.getBob2(pEntity, pPartialTicks);
			setupRotations2(pEntity, pMatrixStack, f7, f, pPartialTicks);
			pMatrixStack.scale(-1.0F, -1.0F, 1.0F);
			scale2(pEntity, pMatrixStack, pPartialTicks);
			pMatrixStack.translate(0.0D, (double)-1.501F, 0.0D);
			float f8 = 0.0F;
			float f5 = 0.0F;
			float gravMulti = (float) (!pEntity.isOnGround() ? Mth.clamp(NorthstarPlanets.getGravMultiplier(pEntity.level.dimension()), 0.25, 1) : 1);
			if (!shouldSit && pEntity.isAlive()) {
				f8 = Mth.lerp(pPartialTicks, pEntity.animationSpeedOld, pEntity.animationSpeed);
				f5 = pEntity.animationPosition - pEntity.animationSpeed * (1.0F - pPartialTicks);
				if (pEntity.isBaby()) {
					f5 *= 3.0F;
				}
				if(gravMulti < 0.7)
				{f5 *= gravMulti;}
				
				if (f8 > 1.0F) {
					f8 = 1.0F;
				}
				if(!pEntity.isOnGround() && gravMulti < 0.7 && !pEntity.isInWater() && !pEntity.isVisuallySwimming() && !pEntity.isFallFlying())
				{
					f8 *= 1.2;}
			}
			
			this.model.prepareMobModel(pEntity, f5, f8, pPartialTicks);
			this.model.setupAnim(pEntity, f5, f8, f7, f2, f6);
			Minecraft minecraft = Minecraft.getInstance();
			boolean flag = isBodyVisible2(pEntity);
			boolean flag1 = !flag && !pEntity.isInvisibleTo(minecraft.player);
			boolean flag2 = minecraft.shouldEntityAppearGlowing(pEntity);
			RenderType rendertype = getRenderType2(pEntity, flag, flag1, flag2);
			if (rendertype != null) {
				VertexConsumer vertexconsumer = pBuffer.getBuffer(rendertype);
				int i = LivingEntityRenderer.getOverlayCoords(pEntity, 0.0f);
				this.model.renderToBuffer(pMatrixStack, vertexconsumer, pPackedLight, i, 1.0F, 1.0F, 1.0F, flag1 ? 0.15F : 1.0F);
			}
	
			if (!pEntity.isSpectator()) {
				for(RenderLayer<T, M> renderlayer : layers) {
					renderlayer.render(pMatrixStack, pBuffer, pPackedLight, pEntity, f5, f8, pPartialTicks, f7, f2, f6);
				}
			}
	
			pMatrixStack.popPose();
			super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
			net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Post<T, M>(pEntity, (LivingEntityRenderer)(Object) this, pPartialTicks, pMatrixStack, pBuffer, pPackedLight));
			info.cancel();
		}
	}
	
	private void scale2(T pLivingEntity, PoseStack pMatrixStack, float pPartialTickTime) {
	}
	
	private boolean isBodyVisible2(T pLivingEntity) {
		return !pLivingEntity.isInvisible();
	}
	
	@Nullable
	private RenderType getRenderType2(T pLivingEntity, boolean pBodyVisible, boolean pTranslucent, boolean pGlowing) {
	      ResourceLocation resourcelocation = this.getTextureLocation(pLivingEntity);
	      if (pTranslucent) {
	         return RenderType.itemEntityTranslucentCull(resourcelocation);
	      } else if (pBodyVisible) {
	         return this.model.renderType(resourcelocation);
	      } else {
	         return pGlowing ? RenderType.outline(resourcelocation) : null;
	      }
	   }
	
	private boolean isShaking2(T pEntity) {
	      return pEntity.isFullyFrozen();
	}
	
	private static float sleepDirectionToRotation2(Direction pFacing) {
	      switch (pFacing) {
	         case SOUTH:
	            return 90.0F;
	         case WEST:
	            return 0.0F;
	         case NORTH:
	            return 270.0F;
	         case EAST:
	            return 180.0F;
	         default:
	            return 0.0F;
	      }
	   }
	
	private static boolean isEntityUpsideDown2(LivingEntity pEntity) {
		if (pEntity instanceof Player || pEntity.hasCustomName()) {
			String s = ChatFormatting.stripFormatting(pEntity.getName().getString());
			if ("Dinnerbone".equals(s) || "Grumm".equals(s)) {
				return !(pEntity instanceof Player) || ((Player)pEntity).isModelPartShown(PlayerModelPart.CAPE);
			}
		}
		return false;
	}
	
	private void setupRotations2(T pEntityLiving, PoseStack pMatrixStack, float pAgeInTicks, float pRotationYaw, float pPartialTicks) {
	      if (this.isShaking2(pEntityLiving)) {
	         pRotationYaw += (float)(Math.cos((double)pEntityLiving.tickCount * 3.25D) * Math.PI * (double)0.4F);
	      }

	      if (!pEntityLiving.hasPose(Pose.SLEEPING)) {
	         pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0F - pRotationYaw));
	      }

	      if (pEntityLiving.deathTime > 0) {
	         float f = ((float)pEntityLiving.deathTime + pPartialTicks - 1.0F) / 20.0F * 1.6F;
	         f = Mth.sqrt(f);
	         if (f > 1.0F) {
	            f = 1.0F;
	         }

	         pMatrixStack.mulPose(Vector3f.ZP.rotationDegrees(f * 90));
	      } else if (pEntityLiving.isAutoSpinAttack()) {
	         pMatrixStack.mulPose(Vector3f.XP.rotationDegrees(-90.0F - pEntityLiving.getXRot()));
	         pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(((float)pEntityLiving.tickCount + pPartialTicks) * -75.0F));
	      } else if (pEntityLiving.hasPose(Pose.SLEEPING)) {
	         Direction direction = pEntityLiving.getBedOrientation();
	         float f1 = direction != null ? sleepDirectionToRotation2(direction) : pRotationYaw;
	         pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(f1));
	         pMatrixStack.mulPose(Vector3f.ZP.rotationDegrees(90));
	         pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(270.0F));
	      } else if (isEntityUpsideDown2(pEntityLiving)) {
	         pMatrixStack.translate(0.0D, (double)(pEntityLiving.getBbHeight() + 0.1F), 0.0D);
	         pMatrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
	      }

	   }
	
	
	private float getAttackAnim2(T pLivingBase, float pPartialTickTime) {
		return pLivingBase.getAttackAnim(pPartialTickTime);
	}
	private float getBob2(T pLivingBase, float pPartialTick) {
		return (float)pLivingBase.tickCount + pPartialTick;
	}

	@Override
	public M getModel() {
		return this.model;
	}
}
