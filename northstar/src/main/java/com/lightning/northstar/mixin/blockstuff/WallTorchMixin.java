package com.lightning.northstar.mixin.blockstuff;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.lightning.northstar.block.NorthstarTechBlocks;
import com.lightning.northstar.world.OxygenStuff;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(WallTorchBlock.class)
public class WallTorchMixin {
	
 //   @Inject(method = "getStateForPlacement", at = @At("HEAD"), cancellable = true)
	public void getStateForPlacement(BlockPlaceContext pContext, CallbackInfoReturnable<BlockState> info) {
    	if (pContext.getItemInHand().getItem() == Items.TORCH) 
    	{        		
            System.out.println(OxygenStuff.hasOxygen(pContext.getClickedPos(),pContext.getLevel().dimension()));
        	
        	if(!OxygenStuff.hasOxygen(pContext.getClickedPos(),pContext.getLevel().dimension())) {
        		pContext.getLevel().playSound(null, pContext.getClickedPos(), SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 1, 0);
        		info.setReturnValue(NorthstarTechBlocks.EXTINGUISHED_TORCH.get().defaultBlockState());}
    	}
    }
    
    @Inject(method = "updateShape", at = @At("TAIL"), cancellable = true)
	public void updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, 
	LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos, CallbackInfoReturnable<BlockState> info) {
    	try {
    		if(pState.is(Blocks.WALL_TORCH)) {
	    	if(!OxygenStuff.hasOxygen(pCurrentPos,((Level)pLevel).dimension()) ) {
	    		if(!pState.canSurvive(pLevel, pCurrentPos)) {
	    			info.setReturnValue(Blocks.AIR.defaultBlockState());
	    			return;
	    		}
	    		pLevel.playSound(null, pCurrentPos, SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 1, 0);
	    		info.setReturnValue(NorthstarTechBlocks.EXTINGUISHED_TORCH_WALL.get().defaultBlockState().setValue(WallTorchBlock.FACING, pState.getValue(WallTorchBlock.FACING)));
	    	}
    		}
		} catch (Exception e) {
			//oops
		}
		   
	}

}
