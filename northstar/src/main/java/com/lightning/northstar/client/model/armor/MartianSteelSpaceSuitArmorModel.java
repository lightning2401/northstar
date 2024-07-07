package com.lightning.northstar.client.model.armor;

import com.lightning.northstar.client.NorthstarEntityResources;
import com.lightning.northstar.item.armor.MartianSteelSpaceSuitArmorItem;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MartianSteelSpaceSuitArmorModel extends AnimatedGeoModel<MartianSteelSpaceSuitArmorItem>{
	@Override
	public ResourceLocation getAnimationResource(MartianSteelSpaceSuitArmorItem animatable) {
		return NorthstarEntityResources.MARTIANSTEELSPACESUITARMOR_ANIMATIONS;
	}

	@Override
	public ResourceLocation getModelResource(MartianSteelSpaceSuitArmorItem object) {
		return NorthstarEntityResources.MARTIANSTEELSPACESUITARMOR_MODEL;
	}

	@Override
	public ResourceLocation getTextureResource(MartianSteelSpaceSuitArmorItem object) {
		return NorthstarEntityResources.MARTIANSTEELSPACESUITARMOR_TEXTURE;
	}

}
