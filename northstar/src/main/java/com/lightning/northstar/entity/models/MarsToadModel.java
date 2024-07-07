package com.lightning.northstar.entity.models;

import com.lightning.northstar.client.NorthstarEntityResources;
import com.lightning.northstar.entity.MarsToadEntity;
import com.lightning.northstar.entity.MarsWormEntity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class MarsToadModel extends AnimatedTickingGeoModel<MarsToadEntity> {
	@Override
	public ResourceLocation getAnimationResource(MarsToadEntity entity) {
		return NorthstarEntityResources.TOAD_ANIMATIONS;
	}

	@Override
	public ResourceLocation getModelResource(MarsToadEntity entity) {
		return NorthstarEntityResources.TOAD_MODEL;
	}

	@Override
	public ResourceLocation getTextureResource(MarsToadEntity entity) {
		return NorthstarEntityResources.TOAD_TEXTURE;
	}
	@Override
	public void setCustomAnimations(MarsToadEntity animatable, int instanceId, AnimationEvent animationEvent) {
		super.setCustomAnimations(animatable, instanceId, animationEvent);
		IBone head = this.getAnimationProcessor().getBone("head");

		EntityModelData extraData = (EntityModelData) animationEvent.getExtraDataOfType(EntityModelData.class).get(0);
		if (head != null) {
			head.setRotationX(extraData.headPitch * Mth.DEG_TO_RAD);
			head.setRotationY(extraData.netHeadYaw * Mth.DEG_TO_RAD);
		}
	}
}
