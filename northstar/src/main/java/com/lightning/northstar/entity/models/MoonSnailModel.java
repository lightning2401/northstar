package com.lightning.northstar.entity.models;

import com.lightning.northstar.client.NorthstarEntityResources;
import com.lightning.northstar.entity.MoonLunargradeEntity;
import com.lightning.northstar.entity.MoonSnailEntity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class MoonSnailModel extends AnimatedTickingGeoModel<MoonSnailEntity> {
	@Override
	public ResourceLocation getAnimationResource(MoonSnailEntity entity) {
		return NorthstarEntityResources.SNAIL_ANIMATIONS;
	}

	@Override
	public ResourceLocation getModelResource(MoonSnailEntity entity) {
		return NorthstarEntityResources.SNAIL_MODEL;
	}

	@Override
	public ResourceLocation getTextureResource(MoonSnailEntity entity) {
		return NorthstarEntityResources.SNAIL_TEXTURE;
	}
	@Override
	public void setCustomAnimations(MoonSnailEntity animatable, int instanceId, AnimationEvent animationEvent) {
		super.setCustomAnimations(animatable, instanceId, animationEvent);
		IBone head = this.getAnimationProcessor().getBone("head");

		EntityModelData extraData = (EntityModelData) animationEvent.getExtraDataOfType(EntityModelData.class).get(0);
		if (head != null) {
			head.setRotationX(extraData.headPitch * Mth.DEG_TO_RAD);
			head.setRotationY(extraData.netHeadYaw * Mth.DEG_TO_RAD);
		}
	}
}
