package com.lightning.northstar.entity.models;

import com.lightning.northstar.Northstar;
import com.lightning.northstar.entity.MercuryRaptorEntity;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class MercuryRaptorModel extends DefaultedEntityGeoModel<MercuryRaptorEntity> {

	
	public MercuryRaptorModel() {
		super(new ResourceLocation(Northstar.MOD_ID, "mercury_raptor"), true);
	}
}