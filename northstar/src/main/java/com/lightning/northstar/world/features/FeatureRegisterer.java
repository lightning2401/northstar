package com.lightning.northstar.world.features;

import java.util.function.Supplier;


import net.minecraft.world.level.levelgen.feature.Feature;

public class FeatureRegisterer {
    public static <T extends Feature<?>> Supplier<T> registerFeature(String name, Supplier<T> feature) {
        throw new AssertionError();
    }
}
