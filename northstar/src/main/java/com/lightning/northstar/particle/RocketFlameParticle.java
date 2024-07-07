package com.lightning.northstar.particle;

import com.simibubi.create.content.equipment.bell.BasicParticleData;
import com.simibubi.create.foundation.utility.VecHelper;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class RocketFlameParticle  extends SimpleAnimatedParticle {

	protected RocketFlameParticle(ClientLevel world, double x, double y, double z, double pXSpeed, double pYSpeed, double pZSpeed, SpriteSet sprite) {
		super(world, x, y, z, sprite, world.random.nextFloat() * 1.2f);
		this.quadSize *= 0.75F;
		this.lifetime = 20;
		this.scale(15F);
		this.setSize(6F, 6F);
		double x_off = random.nextInt(2) * (random .nextBoolean() ? -1 : 1) * 0.01;
		double z_off = random.nextInt(2) * (random .nextBoolean() ? -1 : 1) * 0.01;
		this.xd += x_off;
		this.yd += 0.02;
		this.zd += z_off;
		hasPhysics = true;
		selectSprite(7);
		Vec3 offset = VecHelper.offsetRandomly(Vec3.ZERO, world.random, .15f);
		this.setPos(x + offset.x, y + offset.y - 0.3, z + offset.z);
		this.xo = x;
		this.yo = y;
		this.zo = z;
		setAlpha(0.6f);
	}
	
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}
	
	private void selectSprite(int index) {
		setSprite(sprites.get(index, 8));
	}
	public float getQuadSize(float pScaleFactor) {
		float f = ((float)this.age + pScaleFactor) / (float)this.lifetime;
		return this.quadSize * (1.0F - f * f * 0.5F);
	}

	public int getLightColor(float pPartialTick) {
		float f = ((float)this.age + pPartialTick) / (float)this.lifetime;
		f = Mth.clamp(f, 0.0F, 1.0F);
		int i = super.getLightColor(pPartialTick);
		int j = i & 255;
		int k = i >> 16 & 255;
		j += (int)(f * 15.0F * 16.0F);
		if (j > 240) {
			j = 240;
		}

		return j | k << 16;
	}
		
	public static class Factory implements ParticleProvider<RocketFlameParticleData> {
		private final SpriteSet spriteSet;

		public Factory(SpriteSet animatedSprite) {
			this.spriteSet = animatedSprite;
		}

		public Particle createParticle(RocketFlameParticleData data, ClientLevel worldIn, double x, double y, double z,
				double xSpeed, double ySpeed, double zSpeed) {
			return new RocketFlameParticle(worldIn, x, y, z, zSpeed, zSpeed, zSpeed, this.spriteSet);
		}
	}
		
	public static class Data extends BasicParticleData<RocketFlameParticle> {
		@Override
		public IBasicParticleFactory<RocketFlameParticle> getBasicFactory() {
			return (worldIn, x, y, z, vx, vy, vz, spriteSet) -> new RocketFlameParticle(worldIn, x, y, z, vx, vy, vz,
					spriteSet);
		}

		@Override
		public ParticleType<?> getType() {
			return NorthstarParticles.ROCKET_FLAME.get();
		}
	}
		
}
