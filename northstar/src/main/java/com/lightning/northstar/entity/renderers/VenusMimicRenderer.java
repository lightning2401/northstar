package com.lightning.northstar.entity.renderers;

import com.lightning.northstar.entity.VenusMimicEntity;
import com.lightning.northstar.entity.models.VenusMimicModel;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class VenusMimicRenderer extends GeoEntityRenderer<VenusMimicEntity> {

	public VenusMimicRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new VenusMimicModel());
	}
}
