package com.lightning.northstar.world.features.trunkplacers;

import com.lightning.northstar.Northstar;

import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class NorthstarTrunkPlacerTypes<P extends TrunkPlacer> {
    public static final DeferredRegister<TrunkPlacerType<?>> TRUNK_PLACER_TYPES =
            DeferredRegister.create(Registry.TRUNK_PLACER_TYPE_REGISTRY, Northstar.MOD_ID);
    
	@SuppressWarnings({"unchecked", "rawtypes" })
	public static final RegistryObject<TrunkPlacerType<?>> ARGYRE_TRUNK_PLACER = TRUNK_PLACER_TYPES.register("argyre_trunk_placer", () -> new TrunkPlacerType(ArgyreTrunkPlacer.CODEC));
	@SuppressWarnings({"unchecked", "rawtypes" })
	public static final RegistryObject<TrunkPlacerType<?>> ARGYRE_CEILING_TRUNK_PLACER = TRUNK_PLACER_TYPES.register("argyre_ceiling_trunk_placer", () -> new TrunkPlacerType(ArgyreTrunkPlacer.CODEC));
	@SuppressWarnings({"unchecked", "rawtypes" })
	public static final RegistryObject<TrunkPlacerType<?>> BLOOM_FUNGUS_TRUNK_PLACER = TRUNK_PLACER_TYPES.register("bloom_fungus_trunk_placer", () -> new TrunkPlacerType(BloomTrunkPlacer.CODEC));
	@SuppressWarnings({"unchecked", "rawtypes" })
	public static final RegistryObject<TrunkPlacerType<?>> ROOF_BLOOM_FUNGUS_TRUNK_PLACER = TRUNK_PLACER_TYPES.register("roof_bloom_fungus_trunk_placer", () -> new TrunkPlacerType(RoofBloomTrunkPlacer.CODEC));
	@SuppressWarnings({"unchecked", "rawtypes" })
	public static final RegistryObject<TrunkPlacerType<?>> SPIKE_FUNGUS_TRUNK_PLACER = TRUNK_PLACER_TYPES.register("spike_fungus_trunk_placer", () -> new TrunkPlacerType(SpikeTrunkPlacer.CODEC));
	@SuppressWarnings({"unchecked", "rawtypes" })
	public static final RegistryObject<TrunkPlacerType<?>> PLATE_FUNGUS_TRUNK_PLACER = TRUNK_PLACER_TYPES.register("plate_fungus_trunk_placer", () -> new TrunkPlacerType(PlateTrunkPlacer.CODEC));
	@SuppressWarnings({"unchecked", "rawtypes" })
	public static final RegistryObject<TrunkPlacerType<?>> ROOF_PLATE_FUNGUS_TRUNK_PLACER = TRUNK_PLACER_TYPES.register("roof_plate_fungus_trunk_placer", () -> new TrunkPlacerType(RoofPlateTrunkPlacer.CODEC));
	@SuppressWarnings({"unchecked", "rawtypes" })
	public static final RegistryObject<TrunkPlacerType<?>> TOWER_FUNGUS_TRUNK_PLACER = TRUNK_PLACER_TYPES.register("tower_fungus_trunk_placer", () -> new TrunkPlacerType(TowerTrunkPlacer.CODEC));
	@SuppressWarnings({"unchecked", "rawtypes" })
	public static final RegistryObject<TrunkPlacerType<?>> ROOF_TOWER_FUNGUS_TRUNK_PLACER = TRUNK_PLACER_TYPES.register("roof_tower_fungus_trunk_placer", () -> new TrunkPlacerType(RoofTowerTrunkPlacer.CODEC));
	@SuppressWarnings({"unchecked", "rawtypes" })
	public static final RegistryObject<TrunkPlacerType<?>> CALORIAN_VINES_TRUNK_PLACER = TRUNK_PLACER_TYPES.register("calorian_vines_trunk_placer", () -> new TrunkPlacerType(TestSaplingTrunkPlacer.CODEC));
	   
	public static void register(IEventBus eventBus) {
		TRUNK_PLACER_TYPES.register(eventBus);
	}
}
