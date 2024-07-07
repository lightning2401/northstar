package com.lightning.northstar.block.tech.computer_rack;

import com.lightning.northstar.block.tech.NorthstarPartialModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.contraptions.actors.contraptionControls.ContraptionControlsBlock;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class TargetingComputerRackRenderer extends SmartBlockEntityRenderer<TargetingComputerRackBlockEntity> {

	public TargetingComputerRackRenderer(Context context) {
		super(context);
	}

	@Override
	protected void renderSafe(TargetingComputerRackBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
			int light, int overlay) {
		BlockState blockState = be.getBlockState();
		Direction facing = blockState.getValue(ContraptionControlsBlock.FACING)
			.getOpposite();

		ms.pushPose();
		super.renderSafe(be, partialTicks, ms, buffer, light, overlay);
		VertexConsumer vc = buffer.getBuffer(RenderType.solid());
		ms.popPose();
		
		if(!be.container.getItem(0).isEmpty()) {
		ms.pushPose();
		CachedBufferer.partialFacing(NorthstarPartialModels.CPU1, blockState, facing)
			.light(light)
			.renderInto(ms, vc);
		ms.popPose();}
		
		if(!be.container.getItem(1).isEmpty()) {
		ms.pushPose();
		CachedBufferer.partialFacing(NorthstarPartialModels.CPU2, blockState, facing)
			.light(light)
			.renderInto(ms, vc);
		ms.popPose();}
		
		if(!be.container.getItem(2).isEmpty()) {
		ms.pushPose();
		CachedBufferer.partialFacing(NorthstarPartialModels.CPU3, blockState, facing)
			.light(light)
			.renderInto(ms, vc);
		ms.popPose();}
		
		if(!be.container.getItem(3).isEmpty()) {
		ms.pushPose();
		CachedBufferer.partialFacing(NorthstarPartialModels.CPU4, blockState, facing)
			.light(light)
			.renderInto(ms, vc);
		ms.popPose();}
		
		if(!be.container.getItem(4).isEmpty()) {
		ms.pushPose();
		CachedBufferer.partialFacing(NorthstarPartialModels.CPU5, blockState, facing)
			.light(light)
			.renderInto(ms, vc);
		ms.popPose();}
		  
		if(!be.container.getItem(5).isEmpty()) {
		ms.pushPose();
		CachedBufferer.partialFacing(NorthstarPartialModels.CPU6, blockState, facing)
			.light(light)
			.renderInto(ms, vc);
		ms.popPose();}
	}
}
