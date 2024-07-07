package com.lightning.northstar.block;

import javax.annotation.Nullable;

import com.lightning.northstar.particle.GlowstoneParticleData;
import com.lightning.northstar.world.OxygenStuff;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GlowstoneTorchBlock extends Block implements SimpleWaterloggedBlock{
	protected static final int AABB_STANDING_OFFSET = 2;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	protected static final VoxelShape AABB = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 10.0D, 10.0D);

	public GlowstoneTorchBlock(BlockBehaviour.Properties pProperties) {
		super(pProperties);
		this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, Boolean.valueOf(false)));
	}
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
	return AABB;
	}
	
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(WATERLOGGED);
	}
	
	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		BlockState blockstate = this.defaultBlockState();
		FluidState fluidstate = pContext.getLevel().getFluidState(pContext.getClickedPos());
		if(canSurvive(blockstate.setValue(WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER)), pContext.getLevel(), pContext.getClickedPos()))
		return blockstate.setValue(WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER));

		return null;
	}
	
	
	@SuppressWarnings("deprecation")
	public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
		if (OxygenStuff.hasOxygen(pCurrentPos, ((Level)pLevel).dimension())) {
			System.out.println("Burh");
		}
	return pFacing == Direction.DOWN && !this.canSurvive(pState, pLevel, pCurrentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(pState, 	pFacing,pFacingState,      pLevel, pCurrentPos, pFacingPos);
	}
	
	@SuppressWarnings("deprecation")
	public FluidState getFluidState(BlockState pState) {
		return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
	}

	public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
	return canSupportCenter(pLevel, pPos.below(), Direction.UP);
	}
	   public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
		      double d0 = (double)pPos.getX() + 0.5D;
		      double d1 = (double)pPos.getY() + 0.7D;
		      double d2 = (double)pPos.getZ() + 0.5D;
		      if (pRandom.nextInt(4) == 0)
		      pLevel.addParticle(new GlowstoneParticleData(), d0, d1, d2, 0.0D, 0.0D, 0.0D);
		   }
	
}
