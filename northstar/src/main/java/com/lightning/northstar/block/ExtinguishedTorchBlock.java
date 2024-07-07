package com.lightning.northstar.block;

import com.lightning.northstar.world.OxygenStuff;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ExtinguishedTorchBlock extends Block{
	protected static final int AABB_STANDING_OFFSET = 2;
	protected static final VoxelShape AABB = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 10.0D, 10.0D);

	public ExtinguishedTorchBlock(BlockBehaviour.Properties pProperties) {
	super(pProperties);
	}
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
	return AABB;
	}
	
	
	@SuppressWarnings("deprecation")
	public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
	return pFacing == Direction.DOWN && !this.canSurvive(pState, pLevel, pCurrentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(pState, 	pFacing,pFacingState,      pLevel, pCurrentPos, pFacingPos);
	}

	public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
	return canSupportCenter(pLevel, pPos.below(), Direction.UP);
	}
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
		boolean fireflag = false;
		if ((pPlayer.getItemInHand(pHand).getItem() == Items.FLINT_AND_STEEL || pPlayer.getItemInHand(pHand).getItem() == Items.FIRE_CHARGE) && OxygenStuff.hasOxygen(pPos,((Level)pLevel).dimension())) {
			fireflag = true;}
		if (pPlayer.getAbilities().mayBuild && fireflag) {
			pLevel.setBlock(pPos, Blocks.TORCH.defaultBlockState(), 64);
			pLevel.playSound(pPlayer, pPos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, pLevel.getRandom().nextFloat() * 0.4F + 0.8F);
			return InteractionResult.sidedSuccess(pLevel.isClientSide);
		} else {
			return InteractionResult.PASS;
		}
	}
	
}
