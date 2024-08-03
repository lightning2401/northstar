package com.lightning.northstar.world.dimension;

import com.lightning.northstar.Northstar;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

public class NorthstarDimensions {
	//earth orbit
	public static final ResourceKey<Level> EARTH_ORBIT_DIM_KEY = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(Northstar.MOD_ID, "earth_orbit"));
	public static final ResourceKey<DimensionType> EARTH_ORBIT_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE, new ResourceLocation(Northstar.MOD_ID, "earth_orbit"));
	// mars
	public static final ResourceKey<Level> MARS_DIM_KEY = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(Northstar.MOD_ID, "mars"));
	public static final ResourceKey<DimensionType> MARS_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE, new ResourceLocation(Northstar.MOD_ID, "mars"));
	// venus
	public static final ResourceKey<Level> VENUS_DIM_KEY = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(Northstar.MOD_ID, "venus"));
	public static final ResourceKey<DimensionType> VENUS_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE, new ResourceLocation(Northstar.MOD_ID, "venus"));
	// moon
	public static final ResourceKey<Level> MOON_DIM_KEY = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(Northstar.MOD_ID, "moon"));
	public static final ResourceKey<DimensionType> MOON_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE, new ResourceLocation(Northstar.MOD_ID, "moon"));
	// mercury
	public static final ResourceKey<Level> MERCURY_DIM_KEY = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(Northstar.MOD_ID, "mercury"));
	public static final ResourceKey<DimensionType> MERCURY_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE, new ResourceLocation(Northstar.MOD_ID, "mercury"));
	
	public static void register() {
		System.out.println("Registering dimensions for " + Northstar.MOD_ID);
	}

}
