package com.lightning.northstar.entity.models;

import com.lightning.northstar.Northstar;
import com.lightning.northstar.entity.MoonEelEntity;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class MoonEelModel extends DefaultedEntityGeoModel<MoonEelEntity> {
	
	public MoonEelModel() {
		super(new ResourceLocation(Northstar.MOD_ID, "moon_eel"), false);
	}
}