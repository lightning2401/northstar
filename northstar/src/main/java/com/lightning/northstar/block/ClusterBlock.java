package com.lightning.northstar.block;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AmethystBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ClusterBlock extends AmethystBlock implements SimpleWaterloggedBlock {
	   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	   public static final DirectionProperty FACING = BlockStateProperties.FACING;
	   protected final VoxelShape northAabb;
	   protected final VoxelShape southAabb;
	   protected final VoxelShape eastAabb;
	   protected final VoxelShape westAabb;
	   protected final VoxelShape upAabb;
	   protected final VoxelShape downAabb;

	   public ClusterBlock(int pSize, int pOffset, BlockBehaviour.Properties pProperties) {
	      super(pProperties);
	      this.registerDefaultState(this.defaultBlockState().setValue(WATERLOGGED, Boolean.valueOf(false)).setValue(FACING, Direction.UP));
	      this.upAabb = Block.box((double)pOffset, 0.0D, (double)pOffset, (double)(16 - pOffset), (double)pSize, (double)(16 - pOffset));
	      this.downAabb = Block.box((double)pOffset, (double)(16 - pSize), (double)pOffset, (double)(16 - pOffset), 16.0D, (double)(16 - pOffset));
	      this.northAabb = Block.box((double)pOffset, (double)pOffset, (double)(16 - pSize), (double)(16 - pOffset), (double)(16 - pOffset), 16.0D);
	      this.southAabb = Block.box((double)pOffset, (double)pOffset, 0.0D, (double)(16 - pOffset), (double)(16 - pOffset), (double)pSize);
	      this.eastAabb = Block.box(0.0D, (double)pOffset, (double)pOffset, (double)pSize, (double)(16 - pOffset), (double)(16 - pOffset));
	      this.westAabb = Block.box((double)(16 - pSize), (double)pOffset, (double)pOffset, 16.0D, (double)(16 - pOffset), (double)(16 - pOffset));
	   }

	   public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
	      Direction direction = pState.getValue(FACING);
	      switch (direction) {
	         case NORTH:
	            return this.northAabb;
	         case SOUTH:
	            return this.southAabb;
	         case EAST:
	            return this.eastAabb;
	         case WEST:
	            return this.westAabb;
	         case DOWN:
	            return this.downAabb;
	         case UP:
	         default:
	            return this.upAabb;
	      }
	   }

	   public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
	      Direction direction = pState.getValue(FACING);
	      BlockPos blockpos = pPos.relative(direction.getOpposite());
	      return pLevel.getBlockState(blockpos).isFaceSturdy(pLevel, blockpos, direction);
	   }

	   public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
	      if (pState.getValue(WATERLOGGED)) {
	         pLevel.scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
	      }

	      return pDirection == pState.getValue(FACING).getOpposite() && !pState.canSurvive(pLevel, pCurrentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(pState, pDirection, pNeighborState, pLevel, pCurrentPos, pNeighborPos);
	   }

	   @Nullable
	   public BlockState getStateForPlacement(BlockPlaceContext pContext) {
	      LevelAccessor levelaccessor = pContext.getLevel();
	      BlockPos blockpos = pContext.getClickedPos();
	      return this.defaultBlockState().setValue(WATERLOGGED, Boolean.valueOf(levelaccessor.getFluidState(blockpos).getType() == Fluids.WATER)).setValue(FACING, pContext.getClickedFace());
	   }

	   public BlockState rotate(BlockState pState, Rotation pRotation) {
	      return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
	   }

	   public BlockState mirror(BlockState pState, Mirror pMirror) {
	      return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
	   }

	   public FluidState getFluidState(BlockState pState) {
	      return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
	   }

	   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
	      pBuilder.add(WATERLOGGED, FACING);
	   }

	   public PushReaction getPistonPushReaction(BlockState pState) {
	      return PushReaction.DESTROY;
	   }
	}