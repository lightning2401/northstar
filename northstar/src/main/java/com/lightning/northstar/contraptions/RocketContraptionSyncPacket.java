package com.lightning.northstar.contraptions;

import com.simibubi.create.foundation.networking.SimplePacketBase;

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
	int launchtime;
	boolean launched;
	boolean landing;
	boolean blasting;
	boolean slowing;
	boolean activeLaunch;

	public RocketContraptionSyncPacket(Vec3 syncedPos, float lift_vel2, int id,  int vLaunchtime, boolean vLaunched, boolean vLanding, boolean vBlasting, boolean vSlowing, boolean vActiveLaunch) {
		pos = syncedPos;
		lift_vel = lift_vel2;
		contraptionEntityId = id;
		launchtime = vLaunchtime;
		launched = vLaunched;
		landing = vLanding;
		blasting = vBlasting;
		slowing = vSlowing;
		activeLaunch = vActiveLaunch;
	}
	public RocketContraptionSyncPacket(FriendlyByteBuf buffer) {
		pos = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
		lift_vel = buffer.readFloat();
		contraptionEntityId = buffer.readInt();
		launchtime = buffer.readInt();
		launched = buffer.readBoolean();
		landing = buffer.readBoolean();
		blasting = buffer.readBoolean();
		slowing = buffer.readBoolean();
		activeLaunch = buffer.readBoolean();
	}

	@Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeDouble(pos.x);
		buffer.writeDouble(pos.y);
		buffer.writeDouble(pos.z);
		buffer.writeFloat(lift_vel);
		buffer.writeInt(contraptionEntityId);
		buffer.writeInt(launchtime);
		buffer.writeBoolean(launched);
		buffer.writeBoolean(landing);
		buffer.writeBoolean(blasting);
		buffer.writeBoolean(slowing);
		buffer.writeBoolean(activeLaunch);
	}

	@Override
	public boolean handle(Context context) {
		context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
				() -> () -> RocketContraptionEntity.handleSyncPacket(this)));
		return true;
	}

}