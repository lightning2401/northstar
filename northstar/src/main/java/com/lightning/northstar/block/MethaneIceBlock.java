package com.lightning.northstar.block;

import javax.annotation.Nullable;

import com.lightning.northstar.NorthstarTags;
import com.lightning.northstar.fluids.NorthstarFluids;
import com.lightning.northstar.particle.ColdAirParticleData;
import com.lightning.northstar.world.TemperatureStuff;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.material.PushReaction;

public class MethaneIceBlock extends HalfTransparentBlock {	
	public MethaneIceBlock(BlockBehaviour.Properties pProperties) {
		super(pProperties);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void playerDestroy(Level pLevel, Player pPlayer, BlockPos pPos, BlockState pState, @Nullable BlockEntity pTe, ItemStack pStack) {
		super.playerDestroy(pLevel, pPlayer, pPos, pState, pTe, pStack);
		if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, pStack) == 0) {
			if(TemperatureStuff.getBoilingPoint(NorthstarFluids.METHANE.getSource().defaultFluidState()) < TemperatureStuff.getTemp(pPos, pLevel)){
				this.evaporate(pState, pLevel, pPos);
			}
			else if(TemperatureStuff.getFreezingPoint(NorthstarFluids.METHANE.getSource().defaultFluidState()) < TemperatureStuff.getTemp(pPos, pLevel)){
				this.melt(pState, pLevel, pPos);
		    }
		}
	}
	
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
	      int x = pos.getX();
	      int y = pos.getY();
	      int z = pos.getZ();
	      if(random.nextInt(5) != 0 && TemperatureStuff.getTemp(pos, level) < TemperatureStuff.getFreezingPoint(NorthstarFluids.METHANE.getSource().defaultFluidState())) {
	    	  return;
	      }
	      for(Direction dir : Direction.values()) {
	    	  if(level.getBlockState(pos.relative(dir)).is(NorthstarTags.NorthstarBlockTags.AIR_PASSES_THROUGH.tag) && random.nextInt(20) == 0) {
	    	      double d0 = (double)x + (dir.getStepX() / 2) + random.nextDouble();
	    	      double d1 = (double)y + 0.7D;
	    	      double d2 = (double)z + (dir.getStepZ() / 2) + random.nextDouble();
	    	      level.addParticle(new ColdAirParticleData(), d0, d1, d2, 0.0D, 0.0D, 0.0D);
	    	  }
	      }

	}

		   /**
		    * Performs a random tick on a block.
		    */
	public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
		if(TemperatureStuff.getBoilingPoint(NorthstarFluids.METHANE.getSource().defaultFluidState()) < TemperatureStuff.getTemp(pPos, pLevel)){
			this.evaporate(pState, pLevel, pPos);
		}
		else if(TemperatureStuff.getFreezingPoint(NorthstarFluids.METHANE.getSource().defaultFluidState()) < TemperatureStuff.getTemp(pPos, pLevel)){
			this.melt(pState, pLevel, pPos);
	    }
	}

	protected void melt(BlockState pState, Level pLevel, BlockPos pPos) {
	    pLevel.setBlockAndUpdate(pPos, NorthstarFluids.METHANE.getSource().getFluidType().getBlockForFluidState(pLevel, pPos, NorthstarFluids.METHANE.getSource().defaultFluidState()));
	    pLevel.neighborChanged(pPos, NorthstarFluids.METHANE.getSource().getFluidType().getBlockForFluidState(pLevel, pPos, NorthstarFluids.METHANE.getSource().defaultFluidState()).getBlock(), pPos);
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

}
