package com.lightning.northstar.block;

import com.lightning.northstar.world.TemperatureStuff;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LayerLightEngine;
import net.minecraftforge.common.PlantType;

public class MartianGrassBlock extends Block implements BonemealableBlock  {

	public MartianGrassBlock(Properties pProperties) {
		super(pProperties);
	}
	
	private static boolean canBeGrass(BlockState pState, LevelReader pLevelReader, BlockPos pPos) {
		BlockPos blockpos = pPos.above();
		BlockState blockstate = pLevelReader.getBlockState(blockpos);
		if (blockstate.getFluidState().getAmount() == 8) {
			return false;
		} else {
			int i = LayerLightEngine.getLightBlockInto(pLevelReader, pState, pPos, blockstate, blockpos, Direction.UP, blockstate.getLightBlock(pLevelReader, blockpos));
			return i < pLevelReader.getMaxLightLevel() && TemperatureStuff.getTemp(pPos, (Level) pLevelReader) > 0;
		}
	}
	
	@Override
	public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, net.minecraft.core.Direction facing, net.minecraftforge.common.IPlantable plantable) {
		net.minecraftforge.common.PlantType plantType = plantable.getPlantType(world, pos.relative(facing));
		return plantType != PlantType.CROP && plantType != PlantType.WATER;
	}
	
	private static boolean canPropagate(BlockState pState, LevelReader pLevel, BlockPos pPos) {
		BlockPos blockpos = pPos.above();
		return canBeGrass(pState, pLevel, pPos) && !pLevel.getFluidState(blockpos).is(FluidTags.WATER);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
		if (!canBeGrass(pState, pLevel, pPos)) {
			if (!pLevel.isAreaLoaded(pPos, 1)) return; 
			if(pRandom.nextInt(5) == 0)
			{pLevel.setBlockAndUpdate(pPos, NorthstarBlocks.MARS_SOIL.get().defaultBlockState());}
		} else {
			if (!pLevel.isAreaLoaded(pPos, 3)) return;
			if (pLevel.getMaxLocalRawBrightness(pPos.above()) >= 9) {
				BlockState blockstate = this.defaultBlockState();
	        	 
				for(int i = 0; i < 4; ++i) {
					BlockPos blockpos = pPos.offset(pRandom.nextInt(3) - 1, pRandom.nextInt(5) - 3, pRandom.nextInt(3) - 1);
					if (pLevel.getBlockState(blockpos).is(NorthstarBlocks.MARS_SOIL.get()) && canPropagate(blockstate, pLevel, blockpos)) {
						pLevel.setBlockAndUpdate(blockpos, blockstate);
					}
				}
			}
		}
	}

	@Override
	public boolean isValidBonemealTarget(BlockGetter pLevel, BlockPos pPos, BlockState pState, boolean pIsClient) {
		return pLevel.getBlockState(pPos.above()).isAir();
	}

	@Override
	public boolean isBonemealSuccess(Level pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
		return true;
	}

	@Override
	public void performBonemeal(ServerLevel level, RandomSource pRandom, BlockPos pPos, BlockState pState) {
		BlockState blockstate = NorthstarBlocks.MARTIAN_TALL_GRASS.get().defaultBlockState();
		if(pRandom.nextBoolean() && level.getBlockState(pPos.above()).isAir()) {
			level.setBlock(pPos.above(), blockstate, 2);
		}
		for(int x = -2; x <= 2; x++) {
			for(int y = -2; y <= 2; y++) {
				for(int z = -2; z <= 2; z++) {
					BlockPos pos = new BlockPos(pPos.getX() + x, pPos.getY() + y, pPos.getZ() + z);
					BlockState state = level.getBlockState(pos.below());
					if(level.getBlockState(pos).isAir() && (state.is(NorthstarBlocks.MARTIAN_GRASS.get()) || state.is(NorthstarBlocks.MARS_SOIL.get())) && pRandom.nextInt(2) == 0) {
						level.setBlock(pos, blockstate, 2);
					}
				}
			}
		}
		
	}
}
