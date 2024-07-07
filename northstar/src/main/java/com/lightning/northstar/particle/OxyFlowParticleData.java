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

public class OxyFlowParticleData implements ParticleOptions, ICustomParticleDataWithSprite<OxyFlowParticleData>  {
	
	public static final Codec<OxyFlowParticleData> CODEC = RecordCodecBuilder.create(i -> 
	i.group(
		Codec.INT.fieldOf("x").forGetter(p -> p.posX),
		Codec.INT.fieldOf("y").forGetter(p -> p.posY),
		Codec.INT.fieldOf("z").forGetter(p -> p.posZ))
	.apply(i, OxyFlowParticleData::new));
	
	@SuppressWarnings("deprecation")
	public static final ParticleOptions.Deserializer<OxyFlowParticleData> DESERIALIZER = new ParticleOptions.Deserializer<OxyFlowParticleData>() {
		public OxyFlowParticleData fromCommand(ParticleType<OxyFlowParticleData> particleTypeIn, StringReader reader)
				throws CommandSyntaxException {
			reader.expect(' ');
			int x = reader.readInt();
			reader.expect(' ');
			int y = reader.readInt();
			reader.expect(' ');
			int z = reader.readInt();
			return new OxyFlowParticleData(x, y, z);
		}

		public OxyFlowParticleData fromNetwork(ParticleType<OxyFlowParticleData> particleTypeIn, FriendlyByteBuf buffer) {
			return new OxyFlowParticleData(buffer.readInt(), buffer.readInt(), buffer.readInt());
		}
	};
	
	
	final int posX;
	final int posY;
	final int posZ;
	
	
	public OxyFlowParticleData(Vec3i pos) {
		this(pos.getX(), pos.getY(), pos.getZ());
	}

	public OxyFlowParticleData(int posX, int posY, int posZ) {
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
	}

	public OxyFlowParticleData() {
		this(0, 0, 0);
	}

	@Override
	public ParticleType<?> getType() {
		return NorthstarParticles.OXY_FLOW.get();
	}
	
	@Override
	public void writeToNetwork(FriendlyByteBuf buffer) {
		buffer.writeInt(posX);
		buffer.writeInt(posY);
		buffer.writeInt(posZ);
	}
	
	@Override
	public String writeToString() {
		return String.format(Locale.ROOT, "%s %d %d %d", NorthstarParticles.OXY_FLOW.parameter(), posX, posY, posZ);
	}

	@SuppressWarnings("deprecation")
	@Override
	public Deserializer<OxyFlowParticleData> getDeserializer() {
		return DESERIALIZER;
	}
	
	@Override
	public Codec<OxyFlowParticleData> getCodec(ParticleType<OxyFlowParticleData> type) {
		return CODEC;
	}
	@Override
	@OnlyIn(Dist.CLIENT)
	public SpriteParticleRegistration<OxyFlowParticleData> getMetaFactory() {
		return OxyFlowParticle.Factory::new;
	}
}
