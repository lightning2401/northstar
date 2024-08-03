package com.lightning.northstar.entity.models;

import com.lightning.northstar.Northstar;
import com.lightning.northstar.entity.MarsMothEntity;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class MarsMothModel extends DefaultedEntityGeoModel<MarsMothEntity> {
	
	public MarsMothModel() {
		super(new ResourceLocation(Northstar.MOD_ID, "mars_moth"), true);
	}
}
