package com.lightning.northstar;

import org.slf4j.Logger;

import com.lightning.northstar.advancements.NorthstarAdvancements;
import com.lightning.northstar.advancements.NorthstarTriggers;
import com.lightning.northstar.block.NorthstarBlocks;
import com.lightning.northstar.block.NorthstarTechBlocks;
import com.lightning.northstar.block.entity.NorthstarBlockEntityTypes;
import com.lightning.northstar.block.tech.NorthstarPartialModels;
import com.lightning.northstar.block.tech.astronomy_table.AstronomyTableScreen;
import com.lightning.northstar.block.tech.rocket_station.RocketStationScreen;
import com.lightning.northstar.block.tech.telescope.TelescopeScreen;
import com.lightning.northstar.client.renderer.armor.BrokenIronSpaceSuitLayerRenderer;
import com.lightning.northstar.client.renderer.armor.BrokenIronSpaceSuitModelRenderer;
import com.lightning.northstar.client.renderer.armor.IronSpaceSuitLayerRenderer;
import com.lightning.northstar.client.renderer.armor.IronSpaceSuitModelRenderer;
import com.lightning.northstar.client.renderer.armor.MartianSteelSpaceSuitLayerRenderer;
import com.lightning.northstar.client.renderer.armor.MartianSteelSpaceSuitModelRenderer;
import com.lightning.northstar.contraptions.RocketHandler;
import com.lightning.northstar.entity.MarsCobraEntity;
import com.lightning.northstar.entity.MarsMothEntity;
import com.lightning.northstar.entity.MarsToadEntity;
import com.lightning.northstar.entity.MarsWormEntity;
import com.lightning.northstar.entity.MercuryRaptorEntity;
import com.lightning.northstar.entity.MercuryRoachEntity;
import com.lightning.northstar.entity.MercuryTortoiseEntity;
import com.lightning.northstar.entity.MoonEelEntity;
import com.lightning.northstar.entity.MoonLunargradeEntity;
import com.lightning.northstar.entity.MoonSnailEntity;
import com.lightning.northstar.entity.NorthstarEntityTypes;
import com.lightning.northstar.entity.VenusMimicEntity;
import com.lightning.northstar.entity.VenusScorpionEntity;
import com.lightning.northstar.entity.VenusStoneBullEntity;
import com.lightning.northstar.entity.VenusVultureEntity;
import com.lightning.northstar.fluids.NorthstarFluids;
import com.lightning.northstar.item.NorthstarEnchantments;
import com.lightning.northstar.item.NorthstarItems;
import com.lightning.northstar.item.NorthstarPotions;
import com.lightning.northstar.item.NorthstarRecipeTypes;
import com.lightning.northstar.item.armor.BrokenIronSpaceSuitArmorItem;
import com.lightning.northstar.item.armor.IronSpaceSuitArmorItem;
import com.lightning.northstar.item.armor.MartianSteelSpaceSuitArmorItem;
import com.lightning.northstar.particle.NorthstarParticles;
import com.lightning.northstar.sound.NorthstarSounds;
import com.lightning.northstar.world.OxygenStuff;
import com.lightning.northstar.world.TemperatureStuff;
import com.lightning.northstar.world.dimension.NorthstarDimensions;
import com.lightning.northstar.world.dimension.NorthstarPlanets;
import com.lightning.northstar.world.features.NorthstarConfiguredFeatures;
import com.lightning.northstar.world.features.NorthstarFeatures;
import com.lightning.northstar.world.features.trunkplacers.NorthstarTrunkPlacerTypes;
import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.item.TooltipHelper.Palette;
import com.simibubi.create.foundation.item.TooltipModifier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib3.GeckoLib;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Northstar.MOD_ID)
public class Northstar
{
    // Define mod id in a common place for everything to reference
    public static final double GRAV_CONSTANT = 0.08;
    public static final double EARTH_GRAV = 1;
    public static final double MARS_GRAV = 0.37;
    public static final double VENUS_GRAV = 0.89;
    //test
    
