package com.lightning.northstar.block.crops;

import javax.annotation.Nullable;

import com.lightning.northstar.block.NorthstarBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VenusVinesBlock extends Block implements BonemealableBlock {
	protected static final VoxelShape SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D);
	protected static final VoxelShape TIP_SHAPE = Block.box(4.0D, 4.0D, 4.0D, 12.0D, 16.0D, 12.0D);
	public static final BooleanProperty IS_TIP = BooleanProperty.create("is_tip");
	public static final IntegerProperty AGE = BlockStateProperties.AGE_25;
	public static final int MAX_AGE = 25;

	public VenusVinesBlock(BlockBehaviour.Properties prop) {
		super(prop);
		this.registerDefaultState(this.stateDefinition.any().setValue(IS_TIP, true).setValue(AGE, 1));
	}
	
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		if(pState.getValue(IS_TIP))
			return TIP_SHAPE;
		return SHAPE;
	}
	
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rando) {
		if (state.getValue(AGE) < 25 && net.minecraftforge.common.ForgeHooks.onCropsGrowPre(level, pos.relative(Direction.DOWN), level.getBlockState(pos.relative(Direction.DOWN)),rando.nextDouble() < 0.5f)) {
			BlockPos blockpos = pos.relative(Direction.DOWN);
			if (level.getBlockState(blockpos).isAir()) {
				level.setBlockAndUpdate(blockpos, this.getGrowIntoState(state, level.random));
				net.minecraftforge.common.ForgeHooks.onCropsGrowPost(level, blockpos, level.getBlockState(blockpos));
			}
		}
	}
	
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(IS_TIP);builder.add(AGE);
    }
	
	protected BlockState getGrowIntoState(BlockState pState, RandomSource pRandom) {
		if(pRandom.nextInt(4) == 0 && pState.is(NorthstarBlocks.VENUS_VINES.get())) {
			return NorthstarBlocks.GLOWING_VENUS_VINES.get().defaultBlockState();
		}if(pState.is(NorthstarBlocks.GLOWING_VENUS_VINES.get()) && pRandom.nextInt(2) != 0) {
			return NorthstarBlocks.VENUS_VINES.get().defaultBlockState();
		}
		return pState.cycle(AGE);
	}

	
	
	@Override
	public boolean isValidBonemealTarget(BlockGetter pLevel, BlockPos pPos, BlockState pState, boolean pIsClient) {
		return pState.getValue(IS_TIP);
	}

	@Override
	public boolean isBonemealSuccess(Level pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
		return true;
	}

	@Override
	public void performBonemeal(ServerLevel pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
		BlockPos blockpos = pPos.relative(Direction.DOWN);
		int i = Math.min(pState.getValue(AGE) + 1, 25);
		pLevel.setBlockAndUpdate(blockpos, pState.setValue(AGE, Integer.valueOf(i)));
		blockpos = blockpos.relative(Direction.DOWN);
		i = Math.min(i + 1, 25);
	}
	public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
		if(pLevel.getBlockState(pCurrentPos.above()).isAir()) {
			return Blocks.AIR.defaultBlockState();
			
		}else if(!(pLevel.getBlockState(pCurrentPos.below()).is(NorthstarBlocks.VENUS_VINES.get()) || pLevel.getBlockState(pCurrentPos.below()).is(NorthstarBlocks.GLOWING_VENUS_VINES.get()))){
			return this.defaultBlockState().setValue(IS_TIP, true);
		}else
			return this.defaultBlockState().setValue(IS_TIP, false);
	}
	
	public boolean propagatesSkylightDown(BlockState pState, BlockGetter pReader, BlockPos pPos) {
	      return true;
	}
	
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		if(pContext.getLevel().getBlockState(pContext.getClickedPos().above()).isAir())
			return Blocks.AIR.defaultBlockState();
		BlockState belowstate = pContext.getLevel().getBlockState(pContext.getClickedPos().below());
		
		if(!(belowstate.is(NorthstarBlocks.VENUS_VINES.get()) || belowstate.is(NorthstarBlocks.GLOWING_VENUS_VINES.get()))) {
			return this.defaultBlockState().setValue(IS_TIP, true);
		}else
			return this.defaultBlockState().setValue(IS_TIP, false);
	}

}