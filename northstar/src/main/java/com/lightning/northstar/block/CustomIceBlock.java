package com.lightning.northstar.block;

import javax.annotation.Nullable;

import com.lightning.northstar.world.TemperatureStuff;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.PushReaction;

public class CustomIceBlock extends HalfTransparentBlock {
	public Fluid fluid;
	
	public CustomIceBlock(BlockBehaviour.Properties pProperties) {
		super(pProperties);
	}
	@SuppressWarnings("deprecation")
	public void playerDestroy(Level pLevel, Player pPlayer, BlockPos pPos, BlockState pState, @Nullable BlockEntity pTe, ItemStack pStack) {
		super.playerDestroy(pLevel, pPlayer, pPos, pState, pTe, pStack);
		if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, pStack) == 0) {
			if (pLevel.dimensionType().ultraWarm()) {
				pLevel.removeBlock(pPos, false);
		            return;
		    }
		if(TemperatureStuff.getFreezingPoint(fluid.defaultFluidState()) > TemperatureStuff.getTemp(pPos, pLevel)){
		   	if (pLevel.getBlockState(pPos.below()).blocksMotion() || !pLevel.getBlockState(pPos.below()).getBlock().getFluidState(pState).isEmpty()) {
		   		pLevel.setBlockAndUpdate(pPos, fluid.getFluidType().getBlockForFluidState(pLevel, pPos, fluid.defaultFluidState()));
		       }
		    }
		}
	}

		   /**
		    * Performs a random tick on a block.
		    */
	public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
		if(TemperatureStuff.getBoilingPoint(fluid.defaultFluidState()) > TemperatureStuff.getTemp(pPos, pLevel)){
			this.evaporate(pState, pLevel, pPos);
		}
		if(TemperatureStuff.getFreezingPoint(fluid.defaultFluidState()) > TemperatureStuff.getTemp(pPos, pLevel)){
			this.melt(pState, pLevel, pPos);
	    }
	}

	protected void melt(BlockState pState, Level pLevel, BlockPos pPos) {
	    pLevel.setBlockAndUpdate(pPos, fluid.getFluidType().getBlockForFluidState(pLevel, pPos, fluid.defaultFluidState()));
	    pLevel.neighborChanged(pPos, fluid.getFluidType().getBlockForFluidState(pLevel, pPos, fluid.defaultFluidState()).getBlock(), pPos);
	}
	protected void evaporate(BlockState pState, Level pLevel, BlockPos pPos) {
	    pLevel.setBlockAndUpdate(pPos, Blocks.AIR.defaultBlockState());
	    pLevel.neighborChanged(pPos, Blocks.AIR, pPos);
        int i = pPos.getX();
        int j = pPos.getY();
        int k = pPos.getZ();
        pLevel.playSound(null, pPos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (pLevel.random.nextFloat() - pLevel.random.nextFloat()) * 0.8F);
        for(int l = 0; l < 8; ++l) {
           pLevel.addParticle(ParticleTypes.LARGE_SMOKE, (double)i + Math.random(), (double)j + Math.random(), (double)k + Math.random(), 0.0D, 0.0D, 0.0D);
        }
	}

	/**
	* @deprecated call via {@link
	* net.minecraft.world.level.block.state.BlockBehaviour.BlockStateBase#getPistonPushReaction} whenever possible.
	* Implementing/overriding is fine.
	*/
	public PushReaction getPistonPushReaction(BlockState pState) {
		return PushReaction.NORMAL;
	}
}
