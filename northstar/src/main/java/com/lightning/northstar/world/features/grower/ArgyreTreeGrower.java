package com.lightning.northstar.world.features.grower;

import javax.annotation.Nullable;

import com.lightning.northstar.world.features.NorthstarConfiguredFeatures;

import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class ArgyreTreeGrower extends AbstractTreeGrower {
    @Nullable
    @Override
    protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource pRandom, boolean pLargeHive) {
        return NorthstarConfiguredFeatures.ARGYRE.getHolder().get();
    }
}
