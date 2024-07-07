package com.lightning.northstar.block.tech.circuit_engraver;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.lightning.northstar.block.tech.NorthstarPartialModels;
import com.lightning.northstar.sound.NorthstarSounds;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.simibubi.create.content.kinetics.base.ShaftInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.foundation.render.AllMaterialSpecs;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;

import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;

public class EngraverInstance extends ShaftInstance<CircuitEngraverBlockEntity> implements DynamicInstance {

	private final RotatingData crystalHead;
	private final RotatingData crystalLaser;
	private final CircuitEngraverBlockEntity engraver;
	private int sound = 0;

	public EngraverInstance(MaterialManager materialManager, CircuitEngraverBlockEntity blockEntity) {
		super(materialManager, blockEntity);
		this.engraver = blockEntity;
		
		crystalHead = materialManager.defaultCutout()
				.material(AllMaterialSpecs.ROTATING)
				.getModel(NorthstarPartialModels.CIRCUIT_ENGRAVER_HEAD, blockState)
				.createInstance();	
		crystalLaser = materialManager.defaultCutout()
				.material(AllMaterialSpecs.ROTATING)
				.getModel(NorthstarPartialModels.CIRCUIT_ENGRAVER_LASER, blockState)
				.createInstance();	

		crystalHead.setRotationAxis(Direction.Axis.Y);
		crystalLaser.setRotationAxis(Direction.Axis.Y);

		Quaternion q = Vector3f.YP
			.rotationDegrees(AngleHelper.horizontalAngle(blockState.getValue(CircuitEngraverBlock.HORIZONTAL_FACING)));

		transformModels();
	}

	@Override
	public void beginFrame() {
		transformModels();
		transformHead();
	}

	private void transformModels() {
	}
	
	private void transformHead() {
		boolean runnin = engraver.engravingBehaviour.running;
		sound++;
		float speed = engraver.getRenderedHeadRotationSpeed(AnimationTickHolder.getPartialTicks());
		crystalHead.setPosition(getInstancePosition())
				.setRotationalSpeed(speed / 2);
		if (runnin) {			
		crystalLaser.setPosition(getInstancePosition().getX(), (float) (getInstancePosition().getY() + -0.16), getInstancePosition().getZ())
		.setRotationalSpeed(speed / 1.5F);
		if (sound % 60 == 0) {
			sound = 0;
		engraver.getLevel().playLocalSound(engraver.getBlockPos().getX(), engraver.getBlockPos().getY(), engraver.getBlockPos().getZ(),
				NorthstarSounds.LASER_BURN.get(), SoundSource.BLOCKS, 0.5f, 0.5f, false);}}
		else{
			crystalLaser.setPosition(getInstancePosition().getX(), (float) (getInstancePosition().getY() + -100000000), getInstancePosition().getZ());
		}
		
	}

	@Override
	public void updateLight() {
		super.updateLight();
		relight(pos, crystalHead, crystalLaser);
	}

	@Override
	public void remove() {
		super.remove();
		crystalHead.delete();
		crystalLaser.delete();
	}
}
