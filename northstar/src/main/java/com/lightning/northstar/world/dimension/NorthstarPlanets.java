package com.lightning.northstar.world.dimension;

import com.lightning.northstar.Northstar;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = Northstar.MOD_ID, bus = Bus.FORGE)
public class NorthstarPlanets {	
	
	// MERCURY COORDS
    public static double mercury_x = 0;
    public static double mercury_y = 0;
    public static float mercury_orbit_speed = (float) (Math.PI / 12000);
    public static double mercury_orbit_radius_x = 100;
    public static double mercury_orbit_radius_y = 100;
    public static double mercury_origin_x = 0;
    public static double mercury_origin_y = 0;
	static int mercury_time;
	
	//VENUS COORDS
    public static double venus_x = 0;
    public static double venus_y = 0;
    public static float venus_orbit_speed = (float) (Math.PI / 32000);
    public static double venus_orbit_radius_x = 150;
    public static double venus_orbit_radius_y = 150;
    public static double venus_origin_x = 0;
    public static double venus_origin_y = 0;
	static int venus_time;
	
	//EARTH COORDS
    public static double earth_x = 0;
    public static double earth_y = 0;
    public static float earth_orbit_speed = (float) (Math.PI / 32000);
    public static double earth_orbit_radius_x = 200;
    public static double earth_orbit_radius_y = 200;
    public static double earth_origin_x = 0;
    public static double earth_origin_y = 0;
	static int earth_time;
	
	// EARTH MOON COORDS
	// "earth moon" because these are the coords for the moon when viewed from earth
    public static double earth_moon_x = 0;
    public static double earth_moon_y = 0;
    public static float earth_moon_orbit_speed = (float) (Math.PI / 150000);
    public static double earth_moon_orbit_radius_x = 40;
    public static double earth_moon_orbit_radius_y = 40;
    public static double earth_moon_origin_x = 0;
    public static double earth_moon_origin_y = 0;
	static int earth_moon_time;
	
	//MOON COORDS
	//this is seperate because these are the coords for the moon when viewed from NOT earth
    public static double moon_x = 0;
    public static double moon_y = 0;
    public static float moon_orbit_speed = (float) (Math.PI / 10000);
    public static double moon_orbit_radius_x = 20;
    public static double moon_orbit_radius_y = 20;
	static int moon_time;
	
	// MARS COORDS
    public static double mars_x = 0;
    public static double mars_y = 0;
    public static float mars_orbit_speed = (float) (Math.PI / 100000);
    public static double mars_orbit_radius_x = 250;
    public static double mars_orbit_radius_y = 250;
    public static double mars_origin_x = 0;
    public static double mars_origin_y = 0;
	static int mars_time;
	
	// PHOBOS & DEIMOS COORDS
    public static double pd_x = 0;
    public static double pd_y = 0;
    public static float pd_orbit_speed = (float) (Math.PI / 10000);
    public static double pd_orbit_radius_x = 20;
    public static double pd_orbit_radius_y = 20;
	static int pd_time;
	
	// CERES COORDS
    public static double ceres_x = 0;
    public static double ceres_y = 0;
    public static float ceres_orbit_speed = (float) (Math.PI / 200000);
    public static double ceres_orbit_radius_x = 260;
    public static double ceres_orbit_radius_y = 260;
    public static double ceres_origin_x = 0;
    public static double ceres_origin_y = 0;
	static int ceres_time;
	
	//JUPITER COORDS
    public static double jupiter_x = 0;
    public static double jupiter_y = 0;
    public static float jupiter_orbit_speed = (float) (Math.PI / 32000);
    public static double jupiter_orbit_radius_x = 270;
    public static double jupiter_orbit_radius_y = 270;
    public static double jupiter_origin_x = 0;
    public static double jupiter_origin_y = 0;
	static int jupiter_time;
	
	//SATURN COORDS
    public static double saturn_x = 0;
    public static double saturn_y = 0;
    public static float saturn_orbit_speed = (float) (Math.PI / 40000);
    public static double saturn_orbit_radius_x = 300;
    public static double saturn_orbit_radius_y = 300;
    public static double saturn_origin_x = 0;
    public static double saturn_origin_y = 0;
	static int saturn_time;
	
