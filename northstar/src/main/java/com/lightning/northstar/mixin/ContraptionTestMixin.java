package com.lightning.northstar.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;

@Mixin(CarriageContraptionEntity.class)
public class ContraptionTestMixin {
	
    @Inject(method = "tick", at = @At("TAIL"))
	public void test$tick(CallbackInfo info) {
    	CarriageContraptionEntity entity = (CarriageContraptionEntity) (Object) this;
    	if(entity.level.isClientSide)
    	if(entity.level.getGameTime() % 40 == 0) {
//    		System.out.println(entity.level.dimension());
    	}
    	
    }

}
