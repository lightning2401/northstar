package com.lightning.northstar.particle;

import com.simibubi.create.content.equipment.bell.BasicParticleData;
import com.simibubi.create.foundation.utility.VecHelper;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class SnowflakeParticle  extends SimpleAnimatedParticle {

	protected SnowflakeParticle(ClientLevel world, double x, double y, double z, double pXSpeed, double pYSpeed, double pZSpeed, SpriteSet sprite) {
		super(world, x, y, z, sprite, world.random.nextFloat() * .1f);
		this.quadSize *= 0.75F;
		this.lifetime = 90;
		this.scale(1F);
		this.setSize(0.25F, 0.25F);
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

	@Override
	public int getLightColor(float partialTick) {
		BlockPos blockpos = new BlockPos((int)this.x, (int)this.y, (int)this.z);
		return this.level.isLoaded(blockpos) ? LevelRenderer.getLightColor(level, blockpos) : 0;
	}
		
	public static class Factory implements ParticleProvider<SnowflakeParticleData> {
		private final SpriteSet spriteSet;

		public Factory(SpriteSet animatedSprite) {
			this.spriteSet = animatedSprite;
		}

		public Particle createParticle(SnowflakeParticleData data, ClientLevel worldIn, double x, double y, double z,
				double xSpeed, double ySpeed, double zSpeed) {
			return new SnowflakeParticle(worldIn, x, y, z, zSpeed, zSpeed, zSpeed, this.spriteSet);
		}
	}
		
	public static class Data extends BasicParticleData<SnowflakeParticle> {
		@Override
		public IBasicParticleFactory<SnowflakeParticle> getBasicFactory() {
			return (worldIn, x, y, z, vx, vy, vz, spriteSet) -> new SnowflakeParticle(worldIn, x, y, z, vx, vy, vz,
					spriteSet);
		}

		@Override
		public ParticleType<?> getType() {
			return NorthstarParticles.SNOWFLAKE.get();
		}
	}
		
}
