package com.lightning.northstar.block.tech.temperature_regulator;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.lightning.northstar.block.tech.NorthstarPartialModels;
import com.lightning.northstar.world.dimension.NorthstarPlanets;
import com.simibubi.create.content.kinetics.base.SingleRotatingInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.foundation.render.AllMaterialSpecs;

import net.minecraft.core.Direction;

public class TemperatureRegulatorInstance extends SingleRotatingInstance<TemperatureRegulatorBlockEntity> implements DynamicInstance{
	
	private final RotatingData warmSpinny;
	private final RotatingData coldSpinny;
	private final TemperatureRegulatorBlockEntity regulator;

	public TemperatureRegulatorInstance(MaterialManager materialManager, TemperatureRegulatorBlockEntity blockEntity) {
		super(materialManager, blockEntity);
		this.regulator = blockEntity;
		warmSpinny = materialManager.defaultCutout()
				.material(AllMaterialSpecs.ROTATING)
				.getModel(NorthstarPartialModels.WARM_SPINNY, blockState)
				.createInstance();	
		warmSpinny.setRotationAxis(Direction.Axis.Y);
		coldSpinny = materialManager.defaultCutout()
				.material(AllMaterialSpecs.ROTATING)
				.getModel(NorthstarPartialModels.COLD_SPINNY, blockState)
				.createInstance();	
		coldSpinny.setRotationAxis(Direction.Axis.Y);
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
		float speed = regulator.getSpeed();
		if(regulator.temp >= NorthstarPlanets.getPlanetTemp(world.dimension())) {
			warmSpinny.setPosition(getInstancePosition())
			.setRotationalSpeed(speed / 2);
			coldSpinny.setPosition(getInstancePosition().offset(0, -1500, 0))
			.setRotationalSpeed(speed / 2);
		}
		if(regulator.temp < NorthstarPlanets.getPlanetTemp(world.dimension())) {
			warmSpinny.setPosition(getInstancePosition().offset(0, -1500, 0))
			.setRotationalSpeed(speed / 2);
			coldSpinny.setPosition(getInstancePosition())
			.setRotationalSpeed(speed / 2);
		}
	}
	
	@Override
	public void updateLight() {
		super.updateLight();
		relight(pos, warmSpinny);
		relight(pos, coldSpinny);
	}
	
	@Override
	public void remove() {
		super.remove();
		warmSpinny.delete();
		coldSpinny.delete();
	}

}
