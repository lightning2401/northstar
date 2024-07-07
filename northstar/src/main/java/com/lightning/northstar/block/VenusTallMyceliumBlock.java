package com.lightning.northstar.block;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VenusTallMyceliumBlock extends BushBlock {
	protected static final BooleanProperty IS_ON_CEILING = BooleanProperty.create("is_on_ceiling");
	protected static final VoxelShape CEILING_SHAPE = Block.box(2.0D, 3.0D, 2.0D, 14.0D, 16.0D, 14.0D);
	protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 13.0D, 14.0D);

	public VenusTallMyceliumBlock(Properties pProperties) {
		super(pProperties);
	    this.registerDefaultState(this.defaultBlockState().setValue(IS_ON_CEILING, false));
	}
	
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(IS_ON_CEILING);
	}
	
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		if(pState.getValue(IS_ON_CEILING)){
			return CEILING_SHAPE;
		}
		else return SHAPE;
	}
	
	public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
		if(pState.getValue(IS_ON_CEILING)) {
			BlockPos blockpos = pPos.above();
			BlockState blockstate = pLevel.getBlockState(blockpos);
			if (blockstate.is(BlockTags.MUSHROOM_GROW_BLOCK)) {
				return true;
			} else {
				return blockstate.canSustainPlant(pLevel, blockpos, net.minecraft.core.Direction.DOWN, this);
			}
		}else {
			BlockPos blockpos = pPos.below();
			BlockState blockstate = pLevel.getBlockState(blockpos);
			if (blockstate.is(BlockTags.MUSHROOM_GROW_BLOCK)) {
				return true;
			} else {
				return blockstate.canSustainPlant(pLevel, blockpos, net.minecraft.core.Direction.UP, this);
			}
		}
	}	
	protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
		return pState.isSolidRender(pLevel, pPos);
	}
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		System.out.println(pContext.getClickedFace());
		return this.defaultBlockState().setValue(IS_ON_CEILING, pContext.getClickedFace() == Direction.DOWN);  
	}
}
