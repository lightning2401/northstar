package com.lightning.northstar.client.model.armor;

import com.lightning.northstar.client.NorthstarEntityResources;
import com.lightning.northstar.item.armor.IronSpaceSuitArmorItem;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class IronSpaceSuitArmorModel extends AnimatedGeoModel<IronSpaceSuitArmorItem>{
	@Override
	public ResourceLocation getAnimationResource(IronSpaceSuitArmorItem animatable) {
		return NorthstarEntityResources.IRONSPACESUITARMOR_ANIMATIONS;
	}

	@Override
	public ResourceLocation getModelResource(IronSpaceSuitArmorItem object) {
		return NorthstarEntityResources.IRONSPACESUITARMOR_MODEL;
	}

	@Override
	public ResourceLocation getTextureResource(IronSpaceSuitArmorItem object) {
		return NorthstarEntityResources.IRONSPACESUITARMOR_TEXTURE;
	}

}
