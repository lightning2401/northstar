package com.lightning.northstar.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.MultifaceSpreader;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FrostBlock extends MultifaceBlock {
	public static final BooleanProperty UP = PipeBlock.UP;
	public static final BooleanProperty DOWN = PipeBlock.DOWN;
	public static final BooleanProperty NORTH = PipeBlock.NORTH;
	public static final BooleanProperty EAST = PipeBlock.EAST;
	public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
	public static final BooleanProperty WEST = PipeBlock.WEST;
	public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION.entrySet().stream().collect(Util.toMap());
	protected static final float AABB_OFFSET = 1.0F;
	private static final VoxelShape UP_AABB = Block.box(0.0D, 15.0D, 0.0D, 16.0D, 16.0D, 16.0D);
	private static final VoxelShape DOWN_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
	private static final VoxelShape WEST_AABB = Block.box(0.0D, 0.0D, 0.0D, 1.0D, 16.0D, 16.0D);
	private static final VoxelShape EAST_AABB = Block.box(15.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
	private static final VoxelShape NORTH_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 1.0D);
	private static final VoxelShape SOUTH_AABB = Block.box(0.0D, 0.0D, 15.0D, 16.0D, 16.0D, 16.0D);
	public final MultifaceSpreader spreader = new MultifaceSpreader(this);
	
	private final Map<BlockState, VoxelShape> shapesCache;
	
	
	public FrostBlock(Properties pProperties) {
		super(pProperties);
		this.registerDefaultState(this.stateDefinition.any().setValue(UP, Boolean.valueOf(false)).setValue(DOWN, Boolean.valueOf(false)).setValue(NORTH, Boolean.valueOf(false))
				.setValue(EAST, Boolean.valueOf(false)).setValue(SOUTH, Boolean.valueOf(false)).setValue(WEST, Boolean.valueOf(false)));
		this.shapesCache = ImmutableMap.copyOf(this.stateDefinition.getPossibleStates().stream().collect(Collectors.toMap(Function.identity(), FrostBlock::calculateShape)));
	}

	private static VoxelShape calculateShape(BlockState p_57906_) {
	      VoxelShape voxelshape = Shapes.empty();
	      if (p_57906_.getValue(UP)) {
	         voxelshape = UP_AABB;
	      }

	      if (p_57906_.getValue(NORTH)) {
	         voxelshape = Shapes.or(voxelshape, NORTH_AABB);
	      }

	      if (p_57906_.getValue(SOUTH)) {
	         voxelshape = Shapes.or(voxelshape, SOUTH_AABB);
	      }

	      if (p_57906_.getValue(EAST)) {
	         voxelshape = Shapes.or(voxelshape, EAST_AABB);
	      }

	      if (p_57906_.getValue(WEST)) {
	         voxelshape = Shapes.or(voxelshape, WEST_AABB);
	      }
	      if (p_57906_.getValue(DOWN)) {
	    	  voxelshape = Shapes.or(voxelshape, DOWN_AABB);
	      }
	      
	      return voxelshape.isEmpty() ? Shapes.block() : voxelshape;
	}
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return this.shapesCache.get(pState);
	}
	
	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rando) {
		
	}
	
	
	public static boolean isAcceptableNeighbour(BlockGetter pBlockReader, BlockPos pNeighborPos, Direction pAttachedFace) {
		return MultifaceBlock.canAttachTo(pBlockReader, pAttachedFace, pNeighborPos, pBlockReader.getBlockState(pNeighborPos));
	}
	
	public static BlockState giveStateValues(LevelAccessor level, BlockPos pPos, RandomSource rando) {
		List<Direction> list = new ArrayList<Direction>();
		for(Direction dir : Direction.values()) {
			BlockState block = level.getBlockState(pPos.relative(dir));
					
			if (block.isAir()) {
				list.add(dir);
			}
		}
		BlockState state = rando.nextInt(4) == 0 ? 
				NorthstarBlocks.FROST.get().defaultBlockState()
				.setValue(FrostBlock.UP, list.contains(Direction.UP)).setValue(FrostBlock.DOWN, list.contains(Direction.DOWN))
				.setValue(FrostBlock.NORTH, list.contains(Direction.NORTH)).setValue(FrostBlock.SOUTH, list.contains(Direction.SOUTH))
				.setValue(FrostBlock.EAST, list.contains(Direction.EAST)).setValue(FrostBlock.WEST, list.contains(Direction.WEST))
				: 
				NorthstarBlocks.FROST.get().defaultBlockState()
				.setValue(FrostBlock.UP, list.contains(Direction.UP)).setValue(FrostBlock.DOWN, list.contains(Direction.DOWN))
				.setValue(FrostBlock.NORTH, list.contains(Direction.NORTH)).setValue(FrostBlock.SOUTH, list.contains(Direction.SOUTH))
				.setValue(FrostBlock.EAST, list.contains(Direction.EAST)).setValue(FrostBlock.WEST, list.contains(Direction.WEST));
		return state;
	}

	@SuppressWarnings("static-access")
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		BlockState blockstate = pContext.getLevel().getBlockState(pContext.getClickedPos());
		boolean flag = blockstate.is(this);
		BlockState blockstate1 = flag ? blockstate : this.defaultBlockState();

		for(Direction direction : pContext.getNearestLookingDirections()) {
			BooleanProperty booleanproperty = getPropertyForFace(direction);
			boolean flag1 = flag && blockstate.getValue(booleanproperty);
			if (!flag1 && this.canSupportAtFace(pContext.getLevel(), pContext.getClickedPos(), direction)) {
				return blockstate1.setValue(booleanproperty, Boolean.valueOf(true));
			}
			
		}
		return flag ? blockstate1 : null;
	}
	
	public static BlockState getStateForGeneration(WorldGenLevel level, BlockPos pos, RandomSource rando) {
		BlockState newstate = NorthstarBlocks.FROST.get().defaultBlockState();
		if(rando.nextInt(4) == 0) {newstate = NorthstarBlocks.FROST.get().defaultBlockState();}
		int i = 0;
		
		for(Direction direction : Direction.values()) {
			BooleanProperty booleanproperty = getPropertyForFace(direction);
	   	     if (newstate.getValue(booleanproperty)) {
	   	    	 boolean flag = canSupportAtFace(level, pos, direction);
	   	    	 if (!flag) {
	   	    		 flag = (newstate.is(NorthstarBlocks.FROST.get()) || newstate.is(NorthstarBlocks.FROST.get())) && newstate.getValue(booleanproperty);
	   	    	 }

	   	    	newstate = newstate.setValue(booleanproperty, Boolean.valueOf(flag));
	   	    	if(!flag)
	   	    		i++;
	   	     }
		}		
		if(i == 0)
		{return null;}
		else
		{return newstate;}
	}
	
	@SuppressWarnings("static-access")
	private BlockState getUpdatedState(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
		BlockPos blockpos = pPos.above();
		BlockState blockstate = null;

		for(Direction direction : Direction.values()) {
			BooleanProperty booleanproperty = getPropertyForFace(direction);
	   	     if (pState.getValue(booleanproperty)) {
	   	    	 boolean flag = this.canSupportAtFace(pLevel, pPos, direction);
	   	    	 if (!flag) {
	   	    		 if (blockstate == null) {
	   	    			 blockstate = pLevel.getBlockState(blockpos);
	   	    		 }

	   	    		 flag = blockstate.is(this) && blockstate.getValue(booleanproperty);
	   	    	 }

	   	    	 pState = pState.setValue(booleanproperty, Boolean.valueOf(flag));
	   	     }
		}

		return pState;
	}

	   /**
	    * Update the provided state given the provided neighbor direction and neighbor state, returning a new state.
	    * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately
	    * returns its solidified counterpart.
	    * Note that this method should ideally consider only the specific direction passed in.
	    */
	public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
		BlockState blockstate = this.getUpdatedState(pState, pLevel, pCurrentPos);
		return !this.hasFaces(blockstate) ? Blocks.AIR.defaultBlockState() : blockstate;
	}
	   
	private boolean hasFaces(BlockState pState) {
		return this.countFaces(pState) > 0;
	}

	private int countFaces(BlockState pState) {
		int i = 0;
		for(BooleanProperty booleanproperty : PROPERTY_BY_DIRECTION.values()) {
			if (pState.getValue(booleanproperty)) {
				++i;
			}
		}
		return i;
	}	   
	
	private static boolean canSupportAtFace(BlockGetter pLevel, BlockPos pPos, Direction pDirection) {
		BlockPos blockpos = pPos.relative(pDirection);
		if (isAcceptableNeighbour(pLevel, blockpos, pDirection)) {
			return true;
		} else if (pDirection.getAxis() == Direction.Axis.Y) {
			return false;
		} else {
			BooleanProperty booleanproperty = PROPERTY_BY_DIRECTION.get(pDirection);
			BlockState blockstate = pLevel.getBlockState(pPos.above());
			return (blockstate.is(NorthstarBlocks.MARS_ROOTS.get()) || blockstate.is(NorthstarBlocks.GLOWING_MARS_ROOTS.get())) && blockstate.getValue(booleanproperty);
		}
		
	}
	
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(UP, DOWN, NORTH, EAST, SOUTH, WEST);
	}
	
	public static BooleanProperty getPropertyForFace(Direction pFace) {
		return PROPERTY_BY_DIRECTION.get(pFace);
	}

	@Override
	public MultifaceSpreader getSpreader() {
	      return this.spreader;
	}
}
