package com.lightning.northstar.mixin;

import java.util.Optional;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.lightning.northstar.block.NorthstarTechBlocks;
import com.mojang.logging.LogUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CompassItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

@Mixin(CompassItem.class)
public class CompassMixin {
	private static final Logger LOGGER = LogUtils.getLogger();
	private static final String TAG_LODESTONE_POS = "LodestonePos";
	private static final String TAG_LODESTONE_DIMENSION = "LodestoneDimension";
	private static final String TAG_LODESTONE_TRACKED = "LodestoneTracked";

    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
	private void useOn(UseOnContext pContext, CallbackInfoReturnable<InteractionResult> info) {
		BlockPos blockpos = pContext.getClickedPos();
		Level level = pContext.getLevel();
		if (level.getBlockState(blockpos).is(NorthstarTechBlocks.ROCKET_STATION.get())) {
			level.playSound((Player)null, blockpos, SoundEvents.LODESTONE_COMPASS_LOCK, SoundSource.PLAYERS, 1.0F, 1.0F);
			Player player = pContext.getPlayer();
			ItemStack itemstack = pContext.getItemInHand();
			boolean flag = !player.getAbilities().instabuild && itemstack.getCount() == 1;
			if (flag) {
				addLodestoneTags(level.dimension(), blockpos, itemstack.getOrCreateTag());
			} else {
				ItemStack itemstack1 = new ItemStack(Items.COMPASS, 1);
				CompoundTag compoundtag = itemstack.hasTag() ? itemstack.getTag().copy() : new CompoundTag();
				itemstack1.setTag(compoundtag);
				if (!player.getAbilities().instabuild) {
					itemstack.shrink(1);
				}

				addLodestoneTags(level.dimension(), blockpos, compoundtag);
				if (!player.getInventory().add(itemstack1)) {
					player.drop(itemstack1, false);
				}
			}

			info.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide));
		}
	}

    @Inject(method = "inventoryTick", at = @At("HEAD"), cancellable = true)
    private void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pItemSlot, boolean pIsSelected, CallbackInfo info) {
		if (!pLevel.isClientSide) {
			if (isLodestoneCompass2(pStack)) {
				CompoundTag compoundtag = pStack.getOrCreateTag();
				if (compoundtag.contains("LodestoneTracked") && !compoundtag.getBoolean("LodestoneTracked")) {
					return;
				}

				Optional<ResourceKey<Level>> optional = getLodestoneDimension2(compoundtag);
				if (optional.isPresent() && optional.get() == pLevel.dimension() && compoundtag.contains("LodestonePos")) {
					BlockPos blockpos = NbtUtils.readBlockPos(compoundtag.getCompound("LodestonePos"));
					if (pLevel.isInWorldBounds(blockpos) && ((ServerLevel)pLevel).getBlockState(blockpos).is(NorthstarTechBlocks.ROCKET_STATION.get())) {
						info.cancel();
					}
				}
			}
		}
	}
	@Nullable
	private static GlobalPos getLodestonePosition2(CompoundTag p_220022_) {
		boolean flag = p_220022_.contains("LodestonePos");
		boolean flag1 = p_220022_.contains("LodestoneDimension");
		if (flag && flag1) {
			Optional<ResourceKey<Level>> optional = getLodestoneDimension2(p_220022_);
	         if (optional.isPresent()) {
	        	 BlockPos blockpos = NbtUtils.readBlockPos(p_220022_.getCompound("LodestonePos"));
	        	 return GlobalPos.of(optional.get(), blockpos);
	         }
		}
		return null;
	}
	private static Optional<ResourceKey<Level>> getLodestoneDimension2(CompoundTag pCompoundTag) {
		return Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, pCompoundTag.get("LodestoneDimension")).result();
	}
	
	private static boolean isLodestoneCompass2(ItemStack pStack) {
		CompoundTag compoundtag = pStack.getTag();
		return compoundtag != null && (compoundtag.contains("LodestoneDimension") || compoundtag.contains("LodestonePos"));
	}

	private void addLodestoneTags(ResourceKey<Level> pLodestoneDimension, BlockPos pLodestonePos, CompoundTag pCompoundTag) {
		pCompoundTag.put("LodestonePos", NbtUtils.writeBlockPos(pLodestonePos));
		Level.RESOURCE_KEY_CODEC.encodeStart(NbtOps.INSTANCE, pLodestoneDimension).resultOrPartial(LOGGER::error).ifPresent((p_40731_) -> {
			pCompoundTag.put("LodestoneDimension", p_40731_);
		});
		pCompoundTag.putBoolean("LodestoneTracked", true);
	}

}
