package com.lightning.northstar.entity.projectiles;

import com.lightning.northstar.entity.MoonLunargradeEntity;
import com.lightning.northstar.entity.NorthstarEntityTypes;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class LunargradeSpit extends Projectile {
	
	public LunargradeSpit(EntityType<? extends LunargradeSpit> guy, Level pLevel) {
		super(guy, pLevel);
	}

	public LunargradeSpit(Level pLevel, MoonLunargradeEntity lunargrade) {
		this(NorthstarEntityTypes.LUNARGRADE_SPIT.get(), pLevel);
		this.setOwner(lunargrade);
		this.setPos(lunargrade.getX() - (double)(lunargrade.getBbWidth() + 1.0F) * 0.5D * (double)Mth.sin(lunargrade.yBodyRot * ((float)Math.PI / 180F)), 
		lunargrade.getEyeY() - (double)0.1F, lunargrade.getZ() + (double)(lunargrade.getBbWidth() + 1.0F) * 0.5D * (double)Mth.cos(lunargrade.yBodyRot * ((float)Math.PI / 180F)));
	}

		   /**
		    * Called to update the entity's position/logic.
		    */
	public void tick() {
		super.tick();
		Vec3 vec3 = this.getDeltaMovement();
		HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
		if (hitresult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult))
			this.onHit(hitresult);
		double newX = this.getX() + vec3.x;
		double newY = this.getY() + vec3.y;
		double newZ = this.getZ() + vec3.z;
		this.updateRotation();
		if (this.level().getBlockStates(this.getBoundingBox()).noneMatch(BlockBehaviour.BlockStateBase::isAir)) {
			this.discard();
		} else if (this.isInWaterOrBubble()) {
			this.discard();
		} else {
			this.setDeltaMovement(vec3.scale((double)0.99F));
			if (!this.isNoGravity()) {
				this.setDeltaMovement(this.getDeltaMovement().add(0.0D, (double)-0.06F, 0.0D));
			}

			this.setPos(newX, newY, newZ);
		}
	}

		   /**
		    * Called when the arrow hits an entity
		    */
	protected void onHitEntity(EntityHitResult pResult) {
		super.onHitEntity(pResult);
		Entity entity = this.getOwner();
		if (entity instanceof LivingEntity) {
			pResult.getEntity().hurt(pResult.getEntity().damageSources().mobProjectile(pResult.getEntity(), (LivingEntity) entity), 1);
		}

	}

	protected void onHitBlock(BlockHitResult pResult) {
		super.onHitBlock(pResult);
		if (!this.level().isClientSide) {
			this.discard();
		}
		
	}

	protected void defineSynchedData() {
	}

	public void recreateFromPacket(ClientboundAddEntityPacket pPacket) {
		super.recreateFromPacket(pPacket);
		double d0 = pPacket.getXa();
		double d1 = pPacket.getYa();
		double d2 = pPacket.getZa();

		for(int i = 0; i < 7; ++i) {
			double d3 = 0.4D + 0.1D * (double)i;
			this.level().addParticle(ParticleTypes.SPIT, this.getX(), this.getY(), this.getZ(), d0 * d3, d1, d2 * d3);
		}

		this.setDeltaMovement(d0, d1, d2);
	}
}
