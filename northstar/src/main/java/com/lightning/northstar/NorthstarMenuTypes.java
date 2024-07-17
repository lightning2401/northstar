package com.lightning.northstar;

import com.lightning.northstar.block.tech.astronomy_table.AstronomyTableMenu;
import com.lightning.northstar.block.tech.oxygen_concentrator.OxygenConcentratorMenu;
import com.lightning.northstar.block.tech.rocket_station.RocketStationMenu;
import com.lightning.northstar.block.tech.telescope.TelescopeMenu;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class NorthstarMenuTypes {

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, Northstar.MOD_ID);

   public static final RegistryObject<MenuType<TelescopeMenu>> TELESCOPE_MENU =
            registerMenuType(TelescopeMenu::new, "telescope_menu");
   public static final RegistryObject<MenuType<AstronomyTableMenu>> ASTRONOMY_TABLE_MENU =
           registerMenuType(AstronomyTableMenu::new, "astronomy_table_menu");
   public static final RegistryObject<MenuType<RocketStationMenu>> ROCKET_STATION =
           registerMenuType(RocketStationMenu::new, "rocket_station");
   public static final RegistryObject<MenuType<OxygenConcentratorMenu>> OXYGEN_CONCENTRATOR =
           registerMenuType(OxygenConcentratorMenu::new, "oxygen_concenrtator");
//   public static final RegistryObject<MenuType<TemperatureRegulatorMenu>> TEMPERATURE_REGULATOR =
//           registerMenuType(TemperatureRegulatorMenu::new, "temperature_regulator");


    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory,
                                                                                                  String name) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
