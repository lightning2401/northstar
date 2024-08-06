package com.lightning.northstar.contraptions;

import com.simibubi.create.foundation.networking.SimplePacketBase;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

public class RocketContraptionQuickSyncPacket extends SimplePacketBase {
	public int contraptionEntityId;
	boolean slowing;

	public RocketContraptionQuickSyncPacket(boolean vActiveLaunch, int id) {
		slowing = vActiveLaunch;
		contraptionEntityId = id;
	}
	public RocketContraptionQuickSyncPacket(FriendlyByteBuf buffer) {
		slowing = buffer.readBoolean();
		contraptionEntityId = buffer.readInt();
	}

	@Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeBoolean(slowing);
		buffer.writeInt(contraptionEntityId);
	}

	@Override
	public boolean handle(Context context) {
		context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
				() -> () -> RocketContraptionEntity.handleQuickSyncPacket(this)));
		return true;
	}

}