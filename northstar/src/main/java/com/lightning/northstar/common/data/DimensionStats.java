package com.lightning.northstar.common.data;
//
//		import java.util.ArrayList;
//		import java.util.HashMap;
///		import java.util.HashSet;
//		import java.util.List;
//		import java.util.Map;
//		import java.util.Set;
//		
//		import com.google.gson.Gson;
//		import com.google.gson.GsonBuilder;
///		import com.google.gson.JsonElement;
//		import com.google.gson.JsonObject;
//		import com.lightning.northstar.Northstar;
//		import com.mojang.serialization.JsonOps;
//		
//		import net.minecraft.network.FriendlyByteBuf;
///		import net.minecraft.resources.ResourceKey;
//		import net.minecraft.resources.ResourceLocation;
//		import net.minecraft.server.packs.resources.ResourceManager;
//		import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
//		import net.minecraft.util.GsonHelper;
//		import net.minecraft.util.profiling.ProfilerFiller;
//		import net.minecraft.world.level.Level;
//		
//		public class DimensionStats extends SimpleJsonResourceReloadListener {
//		    private static final Set<SpaceDimension> SPACEDIMENSIONS = new HashSet<>();
//		    private static final Map<ResourceKey<Level>, SpaceDimension> LEVEL_TO_PLANET = new HashMap<>();
//		    private static final Map<ResourceKey<Level>, SpaceDimension> ORBIT_TO_PLANET = new HashMap<>();
//		    private static final Set<ResourceKey<Level>> PLANET_LEVELS = new HashSet<>();
//		    private static final Set<ResourceKey<Level>> ORBITS_LEVELS = new HashSet<>();
//		    private static final Set<ResourceKey<Level>> OXYGEN_LEVELS = new HashSet<>();
//		    
//		    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
//		
//		    public DimensionStats() {
//		        super(GSON, "dimension_stats");
//		    }
//		    @Override
//		    protected void apply(Map<ResourceLocation, JsonElement> objects, ResourceManager resourceManager, ProfilerFiller profiler) {
//		        profiler.push("Northstar Dimension Deserialization");
//		        List<SpaceDimension> spacedimensions = new ArrayList<>();
//		
//		        for (Map.Entry<ResourceLocation, JsonElement> entry : objects.entrySet()) {
//		            JsonObject jsonObject = GsonHelper.convertToJsonObject(entry.getValue(), "spacedimension");
//		            SpaceDimension newSpaceDimension = SpaceDimension.CODEC.parse(JsonOps.INSTANCE, jsonObject).getOrThrow(false, Northstar.LOGGER::error);
//		            spacedimensions.removeIf(spacedimension -> spacedimension.level().equals(SpaceDimension.level()));
//		            spacedimensions.add(newSpaceDimension);
///		        }
//		
//		        DimensionStats.updateSpaceDimensions(spacedimensions);
//		        profiler.pop();
//		    }
//		    
//		    public static Set<SpaceDimension> spacedimensions() {
///		        return SPACEDIMENSIONS;
//		    }
//		    
//		    
//		
//		    public static void readDimensionStats(FriendlyByteBuf buf) {
//		        try {
//		            DimensionStats.updateSpaceDimensions(SpaceDimension.CODEC.listOf().parse(YabnOps.COMPRESSED, YabnParser.parse(new ByteBufByteReader(buf)))
//		                .result()
//		                .orElseThrow());
//		        } catch (Exception e) {
//		            Northstar.LOGGER.error("Failed to parse planet data!");
//		            e.printStackTrace();
//		            DimensionStats.updateSpaceDimensions(List.of());
//		        }
//		    }
//		
//		
//		}
//