package com.lightning.northstar.block;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VenusMushroomBlock extends BushBlock implements BonemealableBlock {
	protected static final BooleanProperty IS_ON_CEILING = BooleanProperty.create("is_on_ceiling");
	protected static final VoxelShape CEILING_SHAPE = Block.box(5.0D, 10.0D, 5.0D, 11.0D, 16.0D, 11.0D);
	protected static final VoxelShape SHAPE = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 6.0D, 11.0D);
	private final Supplier<Holder<? extends ConfiguredFeature<?, ?>>> upsideDownSupplier;
	private final Supplier<Holder<? extends ConfiguredFeature<?, ?>>> featureSupplier;

	public VenusMushroomBlock(Properties pProperties,
			Supplier<Holder<? extends ConfiguredFeature<?, ?>>> upRightFeature, @Nullable Supplier<Holder<? extends ConfiguredFeature<?, ?>>> upsideDownFeature) {
		super(pProperties);
		this.featureSupplier = upRightFeature;	
		this.upsideDownSupplier = upsideDownFeature;	
	    this.registerDefaultState(this.defaultBlockState().setValue(IS_ON_CEILING, false));
	}
	
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(IS_ON_CEILING);
	}
	
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		if(pState.getValue(IS_ON_CEILING)){
			return CEILING_SHAPE;
		}
		else return SHAPE;
	}
	
	public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
		if(pState.getValue(IS_ON_CEILING)) {
			BlockPos blockpos = pPos.above();
			BlockState blockstate = pLevel.getBlockState(blockpos);
			if (blockstate.is(BlockTags.MUSHROOM_GROW_BLOCK)) {
				return true;
			} else {
				return blockstate.canSustainPlant(pLevel, blockpos, net.minecraft.core.Direction.DOWN, this);
			}
		}else {
			BlockPos blockpos = pPos.below();
			BlockState blockstate = pLevel.getBlockState(blockpos);
			if (blockstate.is(BlockTags.MUSHROOM_GROW_BLOCK)) {
				return true;
			} else {
				return blockstate.canSustainPlant(pLevel, blockpos, net.minecraft.core.Direction.UP, this);
			}
		}
	}	
	protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
		return pState.isSolidRender(pLevel, pPos);
	}
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		System.out.println(pContext.getClickedFace());
		return this.defaultBlockState().setValue(IS_ON_CEILING, pContext.getClickedFace() == Direction.DOWN);  
	}
	
	public void performBonemeal(ServerLevel pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
		this.growMushroom(pLevel, pPos, pState, pRandom);
	}
	public boolean growMushroom(ServerLevel pLevel, BlockPos pPos, BlockState pState, RandomSource pRandom) {
		net.minecraftforge.event.level.SaplingGrowTreeEvent event;
		if(pState.getValue(IS_ON_CEILING))
			{event = net.minecraftforge.event.ForgeEventFactory.blockGrowFeature(pLevel, pRandom, pPos, this.upsideDownSupplier.get());}
		else
			{event = net.minecraftforge.event.ForgeEventFactory.blockGrowFeature(pLevel, pRandom, pPos, this.featureSupplier.get());}
		if (event.getResult().equals(net.minecraftforge.eventbus.api.Event.Result.DENY)) return false;
		pLevel.removeBlock(pPos, false);
		if (event.getFeature().value().place(pLevel, pLevel.getChunkSource().getGenerator(), pRandom, pPos)) {
			return true;
		} else {
			pLevel.setBlock(pPos, pState, 3);
			return false;
		}
	}
	public boolean isValidBonemealTarget(BlockGetter pLevel, BlockPos pPos, BlockState pState, boolean pIsClient) {
		return pState.getValue(IS_ON_CEILING) ? ((VenusMushroomBlock)(pState.getBlock())).upsideDownSupplier != null : ((VenusMushroomBlock)(pState.getBlock())).featureSupplier != null;
	}

	public boolean isBonemealSuccess(Level pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
		return pState.getValue(IS_ON_CEILING) ? ((VenusMushroomBlock)(pState.getBlock())).upsideDownSupplier != null : ((VenusMushroomBlock)(pState.getBlock())).featureSupplier != null;
//		return (double)pRandom.nextFloat() < 0.4D;
	}
}
