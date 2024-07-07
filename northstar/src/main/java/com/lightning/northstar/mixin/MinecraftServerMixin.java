package com.lightning.northstar.mixin;

import java.security.KeyPair;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.lightning.northstar.Northstar;
import com.lightning.northstar.world.dimension.NorthstarPlanets;
import com.mojang.authlib.GameProfile;

import net.minecraft.CrashReport;
import net.minecraft.ReportedException;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.MiscOverworldFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerFunctionManager;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.bossevents.CustomBossEvents;
import net.minecraft.server.level.PlayerRespawnLogic;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.server.players.PlayerList;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.ai.village.VillageSiege;
import net.minecraft.world.entity.npc.CatSpawner;
import net.minecraft.world.entity.npc.WanderingTraderSpawner;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.border.BorderChangeListener;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.PatrolSpawner;
import net.minecraft.world.level.levelgen.PhantomSpawner;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.storage.CommandStorage;
import net.minecraft.world.level.storage.DerivedLevelData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.level.storage.WorldData;


@Mixin(MinecraftServer.class)
public class MinecraftServerMixin{
	   protected final LevelStorageSource.LevelStorageAccess storageSource;
	   private final Map<ResourceKey<Level>, ServerLevel> levels = Maps.newLinkedHashMap();
	   private PlayerList playerList;
	   @Nullable
	   private String motd;
	   public final long[] tickTimes = new long[100];
	   @Nullable
	   private KeyPair keyPair;
	   @Nullable
	   private GameProfile singleplayerProfile;
	   protected long nextTickTime = Util.getMillis();

	   private final ServerScoreboard scoreboard = new ServerScoreboard((MinecraftServer) (Object) this);
	   @Nullable
	   private CommandStorage commandStorage;
	   private final CustomBossEvents customBossEvents = new CustomBossEvents();
	   private final ServerFunctionManager functionManager;
	   private final Executor executor;
	   @Nullable
	   private String serverId;
	   @Shadow
	   @Final
	   protected final WorldData worldData;
	   
	   public MinecraftServerMixin() {
		    this.storageSource = null;
			this.functionManager = null;
			this.worldData = null;
		      if (!this.worldData.worldGenSettings().dimensions().containsKey(LevelStem.OVERWORLD)) {
		         throw new IllegalStateException("Missing Overworld dimension data");
		      } else {
		         this.executor = Util.backgroundExecutor();
		      }
		   }
	

	   ///HELP!!!!!!!!!!
	   /// IM STRUGGLING!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	   //I JUST WANT DIFFERENT PLANETS TO HAVE DIFFERENT SEEDS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    @Inject(method = "createLevels", at = @At("HEAD"), cancellable = true)
	private void createLevels(ChunkProgressListener p_129816_, CallbackInfo info) {
    	MinecraftServer mcServer = (MinecraftServer) (Object) this;
    	ServerLevelData serverleveldata = this.worldData.overworldData();
        WorldGenSettings worldgensettings = this.worldData.worldGenSettings();
        boolean flag = worldgensettings.isDebug();
        long i = worldgensettings.seed();
        long j = BiomeManager.obfuscateSeed(i);
        List<CustomSpawner> list = ImmutableList.of(new PhantomSpawner(), new PatrolSpawner(), new CatSpawner(), new VillageSiege(), new WanderingTraderSpawner(serverleveldata));
        Registry<LevelStem> registry = worldgensettings.dimensions();
        LevelStem levelstem = registry.get(LevelStem.OVERWORLD);
        ServerLevel serverlevel = new ServerLevel(mcServer, this.executor, this.storageSource, serverleveldata, Level.OVERWORLD, levelstem, p_129816_, flag, j, list, true);
        this.levels.put(Level.OVERWORLD, serverlevel);
        DimensionDataStorage dimensiondatastorage = serverlevel.getDataStorage();
        this.readScoreboard(dimensiondatastorage);
        this.commandStorage = new CommandStorage(dimensiondatastorage);
        WorldBorder worldborder = serverlevel.getWorldBorder();
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.level.LevelEvent.Load(levels.get(Level.OVERWORLD)));
        if (!serverleveldata.isInitialized()) {
           try {
              setInitialSpawn(serverlevel, serverleveldata, worldgensettings.generateBonusChest(), flag);
              serverleveldata.setInitialized(true);
              if (flag) {
                 this.setupDebugLevel(this.worldData);
              }
           } catch (Throwable throwable1) {
              CrashReport crashreport = CrashReport.forThrowable(throwable1, "Exception initializing level");

              try {
                 serverlevel.fillReportDetails(crashreport);
              } catch (Throwable throwable) {
              }

              throw new ReportedException(crashreport);
           }

           serverleveldata.setInitialized(true);
        }

