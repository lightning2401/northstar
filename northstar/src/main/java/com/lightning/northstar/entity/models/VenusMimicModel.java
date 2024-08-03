package com.lightning.northstar.entity.models;

import com.lightning.northstar.Northstar;
import com.lightning.northstar.entity.VenusMimicEntity;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class VenusMimicModel extends DefaultedEntityGeoModel<VenusMimicEntity> {
	public VenusMimicModel() {
		super(new ResourceLocation(Northstar.MOD_ID, "venus_mimic"), true);
	}
}