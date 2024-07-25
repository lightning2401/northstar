package com.lightning.northstar.fluids;

import static com.lightning.northstar.Northstar.REGISTRATE;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import com.lightning.northstar.Northstar;
import com.lightning.northstar.NorthstarTags;
import com.lightning.northstar.item.NorthstarCreativeModeTab;
import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Vector3f;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.fluids.VirtualFluid;
import com.simibubi.create.foundation.utility.Color;
import com.tterrag.registrate.builders.FluidBuilder.FluidTypeFactory;
import com.tterrag.registrate.util.entry.FluidEntry;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.FogRenderer.FogMode;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class NorthstarFluids {
	
	static {
		Northstar.REGISTRATE_CUSTOM.creativeModeTab(() -> NorthstarCreativeModeTab.NORTHSTAR_TAB);
	}
	
	//thanks, create, for making this simple :]
	
	
	public static final FluidEntry<VirtualFluid> OXYGEN = REGISTRATE.virtualFluid("oxygen")
			.lang("Oxygen")
			.tag(AllTags.forgeFluidTag("oxygen"))
			.register();
	public static final FluidEntry<VirtualFluid> HYDROGEN = REGISTRATE.virtualFluid("hydrogen")
			.lang("Hydrogen")
			.tag(AllTags.forgeFluidTag("hydrogen"))
			.register();
	public static final FluidEntry<VirtualFluid> CHOCOLATE_ICE_CREAM = REGISTRATE.virtualFluid("chocolate_ice_cream")
			.lang("Chocolate Ice Cream")
			.tag(AllTags.forgeFluidTag("chocolate_ice_cream"))
			.register();
	public static final FluidEntry<VirtualFluid> VANILLA_ICE_CREAM = REGISTRATE.virtualFluid("vanilla_ice_cream")
			.lang("Vanilla Ice Cream")
			.tag(AllTags.forgeFluidTag("vanilla_ice_cream"))
			.register();
	public static final FluidEntry<VirtualFluid> STRAWBERRY_ICE_CREAM = REGISTRATE.virtualFluid("strawberry_ice_cream")
			.lang("Strawberry Ice Cream")
			.tag(AllTags.forgeFluidTag("strawberry_ice_cream"))
			.register();
	
	public static final FluidEntry<ForgeFlowingFluid.Flowing> LIQUID_HYDROGEN =
			REGISTRATE.standardFluid("liquid_hydrogen",
					SolidRenderedPlaceableFluidType.create(0xa59999,
						() -> 1f / 8f * 0.8f))
				.lang("Liquid Hydrogen")
				.properties(b -> b.viscosity(2000)
					.density(1400))
				.fluidProperties(p -> p.levelDecreasePerBlock(1)
					.tickRate(5)
					.slopeFindDistance(3)
					.explosionResistance(100f))
				.source(ForgeFlowingFluid.Source::new)
				.bucket().properties(t -> t.craftRemainder(Items.BUCKET))
				.build()
				.register();
	public static final FluidEntry<ForgeFlowingFluid.Flowing> LIQUID_OXYGEN =
			REGISTRATE.standardFluid("liquid_oxygen",
					SolidRenderedPlaceableFluidType.create(0x96AFAF,
						() -> 1f / 8f * 0.8f))
				.lang("Liquid Oxygen")
				.properties(b -> b.viscosity(2000)
					.density(1400))
				.fluidProperties(p -> p.levelDecreasePerBlock(1)
					.tickRate(5)
					.slopeFindDistance(3)
					.explosionResistance(100f))
				.source(ForgeFlowingFluid.Source::new)
				.bucket().properties(t -> t.craftRemainder(Items.BUCKET))
				.build()
				.register();
	
	public static final FluidEntry<ForgeFlowingFluid.Flowing> METHANE =
			REGISTRATE.standardFluid("methane",
					SolidRenderedPlaceableFluidType.create(0x41E08E,
						() -> 1f / 8f * 0.8f))
				.lang("Methane")
				.properties(b -> b.viscosity(2000)
					.density(1400))
				.fluidProperties(p -> p.levelDecreasePerBlock(1)
					.tickRate(5)
					.slopeFindDistance(3)
					.explosionResistance(100f))
				.source(ForgeFlowingFluid.Source::new)
				.tag(NorthstarTags.NorthstarFluidTags.TIER_1_ROCKET_FUEL.tag)
				.bucket()
				.build()
				.register();
	public static final FluidEntry<ForgeFlowingFluid.Flowing> SULFURIC_ACID =
			REGISTRATE.standardFluid("sulfuric_acid",
					SolidRenderedPlaceableFluidType.create(0xA5EC00,
						() -> 1f / 8f * 0.8f))
				.lang("Sulfuric Acid")
				.properties(b -> b.viscosity(2000)
					.density(700))
				.fluidProperties(p -> p.levelDecreasePerBlock(1)
					.tickRate(5)
					.slopeFindDistance(3)
					.explosionResistance(100f))
				.source(ForgeFlowingFluid.Source::new)
				.bucket()
				.build()
				.register();
	public static final FluidEntry<ForgeFlowingFluid.Flowing> HYDROCARBON =
			REGISTRATE.standardFluid("hydrocarbon",
					SolidRenderedPlaceableFluidType.create(0x070505,
						() -> 1f / 8f * 0.25f))
				.lang("Hydrocarbon")
				.properties(b -> b.viscosity(1000)
					.density(1400))
				.fluidProperties(p -> p.levelDecreasePerBlock(2)
					.tickRate(25)
					.slopeFindDistance(3)
					.explosionResistance(100f))
				.source(ForgeFlowingFluid.Source::new)
				.tag(NorthstarTags.NorthstarFluidTags.TIER_1_ROCKET_FUEL.tag)
				.bucket()
				.build()
				.register();
	
	
	
	
	public static void register() {}

	public static abstract class TintedFluidType extends FluidType {

		protected static final int NO_TINT = 0xffffffff;
		private ResourceLocation stillTexture;
		private ResourceLocation flowingTexture;

		public TintedFluidType(Properties properties, ResourceLocation stillTexture, ResourceLocation flowingTexture) {
			super(properties);
			this.stillTexture = stillTexture;
			this.flowingTexture = flowingTexture;
		}

		@Override
		public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
			consumer.accept(new IClientFluidTypeExtensions() {

				@Override
				public ResourceLocation getStillTexture() {
					return stillTexture;
				}

				@Override
				public ResourceLocation getFlowingTexture() {
					return flowingTexture;
				}

				@Override
				public int getTintColor(FluidStack stack) {
					return TintedFluidType.this.getTintColor(stack);
				}

				@Override
				public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
					return TintedFluidType.this.getTintColor(state, getter, pos);
				}
				
				@Override
				public @NotNull Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level,
					int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
					Vector3f customFogColor = TintedFluidType.this.getCustomFogColor();
					return customFogColor == null ? fluidFogColor : customFogColor;
				}

				@Override
				public void modifyFogRender(Camera camera, FogMode mode, float renderDistance, float partialTick,
					float nearDistance, float farDistance, FogShape shape) {
					float modifier = TintedFluidType.this.getFogDistanceModifier();
					float baseWaterFog = 96.0f;
					if (modifier != 1f) {
						RenderSystem.setShaderFogShape(FogShape.CYLINDER);
						RenderSystem.setShaderFogStart(-8);
						RenderSystem.setShaderFogEnd(baseWaterFog * modifier);
					}
				}

			});
		}

		protected abstract int getTintColor(FluidStack stack);

		protected abstract int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos);
		
		protected Vector3f getCustomFogColor() {
			return null;
		}
		
		protected float getFogDistanceModifier() {
			return 1f;
		}

	}

	private static class SolidRenderedPlaceableFluidType extends TintedFluidType {

		private Vector3f fogColor;
		private Supplier<Float> fogDistance;

		public static FluidTypeFactory create(int fogColor, Supplier<Float> fogDistance) {
			return (p, s, f) -> {
				SolidRenderedPlaceableFluidType fluidType = new SolidRenderedPlaceableFluidType(p, s, f);
				fluidType.fogColor = new Color(fogColor, false).asVectorF();
				fluidType.fogDistance = fogDistance;
				return fluidType;
			};
		}

		private SolidRenderedPlaceableFluidType(Properties properties, ResourceLocation stillTexture,
			ResourceLocation flowingTexture) {
			super(properties, stillTexture, flowingTexture);
		}

		@Override
		protected int getTintColor(FluidStack stack) {
			return NO_TINT;
		}

		/*
		 * Removing alpha from tint prevents optifine from forcibly applying biome
		 * colors to modded fluids (this workaround only works for fluids in the solid
		 * render layer)
		 */
		@Override
		public int getTintColor(FluidState state, BlockAndTintGetter world, BlockPos pos) {
			return 0x00ffffff;
		}
		
		@Override
		protected Vector3f getCustomFogColor() {
			return fogColor;
		}
		
		@Override
		protected float getFogDistanceModifier() {
			return fogDistance.get();
		}

	}


}
