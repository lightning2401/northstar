package com.lightning.northstar.world.features.grower;

import javax.annotation.Nullable;

import com.lightning.northstar.world.features.NorthstarConfiguredFeatures;

import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class MercuryCactusGrower extends AbstractTreeGrower {
    @Nullable
    @Override
    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource pRandom, boolean pLargeHive) {
        return NorthstarConfiguredFeatures.MERCURY_CACTUS;
    }
}
