package com.lightning.northstar.block.tech.cogs;

import com.lightning.northstar.block.entity.NorthstarBlockEntityTypes;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;

import net.minecraft.world.level.block.entity.BlockEntityType;

public class CustomCogBlock extends CogWheelBlock {

	protected CustomCogBlock(boolean large, Properties properties) {
		super(large, properties);
	}
	public static CustomCogBlock small(Properties properties) {
		return new CustomCogBlock(false, properties);
	}
	public static CustomCogBlock large(Properties properties) {
		return new CustomCogBlock(true, properties);
	}
	@Override
	public BlockEntityType<? extends KineticBlockEntity> getBlockEntityType() {
		return NorthstarBlockEntityTypes.BRACKETED_KINETIC.get();
	}
}
