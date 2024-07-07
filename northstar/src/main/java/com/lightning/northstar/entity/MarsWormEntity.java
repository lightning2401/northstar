package com.lightning.northstar.entity;

import java.util.Collections;
import java.util.UUID;
import java.util.function.BiConsumer;

import javax.annotation.Nullable;

import org.jetbrains.annotations.Contract;

import com.lightning.northstar.NorthstarTags.NorthstarBlockTags;
import com.lightning.northstar.block.NorthstarBlocks;
import com.lightning.northstar.entity.goals.LayEggInNestGoal;
import com.lightning.northstar.sound.NorthstarSounds;
import com.mojang.serialization.Dynamic;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Unit;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RemoveBlockGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.monster.warden.AngerLevel;
import net.minecraft.world.entity.monster.warden.AngerManagement;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent.Context;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.vibrations.VibrationListener;
import net.minecraft.world.level.levelgen.Heightmap;
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

public class MarsWormEntity extends Monster implements IAnimatable, IAnimationTickable, VibrationListener.VibrationListenerConfig {
	AnimationFactory factory = GeckoLibUtil.createFactory(this);
	private static final UUID SPEED_MODIFIER_ATTACKING_UUID = UUID.fromString("49455A49-7EC5-45BA-B886-3B90B23A1718");
	private static final AttributeModifier SPEED_MODIFIER_ATTACKING = new AttributeModifier(SPEED_MODIFIER_ATTACKING_UUID, "Attacking speed boost", 0.2D, AttributeModifier.Operation.ADDITION);
	private static final EntityDataAccessor<Integer> CLIENT_ANGER_LEVEL = SynchedEntityData.defineId(MarsWormEntity.class, EntityDataSerializers.INT);
	private final DynamicGameEventListener<VibrationListener> dynamicGameEventListener;
	private boolean aggro;
	private int angerTimer = 0;
	public int eggTimer = 0;
	private LivingEntity notTarget;
	private UUID notTargetUUID;
	
	private int attackTick;
	private AngerManagement angerManagement = new AngerManagement(this::canTargetEntity, Collections.emptyList());
	
