package com.lightning.northstar.block.tech.oxygen_concentrator;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.lightning.northstar.block.tech.NorthstarPartialModels;
import com.simibubi.create.content.kinetics.base.SingleRotatingInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.foundation.render.AllMaterialSpecs;

import net.minecraft.core.Direction;

public class OxygenConcentratorInstance extends SingleRotatingInstance<OxygenConcentratorBlockEntity> implements DynamicInstance{
	
	private final RotatingData fan;
	private final OxygenConcentratorBlockEntity concentrator;

	public OxygenConcentratorInstance(MaterialManager materialManager, OxygenConcentratorBlockEntity blockEntity) {
		super(materialManager, blockEntity);
		this.concentrator = blockEntity;
		fan = materialManager.defaultCutout()
				.material(AllMaterialSpecs.ROTATING)
				.getModel(NorthstarPartialModels.OXYGEN_CONCENTATOR_FAN, blockState)
				.createInstance();	
		fan.setRotationAxis(Direction.Axis.Y);
	}
	@Override
	protected Instancer<RotatingData> getModel() {
		return getRotatingMaterial().getModel(NorthstarPartialModels.HALF_SHAFT);
	}
	

	@Override
	public void beginFrame() {
		transformModels();
		transformFan();
	}
	private void transformModels() {
	}
	
	private void transformFan() {
		float speed = concentrator.getSpeed();
		fan.setPosition(getInstancePosition())
				.setRotationalSpeed(speed / 4);
	}
	@Override
	public void updateLight() {
		super.updateLight();
		relight(pos, fan);
	}
	
	@Override
	public void remove() {
		super.remove();
		fan.delete();
	}


}
