package com.lightning.northstar.block.tech.telescope;

import javax.annotation.Nullable;

import org.joml.Vector3f;

import com.lightning.northstar.block.entity.NorthstarBlockEntityTypes;
import com.lightning.northstar.world.dimension.NorthstarPlanets;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.Lang;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

public class TelescopeBlock extends BaseEntityBlock implements IBE<TelescopeBlockEntity>{
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	   protected static final VoxelShape SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 24.0D, 12.0D);

	public TelescopeBlock(Properties properties) {
		super(properties);
	    this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}
	
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);}
    
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return SHAPE;}
	
    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }
	
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		Direction player_looking = pContext.getNearestLookingDirection();
		if (player_looking == Direction.UP || player_looking == Direction.DOWN) 
		{return this.defaultBlockState().setValue(FACING, Direction.NORTH);}else 
		{return this.defaultBlockState().setValue(FACING, pContext.getNearestLookingDirection());}
	}

	@Override
	  public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos,Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
	        if (!pLevel.isClientSide()) {
	            BlockEntity entity = pLevel.getBlockEntity(pPos);
	            if(entity instanceof TelescopeBlockEntity && canSeeSky(pPos.above(), pLevel, pState.getValue(FACING)) && 
	            		(pLevel.isNight() || NorthstarPlanets.canSeeSkyAtDay(pLevel.dimension())) && (!pLevel.isRaining() || !NorthstarPlanets.hasWeather(pLevel.dimension()) )
	            		&& NorthstarPlanets.planetHasSky(pLevel.dimension())) {
	                NetworkHooks.openScreen(((ServerPlayer)pPlayer), (TelescopeBlockEntity)entity, pPos);

	            } else {
	            	pPlayer.displayClientMessage(
	        				Lang.translateDirect("northstar.gui.telescope_fail"), true);
	            }
	        }

	        return InteractionResult.sidedSuccess(pLevel.isClientSide());
	    }
	 
		public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
			return new TelescopeBlockEntity(pPos, pState);
		}
		
	private boolean canSeeSky(BlockPos pos, Level level, Direction dir) {
		boolean flag = false;
		int clearSpots = 0;
		for(int x = 0; x <= 3; x++) {
			Vector3f vec = dir.step();
			vec.mul(x);
			if(level.canSeeSky(pos.offset(new Vec3i((int)vec.x(), (int)vec.y(), (int)vec.z())))) {
				clearSpots++;
				if(clearSpots >= 2)
				{flag = true;}
			}
		}
		
		
		return flag;
	}
		
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, NorthstarBlockEntityTypes.TELESCOPE.get(),
		TelescopeBlockEntity::tick);
	}

	@Override
	public Class<TelescopeBlockEntity> getBlockEntityClass() {
		return TelescopeBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends TelescopeBlockEntity> getBlockEntityType() {
		return NorthstarBlockEntityTypes.TELESCOPE.get();
	}

}
