package com.lightning.northstar.entity.renderers;

import com.lightning.northstar.entity.VenusScorpionEntity;
import com.lightning.northstar.entity.models.VenusScorpionModel;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class VenusScorpionRenderer extends GeoEntityRenderer<VenusScorpionEntity> {

	public VenusScorpionRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new VenusScorpionModel());
	}
}
