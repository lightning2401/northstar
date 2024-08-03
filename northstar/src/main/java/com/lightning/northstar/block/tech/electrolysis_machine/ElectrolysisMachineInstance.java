package com.lightning.northstar.block.tech.electrolysis_machine;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.lightning.northstar.block.tech.NorthstarPartialModels;
import com.simibubi.create.content.kinetics.base.SingleRotatingInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;

public class ElectrolysisMachineInstance extends SingleRotatingInstance<ElectrolysisMachineBlockEntity> implements DynamicInstance{
	
	@SuppressWarnings("unused")
	private final ElectrolysisMachineBlockEntity elecMac;

	public ElectrolysisMachineInstance(MaterialManager materialManager, ElectrolysisMachineBlockEntity blockEntity) {
		super(materialManager, blockEntity);
		this.elecMac = blockEntity;
	}
	@Override
	protected Instancer<RotatingData> getModel() {
		return getRotatingMaterial().getModel(NorthstarPartialModels.HALF_SHAFT);
	}

	
	@Override
	public void remove() {
		super.remove();
	}
	@Override
	public void beginFrame() {}
}