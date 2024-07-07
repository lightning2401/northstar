package com.lightning.northstar.block.tech.jet_engine;

import javax.annotation.Nullable;

import com.jozufozu.flywheel.core.virtual.VirtualRenderWorld;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.render.ContraptionMatrices;
import com.simibubi.create.foundation.block.render.SpriteShiftEntry;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;

import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class JetEngineRenderer extends SafeBlockEntityRenderer<JetEngineBlockEntity>  {
	
	public JetEngineRenderer(BlockEntityRendererProvider.Context context) {}

	@Override
	protected void renderSafe(JetEngineBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource,
			int light, int overlay) {}
	
	public static void renderInContraption(MovementContext context, VirtualRenderWorld renderWorld,
			ContraptionMatrices matrices, MultiBufferSource bufferSource, LerpedFloat headAngle, boolean conductor) {
			BlockState state = context.state;
			Boolean doJetTrail = state.getValue(JetEngineBlock.BOTTOM);
			if (!doJetTrail)
				return;

			Level level = context.world;
			float horizontalAngle = AngleHelper.rad(headAngle.getValue(AnimationTickHolder.getPartialTicks(level)));
			int hashCode = context.hashCode();

			renderShared(matrices.getViewProjection(), matrices.getModel(), bufferSource,
				level, state, doJetTrail, 0, horizontalAngle, hashCode);
		}
	private static void renderShared(PoseStack ms, @Nullable PoseStack modelTransform, MultiBufferSource bufferSource,
			Level level, BlockState blockState, Boolean doJetTrail, float animation, float horizontalAngle, int hashCode) {
		ms.pushPose();
		float time = AnimationTickHolder.getRenderTime(level);
		VertexConsumer cutout = bufferSource.getBuffer(RenderType.cutoutMipped());
		
		SpriteShiftEntry spriteShift = AllSpriteShifts.SUPER_BURNER_FLAME;

			float spriteWidth = spriteShift.getTarget()
				.getU1()
				- spriteShift.getTarget()
					.getU0();

			float spriteHeight = spriteShift.getTarget()
				.getV1()
				- spriteShift.getTarget()
					.getV0();
			
			float speed = 1 / 32f + 1 / 64f;
			
			double vScroll = speed * time;
			vScroll = vScroll - Math.floor(vScroll);
			vScroll = vScroll * spriteHeight / 2;
			
			double uScroll = speed * time / 2;
			uScroll = uScroll - Math.floor(uScroll);
			uScroll = uScroll * spriteWidth / 2;
		
		SuperByteBuffer flameBuffer = CachedBufferer.partial(AllPartialModels.BLAZE_BURNER_FLAME, blockState);
		if (modelTransform != null)
			flameBuffer.transform(modelTransform);
		flameBuffer.shiftUVScrolling(spriteShift, (float) uScroll, (float) vScroll);
		draw(flameBuffer, horizontalAngle, ms, cutout);
		
		ms.popPose();		
	}
	
	private static void draw(SuperByteBuffer buffer, float horizontalAngle, PoseStack ms, VertexConsumer vc) {
		buffer.rotateCentered(Direction.UP, horizontalAngle)
			.light(LightTexture.FULL_BRIGHT)
			.renderInto(ms, vc);
	}

}
