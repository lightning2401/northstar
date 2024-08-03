package com.lightning.northstar.entity.models;

import com.lightning.northstar.Northstar;
import com.lightning.northstar.entity.MarsWormEntity;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class MarsWormModel extends DefaultedEntityGeoModel<MarsWormEntity> {
	
	public MarsWormModel() {
		super(new ResourceLocation(Northstar.MOD_ID, "mars_worm"), true);
	}
}
