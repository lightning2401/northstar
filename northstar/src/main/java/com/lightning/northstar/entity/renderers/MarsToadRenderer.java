package com.lightning.northstar.entity.renderers;

import com.lightning.northstar.entity.MarsToadEntity;
import com.lightning.northstar.entity.models.MarsToadModel;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MarsToadRenderer extends GeoEntityRenderer<MarsToadEntity> {

	public MarsToadRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new MarsToadModel());
	}
}
