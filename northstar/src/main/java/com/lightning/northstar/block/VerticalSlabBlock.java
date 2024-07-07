package com.lightning.northstar.block;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VerticalSlabBlock extends DirectionalBlock implements SimpleWaterloggedBlock{
	   public static final EnumProperty<VerticalSlabTypes> TYPE = EnumProperty.create("type", VerticalSlabTypes.class);
	   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	   protected static final VoxelShape NORTH_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 8.0D);
	   protected static final VoxelShape SOUTH_SHAPE = Block.box(0.0D, 0.0D, 8.0D, 16.0D, 16.0D, 16.0D);
	   protected static final VoxelShape EAST_SHAPE = Block.box(8.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
	   protected static final VoxelShape WEST_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 8.0D, 16.0D, 16.0D);


	   public VerticalSlabBlock(BlockBehaviour.Properties pProperties) {
		      super(pProperties);
		      this.registerDefaultState(this.defaultBlockState().setValue(TYPE, VerticalSlabTypes.NORTH).setValue(WATERLOGGED, Boolean.valueOf(false)));
		   }
	   
	   public boolean useShapeForLightOcclusion(BlockState pState) {
		      return pState.getValue(TYPE) != VerticalSlabTypes.DOUBLE;
		   }
	   
	   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		      pBuilder.add(TYPE, WATERLOGGED);
		   }
	   
	   public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		      VerticalSlabTypes vertslabtype = pState.getValue(TYPE);
		      switch (vertslabtype) {
		         case DOUBLE:
		            return Shapes.block();
		         case EAST:
		            return EAST_SHAPE;
		         case WEST:
			            return WEST_SHAPE;
		         case SOUTH:
			            return SOUTH_SHAPE;
		         default:
		            return NORTH_SHAPE;
		      }
		   }
	   
	   @Nullable
	   public BlockState getStateForPlacement(BlockPlaceContext pContext) {
	      BlockPos blockpos = pContext.getClickedPos();
	      BlockState blockstate = pContext.getLevel().getBlockState(blockpos);
	      if (blockstate.is(this)) {
	         return blockstate.setValue(TYPE, VerticalSlabTypes.DOUBLE).setValue(WATERLOGGED, Boolean.valueOf(false));
	      } else {
	         FluidState fluidstate = pContext.getLevel().getFluidState(blockpos);
	         Direction direction = pContext.getHorizontalDirection();
	         VerticalSlabTypes bob = VerticalSlabTypes.NORTH;
	         switch (direction) {
	         	case NORTH:
	        	 bob = VerticalSlabTypes.NORTH;
	        	 break;
	         	case SOUTH:
	        	 bob = VerticalSlabTypes.SOUTH;
	        	 break;
	         	case EAST:
	        	 bob = VerticalSlabTypes.EAST;
	        	 break;
	         	case WEST:
	        	 bob = VerticalSlabTypes.WEST;
	        	 break;
			default:
				bob = VerticalSlabTypes.NORTH;
				break;}
	         return defaultBlockState()
	         .setValue(TYPE, bob)
	         .setValue(BlockStateProperties.WATERLOGGED, fluidstate.getType() == Fluids.WATER);
	      }
	   }
	   
	   public boolean canBeReplaced(BlockState pState, BlockPlaceContext pUseContext) {
		      ItemStack itemstack = pUseContext.getItemInHand();
		      VerticalSlabTypes slabtype = pState.getValue(TYPE);
		      if (slabtype != VerticalSlabTypes.DOUBLE && itemstack.is(this.asItem())) {
		         if (pUseContext.replacingClickedOnBlock()) {
		            boolean flag = pUseContext.getClickLocation().y - (double)pUseContext.getClickedPos().getY() > 0.5D;
		            Direction direction = pUseContext.getClickedFace();
		            if (slabtype == VerticalSlabTypes.NORTH) {
		               return direction == Direction.EAST || flag && direction.getAxis().isHorizontal();
		            } else {
		               return direction == Direction.WEST || !flag && direction.getAxis().isHorizontal();
		            }
		         } else {
		            return true;
		         }
		      } else {
		         return false;
		      }
		   }

		 @SuppressWarnings("deprecation")
	   public FluidState getFluidState(BlockState pState) {
		      return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
		   }

		   public boolean placeLiquid(LevelAccessor pLevel, BlockPos pPos, BlockState pState, FluidState pFluidState) {
		      return pState.getValue(TYPE) != VerticalSlabTypes.DOUBLE ? SimpleWaterloggedBlock.super.placeLiquid(pLevel, pPos, pState, pFluidState) : false;
		   }

		   public boolean canPlaceLiquid(BlockGetter pLevel, BlockPos pPos, BlockState pState, Fluid pFluid) {
		      return pState.getValue(TYPE) != VerticalSlabTypes.DOUBLE ? SimpleWaterloggedBlock.super.canPlaceLiquid(pLevel, pPos, pState, pFluid) : false;
		   }
		   
		   
		   
		   
		 @SuppressWarnings("deprecation")
		public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
			      if (pState.getValue(WATERLOGGED)) {
			         pLevel.scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
			      }

			      return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
			   }
		 

		 @Override 
		 public BlockState rotate(BlockState blockState, net.minecraft.world.level.block.Rotation rotation) 
		 {return blockState.setValue(TYPE, VerticalSlabTypes.fromDir(rotation.rotate(VerticalSlabTypes.toDir(blockState.getValue(TYPE))))); }
		 @SuppressWarnings("deprecation")
		@Override
		 public BlockState mirror(BlockState blockState, net.minecraft.world.level.block.Mirror mirror) 
		 { return blockState.rotate(mirror.getRotation(VerticalSlabTypes.toDir(blockState.getValue(TYPE)))); }

		 public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
			      switch (pType) {
			   case LAND:
			            return false;
			   case WATER:
			            return pLevel.getFluidState(pPos).is(FluidTags.WATER);
			   case AIR:
			            return false;
			   default:
			      return false;
		}
	}
}
