package com.lightning.northstar.block.entity;

import static com.lightning.northstar.Northstar.REGISTRATE;

import com.lightning.northstar.Northstar;
import com.lightning.northstar.block.NorthstarBlocks;
import com.lightning.northstar.block.NorthstarTechBlocks;
import com.lightning.northstar.block.tech.astronomy_table.AstronomyTableBlockEntity;
import com.lightning.northstar.block.tech.circuit_engraver.CircuitEngraverBlockEntity;
import com.lightning.northstar.block.tech.circuit_engraver.CircuitEngraverRenderer;
import com.lightning.northstar.block.tech.circuit_engraver.EngraverInstance;
import com.lightning.northstar.block.tech.combustion_engine.CombustionEngineBlockEntity;
import com.lightning.northstar.block.tech.combustion_engine.CombustionEngineInstance;
import com.lightning.northstar.block.tech.combustion_engine.CombustionEngineRenderer;
import com.lightning.northstar.block.tech.computer_rack.TargetingComputerRackBlockEntity;
import com.lightning.northstar.block.tech.computer_rack.TargetingComputerRackRenderer;
import com.lightning.northstar.block.tech.electrolysis_machine.ElectrolysisMachineBlockEntity;
import com.lightning.northstar.block.tech.electrolysis_machine.ElectrolysisMachineInstance;
import com.lightning.northstar.block.tech.electrolysis_machine.ElectrolysisMachineRenderer;
import com.lightning.northstar.block.tech.ice_box.IceBoxBlockEntity;
import com.lightning.northstar.block.tech.ice_box.IceBoxRenderer;
import com.lightning.northstar.block.tech.jet_engine.JetEngineBlockEntity;
import com.lightning.northstar.block.tech.jet_engine.JetEngineRenderer;
import com.lightning.northstar.block.tech.oxygen_concentrator.OxygenConcentratorBlockEntity;
import com.lightning.northstar.block.tech.oxygen_concentrator.OxygenConcentratorInstance;
import com.lightning.northstar.block.tech.oxygen_concentrator.OxygenConcentratorRenderer;
import com.lightning.northstar.block.tech.oxygen_filler.OxygenFillerBlockEntity;
import com.lightning.northstar.block.tech.oxygen_filler.OxygenFillerRenderer;
import com.lightning.northstar.block.tech.oxygen_generator.OxygenGeneratorBlockEntity;
import com.lightning.northstar.block.tech.oxygen_generator.OxygenGeneratorInstance;
import com.lightning.northstar.block.tech.oxygen_generator.OxygenGeneratorRenderer;
import com.lightning.northstar.block.tech.rocket_controls.RocketControlsBlockEntity;
import com.lightning.northstar.block.tech.rocket_controls.RocketControlsRenderer;
import com.lightning.northstar.block.tech.rocket_station.RocketStationBlockEntity;
import com.lightning.northstar.block.tech.solar_panel.SolarPanelBlockEntity;
import com.lightning.northstar.block.tech.solar_panel.SolarPanelRenderer;
import com.lightning.northstar.block.tech.telescope.TelescopeBlockEntity;
import com.lightning.northstar.block.tech.temperature_regulator.TemperatureRegulatorBlockEntity;
import com.lightning.northstar.block.tech.temperature_regulator.TemperatureRegulatorInstance;
import com.lightning.northstar.block.tech.temperature_regulator.TemperatureRegulatorRenderer;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlockEntity;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorRenderer;
import com.simibubi.create.content.kinetics.base.ShaftInstance;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class NorthstarBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Northstar.MOD_ID);
    
