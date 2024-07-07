package com.lightning.northstar.client.dimension.renderer;
import com.lightning.northstar.client.dimension.renderer.base.DimensionSettings;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.minecraft.Util;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class NorthstarDimensionSpecialEffects implements DimensionSettings{

private static final Object2ObjectMap<ResourceLocation, DimensionSpecialEffects> NORTHSTAR_EFFECTS = Util.make(new Object2ObjectArrayMap<>(), (p_108881_) -> {
    NorthstarDimensionSpecialEffects.MarsEffects dimensionspecialeffects$marseffects = new NorthstarDimensionSpecialEffects.MarsEffects();
 
	});
		@OnlyIn(Dist.CLIENT)
			public static class MarsEffects extends DimensionSpecialEffects {
			public static final int CLOUD_LEVEL = 192;

			public MarsEffects() {
				super(192.0F, true, DimensionSpecialEffects.SkyType.NONE, false, false);
			}

			public Vec3 getBrightnessDependentFogColor(Vec3 p_108908_, float p_108909_) {
				return p_108908_.multiply((double)(p_108909_ * 0.94F + 0.06F), (double)(p_108909_ * 0.94F + 0.06F), (double)(p_108909_ * 0.91F + 0.09F));
			}

			public boolean isFoggyAt(int p_108905_, int p_108906_) {
				return false;
			}
		    public static void registerDimensionEffects(ResourceKey<Level> id, DimensionSpecialEffects effects) {
		        NORTHSTAR_EFFECTS.put(id.location(), effects);
		    }
}
}
