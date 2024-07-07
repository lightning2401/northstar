package com.lightning.northstar.world.features.trunkplacers;

import com.mojang.serialization.Codec;

import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

public class AlienTrunkPlacerType<P extends TrunkPlacer> {
	
	   public static final TrunkPlacerType<CoilerTrunkPlacer> COILER_TRUNK_PLACER = register("coiler_trunk_placer", CoilerTrunkPlacer.CODEC);
	   public static final TrunkPlacerType<WilterTrunkPlacer> WILTER_TRUNK_PLACER = register("wilter_trunk_placer", WilterTrunkPlacer.CODEC);
	   public static final TrunkPlacerType<ArgyreTrunkPlacer> ARGYRE_TRUNK_PLACER = register("argyre_trunk_placer", ArgyreTrunkPlacer.CODEC);
	   private final Codec<P> codec;
	
	   private static <P extends TrunkPlacer> TrunkPlacerType<P> register(String pKey, Codec<P> pCodec) {
		      return Registry.register(Registry.TRUNK_PLACER_TYPES, pKey, new TrunkPlacerType<>(pCodec));
	   }

	public AlienTrunkPlacerType(Codec<P> pCodec) {
		      this.codec = pCodec;
	   }

	   public Codec<P> codec() {
		      return this.codec();
	   }
}