//    public static final BlockEntityEntry<RocketStationBlockEntity> ROCKET_STATION = BLOCK_ENTITIES.register("rocket_station",
//    		() -> BlockEntityType.Builder.of(RocketStationBlockEntity::new, NorthstarBlocks.ROCKET_STATION.get()).build(null));
    
    public static final RegistryObject<BlockEntityType<TelescopeBlockEntity>> TELESCOPE = BLOCK_ENTITIES.register("telescope",
    		() -> BlockEntityType.Builder.of(TelescopeBlockEntity::new, NorthstarBlocks.TELESCOPE.get()).build(null));
    
    public static final RegistryObject<BlockEntityType<OxygenBubbleGeneratorBlockEntity>> OXYGEN_BUBBLE_GENERATOR = BLOCK_ENTITIES.register("oxygen_bubble_generator",
    		() -> BlockEntityType.Builder.of(OxygenBubbleGeneratorBlockEntity::new, NorthstarBlocks.OXYGEN_BUBBLE_GENERATOR.get()).build(null));
    
    public static final RegistryObject<BlockEntityType<VenusExhaustBlockEntity>> VENUS_EXHAUST = BLOCK_ENTITIES.register("venus_exhaust",
    		() -> BlockEntityType.Builder.of(VenusExhaustBlockEntity::new, NorthstarBlocks.VENUS_PLUME.get()).build(null));
    
    public static final BlockEntityEntry<TemperatureRegulatorBlockEntity> TEMPERATURE_REGULATOR_BLOCK_ENTITY = REGISTRATE
 			.blockEntity("temperature_regulator", TemperatureRegulatorBlockEntity::new)
 			.instance(() -> TemperatureRegulatorInstance::new, false)
 			.validBlocks(NorthstarTechBlocks.TEMPERATURE_REGULATOR)
 			.renderer(() -> TemperatureRegulatorRenderer::new)
 			.register();
    public static final BlockEntityEntry<OxygenGeneratorBlockEntity> OXYGEN_GENERATOR = REGISTRATE
 			.blockEntity("oxygen_generator", OxygenGeneratorBlockEntity::new)
 			.instance(() -> OxygenGeneratorInstance::new, false)
 			.validBlocks(NorthstarTechBlocks.OXYGEN_GENERATOR)
 			.renderer(() -> OxygenGeneratorRenderer::new)
 			.register();
    
   public static final BlockEntityEntry<SolarPanelBlockEntity> SOLAR_PANEL = REGISTRATE
			.blockEntity("solar_panel", SolarPanelBlockEntity::new)
			.instance(() -> ShaftInstance::new, false)
			.validBlocks(NorthstarTechBlocks.SOLAR_PANEL)
 			.renderer(() -> SolarPanelRenderer::new)
			.register();
   
   public static final BlockEntityEntry<CombustionEngineBlockEntity> COMBUSTION_ENGINE = REGISTRATE
			.blockEntity("combustion_engine", CombustionEngineBlockEntity::new)
			.instance(() -> CombustionEngineInstance::new, false)
			.validBlocks(NorthstarTechBlocks.COMBUSTION_ENGINE)
 			.renderer(() -> CombustionEngineRenderer::new)
			.register();
   
   public static final BlockEntityEntry<LaserLenseBlockEntity> LASER_LENSE = REGISTRATE
			.blockEntity("laser_lense", LaserLenseBlockEntity::new)
			.validBlocks(NorthstarTechBlocks.LASER_LENSE)
			.register();
   public static final BlockEntityEntry<AstronomyTableBlockEntity> ASTRONOMY_TABLE = REGISTRATE
			.blockEntity("astronomy_table", AstronomyTableBlockEntity::new)
			.validBlocks(NorthstarTechBlocks.ASTRONOMY_TABLE)
			.register();
   public static final BlockEntityEntry<CircuitEngraverBlockEntity> CIRCUIT_ENGRAVER = REGISTRATE
			.blockEntity("circuit_engraver", CircuitEngraverBlockEntity::new)
			.instance(() -> EngraverInstance::new, false)
			.validBlocks(NorthstarTechBlocks.CIRCUIT_ENGRAVER)
			.renderer(() -> CircuitEngraverRenderer::new)
			.register();
   public static final BlockEntityEntry<OxygenConcentratorBlockEntity> OXYGEN_CONCENTRATOR = REGISTRATE
			.blockEntity("oxygen_concentrator", OxygenConcentratorBlockEntity::new)
			.instance(() -> OxygenConcentratorInstance::new, false)
			.validBlocks(NorthstarTechBlocks.OXYGEN_CONCENTRATOR)
			.renderer(() -> OxygenConcentratorRenderer::new)
			.register();
   public static final BlockEntityEntry<OxygenFillerBlockEntity> OXYGEN_FILLER = REGISTRATE
			.blockEntity("oxygen_filler", OxygenFillerBlockEntity::new)
			.validBlocks(NorthstarTechBlocks.OXYGEN_FILLER)
			.renderer(() -> OxygenFillerRenderer::new)
			.register();
   public static final BlockEntityEntry<ElectrolysisMachineBlockEntity> ELECTROLYSIS_MACHINE = REGISTRATE
			.blockEntity("electrolysis_machine", ElectrolysisMachineBlockEntity::new)
 			.instance(() -> ElectrolysisMachineInstance::new, false)
			.validBlocks(NorthstarTechBlocks.ELECTROLYSIS_MACHINE)
			.renderer(() -> ElectrolysisMachineRenderer::new)
			.register();
   
	public static final BlockEntityEntry<TargetingComputerRackBlockEntity> COMPUTER_RACK = REGISTRATE
			.blockEntity("computer_rack", TargetingComputerRackBlockEntity::new)
//			.instance(() -> RocketControlsInstance::new, false)
			.validBlocks(NorthstarTechBlocks.COMPUTER_RACK)
			.renderer(() -> TargetingComputerRackRenderer::new)
			.register();
   
	public static final BlockEntityEntry<RocketControlsBlockEntity> ROCKET_CONTROLS = REGISTRATE
			.blockEntity("rocket_controls", RocketControlsBlockEntity::new)
//			.instance(() -> RocketControlsInstance::new, false)
			.validBlocks(NorthstarTechBlocks.ROCKET_CONTROLS)
			.renderer(() -> RocketControlsRenderer::new)
			.register();
	
	public static final BlockEntityEntry<JetEngineBlockEntity> JET_ENGINE = REGISTRATE
			.blockEntity("jet_engine", JetEngineBlockEntity::new)
			.renderer(() -> JetEngineRenderer::new)
			.validBlocks(NorthstarTechBlocks.JET_ENGINE)
			.register();
	
	public static final BlockEntityEntry<SlidingDoorBlockEntity> SPACE_DOORS =
			REGISTRATE.blockEntity("space_doors", SlidingDoorBlockEntity::new)
				.renderer(() -> SlidingDoorRenderer::new)
				.validBlocks(NorthstarTechBlocks.IRON_SPACE_DOOR)
				.register();
    
   public static final BlockEntityEntry<IceBoxBlockEntity> ICE_BOX = REGISTRATE
			.blockEntity("ice_box", IceBoxBlockEntity::new)
			.validBlocks(NorthstarTechBlocks.ICE_BOX)
			.renderer(() -> IceBoxRenderer::new)
			.register();
    
	public static final BlockEntityEntry<RocketStationBlockEntity> ROCKET_STATION = REGISTRATE
			.blockEntity("rocket_station", RocketStationBlockEntity::new)
			.validBlocks(NorthstarTechBlocks.ROCKET_STATION)
			.register();

//   public static final RegistryObject<BlockEntityType<JetEngineBlockEntity>> JET_ENGINE = BLOCK_ENTITIES.register("jet_engine",
//   		() -> BlockEntityType.Builder.of(JetEngineBlockEntity::new, NorthstarBlocks.JET_ENGINE.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