	public MarsWormEntity(EntityType<? extends MarsWormEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	    this.maxUpStep = 1.0F;
	    this.dynamicGameEventListener = new DynamicGameEventListener<>(new VibrationListener(new EntityPositionSource(this, this.getEyeHeight()), 16, this, (VibrationListener.ReceivingEvent)null, 0.0F, 0));
	}
	
	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 16.0D).add(Attributes.MAX_HEALTH, 20.0D).add(Attributes.ATTACK_DAMAGE, 5).add(Attributes.MOVEMENT_SPEED, 0.2f);
	}
	
	private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
		if(this.attackTick > 0) {
			this.attackTick--;
			event.getController().setAnimation(new AnimationBuilder().addAnimation("bite", EDefaultLoopTypes.PLAY_ONCE));
			event.getController().animationSpeed = 1;
		}
		else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F) ) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", EDefaultLoopTypes.LOOP));
			event.getController().animationSpeed = event.getLimbSwingAmount();
		} else {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", EDefaultLoopTypes.LOOP));
			event.getController().animationSpeed = 1;
		}
		return PlayState.CONTINUE;
	}
	
	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController<MarsWormEntity>(this, "controller", 2, this::predicate));
		
	}
	
	//this handles client side stuff, and creates parity between server and client
	@Override
	public void handleEntityEvent(byte pId) {
		if (pId == 4) {
			this.attackTick = 40;
		}else{
			super.handleEntityEvent(pId);
		}

	}
	
	public static boolean wormSpawnRules(EntityType<MarsWormEntity> cobra, LevelAccessor level, MobSpawnType spawntype, BlockPos pos, RandomSource rando) {
		int surfaceY = level.getHeight(Heightmap.Types.MOTION_BLOCKING,(int) pos.getX(),(int) pos.getZ());
		BlockState state = level.getBlockState(pos.below());
		
		if (pos.getY() >= surfaceY) {
			return false;
		} else if (pos.getY() < surfaceY / 2.5) {
			int light = level.getMaxLocalRawBrightness(pos);
			return light != 0 ? false : checkMobSpawnRules(cobra, level, spawntype, pos, rando) && state.is(NorthstarBlockTags.NATURAL_MARS_BLOCKS.tag);
		}
		else 
			return false;
	}
	
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(CLIENT_ANGER_LEVEL, 0);
	}

	public int getClientAngerLevel() {
		return this.entityData.get(CLIENT_ANGER_LEVEL);
	}
	public void updateDynamicGameEventListener(BiConsumer<DynamicGameEventListener<?>, ServerLevel> pListenerConsumer) {
		Level level = this.level;
		if (level instanceof ServerLevel serverlevel) {
			pListenerConsumer.accept(this.dynamicGameEventListener, serverlevel);
		}
	}
	
	private void syncClientAngerLevel() {
		this.entityData.set(CLIENT_ANGER_LEVEL, this.getActiveAnger());
	}
	
	private int getActiveAnger() {
		return this.angerManagement.getActiveAnger(this.getTarget());
	}

	
	
	@Override
	public void tick() {		
	      Level level = this.level;
	      
	      if(eggTimer > 0) {eggTimer--;}
	      
	      if (level instanceof ServerLevel serverlevel) {
	    	  if(notTargetUUID != null) {
	    		  notTarget = (LivingEntity) ((ServerLevel) level).getEntity(notTargetUUID);
	    		  notTargetUUID = null;
	    	  }
	    	  
	         this.dynamicGameEventListener.getListener().tick(serverlevel);
	         
	         if(getTarget() != null) {
		         if(angerTimer > 0 && this.getTarget().distanceTo(this) > this.getAttributeValue(Attributes.FOLLOW_RANGE)) {
		        	 angerTimer--;
		         }
	         }
	         if(this.getTarget() != null) {
		         if(this.getTarget().isSpectator()) 
		         {this.setTarget(null); this.notTarget = null;}
		         if(this.getTarget() instanceof Player plyr)
		         {if(plyr.isCreative()) {this.setTarget(null); this.notTarget = null;}}
	         }
	         
	         
	         if(tickCount % 40 == 0) {
	        	 
	         } 
	         
	         if(angerTimer <= 0) {
	        	 this.setTarget(null);
	        	 notTarget = null;
	        	 aggro = false;
	         }
	         for(Entity ent : level.getEntities(this, getBoundingBox())) {
	        	 if(ent instanceof LivingEntity lEnt)
	        	 { 
	        		 if(canTargetEntity(lEnt)) {
	        			 this.notTarget = lEnt;
	        			 aggro = true;
	        		 }
	        	 }
	         }
	         
	         
	         if(this.notTarget != null) {
	        	 if(this.distanceTo(notTarget) < 2) {
	        		 aggro = true;
	        		 System.out.println("bruh");
	        	 }
		         if(this.notTarget.getHealth() <= 0) {
		        	 this.notTarget = null;
		        	 this.setTarget(null);
		        	 this.aggro = false;
		         }
	         }
	         
	         if(aggro && this.notTarget != null) {
	        	 this.setTarget(notTarget);
	         }else {
	        	 this.setTarget(null);
	        	 aggro = false;
	         }
	         if(aggro && this.getTarget() == null) {
	        	 aggro = false;
	         }
	      }
	      super.tick();
	}
	
	public void addAdditionalSaveData(CompoundTag pCompound) {
		super.addAdditionalSaveData(pCompound);
		pCompound.putBoolean("aggro", aggro);
		if(this.notTarget != null)
		{pCompound.putUUID("notTarget", notTarget.getUUID());}
		pCompound.putInt("eggTimer", eggTimer);
		pCompound.putInt("angerTimer", angerTimer);
	}
	public void readAdditionalSaveData(CompoundTag pCompound) {
		super.readAdditionalSaveData(pCompound);
		if (pCompound.contains("aggro")) {
			aggro = pCompound.getBoolean("aggro");
		}
		if (pCompound.contains("notTarget")) {
			notTargetUUID = pCompound.getUUID("notTarget");
		}
		eggTimer = pCompound.getInt("eggTimer");
		angerTimer = pCompound.getInt("angerTimer");
	}
	
	
	public boolean hurt(DamageSource pSource, float pAmount) {
		boolean flag = super.hurt(pSource, pAmount);
		if (!this.level.isClientSide && !this.isNoAi()) {
			Entity entity = pSource.getEntity();
			if(entity instanceof LivingEntity living) {
				if(canTargetEntity(entity) && this.notTarget == null) {
					this.notTarget = living;
					this.aggro = true;
				
				}
			}
		}

		return flag;
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
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
		this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0, false));
		this.goalSelector.addGoal(2, new LayEggInNestGoal(this, 1.0, 10));
		super.registerGoals();
	}
	@Override
	protected SoundEvent getAmbientSound() {
		super.getAmbientSound();
		return NorthstarSounds.MARS_WORM_CLICK.get();
	}
	@Override
	protected SoundEvent getHurtSound(DamageSource pDamageSource) {
		super.getHurtSound(pDamageSource);
		return NorthstarSounds.MARS_WORM_HURT.get();
	}
	protected SoundEvent getDeathSound() {
		super.getDeathSound();
		return NorthstarSounds.MARS_WORM_DEATH.get();
	}
	@Override
	public boolean doHurtTarget(Entity pEntity) {
		boolean flag = super.doHurtTarget(pEntity);
		this.level.broadcastEntityEvent(this, (byte)4);
		this.playSound(NorthstarSounds.MARS_WORM_ATTACK.get(), 1.0F, 1.0F);
		if(pEntity instanceof LivingEntity liv) {
			if(liv.getHealth() <= 0) {
				this.setTarget(null);
				aggro = false;
				this.notTarget = null;
			}
		}
		return flag;
	}


	@Override
	public AnimationFactory getFactory() {
		return factory;
	}

	@Override
	public int tickTimer() {
		return tickCount;
	}
	
	@Contract("null->false")
	public boolean canTargetEntity(@Nullable Entity ent) {
		if (ent instanceof LivingEntity livingentity) {
			if (this.level == ent.level && (ent instanceof Player || ent instanceof ZombifiedPiglin || ent instanceof MarsToadEntity)) {
				if(ent instanceof Player player) {
					if(player.isCreative()) {
						return false;
					}
				}
				if(ent.isSpectator()) {
					return false;
				}
				if(notTarget == null)
				{notTarget = livingentity;
				angerTimer = 2400;}
				return true;
	        }
	    }
	    return false;
	}

	@Override
	public boolean shouldListen(ServerLevel pLevel, GameEventListener pListener, BlockPos pPos, GameEvent pGameEvent,
			Context pContext) {
	      if (!this.isNoAi() && !this.isDeadOrDying() && !this.getBrain().hasMemoryValue(MemoryModuleType.VIBRATION_COOLDOWN) && pLevel.getWorldBorder().isWithinBounds(pPos) && !this.isRemoved() && this.level == pLevel) {
	          Entity entity = pContext.sourceEntity();
	          if (entity instanceof LivingEntity livingentity) {
	             if (!this.canTargetEntity(livingentity)) {
	                return false;
	             }
	          }

	          return true;
	       } else {
	          return false;
	       }
	    }
	@Override
	public void onSignalReceive(ServerLevel pLevel, GameEventListener pListener, BlockPos pSourcePos, GameEvent pGameEvent, @Nullable Entity pSourceEntity, @Nullable Entity pProjectileOwner, float pDistance) {
//		System.out.println("Big Bazinga 24");   
		if (!this.isDeadOrDying()) {
			this.brain.setMemoryWithExpiry(MemoryModuleType.VIBRATION_COOLDOWN, Unit.INSTANCE, 40L);
			pLevel.broadcastEntityEvent(this, (byte)61);
			if(!this.aggro)
			{this.playSound(NorthstarSounds.MARS_WORM_CLICK_NOTICE.get(), 5.0F, this.getVoicePitch());}
			BlockPos blockpos = pSourcePos;
			MarsWormAi.setDisturbanceLocation(this, blockpos);
			if(this.notTarget != null && !aggro && (this.notTarget == pSourceEntity || this.notTarget == pProjectileOwner)) {
				aggro = true;
			}
		         

		}
	}



}
