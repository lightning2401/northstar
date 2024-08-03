package com.lightning.northstar.entity.models;

import com.lightning.northstar.Northstar;
import com.lightning.northstar.entity.MoonSnailEntity;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class MoonSnailModel extends DefaultedEntityGeoModel<MoonSnailEntity> {
	public MoonSnailModel() {
		super(new ResourceLocation(Northstar.MOD_ID, "moon_snail"), true);
	}
}
