package com.lightning.northstar.entity;

import com.lightning.northstar.NorthstarTags.NorthstarBlockTags;
import com.lightning.northstar.sound.NorthstarSounds;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.monster.Monster;
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

public class MoonEelEntity extends Monster implements IAnimatable, IAnimationTickable{
	AnimationFactory factory = GeckoLibUtil.createFactory(this);

	protected MoonEelEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.maxUpStep = 1;
	}

	protected void registerGoals() {
		this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 0.7D));
		this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 6.0F));
		this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
		return NorthstarSounds.MOON_EEL_IDLE.get();
	}
	@Override
	protected SoundEvent getHurtSound(DamageSource pDamageSource) {
		super.getHurtSound(pDamageSource);
		return NorthstarSounds.MOON_EEL_HURT.get();
	}
	@Override
	protected SoundEvent getDeathSound() {
		super.getDeathSound();
		return NorthstarSounds.MOON_EEL_DIE.get();
	}
	
	@Override
	public int tickTimer() {
		return tickCount;
	}
	
	public static boolean eelSpawnRules(EntityType<MoonEelEntity> eel, LevelAccessor level, MobSpawnType spawntype, BlockPos pos, RandomSource rando) {
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
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController<MoonEelEntity>(this, "controller", 2, this::predicate));
	}
	
	private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
		if (!(event.getLimbSwingAmount() > -0.05F && event.getLimbSwingAmount() < 0.05F)) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", EDefaultLoopTypes.LOOP));
		} else {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", EDefaultLoopTypes.LOOP));
		}

		return PlayState.CONTINUE;
	}

	@Override
	public AnimationFactory getFactory() {
		return factory;
	}
}
