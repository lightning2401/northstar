package com.lightning.northstar.entity.renderers;

import com.lightning.northstar.entity.MarsWormEntity;
import com.lightning.northstar.entity.models.MarsWormModel;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MarsWormRenderer extends GeoEntityRenderer<MarsWormEntity> {

	public MarsWormRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new MarsWormModel());
	}

}
