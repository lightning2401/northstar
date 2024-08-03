package com.lightning.northstar.block.tech.combustion_engine;

import org.joml.Quaternionf;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.materials.oriented.OrientedData;
import com.lightning.northstar.block.tech.NorthstarPartialModels;
import com.mojang.math.Axis;
import com.simibubi.create.content.kinetics.base.ShaftInstance;
import com.simibubi.create.foundation.utility.AngleHelper;

public class CombustionEngineInstance extends ShaftInstance<CombustionEngineBlockEntity> implements DynamicInstance{
	
	private final CombustionEngineBlockEntity engine;
	private final OrientedData piston1;
	private final OrientedData piston2;
	private final OrientedData piston3;
	private final OrientedData piston4;
	private final OrientedData piston5;
	private final OrientedData piston6;
	private float time = 0;

	public CombustionEngineInstance(MaterialManager materialManager, CombustionEngineBlockEntity blockEntity) {
		super(materialManager, blockEntity);
		Quaternionf q = Axis.YP.rotationDegrees(AngleHelper.horizontalAngle(blockState.getValue(CombustionEngineBlock.HORIZONTAL_FACING)));
		piston1 = materialManager.defaultSolid().material(Materials.ORIENTED).getModel(NorthstarPartialModels.PISTON1, blockState).createInstance();
		piston1.setRotation(q);
		piston2 = materialManager.defaultSolid().material(Materials.ORIENTED).getModel(NorthstarPartialModels.PISTON2, blockState).createInstance();
		piston2.setRotation(q);
		piston3 = materialManager.defaultSolid().material(Materials.ORIENTED).getModel(NorthstarPartialModels.PISTON3, blockState).createInstance();
		piston3.setRotation(q);
		piston4 = materialManager.defaultSolid().material(Materials.ORIENTED).getModel(NorthstarPartialModels.PISTON4, blockState).createInstance();
		piston4.setRotation(q);
		piston5 = materialManager.defaultSolid().material(Materials.ORIENTED).getModel(NorthstarPartialModels.PISTON5, blockState).createInstance();
		piston5.setRotation(q);
		piston6 = materialManager.defaultSolid().material(Materials.ORIENTED).getModel(NorthstarPartialModels.PISTON6, blockState).createInstance();
		piston6.setRotation(q);
		this.engine = blockEntity;
		transformModels();
	}

	@Override
	public void beginFrame() {
		transformModels();
	}
	private void transformModels() {
		if(!engine.isOverStressed() && Math.abs(engine.getSpeed()) > 0) {
			time += engine.getSpeed() * 0.002;
		}
		piston1.setPosition(getInstancePosition()).nudge(0, getPistonOffset(time), 0);
		piston2.setPosition(getInstancePosition()).nudge(0, getPistonOffset(time + 2), 0);
		piston3.setPosition(getInstancePosition()).nudge(0, getPistonOffset(time + 4), 0);
		piston4.setPosition(getInstancePosition()).nudge(0, getPistonOffset(time + 8), 0);
		piston5.setPosition(getInstancePosition()).nudge(0, getPistonOffset(time + 10), 0);
		piston6.setPosition(getInstancePosition()).nudge(0, getPistonOffset(time + 12), 0);
	}

	@Override
	public void updateLight() {
		super.updateLight();
		relight(pos, piston1);
		relight(pos, piston2);
		relight(pos, piston3);
		relight(pos, piston4);
		relight(pos, piston5);
		relight(pos, piston6);
	}
	
	@Override
	public void remove() {
		super.remove();
		piston1.delete();
		piston2.delete();
		piston3.delete();
		piston4.delete();
		piston5.delete();
		piston6.delete();
	}
	public float getPistonOffset(double time2) {
		return (float) (Math.sin(time2) * 0.05f);
	}


}
