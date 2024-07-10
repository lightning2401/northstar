package com.lightning.northstar.block;

import javax.annotation.Nullable;

import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public class LogBlock extends RotatedPillarBlock {
	
	private Block strippedVariant;

	public LogBlock(Properties pProperties, Block stripped) {
		super(pProperties);
		strippedVariant = stripped;
	}
	
	@Override
	@Nullable
	public BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate) {
		if (toolAction.equals(ToolActions.AXE_STRIP) ) {
			return strippedVariant.defaultBlockState().setValue(AXIS, state.getValue(AXIS));
		}
		return null;
	}

}
