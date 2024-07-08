package com.lightning.northstar.block.tech.rocket_station;

import com.lightning.northstar.contraptions.RocketContraption;
import com.lightning.northstar.contraptions.RocketContraptionEntity;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.behaviour.MovingInteractionBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class RocketStationBlockMovingInteraction extends MovingInteractionBehaviour{

	@Override
	public boolean handlePlayerInteraction(Player player, InteractionHand activeHand, BlockPos localPos,
		AbstractContraptionEntity contraptionEntity) {
		if (!(contraptionEntity.getContraption() instanceof RocketContraption)) {return false;}
		if (!(contraptionEntity instanceof RocketContraptionEntity)) {return false;}
		if (!contraptionEntity.level.isInWorldBounds(localPos)) {
			return false;
			
		}
		AllSoundEvents.CONTROLLER_CLICK.play(player.level, null,
			new BlockPos(contraptionEntity.toGlobalVector(Vec3.atCenterOf(localPos), 1)), 1, 1.2f);
		if(!contraptionEntity.level.isClientSide && contraptionEntity.level.isInWorldBounds(contraptionEntity.blockPosition()))
		contraptionEntity.disassemble();

		return true;
	}
}
