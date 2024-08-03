package com.lightning.northstar.mixin;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.lightning.northstar.world.TemperatureStuff;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;

@Mixin(BucketItem.class)
public abstract class BucketItemMixin extends Item{	
    @Shadow
    @Final
	private Fluid content;
	public BucketItemMixin(Properties pProperties) {
		super(pProperties);
	}
	@SuppressWarnings("deprecation")
//	 @Inject(method = "emptyContents(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/BlockHitResult;Lnet/minecraft/world/item/ItemStack;)Z",
//	at = @At(value = "HEAD", target = "Lnet/minecraft/world/item/BucketItem;emptyContents(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/BlockHitResult;)Z"),
//	cancellable = true)
	protected void emptyContentsReal(@Nullable Player pPlayer, Level pLevel, BlockPos pPos, @Nullable BlockHitResult blockHitResult, CallbackInfoReturnable<Boolean> info) {
		BlockState blockstate = pLevel.getBlockState(pPos);
		if (!(this.content instanceof FlowingFluid)) {
			info.setReturnValue(false);
		} else if (pLevel.dimensionType().ultraWarm() && this.content.is(FluidTags.WATER) && TemperatureStuff.getTemp(pPos, pLevel) < 212) {
			if (!pLevel.setBlock(pPos, this.content.defaultFluidState().createLegacyBlock(), 11) && !blockstate.getFluidState().isSource()) {
				info.setReturnValue(false);
			} else {
				this.playEmptySound(pPlayer, pLevel, pPos, this.content);
				info.setReturnValue(true);
			}
		}
	}
    @SuppressWarnings("deprecation")
	@Inject(method = "emptyContents(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/BlockHitResult;Lnet/minecraft/world/item/ItemStack;)Z",
    at = @At(value = "HEAD", target = "Lnet/minecraft/world/item/BucketItem;emptyContents(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/BlockHitResult;)Z"),
    cancellable = true)
    private void emptyContentsReal(@Nullable Player pPlayer, Level pLevel, BlockPos pPos, @Nullable BlockHitResult blockHitResult, ItemStack container, CallbackInfoReturnable<Boolean> info) {
//		System.out.println("YOooo buckets are real");
    	BucketItem item = (BucketItem) (Object) this;
 //   	System.out.println(item.getFluid());
		int temp = TemperatureStuff.getTemp(pPos, pLevel);
//		System.out.println(temp);
		BlockState blockstate = pLevel.getBlockState(pPos);
//		System.out.println(item.getFluid());
		if(item.getFluid() == null)
			return;
		int boilingpoint = TemperatureStuff.getBoilingPoint(item.getFluid().defaultFluidState());
		int freezingpoint = TemperatureStuff.getFreezingPoint(item.getFluid().defaultFluidState());
		Block block = pLevel.getBlockState(pPos).getBlock();
		if (!(item.getFluid() instanceof FlowingFluid)) {
			return;
		} else if (pLevel.dimensionType().ultraWarm() && temp < boilingpoint && temp > freezingpoint) {
			if(pLevel.getBlockState(pPos).is(Blocks.AIR) || pLevel.getBlockState(pPos).canBeReplaced(content)) {
				if (!pLevel.setBlock(pPos, item.getFluid().defaultFluidState().createLegacyBlock(), 11) && !blockstate.getFluidState().isSource()) {
					return;
				} else {
					this.playEmptySound(pPlayer, pLevel, pPos, item.getFluid());
					info.setReturnValue(true);
				}
			}else if(block instanceof LiquidBlockContainer && ((LiquidBlockContainer)block).canPlaceLiquid(pLevel,pPos,blockstate,content)) {
	            ((LiquidBlockContainer)block).placeLiquid(pLevel, pPos, blockstate, ((FlowingFluid)this.content).getSource(false));
	            this.playEmptySound(pPlayer, pLevel, pPos, item.getFluid());
				info.setReturnValue(true);
			}
		} else if (temp < freezingpoint && item.getFluid().is(FluidTags.WATER) && pLevel.getBlockState(pPos).is(Blocks.AIR)) {
			if (!pLevel.setBlock(pPos, Blocks.ICE.defaultBlockState(), 11)) {
				return;
			} else {
				this.playEmptySound(pPlayer, pLevel, pPos, item.getFluid());
				info.setReturnValue(true);
			}
		}
		else if(temp > boilingpoint) {
            int i = pPos.getX();
            int j = pPos.getY();
            int k = pPos.getZ();
            pLevel.playSound(pPlayer, pPos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (pLevel.random.nextFloat() - pLevel.random.nextFloat()) * 0.8F);

            for(int l = 0; l < 8; ++l) {
               pLevel.addParticle(ParticleTypes.LARGE_SMOKE, (double)i + Math.random(), (double)j + Math.random(), (double)k + Math.random(), 0.0D, 0.0D, 0.0D);
            }
            info.setReturnValue(true);

		}
    }
    
	
	@SuppressWarnings("deprecation")
	protected void playEmptySound(@Nullable Player pPlayer, LevelAccessor pLevel, BlockPos pPos, Fluid content) {
		SoundEvent soundevent = content.getFluidType().getSound(pPlayer, pLevel, pPos, net.minecraftforge.common.SoundActions.BUCKET_EMPTY);
		if(soundevent == null) soundevent = content.is(FluidTags.LAVA) ? SoundEvents.BUCKET_EMPTY_LAVA : SoundEvents.BUCKET_EMPTY;
		pLevel.playSound(pPlayer, pPos, soundevent, SoundSource.BLOCKS, 1.0F, 1.0F);
		pLevel.gameEvent(pPlayer, GameEvent.FLUID_PLACE, pPos);
	}
}
