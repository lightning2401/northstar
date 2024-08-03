package com.lightning.northstar.entity;

import javax.annotation.Nullable;

import com.lightning.northstar.NorthstarTags.NorthstarBlockTags;
import com.lightning.northstar.block.NorthstarBlocks;
import com.lightning.northstar.block.crops.MartianFlowerBlock;
import com.lightning.northstar.block.crops.MartianTallFlowerBlock;
import com.lightning.northstar.item.NorthstarItems;
import com.lightning.northstar.sound.NorthstarSounds;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.ClientUtils;
import software.bernie.geckolib.util.GeckoLibUtil;

public class MarsMothEntity extends Monster implements GeoEntity {
	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
	private static final EntityDataAccessor<Byte> RESTING = SynchedEntityData.defineId(MarsMothEntity.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<Byte> FLYING = SynchedEntityData.defineId(MarsMothEntity.class, EntityDataSerializers.BYTE);
	private static final TargetingConditions BAT_RESTING_TARGETING = TargetingConditions.forNonCombat().range(4.0D);
	@Nullable
	private BlockPos targetPosition;
	private int pollinationTimer = 0;
	private int timeForPollination = 400;
	private int timeForPathFinding = 400;
	private boolean pollinating = false;
	
	//true = floor, false = ceiling
	boolean floorOrCeiling;
	//used for determining whether to land on the ground and start walking or to land on the ceiling and sleep
	   
	public MarsMothEntity(EntityType<? extends MarsMothEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.setMaxUpStep(1f);
	}
	
	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 16.0D).add(Attributes.MAX_HEALTH, 8.0D).add(Attributes.MOVEMENT_SPEED, 0.25f);
	}
	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		RawAnimation idle = RawAnimation.begin().thenLoop("idle");
		RawAnimation walk = RawAnimation.begin().thenLoop("walk");
		RawAnimation sleep = RawAnimation.begin().thenLoop("sleep");
		RawAnimation fly = RawAnimation.begin().thenPlay("fly");

		boolean resting = this.isResting();
		controllers.add(
				new AnimationController<>(this, state -> {
					if(this.isFlying() && !resting) {state.setControllerSpeed(2);  return state.setAndContinue(fly);}
					else if (state.isMoving()) {state.setControllerSpeed(1); return state.setAndContinue(walk);}
					else if (resting) {state.setControllerSpeed(1); return state.setAndContinue(sleep);}
					else state.setControllerSpeed(1); return state.setAndContinue(idle);})
						.setCustomInstructionKeyframeHandler(state -> {
							Player player = ClientUtils.getClientPlayer();

							if (player != null)
								player.displayClientMessage(Component.literal("KeyFraming"), true);
						})
		);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(RESTING, (byte)0);
		this.entityData.define(FLYING, (byte)1);
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
		if(isResting()) 
		{return NorthstarSounds.MARS_MOTH_SNORE.get();}
//		System.out.println("GABINGA!!!");
		if(random.nextInt(9) == 0)
		{return NorthstarSounds.MARS_MOTH_IDLE.get();}
		return null;
	}
	@Override
	protected SoundEvent getHurtSound(DamageSource pDamageSource) {
		return NorthstarSounds.MARS_MOTH_HURT.get();
	}
	@Override
	protected SoundEvent getDeathSound() {
		return NorthstarSounds.MARS_MOTH_DEATH.get();
	}

	
	//this handles client side stuff, and creates parity between server and client
	@Override
	public void handleEntityEvent(byte pId) {
		super.handleEntityEvent(pId);
	}
	
	public static boolean mothSpawnRules(EntityType<MarsMothEntity> moth, LevelAccessor level, MobSpawnType spawntype, BlockPos pos, RandomSource rando) {
		int surfaceY = level.getHeight(Heightmap.Types.MOTION_BLOCKING,(int) pos.getX(),(int) pos.getZ());
		BlockState state = level.getBlockState(pos.below());
		if (pos.getY() >= surfaceY) {
			return false;
		} else if (pos.getY() < surfaceY / 2) {
			int light = level.getMaxLocalRawBrightness(pos);
			return light != 0 ? false : checkMobSpawnRules(moth, level, spawntype, pos, rando) && state.is(NorthstarBlockTags.NATURAL_MARS_BLOCKS.tag);
		}
		else
			return false;
	}
	
	@Override
	public void tick() {
		super.tick();
		if(pollinationTimer > 0) {
			pollinationTimer = Mth.clamp(pollinationTimer, 0, pollinationTimer - 1);
		}
		if (this.isResting()) {
			this.setDeltaMovement(Vec3.ZERO);
			this.setPosRaw(this.getX(), (double)Mth.floor(this.getY()) + 1.0D - (double)this.getBbHeight(), this.getZ());
		} else if (this.isFlying()) {
			this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.6D, 1.0D));
		}
		
	}
	
