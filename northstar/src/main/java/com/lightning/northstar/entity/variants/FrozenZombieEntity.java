package com.lightning.northstar.entity.variants;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FrozenZombieEntity extends Zombie {
	public FrozenZombieEntity(EntityType<? extends FrozenZombieEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	protected boolean isSunSensitive() {
		return false;
	}

	protected SoundEvent getAmbientSound() {
		return SoundEvents.HUSK_AMBIENT;
	}
	
	protected SoundEvent getHurtSound(DamageSource pDamageSource) {
		return SoundEvents.HUSK_HURT;
	}
	
	protected SoundEvent getDeathSound() {
		return SoundEvents.HUSK_DEATH;
	}

	protected SoundEvent getStepSound() {
		return SoundEvents.HUSK_STEP;
	}

	public boolean doHurtTarget(Entity pEntity) {
		boolean flag = super.doHurtTarget(pEntity);
		if (flag && pEntity instanceof LivingEntity) {
			float f = this.level().getCurrentDifficultyAt(this.blockPosition()).getEffectiveDifficulty();
			((LivingEntity)pEntity).setTicksFrozen((int) (140 + (50 * f)));
		}
		return flag;
	}

	protected boolean convertsInWater() {
		return false;
	}
	
	protected ItemStack getSkull() {
		return ItemStack.EMPTY;
	}
}