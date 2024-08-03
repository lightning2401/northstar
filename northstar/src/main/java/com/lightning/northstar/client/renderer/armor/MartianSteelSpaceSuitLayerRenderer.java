package com.lightning.northstar.client.renderer.armor;

import com.lightning.northstar.block.tech.NorthstarPartialModels;
import com.lightning.northstar.item.armor.BrokenIronSpaceSuitArmorItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class MartianSteelSpaceSuitLayerRenderer <T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

	public MartianSteelSpaceSuitLayerRenderer(RenderLayerParent<T, M> pRenderer) {
		super(pRenderer);
	}

	@Override
	public void render(PoseStack ms, MultiBufferSource buffer, int light, T entity,
			float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw,
			float pHeadPitch) {
		if (entity.getPose() == Pose.SLEEPING)
			return;

		Item item = entity.getItemBySlot(EquipmentSlot.HEAD).getItem();
		if (!(item instanceof BrokenIronSpaceSuitArmorItem))
			return;

		M entityModel = getParentModel();
		if (!(entityModel instanceof HumanoidModel))
			return;
		
		HumanoidModel<?> model = (HumanoidModel<?>) entityModel;
		BlockState air = Blocks.AIR.defaultBlockState();
		RenderType renderType = Sheets.translucentCullBlockSheet();
		SuperByteBuffer helmet = CachedBufferer.partial(NorthstarPartialModels.BROKEN_IRON_SPACE_SUIT_HELMET, air);
		
		ms.pushPose();
		
		model.head.translateAndRotate(ms);
		ms.translate(0.5, 1.45, -0.5);
		ms.scale(-1, -1, 1);
		
		helmet.forEntityRender()
		.light(light)
		.renderInto(ms, buffer.getBuffer(renderType));

		ms.popPose();
	}

	public static void registerOnAll(EntityRenderDispatcher renderManager) {
		for (EntityRenderer<? extends Player> renderer : renderManager.getSkinMap().values())
			registerOn(renderer);
		for (EntityRenderer<?> renderer : renderManager.renderers.values())
			registerOn(renderer);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void registerOn(EntityRenderer<?> entityRenderer) {
		if (!(entityRenderer instanceof LivingEntityRenderer))
			return;
		LivingEntityRenderer<?, ?> livingRenderer = (LivingEntityRenderer<?, ?>) entityRenderer;
		if (!(livingRenderer.getModel() instanceof HumanoidModel))
			return;
		BrokenIronSpaceSuitLayerRenderer<?, ?> layer = new BrokenIronSpaceSuitLayerRenderer<>(livingRenderer);
		livingRenderer.addLayer((BrokenIronSpaceSuitLayerRenderer) layer);
	}
}
