package com.lightning.northstar.entity.models;

import com.lightning.northstar.client.NorthstarEntityResources;
import com.lightning.northstar.entity.VenusScorpionEntity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class VenusScorpionModel extends AnimatedTickingGeoModel<VenusScorpionEntity> {
	@Override
	public ResourceLocation getAnimationResource(VenusScorpionEntity entity) {
		return NorthstarEntityResources.SCORPION_ANIMATIONS;
	}

	@Override
	public ResourceLocation getModelResource(VenusScorpionEntity entity) {
		return NorthstarEntityResources.SCORPION_MODEL;
	}

	@Override
	public ResourceLocation getTextureResource(VenusScorpionEntity entity) {
		return NorthstarEntityResources.SCORPION_TEXTURE;
	}
	@Override
	public void setCustomAnimations(VenusScorpionEntity animatable, int instanceId, AnimationEvent animationEvent) {
		super.setCustomAnimations(animatable, instanceId, animationEvent);
		IBone head = this.getAnimationProcessor().getBone("head");

		EntityModelData extraData = (EntityModelData) animationEvent.getExtraDataOfType(EntityModelData.class).get(0);
		if (head != null) {
			head.setRotationX(extraData.headPitch * Mth.DEG_TO_RAD);
			head.setRotationY(extraData.netHeadYaw * Mth.DEG_TO_RAD);
		}
	}
}
