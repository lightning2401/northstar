package com.lightning.northstar.block;

import javax.annotation.Nullable;

import com.lightning.northstar.sound.NorthstarSounds;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.PushReaction;

public class LaserBlock extends Block implements SimpleWaterloggedBlock {
	public static final DirectionProperty DIRECTION = BlockStateProperties.FACING;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final IntegerProperty POWER = BlockStateProperties.LEVEL;
	
	public LaserBlock(Properties pProperties) {
		super(pProperties);
	    this.registerDefaultState(this.defaultBlockState().setValue(DIRECTION, Direction.UP).setValue(WATERLOGGED, Boolean.valueOf(false)).setValue(POWER, 1));
	}
	
	public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
		updateColumn(pLevel, pPos, pState, pLevel.getBlockState(pPos.relative(pState.getValue(DIRECTION).getOpposite())));
	}
	
	public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
		double d0 = (double)pPos.getX();
		double d1 = (double)pPos.getY();
		double d2 = (double)pPos.getZ();
		if (pRandom.nextInt(200) == 0) {
			pLevel.playLocalSound(d0, d1, d2, NorthstarSounds.LASER_AMBIENT.get(), SoundSource.BLOCKS, 0.2F + pRandom.nextFloat() * 0.2F, 0.9F + pRandom.nextFloat() * 0.15F, false);
		  	}
		
	}
	
	public static void updateColumn(LevelAccessor pLevel, BlockPos pPos, BlockState pState) {
		updateColumn(pLevel, pPos, pLevel.getBlockState(pPos), pState);
	}
	
	public static void updateColumn(LevelAccessor pLevel, BlockPos pPos, BlockState pLaser, BlockState pState) {	
			BlockPos.MutableBlockPos blockpos$mutableblockpos = pPos.mutable();
			BlockState blockstate = getLaserState(pState, (Level) pLevel, pPos);
		    Direction dir;
		    
		    
		    
		    
		    if (pState.is(NorthstarTechBlocks.LASER.get()) || pState.is(NorthstarTechBlocks.AMETHYST_CRYSTAL.get())) {
		    dir = pState.getValue(DIRECTION);}
		    else if(pState.is(Blocks.AIR)) 
		    {dir = pLaser.getValue(DIRECTION);}
		    else 
		    {dir = Direction.DOWN;}

		    if (pState.getBlock() == NorthstarTechBlocks.LASER_LENSE.get()) {
		    	blockpos$mutableblockpos = pPos.below().mutable();
		    }
		    while(pLevel.getBlockState(blockpos$mutableblockpos) == Blocks.AIR.defaultBlockState() || 
		    		pLevel.getBlockState(blockpos$mutableblockpos).getBlock() == NorthstarTechBlocks.LASER.get() ||
		    		pLevel.getBlockState(blockpos$mutableblockpos).getBlock() == NorthstarTechBlocks.AMETHYST_CRYSTAL.get()) {
		    boolean crystal_flag;
		    if (pLevel.getBlockState(blockpos$mutableblockpos).getBlock() == NorthstarTechBlocks.AMETHYST_CRYSTAL.get()) {
		    	crystal_flag = true;
		    	if(pLevel.getBlockState(blockpos$mutableblockpos).getValue(CrystalBlock.FACING) != dir.getOpposite())
		    	dir = pLevel.getBlockState(blockpos$mutableblockpos).getValue(CrystalBlock.FACING);
		    	else {return;}
		    	if(blockstate.getBlock() != Blocks.AIR)
		    	blockstate = blockstate.setValue(DIRECTION, dir);
		    	
			    blockpos$mutableblockpos.move(dir);
		    	System.out.println("AMETHYST DETECTED!");
		    }else {crystal_flag = false;}
		    if (blockstate != Blocks.AIR.defaultBlockState() && !crystal_flag) {
		    	blockstate = blockstate.setValue(DIRECTION, dir);
		    }
		    if (!pLevel.setBlock(blockpos$mutableblockpos, blockstate, 2) && crystal_flag) {
		    	return;
		    }
		    
		    blockpos$mutableblockpos.move(dir);
		   }
	}
	@SuppressWarnings("deprecation")
	@Override
	public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
		if (!pEntity.fireImmune()) {
			pEntity.setRemainingFireTicks(pEntity.getRemainingFireTicks() + 1);
			if (pEntity.getRemainingFireTicks() == 0) {
				pEntity.setSecondsOnFire(8);
			}
		}

		pEntity.hurt(DamageSource.IN_FIRE, 4);
		super.entityInside(pState, pLevel, pPos, pEntity);
	}
	
	
	private static BlockState getLaserState(BlockState pBlockState, Level level, BlockPos pos) {
		if (pBlockState.is(NorthstarTechBlocks.LASER.get())) {
			return pBlockState;
		}else if (pBlockState.is(NorthstarTechBlocks.LASER_LENSE.get()))
		{return NorthstarTechBlocks.LASER.getDefaultState();}
		else if (pBlockState.is(NorthstarTechBlocks.AMETHYST_CRYSTAL.get()))
		{	int count = 0;
	        BlockPos.MutableBlockPos blockpos = pos.mutable();
			for(Direction direction : Direction.values()) {
	        	blockpos.setWithOffset(pos, direction);	
	        	if (level.getBlockState(blockpos).is(NorthstarTechBlocks.LASER.get())) {
	        		count++;
	        	}
			}
		if(count > 1) {return NorthstarTechBlocks.LASER.getDefaultState().setValue(DIRECTION, pBlockState.getValue(CrystalBlock.FACING));}
		else {return Blocks.AIR.defaultBlockState();}
		}
		else 
		{return Blocks.AIR.defaultBlockState();}
	}
	
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(DIRECTION, WATERLOGGED, POWER);
	}
	
	public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
		Direction direction = pState.getValue(DIRECTION);
		BlockPos blockpos = pPos.relative(direction.getOpposite());
		if(direction != Direction.DOWN) {
			System.out.println(pLevel.getBlockState(blockpos).is(NorthstarTechBlocks.AMETHYST_CRYSTAL.get()));
			return true;
		}
		return pLevel.getBlockState(blockpos).is(NorthstarTechBlocks.LASER.get()) || 
				pLevel.getBlockState(blockpos).is(NorthstarTechBlocks.LASER_LENSE.get()) || 
				pLevel.getBlockState(blockpos).is(NorthstarTechBlocks.AMETHYST_CRYSTAL.get());
	}
	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
	    return this.defaultBlockState().setValue(DIRECTION, pContext.getClickedFace());
	}
	
	public BlockState rotate(BlockState pState, Rotation pRotation) {
		return pState.setValue(DIRECTION, pRotation.rotate(pState.getValue(DIRECTION)));
	}
	public PushReaction getPistonPushReaction(BlockState pState) {
		return PushReaction.DESTROY;
	}
	public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
		pLevel.scheduleTick(pPos, this, 0);
	}
	@SuppressWarnings("deprecation")
	public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
		pLevel.scheduleTick(pCurrentPos, this, 1);
		return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
	}

}
