package com.lightning.northstar.entity.renderers;

import com.lightning.northstar.entity.MercuryTortoiseEntity;
import com.lightning.northstar.entity.models.MercuryTortoiseModel;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MercuryTortoiseRenderer extends GeoEntityRenderer<MercuryTortoiseEntity> {

	public MercuryTortoiseRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new MercuryTortoiseModel());
	}
}