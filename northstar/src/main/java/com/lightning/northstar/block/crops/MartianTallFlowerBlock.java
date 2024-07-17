package com.lightning.northstar.block.crops;

import com.lightning.northstar.block.NorthstarBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.minecraft.world.level.block.state.BlockState;

public class MartianTallFlowerBlock extends TallFlowerBlock {

	public MartianTallFlowerBlock(Properties pProperties) {
		super(pProperties);
	}

	@Override
	protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
		return pState.is(BlockTags.DIRT) || pState.is(Blocks.FARMLAND) || pState.is(NorthstarBlocks.MARS_SOIL.get()) || pState.is(NorthstarBlocks.MARS_FARMLAND.get());
	}
	
	public boolean isValidBonemealTarget(BlockGetter pLevel, BlockPos pPos, BlockState pState, boolean pIsClient) {
		return false;
	}
	@Override
	public void performBonemeal(ServerLevel pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {}
	//bruh
}