//	@Override
	protected void usePlayerItem(Player player, InteractionHand hand, ItemStack stack) {
		if (this.isFood(stack)) {
			this.playSound(SoundEvents.CAT_EAT, 1.0F, 1.0F);
			this.targetPosition = new BlockPos((int)this.getX(),this.level().getHeight(Heightmap.Types.MOTION_BLOCKING,(int) this.getX(),(int) this.getZ()), (int)this.getZ());
			this.setFlying(true);
		}

//		super.usePlayerItem(player, hand, stack);
	}
	
//	@Override
	public boolean isFood(ItemStack stack) {
		return stack.is(NorthstarBlocks.GLOWING_MARS_ROOTS.get().asItem());
	}

	@Override
	protected void customServerAiStep() {
		super.customServerAiStep();
		BlockPos blockpos = this.blockPosition();
		BlockPos blockpos1 = blockpos.above();
		if (this.isResting()) {
			boolean flag = this.isSilent();
			if (this.level().getBlockState(blockpos1).isRedstoneConductor(this.level(), blockpos)) {
				if (this.random.nextInt(200) == 0) {
					this.yHeadRot = (float)this.random.nextInt(360);
				}

				if (this.level().getNearestPlayer(BAT_RESTING_TARGETING, this) != null) {
					this.setResting(false);
					if (!flag) {
						this.level().levelEvent(null, 1025, blockpos, 0);
					}
				}
			} else {
				this.setResting(false);
				if (!flag) {
					this.level().levelEvent(null, 1025, blockpos, 0);
				}
			}
		} else if (this.isFlying()){
			this.fallDistance = 0;
			if (this.targetPosition != null && (!this.level().isEmptyBlock(this.targetPosition) || this.targetPosition.getY() <= this.level().getMinBuildHeight())) {
				this.targetPosition = null;
			}
			
			if(timeForPathFinding > 0) {
				timeForPathFinding = Mth.clamp(timeForPathFinding, 0, timeForPathFinding - 1);
			}else {
				this.targetPosition = null;
				this.timeForPathFinding = 400;
			}
			
			if(pollinationTimer <= 0 && this.targetPosition == null) {
				this.targetPosition = this.searchForFlower();
			}
			

			if (this.targetPosition == null || this.random.nextInt(30) == 0 || (this.targetPosition.closerToCenterThan(this.position(), 2.0D) && !pollinating)) {
				this.targetPosition = new BlockPos((int)this.getX() + (int)this.random.nextInt(16) - (int)this.random.nextInt(16), (int) this.getY() + (int)this.random.nextInt(16) - (int)this.random.nextInt(16), (int) this.getZ() + (int)this.random.nextInt(16) - (int)this.random.nextInt(16));
			}
			if(timeForPollination > 0) {
				timeForPollination--;
			}else {
				pollinating = false;
			}
			
			if(pollinating && this.targetPosition.closerToCenterThan(this.position(), 0.5D)) {
				this.pollinating = false;
				this.pollinationTimer = 6000;
				if(this.level().getBlockState(this.targetPosition).getBlock() instanceof MartianFlowerBlock) {
					Item item = ((MartianFlowerBlock)this.level().getBlockState(this.targetPosition).getBlock()).getSeedItem();
					ItemEntity spawnedItem = new ItemEntity(level(), this.getX(), this.getY(), this.getZ(), new ItemStack(item, 1));
					this.level().addFreshEntity(spawnedItem);
				}
				else if(this.level().getBlockState(this.targetPosition).getBlock() instanceof MartianTallFlowerBlock) {
					Item item = NorthstarItems.MARS_SPROUT_SEEDS.get();
					ItemEntity spawnedItem = new ItemEntity(level(), this.getX(), this.getY(), this.getZ(), new ItemStack(item, 1));
					this.level().addFreshEntity(spawnedItem);
				}
				this.targetPosition = null;
			}
			if(this.targetPosition != null) {
				double tX = (double)this.targetPosition.getX() + 0.5D - this.getX();
				double tY = (double)this.targetPosition.getY() + 0.1D - this.getY();
				double tZ = (double)this.targetPosition.getZ() + 0.5D - this.getZ();
				Vec3 vec3 = this.getDeltaMovement();
				Vec3 vec31 = vec3.add((Math.signum(tX) * 0.5D - vec3.x) * (double)0.1F, (Math.signum(tY) * (double)0.7F - vec3.y) * (double)0.1F, (Math.signum(tZ) * 0.5D - vec3.z) * (double)0.1F);
				this.setDeltaMovement(vec31);
				float f = (float)(Mth.atan2(vec31.z, vec31.x) * (double)(180F / (float)Math.PI)) - 90.0F;
				float f1 = Mth.wrapDegrees(f - this.getYRot());
				this.zza = 0.5F;
				this.setYRot(this.getYRot() + f1);
				if (this.random.nextInt(100) == 0 && this.level().getBlockState(blockpos1).isRedstoneConductor(this.level(), blockpos1) && !this.pollinating) {
					this.setResting(true);
				} else if(this.random.nextInt(300) == 0 && this.level().getBlockState(blockpos.below()).isRedstoneConductor(level(), blockpos.below()) && !this.pollinating) {
					this.setFlying(false);
				}
			}
		}else if (this.random.nextInt(500) == 0) {
			this.setFlying(true);
		}
	}
	
	public void addAdditionalSaveData(CompoundTag pCompound) {
		super.addAdditionalSaveData(pCompound);
		pCompound.putBoolean("flying", this.isFlying());
		pCompound.putBoolean("resting", this.isResting());
		pCompound.putBoolean("pollinating", this.pollinating);
		pCompound.putInt("pollinationTimer", this.pollinationTimer);
		pCompound.putInt("timeForPollination", this.timeForPollination);
		pCompound.putInt("timeForPathFinding", this.timeForPathFinding);
	}
	public void readAdditionalSaveData(CompoundTag pCompound) {
		super.readAdditionalSaveData(pCompound);
		if (pCompound.contains("flying")) {
			this.setFlying(pCompound.getBoolean("flying"));
		}if (pCompound.contains("resting")) {
			this.setResting(pCompound.getBoolean("resting"));
		}if (pCompound.contains("pollinating")) {
			this.pollinating = pCompound.getBoolean("pollinating");
		}if (pCompound.contains("pollinationTimer")) {
			this.pollinationTimer = pCompound.getInt("pollinationTimer");
		}if (pCompound.contains("timeForPollination")) {
			this.timeForPollination = pCompound.getInt("timeForPollination");
		}if (pCompound.contains("timeForPathFinding")) {
			this.timeForPathFinding = pCompound.getInt("timeForPathFinding");
		}
	}
	
	@Override
	public boolean hurt(DamageSource pSource, float pAmount) {
		if (this.isInvulnerableTo(pSource)) {
			return false;
		} else {
			if (!this.level().isClientSide && this.isResting()) {
				this.setResting(false);
			}else if(!this.level().isClientSide && !this.isFlying()) {
				setFlying(true);
			}
			
			return super.hurt(pSource, pAmount);
		}
	}
	
	public boolean isResting() {
		return (this.entityData.get(RESTING) & 1) != 0;
	}
	public boolean isFlying() {
		return (this.entityData.get(FLYING) & 1) != 0;
	}

	public void setResting(boolean pIsResting) {
		byte b0 = this.entityData.get(RESTING);
		if (pIsResting) {
			this.entityData.set(RESTING, (byte)(b0 | 1));
		} else {
			this.entityData.set(RESTING, (byte)(b0 & -2));
		}
			
	}
	public void setFlying(boolean flying) {
		byte b0 = this.entityData.get(RESTING);
		if (flying) {
			this.entityData.set(FLYING, (byte)(b0 | 1));
		} else {
			this.entityData.set(FLYING, (byte)(b0 & -2));
		}
			
	}
	
	
	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		super.registerGoals();
	}
	@Nullable
	private BlockPos searchForFlower() {
		for(int bX = -5; bX < 5; bX++) {
			for(int bY = -5; bY < 5; bY++) {
				for(int bZ = -5; bZ < 5; bZ++) {
					BlockPos blockpo = this.blockPosition().offset(bX, bY, bZ);
					BlockState state = this.level().getBlockState(blockpo); {
						if(state.getBlock() instanceof MartianFlowerBlock && this.level().random.nextInt(4) == 0){
							if(state.getValue(MartianFlowerBlock.AGE) == 2) 
							{timeForPollination = 400;
							pollinating = true;
							return blockpo;};

						}else if(state.getBlock() instanceof MartianTallFlowerBlock && this.level().random.nextInt(4) == 0) {
							timeForPollination = 400;
							pollinating = true;
							return blockpo;
						}
					}
				}
			}
		}	
		return null;
	}
	
	public boolean doHurtTarget(Entity pEntity) {
		this.level().broadcastEntityEvent(this, (byte)4);
		this.playSound(SoundEvents.RAVAGER_ATTACK, 1.0F, 1.0F);
		return super.doHurtTarget(pEntity);
	}

//	@Override
	public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
		return null;
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}
}
