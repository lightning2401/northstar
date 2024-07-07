package com.lightning.northstar.block;

import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.lightning.northstar.world.OxygenStuff;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ExtinguishedTorchWallBlock extends Block{
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	   protected static final float AABB_OFFSET = 2.5F;
	   private static final Map<Direction, VoxelShape> AABBS = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, Block.box(5.5D, 3.0D, 11.0D, 10.5D, 13.0D, 16.0D),
			   Direction.SOUTH, Block.box(5.5D, 3.0D, 0.0D, 10.5D, 13.0D, 5.0D), Direction.WEST, Block.box(11.0D, 3.0D, 5.5D, 16.0D, 13.0D, 10.5D),
			   Direction.EAST, Block.box(0.0D, 3.0D, 5.5D, 5.0D, 13.0D, 10.5D)));

	public ExtinguishedTorchWallBlock(BlockBehaviour.Properties pProperties) {
	super(pProperties);
	this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return getShape(pState);
	}

	public static VoxelShape getShape(BlockState pState) {
		return AABBS.get(pState.getValue(FACING));
	}

	
	
	public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
		return pFacing.getOpposite() == pState.getValue(FACING) && !pState.canSurvive(pLevel, pCurrentPos) ? Blocks.AIR.defaultBlockState() : pState;
	}
	
	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		BlockState blockstate = this.defaultBlockState();
		LevelReader levelreader = pContext.getLevel();
		BlockPos blockpos = pContext.getClickedPos();
		Direction[] adirection = pContext.getNearestLookingDirections();
		      
		for(Direction direction : adirection) {
			if (direction.getAxis().isHorizontal()) {
				Direction direction1 = direction.getOpposite();
				blockstate = blockstate.setValue(FACING, direction1);
				if (blockstate.canSurvive(levelreader, blockpos)) {
	            	return blockstate;
	            }
			}
		}
		
		return null;
	}

	public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
		Direction direction = pState.getValue(FACING);
		BlockPos blockpos = pPos.relative(direction.getOpposite());
		BlockState blockstate = pLevel.getBlockState(blockpos);
		return blockstate.isFaceSturdy(pLevel, blockpos, direction);
	}
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
		boolean fireflag = false;
		if ((pPlayer.getItemInHand(pHand).getItem() == Items.FLINT_AND_STEEL || pPlayer.getItemInHand(pHand).getItem() == Items.FIRE_CHARGE) && OxygenStuff.hasOxygen(pPos,pLevel.dimension())) {
			fireflag = true;}
		if (pPlayer.getAbilities().mayBuild && fireflag) {
			pLevel.setBlock(pPos, Blocks.WALL_TORCH.defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, pState.getValue(FACING)), 64);
			pLevel.playSound(pPlayer, pPos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, pLevel.getRandom().nextFloat() * 0.4F + 0.8F);
			return InteractionResult.sidedSuccess(pLevel.isClientSide);
		} else {
			return InteractionResult.PASS;
		}
	}
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(FACING);
	}
	
}
