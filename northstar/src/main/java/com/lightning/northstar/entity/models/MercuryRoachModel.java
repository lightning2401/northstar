package com.lightning.northstar.entity.models;

import com.lightning.northstar.Northstar;
import com.lightning.northstar.entity.MercuryRoachEntity;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class MercuryRoachModel extends DefaultedEntityGeoModel<MercuryRoachEntity> {
	
	public MercuryRoachModel() {
		super(new ResourceLocation(Northstar.MOD_ID, "mercury_roach"), true);
	}
}