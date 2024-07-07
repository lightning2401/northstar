package com.lightning.northstar.entity.models;

import com.lightning.northstar.client.NorthstarEntityResources;
import com.lightning.northstar.entity.MarsWormEntity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class MarsWormModel extends AnimatedTickingGeoModel<MarsWormEntity> {
	@Override
	public ResourceLocation getAnimationResource(MarsWormEntity entity) {
		return NorthstarEntityResources.WORM_ANIMATIONS;
	}

	@Override
	public ResourceLocation getModelResource(MarsWormEntity entity) {
		return NorthstarEntityResources.WORM_MODEL;
	}

	@Override
	public ResourceLocation getTextureResource(MarsWormEntity entity) {
		return NorthstarEntityResources.WORM_TEXTURE;
	}
	@Override
	public void setCustomAnimations(MarsWormEntity animatable, int instanceId, AnimationEvent animationEvent) {
		super.setCustomAnimations(animatable, instanceId, animationEvent);
		IBone head = this.getAnimationProcessor().getBone("neck");

		EntityModelData extraData = (EntityModelData) animationEvent.getExtraDataOfType(EntityModelData.class).get(0);
		if (head != null) {
			head.setRotationX(extraData.headPitch * Mth.DEG_TO_RAD);
			head.setRotationY(extraData.netHeadYaw * Mth.DEG_TO_RAD);
		}
	}
}
