package com.lightning.northstar.entity.models;

import com.lightning.northstar.Northstar;
import com.lightning.northstar.entity.MercuryTortoiseEntity;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class MercuryTortoiseModel extends DefaultedEntityGeoModel<MercuryTortoiseEntity> {
	
	public MercuryTortoiseModel() {
		super(new ResourceLocation(Northstar.MOD_ID, "mercury_tortoise"), true);
	}
}