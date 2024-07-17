package com.lightning.northstar.block.tech.oxygen_filler;

import com.lightning.northstar.block.entity.NorthstarBlockEntityTypes;
import com.simibubi.create.Create;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class OxygenFillerBlock extends HorizontalKineticBlock implements IBE<OxygenFillerBlockEntity> {

	public OxygenFillerBlock(Properties properties) {
		super(properties);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand,
		BlockHitResult hit) {
		ItemStack heldItem = player.getItemInHand(hand);
		System.out.println(heldItem);

		return onBlockEntityUse(world, pos, be -> {
			ItemStack mainItemStack = be.container.getItem(0);
			player.getInventory().placeItemBackInInventory(mainItemStack);
			player.getInventory().removeItem(heldItem);
			be.container.setItem(0, heldItem);
			if(!mainItemStack.isEmpty())
			world.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, .2f, 1f + Create.RANDOM.nextFloat());

			be.notifyUpdate();
			return InteractionResult.SUCCESS;
		});	
	}
	
	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean pIsMoving) {
		if (!state.is(newState.getBlock())) {
			BlockEntity be = world.getBlockEntity(pos);
			if (!(be instanceof OxygenFillerBlockEntity))
				return;
			OxygenFillerBlockEntity fillerBE = (OxygenFillerBlockEntity) be;
			Block.popResource(world, pos, fillerBE.container.getItem(0));
			world.removeBlockEntity(pos);
		}
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return IBE.super.newBlockEntity(pPos, pState);
	}

	@Override
	public Axis getRotationAxis(BlockState state) {
		return Axis.Y;
	}
	
	@Override
	public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
		return face == Direction.DOWN;
	}

	@Override
	public Class<OxygenFillerBlockEntity> getBlockEntityClass() {
		return OxygenFillerBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends OxygenFillerBlockEntity> getBlockEntityType() {
		return NorthstarBlockEntityTypes.OXYGEN_FILLER.get();
	}

}