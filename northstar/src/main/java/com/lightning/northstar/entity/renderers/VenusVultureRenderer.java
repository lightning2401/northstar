package com.lightning.northstar.entity.renderers;

import com.lightning.northstar.entity.VenusVultureEntity;
import com.lightning.northstar.entity.models.VenusVultureModel;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class VenusVultureRenderer extends GeoEntityRenderer<VenusVultureEntity> {

	public VenusVultureRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new VenusVultureModel());
	}
	
}
