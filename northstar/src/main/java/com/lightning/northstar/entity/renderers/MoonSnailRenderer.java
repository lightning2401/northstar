package com.lightning.northstar.entity.renderers;

import com.lightning.northstar.entity.MoonSnailEntity;
import com.lightning.northstar.entity.models.MoonSnailModel;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MoonSnailRenderer extends GeoEntityRenderer<MoonSnailEntity> {

	public MoonSnailRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new MoonSnailModel());
	}
}
