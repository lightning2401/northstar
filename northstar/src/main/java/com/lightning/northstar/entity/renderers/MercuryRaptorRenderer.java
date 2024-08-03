package com.lightning.northstar.entity.renderers;

import com.lightning.northstar.entity.MercuryRaptorEntity;
import com.lightning.northstar.entity.models.MercuryRaptorModel;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MercuryRaptorRenderer extends GeoEntityRenderer<MercuryRaptorEntity> {

	public MercuryRaptorRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new MercuryRaptorModel());
	}

}