package com.lightning.northstar.contraptions;

import com.simibubi.create.foundation.networking.SimplePacketBase;
import com.simibubi.create.foundation.utility.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent.Context;

import java.util.UUID;

public class RocketControlPacket extends SimplePacketBase {
	int rce;
	UUID playerID;
	BlockPos localControlsPos;

	public RocketControlPacket(UUID player, int rce2, BlockPos controlsPos) {
		playerID = player;
		rce = rce2;
		localControlsPos = controlsPos;
	}
	public RocketControlPacket(FriendlyByteBuf buffer) {
		playerID = buffer.readUUID();
		localControlsPos = buffer.readBlockPos();
		rce = buffer.readInt();
	}

	@Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeUUID(playerID);
		buffer.writeBlockPos(localControlsPos);
		buffer.writeInt(rce);
	}

	@Override
	public boolean handle(Context context) {
		RocketHandler.CONTROL_QUEUE.put(Pair.of(playerID, localControlsPos), rce);
		return true;
	}

}