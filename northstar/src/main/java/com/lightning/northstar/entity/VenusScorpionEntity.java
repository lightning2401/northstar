package com.lightning.northstar.entity;

import java.util.EnumSet;
import java.util.UUID;

import com.lightning.northstar.NorthstarTags.NorthstarBlockTags;
import com.lightning.northstar.entity.projectiles.VenusScorpionSpit;
import com.lightning.northstar.sound.NorthstarSounds;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.ClientUtils;
import software.bernie.geckolib.util.GeckoLibUtil;

public class VenusScorpionEntity extends Monster implements GeoEntity, RangedAttackMob {
	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
	private static final UUID SPEED_MODIFIER_ATTACKING_UUID = UUID.fromString("49455A49-7EC5-45BA-B886-3B90B23A1718");
	private static final AttributeModifier SPEED_MODIFIER_ATTACKING = new AttributeModifier(SPEED_MODIFIER_ATTACKING_UUID, "Attacking speed boost", 0.05D, AttributeModifier.Operation.ADDITION);
	private int spitAnim = 0;
	
	public VenusScorpionEntity(EntityType<? extends VenusScorpionEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}
	
	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 16.0D).add(Attributes.MAX_HEALTH, 30.0D).add(Attributes.ATTACK_DAMAGE, 5).add(Attributes.MOVEMENT_SPEED, 0.2f);
	}
	
	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		RawAnimation idle = RawAnimation.begin().thenLoop("idle");
		RawAnimation walk = RawAnimation.begin().thenLoop("walk");
		RawAnimation spit = RawAnimation.begin().thenLoop("spit");

		
		controllers.add(
				// Add our flying animation controller
				new AnimationController<>(this, state -> {
					if(spitAnim > 0) {return state.setAndContinue(spit);}
					else if (!(state.getLimbSwingAmount() > -0.15F && state.getLimbSwingAmount() < 0.15F)) {return state.setAndContinue(walk);}
					else return state.setAndContinue(idle);})
						// Handle the custom instruction keyframe that is part of our animation json
						.setCustomInstructionKeyframeHandler(state -> {
							Player player = ClientUtils.getClientPlayer();

							if (player != null)
								player.displayClientMessage(Component.literal("KeyFraming"), true);
						})
		);
	}  
	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return cache;
	}
	
	public static boolean scorpionSpawnRules(EntityType<VenusScorpionEntity> moth, LevelAccessor level, MobSpawnType spawntype, BlockPos pos, RandomSource rando) {
		BlockState state = level.getBlockState(pos.below());
		return state.is(NorthstarBlockTags.NATURAL_VENUS_BLOCKS.tag);
	}
	
	//this handles client side stuff, and creates parity between server and client
	@Override
	public void handleEntityEvent(byte pId) {
		if (pId == 4) {
			spitAnim = 10;
		}
			super.handleEntityEvent(pId);
	}
	
	@Override
	public void tick() {		
		if(spitAnim > 0) {
			spitAnim--;
		}
		
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
		return NorthstarSounds.VENUS_SCORPION_IDLE.get();
	}
	@Override
	protected SoundEvent getHurtSound(DamageSource pDamageSource) {
		return NorthstarSounds.VENUS_SCORPION_HURT.get();
	}
	@Override
	protected SoundEvent getDeathSound() {
		return NorthstarSounds.VENUS_SCORPION_DEATH.get();
	}
	
	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Player.class, 3.0F, 1.0D, 1.0D));
		this.goalSelector.addGoal(4, new VenusScorpionEntity.ShootAcidGoal(this));
		this.goalSelector.addGoal(9, new VenusScorpionEntity.StareAtTargetGoal(this));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, ZombifiedPiglin.class, true));
		super.registerGoals();
	}
	@Override
	public boolean doHurtTarget(Entity pEntity) {
		this.level().broadcastEntityEvent(this, (byte)4);
		this.playSound(SoundEvents.RAVAGER_ATTACK, 1.0F, 1.0F);
		return super.doHurtTarget(pEntity);
	}

	@Override
	public void performRangedAttack(LivingEntity pTarget, float pVelocity) {
		
	}
	static class StareAtTargetGoal extends Goal {
	      private final VenusScorpionEntity shooter;

	      public StareAtTargetGoal(VenusScorpionEntity pShooter) {
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
		private final VenusScorpionEntity shooter;
		public int chargeTime;

		public ShootAcidGoal(VenusScorpionEntity shooter) {
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
			if (livingentity != null && !shooter.getNavigation().isInProgress()) {
				if (livingentity.distanceToSqr(this.shooter) < 4096.0D && this.shooter.hasLineOfSight(livingentity)) {
					Level level = this.shooter.level();
					++this.chargeTime;
					if (this.chargeTime == 20) {
						Vec3 vec3 = this.shooter.getViewVector(1.0F);
						VenusScorpionSpit acidspit = new VenusScorpionSpit(level, this.shooter);
						double newX = shooter.getTarget().getX() - shooter.getX();
						double newY = shooter.getTarget().getY(0.3333333333333333D) - acidspit.getY();
						double newZ = shooter.getTarget().getZ() - shooter.getZ();
						double newThing = Math.sqrt(newX * newX + newZ * newZ) * (double)0.2F;
						shooter.spitAnim = 10;
						level.broadcastEntityEvent(shooter, (byte) 4);
						acidspit.shoot(newX, newY + newThing, newZ, 1.5F, 10.0F);
						acidspit.setPos(this.shooter.getX() + vec3.x, this.shooter.getY(0.5D), acidspit.getZ() + vec3.z);
						level.addFreshEntity(acidspit);
						this.chargeTime = -40;
					}
				} else if (this.chargeTime > 0) {
					--this.chargeTime;
				}
			}
		}
	}

}
