package com.lightning.northstar.entity.goals;

import com.lightning.northstar.entity.VenusStoneBullEntity;
import com.lightning.northstar.sound.NorthstarSounds;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.phys.Vec3;

public class ChargeAtTargetGoal extends MoveToBlockGoal {
	   private final VenusStoneBullEntity chargingMob;
	   private int ticksSinceReachedGoal;

	   public ChargeAtTargetGoal(VenusStoneBullEntity mob, double pSpeedModifier, int pSearchRange) {
	      super(mob, pSpeedModifier, 24, pSearchRange);
	      this.chargingMob = mob;
	   }

	   @Override
	   public double acceptedDistance() 
	   {return 0.5d;}
	   /**
	    * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
	    * method as well.
	    */
	   	@Override
	   public boolean canUse() {
		  if(chargingMob.chargeCooldown > 0 ) {
			  return false;
		  }else if (this.chargingMob.chargeTimer > 0) {
	    	  return true;
	      } else if (this.searchForTarget()) {
	         this.nextStartTick = reducedTickDelay(20);
	         this.chargingMob.charging = true;
	         chargingMob.level().broadcastEntityEvent(chargingMob, (byte)63);
	         return true;
	      } else {
	         this.nextStartTick = this.nextStartTick(this.mob);
	         chargingMob.moveDirection = null;
	         chargingMob.level().broadcastEntityEvent(chargingMob, (byte)65);
	         this.chargingMob.charging = false;
	         return false;
	      }
	   }

	   private boolean searchForTarget() {
		  if(this.chargingMob.getTarget() == null)
			  return false;
		  BlockPos targetPos = this.chargingMob.getTarget().blockPosition();
		  BlockPos subPos = targetPos.subtract((this.chargingMob.blockPosition()));
		  double newX = subPos.getX();
		  double newZ = subPos.getZ();
		  targetPos = this.chargingMob.blockPosition();
		  if(chargingMob.moveDirection == null || chargingMob.moveDirection == Vec3.ZERO)
		  {chargingMob.moveDirection = new Vec3(newX, 0, newZ);
//		  System.out.println("DOING THINGS!!!!!!");
		  chargingMob.ticksSpentCharging = 0;
			this.chargingMob.playSound(NorthstarSounds.VENUS_STONE_BULL_CHARGE.get(), 1.0F, 1.0F);;
			chargingMob.chargeTimer = 150;
			chargingMob.targetPos = this.chargingMob.getTarget().blockPosition();}
	      return true;
	   }

	   /**
	    * Reset the task's internal state. Called when this task is interrupted by another one
	    */
	   @Override
	   public void stop() {
	      super.stop();
//	      this.chargingMob.chargeTimer = 60;
	   }

	   /**
	    * Execute a one shot task or start executing a continuous task
	    */
	   @Override
	   public void start() {
	      super.start();
	      this.ticksSinceReachedGoal = 0;
	   }

	   public void playDestroyProgressSound(LevelAccessor pLevel, BlockPos pPos) {
	   }

	   public void playBreakSound(Level pLevel, BlockPos pPos) {
	   }

