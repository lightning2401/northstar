package com.lightning.northstar.block.tech.rocket_controls;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import com.lightning.northstar.contraptions.RocketContraptionEntity;
import com.simibubi.create.foundation.networking.SimplePacketBase;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent.Context;

public class RocketControlsInputPacket extends SimplePacketBase {

	private Collection<Integer> activatedButtons;
	private boolean press;
	private int contraptionEntityId;
	private BlockPos controlsPos;
	private boolean stopControlling;

	public RocketControlsInputPacket(Collection<Integer> activatedButtons, boolean press, int contraptionEntityId,
		BlockPos controlsPos, boolean stopControlling) {
		this.contraptionEntityId = contraptionEntityId;
		this.activatedButtons = activatedButtons;
		this.press = press;
		this.controlsPos = controlsPos;
		this.stopControlling = stopControlling;
	}

	public RocketControlsInputPacket(FriendlyByteBuf buffer) {
		contraptionEntityId = buffer.readInt();
		activatedButtons = new ArrayList<>();
		press = buffer.readBoolean();
		int size = buffer.readVarInt();
		for (int i = 0; i < size; i++)
			activatedButtons.add(buffer.readVarInt());
		controlsPos = buffer.readBlockPos();
		stopControlling = buffer.readBoolean();
	}

	@Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(contraptionEntityId);
		buffer.writeBoolean(press);
		buffer.writeVarInt(activatedButtons.size());
		activatedButtons.forEach(buffer::writeVarInt);
		buffer.writeBlockPos(controlsPos);
		buffer.writeBoolean(stopControlling);
	}

	@Override
	public boolean handle(Context context) {
		context.enqueueWork(() -> {
			ServerPlayer player = context.getSender();
			Level world = player.getCommandSenderWorld();
			UUID uniqueID = player.getUUID();

			if (player.isSpectator() && press)
				return;

			Entity entity = world.getEntity(contraptionEntityId);
			if (!(entity instanceof RocketContraptionEntity rce))
				return;
			if (stopControlling) {
				rce.stopControlling(controlsPos);
				return;
			}

			if (rce.toGlobalVector(Vec3.atCenterOf(controlsPos), 0)
				.closerThan(player.position(), 16))
//				System.out.println("Key press Detected!");
				RocketControlsServerHandler.receivePressed(world, rce, controlsPos, uniqueID, activatedButtons, press);
		});
		return true;
	}

}