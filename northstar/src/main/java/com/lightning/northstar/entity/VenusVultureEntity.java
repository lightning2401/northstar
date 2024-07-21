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
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
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

public class VenusVultureEntity extends Monster implements IAnimatable, IAnimationTickable {
	AnimationFactory factory = GeckoLibUtil.createFactory(this);
	private static final EntityDataAccessor<Byte> FLYING = SynchedEntityData.defineId(VenusVultureEntity.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<Byte> IS_LANDING = SynchedEntityData.defineId(VenusVultureEntity.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<Byte> IS_TAKING_OFF = SynchedEntityData.defineId(VenusVultureEntity.class, EntityDataSerializers.BYTE);
	@Nullable
	private BlockPos targetPosition;
	private int pollinationTimer = 0;
	private int rotationTimer = 400;
	private int flapTimer = 45;
	private float rotationModifier = 0;
	private int timeForPathFinding = 400;
	private boolean pollinating = false;
	BlockPos anchorPoint;
	
	//true = floor, false = ceiling
	boolean floorOrCeiling;
	//used for determining whether to land on the ground and start walking or to land on the ceiling and sleep
	   
	public VenusVultureEntity(EntityType<? extends VenusVultureEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		anchorPoint = this.blockPosition();
	}
	
	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 16.0D).add(Attributes.MAX_HEALTH, 8.0D).add(Attributes.MOVEMENT_SPEED, 0.25f);
	}
	
	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {		
	}
	
	public static boolean vultureSpawnRules(EntityType<VenusVultureEntity> moth, LevelAccessor level, MobSpawnType spawntype, BlockPos pos, RandomSource rando) {
		BlockState state = level.getBlockState(pos.below());
		return state.is(NorthstarBlockTags.NATURAL_VENUS_BLOCKS.tag) && level.canSeeSky(pos);
	}
	
