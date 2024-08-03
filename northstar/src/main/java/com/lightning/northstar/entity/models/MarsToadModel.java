package com.lightning.northstar.entity.models;

import com.lightning.northstar.Northstar;
import com.lightning.northstar.entity.MarsToadEntity;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;


public class MarsToadModel extends DefaultedEntityGeoModel<MarsToadEntity> {
	
	public MarsToadModel() {
		super(new ResourceLocation(Northstar.MOD_ID, "mars_toad"), true);
	}
}
