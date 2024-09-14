package com.lightning.northstar.block;

import static com.lightning.northstar.Northstar.REGISTRATE;
import static com.simibubi.create.AllInteractionBehaviours.interactionBehaviour;
import static com.simibubi.create.AllMovementBehaviours.movementBehaviour;
import static com.simibubi.create.content.redstone.displayLink.AllDisplayBehaviours.assignDataBehaviour;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

import com.lightning.northstar.block.tech.astronomy_table.AstronomyTableBlock;
import com.lightning.northstar.block.tech.circuit_engraver.CircuitEngraverBlock;
import com.lightning.northstar.block.tech.cogs.CustomCogBlock;
import com.lightning.northstar.block.tech.combustion_engine.CombustionEngineBlock;
import com.lightning.northstar.block.tech.computer_rack.TargetingComputerRackBlock;
import com.lightning.northstar.block.tech.electrolysis_machine.ElectrolysisMachineBlock;
import com.lightning.northstar.block.tech.ice_box.IceBoxBlock;
import com.lightning.northstar.block.tech.jet_engine.JetEngineBlock;
import com.lightning.northstar.block.tech.jet_engine.JetEngineItem;
import com.lightning.northstar.block.tech.jet_engine.JetEngineMovementBehaviour;
import com.lightning.northstar.block.tech.oxygen_concentrator.OxygenConcentratorBlock;
import com.lightning.northstar.block.tech.oxygen_detector.OxygenDetectorBlock;
import com.lightning.northstar.block.tech.oxygen_filler.OxygenFillerBlock;
import com.lightning.northstar.block.tech.oxygen_generator.OxygenGeneratorBlock;
import com.lightning.northstar.block.tech.rocket_controls.RocketControlsBlock;
import com.lightning.northstar.block.tech.rocket_controls.RocketControlsInteractionBehaviour;
import com.lightning.northstar.block.tech.rocket_controls.RocketControlsMovementBehaviour;
import com.lightning.northstar.block.tech.rocket_station.RocketStationBlock;
import com.lightning.northstar.block.tech.rocket_station.RocketStationBlockMovingInteraction;
import com.lightning.northstar.block.tech.solar_panel.SolarPanelBlock;
import com.lightning.northstar.block.tech.temperature_regulator.TemperatureRegulatorBlock;
import com.lightning.northstar.item.NorthstarCreativeModeTab;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockModel;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import com.simibubi.create.content.kinetics.simpleRelays.CogwheelBlockItem;
import com.simibubi.create.content.processing.AssemblyOperatorBlockItem;
import com.simibubi.create.content.processing.basin.BasinMovementBehaviour;
import com.simibubi.create.content.redstone.displayLink.source.StationSummaryDisplaySource;
import com.simibubi.create.content.redstone.displayLink.source.TrainStatusDisplaySource;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.BuilderTransformers;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MaterialColor;

public class NorthstarTechBlocks {
	static {
		REGISTRATE.creativeModeTab(() -> NorthstarCreativeModeTab.NORTHSTAR_TECH);
	}	
	@SuppressWarnings("removal")
	public static final BlockEntry<SolarPanelBlock> SOLAR_PANEL = REGISTRATE.block("solar_panel", SolarPanelBlock::new)
			.initialProperties(SharedProperties::stone)
			.properties(p -> p.color(MaterialColor.CLAY))
			.properties(BlockBehaviour.Properties::noOcclusion)
			.transform(axeOrPickaxe())
			.blockstate(
				(c, p) -> BlockStateGen.directionalBlockIgnoresWaterlogged(c, p, s -> AssetLookup.partialBaseModel(c, p)))
			.addLayer(() -> RenderType::cutoutMipped)
			.transform(BlockStressDefaults.setCapacity(128.0))
			.transform(BlockStressDefaults.setGeneratorSpeed(SolarPanelBlock::getSpeedRange))
			.item()
			.transform(customItemModel())
			.register();
    @SuppressWarnings("removal")
	public static final BlockEntry<LaserLenseBlock> LASER_LENSE = REGISTRATE.block("laser_lense", LaserLenseBlock::new)
    		.initialProperties(SharedProperties::softMetal)
			.properties(p -> p.color(MaterialColor.COLOR_GRAY).noOcclusion())
			.properties(p -> p.sound(SoundType.COPPER))
    		.transform(pickaxeOnly())
    		.addLayer(() -> RenderType::cutoutMipped)
    		.simpleItem()
    		