	//URANUS COORDS
    public static double uranus_x = 0;
    public static double uranus_y = 0;
    public static float uranus_orbit_speed = (float) (Math.PI / 50000);
    public static double uranus_orbit_radius_x = 375;
    public static double uranus_orbit_radius_y = 375;
    public static double uranus_origin_x = 0;
    public static double uranus_origin_y = 0;
	static int uranus_time;
	
	//NEPTUNE COORDS
    public static double neptune_x = 0;
    public static double neptune_y = 0;
    public static float neptune_orbit_speed = (float) (Math.PI / 80000);
    public static double neptune_orbit_radius_x = 450;
    public static double neptune_orbit_radius_y = 450;
    public static double neptune_origin_x = 0;
    public static double neptune_origin_y = 0;
	static int neptune_time;
	
	//PLUTO COORDS
    public static double pluto_x = 0;
    public static double pluto_y = 0;
    public static float pluto_orbit_speed = (float) (Math.PI / 150000);
    public static double pluto_orbit_radius_x = 525;
    public static double pluto_orbit_radius_y = 515;
    public static double pluto_origin_x = 0;
    public static double pluto_origin_y = 0;
	static int pluto_time;
	
	//ERIS COORDS
    public static double eris_x = 0;
    public static double eris_y = 0;
    public static float eris_orbit_speed = (float) (Math.PI / 16000);
    public static double eris_orbit_radius_x = 1000;
    public static double eris_orbit_radius_y = 600;
    public static double eris_origin_x = 0;
    public static double eris_origin_y = 0;
	static int eris_time;
	
	static long time;

    @SubscribeEvent
    public static void onWorldTick(TickEvent.LevelTickEvent event){
    	time = event.level.getLevelData().getGameTime();
        //mercury!!!! yeah cool
        double mercury_radian = mercury_orbit_speed * time;
        mercury_x = (mercury_origin_x + (Math.cos(mercury_radian)* mercury_orbit_radius_x));
        mercury_y = (mercury_origin_y + (Math.sin(mercury_radian)* mercury_orbit_radius_y));
        //venus, very cool (or not, actually)
        double venus_radian = venus_orbit_speed * time;
        venus_x = (venus_origin_x + (Math.cos(venus_radian)* venus_orbit_radius_x));
        venus_y = (venus_origin_y + (Math.sin(venus_radian)* venus_orbit_radius_y));
        
        //calcs for earth when viewed from another planet
        double earth_radian = earth_orbit_speed * time;
        earth_x = (earth_origin_x + (Math.cos(earth_radian)* earth_orbit_radius_x));
        earth_y = (earth_origin_y + (Math.sin(earth_radian)* earth_orbit_radius_y));
        
        //calcs for moon when viewed from earth
        double earth_moon_radian = earth_moon_orbit_speed * time;
        earth_moon_x = (earth_moon_origin_x + (Math.cos(earth_moon_radian)* earth_moon_orbit_radius_x));
        earth_moon_y = (earth_moon_origin_y + (Math.sin(earth_moon_radian)* earth_moon_orbit_radius_y));
        
        //calcs for moon when NOT viewed from earth
        double moon_radian = moon_orbit_speed * time;
        moon_x = (earth_x + (Math.cos(moon_radian)* moon_orbit_radius_x));
        moon_y = (earth_y + (Math.sin(moon_radian)* moon_orbit_radius_y));
    	//mars my beloved
        double mars_radian = mars_orbit_speed * time;
        mars_x = (mars_origin_x + (Math.cos(mars_radian)* mars_orbit_radius_x));
        mars_y = (mars_origin_y + (Math.sin(mars_radian)* mars_orbit_radius_y));
        //phobos and deimos (doom reference????)
        double pd_radian = pd_orbit_speed * time;
        pd_x = (mars_x + (Math.cos(pd_radian)* pd_orbit_radius_x));
        pd_y = (mars_y + (Math.sin(pd_radian)* pd_orbit_radius_y));
        
        //calcs for ceres
        double ceres_radian = ceres_orbit_speed * time;
        ceres_x = (ceres_x + (Math.cos(ceres_radian)* ceres_orbit_radius_x));
        ceres_y = (ceres_y + (Math.sin(ceres_radian)* ceres_orbit_radius_y));
        
        //calcs for jupiter
        double jupiter_radian = jupiter_orbit_speed * time;
        jupiter_x = (jupiter_origin_x + (Math.cos(jupiter_radian)* jupiter_orbit_radius_x));
        jupiter_y = (jupiter_origin_y + (Math.sin(jupiter_radian)* jupiter_orbit_radius_y));
        
        //calcs for saturn
        double saturn_radian = saturn_orbit_speed * time;
        saturn_x = (saturn_origin_x + (Math.cos(saturn_radian)* saturn_orbit_radius_x));
        saturn_y = (saturn_origin_y + (Math.sin(saturn_radian)* saturn_orbit_radius_y));
        
        //calcs for uranus
        double uranus_radian = uranus_orbit_speed * time;
        uranus_x = (uranus_origin_x + (Math.cos(uranus_radian)* uranus_orbit_radius_x));
        uranus_y = (uranus_origin_y + (Math.sin(uranus_radian)* uranus_orbit_radius_y));
        
        //calcs for neptune
        double neptune_radian = neptune_orbit_speed * time;
        neptune_x = (neptune_origin_x + (Math.cos(neptune_radian)* neptune_orbit_radius_x));
        neptune_y = (neptune_origin_y + (Math.sin(neptune_radian)* neptune_orbit_radius_y));
        
        //calcs for pluto
        //pluto deserves to be here :]
        double pluto_radian = pluto_orbit_speed * time;
        pluto_x = (pluto_origin_x + (Math.cos(pluto_radian)* pluto_orbit_radius_x));
        pluto_y = (pluto_origin_y + (Math.sin(pluto_radian)* pluto_orbit_radius_y));
        
        
        //calcs for eris
        //eris ALSO deserves to be here :]
        //no one ever talks about eris and that makes me sad :[
        double eris_radian = eris_orbit_speed * time;
        eris_x = (eris_origin_x + (Math.cos(eris_radian)* eris_orbit_radius_x));
        eris_y = (eris_origin_y + (Math.sin(eris_radian)* eris_orbit_radius_y));
//      System.out.println(mars_x + "   Mars X");
//      System.out.println(mars_y + "   Mars Y");
    }
    
