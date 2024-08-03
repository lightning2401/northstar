package com.lightning.northstar.entity.renderers;

import com.lightning.northstar.entity.MarsCobraEntity;
import com.lightning.northstar.entity.models.MarsCobraModel;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MarsCobraRenderer extends GeoEntityRenderer<MarsCobraEntity> {

	public MarsCobraRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new MarsCobraModel());
	}
}
