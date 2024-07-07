package com.lightning.northstar.block.tech.electrolysis_machine;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour.TankSegment;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import com.simibubi.create.foundation.fluid.FluidRenderer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.util.Mth;
import net.minecraftforge.fluids.FluidStack;

public class ElectrolysisMachineRenderer extends SmartBlockEntityRenderer<ElectrolysisMachineBlockEntity>  {

	public ElectrolysisMachineRenderer(Context context) {
		super(context);
	}
	
	@Override
	protected void renderSafe(ElectrolysisMachineBlockEntity elecMac, float partialTicks, PoseStack ms, MultiBufferSource buffer,
		int light, int overlay) {
		super.renderSafe(elecMac, partialTicks, ms, buffer, light, overlay);
		renderFluids(elecMac, partialTicks, ms, buffer, light, overlay);
	}
	
	protected float renderFluids(ElectrolysisMachineBlockEntity elecMac, float partialTicks, PoseStack ms, MultiBufferSource buffer,
			int light, int overlay) {
			SmartFluidTankBehaviour inputFluids = elecMac.getBehaviour(SmartFluidTankBehaviour.INPUT);
			SmartFluidTankBehaviour outputFluids = elecMac.getBehaviour(SmartFluidTankBehaviour.OUTPUT);
			SmartFluidTankBehaviour[] tanks = { inputFluids, outputFluids };
			float totalUnits = elecMac.getTotalFluidUnits(partialTicks);
			if (totalUnits < 1)
				return 0;

			float fluidLevel = Mth.clamp(totalUnits / 2000, 0, 1);

			fluidLevel = 1 - ((1 - fluidLevel) * (1 - fluidLevel));

			float xMin = 2 / 16f;
			float xMax = 2 / 16f;
			final float yMin = 2 / 16f;
			final float yMax = yMin + 12 / 16f * fluidLevel;
			final float zMin = 2 / 16f;
			final float zMax = 14 / 16f;

			for (SmartFluidTankBehaviour behaviour : tanks) {
				if (behaviour == null)
					continue;
				for (TankSegment tankSegment : behaviour.getTanks()) {
					FluidStack renderedFluid = tankSegment.getRenderedFluid();
					if (renderedFluid.isEmpty())
						continue;
					float units = tankSegment.getTotalUnits(partialTicks);
					if (units < 1)
						continue;

					float partial = Mth.clamp(units / totalUnits, 0, 1);
					xMax += partial * 12 / 16f;
					FluidRenderer.renderFluidBox(renderedFluid, xMin, yMin, zMin, xMax, yMax, zMax, buffer, ms, light,
						false);

					xMin = xMax;
				}
			}

			return yMax;
		}
}
