package com.lightning.northstar.mixin.blockstuff;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.lightning.northstar.block.ExtinguishedTorchWallBlock;
import com.lightning.northstar.block.NorthstarTechBlocks;
import com.lightning.northstar.world.OxygenStuff;

import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(StandingAndWallBlockItem.class)
public class TorchItemMixin {
	
		@Inject(method = "getPlacementState", at = @At("HEAD"), cancellable = true)
		public void getPlacementState(BlockPlaceContext pContext, CallbackInfoReturnable<BlockState> info) {
	    	if (pContext.getItemInHand().getItem() == Items.TORCH) 
	    	{        	   
	    		if(!OxygenStuff.hasOxygen(pContext.getClickedPos(),pContext.getLevel().dimension())){
		        	BlockState state = NorthstarTechBlocks.EXTINGUISHED_TORCH_WALL.getDefaultState();
		        	for(Direction dirs : pContext.getNearestLookingDirections()) {
		        		if(dirs != Direction.DOWN && dirs != Direction.UP) {
		        			if(state.setValue(ExtinguishedTorchWallBlock.FACING, dirs).canSurvive(pContext.getLevel(), pContext.getClickedPos())) {
		        				pContext.getLevel().playSound(null, pContext.getClickedPos(), SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 1, 0);
		        				info.setReturnValue(NorthstarTechBlocks.EXTINGUISHED_TORCH_WALL.get().defaultBlockState().setValue(ExtinguishedTorchWallBlock.FACING, dirs));
		        				return;
		        			}
		        		}
		        	}
	    			if (NorthstarTechBlocks.EXTINGUISHED_TORCH.get().defaultBlockState().canSurvive(pContext.getLevel(), pContext.getClickedPos())) {
		        		pContext.getLevel().playSound(null, pContext.getClickedPos(), SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 1, 0);
		        		info.setReturnValue(NorthstarTechBlocks.EXTINGUISHED_TORCH.get().defaultBlockState());
		        		return;
		        	}
	    		}
	    	}
		}

}
