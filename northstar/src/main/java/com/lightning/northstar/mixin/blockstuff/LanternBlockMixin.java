package com.lightning.northstar.mixin.blockstuff;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.lightning.northstar.block.ExtinguishedLanternBlock;
import com.lightning.northstar.block.NorthstarTechBlocks;
import com.lightning.northstar.world.OxygenStuff;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

@Mixin(LanternBlock.class)
public class LanternBlockMixin {
	@Shadow
	@Final
	private static final BooleanProperty HANGING = BlockStateProperties.HANGING;
	@Shadow
	@Final
	private static final  BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	

    @Inject(method = "getStateForPlacement", at = @At("HEAD"), cancellable = true)
	public void getStateForPlacement(BlockPlaceContext pContext, CallbackInfoReturnable<BlockState> info) {
    	try{
    		if (pContext.getItemInHand().getItem() == Blocks.LANTERN.asItem()) 
    	
    	{    	boolean hanging = false;
        BlockState blockstate = Blocks.LANTERN.defaultBlockState().setValue(HANGING, true);
        if (blockstate.canSurvive(pContext.getLevel(), pContext.getClickedPos())) {hanging = true;}
    	
        FluidState fluidstate = pContext.getLevel().getFluidState(pContext.getClickedPos());    	
        System.out.println(OxygenStuff.hasOxygen(pContext.getClickedPos(),pContext.getLevel().dimension()));
    	
    	if(!OxygenStuff.hasOxygen(pContext.getClickedPos(),pContext.getLevel().dimension())) {
    		pContext.getLevel().playSound(null, pContext.getClickedPos(), SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 1, 0);
    		info.setReturnValue(NorthstarTechBlocks.EXTINGUISHED_LANTERN.get().defaultBlockState()
    		.setValue(ExtinguishedLanternBlock.HANGING, hanging).setValue(ExtinguishedLanternBlock.WATERLOGGED, fluidstate.is(Fluids.WATER)));
    		
    	}
    	
    }}	catch(Exception e) {
    }
	}
	
    @Inject(method = "updateShape", at = @At("TAIL"), cancellable = true)
	public void updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, 
	LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos, CallbackInfoReturnable<BlockState> info) {
    	try{
    	if(pState.getBlock() == Blocks.LANTERN) {
            System.out.println(OxygenStuff.hasOxygen(pCurrentPos,((Level)pLevel).dimension()));
    	if(!OxygenStuff.hasOxygen(pCurrentPos, ((Level)pLevel).dimension())) {
    		pLevel.playSound(null, pCurrentPos, SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 1, 0);
    		info.setReturnValue(NorthstarTechBlocks.EXTINGUISHED_LANTERN.get().defaultBlockState()
    				.setValue(ExtinguishedLanternBlock.HANGING, pState.getValue(HANGING)).setValue(ExtinguishedLanternBlock.WATERLOGGED, pState.getValue(WATERLOGGED)));
    	}
    	}
    	}
    	catch(Exception e) {
    		
    	}
	}

}
