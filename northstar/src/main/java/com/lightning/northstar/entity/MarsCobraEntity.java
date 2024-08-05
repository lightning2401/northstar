package com.lightning.northstar.entity;

import java.util.EnumSet;
import java.util.UUID;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.lightning.northstar.NorthstarTags.NorthstarBlockTags;
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
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.ClientUtils;
import software.bernie.geckolib.util.GeckoLibUtil;

public class MarsCobraEntity extends Monster implements GeoEntity {
	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
	private static final UUID SPEED_MODIFIER_ATTACKING_UUID = UUID.fromString("49455A49-7EC5-45BA-B886-3B90B23A1718");
	private static final AttributeModifier SPEED_MODIFIER_ATTACKING = new AttributeModifier(SPEED_MODIFIER_ATTACKING_UUID, "Attacking speed boost", 0.1D, AttributeModifier.Operation.ADDITION);
	private int attackTick;
	private int lookedAt;
	
	public MarsCobraEntity(EntityType<? extends MarsCobraEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.setMaxUpStep(1f);
	}
	
	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 16.0D).add(Attributes.MAX_HEALTH, 20.0D).add(Attributes.ATTACK_DAMAGE, 5).add(Attributes.MOVEMENT_SPEED, 0.2f).add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 1);
	}
	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		RawAnimation idle = RawAnimation.begin().thenLoop("idle");
		RawAnimation walk = RawAnimation.begin().thenLoop("walk");
		RawAnimation bite = RawAnimation.begin().thenPlay("bite");
		
		controllers.add(
				// Add our flying animation controller
				new AnimationController<>(this, state -> {
					if(attackTick > 0) {return state.setAndContinue(bite);}
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
	protected SoundEvent getAmbientSound() {
		super.getAmbientSound();
		return NorthstarSounds.MARS_COBRA_IDLE.get();
	}
	@Override
	protected SoundEvent getHurtSound(DamageSource pDamageSource) {
		return NorthstarSounds.MARS_COBRA_HURT.get();
	}
	@Override
	protected SoundEvent getDeathSound() {
		return NorthstarSounds.MARS_COBRA_DEATH.get();
	}
	
	//this handles client side stuff, and creates parity between server and client
	@Override
	public void handleEntityEvent(byte pId) {
		if (pId == 4) {
			this.attackTick = 40;
		}
		super.handleEntityEvent(pId);
	}
	
	public static boolean cobraSpawnRules(EntityType<MarsCobraEntity> cobra, LevelAccessor level, MobSpawnType spawntype, BlockPos pos, RandomSource rando) {
		int surfaceY = level.getHeight(Heightmap.Types.MOTION_BLOCKING,(int) pos.getX(),(int) pos.getZ());
		BlockState state = level.getBlockState(pos.below());
		if (pos.getY() >= surfaceY) {
			return false;
		} else if (pos.getY() > (surfaceY / 1.5)) {
			int light = level.getMaxLocalRawBrightness(pos);
			return light != 0 ? false : checkMobSpawnRules(cobra, level, spawntype, pos, rando) && state.is(NorthstarBlockTags.NATURAL_MARS_BLOCKS.tag);
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
	protected void registerGoals() {
		this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.targetSelector.addGoal(1, new MarsCobraEntity.CobraLookForPlayerGoal(this, this::canTarget));
		this.targetSelector.addGoal(1, new MarsCobraEntity.CobraAttackWhenStaredAt(this));
		super.registerGoals();
	}
	@Override
	public boolean doHurtTarget(Entity pEntity) {
		this.level().broadcastEntityEvent(this, (byte)4);
		this.playSound(SoundEvents.RAVAGER_ATTACK, 1.0F, 1.0F);
		return super.doHurtTarget(pEntity);
	}

	public int tickTimer() {
		return tickCount;
	}
	
	boolean isLookingAtMe(Player pPlayer) {
		ItemStack itemstack = pPlayer.getInventory().armor.get(3);
		if (itemstack.is(Blocks.CARVED_PUMPKIN.asItem())) {
			return false;
		} else {
	         Vec3 vec3 = pPlayer.getViewVector(1.0F).normalize();
	         Vec3 vec31 = new Vec3(this.getX() - pPlayer.getX(), this.getEyeY() - pPlayer.getEyeY(), this.getZ() - pPlayer.getZ());
	         double d0 = vec31.length();
	         vec31 = vec31.normalize();
	         double d1 = vec3.dot(vec31);
//	         System.out.println(d1);
//	         System.out.println("comparer: " + String.valueOf(1.0D - 0.35D / (d0 / 8)));
	         return d1 > 1.0D - 0.35D / (d0 / 8) ? pPlayer.hasLineOfSight(this) : false;
	      }
	}
	
	static class CobraAttackWhenStaredAt extends Goal {
		private final MarsCobraEntity cobra;
		@Nullable
		private LivingEntity target;
		
		public CobraAttackWhenStaredAt(MarsCobraEntity coberuh) {
			this.cobra = coberuh;
			this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
		}

		public boolean canUse() {
			this.target = this.cobra.getTarget();
			if (!(this.target instanceof Player)) {
				return false;
			} else {
				double d0 = this.target.distanceToSqr(this.cobra);
				if(d0 > 256.0D ? false : this.cobra.isLookingAtMe((Player)this.target)) 
				{return true;}
					else	
				{return false;}
			}
		}

		public void start() {
			this.cobra.getNavigation().stop();
		}

		public void tick() {
			this.cobra.getLookControl().setLookAt(this.target.getX(), this.target.getEyeY(), this.target.getZ());
		}
	}
	boolean canTarget(LivingEntity target) {
		if (!this.canAttack(target)) {
			return false;
		} else {
			return target.getType() == EntityType.PLAYER;
		}
	}
	
	static class CobraLookForPlayerGoal extends NearestAttackableTargetGoal<Player> {
		private final MarsCobraEntity cobra;
		@Nullable
		private Player pendingTarget;
		private int aggroTime;
		private int stareTimer;
		private final TargetingConditions startAggroTargetConditions;
		private final TargetingConditions continueAggroTargetConditions = TargetingConditions.forCombat().ignoreLineOfSight();

		public CobraLookForPlayerGoal(MarsCobraEntity coberuh, @Nullable Predicate<LivingEntity> pSelectionPredicate) {
			super(coberuh, Player.class, 10, false, false, pSelectionPredicate);
			this.cobra = coberuh;
			this.startAggroTargetConditions = TargetingConditions.forCombat().range(this.getFollowDistance()).selector((p_32578_) -> {
				return coberuh.isLookingAtMe((Player)p_32578_);
			});
		}

		public boolean canUse() {
			this.pendingTarget = this.cobra.level().getNearestPlayer(this.startAggroTargetConditions, this.cobra);
			return this.pendingTarget != null;
		}

		public void start() {
			this.aggroTime = this.adjustedTickDelay(5);
			this.cobra.lookedAt++;;
		}

		public void stop() {
			this.pendingTarget = null;
			super.stop();
		}

		public boolean canContinueToUse() {
			if (this.pendingTarget != null) {
				if (!this.cobra.isLookingAtMe(this.pendingTarget)) {

					return false;
				} else {
					stareTimer = Mth.clamp(stareTimer + 1, 0, 120);
					System.out.println(stareTimer);
					if(stareTimer >= 32) {
						this.cobra.lookAt(this.pendingTarget, 10.0F, 10.0F);
						return true;
					}
					else {
						return false;
					}
				}
			} else {
				return this.target != null && this.continueAggroTargetConditions.test(this.cobra, this.target) ? true : super.canContinueToUse();
			}
		}

		public void tick() {
			if (this.cobra.getTarget() == null) {
				super.setTarget((LivingEntity)null);
			}				
			if (this.pendingTarget != null) {
				if (!this.cobra.isLookingAtMe(this.pendingTarget)) {
					stareTimer = Mth.clamp(stareTimer, 0, stareTimer - 1);
					System.out.println(stareTimer);
				}
				if (--this.aggroTime <= 0) {
					this.target = this.pendingTarget;
					this.pendingTarget = null;
					super.start();
				}
			}
			super.tick();

		}
	}


	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}
}