package com.lightning.northstar.client.renderer.armor;

import com.lightning.northstar.client.model.armor.BrokenIronSpaceSuitArmorModel;
import com.lightning.northstar.item.armor.BrokenIronSpaceSuitArmorItem;

import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class BrokenIronSpaceSuitModelRenderer extends GeoArmorRenderer<BrokenIronSpaceSuitArmorItem> {
	public BrokenIronSpaceSuitModelRenderer() {
		super(new BrokenIronSpaceSuitArmorModel());

		// These values are what each bone name is in blockbench. So if your head bone
		// is named "bone545", make sure to do this.headBone = "bone545";
		// The default values are the ones that come with the default armor template in
		// the geckolib blockbench plugin.
		this.headBone = "armorHead";
		this.bodyBone = "armorChest";
		this.rightArmBone = "armorRightArm";
		this.leftArmBone = "armorLeftArm";
		this.rightLegBone = "armorRightLeg";
		this.leftLegBone = "armorLeftLeg";
		this.rightBootBone = "armorRightBoot";
		this.leftBootBone = "armorLeftBoot";
	}
}