    public static double getPlanetX(String name) {
    	switch (name) {
		case "mercury": {return mercury_x;}
		case "venus": {return venus_x;}
		case "earth": {return earth_x;}
		case "earth_moon": {return earth_moon_x;}
		case "moon": {return moon_x;}
		case "mars": {return mars_x;}
		case "ceres": {return ceres_x;}
		case "jupiter": {return jupiter_x;}
		case "saturn": {return saturn_x;}
		case "uranus": {return uranus_x;}
		case "neptune": {return neptune_x;}
		case "pluto": {return pluto_x;}
		case "eris": {return eris_x;}
		default:return 0;}
    }
    public static double getPlanetY(String name) {
    	switch (name) {
		case "mercury": {return mercury_y;}
		case "venus": {return venus_y;}
		case "earth": {return earth_y;}
		case "earth_moon": {return earth_moon_y;}
		case "moon": {return moon_y;}
		case "mars": {return mars_y;}
		case "ceres": {return ceres_y;}
		case "jupiter": {return jupiter_y;}
		case "saturn": {return saturn_y;}
		case "uranus": {return uranus_y;}
		case "neptune": {return neptune_y;}
		case "pluto": {return pluto_y;}
		case "eris": {return eris_y;}
		default:return 0;}
    }
    public static String getPlanetName(ResourceKey<Level> level) {
		if (level == NorthstarDimensions.MARS_DIM_KEY) {return "mars";}
		if (level == NorthstarDimensions.MOON_DIM_KEY) {return "moon";}
		if (level == NorthstarDimensions.VENUS_DIM_KEY) {return "venus";}
		if (level == NorthstarDimensions.MERCURY_DIM_KEY) {return "mercury";}
		if (level == Level.OVERWORLD) {return "earth";}
		return "earth";
    }
    public static int getPlanetTemp(ResourceKey<Level> level) {
		if (level == NorthstarDimensions.MARS_DIM_KEY) {return -100;}
		if (level == NorthstarDimensions.MOON_DIM_KEY) {return -183;}
		if (level == NorthstarDimensions.VENUS_DIM_KEY) {return 464;}
		if (level == NorthstarDimensions.MERCURY_DIM_KEY) {return 400;}
		if (level == Level.OVERWORLD) {return 15;}
		if (level == Level.NETHER) {return 230;}
		if (level == Level.END) {return 4;}
		return 15;
    }
    public static int getPlanetAtmosphereCost(ResourceKey<Level> level) {
		if (level == NorthstarDimensions.MARS_DIM_KEY) {return 200;}
		if (level == Level.OVERWORLD) {return 1600;}
		if (level == NorthstarDimensions.MOON_DIM_KEY) {return 0;}
		if (level == NorthstarDimensions.VENUS_DIM_KEY) {return 4000;}
		if (level == NorthstarDimensions.MERCURY_DIM_KEY) {return 0;}
		return 0;
    }
    public static int getComputingCost(ResourceKey<Level> level) {
		if (level == NorthstarDimensions.MARS_DIM_KEY) {return 400;}
		if (level == Level.OVERWORLD) {return 0;}
		if (level == NorthstarDimensions.MOON_DIM_KEY) {return 50;}
		if (level == NorthstarDimensions.VENUS_DIM_KEY) {return 200;}
		if (level == NorthstarDimensions.MERCURY_DIM_KEY) {return 800;}
		return 0;
    }
    
