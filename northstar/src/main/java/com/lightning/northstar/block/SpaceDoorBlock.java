package com.lightning.northstar.block;

import com.lightning.northstar.block.entity.NorthstarBlockEntityTypes;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlock;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlockEntity;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.world.level.block.entity.BlockEntityType;

public class SpaceDoorBlock extends SlidingDoorBlock implements IBE<SlidingDoorBlockEntity> {

	public SpaceDoorBlock(Properties pProperties, boolean folds) {
		super(pProperties, folds);
	}
	
	@Override
	public Class<SlidingDoorBlockEntity> getBlockEntityClass() {
		return SlidingDoorBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends SlidingDoorBlockEntity> getBlockEntityType() {
		return NorthstarBlockEntityTypes.SPACE_DOORS.get();
	}

}