    		.register();
    public static final BlockEntry<LaserBlock> LASER = REGISTRATE.block("laser", LaserBlock::new)
    		.initialProperties(SharedProperties::softMetal)
			.properties(p -> p.color(MaterialColor.COLOR_RED).noCollission().lightLevel((p_50872_) -> {return 15;}).noOcclusion())
			.simpleItem()
			.register();
    public static final BlockEntry<CircuitEngraverBlock> CIRCUIT_ENGRAVER = REGISTRATE.block("circuit_engraver", CircuitEngraverBlock::new)
    		.initialProperties(SharedProperties::softMetal)
    		.properties(p -> p.color(MaterialColor.COLOR_BLACK).isViewBlocking(NorthstarTechBlocks::never))
			.properties(BlockBehaviour.Properties::noOcclusion)
			.transform(axeOrPickaxe())
			.blockstate(BlockStateGen.horizontalBlockProvider(true))
			.transform(BlockStressDefaults.setImpact(8.0))
			.item(AssemblyOperatorBlockItem::new)
			.transform(customItemModel())
    		.register();
    @SuppressWarnings("removal")
	public static final BlockEntry<OxygenConcentratorBlock> OXYGEN_CONCENTRATOR = REGISTRATE.block("oxygen_concentrator", OxygenConcentratorBlock::new)
    		.initialProperties(SharedProperties::softMetal)
    		.properties(p -> p.color(MaterialColor.COLOR_GRAY).isViewBlocking(NorthstarTechBlocks::never)
    				.strength(6, 6))
			.properties(BlockBehaviour.Properties::noOcclusion)
			.transform(axeOrPickaxe())
			.blockstate(BlockStateGen.horizontalBlockProvider(true))
			.transform(BlockStressDefaults.setImpact(16.0))
			.addLayer(() -> RenderType::cutoutMipped)
			.simpleItem()
    		.register();
    @SuppressWarnings("removal")
	public static final BlockEntry<OxygenFillerBlock> OXYGEN_FILLER = REGISTRATE.block("oxygen_filler", OxygenFillerBlock::new)
    		.initialProperties(SharedProperties::softMetal)
    		.properties(p -> p.color(MaterialColor.COLOR_GRAY).isViewBlocking(NorthstarTechBlocks::never)
    				.strength(6, 6))
			.properties(BlockBehaviour.Properties::noOcclusion)
			.transform(axeOrPickaxe())
			.blockstate(BlockStateGen.horizontalBlockProvider(true))
			.addLayer(() -> RenderType::cutoutMipped)
			.simpleItem()
    		.register();
    
    @SuppressWarnings("removal")
	public static final BlockEntry<TemperatureRegulatorBlock> TEMPERATURE_REGULATOR = REGISTRATE.block("temperature_regulator", TemperatureRegulatorBlock::new)
    		.initialProperties(SharedProperties::softMetal)
    		.properties(p -> p.color(MaterialColor.COLOR_GRAY).isViewBlocking(NorthstarTechBlocks::never)
    				.strength(8, 8))
			.properties(BlockBehaviour.Properties::noOcclusion)
			.transform(axeOrPickaxe())
			.blockstate(BlockStateGen.horizontalBlockProvider(true))
			.transform(BlockStressDefaults.setImpact(16.0))
			.addLayer(() -> RenderType::cutoutMipped)
			.simpleItem()
    		.register();
    @SuppressWarnings("removal")
	public static final BlockEntry<OxygenGeneratorBlock> OXYGEN_GENERATOR = REGISTRATE.block("oxygen_generator", OxygenGeneratorBlock::new)
    		.initialProperties(SharedProperties::softMetal)
    		.properties(p -> p.color(MaterialColor.COLOR_GRAY).isViewBlocking(NorthstarTechBlocks::never)
    				.strength(8, 8))
			.properties(BlockBehaviour.Properties::noOcclusion)
			.transform(axeOrPickaxe())
			.blockstate(BlockStateGen.horizontalBlockProvider(true))
			.transform(BlockStressDefaults.setImpact(16.0))
			.addLayer(() -> RenderType::cutoutMipped)
			.simpleItem()
    		.register();
    
