package com.lightning.northstar.entity.models;

import com.lightning.northstar.Northstar;
import com.lightning.northstar.entity.VenusStoneBullEntity;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class VenusStoneBullModel extends DefaultedEntityGeoModel<VenusStoneBullEntity> {
	public VenusStoneBullModel() {
		super(new ResourceLocation(Northstar.MOD_ID, "venus_stone_bull"), true);
	}
}
