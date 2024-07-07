package com.lightning.northstar.entity;

import java.util.EnumSet;
import java.util.UUID;

import com.lightning.northstar.NorthstarTags.NorthstarBlockTags;
import com.lightning.northstar.entity.goals.EatRootsGoal;
import com.lightning.northstar.entity.goals.RunToGroupGoal;
import com.lightning.northstar.sound.NorthstarSounds;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class MarsToadEntity extends Monster implements IAnimatable, IAnimationTickable, RangedAttackMob {
	AnimationFactory factory = GeckoLibUtil.createFactory(this);
	private static final UUID SPEED_MODIFIER_ATTACKING_UUID = UUID.fromString("49455A49-7EC5-45BA-B886-3B90B23A1718");
	private static final AttributeModifier SPEED_MODIFIER_ATTACKING = new AttributeModifier(SPEED_MODIFIER_ATTACKING_UUID, "Attacking speed boost", 0.2D, AttributeModifier.Operation.ADDITION);
	public int eating = 0;
	
	public MarsToadEntity(EntityType<? extends MarsToadEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}
	
	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 16.0D).add(Attributes.MAX_HEALTH, 20.0D).add(Attributes.ATTACK_DAMAGE, 5).add(Attributes.MOVEMENT_SPEED, 0.2f);
	}
	
	private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
		if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F) ) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", EDefaultLoopTypes.LOOP));
			event.getController().animationSpeed = event.getLimbSwingAmount();
		} else if(eating > 0) {
			eating--;
			event.getController().setAnimation(new AnimationBuilder().addAnimation("eat", EDefaultLoopTypes.PLAY_ONCE));
			event.getController().animationSpeed = 1;
		}else {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", EDefaultLoopTypes.LOOP));
			event.getController().animationSpeed = 1;
		}

		return PlayState.CONTINUE;
	}
	
	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController<MarsToadEntity>(this, "controller", 2, this::predicate));
		
	}
	
	//this handles client side stuff, and creates parity between server and client
	@Override
	public void handleEntityEvent(byte pId) {
		if (pId == 10) {
			eating = 40;
		}
			super.handleEntityEvent(pId);
	}
	
	public static boolean toadSpawnRules(EntityType<MarsToadEntity> toad, LevelAccessor level, MobSpawnType spawntype, BlockPos pos, RandomSource rando) {
		int surfaceY = level.getHeight(Heightmap.Types.MOTION_BLOCKING,(int) pos.getX(),(int) pos.getZ());
		BlockState state = level.getBlockState(pos.below());
		if (pos.getY() >= surfaceY) {
			return false;
		} else if (pos.getY() > (surfaceY / 2)){
			int light = level.getMaxLocalRawBrightness(pos);
			return light > 7 ? false :  checkMobSpawnRules(toad, level, spawntype, pos, rando) && (state.is(NorthstarBlockTags.NATURAL_MARS_BLOCKS.tag) || state.isAir());
		}
		else
			return false;
	}
	
	@Override
	public void tick() {		
		
	      super.tick();
	}
	protected void customServerAiStep() {
	      AttributeInstance attributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
	      if (this.getTarget() != null) {
	         if (!attributeinstance.hasModifier(SPEED_MODIFIER_ATTACKING)) {
	            attributeinstance.addTransientModifier(SPEED_MODIFIER_ATTACKING);
	         }

	      } else if (attributeinstance.hasModifier(SPEED_MODIFIER_ATTACKING)) {
	         attributeinstance.removeModifier(SPEED_MODIFIER_ATTACKING);
	      }

	      super.customServerAiStep();
	}
	@Override
	protected SoundEvent getAmbientSound() {
		super.getAmbientSound();
		return NorthstarSounds.MARS_TOAD_IDLE.get();
	}
	@Override
	protected SoundEvent getHurtSound(DamageSource pDamageSource) {
		return NorthstarSounds.MARS_TOAD_HURT.get();
	}
	@Override
	protected SoundEvent getDeathSound() {
		return NorthstarSounds.MARS_TOAD_DEATH.get();
	}

	
	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(1, new EatRootsGoal(this));
	    this.goalSelector.addGoal(9, new AvoidEntityGoal<>(this, MarsWormEntity.class, 6.0F, 1.0D, 1.35D));
	    this.goalSelector.addGoal(9, new RunToGroupGoal<>(this, Player.class, 7, 1, 1.2));
		super.registerGoals();
	}
	@Override
	public boolean doHurtTarget(Entity pEntity) {
		this.level.broadcastEntityEvent(this, (byte)4);
		this.playSound(SoundEvents.RAVAGER_ATTACK, 1.0F, 1.0F);
		return super.doHurtTarget(pEntity);
	}


	@Override
	public AnimationFactory getFactory() {
		return factory;
	}

	@Override
	public int tickTimer() {
		return tickCount;
	}

	@Override
	public void performRangedAttack(LivingEntity pTarget, float pVelocity) {
		
	}
	static class StareAtTargetGoal extends Goal {
	      private final MarsToadEntity shooter;

	      public StareAtTargetGoal(MarsToadEntity pShooter) {
	         this.shooter = pShooter;
	         this.setFlags(EnumSet.of(Goal.Flag.LOOK));
	      }
	      public boolean canUse() {
	         return true;
	      }

	      public boolean requiresUpdateEveryTick() {
	         return true;
	      }
	      public void tick() {
	         if (this.shooter.getTarget() == null) {
	            Vec3 vec3 = this.shooter.getDeltaMovement();
	            this.shooter.setYRot(-((float)Mth.atan2(vec3.x, vec3.z)) * (180F / (float)Math.PI));
	            this.shooter.yBodyRot = this.shooter.getYRot();
	         } else {
	            LivingEntity livingentity = this.shooter.getTarget();
	            if (livingentity.distanceToSqr(this.shooter) < 4096.0D) {
	               double d1 = livingentity.getX() - this.shooter.getX();
	               double d2 = livingentity.getZ() - this.shooter.getZ();
	               this.shooter.setYRot(-((float)Mth.atan2(d1, d2)) * (180F / (float)Math.PI));
	               this.shooter.yBodyRot = this.shooter.getYRot();
	            }
	         }

	      }
	   }
	
	static class ShootAcidGoal extends Goal {
		private final MarsToadEntity shooter;
		public int chargeTime;

		public ShootAcidGoal(MarsToadEntity shooter) {
			this.shooter = shooter;
		}

		/**
		 * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
		 * method as well.
		 */
		public boolean canUse() {
			return this.shooter.getTarget() != null;
		}
		
		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		public void start() {
			this.chargeTime = 0;
		}

		/**
		 * Reset the task's internal state. Called when this task is interrupted by another one
		 */
		public void stop() {
		}

		public boolean requiresUpdateEveryTick() {
			return true;
		}
		
		public void tick() {
			LivingEntity livingentity = this.shooter.getTarget();
			if (livingentity != null) {
				if (livingentity.distanceToSqr(this.shooter) < 4096.0D && this.shooter.hasLineOfSight(livingentity)) {
					Level level = this.shooter.level;
					++this.chargeTime;
					if (this.chargeTime == 20) {
						Vec3 vec3 = this.shooter.getViewVector(1.0F);
						double d2 = livingentity.getX() - (this.shooter.getX() + vec3.x * 4.0D);
						double d3 = livingentity.getY(0.5D) - (0.5D + this.shooter.getY(0.5D));
						double d4 = livingentity.getZ() - (this.shooter.getZ() + vec3.z * 4.0D);						
						LargeFireball largefireball = new LargeFireball(level, this.shooter, d2, d3, d4, 2);
						largefireball.setPos(this.shooter.getX() + vec3.x, this.shooter.getY(0.5D), largefireball.getZ() + vec3.z);
						level.addFreshEntity(largefireball);
						this.chargeTime = -40;
					}
				} else if (this.chargeTime > 0) {
					--this.chargeTime;
				}
			}
		}
	}

}
