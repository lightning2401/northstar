package com.lightning.northstar.block.tech.ice_box;

import com.lightning.northstar.block.NorthstarTechBlocks;
import com.lightning.northstar.block.entity.NorthstarBlockEntityTypes;
import com.simibubi.create.AllShapes;
import com.simibubi.create.Create;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.fluids.transfer.GenericItemEmptying;
import com.simibubi.create.content.fluids.transfer.GenericItemFilling;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.fluid.FluidHelper;
import com.simibubi.create.foundation.item.ItemHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

public class IceBoxBlock extends Block implements IBE<IceBoxBlockEntity>, IWrenchable {
	
	public static final DirectionProperty FACING = BlockStateProperties.FACING_HOPPER;

	public IceBoxBlock(Properties p_i48440_1_) {
		super(p_i48440_1_);
		registerDefaultState(defaultBlockState().setValue(FACING, Direction.DOWN));
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> p_206840_1_) {
		super.createBlockStateDefinition(p_206840_1_.add(FACING));
	}

	@Override
	public InteractionResult onWrenched(BlockState state, UseOnContext context) {
		if (!context.getLevel().isClientSide)
			withBlockEntityDo(context.getLevel(), context.getClickedPos(),
				bte -> bte.onWrenched(context.getClickedFace()));
		return InteractionResult.SUCCESS;
	}

	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn,
		BlockHitResult hit) {
		ItemStack heldItem = player.getItemInHand(handIn);

		return onBlockEntityUse(worldIn, pos, be -> {
			if (!heldItem.isEmpty()) {
				if (FluidHelper.tryEmptyItemIntoBE(worldIn, player, handIn, heldItem, be))
					return InteractionResult.SUCCESS;
				if (FluidHelper.tryFillItemFromBE(worldIn, player, handIn, heldItem, be))
					return InteractionResult.SUCCESS;

				if (GenericItemEmptying.canItemBeEmptied(worldIn, heldItem)
					|| GenericItemFilling.canItemBeFilled(worldIn, heldItem))
					return InteractionResult.SUCCESS;
				if (heldItem.getItem()
					.equals(Items.SPONGE)
					&& !be.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
						.map(iFluidHandler -> iFluidHandler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.EXECUTE))
						.orElse(FluidStack.EMPTY)
						.isEmpty()) {
					return InteractionResult.SUCCESS;
				}
				return InteractionResult.PASS;
			}

			IItemHandlerModifiable inv = be.itemCapability.orElse(new ItemStackHandler(1));
			boolean success = false;
			for (int slot = 0; slot < inv.getSlots(); slot++) {
				ItemStack stackInSlot = inv.getStackInSlot(slot);
				if (stackInSlot.isEmpty())
					continue;
				player.getInventory()
					.placeItemBackInInventory(stackInSlot);
				inv.setStackInSlot(slot, ItemStack.EMPTY);
				success = true;
			}
			if (success)
				worldIn.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, .2f,
					1f + Create.RANDOM.nextFloat());
			return InteractionResult.SUCCESS;
		});
	}

	@Override
	public void updateEntityAfterFallOn(BlockGetter worldIn, Entity entityIn) {
		super.updateEntityAfterFallOn(worldIn, entityIn);
		if (!NorthstarTechBlocks.ICE_BOX.has(worldIn.getBlockState(entityIn.blockPosition())))
			return;
		if (!(entityIn instanceof ItemEntity))
			return;
		if (!entityIn.isAlive())
			return;
		ItemEntity itemEntity = (ItemEntity) entityIn;
		withBlockEntityDo(worldIn, entityIn.blockPosition(), be -> {

			// Tossed items bypass the quarter-stack limit
			be.inputInventory.withMaxStackSize(64);
			ItemStack insertItem = ItemHandlerHelper.insertItem(be.inputInventory, itemEntity.getItem()
				.copy(), false);
			be.inputInventory.withMaxStackSize(64);

			if (insertItem.isEmpty()) {
				itemEntity.discard();
				return;
			}

			itemEntity.setItem(insertItem);
		});
	}

	@Override
	public VoxelShape getInteractionShape(BlockState p_199600_1_, BlockGetter p_199600_2_, BlockPos p_199600_3_) {
		return AllShapes.BASIN_RAYTRACE_SHAPE;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return AllShapes.BASIN_BLOCK_SHAPE;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext ctx) {
		if (ctx instanceof EntityCollisionContext && ((EntityCollisionContext) ctx).getEntity() instanceof ItemEntity)
			return AllShapes.BASIN_COLLISION_SHAPE;
		return getShape(state, reader, pos, ctx);
	}

	@Override
	public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		IBE.onRemove(state, worldIn, pos, newState);
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos) {
		return getBlockEntityOptional(worldIn, pos).map(IceBoxBlockEntity::getInputInventory)
			.map(ItemHelper::calcRedstoneFromInventory)
			.orElse(0);
	}

	@Override
	public Class<IceBoxBlockEntity> getBlockEntityClass() {
		return IceBoxBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends IceBoxBlockEntity> getBlockEntityType() {
		return NorthstarBlockEntityTypes.ICE_BOX.get();
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
		return false;
	}
}
