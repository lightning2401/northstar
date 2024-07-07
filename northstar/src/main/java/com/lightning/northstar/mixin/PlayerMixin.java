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
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

@Mixin(Player.class)
public class PlayerMixin {

    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
	private void attack(Entity pTarget, CallbackInfo info) {
        float f = (float) ((Player)(Object)this).getAttributeValue(Attributes.ATTACK_DAMAGE);
        float f1;
        if (pTarget instanceof LivingEntity) {
            f1 = EnchantmentHelper.getDamageBonus(((Player)(Object)this).getMainHandItem(), ((LivingEntity)pTarget).getMobType());
         } else {
            f1 = EnchantmentHelper.getDamageBonus(((Player)(Object)this).getMainHandItem(), MobType.UNDEFINED);
         }
    	float f2 = ((Player)(Object)this).getAttackStrengthScale(0.5F);
        f *= 0.2F + f2 * f2 * 0.8F;
        f1 *= f2;


        if (f > 0.0F || f1 > 0.0F) {
        	int j = EnchantmentHelper.getEnchantmentLevel(NorthstarEnchantments.FROSTBITE.get(), (Player)(Object) this);
            if (pTarget instanceof LivingEntity) {
               if (j > 0) {
            	   System.out.println("frezzing");
                  pTarget.setTicksFrozen((j * 80) + 150);;
                  frost(pTarget);
               }
            }           
        	
        }
	}
    
    @SuppressWarnings("resource")
	public void frost(Entity pEntityHit) {
    	if(((Object)this) instanceof LocalPlayer)
    	{Minecraft.getInstance().particleEngine.createTrackingEmitter(pEntityHit, new SnowflakeParticleData());}
    }
	
}
