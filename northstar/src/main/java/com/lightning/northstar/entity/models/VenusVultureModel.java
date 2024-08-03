package com.lightning.northstar.entity.models;

import com.lightning.northstar.Northstar;
import com.lightning.northstar.entity.VenusVultureEntity;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class VenusVultureModel extends DefaultedEntityGeoModel<VenusVultureEntity> {
	
	public VenusVultureModel() {
		super(new ResourceLocation(Northstar.MOD_ID, "venus_vulture"), true);
	}
}
