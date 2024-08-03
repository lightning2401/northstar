package com.lightning.northstar.entity.renderers;

import com.lightning.northstar.entity.MoonEelEntity;
import com.lightning.northstar.entity.models.MoonEelModel;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MoonEelRenderer extends GeoEntityRenderer<MoonEelEntity> {

	public MoonEelRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new MoonEelModel());
	}
}