	   /**
	    * Keep ticking a continuous task that has already been started
	    */
	   @Override
	   public void tick() {
		  //im gonna scream i cant figure this out im so dumb 
	      if(chargingMob.ticksSpentCharging > 60) {
	    	  chargingMob.moveDirection = Vec3.ZERO;
	    	  chargingMob.targetPos = null;
	    	  chargingMob.chargeTimer = 0;
	    	  chargingMob.stopChargeTimer = 0;
	    	  chargingMob.charging = false;
	    	  chargingMob.level().broadcastEntityEvent(chargingMob, (byte) 67);
	    	  chargingMob.passedTarget = false;
	    	  chargingMob.chargeCooldown = 200;
	    	  chargingMob.ticksSpentCharging = 0;
	    	  this.stop();
	    	  return;
	      }else {
		      this.chargingMob.getNavigation().stop();
	      }
	      super.tick();
	      if(this.chargingMob.chargeTimer != 0) {
	    	  this.stop();
	      }
	      
//	      System.out.println("targetPos: " + chargingMob.targetPos);
//	      System.out.println("blockPos: " + chargingMob.blockPosition());
//	      System.out.println("moveDirection: " + chargingMob.moveDirection);
//	      System.out.println("target: " + chargingMob.getTarget());
	      if(chargingMob.targetPos != null  && chargingMob.blockPosition() != null && chargingMob.moveDirection != null) {
		      Vec3 targetVec = blockPosToVec3(chargingMob.targetPos);
		      Vec3 chargerVec = blockPosToVec3(chargingMob.blockPosition());
		      Vec3 subVec = targetVec.subtract(chargerVec);
//		      System.out.println("SUBVEC: " + subVec);
		      
		      for(Entity colliders : this.chargingMob.level().getEntities(chargingMob, chargingMob.getBoundingBox())) {
		    	  if(colliders instanceof LivingEntity lc) {
		    		  Vec3 vec = lc.getDeltaMovement();		    		  
		    		  lc.hurt(lc.damageSources().cramming(), 5);		
		    		  this.chargingMob.playSound(NorthstarSounds.VENUS_STONE_BULL_ATTACK.get(), 1.0F, 1.0F);
		    		  if(chargingMob.moveDirection != null)
		    		  {lc.setDeltaMovement(vec.x + (chargingMob.moveDirection.x / 50), vec.y + 0.3, vec.z + (chargingMob.moveDirection.z / 50));}
		    	  }
		      }
		      boolean xFlag = false;
		      boolean zFlag = false;
		      if(chargingMob.moveDirection.x > 0) {xFlag = subVec.x < 0;}
		      if(chargingMob.moveDirection.x < 0) {xFlag = subVec.x > 0;}
			      
		      if(chargingMob.moveDirection.z > 0) {zFlag = subVec.z < 0;}
		      if(chargingMob.moveDirection.z < 0) {zFlag = subVec.z > 0;}
			      
		      if (xFlag && zFlag && !chargingMob.passedTarget) {
		    	  if(!chargingMob.passedTarget) {
		    		  chargingMob.passedTarget = true;
		    		  chargingMob.level().broadcastEntityEvent(chargingMob, (byte) 66);
		    	  }
		      }
//		      System.out.println("distance: " +  blockPosToVec3(chargingMob.blockPosition()).distanceTo(blockPosToVec3(this.targetPos)));
		      if(blockPosToVec3(chargingMob.blockPosition()).distanceTo(blockPosToVec3(chargingMob.targetPos)) > 15) {
		    	  this.stop();
		    	  chargingMob.moveDirection = Vec3.ZERO;
		    	  chargingMob.targetPos = null;
		    	  chargingMob.chargeTimer = 0;
		    	  chargingMob.stopChargeTimer = 0;
		    	  chargingMob.charging = false;
		    	  chargingMob.level().broadcastEntityEvent(chargingMob, (byte) 67);
		    	  chargingMob.passedTarget = false;
		    	  chargingMob.chargeCooldown = 200;
		    	  chargingMob.ticksSpentCharging = 0;
		    	  this.stop();
		    	  return;
		      }
		      if(!chargingMob.passedTarget && chargingMob.getTarget() != null) {
			      LivingEntity livingentity = chargingMob.getTarget();
			      if (livingentity.distanceToSqr(chargingMob) < 4096.0D) {
			    	  double d1 = livingentity.getX() - this.chargingMob.getX();
			    	  double d2 = livingentity.getZ() - this.chargingMob.getZ();
			    	  chargingMob.setYRot(-((float)Mth.atan2(d1, d2)) * (180F / (float)Math.PI));
			    	  chargingMob.yBodyRot = chargingMob.getYRot();
			      }
		      }
		      
		      Vec3 vec = chargingMob.getDeltaMovement();
			  vec = vec.add(chargingMob.moveDirection.x, 0, chargingMob.moveDirection.z);
		      this.chargingMob.yBodyRot = this.chargingMob.getYRot();
		      chargingMob.ticksSpentCharging++;
			  Vec3 delta = this.chargingMob.getDeltaMovement();
			  if(!chargingMob.passedTarget)
			  {this.chargingMob.setDeltaMovement(delta.x + chargingMob.moveDirection.x / 50, delta.y + (chargingMob.moveDirection.y / 50), delta.z + chargingMob.moveDirection.z / 50);}
			  else
			  {this.chargingMob.setDeltaMovement(delta.x + ((chargingMob.moveDirection.x / 50) * 0.7), delta.y + ((chargingMob.moveDirection.y / 50) * 0.7), delta.z + ((chargingMob.moveDirection.z / 50) * 0.7)); ticksSinceReachedGoal++;} 
	      }
	   }
	   
	   public Vec3 blockPosToVec3(BlockPos pos) {
			Vec3 vec = new Vec3(pos.getX(), pos.getY(), pos.getZ());
			return vec;
		}
	   
	   @Override
	   public boolean canContinueToUse() {
		   return chargingMob.moveDirection != null && chargingMob.targetPos != null;
	   }
	   
	   @Override
	   protected void moveMobToBlock() {
		   //idk why im extending movetoblock goal but at this point im too far gone
		   //i cant change it anymore 
		   // sunk cost fallacy and all that
		   //except its NOT a fallacy!!!!!!!!!
		   //i am completely justified in this decision!!!!!!
	   }

	   /**
	    * Return {@code true} to set given position as destination
	    */
	   @Override
	   protected boolean isValidTarget(LevelReader pLevel, BlockPos pPos) {
	      ChunkAccess chunkaccess = pLevel.getChunk(SectionPos.blockToSectionCoord(pPos.getX()), SectionPos.blockToSectionCoord(pPos.getZ()), ChunkStatus.FULL, false);
	      if (chunkaccess == null) {
	         return false;
	      } else {
	    	  return true;
	      }
	   }
	}
