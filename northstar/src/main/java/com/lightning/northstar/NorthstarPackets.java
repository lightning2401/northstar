package com.lightning.northstar;

import static net.minecraftforge.network.NetworkDirection.PLAY_TO_SERVER;
import static net.minecraftforge.network.NetworkDirection.PLAY_TO_CLIENT;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.lightning.northstar.block.tech.rocket_controls.RocketControlsInputPacket;
import com.lightning.northstar.block.tech.rocket_station.RocketStationEditPacket;
import com.lightning.northstar.block.tech.telescope.TelescopePrintPacket;
import com.lightning.northstar.block.tech.temperature_regulator.TemperatureRegulatorEditPacket;
import com.lightning.northstar.contraptions.RocketContraptionSyncPacket;
import com.simibubi.create.foundation.networking.SimplePacketBase;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent.Context;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.PacketDistributor.TargetPoint;
import net.minecraftforge.network.simple.SimpleChannel;

public enum NorthstarPackets {
	//client to server
	UPDATE_ROCKET_STATION(RocketStationEditPacket.class, RocketStationEditPacket::new, PLAY_TO_SERVER),
	UPDATE_TEMPERATURE_REGULATOR(TemperatureRegulatorEditPacket.class, TemperatureRegulatorEditPacket::new, PLAY_TO_SERVER),
	TELESCOPE_PRINT(TelescopePrintPacket.class, TelescopePrintPacket::new, PLAY_TO_SERVER),
	ROCKET_CONTROLS_INPUT(RocketControlsInputPacket.class, RocketControlsInputPacket::new, PLAY_TO_SERVER),
	//server to client
	ROCKET_SYNC_PACKET(RocketContraptionSyncPacket.class, RocketContraptionSyncPacket::new, PLAY_TO_CLIENT);

	public static final ResourceLocation CHANNEL_NAME = Northstar.asResource("main");
	public static final int NETWORK_VERSION = 3;
	public static final String NETWORK_VERSION_STR = String.valueOf(NETWORK_VERSION);
	private static SimpleChannel channel;

	private PacketType<?> packetType;

	<T extends SimplePacketBase> NorthstarPackets(Class<T> type, Function<FriendlyByteBuf, T> factory,
		NetworkDirection direction) {
		packetType = new PacketType<>(type, factory, direction);
	}

	public static void registerPackets() {
		channel = NetworkRegistry.ChannelBuilder.named(CHANNEL_NAME)
			.serverAcceptedVersions(NETWORK_VERSION_STR::equals)
			.clientAcceptedVersions(NETWORK_VERSION_STR::equals)
			.networkProtocolVersion(() -> NETWORK_VERSION_STR)
			.simpleChannel();

		for (NorthstarPackets packet : values())
			packet.packetType.register();
	}

	public static SimpleChannel getChannel() {
		return channel;
	}

	public static void sendToNear(Level world, BlockPos pos, int range, Object message) {
		getChannel().send(
			PacketDistributor.NEAR.with(TargetPoint.p(pos.getX(), pos.getY(), pos.getZ(), range, world.dimension())),
			message);
	}

	private static class PacketType<T extends SimplePacketBase> {
		private static int index = 0;

		private BiConsumer<T, FriendlyByteBuf> encoder;
		private Function<FriendlyByteBuf, T> decoder;
		private BiConsumer<T, Supplier<Context>> handler;
		private Class<T> type;
		private NetworkDirection direction;

		private PacketType(Class<T> type, Function<FriendlyByteBuf, T> factory, NetworkDirection direction) {
			encoder = T::write;
			decoder = factory;
			handler = (packet, contextSupplier) -> {
				Context context = contextSupplier.get();
				if (packet.handle(context)) {
					context.setPacketHandled(true);
				}
			};
			this.type = type;
			this.direction = direction;
		}

		private void register() {
			getChannel().messageBuilder(type, index++, direction)
				.encoder(encoder)
				.decoder(decoder)
				.consumerNetworkThread(handler)
				.add();
		}
	}
}