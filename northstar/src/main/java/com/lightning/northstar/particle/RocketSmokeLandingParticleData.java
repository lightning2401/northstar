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

public class RocketSmokeLandingParticleData implements ParticleOptions, ICustomParticleDataWithSprite<RocketSmokeLandingParticleData>  {
	
	public static final Codec<RocketSmokeLandingParticleData> CODEC = RecordCodecBuilder.create(i -> 
	i.group(
		Codec.INT.fieldOf("x").forGetter(p -> p.posX),
		Codec.INT.fieldOf("y").forGetter(p -> p.posY),
		Codec.INT.fieldOf("z").forGetter(p -> p.posZ))
	.apply(i, RocketSmokeLandingParticleData::new));
	
	@SuppressWarnings("deprecation")
	public static final ParticleOptions.Deserializer<RocketSmokeLandingParticleData> DESERIALIZER = new ParticleOptions.Deserializer<RocketSmokeLandingParticleData>() {
		public RocketSmokeLandingParticleData fromCommand(ParticleType<RocketSmokeLandingParticleData> particleTypeIn, StringReader reader)
				throws CommandSyntaxException {
			reader.expect(' ');
			int x = reader.readInt();
			reader.expect(' ');
			int y = reader.readInt();
			reader.expect(' ');
			int z = reader.readInt();
			return new RocketSmokeLandingParticleData(x, y, z);
		}

		public RocketSmokeLandingParticleData fromNetwork(ParticleType<RocketSmokeLandingParticleData> particleTypeIn, FriendlyByteBuf buffer) {
			return new RocketSmokeLandingParticleData(buffer.readInt(), buffer.readInt(), buffer.readInt());
		}
	};
	
	
	final int posX;
	final int posY;
	final int posZ;
	
	
	public RocketSmokeLandingParticleData(Vec3i pos) {
		this(pos.getX(), pos.getY(), pos.getZ());
	}

	public RocketSmokeLandingParticleData(int posX, int posY, int posZ) {
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
	}

	public RocketSmokeLandingParticleData() {
		this(0, 0, 0);
	}

	@Override
	public ParticleType<?> getType() {
		return NorthstarParticles.ROCKET_SMOKE_LANDING.get();
	}
	
	@Override
	public void writeToNetwork(FriendlyByteBuf buffer) {
		buffer.writeInt(posX);
		buffer.writeInt(posY);
		buffer.writeInt(posZ);
	}
	
	@Override
	public String writeToString() {
		return String.format(Locale.ROOT, "%s %d %d %d", NorthstarParticles.ROCKET_SMOKE_LANDING.parameter(), posX, posY, posZ);
	}

	@SuppressWarnings("deprecation")
	@Override
	public Deserializer<RocketSmokeLandingParticleData> getDeserializer() {
		return DESERIALIZER;
	}
	
	@Override
	public Codec<RocketSmokeLandingParticleData> getCodec(ParticleType<RocketSmokeLandingParticleData> type) {
		return CODEC;
	}
	@Override
	@OnlyIn(Dist.CLIENT)
	public SpriteParticleRegistration<RocketSmokeLandingParticleData> getMetaFactory() {
		return RocketSmokeLandingParticle.Factory::new;
	}
}
