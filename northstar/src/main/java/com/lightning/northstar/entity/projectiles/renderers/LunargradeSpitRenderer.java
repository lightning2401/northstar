package com.lightning.northstar.entity.projectiles.renderers;

import com.lightning.northstar.entity.projectiles.LunargradeSpit;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.model.LlamaSpitModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LunargradeSpitRenderer extends EntityRenderer<LunargradeSpit> {
	   private static final ResourceLocation LLAMA_SPIT_LOCATION = new ResourceLocation("textures/entity/llama/spit.png");
	   private final LlamaSpitModel<LunargradeSpit> model;

	   public LunargradeSpitRenderer(EntityRendererProvider.Context pContext) {
	      super(pContext);
	      this.model = new LlamaSpitModel<>(pContext.bakeLayer(ModelLayers.LLAMA_SPIT));
	   }

	   public void render(LunargradeSpit pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
	      pMatrixStack.pushPose();
	      pMatrixStack.translate(0.0D, (double)0.15F, 0.0D);
	      pMatrixStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(pPartialTicks, pEntity.yRotO, pEntity.getYRot()) - 90.0F));
	      pMatrixStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(pPartialTicks, pEntity.xRotO, pEntity.getXRot())));
	      this.model.setupAnim(pEntity, pPartialTicks, 0.0F, -0.1F, 0.0F, 0.0F);
	      VertexConsumer vertexconsumer = pBuffer.getBuffer(this.model.renderType(LLAMA_SPIT_LOCATION));
	      this.model.renderToBuffer(pMatrixStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
	      pMatrixStack.popPose();
	      super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
	   }

	   /**
	    * Returns the location of an entity's texture.
	    */
	   @Override
	   public ResourceLocation getTextureLocation(LunargradeSpit pEntity) {
	      return LLAMA_SPIT_LOCATION;
	   }
}
