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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class SulfurPoofParticle  extends SimpleAnimatedParticle {

	protected SulfurPoofParticle(ClientLevel world, double x, double y, double z, double pXSpeed, double pYSpeed, double pZSpeed, SpriteSet sprite) {
		super(world, x, y, z, sprite, -3f + world.random.nextFloat());
		this.quadSize *= 0.75F;
		this.lifetime = 40;
		this.scale(15);
		this.setSize(6F, 6F);
		this.setBoundingBox(new AABB(0.1, 0.1, 0.1, -0.1, -0.1, -0.1));
		double x_off = random.nextInt(2) * (random .nextBoolean() ? -1 : 1) * 0.1;
		double z_off = random.nextInt(2) * (random .nextBoolean() ? -1 : 1) * 0.1;
		this.xd += x_off;
		this.yd += 0.02;
		this.zd += z_off;
		hasPhysics = true;
		selectSprite(7);
		Vec3 offset = VecHelper.offsetRandomly(Vec3.ZERO, world.random, .00f);
		this.setPos(x + offset.x, y + offset.y, z + offset.z);
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
		BlockPos blockpos = new BlockPos(this.x, this.y, this.z);
		return this.level.isLoaded(blockpos) ? LevelRenderer.getLightColor(level, blockpos) : 0;
	}
		
	public static class Factory implements ParticleProvider<SulfurPoofParticleData> {
		private final SpriteSet spriteSet;

		public Factory(SpriteSet animatedSprite) {
			this.spriteSet = animatedSprite;
		}

		public Particle createParticle(SulfurPoofParticleData data, ClientLevel worldIn, double x, double y, double z,
				double xSpeed, double ySpeed, double zSpeed) {
			return new SulfurPoofParticle(worldIn, x, y, z, zSpeed, zSpeed, zSpeed, this.spriteSet);
		}
	}
		
	public static class Data extends BasicParticleData<SulfurPoofParticle> {
		@Override
		public IBasicParticleFactory<SulfurPoofParticle> getBasicFactory() {
			return (worldIn, x, y, z, vx, vy, vz, spriteSet) -> new SulfurPoofParticle(worldIn, x, y, z, vx, vy, vz,
					spriteSet);
		}

		@Override
		public ParticleType<?> getType() {
			return NorthstarParticles.SULFUR_POOF.get();
		}
	}
		
}
