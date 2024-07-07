package com.lightning.northstar.contraptions;

import com.simibubi.create.foundation.networking.SimplePacketBase;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

public class RocketContraptionSyncPacket extends SimplePacketBase {
	RocketContraptionEntity entity;
	public int contraptionEntityId;
	Vec3 pos;
	float lift_vel;

	public RocketContraptionSyncPacket(Vec3 syncedPos, float lift_vel2, int id) {
		pos = syncedPos;

		lift_vel = lift_vel2;

		contraptionEntityId = id;
	}
	public RocketContraptionSyncPacket(FriendlyByteBuf buffer) {
		pos = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
		lift_vel = buffer.readFloat();
		contraptionEntityId = buffer.readInt();
	}

	@Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeDouble(pos.x);
		buffer.writeDouble(pos.y);
		buffer.writeDouble(pos.z);
		buffer.writeFloat(lift_vel);
		buffer.writeInt(contraptionEntityId);
	}

	@Override
	public boolean handle(Context context) {
		context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
				() -> () -> RocketContraptionEntity.handleSyncPacket(this)));
		return true;
	}

}