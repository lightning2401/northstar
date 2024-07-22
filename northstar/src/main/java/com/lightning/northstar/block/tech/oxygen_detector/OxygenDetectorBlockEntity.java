package com.lightning.northstar.block.tech.oxygen_detector;

import java.util.List;

import com.lightning.northstar.world.OxygenStuff;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class OxygenDetectorBlockEntity extends SmartBlockEntity {

	public OxygenDetectorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
		
	}
	
	@Override
	public void tick() {
		BlockState currentState = this.level.getBlockState(worldPosition);
		Direction dir = currentState.getValue(OxygenDetectorBlock.FACING);
		BlockPos facingpos = worldPosition.relative(dir);
		boolean oxyflag = OxygenStuff.hasOxygen(facingpos, level.dimension());

		if(oxyflag && !currentState.getValue(OxygenDetectorBlock.POWERED)) {
			this.level.setBlock(worldPosition, currentState.setValue(OxygenDetectorBlock.POWERED, true), 3);
		}else if(!oxyflag && currentState.getValue(OxygenDetectorBlock.POWERED)){
			this.level.setBlock(worldPosition, currentState.setValue(OxygenDetectorBlock.POWERED, false), 3);
		}
	}

}
