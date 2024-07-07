package com.lightning.northstar.block.tech.temperature_regulator;

import com.lightning.northstar.block.entity.NorthstarBlockEntityTypes;
import com.lightning.northstar.world.dimension.NorthstarPlanets;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.gui.ScreenOpener;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;

public class TemperatureRegulatorBlock extends HorizontalKineticBlock implements IBE<TemperatureRegulatorBlockEntity> {
	protected static final VoxelShape AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D);
	
	public TemperatureRegulatorBlock(Properties pProperties) {
		super(pProperties);
	}
	
	@Override
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos,Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
		BlockEntity entity = pLevel.getBlockEntity(pPos);
		if(entity instanceof TemperatureRegulatorBlockEntity) {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
				() -> () -> withBlockEntityDo(pLevel, pPos, be -> this.displayScreen(be, pPlayer)));
			return InteractionResult.SUCCESS;	
	    }
		return InteractionResult.sidedSuccess(pLevel.isClientSide());
	}
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return IBE.super.newBlockEntity(pPos, pState);
	}

	@Override
	public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
		System.out.println("big cheese is real");
		if (!pState.is(pNewState.getBlock())) {
			BlockEntity blockentity = pLevel.getBlockEntity(pPos);
			if (blockentity instanceof TemperatureRegulatorBlockEntity) {
				System.out.println("REMOVING I THINK");
				((TemperatureRegulatorBlockEntity)blockentity).removeTemp((TemperatureRegulatorBlockEntity) blockentity);
			}

			super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
		}
	}
	public RenderShape getRenderShape(BlockState pState) {
		return RenderShape.MODEL;
	}
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return AABB;
	}
	
	@Override
	public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
		BlockEntity be = world.getBlockEntity(pos);
		TemperatureRegulatorBlockEntity reg = null;
		if(be instanceof TemperatureRegulatorBlockEntity) {
			reg = (TemperatureRegulatorBlockEntity) be;
		}
		if (reg == null)
			return 0;
		return reg.temp > NorthstarPlanets.getPlanetTemp(reg.getLevel().dimension()) ? 9 : 0;
	}
	
	@OnlyIn(value = Dist.CLIENT)
	protected void displayScreen(TemperatureRegulatorBlockEntity be, Player player) {
		if (player instanceof LocalPlayer)
			ScreenOpener.open(new TemperatureRegulatorScreen(be));
	}
	
	
	
	@Override
	public Class<TemperatureRegulatorBlockEntity> getBlockEntityClass() {
		return TemperatureRegulatorBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends TemperatureRegulatorBlockEntity> getBlockEntityType() {
		return NorthstarBlockEntityTypes.TEMPERATURE_REGULATOR_BLOCK_ENTITY.get();
	}
	@Override
	public Axis getRotationAxis(BlockState state) {
		return Axis.Y;
	}
	@Override
	public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
		return face == Direction.DOWN;
	}
}
