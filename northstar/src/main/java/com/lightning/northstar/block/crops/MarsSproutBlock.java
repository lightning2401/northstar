package com.lightning.northstar.block.crops;

import com.lightning.northstar.block.NorthstarBlocks;
import com.lightning.northstar.item.NorthstarItems;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class MarsSproutBlock extends MartianFlowerBlock {

	public MarsSproutBlock(Properties pProperties) {
		super(pProperties);
	}
	
	@Override
	public Item getSeedItem() {
		return NorthstarItems.MARS_SPROUT_SEEDS.get();
	}
	
	@Override
	public boolean isValidBonemealTarget(BlockGetter pLevel, BlockPos pPos, BlockState pState, boolean pIsClient) {
		return true;
	}
	
	@SuppressWarnings("static-access")
	@Override
	public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
		if(state.is(this)) {
			int age = state.getValue(this.AGE);
			switch (age) {
			case 0: return 0;
			case 1: return 4;
			case 2: return 7;
			}
		}
		return 7;
	}
	
	@Override
	public void performBonemeal(ServerLevel pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {

		if(this.isMaxAge(pState) && pLevel.getBlockState(pPos.above()).isAir()) {
			pLevel.setBlock(pPos, NorthstarBlocks.MARS_SPROUT_BIG.get().defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER), 2);
			pLevel.setBlock(pPos.above(), NorthstarBlocks.MARS_SPROUT_BIG.get().defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER), 2);
		}
		super.performBonemeal(pLevel, pRandom, pPos, pState);
	}
	

}
