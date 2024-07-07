package com.lightning.northstar.entity.models;

import com.lightning.northstar.client.NorthstarEntityResources;
import com.lightning.northstar.entity.MarsCobraEntity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class MarsCobraModel extends AnimatedTickingGeoModel<MarsCobraEntity> {
	@Override
	public ResourceLocation getAnimationResource(MarsCobraEntity entity) {
		return NorthstarEntityResources.COBRA_ANIMATIONS;
	}

	@Override
	public ResourceLocation getModelResource(MarsCobraEntity entity) {
		return NorthstarEntityResources.COBRA_MODEL;
	}

	@Override
	public ResourceLocation getTextureResource(MarsCobraEntity entity) {
		return NorthstarEntityResources.COBRA_TEXTURE;
	}
	@SuppressWarnings("unchecked")
	@Override
	public void setCustomAnimations(MarsCobraEntity animatable, int instanceId, AnimationEvent animationEvent) {
		super.setCustomAnimations(animatable, instanceId, animationEvent);
		IBone head = this.getAnimationProcessor().getBone("head");

		EntityModelData extraData = (EntityModelData) animationEvent.getExtraDataOfType(EntityModelData.class).get(0);
		if (head != null) {
			head.setRotationX((extraData.headPitch * -Mth.DEG_TO_RAD));
			head.setRotationY(extraData.netHeadYaw * Mth.DEG_TO_RAD);
//			head.setRotationZ((extraData.netHeadYaw * Mth.DEG_TO_RAD));
		}
	}
}
