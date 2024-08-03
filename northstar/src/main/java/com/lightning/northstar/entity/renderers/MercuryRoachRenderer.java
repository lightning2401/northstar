package com.lightning.northstar.entity.renderers;

import com.lightning.northstar.entity.MercuryRoachEntity;
import com.lightning.northstar.entity.models.MercuryRoachModel;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MercuryRoachRenderer extends GeoEntityRenderer<MercuryRoachEntity> {

	public MercuryRoachRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new MercuryRoachModel());
	}
}