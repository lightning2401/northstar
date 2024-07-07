package com.lightning.northstar.block.tech.oxygen_concentrator;

import com.lightning.northstar.block.entity.NorthstarBlockEntityTypes;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class OxygenConcentratorBlock extends HorizontalKineticBlock implements IBE<OxygenConcentratorBlockEntity> {
	   protected static final VoxelShape AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D);

	public OxygenConcentratorBlock(Properties properties) {
		super(properties);
	}
	
//	@Override
//	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand,
//		BlockHitResult pHit) { 	       	
//		if (!pLevel.isClientSide()) {
//			BlockEntity entity = pLevel.getBlockEntity(pPos);
//			if(entity instanceof OxygenConcentratorBlockEntity) {
 //              NetworkHooks.openScreen(((ServerPlayer)pPlayer), (OxygenConcentratorBlockEntity)entity, pPos);
 //               
 //           } else {
 //               throw new IllegalStateException("Our Container provider is missing!");
 //           }
 //       }
 //       return InteractionResult.sidedSuccess(pLevel.isClientSide());
 //   }
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return IBE.super.newBlockEntity(pPos, pState);
	}

	@Override
	public Axis getRotationAxis(BlockState state) {
		return Axis.Y;
	}
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return AABB;
	}
	
	@Override
	public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
		return face == Direction.DOWN;
	}

	@Override
	public Class<OxygenConcentratorBlockEntity> getBlockEntityClass() {
		return OxygenConcentratorBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends OxygenConcentratorBlockEntity> getBlockEntityType() {
		return NorthstarBlockEntityTypes.OXYGEN_CONCENTRATOR.get();
	}

}
