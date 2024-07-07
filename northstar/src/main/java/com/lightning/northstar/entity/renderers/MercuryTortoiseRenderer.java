package com.lightning.northstar.entity.renderers;

import com.lightning.northstar.entity.MercuryTortoiseEntity;
import com.lightning.northstar.entity.models.MercuryTortoiseModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class MercuryTortoiseRenderer extends GeoEntityRenderer<MercuryTortoiseEntity> {

	public MercuryTortoiseRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new MercuryTortoiseModel());
	}

	@Override
	public RenderType getRenderType(MercuryTortoiseEntity animatable, float partialTick, PoseStack poseStack,
									MultiBufferSource bufferSource, VertexConsumer buffer, int packedLight,
									ResourceLocation texture) {
		return RenderType.entityCutout(getTextureLocation(animatable));
	}

	@Override
	public void render(GeoModel model, MercuryTortoiseEntity animatable, float partialTick, RenderType type,
					   PoseStack poseStack, MultiBufferSource bufferSource, VertexConsumer buffer,
					   int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		super.render(model, animatable, partialTick, type, poseStack, bufferSource, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}