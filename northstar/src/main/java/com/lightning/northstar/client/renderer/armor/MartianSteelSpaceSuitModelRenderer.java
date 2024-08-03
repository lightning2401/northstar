package com.lightning.northstar.client.renderer.armor;

import com.lightning.northstar.Northstar;
import com.lightning.northstar.item.armor.MartianSteelSpaceSuitArmorItem;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;


public class MartianSteelSpaceSuitModelRenderer extends GeoArmorRenderer<MartianSteelSpaceSuitArmorItem> {
	public MartianSteelSpaceSuitModelRenderer() {
		super(new DefaultedItemGeoModel<>(new ResourceLocation(Northstar.MOD_ID, "armor/martian_steel_space_suit")));

	}
}
