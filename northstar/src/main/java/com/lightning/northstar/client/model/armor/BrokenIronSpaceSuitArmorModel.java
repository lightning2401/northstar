package com.lightning.northstar.client.model.armor;

import com.lightning.northstar.client.NorthstarEntityResources;
import com.lightning.northstar.item.armor.BrokenIronSpaceSuitArmorItem;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class BrokenIronSpaceSuitArmorModel extends AnimatedGeoModel<BrokenIronSpaceSuitArmorItem>{
	@Override
	public ResourceLocation getAnimationResource(BrokenIronSpaceSuitArmorItem animatable) {
		return NorthstarEntityResources.IRONSPACESUITARMOR_ANIMATIONS;
	}

	@Override
	public ResourceLocation getModelResource(BrokenIronSpaceSuitArmorItem object) {
		return NorthstarEntityResources.IRONSPACESUITARMOR_MODEL;
	}

	@Override
	public ResourceLocation getTextureResource(BrokenIronSpaceSuitArmorItem object) {
		return NorthstarEntityResources.BROKEN_IRONSPACESUITARMOR_TEXTURE;
	}

}
