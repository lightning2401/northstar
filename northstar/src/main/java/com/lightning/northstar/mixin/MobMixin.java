package com.lightning.northstar.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.lightning.northstar.item.NorthstarEnchantments;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

@Mixin(Mob.class)
public class MobMixin {

    @Inject(method = "doHurtTarget", at = @At("HEAD"), cancellable = true)
	private void doHurtTarget(Entity pEntity, CallbackInfoReturnable<Boolean> info) {
		int i = EnchantmentHelper.getEnchantmentLevel(NorthstarEnchantments.FROSTBITE.get(), (Mob)(Object) this);
		if (i > 0) {
     	   System.out.println("frezzing");
     	   System.out.println(i * 20);
			pEntity.setTicksFrozen((i * 80) + 150);
		}

	}

}
