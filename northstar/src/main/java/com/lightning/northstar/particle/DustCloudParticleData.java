package com.lightning.northstar.particle;

import java.util.Locale;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.foundation.particle.ICustomParticleDataWithSprite;

import net.minecraft.client.particle.ParticleEngine.SpriteParticleRegistration;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class DustCloudParticleData implements ParticleOptions, ICustomParticleDataWithSprite<DustCloudParticleData>  {
	
	public static final Codec<DustCloudParticleData> CODEC = RecordCodecBuilder.create(i -> 
	i.group(
		Codec.INT.fieldOf("x").forGetter(p -> p.posX),
		Codec.INT.fieldOf("y").forGetter(p -> p.posY),
		Codec.INT.fieldOf("z").forGetter(p -> p.posZ))
	.apply(i, DustCloudParticleData::new));
	
	@SuppressWarnings("deprecation")
	public static final ParticleOptions.Deserializer<DustCloudParticleData> DESERIALIZER = new ParticleOptions.Deserializer<DustCloudParticleData>() {
		public DustCloudParticleData fromCommand(ParticleType<DustCloudParticleData> particleTypeIn, StringReader reader)
				throws CommandSyntaxException {
			reader.expect(' ');
			int x = reader.readInt();
			reader.expect(' ');
			int y = reader.readInt();
			reader.expect(' ');
			int z = reader.readInt();
			return new DustCloudParticleData(x, y, z);
		}

		public DustCloudParticleData fromNetwork(ParticleType<DustCloudParticleData> particleTypeIn, FriendlyByteBuf buffer) {
			return new DustCloudParticleData(buffer.readInt(), buffer.readInt(), buffer.readInt());
		}
	};
	
	
	final int posX;
	final int posY;
	final int posZ;
	
	
	public DustCloudParticleData(Vec3i pos) {
		this(pos.getX(), pos.getY(), pos.getZ());
	}

	public DustCloudParticleData(int posX, int posY, int posZ) {
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
	}

	public DustCloudParticleData() {
		this(0, 0, 0);
	}

	@Override
	public ParticleType<?> getType() {
		return NorthstarParticles.DUST_CLOUD.get();
	}
	
	@Override
	public void writeToNetwork(FriendlyByteBuf buffer) {
		buffer.writeInt(posX);
		buffer.writeInt(posY);
		buffer.writeInt(posZ);
	}
	
	@Override
	public String writeToString() {
		return String.format(Locale.ROOT, "%s %d %d %d", NorthstarParticles.DUST_CLOUD.parameter(), posX, posY, posZ);
	}

	@SuppressWarnings("deprecation")
	@Override
	public Deserializer<DustCloudParticleData> getDeserializer() {
		return DESERIALIZER;
	}
	
	@Override
	public Codec<DustCloudParticleData> getCodec(ParticleType<DustCloudParticleData> type) {
		return CODEC;
	}
	@Override
	@OnlyIn(Dist.CLIENT)
	public SpriteParticleRegistration<DustCloudParticleData> getMetaFactory() {
		return DustCloudParticle.Factory::new;
	}
}
