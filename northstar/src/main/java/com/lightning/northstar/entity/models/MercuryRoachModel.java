package com.lightning.northstar.entity.models;

import com.lightning.northstar.client.NorthstarEntityResources;
import com.lightning.northstar.entity.MercuryRaptorEntity;
import com.lightning.northstar.entity.MercuryRoachEntity;
import com.lightning.northstar.entity.MoonLunargradeEntity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class MercuryRoachModel extends AnimatedTickingGeoModel<MercuryRoachEntity> {
	@Override
	public ResourceLocation getAnimationResource(MercuryRoachEntity entity) {
		return NorthstarEntityResources.ROACH_ANIMATIONS;
	}

	@Override
	public ResourceLocation getModelResource(MercuryRoachEntity entity) {
		return NorthstarEntityResources.ROACH_MODEL;
	}

	@Override
	public ResourceLocation getTextureResource(MercuryRoachEntity entity) {
		return NorthstarEntityResources.ROACH_TEXTURE;
	}
	@Override
	public void setCustomAnimations(MercuryRoachEntity animatable, int instanceId, AnimationEvent animationEvent) {
		super.setCustomAnimations(animatable, instanceId, animationEvent);
		IBone head = this.getAnimationProcessor().getBone("neck");

		EntityModelData extraData = (EntityModelData) animationEvent.getExtraDataOfType(EntityModelData.class).get(0);
		if (head != null) {
			head.setRotationX(extraData.headPitch * Mth.DEG_TO_RAD);
			head.setRotationY(extraData.netHeadYaw * Mth.DEG_TO_RAD);
		}
	}
}