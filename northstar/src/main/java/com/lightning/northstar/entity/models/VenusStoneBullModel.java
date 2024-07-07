package com.lightning.northstar.entity.models;

import com.lightning.northstar.client.NorthstarEntityResources;
import com.lightning.northstar.entity.VenusStoneBullEntity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class VenusStoneBullModel extends AnimatedTickingGeoModel<VenusStoneBullEntity> {
	@Override
	public ResourceLocation getAnimationResource(VenusStoneBullEntity entity) {
		return NorthstarEntityResources.STONE_BULL_ANIMATIONS;
	}

	@Override
	public ResourceLocation getModelResource(VenusStoneBullEntity entity) {
		return NorthstarEntityResources.STONE_BULL_MODEL;
	}

	@Override
	public ResourceLocation getTextureResource(VenusStoneBullEntity entity) {
		return NorthstarEntityResources.STONE_BULL_TEXTURE;
	}
	@Override
	public void setCustomAnimations(VenusStoneBullEntity animatable, int instanceId, AnimationEvent animationEvent) {
		super.setCustomAnimations(animatable, instanceId, animationEvent);
		IBone head = this.getAnimationProcessor().getBone("neck");

		EntityModelData extraData = (EntityModelData) animationEvent.getExtraDataOfType(EntityModelData.class).get(0);
		if (head != null) {
			head.setRotationX(extraData.headPitch * -Mth.DEG_TO_RAD);
			head.setRotationY(extraData.netHeadYaw * Mth.DEG_TO_RAD);
		}
	}
}
