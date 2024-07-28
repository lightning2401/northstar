package com.lightning.northstar.block.tech.rocket_controls;

import java.util.UUID;

import com.google.common.base.Objects;
import com.lightning.northstar.contraptions.RocketContraptionEntity;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.behaviour.MovingInteractionBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class RocketControlsInteractionBehaviour extends MovingInteractionBehaviour {
	@Override
	public boolean handlePlayerInteraction(Player player, InteractionHand activeHand, BlockPos localPos,
		AbstractContraptionEntity contraptionEntity) {
		if(!(contraptionEntity instanceof RocketContraptionEntity rce))
			return false;

//		System.out.println("Huhh????");

		UUID currentlyControlling = rce.getControllingPlayer()
			.orElse(null);

		if (currentlyControlling != null) {
			rce.stopControlling(localPos);
			if (Objects.equal(currentlyControlling, player.getUUID()))
				return true;
		}

//		System.out.println("I LIVED!!!!!!!!!");
		if (!contraptionEntity.startControlling(localPos, player))
			return false;

		rce.setControllingPlayer(player.getUUID());
		if (player.level.isClientSide)
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
				() -> () -> RocketControlsHandler.startControlling(rce, localPos));
		return true;
	}
}
