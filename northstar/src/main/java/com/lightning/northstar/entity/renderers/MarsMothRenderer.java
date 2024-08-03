package com.lightning.northstar.entity.renderers;

import com.lightning.northstar.entity.MarsMothEntity;
import com.lightning.northstar.entity.models.MarsCobraModel;
import com.lightning.northstar.entity.models.MarsMothModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;


public class MarsMothRenderer extends GeoEntityRenderer<MarsMothEntity> {


	public MarsMothRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new MarsMothModel());
	}
}
