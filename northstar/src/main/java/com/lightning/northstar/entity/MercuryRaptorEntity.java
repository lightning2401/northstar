package com.lightning.northstar.entity;

import java.util.UUID;

import com.lightning.northstar.NorthstarTags.NorthstarBlockTags;
import com.lightning.northstar.entity.goals.PushRedstoneComponentsGoal;
import com.lightning.northstar.sound.NorthstarSounds;
import com.lightning.northstar.world.dimension.NorthstarDimensions;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FleeSunGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RestrictSunGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
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

public class MercuryRaptorEntity extends Monster implements IAnimatable, IAnimationTickable {
	AnimationFactory factory = GeckoLibUtil.createFactory(this);
	private static final UUID SPEED_MODIFIER_ATTACKING_UUID = UUID.fromString("49455A49-7EC5-45BA-B886-3B90B23A1718");
	private static final AttributeModifier SPEED_MODIFIER_ATTACKING = new AttributeModifier(SPEED_MODIFIER_ATTACKING_UUID, "Attacking speed boost", 0.1D, AttributeModifier.Operation.ADDITION);
	private int attackTick;
	public int timeSpentAttacking;
	public int disruptTimer;
	
	@SuppressWarnings("deprecation")
	public MercuryRaptorEntity(EntityType<? extends MercuryRaptorEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.maxUpStep = 1f;
	}
	
	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 16.0D).add(Attributes.MAX_HEALTH, 20.0D).add(Attributes.ATTACK_DAMAGE, 5).add(Attributes.MOVEMENT_SPEED, 0.2f);
	}
	
	public static boolean raptorSpawnRules(EntityType<MercuryRaptorEntity> lunargrade, LevelAccessor level, MobSpawnType spawntype, BlockPos pos, RandomSource rando) {
		int surfaceY = level.getHeight(Heightmap.Types.MOTION_BLOCKING,(int) pos.getX(),(int) pos.getZ());
		BlockState state = level.getBlockState(pos.below());
		if (pos.getY() >= surfaceY) {
			return false;
		} else {
			return state.is(NorthstarBlockTags.NATURAL_MERCURY_BLOCKS.tag);
		}
	}
	
	private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
		
		if(this.attackTick > 0) {
			this.attackTick--;
			event.getController().setAnimation(new AnimationBuilder().addAnimation("bite", EDefaultLoopTypes.PLAY_ONCE));
		}else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F) ) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", EDefaultLoopTypes.LOOP));
			event.getController().animationSpeed = event.getLimbSwingAmount() * 2;
		} else {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", EDefaultLoopTypes.LOOP));
			event.getController().animationSpeed = 1;
		}

		return PlayState.CONTINUE;
	}
	
	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController<MercuryRaptorEntity>(this, "controller", 2, this::predicate));
		
	}
	
	//this handles client side stuff, and creates parity between server and client
	@Override
	public void handleEntityEvent(byte pId) {
		if (pId == 4) {
			this.attackTick = 40;
		}
		super.handleEntityEvent(pId);
	}
	
	@Override
	public void aiStep() {
		super.aiStep();
		boolean flag = this.isSunBurnTick();
		if (flag && (level.dimension() == NorthstarDimensions.MERCURY_DIM_KEY || level.dimension() == Level.OVERWORLD)) {
			this.setSecondsOnFire(8);
		}
	}
	
	@Override
	public void tick() {		
		if(this.getTarget() != null) {
			timeSpentAttacking++;
			System.out.println("timespentattacking: " + timeSpentAttacking);
		}
		if(disruptTimer > 0) {
			disruptTimer = Mth.clamp(disruptTimer, 0, disruptTimer - 1);
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
		return NorthstarSounds.MERCURY_RAPTOR_IDLE.get();
	}
	@Override
	protected SoundEvent getHurtSound(DamageSource pDamageSource) {
		return NorthstarSounds.MERCURY_RAPTOR_HURT.get();
	}
	@Override
	protected SoundEvent getDeathSound() {
		return NorthstarSounds.MERCURY_RAPTOR_DIE.get();
	}

	
	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(3, new RestrictSunGoal(this));
		this.goalSelector.addGoal(3, new FleeSunGoal(this, 1.5D));
		this.goalSelector.addGoal(3, new PushRedstoneComponentsGoal(this, 1.0D, 16));
		this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.5D, false));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Villager.class, true));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
		super.registerGoals();
	}
	@Override
	public boolean doHurtTarget(Entity pEntity) {
		this.level.broadcastEntityEvent(this, (byte)4);
		this.playSound(NorthstarSounds.MERCURY_RAPTOR_ATTACK.get(), 1.0F, 1.0F);
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
}