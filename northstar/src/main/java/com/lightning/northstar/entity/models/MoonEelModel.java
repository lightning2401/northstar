package com.lightning.northstar.entity.models;

import com.lightning.northstar.client.NorthstarEntityResources;
import com.lightning.northstar.entity.MoonEelEntity;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;

public class MoonEelModel extends AnimatedTickingGeoModel<MoonEelEntity> {
	@Override
	public ResourceLocation getAnimationResource(MoonEelEntity entity) {
		return NorthstarEntityResources.EEL_ANIMATIONS;
	}

	@Override
	public ResourceLocation getModelResource(MoonEelEntity entity) {
		return NorthstarEntityResources.EEL_MODEL;
	}

	@Override
	public ResourceLocation getTextureResource(MoonEelEntity entity) {
		return NorthstarEntityResources.EEL_TEXTURE;
	}
}