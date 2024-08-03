package com.lightning.northstar.entity.models;

import com.lightning.northstar.Northstar;
import com.lightning.northstar.entity.MarsCobraEntity;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;


public class MarsCobraModel extends DefaultedEntityGeoModel<MarsCobraEntity> {
	
	public MarsCobraModel() {
		super(new ResourceLocation(Northstar.MOD_ID, "mars_cobra"), true);
	}
}
