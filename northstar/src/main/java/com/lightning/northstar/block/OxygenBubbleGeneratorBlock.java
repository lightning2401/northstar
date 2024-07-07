package com.lightning.northstar.block;

import javax.annotation.Nullable;

import com.lightning.northstar.block.entity.NorthstarBlockEntityTypes;
import com.lightning.northstar.block.entity.OxygenBubbleGeneratorBlockEntity;
import com.simibubi.create.foundation.utility.worldWrappers.WrappedWorld;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;

public class OxygenBubbleGeneratorBlock extends BaseEntityBlock{
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

	protected OxygenBubbleGeneratorBlock(Properties pProperties) {
		super(pProperties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);builder.add(POWERED);}
    
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
	      return new OxygenBubbleGeneratorBlockEntity(pPos, pState);
}
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, NorthstarBlockEntityTypes.OXYGEN_BUBBLE_GENERATOR.get(),
		OxygenBubbleGeneratorBlockEntity::tick);
	}
	

	public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
	      if (pStack.hasCustomHoverName()) {
	         BlockEntity blockentity = pLevel.getBlockEntity(pPos);
	         if (blockentity instanceof BeaconBlockEntity) {
	            ((BeaconBlockEntity)blockentity).setCustomName(pStack.getHoverName());
	         }
	      }
    }
	

	
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return defaultBlockState().setValue(FACING, Direction.NORTH);
	}
	
	
	public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
	      if (!pLevel.isClientSide) {
	         boolean flag = pState.getValue(POWERED);
	         if (flag != pLevel.hasNeighborSignal(pPos)) {
	            if (flag) {
	               pLevel.scheduleTick(pPos, this, 4);
	            } else {
	               pLevel.setBlock(pPos, pState.cycle(POWERED), 2);
	            }
	         }
	     }
    }
	
	public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
		if (pState.getValue(POWERED) && !pLevel.hasNeighborSignal(pPos)) {
			pLevel.setBlock(pPos, pState.cycle(POWERED), 2);
		}
	}
	
	
	protected void blockUpdate(BlockState state, LevelAccessor worldIn, BlockPos pos) {
		if (worldIn instanceof WrappedWorld)
			return;
	}
	
	
	public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
	     return false;
	}
}
