package com.lightning.northstar.entity.models;

import com.lightning.northstar.Northstar;
import com.lightning.northstar.entity.VenusVultureEntity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class VenusVultureModel extends DefaultedEntityGeoModel<VenusVultureEntity> {
	
	public VenusVultureModel() {
		super(new ResourceLocation(Northstar.MOD_ID, "venus_vulture"), true);
	}
	
	@Override
	public void setCustomAnimations(VenusVultureEntity animatable, long instanceId, AnimationState<VenusVultureEntity> animationState) {

		CoreGeoBone head = getAnimationProcessor().getBone("head");
		CoreGeoBone neck = getAnimationProcessor().getBone("neck");

		if (head != null) {
			EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
			if(animatable.isFlying()) {
				if (neck != null) {
					neck.setRotX(entityData.headPitch() * -Mth.DEG_TO_RAD);
					neck.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
				}
			}else {
				head.setRotX(((entityData.headPitch() + 90) * -Mth.DEG_TO_RAD));
				head.setRotZ(-((entityData.netHeadYaw()) * Mth.DEG_TO_RAD));
			}
		}
	}
}
