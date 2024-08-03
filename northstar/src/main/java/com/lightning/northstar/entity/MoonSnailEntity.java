package com.lightning.northstar.entity;

import com.lightning.northstar.NorthstarTags.NorthstarBlockTags;
import com.lightning.northstar.particle.SnailSlimeParticleData;
import com.lightning.northstar.sound.NorthstarSounds;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.ClientUtils;
import software.bernie.geckolib.util.GeckoLibUtil;

public class MoonSnailEntity extends Monster implements GeoEntity{
	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
	int slimeTimer = this.random.nextInt(6000) + 6000;

	protected MoonSnailEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.setMaxUpStep(1f);
	}

	protected void registerGoals() {
		this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 0.7D));
		this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 6.0F));
		this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
	}
	@Override
	public void tick() {
		super.tick();
		if(this.onGround() && this.tickCount % 4 == 0) {
			this.level().addParticle(new SnailSlimeParticleData(), this.getX(), this.getY() + 0.1, this.getZ(), 0.0D, 0.0D, 0.0D);
		}		
		if(slimeTimer > 0) {
			slimeTimer = Mth.clamp(slimeTimer, 0, slimeTimer - 1);
		}else if (!this.level().isClientSide && !this.isDeadOrDying() && slimeTimer <= 0) {
			playSound(SoundEvents.CHICKEN_EGG, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
			spawnAtLocation(Items.SLIME_BALL);
			gameEvent(GameEvent.ENTITY_PLACE);
			slimeTimer = 6000;
		}
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
		return NorthstarSounds.MOON_SNAIL_IDLE.get();
	}
	@Override
	protected SoundEvent getHurtSound(DamageSource pDamageSource) {
		super.getHurtSound(pDamageSource);
		return NorthstarSounds.MOON_SNAIL_HURT.get();
	}
	@Override
	protected SoundEvent getDeathSound() {
		super.getDeathSound();
		return NorthstarSounds.MOON_SNAIL_DIE.get();
	}
	
	public static boolean snailSpawnRules(EntityType<MoonSnailEntity> snail, LevelAccessor level, MobSpawnType spawntype, BlockPos pos, RandomSource rando) {
		int surfaceY = level.getHeight(Heightmap.Types.MOTION_BLOCKING,(int) pos.getX(),(int) pos.getZ());
		BlockState state = level.getBlockState(pos.below());
		if(level.getEntitiesOfClass(Monster.class, new AABB(pos).inflate(92)).size() >= 24) {
			return false;
		}else if (pos.getY() >= surfaceY) {
			return false;
		} else {
			return state.is(NorthstarBlockTags.NATURAL_MOON_BLOCKS.tag);
		}
	}
	@Override
	public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
		return false;
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		RawAnimation idle = RawAnimation.begin().thenLoop("idle");
		RawAnimation walk = RawAnimation.begin().thenLoop("walk");

		
		controllers.add(
				// Add our flying animation controller
				new AnimationController<>(this, 10, state -> state.setAndContinue(state.isMoving() ? walk : idle))
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
	
	public void readAdditionalSaveData(CompoundTag pCompound) {
		super.readAdditionalSaveData(pCompound);
		if (pCompound.contains("slimeTimer")) {
			this.slimeTimer = pCompound.getInt("slimeTimer");
		}
	}

	public void addAdditionalSaveData(CompoundTag pCompound) {
		super.addAdditionalSaveData(pCompound);
		pCompound.putInt("slimeTimer", this.slimeTimer);
	}
}
