package com.lightning.northstar.client.renderer.armor;

import com.lightning.northstar.Northstar;
import com.lightning.northstar.item.armor.IronSpaceSuitArmorItem;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class IronSpaceSuitModelRenderer extends GeoArmorRenderer<IronSpaceSuitArmorItem> {
	public IronSpaceSuitModelRenderer() {
		super(new DefaultedItemGeoModel<>(new ResourceLocation(Northstar.MOD_ID, "armor/iron_space_suit")));
	}
}
