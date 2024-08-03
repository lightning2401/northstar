package com.lightning.northstar.entity.renderers;

import com.lightning.northstar.entity.VenusStoneBullEntity;
import com.lightning.northstar.entity.models.VenusStoneBullModel;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class VenusStoneBullRenderer extends GeoEntityRenderer<VenusStoneBullEntity> {

	public VenusStoneBullRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new VenusStoneBullModel());
	}
}
