package com.lightning.northstar.block.crops;

import javax.annotation.Nullable;

import com.lightning.northstar.block.NorthstarBlocks;
import com.lightning.northstar.item.NorthstarItems;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MartianFlowerBlock extends BushBlock implements BonemealableBlock {
	public static final int MAX_AGE = 2;
	public static final IntegerProperty AGE = BlockStateProperties.AGE_2;
	protected static final float AABB_OFFSET = 3.0F;
	protected static final VoxelShape SHAPE = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 10.0D, 11.0D);

	
	public MartianFlowerBlock(BlockBehaviour.Properties pProperties) {
		super(pProperties);
		this.registerDefaultState(this.stateDefinition.any().setValue(this.getAgeProperty(), Integer.valueOf(2)));
	}
	
	public Item getSeedItem() {
		return NorthstarItems.MARS_TULIP_SEEDS.get();
	}
	
	public IntegerProperty getAgeProperty() {
		return AGE;
	}
	public int getMaxAge() {
		return MAX_AGE;
	}

	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		Vec3 vec3 = pState.getOffset(pLevel, pPos);
		return SHAPE.move(vec3.x, vec3.y, vec3.z);
	}
	
	@Override
	protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
		return pState.is(BlockTags.DIRT) || pState.is(Blocks.FARMLAND) || pState.is(NorthstarBlocks.MARS_SOIL.get()) || pState.is(NorthstarBlocks.MARS_FARMLAND.get()) || pState.is(NorthstarBlocks.MARTIAN_GRASS.get());
	}
	public boolean isMaxAge(BlockState pState) {
		return pState.getValue(this.getAgeProperty()) >= this.getMaxAge();
	}
	public boolean isRandomlyTicking(BlockState pState) {
		return !this.isMaxAge(pState);
	}
	
	@SuppressWarnings("deprecation")
	public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
		if (!pLevel.isAreaLoaded(pPos, 1)) return; 
		if (pLevel.getRawBrightness(pPos, 0) >= 9) {
			int i = this.getAge(pState);
			if (i < this.getMaxAge()) {
	               pLevel.setBlock(pPos, this.getStateForAge(i + 1), 2);
			}
		}
	}
	
	public BlockState getStateForAge(int pAge) {
		return this.defaultBlockState().setValue(this.getAgeProperty(), Integer.valueOf(pAge));
	}
	
	protected int getAge(BlockState pState) {
		return pState.getValue(this.getAgeProperty());
	}
	
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(AGE);
	}
	
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		Item playerItem = pContext.getPlayer().getItemInHand(pContext.getHand()).getItem();
		if(playerItem == this.getSeedItem())
			return this.defaultBlockState().setValue(AGE, Integer.valueOf(0));
		return this.defaultBlockState();
	}

	@Override
	public boolean isBonemealSuccess(Level pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
		return true;
	}

	@Override
	public void performBonemeal(ServerLevel pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
		if(!this.isMaxAge(pState)) {
			pLevel.setBlock(pPos, this.getStateForAge(this.getAge(pState) + 1), 2);
		}
	}

	@Override
	public boolean isValidBonemealTarget(LevelReader p_256559_, BlockPos p_50898_, BlockState pState,
			boolean p_50900_) {
		return !this.isMaxAge(pState);
	}

}
