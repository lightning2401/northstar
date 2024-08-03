package com.lightning.northstar.block.crops;

import java.util.Optional;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class TallFungusBlock extends TallFlowerBlock {
	protected static final BooleanProperty IS_ON_CEILING = BooleanProperty.create("is_on_ceiling");
	   private final ResourceKey<ConfiguredFeature<?, ?>> feature;
	   private final ResourceKey<ConfiguredFeature<?, ?>> ceilingFeature;

	public TallFungusBlock(Properties pProperties, ResourceKey<ConfiguredFeature<?, ?>> feature2,ResourceKey<ConfiguredFeature<?, ?>> ceilingfeature) {
		super(pProperties);
		feature = feature2;
		ceilingFeature = ceilingfeature;
	    this.registerDefaultState(this.defaultBlockState().setValue(IS_ON_CEILING, false).setValue(HALF, DoubleBlockHalf.LOWER));
	}
	
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(IS_ON_CEILING);
		pBuilder.add(HALF);
	}
	
	@Override
	public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
		if(!pState.getValue(IS_ON_CEILING)) {
			if (pState.getValue(HALF) != DoubleBlockHalf.UPPER) {
				return pLevel.getBlockState(pPos.below()).isSolidRender(pLevel, pPos.below());
			} else {
				BlockState blockstate = pLevel.getBlockState(pPos.below());
				if (pState.getBlock() != this) return this.mayPlaceOn(pState, pLevel, pPos);
				return blockstate.is(this) && blockstate.getValue(HALF) == DoubleBlockHalf.LOWER;
			}
		}else {
			if (pState.getValue(HALF) != DoubleBlockHalf.UPPER) {
				return pLevel.getBlockState(pPos.above()).isSolidRender(pLevel, pPos.above());
			} else {
				BlockState blockstate = pLevel.getBlockState(pPos.above());
				if (pState.getBlock() != this) return this.mayPlaceOn(pState, pLevel, pPos);
				return blockstate.is(this) && blockstate.getValue(HALF) == DoubleBlockHalf.LOWER;
			}
		}
	}
	
	public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
		if(!pState.getValue(IS_ON_CEILING)) {
			DoubleBlockHalf doubleblockhalf = pState.getValue(HALF);
			if (pFacing.getAxis() != Direction.Axis.Y || doubleblockhalf == DoubleBlockHalf.LOWER != (pFacing == Direction.UP) || pFacingState.is(this) && pFacingState.getValue(HALF) != doubleblockhalf) {
				return doubleblockhalf == DoubleBlockHalf.LOWER && pFacing == Direction.DOWN && !pState.canSurvive(pLevel, pCurrentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
			} else {
				return Blocks.AIR.defaultBlockState();
			}
		}else {
			DoubleBlockHalf doubleblockhalf = pState.getValue(HALF);
			if(doubleblockhalf == DoubleBlockHalf.LOWER && !pLevel.getBlockState(pCurrentPos.below()).is(this)) {
				return Blocks.AIR.defaultBlockState();
			}
			if (pFacing.getAxis() != Direction.Axis.Y || doubleblockhalf == DoubleBlockHalf.LOWER != (pFacing == Direction.UP) || pFacingState.is(this) && pFacingState.getValue(HALF) != doubleblockhalf) {
				return doubleblockhalf == DoubleBlockHalf.LOWER && pFacing == Direction.UP && !pState.canSurvive(pLevel, pCurrentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
			} else {
				return Blocks.AIR.defaultBlockState();
			}
		}
	}
	
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		System.out.println(pContext.getClickedFace());
		boolean ceiling_flag = pContext.getClickedFace() == Direction.DOWN;  
		BlockPos blockpos = pContext.getClickedPos();
		Level level = pContext.getLevel();
		if(ceiling_flag)
		return blockpos.getY() < level.getMaxBuildHeight() && level.getBlockState(blockpos.below()).canBeReplaced(pContext) ? this.defaultBlockState().setValue(IS_ON_CEILING, ceiling_flag) : null;
		else
		return blockpos.getY() < level.getMaxBuildHeight() && level.getBlockState(blockpos.above()).canBeReplaced(pContext) ? this.defaultBlockState().setValue(IS_ON_CEILING, ceiling_flag).setValue(HALF, DoubleBlockHalf.LOWER) : null;
			
	}
	
	public static void placeAt(LevelAccessor pLevel, BlockState pState, BlockPos pPos, int pFlags) {
		BlockPos blockpos = pPos.above();
		if(pLevel.getBlockState(blockpos).isSolidRender(pLevel, blockpos)) {
			pLevel.setBlock(pPos, copyWaterloggedFrom(pLevel, pPos, pState.setValue(HALF, DoubleBlockHalf.LOWER).setValue(IS_ON_CEILING, true)), pFlags);
			pLevel.setBlock(pPos.below(), copyWaterloggedFrom(pLevel, pPos.below(), pState.setValue(HALF, DoubleBlockHalf.UPPER).setValue(IS_ON_CEILING, true)), pFlags);
		}else {
			pLevel.setBlock(pPos, copyWaterloggedFrom(pLevel, pPos, pState.setValue(HALF, DoubleBlockHalf.LOWER)), pFlags);
			pLevel.setBlock(blockpos, copyWaterloggedFrom(pLevel, blockpos, pState.setValue(HALF, DoubleBlockHalf.UPPER)), pFlags);
		}
	}

	@Override
	protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
		if(pState.getValue(IS_ON_CEILING)) {
			return pState.isSolidRender(pLevel, pPos.above());
		}else {
			return pState.isSolidRender(pLevel, pPos);
		}
	}
	@Override
	public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
		if(pState.getValue(IS_ON_CEILING)) {
			BlockPos blockpos = pPos.below();
			pLevel.setBlock(blockpos, copyWaterloggedFrom(pLevel, blockpos, this.defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER).setValue(IS_ON_CEILING, true)), 3);
		}
		else {BlockPos blockpos = pPos.above();
		pLevel.setBlock(blockpos, copyWaterloggedFrom(pLevel, blockpos, this.defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER)), 3);}
	}
	
	@Override
	public boolean isValidBonemealTarget(LevelReader pLevel, BlockPos pPos, BlockState pState, boolean pIsClient) {
		return true;
	}
	@Override
	public boolean isBonemealSuccess(Level pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
		return true;
	}
	@Override
	public void performBonemeal(ServerLevel pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
		growMushroom(pLevel, pPos, pState, pRandom);
	}
	//bruh
	
	public boolean growMushroom(ServerLevel pLevel, BlockPos pPos, BlockState pState, RandomSource pRandom) {
		BlockPos placePos = pPos;
		if(pState.getValue(HALF) == DoubleBlockHalf.UPPER && pState.getValue(IS_ON_CEILING)) 
		{placePos = new BlockPos(pPos.getX(), pPos.getY() + 1, pPos.getZ());}
		if(pState.getValue(HALF) == DoubleBlockHalf.UPPER && !pState.getValue(IS_ON_CEILING)) 
		{placePos = new BlockPos(pPos.getX(), pPos.getY() - 1, pPos.getZ());}
		pLevel.setBlock(pPos, Blocks.AIR.defaultBlockState(), 0);
		pLevel.setBlock(placePos, Blocks.AIR.defaultBlockState(), 0);

		if(!pState.getValue(IS_ON_CEILING)) {
			Optional<? extends Holder<ConfiguredFeature<?, ?>>> optional = pLevel.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE).getHolder(this.feature);
	        var event = net.minecraftforge.event.ForgeEventFactory.blockGrowFeature(pLevel, pRandom, pPos, optional.get());
			if (event.getResult().equals(net.minecraftforge.eventbus.api.Event.Result.DENY)) return false;
			pLevel.removeBlock(pPos, false);
			if (event.getFeature().value().place(pLevel, pLevel.getChunkSource().getGenerator(), pRandom, pPos)) {
				return true;
			} else {
				pLevel.setBlock(pPos, pState, 3);
				return false;
			}  
		}else {
			Optional<? extends Holder<ConfiguredFeature<?, ?>>> optional = pLevel.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE).getHolder(this.ceilingFeature);
	        var event = net.minecraftforge.event.ForgeEventFactory.blockGrowFeature(pLevel, pRandom, pPos, optional.get());
			if (event.getResult().equals(net.minecraftforge.eventbus.api.Event.Result.DENY)) return false;
			pLevel.removeBlock(pPos, false);
			if (event.getFeature().value().place(pLevel, pLevel.getChunkSource().getGenerator(), pRandom, pPos)) {
				return true;
			} else {
				pLevel.setBlock(pPos, pState, 3);
				return false;
			}  
		}
		
		
		
	}
}
