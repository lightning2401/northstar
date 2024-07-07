package com.lightning.northstar.contraptions;

import com.simibubi.create.content.contraptions.render.ContraptionEntityRenderer;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class RocketContraptionEntityRenderer extends ContraptionEntityRenderer<RocketContraptionEntity> {

	public RocketContraptionEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
	}
	
	@Override
	public boolean shouldRender(RocketContraptionEntity entity, Frustum clippingHelper, double cameraX, double cameraY,
		double cameraZ) {
		if (entity.getContraption() == null)
			return false;
		if (!entity.isAliveOrStale())
			return false;
		if (!entity.isReadyForRender())
			return false;

		return super.shouldRender(entity, clippingHelper, cameraX, cameraY, cameraZ);
	}
}