        this.getPlayerList().addWorldborderListener(serverlevel);
        if (this.worldData.getCustomBossEvents() != null) {
           this.getCustomBossEvents().load(this.worldData.getCustomBossEvents());
        }
        int num = 0;
        for(Map.Entry<ResourceKey<LevelStem>, LevelStem> entry : registry.entrySet()) {
           num++;
           ResourceKey<LevelStem> resourcekey = entry.getKey();
           if (resourcekey != LevelStem.OVERWORLD) {
        	  long seed = j;
        	  if(NorthstarPlanets.isCustomDimension(resourcekey.location())) {
        		  seed = j + (num * 10);
        		  Northstar.LOGGER.debug("Registering " + resourcekey.location());
        	  }
              ResourceKey<Level> resourcekey1 = ResourceKey.create(Registry.DIMENSION_REGISTRY, resourcekey.location());
              DerivedLevelData derivedleveldata = new DerivedLevelData(this.worldData, serverleveldata);
              ServerLevel serverlevel1 = new ServerLevel(mcServer, this.executor, this.storageSource, derivedleveldata, resourcekey1, entry.getValue(), p_129816_, flag, seed, ImmutableList.of(), false);
              worldborder.addListener(new BorderChangeListener.DelegateBorderChangeListener(serverlevel1.getWorldBorder()));
              this.levels.put(resourcekey1, serverlevel1);
              net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.level.LevelEvent.Load(levels.get(resourcekey)));
           }
        }

        worldborder.applySettings(serverleveldata.getWorldBorder());		   
        info.cancel();
	}

    private void readScoreboard(DimensionDataStorage pDataStorage) {
        pDataStorage.computeIfAbsent(this.getScoreboard()::createData, this.getScoreboard()::createData, "scoreboard");
    }
    public ServerScoreboard getScoreboard() {
        return this.scoreboard;
    }
    
    private static void setInitialSpawn(ServerLevel pLevel, ServerLevelData pLevelData, boolean pGenerateBonusChest, boolean pDebug) {
        if (pDebug) {
           pLevelData.setSpawn(BlockPos.ZERO.above(80), 0.0F);
        } else {
           ServerChunkCache serverchunkcache = pLevel.getChunkSource();
           if (net.minecraftforge.event.ForgeEventFactory.onCreateWorldSpawn(pLevel, pLevelData)) return;
           ChunkPos chunkpos = new ChunkPos(serverchunkcache.randomState().sampler().findSpawnPosition());
           int i = serverchunkcache.getGenerator().getSpawnHeight(pLevel);
           if (i < pLevel.getMinBuildHeight()) {
              BlockPos blockpos = chunkpos.getWorldPosition();
              i = pLevel.getHeight(Heightmap.Types.WORLD_SURFACE, blockpos.getX() + 8, blockpos.getZ() + 8);
           }

           pLevelData.setSpawn(chunkpos.getWorldPosition().offset(8, i, 8), 0.0F);
           int k1 = 0;
           int j = 0;
           int k = 0;
           int l = -1;
           int i1 = 5;

           for(int j1 = 0; j1 < Mth.square(11); ++j1) {
              if (k1 >= -5 && k1 <= 5 && j >= -5 && j <= 5) {
                 BlockPos blockpos1 = PlayerRespawnLogic.getSpawnPosInChunk(pLevel, new ChunkPos(chunkpos.x + k1, chunkpos.z + j));
                 if (blockpos1 != null) {
                    pLevelData.setSpawn(blockpos1, 0.0F);
                    break;
                 }
              }

              if (k1 == j || k1 < 0 && k1 == -j || k1 > 0 && k1 == 1 - j) {
                 int l1 = k;
                 k = -l;
                 l = l1;
              }

              k1 += k;
              j += l;
           }

           if (pGenerateBonusChest) {
              ConfiguredFeature<?, ?> configuredfeature = MiscOverworldFeatures.BONUS_CHEST.value();
              configuredfeature.place(pLevel, serverchunkcache.getGenerator(), pLevel.random, new BlockPos(pLevelData.getXSpawn(), pLevelData.getYSpawn(), pLevelData.getZSpawn()));
           }

        }
     }
    
    private void setupDebugLevel(WorldData pWorldData) {
        pWorldData.setDifficulty(Difficulty.PEACEFUL);
        pWorldData.setDifficultyLocked(true);
        ServerLevelData serverleveldata = pWorldData.overworldData();
        serverleveldata.setRaining(false);
        serverleveldata.setThundering(false);
        serverleveldata.setClearWeatherTime(1000000000);
        serverleveldata.setDayTime(6000L);
        serverleveldata.setGameType(GameType.SPECTATOR);
     }


    public PlayerList getPlayerList() {
       return this.playerList;
    }
    public CustomBossEvents getCustomBossEvents() {
        return this.customBossEvents;
     }

}
