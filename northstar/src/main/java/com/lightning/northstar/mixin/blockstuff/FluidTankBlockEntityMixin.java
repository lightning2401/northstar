package com.lightning.northstar.mixin.blockstuff;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(value = FluidTankBlockEntity.class, remap = false)
public class FluidTankBlockEntityMixin extends SmartBlockEntity {

	public FluidTankBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}
	@Shadow
	protected BlockPos controller;

//	@Inject(method = "getControllerBE", at = @At("HEAD"), cancellable = true)
	public void getControllerBECustom(CallbackInfoReturnable<FluidTankBlockEntity> info) {
			FluidTankBlockEntity self = (FluidTankBlockEntity)(Object) this;
			if(!self.hasLevel())
				info.cancel();
				info.setReturnValue(null);

	}

	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
		registerAwardables(behaviours, AllAdvancements.STEAM_ENGINE_MAXED, AllAdvancements.PIPE_ORGAN);
	}

}
