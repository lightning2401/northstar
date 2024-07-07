package com.lightning.northstar.block.tech.temperature_regulator;

import com.simibubi.create.foundation.networking.BlockEntityConfigurationPacket;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public class TemperatureRegulatorEditPacket extends BlockEntityConfigurationPacket<TemperatureRegulatorBlockEntity> {

	int offsetX;
	int offsetY;
	int offsetZ;
	int sizeChangeX;
	int sizeChangeY;
	int sizeChangeZ;
	int temp;
	boolean envFill;


	public static TemperatureRegulatorEditPacket changeStuff(BlockPos pos, int X, int Y, int Z, int sizeX, int sizeY, int sizeZ, int tempChange, boolean envFill) {
		TemperatureRegulatorEditPacket packet = new TemperatureRegulatorEditPacket(pos, X, Y, Z, sizeX, sizeY, sizeZ, tempChange, envFill);
		packet.offsetX = X;
		packet.offsetY = Y;
		packet.offsetZ = Z;
		packet.sizeChangeX = sizeX;
		packet.sizeChangeY = sizeY;
		packet.sizeChangeZ = sizeZ;
		packet.temp = tempChange;
		packet.envFill = envFill;
		return packet;
	}

	public TemperatureRegulatorEditPacket(FriendlyByteBuf buffer) {
		super(buffer);
	}
	
	public TemperatureRegulatorEditPacket(BlockPos pos, int offX, int offY, int offZ, int sizeX, int sizeY, int sizeZ, int tempChange, boolean envFill) {
		super(pos);
		this.offsetX = offX;
		this.offsetY = offY;
		this.offsetZ = offZ;
		this.sizeChangeX = sizeX;
		this.sizeChangeY = sizeY;
		this.sizeChangeZ = sizeZ;
		this.temp = tempChange;
		this.envFill = envFill;
	}

	@Override
	protected void writeSettings(FriendlyByteBuf buffer) {
		buffer.writeVarInt(offsetX);
		buffer.writeVarInt(offsetY);
		buffer.writeVarInt(offsetZ);
		buffer.writeVarInt(sizeChangeX);
		buffer.writeVarInt(sizeChangeY);
		buffer.writeVarInt(sizeChangeZ);
		buffer.writeVarInt(temp);
		buffer.writeBoolean(envFill);
	}

	@Override
	protected void readSettings(FriendlyByteBuf buffer) {
		offsetX = buffer.readVarInt();
		offsetY = buffer.readVarInt();
		offsetZ = buffer.readVarInt();
		sizeChangeX = buffer.readVarInt();
		sizeChangeY = buffer.readVarInt();
		sizeChangeZ = buffer.readVarInt();
		temp = buffer.readVarInt();
		envFill = buffer.readBoolean();
	}
	@Override
	protected void applySettings(TemperatureRegulatorBlockEntity be) {
		be.changeTemp(temp);System.out.println("WE CHANGING!!!!!!");
		be.changeSize(sizeChangeX, sizeChangeY, sizeChangeZ, offsetX, offsetY, offsetZ, envFill);
	}

}