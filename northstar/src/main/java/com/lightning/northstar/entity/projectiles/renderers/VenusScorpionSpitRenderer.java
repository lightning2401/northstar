package com.lightning.northstar.entity.projectiles.renderers;

import com.lightning.northstar.Northstar;
import com.lightning.northstar.entity.projectiles.VenusScorpionSpit;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;

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
public class VenusScorpionSpitRenderer extends EntityRenderer<VenusScorpionSpit> {
	   private static final ResourceLocation ACID_SPIT_LOCATION = Northstar.asResource("textures/entity/acid_spit.png");
	   private final LlamaSpitModel<VenusScorpionSpit> model;

	   public VenusScorpionSpitRenderer(EntityRendererProvider.Context pContext) {
	      super(pContext);
	      this.model = new LlamaSpitModel<>(pContext.bakeLayer(ModelLayers.LLAMA_SPIT));
	   }

	   public void render(VenusScorpionSpit pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
	      pMatrixStack.pushPose();
	      pMatrixStack.translate(0.0D, (double)0.15F, 0.0D);
	      pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(pPartialTicks, pEntity.yRotO, pEntity.getYRot()) - 90.0F));
	      pMatrixStack.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(pPartialTicks, pEntity.xRotO, pEntity.getXRot())));
	      this.model.setupAnim(pEntity, pPartialTicks, 0.0F, -0.1F, 0.0F, 0.0F);
	      VertexConsumer vertexconsumer = pBuffer.getBuffer(this.model.renderType(ACID_SPIT_LOCATION));
	      this.model.renderToBuffer(pMatrixStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
	      pMatrixStack.popPose();
	      super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
	   }

	   /**
	    * Returns the location of an entity's texture.
	    */
	   @Override
	   public ResourceLocation getTextureLocation(VenusScorpionSpit pEntity) {
	      return ACID_SPIT_LOCATION;
	   }
}
