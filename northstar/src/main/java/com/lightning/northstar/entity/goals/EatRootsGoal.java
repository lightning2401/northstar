package com.lightning.northstar.entity.goals;

import java.util.EnumSet;
import java.util.function.Predicate;

import com.lightning.northstar.block.NorthstarBlocks;
import com.lightning.northstar.entity.MarsToadEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;

public class EatRootsGoal extends Goal {
	   private static final Predicate<BlockState> IS_EDIBLE = BlockStatePredicate.forBlock(NorthstarBlocks.MARS_ROOTS.get());
	   private final Mob mob;
	   private final Level level;
	   private int eatAnimationTick;
	   private boolean performedCheck = false;

	   public EatRootsGoal(Mob pMob) {
	      this.mob = pMob;
	      this.level = pMob.level;
	      this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
	   }

	   public boolean canUse() {
	      if (this.mob.getRandom().nextInt(this.mob.isBaby() ? 50 : 1000) != 0) {
	         return false;
	      } else {
	         BlockPos blockpos = this.mob.blockPosition();
	         if (IS_EDIBLE.test(this.level.getBlockState(blockpos))) {
	            return true;
	         }else
	        	 return false;
	      }
	   }
	   public void start() {
		   
	      this.eatAnimationTick = this.adjustedTickDelay(40);
	      if(this.mob instanceof MarsToadEntity toad) {
	    	  toad.eating = 40;
	      }
	      this.level.broadcastEntityEvent(this.mob, (byte)10);
	      this.mob.getNavigation().stop();
	   }

	   public void stop() {
	      this.eatAnimationTick = 0;
	   }


	   public boolean canContinueToUse() {
	      return this.eatAnimationTick > 0;
	   }

	   public int getEatAnimationTick() {
	      return this.eatAnimationTick;
	   }

	   public void tick() {
		   if(!performedCheck) { 
			   int rootnumber = 0;
			   for(BlockState state : this.level.getBlockStates(this.mob.getBoundingBox().inflate(15)).toList()) {
				   if(state.is(NorthstarBlocks.MARS_ROOTS.get())) {
					   rootnumber++;
				   }
			   }
			   if(rootnumber < 10) {
				   this.stop();
				   return;
			   }
			   performedCheck = true;
		   }
	      this.eatAnimationTick = Math.max(0, this.eatAnimationTick - 1);
	      if (this.eatAnimationTick == this.adjustedTickDelay(4)) {
	         BlockPos blockpos = this.mob.blockPosition();
	         if (IS_EDIBLE.test(this.level.getBlockState(blockpos))) {
	            if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this.mob)) {
	               this.level.destroyBlock(blockpos, false);
	            }

	            this.mob.ate();
	         }
	      }
	   }
	}