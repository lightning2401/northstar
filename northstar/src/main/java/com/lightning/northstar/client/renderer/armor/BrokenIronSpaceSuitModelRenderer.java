package com.lightning.northstar.client.renderer.armor;

import com.lightning.northstar.Northstar;
import com.lightning.northstar.item.armor.BrokenIronSpaceSuitArmorItem;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;


public class BrokenIronSpaceSuitModelRenderer extends GeoArmorRenderer<BrokenIronSpaceSuitArmorItem> {
	public BrokenIronSpaceSuitModelRenderer() {
		super(new DefaultedItemGeoModel<>(new ResourceLocation(Northstar.MOD_ID, "armor/broken_iron_space_suit")));
	}
}