    public static final String MOD_ID = "northstar";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
	public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);
	public static final NorthstarRegistrate REGISTRATE_CUSTOM = NorthstarRegistrate.create(MOD_ID);
	

	static {
		REGISTRATE.setTooltipModifierFactory(item -> {
			return new ItemDescription.Modifier(item, Palette.STANDARD_CREATE)
				.andThen(TooltipModifier.mapNull(KineticStats.create(item)));
		});
	}

    public Northstar()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
		IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        REGISTRATE.registerEventListeners(modEventBus);
        NorthstarItems.register(modEventBus);
        NorthstarBlocks.register(modEventBus);
        NorthstarPotions.register(modEventBus);
        NorthstarEnchantments.register();
        NorthstarTechBlocks.register();
  //      NorthstarTechBlocks.register(modEventBus);
        NorthstarBlockEntityTypes.register(modEventBus);
        NorthstarFeatures.register(modEventBus);
        NorthstarConfiguredFeatures.register(modEventBus);
        NorthstarRecipeTypes.register(modEventBus);
        NorthstarParticles.register(modEventBus);
        NorthstarSounds.register(modEventBus);
        NorthstarMenuTypes.register(modEventBus);
        NorthstarPlanets.register();
        NorthstarDimensions.register();
        NorthstarEntityTypes.register();
        NorthstarEntityTypes.ENTITIES.register(modEventBus);
        NorthstarFluids.register();

        
        NorthstarTrunkPlacerTypes.register(modEventBus);
		NorthstarPartialModels.init();
		
        OxygenStuff.register();
        TemperatureStuff.register();
        
        RocketHandler.register();
        
        GeckoLib.initialize();
        // Register the commonSetup method for modloading
		modEventBus.addListener(Northstar::init);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::registerSpawnPlacements);
        
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> NorthstarClient.onCtorClient(modEventBus, forgeEventBus));



        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }
	public static void init(final FMLCommonSetupEvent event) {
		NorthstarPackets.registerPackets();

		event.enqueueWork(() -> {
			NorthstarAdvancements.register();
			NorthstarTriggers.register();
		});
	}
    
    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
        LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

    }

	static {
		REGISTRATE.setTooltipModifierFactory(item -> new ItemDescription.Modifier(item, TooltipHelper.Palette.STANDARD_CREATE)
			.andThen(TooltipModifier.mapNull(KineticStats.create(item))));
	}
	
	private void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
		
		
		
        event.register(NorthstarEntityTypes.MARS_WORM.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        		MarsWormEntity::wormSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(NorthstarEntityTypes.MARS_TOAD.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        		MarsToadEntity::toadSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(NorthstarEntityTypes.MARS_COBRA.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        		MarsCobraEntity::cobraSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(NorthstarEntityTypes.MARS_MOTH.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        		MarsMothEntity::mothSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
        
        event.register(NorthstarEntityTypes.VENUS_MIMIC.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        		VenusMimicEntity::mimicSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(NorthstarEntityTypes.VENUS_SCORPION.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        		VenusScorpionEntity::scorpionSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(NorthstarEntityTypes.VENUS_STONE_BULL.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        		VenusStoneBullEntity::stoneBullSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(NorthstarEntityTypes.VENUS_VULTURE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        		VenusVultureEntity::vultureSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
        
        event.register(NorthstarEntityTypes.MOON_SNAIL.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        		MoonSnailEntity::snailSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(NorthstarEntityTypes.MOON_LUNARGRADE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        		MoonLunargradeEntity::lunargradeSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(NorthstarEntityTypes.MOON_EEL.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        		MoonEelEntity::eelSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
        
        event.register(NorthstarEntityTypes.MERCURY_RAPTOR.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        		MercuryRaptorEntity::raptorSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(NorthstarEntityTypes.MERCURY_ROACH.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        		MercuryRoachEntity::roachSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(NorthstarEntityTypes.MERCURY_TORTOISE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        		MercuryTortoiseEntity::tortoiseSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
            MenuScreens.register(NorthstarMenuTypes.TELESCOPE_MENU.get(), TelescopeScreen::new);
            MenuScreens.register(NorthstarMenuTypes.ASTRONOMY_TABLE_MENU.get(), AstronomyTableScreen::new);
            MenuScreens.register(NorthstarMenuTypes.ROCKET_STATION.get(), RocketStationScreen::new);
        }
    	@SubscribeEvent
    	public static void registerRenderers(final EntityRenderersEvent.AddLayers event) {
    		{
    			GeoArmorRenderer.registerArmorRenderer(BrokenIronSpaceSuitArmorItem.class, () -> new BrokenIronSpaceSuitModelRenderer());
    			GeoArmorRenderer.registerArmorRenderer(IronSpaceSuitArmorItem.class, () -> new IronSpaceSuitModelRenderer());
    			GeoArmorRenderer.registerArmorRenderer(MartianSteelSpaceSuitArmorItem.class, () -> new MartianSteelSpaceSuitModelRenderer());
    		}
    	}
		@SubscribeEvent	
		public static void addEntityRendererLayers(EntityRenderersEvent.AddLayers event) {
			EntityRenderDispatcher dispatcher = Minecraft.getInstance()
					.getEntityRenderDispatcher();
				IronSpaceSuitLayerRenderer.registerOnAll(dispatcher);
				BrokenIronSpaceSuitLayerRenderer.registerOnAll(dispatcher);
				MartianSteelSpaceSuitLayerRenderer.registerOnAll(dispatcher);
		}
    	@SuppressWarnings("removal")
    	@SubscribeEvent
    	public static void registerRenderers(final FMLClientSetupEvent event) {
    		ItemBlockRenderTypes.setRenderLayer(NorthstarBlocks.LUNAR_SAPPHIRE_CLUSTER.get(), RenderType.cutout());
    		ItemBlockRenderTypes.setRenderLayer(NorthstarBlocks.ARGYRE_LEAVES.get(), RenderType.cutout());
    		ItemBlockRenderTypes.setRenderLayer(NorthstarBlocks.COILER_VINES.get(), RenderType.cutout());
    		ItemBlockRenderTypes.setRenderLayer(NorthstarBlocks.MARS_ROOTS.get(), RenderType.cutout());
    		ItemBlockRenderTypes.setRenderLayer(NorthstarBlocks.POINTED_CRIMSITE.get(), RenderType.cutout());
    		ItemBlockRenderTypes.setRenderLayer(NorthstarBlocks.ICICLE.get(), RenderType.cutout());
    		ItemBlockRenderTypes.setRenderLayer(NorthstarBlocks.MERCURY_CACTUS.get(), RenderType.cutout());
    		ItemBlockRenderTypes.setRenderLayer(NorthstarBlocks.FROST.get(), RenderType.translucent());
    		ItemBlockRenderTypes.setRenderLayer(NorthstarBlocks.MARTIAN_TALL_GRASS.get(), RenderType.cutout());
    		ItemBlockRenderTypes.setRenderLayer(NorthstarBlocks.MARTIAN_STRAWBERRY_BUSH.get(), RenderType.cutout());
    		ItemBlockRenderTypes.setRenderLayer(NorthstarBlocks.MARS_TULIP.get(), RenderType.cutout());
    		ItemBlockRenderTypes.setRenderLayer(NorthstarBlocks.MARS_PALM.get(), RenderType.cutout());
    		ItemBlockRenderTypes.setRenderLayer(NorthstarBlocks.MARS_SPROUT.get(), RenderType.cutout());
    		ItemBlockRenderTypes.setRenderLayer(NorthstarBlocks.MARS_SPROUT_BIG.get(), RenderType.cutout());
    		ItemBlockRenderTypes.setRenderLayer(NorthstarBlocks.GLOWING_MARS_ROOTS.get(), RenderType.cutout());
    		ItemBlockRenderTypes.setRenderLayer(NorthstarBlocks.SMALL_LUNAR_SAPPHIRE_BUD.get(), RenderType.cutout());
    		ItemBlockRenderTypes.setRenderLayer(NorthstarBlocks.MEDIUM_LUNAR_SAPPHIRE_BUD.get(), RenderType.cutout());
    		ItemBlockRenderTypes.setRenderLayer(NorthstarBlocks.LARGE_LUNAR_SAPPHIRE_BUD.get(), RenderType.cutout());
    		ItemBlockRenderTypes.setRenderLayer(NorthstarBlocks.MARS_WORM_NEST.get(), RenderType.cutout());
    		ItemBlockRenderTypes.setRenderLayer(NorthstarBlocks.MERCURY_SHELF_FUNGUS.get(), RenderType.cutout());
    		ItemBlockRenderTypes.setRenderLayer(NorthstarBlocks.METHANE_ICE.get(), RenderType.translucent());
    		ItemBlockRenderTypes.setRenderLayer(NorthstarBlocks.IRON_GRATE.get(), RenderType.cutout());
    		ItemBlockRenderTypes.setRenderLayer(NorthstarBlocks.MARTIAN_STEEL_GRATE.get(), RenderType.cutout());
    		ItemBlockRenderTypes.setRenderLayer(NorthstarBlocks.TUNGSTEN_GRATE.get(), RenderType.cutout());
    		ItemBlockRenderTypes.setRenderLayer(NorthstarBlocks.VENT_BLOCK.get(), RenderType.cutout());
    		ItemBlockRenderTypes.setRenderLayer(NorthstarFluids.SULFURIC_ACID.get().getSource(), RenderType.translucent());
    		ItemBlockRenderTypes.setRenderLayer(NorthstarFluids.SULFURIC_ACID.get(), RenderType.translucent());
    		
    		ItemBlockRenderTypes.setRenderLayer(NorthstarFluids.LIQUID_HYDROGEN.get().getSource(), RenderType.translucent());
    		ItemBlockRenderTypes.setRenderLayer(NorthstarFluids.LIQUID_HYDROGEN.get(), RenderType.translucent());
    		
    		ItemBlockRenderTypes.setRenderLayer(NorthstarFluids.LIQUID_OXYGEN.get().getSource(), RenderType.translucent());
    		ItemBlockRenderTypes.setRenderLayer(NorthstarFluids.LIQUID_OXYGEN.get(), RenderType.translucent());
    		
    		ItemBlockRenderTypes.setRenderLayer(NorthstarFluids.METHANE.get().getSource(), RenderType.translucent());
    		ItemBlockRenderTypes.setRenderLayer(NorthstarFluids.METHANE.get(), RenderType.translucent());
    		
    		

    	}
    }
	public static ResourceLocation asResource(String path) {
		return new ResourceLocation(Northstar.MOD_ID, path);
	}
}
