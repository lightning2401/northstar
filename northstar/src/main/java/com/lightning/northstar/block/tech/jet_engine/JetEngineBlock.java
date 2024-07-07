package com.lightning.northstar.block.tech.jet_engine;

import com.lightning.northstar.block.entity.NorthstarBlockEntityTypes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class JetEngineBlock extends Block implements IWrenchable, IBE<JetEngineBlockEntity>{
	public static final BooleanProperty TOP = BooleanProperty.create("top");
	public static final BooleanProperty BOTTOM = BooleanProperty.create("bottom");
	
	public JetEngineBlock(Properties properties) 
	{super(properties);
    this.registerDefaultState(defaultBlockState().setValue(TOP, true)
			.setValue(BOTTOM, true));}
	
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> p_206840_1_) {
		p_206840_1_.add(TOP, BOTTOM);
	}
	
	public static boolean isTank(BlockState state) {
		return state.getBlock() instanceof JetEngineBlock;
	}
	
	@Override
	public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean moved) {
		if (oldState.getBlock() == state.getBlock())
			return;
		if (moved)
			return;
		withBlockEntityDo(world, pos, JetEngineBlockEntity::updateConnectivity);
	}

	@Override
	public Class<JetEngineBlockEntity> getBlockEntityClass() {
		return JetEngineBlockEntity.class;
	}


	@Override
	public BlockEntityType<? extends JetEngineBlockEntity> getBlockEntityType() {
		return NorthstarBlockEntityTypes.JET_ENGINE.get();
	}
	
}
