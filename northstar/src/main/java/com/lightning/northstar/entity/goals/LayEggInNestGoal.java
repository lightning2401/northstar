package com.lightning.northstar.entity.goals;

import com.lightning.northstar.block.MarsWormNestBlock;
import com.lightning.northstar.block.NorthstarBlocks;
import com.lightning.northstar.entity.MarsWormEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;

public class LayEggInNestGoal extends MoveToBlockGoal {
	   private final Block blockToRemove;
	   private final MarsWormEntity removerMob;
	   private BlockPos pos;
	   private int ticksSinceReachedGoal;
	   private static final int WAIT_AFTER_BLOCK_FOUND = 20;

	   public LayEggInNestGoal(MarsWormEntity pRemoverMob, double pSpeedModifier, int pSearchRange) {
	      super(pRemoverMob, pSpeedModifier, 24, pSearchRange);
	      this.blockToRemove = NorthstarBlocks.MARS_WORM_NEST.get();
	      this.removerMob = pRemoverMob;
	   }

	   @Override
	   public double acceptedDistance() {
		      return 2;
		   }
	   /**
	    * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
	    * method as well.
	    */
	   @Override
	   public boolean canUse() {
	      if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.removerMob.level(), this.removerMob)) {
	         return false;
	      } else if (this.removerMob.eggTimer > 0) {
	    	  return false;
	      } else if (this.nextStartTick > 0) {
	         --this.nextStartTick;
	         return false;
	      } else if (this.tryFindBlock()) {
	         this.nextStartTick = reducedTickDelay(20);
	         return true;
	      } else {
	         this.nextStartTick = this.nextStartTick(this.mob);
	         return false;
	      }
	   }

	   private boolean tryFindBlock() {
	      return this.blockPos != null && this.isValidTarget(this.mob.level(), this.blockPos) ? true : this.findNearestBlock();
	   }

	   /**
	    * Reset the task's internal state. Called when this task is interrupted by another one
	    */
	   @Override
	   public void stop() {
	      super.stop();
	      this.removerMob.fallDistance = 1.0F;
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
	      super.tick();
	      Level level = this.removerMob.level();
	      BlockState state = level.getBlockState(pos);
	      if(this.removerMob.eggTimer != 0) {
	    	  this.stop();
	      }
	      if(state.is(blockToRemove)) {
	    	  if(state.getValue(MarsWormNestBlock.HAS_EGG).booleanValue()) 
	    	  {this.stop();}
	      }else {this.stop();}
	      
	      if (this.isReachedTarget() && this.removerMob.eggTimer == 0) {
	    	  if(level.getBlockState(pos).is(NorthstarBlocks.MARS_WORM_NEST.get())) 
	    	  {level.setBlockAndUpdate(pos, level.getBlockState(pos).setValue(MarsWormNestBlock.HAS_EGG, true));
	    	  level.playSound(null, pos, SoundEvents.FROG_LAY_SPAWN, SoundSource.BLOCKS, 1, 1);}
	    	  removerMob.eggTimer = 6000;
	         ++this.ticksSinceReachedGoal;
	      }

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
	         if (!chunkaccess.getBlockState(pPos).canEntityDestroy(pLevel, pPos, this.removerMob)) return false;
	         boolean flag = chunkaccess.getBlockState(pPos).is(this.blockToRemove) && chunkaccess.getBlockState(pPos.above()).isAir() && chunkaccess.getBlockState(pPos.above(2)).isAir();
	         if(flag) 
	         {
	        	 if(!chunkaccess.getBlockState(pPos).getValue(MarsWormNestBlock.HAS_EGG)) {
		        	 pos = pPos;
	        	 }else {flag = false;}
	         }
	         return flag;
	      }
	   }
	}
