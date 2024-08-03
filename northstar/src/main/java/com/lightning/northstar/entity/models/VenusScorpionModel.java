package com.lightning.northstar.entity.models;

import com.lightning.northstar.Northstar;
import com.lightning.northstar.entity.VenusScorpionEntity;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class VenusScorpionModel extends DefaultedEntityGeoModel<VenusScorpionEntity> {
	public VenusScorpionModel() {
		super(new ResourceLocation(Northstar.MOD_ID, "venus_scorpion"), true);
	}
}
