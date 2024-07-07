package com.lightning.northstar.item.armor;

import javax.annotation.Nullable;

import com.lightning.northstar.item.NorthstarCreativeModeTab;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.item.GeoArmorItem;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class BrokenIronSpaceSuitArmorItem extends GeoArmorItem implements IAnimatable{
	private AnimationFactory factory = GeckoLibUtil.createFactory(this);
	public static final EquipmentSlot SLOT = EquipmentSlot.CHEST;
	
	public BrokenIronSpaceSuitArmorItem(ArmorMaterial materialIn, EquipmentSlot slot, Properties builder) {
		super(materialIn, slot, builder.tab(NorthstarCreativeModeTab.NORTHSTAR_TAB));
	}

	// Predicate runs every frame
    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

	// All you need to do here is add your animation controllers to the
	// AnimationData
	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController<BrokenIronSpaceSuitArmorItem>(this, "controller", 20, this::predicate));
	}
	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}
	
	public static boolean hasChestplateOn(Player player) {
        ItemStack chestplate = player.getInventory().getArmor(2);
        return !chestplate.isEmpty();
    }
    
    public static boolean hasCorrectChestplateOn(ArmorMaterial material, Player player) {
        for (ItemStack armorStack: player.getInventory().armor) {
            if(!(armorStack.getItem() instanceof ArmorItem)) {
                return false;
            }
        }
        
        ArmorItem chestplate = ((ArmorItem)player.getInventory().getArmor(2).getItem());
        return chestplate.getMaterial() == material;
    }
	@Override
	public boolean canBeDepleted() {
		return true;
	}

	@Override
	public boolean isEnchantable(ItemStack p_77616_1_) {
		return true;
	}
}