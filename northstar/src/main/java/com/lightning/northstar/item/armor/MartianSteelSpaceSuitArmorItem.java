package com.lightning.northstar.item.armor;

import java.util.Set;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import com.lightning.northstar.client.renderer.armor.MartianSteelSpaceSuitModelRenderer;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.example.registry.ItemRegistry;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

public class MartianSteelSpaceSuitArmorItem extends ArmorItem implements GeoItem{
	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
	public static final EquipmentSlot SLOT = EquipmentSlot.CHEST;
	
	public MartianSteelSpaceSuitArmorItem(ArmorMaterial materialIn, Type slot, Properties builder) {
		super(materialIn, slot, builder);
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			private GeoArmorRenderer<?> renderer;

			@Override
			public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
				if (this.renderer == null)
					this.renderer = new MartianSteelSpaceSuitModelRenderer();

				// This prepares our GeoArmorRenderer for the current render frame.
				// These parameters may be null however, so we don't do anything further with them
				this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);

				return this.renderer;
			}
		});
	}

	// Let's add our animation controller
	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(this, 20, state -> {
			// Apply our generic idle animation.
			// Whether it plays or not is decided down below.
			state.resetCurrentAnimation();

			// Let's gather some data from the state to use below
			// This is the entity that is currently wearing/holding the item
			Entity entity = state.getData(DataTickets.ENTITY);

			// We'll just have ArmorStands always animate, so we can return here
			if (entity instanceof ArmorStand)
				return PlayState.CONTINUE;

			// For this example, we only want the animation to play if the entity is wearing all pieces of the armor
			// Let's collect the armor pieces the entity is currently wearing
			Set<Item> wornArmor = new ObjectOpenHashSet<>();

			for (ItemStack stack : entity.getArmorSlots()) {
				// We can stop immediately if any of the slots are empty
				if (stack.isEmpty())
					return PlayState.STOP;

				wornArmor.add(stack.getItem());
			}

			// Check each of the pieces match our set
			boolean isFullSet = wornArmor.containsAll(ObjectArrayList.of(
					ItemRegistry.GECKO_ARMOR_BOOTS.get(),
					ItemRegistry.GECKO_ARMOR_LEGGINGS.get(),
					ItemRegistry.GECKO_ARMOR_CHESTPLATE.get(),
					ItemRegistry.GECKO_ARMOR_HELMET.get()));

			// Play the animation if the full set is being worn, otherwise stop
			return isFullSet ? PlayState.CONTINUE : PlayState.STOP;
		}));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
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
  
	public static int getRemainingAir(ItemStack stack) {
		CompoundTag orCreateTag = stack.getOrCreateTag();
		return orCreateTag.getInt("Air");
	}
	
	
	@Nullable
	public static MartianSteelSpaceSuitArmorItem getWornBy(Entity entity) {
		if (!(entity instanceof LivingEntity livingEntity)) {
			return null;
		}
		if (!(livingEntity.getItemBySlot(SLOT).getItem() instanceof MartianSteelSpaceSuitArmorItem item)) {
			return null;
		}
		return item;
	}
	@Override
	public boolean canBeDepleted() {
		return true;
	}

	@Override
	public boolean isEnchantable(ItemStack p_77616_1_) {
		return true;
	}

//	@Override
//	public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) {
//		if (!allowedIn(tab))
//			return;

//		ItemStack stack = new ItemStack(this);
//		CompoundTag nbt = new CompoundTag();
//		nbt.putInt("Oxygen", OxygenStuff.maximumOxy);
//		stack.setTag(nbt);
//		if(stack.is(NorthstarItemTags.OXYGEN_SOURCES.tag)) {
//			ListTag lore = new ListTag();
//			lore.add(StringTag.valueOf(Component.Serializer.toJson(Component.literal( "Oxygen: " + OxygenStuff.maximumOxy + "mb").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY).withItalic(false))).toString())); 
//			stack.getOrCreateTagElement("display").put("Lore", lore);
//		}
//		items.add(stack);
//	}
	
	
}