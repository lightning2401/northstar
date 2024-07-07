package com.lightning.northstar.block.tech.oxygen_filler;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.VecHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class OxygenFillerRenderer extends SmartBlockEntityRenderer<OxygenFillerBlockEntity>  {

	public OxygenFillerRenderer(Context context) {
		super(context);
	}
	
	@Override
	protected void renderSafe(OxygenFillerBlockEntity oxyFiller, float partialTicks, PoseStack ms, MultiBufferSource buffer,
		int light, int overlay) {
		super.renderSafe(oxyFiller, partialTicks, ms, buffer, light, overlay);
		ms.pushPose();

		BlockPos pos = oxyFiller.getBlockPos();

		BlockState blockState = oxyFiller.getBlockState();
		if (!(blockState.getBlock() instanceof OxygenFillerBlock))
			return;
		Direction direction = blockState.getValue(OxygenFillerBlock.HORIZONTAL_FACING);
		if (direction == Direction.DOWN)
			{return;}
		switch(direction) {
		case NORTH: ms.translate(.5, .3f, 0.25f); break;
		case SOUTH: ms.translate(.5, .3f, 0.75f); break;
		case EAST: 	ms.translate(0.75, .3f, 0.5f); break;
		case WEST:	ms.translate(0.25, .3f, 0.5f); break;
		default:
			break;
		}

        TransformStack.cast(ms).rotateY(AngleHelper.horizontalAngle(direction));

		RandomSource r = RandomSource.create(pos.hashCode());

		Container inv = oxyFiller.container;
		int itemCount = 0;
		for (int slot = 0; slot < inv.getContainerSize(); slot++)
			if (!inv.getItem(slot)
				.isEmpty())
				itemCount++;

		float anglePartition = 360f;
		for (int slot = 0; slot < inv.getContainerSize(); slot++) {
			ItemStack stack = inv.getItem(slot);
			if (stack.isEmpty())
				continue;

			ms.pushPose();

			Vec3 itemPosition = VecHelper.rotate(Vec3.ZERO, anglePartition * itemCount, Axis.Y);
			ms.translate(itemPosition.x, itemPosition.y, itemPosition.z);


			for (int i = 0; i <= stack.getCount() / 8; i++) {
				ms.pushPose();

				Vec3 vec = VecHelper.offsetRandomly(Vec3.ZERO, r, 1 / 16f);

				ms.translate(vec.x, vec.y, vec.z);
				renderItem(ms, buffer, light, overlay, stack);
				ms.popPose();
			}
			ms.popPose();

			itemCount--;
		}
		ms.popPose();
	}	
	
	protected void renderItem(PoseStack ms, MultiBufferSource buffer, int light, int overlay, ItemStack stack) {
		Minecraft.getInstance()
			.getItemRenderer()
			.renderStatic(stack, TransformType.GROUND, light, overlay, ms, buffer, 0);
	}


}