	private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
		if(flapTimer > 0 && this.isFlying()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("flap", EDefaultLoopTypes.LOOP));
			event.getController().animationSpeed = 1;
		}else if (this.isFlying() && !(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("fly", EDefaultLoopTypes.LOOP));
			event.getController().animationSpeed = 2;
		}else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", EDefaultLoopTypes.LOOP));
			event.getController().animationSpeed = event.getLimbSwingAmount();
		} else {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", EDefaultLoopTypes.LOOP));
			event.getController().animationSpeed = 1;
		}


		return PlayState.CONTINUE;
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(FLYING, (byte)1);
		this.entityData.define(IS_LANDING, (byte)0);
		this.entityData.define(IS_TAKING_OFF, (byte)0);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		super.getAmbientSound();
		return NorthstarSounds.VENUS_VULTURE_IDLE.get();
	}
	@Override
	protected SoundEvent getHurtSound(DamageSource pDamageSource) {
		return NorthstarSounds.VENUS_VULTURE_HURT.get();
	}
	@Override
	protected SoundEvent getDeathSound() {
		return NorthstarSounds.VENUS_VULTURE_DIE.get();
	}
	
	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController<VenusVultureEntity>(this, "controller", 2, this::predicate));
		
	}
	
	//this handles client side stuff, and creates parity between server and client
	@Override
	public void handleEntityEvent(byte pId) {
		super.handleEntityEvent(pId);
		if(pId == 120) {
			this.setFlying(false);
		}
		if(pId == 150) {
			flapTimer = 45;
		}
	}
	
	@Override
	public void tick() {
		super.tick();
		if(this.isFlying()) {
			this.setBoundingBox(new AABB(-0.8, 0, -0.8, 0.8, 0.9, 0.8).move(this.getX(), this.getY(), this.getZ()));
			this.setDeltaMovement(this.getForward());
			if(this.rotationTimer > 0 && rotationModifier != 0) {
				this.rotationTimer--;

				this.setXRot(this.getXRot() + rotationModifier);
				flapTimer = Mth.clamp(flapTimer, 0, flapTimer - 1);
				int surfaceDif = this.blockPosition().getY() - level.getHeight(Heightmap.Types.MOTION_BLOCKING, blockPosition().getX(), blockPosition().getZ());
				Vec3 delta = this.getDeltaMovement();
				if(isTakingOff() && surfaceDif < 15){
					this.setDeltaMovement(delta.x,delta.y + 0.9,delta.z);
				}else if (surfaceDif > 25) {
					this.setDeltaMovement(delta.x,delta.y - 0.5,delta.z);
				}else if (surfaceDif < 15) {
					this.setDeltaMovement(delta.x,delta.y + 0.5,delta.z);
				} else {
					setTakingOff(false);
				}
				if(isLanding()) {
					this.setDeltaMovement(delta.x,delta.y - 0.7,delta.z);
					if(!this.level.getBlockState(this.blockPosition().below()).isAir()) {
						this.level.broadcastEntityEvent(this, (byte) 120);
						this.setFlying(false);
						setTakingOff(false);
					}
				}
				if(level.random.nextInt(3000) == 0) {
					setIsLanding(true);
				}
			}
			else {
				rotationModifier = this.level.random.nextFloat();
				rotationModifier = Mth.clamp(rotationModifier, 0.2f, 0.8f);
				flapTimer = 45;
				rotationModifier = this.level.random.nextBoolean() ? rotationModifier : rotationModifier * -1;
				rotationTimer = this.level.random.nextIntBetweenInclusive(300, 1500);
			}
			for(Player player : this.level.getEntitiesOfClass(Player.class, getBoundingBox().inflate(15))) {
				if(isFood(player.getItemInHand(InteractionHand.MAIN_HAND)) || isFood(player.getItemInHand(InteractionHand.OFF_HAND))) {
					setIsLanding(true);
				}
			}
			
		}else {
			this.setBoundingBox(new AABB(-0.4, 0, -0.4, 0.4, 1.8, 0.4).move(this.getX(), this.getY(), this.getZ()));
			if(level.random.nextInt(1000) == 0) {
				setFlying(true);
				setTakingOff(true);
				setIsLanding(false);
			}
		}		
	}
	
	public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
	      ItemStack itemstack = pPlayer.getItemInHand(pHand);
	      if (this.isFood(itemstack)) {
	         if (this.level.isClientSide) {
	            return InteractionResult.CONSUME;
	         }
	         this.usePlayerItem(pPlayer, pHand, itemstack);
	      }

	      return super.mobInteract(pPlayer, pHand);
	   }
	
	protected void usePlayerItem(Player player, InteractionHand hand, ItemStack stack) {
		if (this.isFood(stack)) {
			this.playSound(SoundEvents.CAT_EAT, 1.0F, 1.0F);
			this.targetPosition = new BlockPos(this.getX(),this.level.getHeight(Heightmap.Types.MOTION_BLOCKING,(int) this.getX(),(int) this.getZ()),this.getZ());
		}
	}
	
	public boolean isFood(ItemStack stack) {
		return stack.is(NorthstarBlocks.BLOOM_FUNGUS.get().asItem());
	}

	@Override
	protected void customServerAiStep() {
		super.customServerAiStep();
		BlockPos blockpos = this.blockPosition();
		if (this.isFlying()){
			this.fallDistance = 0;
			if (this.targetPosition != null && (!this.level.isEmptyBlock(this.targetPosition) || this.targetPosition.getY() <= this.level.getMinBuildHeight())) {
				this.targetPosition = null;
			}
			
			if(timeForPathFinding > 0) {
				timeForPathFinding = Mth.clamp(timeForPathFinding, 0, timeForPathFinding - 1);
			}else {
				this.targetPosition = null;
				this.timeForPathFinding = 400;
			}
			

			if (this.targetPosition == null || this.random.nextInt(30) == 0 || (this.targetPosition.closerToCenterThan(this.position(), 2.0D) && !pollinating)) {
				this.targetPosition = new BlockPos(this.getX() + (double)this.random.nextInt(16) - (double)this.random.nextInt(16), this.getY() + (double)this.random.nextInt(16) - (double)this.random.nextInt(16), this.getZ() + (double)this.random.nextInt(16) - (double)this.random.nextInt(16));
			}
			
			if(pollinating && this.targetPosition.closerToCenterThan(this.position(), 0.5D)) {
				this.pollinating = false;
				this.pollinationTimer = 6000;
				if(this.level.getBlockState(this.targetPosition).getBlock() instanceof MartianFlowerBlock) {
					Item item = ((MartianFlowerBlock)this.level.getBlockState(this.targetPosition).getBlock()).getSeedItem();
					ItemEntity spawnedItem = new ItemEntity(level, this.getX(), this.getY(), this.getZ(), new ItemStack(item, 1));
					this.level.addFreshEntity(spawnedItem);
				}
				else if(this.level.getBlockState(this.targetPosition).getBlock() instanceof MartianTallFlowerBlock) {
					Item item = NorthstarItems.MARS_SPROUT_SEEDS.get();
					ItemEntity spawnedItem = new ItemEntity(level, this.getX(), this.getY(), this.getZ(), new ItemStack(item, 1));
					this.level.addFreshEntity(spawnedItem);
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
				if(this.random.nextInt(300) == 0 && this.level.getBlockState(blockpos.below()).isRedstoneConductor(level, blockpos.below()) && !this.pollinating) {
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
		pCompound.putBoolean("pollinating", this.pollinating);
		pCompound.putInt("pollinationTimer", this.pollinationTimer);
		pCompound.putInt("timeForPathFinding", this.timeForPathFinding);
	}
	public void readAdditionalSaveData(CompoundTag pCompound) {
		super.readAdditionalSaveData(pCompound);
		if (pCompound.contains("flying")) {
			this.setFlying(pCompound.getBoolean("flying"));
		}if (pCompound.contains("pollinating")) {
			this.pollinating = pCompound.getBoolean("pollinating");
		}if (pCompound.contains("pollinationTimer")) {
			this.pollinationTimer = pCompound.getInt("pollinationTimer");
		}if (pCompound.contains("timeForPathFinding")) {
			this.timeForPathFinding = pCompound.getInt("timeForPathFinding");
		}
	}
	
	@Override
	public boolean hurt(DamageSource pSource, float pAmount) {
		if (this.isInvulnerableTo(pSource)) {
			return false;
		} else {
			if(!this.level.isClientSide && !this.isFlying()) {
				setFlying(true);
				setTakingOff(true);
				setIsLanding(false);
			}
			
			return super.hurt(pSource, pAmount);
		}
	}

	public boolean isFlying() {
		return (this.entityData.get(FLYING) & 1) != 0;
	}

	public void setFlying(boolean flying) {
		if (flying) {
			this.entityData.set(FLYING, (byte) 1 );
			rotationModifier = this.level.random.nextFloat();
			rotationModifier = Mth.clamp(rotationModifier, 0.2f, 0.8f);
			flapTimer = 45;
			rotationModifier = this.level.random.nextBoolean() ? rotationModifier : rotationModifier * -1;
			this.level.broadcastEntityEvent(this, (byte)150);
		} else {
			this.entityData.set(FLYING, (byte) -2 );
		}
			
	}
	
	public boolean isTakingOff() {
		return (this.entityData.get(IS_TAKING_OFF) & 1) != 0;
	}
	public boolean isLanding() {
		return (this.entityData.get(IS_LANDING) & 1) != 0;
	}

	public void setIsLanding(boolean flying) {
		if (flying) {
			this.entityData.set(IS_LANDING, (byte) 1 );
		} else {
			this.entityData.set(IS_LANDING, (byte) -2 );
		}
	}
	
	public void setTakingOff(boolean flying) {
		if (flying) {
			this.entityData.set(IS_TAKING_OFF, (byte) 1 );
		} else {
			this.entityData.set(IS_TAKING_OFF, (byte) -2 );
		}
	}
	
	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(3, new TemptGoal(this, 1, Ingredient.of(NorthstarBlocks.BLOOM_FUNGUS.get().asItem()), false));
		super.registerGoals();
	}
	
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

//	@Override
	public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
		return null;
	}
}
