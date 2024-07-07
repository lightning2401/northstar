package com.lightning.northstar.block.tech.telescope;

import com.simibubi.create.foundation.networking.BlockEntityConfigurationPacket;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class TelescopePrintPacket extends BlockEntityConfigurationPacket<TelescopeBlockEntity> {
	
	private String planetname;
	

	public TelescopePrintPacket(FriendlyByteBuf buffer) {
		super(buffer);
	}

	public TelescopePrintPacket(BlockPos pos) {
		super(pos);
	}
	
	public static TelescopePrintPacket print(BlockPos pos, String strin) {
		TelescopePrintPacket packet = new TelescopePrintPacket(pos);
		packet.planetname = strin;
		return packet;
	}

	@Override
	protected void writeSettings(FriendlyByteBuf buffer) {
		buffer.writeComponent(Component.literal(planetname));
	}

	@Override
	protected void readSettings(FriendlyByteBuf buffer) {
		planetname = buffer.readComponent().getString();
	}

	@Override
	protected void applySettings(ServerPlayer player, TelescopeBlockEntity be) {
		Level level = be.getLevel();
		BlockPos blockPos = be.getBlockPos();
		BlockState blockState = level.getBlockState(blockPos);

		if (!(blockState.getBlock() instanceof TelescopeBlock))
			return;

		be.print(planetname, player);
	}

	@Override
	protected void applySettings(TelescopeBlockEntity be) {		
	}

}