package com.lightning.northstar.block.tech.astronomy_table;

import com.lightning.northstar.block.entity.NorthstarBlockEntityTypes;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

public class AstronomyTableBlock extends Block implements IBE<AstronomyTableBlockEntity> {
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	public static final VoxelShape SHAPE_BASE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D);
	public static final VoxelShape SHAPE_POST = Block.box(3.0D, 2.0D, 3.0D, 13.0D, 14.0D, 13.0D);
	public static final VoxelShape SHAPE_COMMON = Shapes.or(SHAPE_BASE, SHAPE_POST);
	public static final VoxelShape SHAPE_TOP_PLATE = Block.box(0.0D, 15.0D, 0.0D, 16.0D, 15.0D, 16.0D);
	public static final VoxelShape SHAPE_COLLISION = Shapes.or(SHAPE_COMMON, SHAPE_TOP_PLATE);
	public static final VoxelShape SHAPE_WEST = Shapes.or(Block.box(1.0D, 10.0D, 0.0D, 5.333333D, 14.0D, 16.0D), Block.box(5.333333D, 12.0D, 0.0D, 9.666667D, 16.0D, 16.0D), Block.box(9.666667D, 14.0D, 0.0D, 14.0D, 18.0D, 16.0D), SHAPE_COMMON);
	public static final VoxelShape SHAPE_NORTH = Shapes.or(Block.box(0.0D, 10.0D, 1.0D, 16.0D, 14.0D, 5.333333D), Block.box(0.0D, 12.0D, 5.333333D, 16.0D, 16.0D, 9.666667D), Block.box(0.0D, 14.0D, 9.666667D, 16.0D, 18.0D, 14.0D), SHAPE_COMMON);
	public static final VoxelShape SHAPE_EAST = Shapes.or(Block.box(10.666667D, 10.0D, 0.0D, 15.0D, 14.0D, 16.0D), Block.box(6.333333D, 12.0D, 0.0D, 10.666667D, 16.0D, 16.0D), Block.box(2.0D, 14.0D, 0.0D, 6.333333D, 18.0D, 16.0D), SHAPE_COMMON);
	public static final VoxelShape SHAPE_SOUTH = Shapes.or(Block.box(0.0D, 10.0D, 10.666667D, 16.0D, 14.0D, 15.0D), Block.box(0.0D, 12.0D, 6.333333D, 16.0D, 16.0D, 10.666667D), Block.box(0.0D, 14.0D, 2.0D, 16.0D, 18.0D, 6.333333D), SHAPE_COMMON);

	public AstronomyTableBlock(Properties properties) {
		super(properties);
		registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
	}
	
	public RenderShape getRenderShape(BlockState pState) {
		return RenderShape.MODEL;
	}
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
    
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE_COLLISION;
     }

     public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        switch ((Direction)pState.getValue(FACING)) {
           case NORTH:
              return SHAPE_NORTH;
           case SOUTH:
              return SHAPE_SOUTH;
           case EAST:
              return SHAPE_EAST;
           case WEST:
              return SHAPE_WEST;
           default:
              return SHAPE_COMMON;
        }
     }
    
	
	 @Override
	  public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos,Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
	        if (!pLevel.isClientSide()) {
	            BlockEntity entity = pLevel.getBlockEntity(pPos);
	            if(entity instanceof AstronomyTableBlockEntity) {
	                NetworkHooks.openScreen(((ServerPlayer)pPlayer), (AstronomyTableBlockEntity)entity, pPos);

	            } else {
	                throw new IllegalStateException("Our Container provider is missing!");
	            }
	        }

	        return InteractionResult.sidedSuccess(pLevel.isClientSide());
	    }

	 public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		 return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
	 }
	 
	 
	@Override
	public Class<AstronomyTableBlockEntity> getBlockEntityClass() {
		return AstronomyTableBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends AstronomyTableBlockEntity> getBlockEntityType() {
		return NorthstarBlockEntityTypes.ASTRONOMY_TABLE.get();
	}
	

}
