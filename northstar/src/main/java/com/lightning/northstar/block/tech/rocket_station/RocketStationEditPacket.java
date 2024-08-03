package com.lightning.northstar.block.tech.rocket_station;

import com.simibubi.create.foundation.networking.BlockEntityConfigurationPacket;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class RocketStationEditPacket extends BlockEntityConfigurationPacket<RocketStationBlockEntity> {

	Boolean tryAssemble;

	public static RocketStationEditPacket dropSchedule(BlockPos pos) {
		RocketStationEditPacket packet = new RocketStationEditPacket(pos);
		return packet;
	}

	public static RocketStationEditPacket tryAssemble(BlockPos pos) {
		RocketStationEditPacket packet = new RocketStationEditPacket(pos);
		packet.tryAssemble = true;
		return packet;
	}

	public static RocketStationEditPacket tryDisassemble(BlockPos pos) {
		RocketStationEditPacket packet = new RocketStationEditPacket(pos);
		packet.tryAssemble = false;
		return packet;
	}

	public static RocketStationEditPacket configure(BlockPos pos, boolean assemble) {
		RocketStationEditPacket packet = new RocketStationEditPacket(pos);
		packet.tryAssemble = assemble;
		return packet;
	}

	public RocketStationEditPacket(FriendlyByteBuf buffer) {
		super(buffer);
	}

	public RocketStationEditPacket(BlockPos pos) {
		super(pos);
	}

	@Override
	protected void writeSettings(FriendlyByteBuf buffer) {
		buffer.writeBoolean(tryAssemble != null);
		if (tryAssemble != null) {
			buffer.writeBoolean(tryAssemble);
			return;
		}
	}

	@Override
	protected void readSettings(FriendlyByteBuf buffer) {
		if (buffer.readBoolean()) {
			tryAssemble = buffer.readBoolean();
			return;
		}
	}

	@Override
	protected void applySettings(ServerPlayer player, RocketStationBlockEntity be) {
		Level level = be.getLevel();
		BlockPos blockPos = be.getBlockPos();
		BlockState blockState = level.getBlockState(blockPos);

		if (!(blockState.getBlock() instanceof RocketStationBlock))
			return;

		if (tryAssemble)
			be.queueAssembly(player);
		else
			return;
	}

	@Override
	protected void applySettings(RocketStationBlockEntity be) {}

}