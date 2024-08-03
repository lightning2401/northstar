package com.lightning.northstar.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.lightning.northstar.item.NorthstarEnchantments;
import com.lightning.northstar.particle.SnowflakeParticleData;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

@Mixin(Player.class)
public class PlayerFrostEffectMixin {
	
    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
	private void attack(Entity pTarget, CallbackInfo info) {
    	int j = EnchantmentHelper.getEnchantmentLevel(NorthstarEnchantments.FROSTBITE.get(), (Player)(Object) this);
        if (pTarget instanceof LivingEntity) {
            if (j > 0) {
            	frost(pTarget);
            }
        }
	}
    
    @SuppressWarnings("resource")
	public void frost(Entity pEntityHit) {
    	if(((Object)this) instanceof LocalPlayer)
    	{Minecraft.getInstance().particleEngine.createTrackingEmitter(pEntityHit, new SnowflakeParticleData());}
    }
}
