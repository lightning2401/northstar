package com.lightning.northstar.entity.goals;

import com.lightning.northstar.block.MarsWormNestBlock;
import com.lightning.northstar.block.NorthstarBlocks;
import com.lightning.northstar.entity.MarsWormEntity;
import com.lightning.northstar.entity.MercuryRaptorEntity;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.gameevent.GameEvent;

public class PushRedstoneComponentsGoal extends MoveToBlockGoal {
	   private final MercuryRaptorEntity disruptor;
	   private BlockPos pos;
	   private int ticksSinceReachedGoal;
	   private static final int WAIT_AFTER_BLOCK_FOUND = 20;

	   public PushRedstoneComponentsGoal(MercuryRaptorEntity pRemoverMob, double pSpeedModifier, int pSearchRange) {
	      super(pRemoverMob, pSpeedModifier, 24, pSearchRange);
	      this.disruptor = pRemoverMob;
	   }

	   @Override
	   public double acceptedDistance() {
		      return 3;
		   }
	   /**
	    * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
	    * method as well.
	    */
	   @Override
	   public boolean canUse() {
	      if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.disruptor.level, this.disruptor)) {
	         return false;
	      } else if (disruptor.disruptTimer > 0 && disruptor.timeSpentAttacking < 300) {
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
	      return this.blockPos != null && this.isValidTarget(this.mob.level, this.blockPos) ? true : this.findNearestBlock();
	   }

	   /**
	    * Reset the task's internal state. Called when this task is interrupted by another one
	    */
	   @Override
	   public void stop() {
	      super.stop();
	      this.disruptor.fallDistance = 1.0F;
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
	      Level level = this.disruptor.level;
	      BlockState state = level.getBlockState(pos);
	      if(this.disruptor.disruptTimer != 0) {
	    	  this.stop();
	      }
	      
	      if (this.isReachedTarget() && this.disruptor.disruptTimer == 0) {
	    	  BlockState newstate = level.getBlockState(pos);
	    	  disruptor.disruptTimer = 600;
	    	  ++this.ticksSinceReachedGoal;
	    	  if(newstate.getBlock() instanceof SlidingDoorBlock sliding) 
	    	  {
	    	      disruptor.timeSpentAttacking = 0;
	    		  sliding.setOpen(disruptor, level, newstate, pos, !newstate.getValue(SlidingDoorBlock.OPEN));
	    	  }
	    	  if(newstate.getBlock() instanceof DoorBlock door) 
	    	  {
	    	      disruptor.timeSpentAttacking = 0;
		    	  door.setOpen(disruptor, level, newstate, pos, !newstate.getValue(DoorBlock.OPEN));
	    		  return;
	    	  }
	    	  if(newstate.getBlock() instanceof ButtonBlock button) 
	    	  {
	    	      disruptor.timeSpentAttacking = 0;
		    	  button.press(newstate, level, pos);
	    		  return;
	    	  }
	    	  if(newstate.getBlock() instanceof LeverBlock lever) 
	    	  {
	    	      disruptor.timeSpentAttacking = 0;
		    	  BlockState pulledState = lever.pull(state, level, pos);
	    		  level.setBlockAndUpdate(pos, pulledState);
	    		  float f = pulledState.getValue(LeverBlock.POWERED) ? 0.6F : 0.5F;
	    		  level.playSound((Player)null, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.3F, f);
	    		  level.gameEvent((Player)null, pulledState.getValue(LeverBlock.POWERED) ? GameEvent.BLOCK_ACTIVATE : GameEvent.BLOCK_DEACTIVATE, pos);
	    		  return;
	    	  }
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
	         if (!chunkaccess.getBlockState(pPos).canEntityDestroy(pLevel, pPos, this.disruptor)) return false;
	         boolean flag = chunkaccess.getBlockState(pPos).hasProperty(DoorBlock.OPEN) ? true : false;
	         if(chunkaccess.getBlockState(pPos).hasProperty(ButtonBlock.POWERED) || chunkaccess.getBlockState(pPos).hasProperty(LeverBlock.POWERED)) {
	        	 flag = true;
	         }
	         if(flag) {
	        	 pos = pPos;
	        	 return true;
	         }else {return false;}
	      }
	   }
	}