    @SuppressWarnings("removal")
	public static final BlockEntry<CombustionEngineBlock> COMBUSTION_ENGINE = REGISTRATE.block("combustion_engine", CombustionEngineBlock::new)
    		.initialProperties(SharedProperties::softMetal)
    		.properties(p -> p.color(MaterialColor.COLOR_GRAY).isViewBlocking(NorthstarTechBlocks::never)
    				.strength(8, 8))
			.properties(BlockBehaviour.Properties::noOcclusion)
			.transform(axeOrPickaxe())
			.blockstate(BlockStateGen.horizontalBlockProvider(true))
			.transform(BlockStressDefaults.setCapacity(256.0))
			.transform(BlockStressDefaults.setGeneratorSpeed(CombustionEngineBlock::getSpeedRange))
			.addLayer(() -> RenderType::cutoutMipped)
			.simpleItem()
    		.register();
    @SuppressWarnings("removal")
	public static final BlockEntry<JetEngineBlock> JET_ENGINE = REGISTRATE.block("jet_engine", JetEngineBlock::new)
    		.initialProperties(SharedProperties::softMetal)
    		.properties(p -> p.color(MaterialColor.COLOR_LIGHT_GRAY).isViewBlocking(NorthstarTechBlocks::never))
			.properties(BlockBehaviour.Properties::noOcclusion)
			.transform(pickaxeOnly())
			.addLayer(() -> RenderType::cutoutMipped)
			.onRegister(movementBehaviour(new JetEngineMovementBehaviour()))
			.item(JetEngineItem::new)
			.build()
    		.register();
    
    public static final BlockEntry<AstronomyTableBlock> ASTRONOMY_TABLE = REGISTRATE.block("astronomy_table", AstronomyTableBlock::new)
    		.initialProperties(SharedProperties::softMetal)
			.properties(p -> p.color(MaterialColor.COLOR_GRAY).noOcclusion())
			.properties(p -> p.sound(SoundType.WOOD))
    		.transform(pickaxeOnly())
    		.simpleItem()
    		.register();

