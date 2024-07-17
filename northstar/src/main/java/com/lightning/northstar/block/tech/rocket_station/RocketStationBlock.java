package com.lightning.northstar.block.tech.rocket_station;

import com.lightning.northstar.block.entity.NorthstarBlockEntityTypes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.Couple;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

public class RocketStationBlock extends HorizontalDirectionalBlock implements IBE<RocketStationBlockEntity>, IWrenchable {
	public static final VoxelShape SHAPE = Shapes.or(Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D), Block.box(1.0D, 4.0D, 1.0D, 15.0D, 16.0D, 15.0D));
	public static final BooleanProperty ASSEMBLING = BooleanProperty.create("assembling");

	public RocketStationBlock(Properties properties) {
		super(properties);
		registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		BlockState state = defaultBlockState();
		Direction horizontalDirection = pContext.getHorizontalDirection();

		state = state.setValue(FACING, horizontalDirection.getOpposite());
		return state;
	}
	
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return SHAPE;
	}
	
	@Override
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand,
		BlockHitResult pHit) { 	       	
		if(pPlayer.getItemInHand(pHand).is(Items.COMPASS)) {
			return InteractionResult.PASS;
		}
		if (!pLevel.isClientSide()) {
			BlockEntity entity = pLevel.getBlockEntity(pPos);
			if(entity instanceof RocketStationBlockEntity) {
               NetworkHooks.openScreen(((ServerPlayer)pPlayer), (RocketStationBlockEntity)entity, pPos);
                
            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return IBE.super.newBlockEntity(pPos, pState);
	}
	
	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block p_220069_4_, BlockPos updatePos,
		boolean p_220069_6_) {
	}

	
	public static Couple<Integer> getSpeedRange() {
		return Couple.create(1, 16);
	}
	
	@Override
	public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock())) {
			BlockEntity be = worldIn.getBlockEntity(pos);
			if (!(be instanceof RocketStationBlockEntity))
				return;
			RocketStationBlockEntity stationBE = (RocketStationBlockEntity) be;
			Block.popResource(worldIn, pos, stationBE.container.getItem(0));
		}
		IBE.onRemove(state, worldIn, pos, newState);
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> pBuilder) {
		super.createBlockStateDefinition(pBuilder.add(FACING));
	}


	@Override
	public Class<RocketStationBlockEntity> getBlockEntityClass() {
		return RocketStationBlockEntity.class;
	}



	@Override
	public BlockEntityType<? extends RocketStationBlockEntity> getBlockEntityType() {
		return NorthstarBlockEntityTypes.ROCKET_STATION.get();
	}

}
