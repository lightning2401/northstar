package com.lightning.northstar.block;

import javax.annotation.Nullable;

import com.lightning.northstar.block.entity.NorthstarBlockEntityTypes;
import com.lightning.northstar.block.entity.VenusExhaustBlockEntity;
import com.lightning.northstar.particle.SulfurPoofParticleData;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class VenusSporeSpreaderBlock extends BaseEntityBlock {

	public VenusSporeSpreaderBlock(Properties pProperties) {
		super(pProperties);
	}
	public static void makeParticles(Level level, BlockPos pos) {
		if(!level.random.nextBoolean())
			return;
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		level.addAlwaysVisibleParticle(new SulfurPoofParticleData(), true, x + 0.5, y + 2, z + 0.5, 0.0D, 0.0D, 0.0D);
		BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

		for(int l = 0; l < 14 + level.random.nextInt(); ++l) {
			blockpos$mutableblockpos.set(x, y, z);
			BlockState blockstate = level.getBlockState(blockpos$mutableblockpos);
			if (!blockstate.isCollisionShapeFullBlock(level, blockpos$mutableblockpos)) {
				level.addAlwaysVisibleParticle(new SulfurPoofParticleData(), true, (double)blockpos$mutableblockpos.getX() + level.random.nextDouble(), (double)blockpos$mutableblockpos.getY() + level.random.nextDouble(), (double)blockpos$mutableblockpos.getZ() + level.random.nextDouble(), 0.0D, 0.0D, 0.0D);
			}
		}
	}
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
		return createTickerHelper(pBlockEntityType, NorthstarBlockEntityTypes.VENUS_EXHAUST.get(), VenusExhaustBlockEntity::particleTick);
	}
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new VenusExhaustBlockEntity(pPos, pState);
	}
	public RenderShape getRenderShape(BlockState pState) {
		return RenderShape.MODEL;
	}
}
