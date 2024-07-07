package com.lightning.northstar.block.tech.oxygen_generator;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.lightning.northstar.block.tech.NorthstarPartialModels;
import com.simibubi.create.content.kinetics.base.SingleRotatingInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.foundation.render.AllMaterialSpecs;

import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;

public class OxygenGeneratorInstance extends SingleRotatingInstance<OxygenGeneratorBlockEntity> implements DynamicInstance{

	private final RotatingData propeller;
	private final OxygenGeneratorBlockEntity gen;
	
	public OxygenGeneratorInstance(MaterialManager materialManager, OxygenGeneratorBlockEntity blockEntity) {
		super(materialManager, blockEntity);
		propeller = materialManager.defaultCutout()
				.material(AllMaterialSpecs.ROTATING)
				.getModel(NorthstarPartialModels.OXYGEN_FAN, blockState)
				.createInstance();	
		propeller.setRotationAxis(Direction.Axis.Y);
		gen = blockEntity;
	}
	
	@Override
	protected Instancer<RotatingData> getModel() {
		return getRotatingMaterial().getModel(NorthstarPartialModels.HALF_SHAFT);
	}

	@Override
	public void beginFrame() {
		transformModels();
		transformSpinnys();
	}
	private void transformModels() {
	}
	
	private void transformSpinnys() {
		float speed = gen.getSpeed();
		propeller.setPosition(getInstancePosition().offset(0, 0, 0))
		.setRotationalSpeed(speed);
	}
	
	@Override
	public void updateLight() {
		super.updateLight();
		relight(pos, propeller);
	}
	
	@Override
	public void remove() {
		super.remove();
		propeller.delete();
	}
	

}
