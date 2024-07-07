package com.lightning.northstar.world.features.foliageplacers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

public class CoilerFoliagePlacer extends FoliagePlacer {
   public static final Codec<CoilerFoliagePlacer> CODEC = RecordCodecBuilder.create((p_68380_) -> {
      return foliagePlacerParts(p_68380_).apply(p_68380_, CoilerFoliagePlacer::new);
   });

   public CoilerFoliagePlacer(IntProvider int1, IntProvider int2) {
      super(int1, int2);
   }

   protected FoliagePlacerType<?> type() {
      return FoliagePlacerType.ACACIA_FOLIAGE_PLACER;
   }

   protected void createFoliage(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blocksetter, RandomSource pRandom, TreeConfiguration pConfig, int int1, FoliagePlacer.FoliageAttachment foliage, int int2, int int3, int int4) {
      boolean flag = foliage.doubleTrunk();
      BlockPos blockpos = foliage.pos().above(int4);
      this.placeLeavesRow(level, blocksetter, pRandom, pConfig, blockpos, int3 + foliage.radiusOffset(), -1 - int2, flag);
      this.placeLeavesRow(level, blocksetter, pRandom, pConfig, blockpos, int3 - 1, -int2, flag);
      this.placeLeavesRow(level, blocksetter, pRandom, pConfig, blockpos, int3 - 1, -2 -int2, flag);
      this.placeLeavesRow(level, blocksetter, pRandom, pConfig, blockpos, int3 - 3, 1 -int2, flag);
   }

   public int foliageHeight(RandomSource pRandom, int p_225496_, TreeConfiguration p_225497_) {
      return 0;
   }

   /**
    * Skips certain positions based on the provided shape, such as rounding corners randomly.
    * The coordinates are passed in as absolute value, and should be within [0, {@code range}].
    */
   protected boolean shouldSkipLocation(RandomSource pRandom, int p_225489_, int p_225490_, int p_225491_, int p_225492_, boolean p_225493_) {
      if (p_225490_ == 0) {
         return (p_225489_ > 1 || p_225491_ > 1) && p_225489_ != 0 && p_225491_ != 0;
      } else {
         return p_225489_ == p_225492_ && p_225491_ == p_225492_ && p_225492_ > 0;
      }
   }
}