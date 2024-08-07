package com.lightning.northstar.world;

import com.lightning.northstar.Northstar;
import com.lightning.northstar.world.dimension.NorthstarDimensions;
import com.simibubi.create.foundation.utility.Pair;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import java.util.HashMap;
import java.util.Map.Entry;


@EventBusSubscriber(modid = Northstar.MOD_ID, bus = Bus.FORGE)
public class WeatherStuff {
	
	public enum WeatherCondition {
		CLEAR,
		RAINY,
		STORMY
	}


	public static HashMap<Pair<Pair<Direction, Direction>, Pair<WeatherCondition, Integer>>,ResourceKey<Level>> managedPlanets = new HashMap<Pair<Pair<Direction, Direction>, Pair<WeatherCondition, Integer>>, ResourceKey<Level>>();
	
//	@SubscribeEvent
//	public static void onWorldTick(TickEvent.LevelTickEvent event){
//		for(Entry<Pair<Pair<Direction, Direction>, Pair<WeatherCondition, Integer>>, ResourceKey<Level>> entries:	managedPlanets.entrySet()) {
//			for(Pair<Pair<Direction, Direction>, Pair<WeatherCondition, Integer>> entries2 : entries.getKey()) {
//				
//			}
//		}
//	}
	
	public static void init() {
			managedPlanets.put(null, NorthstarDimensions.MARS_DIM_KEY);
			managedPlanets.put(null, NorthstarDimensions.VENUS_DIM_KEY);
	}
	
	
	
	public Pair<Direction,Direction> getWindDirection(ResourceKey<Level> lev){
		if(managedPlanets.containsValue(lev)) {
			for(Entry<Pair<Pair<Direction, Direction>, Pair<WeatherCondition, Integer>>, ResourceKey<Level>> entries:	managedPlanets.entrySet()) {
				if(entries.getValue() == lev) {
					return entries.getKey().getFirst();
				}
				
			}
		}
		return null;
	}
	
	public WeatherCondition getWeatherConditions(ResourceKey<Level> lev) {
		if(managedPlanets.containsValue(lev)) {
			for(Entry<Pair<Pair<Direction, Direction>, Pair<WeatherCondition, Integer>>, ResourceKey<Level>> entries:	managedPlanets.entrySet()) {
				if(entries.getValue() == lev) {
					return entries.getKey().getSecond().getFirst();
				}
				
			}
		}
		return WeatherCondition.CLEAR;
	}
	
	public static boolean hasWind(ResourceKey<Level> lev) {
		return lev == NorthstarDimensions.MARS_DIM_KEY;
	}
	
	public static boolean hasWeather(ResourceKey<Level> lev) {
		if(lev == NorthstarDimensions.MARS_DIM_KEY) {return true;}
		else if(lev == NorthstarDimensions.VENUS_DIM_KEY) {return true;}
		return false;
	}
	
	
}
