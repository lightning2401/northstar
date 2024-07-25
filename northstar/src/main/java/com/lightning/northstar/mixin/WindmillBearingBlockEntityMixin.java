package com.lightning.northstar.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.lightning.northstar.world.dimension.NorthstarPlanets;
import com.simibubi.create.content.contraptions.bearing.BearingContraption;
import com.simibubi.create.content.contraptions.bearing.MechanicalBearingBlockEntity;
import com.simibubi.create.content.contraptions.bearing.WindmillBearingBlockEntity;
import com.simibubi.create.content.contraptions.bearing.WindmillBearingBlockEntity.RotationDirection;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollOptionBehaviour;
import com.simibubi.create.infrastructure.config.AllConfigs;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(value = WindmillBearingBlockEntity.class, remap = false)
public abstract class WindmillBearingBlockEntityMixin extends MechanicalBearingBlockEntity {

	public WindmillBearingBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}
	@Shadow
	protected float lastGeneratedSpeed;
	@Shadow
	protected ScrollOptionBehaviour<RotationDirection> movementDirection;

    @Inject(method = "getGeneratedSpeed", at = @At("HEAD"), cancellable = true)
	public void getGeneratedSpeed2(CallbackInfoReturnable<Float> info) {
    	if(level != null) {
	    	float finalwindValue = 0;
	    	if (!running)
	    		finalwindValue = 0;
	    	else if (movedContraption == null)
				finalwindValue = lastGeneratedSpeed;
			else
			{int sails = ((BearingContraption) movedContraption.getContraption()).getSailBlocks()
				/ AllConfigs.server().kinetics.windmillSailsPerRPM.get();
			finalwindValue = Mth.clamp(sails, 1, 16) * getAngleSpeedDirection2();}
			
			
			info.setReturnValue(finalwindValue * NorthstarPlanets.getWindMultiplier(level));
			info.cancel();
    	}
	}
    
    
    
	protected float getAngleSpeedDirection2() {
		RotationDirection rotationDirection = RotationDirection.values()[movementDirection.getValue()];
		return (rotationDirection == RotationDirection.CLOCKWISE ? 1 : -1);
	}
	
}
