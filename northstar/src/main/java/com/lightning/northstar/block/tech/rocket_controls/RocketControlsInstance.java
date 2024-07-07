package com.lightning.northstar.block.tech.rocket_controls;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.materials.oriented.OrientedData;
import com.lightning.northstar.block.tech.NorthstarPartialModels;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.simibubi.create.foundation.utility.AngleHelper;

public class RocketControlsInstance extends BlockEntityInstance<RocketControlsBlockEntity> implements DynamicInstance {

	private final OrientedData lever;
	
	public RocketControlsInstance(MaterialManager materialManager, RocketControlsBlockEntity blockEntity) {
		super(materialManager, blockEntity);
		Quaternion q = Vector3f.YP.rotationDegrees(AngleHelper.horizontalAngle(blockState.getValue(RocketControlsBlock.FACING)));
		lever = materialManager.defaultSolid()
				.material(Materials.ORIENTED)
				.getModel(NorthstarPartialModels.CONTROL_LEVER_BLOCK, blockState)
				.createInstance();
		lever.setRotation(q);
	}	
	@Override
	public void beginFrame() {
		lever.setPosition(getInstancePosition());
	}
	
	@Override
	public void updateLight() {
		super.updateLight();
		relight(pos, lever);
	}

	@Override
	protected void remove() {
		lever.delete();
	}
	
}
