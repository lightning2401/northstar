package com.lightning.northstar.entity.models;

import com.lightning.northstar.client.NorthstarEntityResources;
import com.lightning.northstar.entity.VenusMimicEntity;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;

public class VenusMimicModel extends AnimatedTickingGeoModel<VenusMimicEntity> {
	@Override
	public ResourceLocation getAnimationResource(VenusMimicEntity entity) {
		return NorthstarEntityResources.MIMIC_ANIMATIONS;
	}

	@Override
	public ResourceLocation getModelResource(VenusMimicEntity entity) {
		return NorthstarEntityResources.MIMIC_MODEL;
	}

	@Override
	public ResourceLocation getTextureResource(VenusMimicEntity entity) {
		if(entity.isDeep) {
			return NorthstarEntityResources.MIMIC_DEEP_TEXTURE;
		}
		return NorthstarEntityResources.MIMIC_TEXTURE;
	}
}