	@SuppressWarnings("removal")
	public static final BlockEntry<IceBoxBlock> ICE_BOX = REGISTRATE.block("ice_box", IceBoxBlock::new)
			.initialProperties(SharedProperties::stone)
			.properties(p -> p.color(MaterialColor.COLOR_GRAY))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.transform(pickaxeOnly())
			.addLayer(() -> RenderType::cutoutMipped)
			.onRegister(movementBehaviour(new BasinMovementBehaviour()))
			.item()
			.transform(customItemModel("_", "block"))
			.register();
	@SuppressWarnings("removal")
	public static final BlockEntry<ElectrolysisMachineBlock> ELECTROLYSIS_MACHINE = REGISTRATE.block("electrolysis_machine", ElectrolysisMachineBlock::new)
			.initialProperties(SharedProperties::stone)
			.properties(p -> p.color(MaterialColor.COLOR_GRAY).isViewBlocking(NorthstarTechBlocks::never))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK).noOcclusion())
			.transform(pickaxeOnly())
			.addLayer(() -> RenderType::cutoutMipped)
			.item()
			.transform(customItemModel("_", "block"))
			.register();
    public static final BlockEntry<RocketStationBlock> ROCKET_STATION = REGISTRATE.block("rocket_station", RocketStationBlock::new)
    		.initialProperties(SharedProperties::softMetal)
			.properties(p -> p.color(MaterialColor.COLOR_GRAY))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
    		.transform(pickaxeOnly())
    		.simpleItem()
    		.onRegister(assignDataBehaviour(new StationSummaryDisplaySource(), "station_summary"))
    		.onRegister(assignDataBehaviour(new TrainStatusDisplaySource(), "train_status"))
			.onRegister(interactionBehaviour(new RocketStationBlockMovingInteraction()))
    		.lang("Rocket Station")
    		.register();
    public static final BlockEntry<RocketControlsBlock> ROCKET_CONTROLS = REGISTRATE.block("rocket_controls", RocketControlsBlock::new)
    		.initialProperties(SharedProperties::softMetal)
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK).color(MaterialColor.COLOR_GRAY).noOcclusion())
    		.transform(pickaxeOnly())
    		.onRegister(movementBehaviour(new RocketControlsMovementBehaviour()))
    		.onRegister(interactionBehaviour(new RocketControlsInteractionBehaviour()))
    		.simpleItem()
    		.lang("Rocket Controls")
    		.register();
    public static final BlockEntry<TargetingComputerRackBlock> COMPUTER_RACK = REGISTRATE.block("computer_rack", TargetingComputerRackBlock::new)
    		.initialProperties(SharedProperties::softMetal)
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK).color(MaterialColor.COLOR_GRAY).noOcclusion())
    		.transform(pickaxeOnly())
    		.simpleItem()
    		.lang("Computer Rack")
    		.register();
    public static final BlockEntry<OxygenDetectorBlock> OXYGEN_DETECTOR = REGISTRATE.block("oxygen_detector", OxygenDetectorBlock::new)
    		.initialProperties(SharedProperties::softMetal)
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK).color(MaterialColor.COLOR_GRAY).noOcclusion())
    		.transform(pickaxeOnly())
    		.simpleItem()
    		.lang("Oxygen Detector")
    		.register();
    
    public static final BlockEntry<SpaceDoorBlock> IRON_SPACE_DOOR =
    		REGISTRATE.block("iron_space_door", p -> new SpaceDoorBlock(p, false))
    			.transform(BuilderTransformers.slidingDoor("iron_space"))
    			.properties(p -> p.color(MaterialColor.TERRACOTTA_CYAN)
    				.sound(SoundType.NETHERITE_BLOCK)
    				.noOcclusion())
    			.register();
    
    
    
	static {
		REGISTRATE.creativeModeTab(() -> NorthstarCreativeModeTab.NORTHSTAR_BLOCKS);
	}	
    @SuppressWarnings("removal")
	public static final BlockEntry<ExtinguishedTorchBlock> EXTINGUISHED_TORCH = REGISTRATE.block("extinguished_torch", ExtinguishedTorchBlock::new)
			.properties(p -> p.sound(SoundType.WOOD).noCollission().instabreak())
			.addLayer(() -> RenderType::cutoutMipped)
			.register();
    @SuppressWarnings("removal")
	public static final BlockEntry<ExtinguishedTorchWallBlock> EXTINGUISHED_TORCH_WALL = REGISTRATE.block("extinguished_torch_wall", ExtinguishedTorchWallBlock::new)
			.properties(p -> p.sound(SoundType.WOOD).noCollission().instabreak())
			.addLayer(() -> RenderType::cutoutMipped)
			.register();
    @SuppressWarnings("removal")  
	public static final BlockEntry<ExtinguishedLanternBlock> EXTINGUISHED_LANTERN = REGISTRATE.block("extinguished_lantern", ExtinguishedLanternBlock::new)
    		.initialProperties(SharedProperties::softMetal)
			.properties(p -> p.sound(SoundType.LANTERN))
			.addLayer(() -> RenderType::cutoutMipped)
			.simpleItem()
			.register();
    @SuppressWarnings("removal")  
	public static final BlockEntry<GlowstoneTorchBlock> GLOWSTONE_TORCH = REGISTRATE.block("glowstone_torch", GlowstoneTorchBlock::new)
			.properties(p -> p.color(MaterialColor.COLOR_GRAY).lightLevel(value -> 15).sound(SoundType.METAL).noCollission().instabreak())
			.addLayer(() -> RenderType::cutoutMipped)
			.register();
	@SuppressWarnings("removal")
	public static final BlockEntry<GlowstoneTorchWallBlock> GLOWSTONE_TORCH_WALL = REGISTRATE.block("glowstone_torch_wall", GlowstoneTorchWallBlock::new)
			.properties(p -> p.color(MaterialColor.COLOR_GRAY).lightLevel(value -> 15).sound(SoundType.METAL).noCollission().instabreak())
			.addLayer(() -> RenderType::cutoutMipped)
			.register();
    @SuppressWarnings("removal")  
	public static final BlockEntry<LanternBlock> GLOWSTONE_LANTERN = REGISTRATE.block("glowstone_lantern", LanternBlock::new)
    		.initialProperties(SharedProperties::softMetal)
			.properties(p -> p.color(MaterialColor.COLOR_GRAY).lightLevel(value -> 15).sound(SoundType.LANTERN))
			.addLayer(() -> RenderType::cutoutMipped)
			.simpleItem()
			.register();
    @SuppressWarnings("removal")  
	public static final BlockEntry<CrystalBlock> AMETHYST_CRYSTAL = REGISTRATE.block("amethyst_crystal", CrystalBlock::new)
    		.initialProperties(SharedProperties::softMetal)
			.properties(p -> p.color(MaterialColor.COLOR_PURPLE).lightLevel(value -> 5).sound(SoundType.AMETHYST_CLUSTER))
			.addLayer(() -> RenderType::cutoutMipped)
			.simpleItem()
			.register();
    @SuppressWarnings("removal")  
	public static final BlockEntry<CrystalBlock> LUNAR_SAPPHIRE_CRYSTAL = REGISTRATE.block("lunar_sapphire_crystal", CrystalBlock::new)
    		.initialProperties(SharedProperties::softMetal)
			.properties(p -> p.color(MaterialColor.COLOR_BLUE).lightLevel(value -> 7).sound(SoundType.AMETHYST_CLUSTER))
			.addLayer(() -> RenderType::cutoutMipped)
			.simpleItem()
			.register();
    
	public static final BlockEntry<CustomCogBlock> IRON_COGWHEEL = REGISTRATE.block("iron_cogwheel", CustomCogBlock::small)
			.initialProperties(SharedProperties::softMetal)
			.properties(p -> p.sound(SoundType.METAL))
			.properties(p -> p.color(MaterialColor.COLOR_GRAY))
			.transform(BlockStressDefaults.setNoImpact())
			.blockstate(BlockStateGen.axisBlockProvider(false))
			.onRegister(CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new))
			.item(CogwheelBlockItem::new)
			.build()
			.register();
//	public static final BlockEntry<CogWheelBlock> IRON_LARGE_COGWHEEL = REGISTRATE.block("iron_large_cogwheel", CogWheelBlock::large)
//			.initialProperties(SharedProperties::softMetal)
//			.properties(p -> p.sound(SoundType.METAL))
//			.properties(p -> p.color(MaterialColor.COLOR_GRAY))
//			.transform(BlockStressDefaults.setNoImpact())
//			.blockstate(BlockStateGen.axisBlockProvider(false))
//			.onRegister(CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new))
//			.item(CogwheelBlockItem::new)
//			.build()
//			.register();
    
	public static void register() {}
	
	   private static boolean never(BlockState p_50806_, BlockGetter p_50807_, BlockPos p_50808_) {
		      return false;
		   }
}