    public static ResourceKey<Level> getPlanetDimension(String name) {
    	switch (name) {
		case "mercury": {return NorthstarDimensions.MERCURY_DIM_KEY;}
		case "mars": {return NorthstarDimensions.MARS_DIM_KEY;}
		case "venus": {return NorthstarDimensions.VENUS_DIM_KEY;}
		case "earth": {return Level.OVERWORLD;}
		case "earth_moon": {return NorthstarDimensions.MOON_DIM_KEY;}
		case "moon": {return NorthstarDimensions.MOON_DIM_KEY;}
		default:return Level.OVERWORLD;}
    }
    public static boolean getPlanetOxy(ResourceKey<Level> level) {
    	if(level == NorthstarDimensions.MARS_DIM_KEY) {return false;}
    	if(level == NorthstarDimensions.MERCURY_DIM_KEY) {return false;}
    	if(level == NorthstarDimensions.MOON_DIM_KEY) {return false;}
    	if(level == NorthstarDimensions.VENUS_DIM_KEY) {return false;}
    	return true;
    }
    public static boolean hasNormalGrav(ResourceKey<Level> level) {
    	if(level == NorthstarDimensions.MARS_DIM_KEY) {return false;}
    	if(level == NorthstarDimensions.MERCURY_DIM_KEY) {return false;}
    	if(level == NorthstarDimensions.MOON_DIM_KEY) {return false;}
    	if(level == NorthstarDimensions.VENUS_DIM_KEY) {return false;}
    	return true;
    }
    
    public static boolean isCustomDimension(ResourceLocation resourceLocation) {
    	if(resourceLocation == NorthstarDimensions.MARS_DIM_KEY.location()) {return true;}
    	if(resourceLocation == NorthstarDimensions.MERCURY_DIM_KEY.location()) {return true;}
    	if(resourceLocation == NorthstarDimensions.MOON_DIM_KEY.location()) {return true;}
    	if(resourceLocation == NorthstarDimensions.VENUS_DIM_KEY.location()) {return true;}
    	return false;
    }
    public static long getSeedOffset(ResourceKey<Level> level) {
    	if(level == NorthstarDimensions.MARS_DIM_KEY) {return 1;}
    	if(level == NorthstarDimensions.MERCURY_DIM_KEY) {return 2;}
    	if(level == NorthstarDimensions.MOON_DIM_KEY) {return 3;}
    	if(level == NorthstarDimensions.VENUS_DIM_KEY) {return 4;}
    	return 0;
    }
    
	public static String targetGetter(String thing) {
		String newthing = "";
		for(int i = 0;i < thing.length(); i++) {
			if(i > 6 && i < thing.length() - 2) {newthing += thing.charAt(i);}
		}
		return newthing;
	}
	
	public static boolean isInOrbit(ResourceKey<Level> level) {
    	if(level == NorthstarDimensions.EARTH_ORBIT_DIM_KEY) {return true;}
    	return false;
		
	}
	
	


    
    
    
	public static void register() {
		System.out.println("Calculating Planets for " + Northstar.MOD_ID);
	}

}
