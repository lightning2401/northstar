package com.lightning.northstar.entity.goals;

import com.lightning.northstar.entity.MercuryRoachEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.phys.Vec3;

public class ScourChestGoal extends MoveToBlockGoal {
	   private final MercuryRoachEntity disruptor;
	   private BlockPos pos;
	   private int ticksSinceReachedGoal;
	   private int searchTime = 0;
	   private static final int WAIT_AFTER_BLOCK_FOUND = 20;

	   public ScourChestGoal(MercuryRoachEntity pRemoverMob, double pSpeedModifier, int pSearchRange) {
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
	      if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.disruptor.level(), this.disruptor)) {
	         return false;
	      } else if (disruptor.stealTimer > 0) {
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
	      Level level = this.disruptor.level();
	      BlockState state = level.getBlockState(pos);
	      
    	  if(searchTime > 0 && this.isReachedTarget()) {
    		  if(level.getBlockEntity(pos) != null) {
    		  ChestBlockEntity ent = (ChestBlockEntity) level.getBlockEntity(pos);
    		  
    		  if(level.getNearestPlayer(disruptor, 128) != null)
    		  {ent.startOpen(level.getNearestPlayer(disruptor, 128));}
    		  
    		  if(!ent.isEmpty()) {
    			  int randomint = level.random.nextInt(27);
	    		  ItemStack rando = ent.getItem(randomint);
	    		  ItemStack newstack = rando.copy();
	    		  newstack.setCount(1);	    		  
	    		  rando.setCount(rando.getCount() - 1);
	    		  ent.setItem(randomint, rando);
	    		  ItemEntity item = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, newstack);
	    		  item.setDeltaMovement(new Vec3(level.random.nextInt(-1, 1) * 0.2, level.random.nextInt(0, 1) * 0.2, level.random.nextInt(-1, 1) * 0.2));
	    		  level.addFreshEntity(item);
    		  	  }
    		  }    		  
    		  searchTime = Mth.clamp(searchTime, 0, searchTime - 1);
    	  }
    	  
	      if(this.disruptor.stealTimer != 0) {
	    	  this.stop();
	      }
	      if (this.isReachedTarget() && this.disruptor.stealTimer == 0) {
	    	  BlockState newstate = level.getBlockState(pos);
	    	  disruptor.stealTimer = 600;
	    	  ++this.ticksSinceReachedGoal;
	    	  if(newstate.getBlock() instanceof ChestBlock chest && searchTime == 0) 
	    	  {
	    		  searchTime = 60;
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
	         boolean flag = false;
	         if(chunkaccess.getBlockState(pPos).is(Blocks.CHEST) && !((ChestBlockEntity)pLevel.getBlockEntity(pPos)).isEmpty()) {
	        	 flag = true;
	         }
	         if(flag) {
	        	 pos = pPos;
	        	 return true;
	         }else {return false;}
	      }
	   }
	}
