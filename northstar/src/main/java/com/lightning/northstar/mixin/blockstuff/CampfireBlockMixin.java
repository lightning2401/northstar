package com.lightning.northstar.mixin.blockstuff;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.lightning.northstar.world.OxygenStuff;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

@Mixin(CampfireBlock.class)
public class CampfireBlockMixin {

	@Inject(method = "getStateForPlacement", at = @At("HEAD"), cancellable = true)
	public void getStateForPlacement(BlockPlaceContext pContext, CallbackInfoReturnable<BlockState> info) {
		try {
			ItemStack item = pContext.getItemInHand();
			BlockPos pos = pContext.getClickedPos();
			Level level = pContext.getLevel();
			boolean flag = level.getFluidState(pos).getType() == Fluids.WATER;
			if(item.is(Items.CAMPFIRE)) {
				if(!OxygenStuff.hasOxygen(pos, level.dimension())) {
					info.cancel();
					info.setReturnValue(Blocks.CAMPFIRE.defaultBlockState().setValue(CampfireBlock.WATERLOGGED, Boolean.valueOf(flag))
							.setValue(CampfireBlock.LIT, false).setValue(CampfireBlock.FACING, pContext.getHorizontalDirection()));
					
				}
			}
		} catch (Exception e) {
			//oops
		}
	}
}
