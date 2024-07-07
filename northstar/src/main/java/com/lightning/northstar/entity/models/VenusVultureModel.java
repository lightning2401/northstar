package com.lightning.northstar.entity.models;

import com.lightning.northstar.client.NorthstarEntityResources;
import com.lightning.northstar.entity.VenusVultureEntity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class VenusVultureModel extends AnimatedTickingGeoModel<VenusVultureEntity> {
	@Override
	public ResourceLocation getAnimationResource(VenusVultureEntity entity) {
		return NorthstarEntityResources.VULTURE_ANIMATIONS;
	}

	@Override
	public ResourceLocation getModelResource(VenusVultureEntity entity) {
		return NorthstarEntityResources.VULTURE_MODEL;
	}

	@Override
	public ResourceLocation getTextureResource(VenusVultureEntity entity) {
		return NorthstarEntityResources.VULTURE_TEXTURE;
	}
	@Override
	public void setCustomAnimations(VenusVultureEntity animatable, int instanceId, AnimationEvent animationEvent) {
		super.setCustomAnimations(animatable, instanceId, animationEvent);
		IBone neck = this.getAnimationProcessor().getBone("neck");
		IBone head = this.getAnimationProcessor().getBone("head");

		EntityModelData extraData = (EntityModelData) animationEvent.getExtraDataOfType(EntityModelData.class).get(0);
		if(animatable.isFlying()) {
			if (neck != null) {
				neck.setRotationX(extraData.headPitch * -Mth.DEG_TO_RAD);
				neck.setRotationY(extraData.netHeadYaw * Mth.DEG_TO_RAD);
			}
		}else {
			head.setRotationX(((extraData.headPitch + 90) * -Mth.DEG_TO_RAD));
			head.setRotationZ(-((extraData.netHeadYaw) * Mth.DEG_TO_RAD));
		}
	}
}
