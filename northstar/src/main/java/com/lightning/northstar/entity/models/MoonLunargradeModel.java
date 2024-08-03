package com.lightning.northstar.entity.models;

import com.lightning.northstar.Northstar;
import com.lightning.northstar.entity.MoonLunargradeEntity;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class MoonLunargradeModel extends DefaultedEntityGeoModel<MoonLunargradeEntity> {
	public MoonLunargradeModel() {
		super(new ResourceLocation(Northstar.MOD_ID, "moon_lunargrade"), true);
	}
}
