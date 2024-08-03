package com.lightning.northstar.entity.renderers;

import com.lightning.northstar.entity.MoonLunargradeEntity;
import com.lightning.northstar.entity.models.MoonLunargradeModel;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MoonLunargradeRenderer extends GeoEntityRenderer<MoonLunargradeEntity> {

	public MoonLunargradeRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new MoonLunargradeModel());
	}
}
