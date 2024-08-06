package com.lightning.northstar.mixin.blockstuff;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.simibubi.create.content.fluids.tank.FluidTankBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(value = FluidTankBlock.class, remap = false)
public class FluidTankBlockMixin {

	@Inject(method = "getLightEmission", at = @At("HEAD"), cancellable = true)
	public void getLightEmissionNew(BlockState state, BlockGetter world, BlockPos pos, CallbackInfoReturnable<Integer> info) {
		// idk what else to do, besides fluid tank emission is very inconsistent anyway
		// also this doesn't break anything in 1.19??????
		// sorry :(
		// hopefully I can figure this out down the line
		info.cancel();
		info.setReturnValue(0);
	}
}
