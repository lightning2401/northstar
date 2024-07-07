package com.lightning.northstar.block.entity;

import com.lightning.northstar.block.VenusExhaustBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class VenusExhaustBlockEntity extends BlockEntity {

	public VenusExhaustBlockEntity(BlockPos pPos, BlockState pBlockState) {
		super(NorthstarBlockEntityTypes.VENUS_EXHAUST.get(), pPos, pBlockState);
	}

	public static void particleTick(Level pLevel, BlockPos pPos, BlockState pState, VenusExhaustBlockEntity pBlockEntity) {
		RandomSource randomsource = pLevel.random;
		if (randomsource.nextFloat() < 0.11F) {
			for(int i = 0; i < randomsource.nextInt(2) + 2; ++i) {
				VenusExhaustBlock.makeParticles(pLevel, pPos);
			}
		}
	}
}
