package com.lightning.northstar.block.tech.solar_panel;

import com.jozufozu.flywheel.api.MaterialManager;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityInstance;

public class SolarPanelInstance<T extends SolarPanelBlockEntity> extends KineticBlockEntityInstance<SolarPanelBlockEntity> {

	public SolarPanelInstance(MaterialManager materialManager, SolarPanelBlockEntity blockEntity) {
		super(materialManager, blockEntity);
	}

	@Override
	protected void remove() {
		
	}

}
