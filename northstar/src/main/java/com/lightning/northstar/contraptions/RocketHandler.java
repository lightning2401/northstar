package com.lightning.northstar.contraptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.commons.lang3.mutable.MutableInt;

import com.ibm.icu.impl.Pair;
import com.lightning.northstar.Northstar;
import com.lightning.northstar.NorthstarPackets;
import com.lightning.northstar.block.tech.rocket_station.RocketStationBlockEntity;
import com.lightning.northstar.world.dimension.NorthstarDimensions;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundChangeDifficultyPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.portal.PortalForcer;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.level.storage.LevelData;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.network.PacketDistributor;

@EventBusSubscriber(modid = Northstar.MOD_ID, bus = Bus.FORGE)
public class RocketHandler {
	public static List<RocketContraptionEntity> ROCKETS = new ArrayList<>();
	public static List<Pair<Level, BlockPos>> TICKET_QUEUE = new ArrayList<>();
	public static HashMap<Pair<UUID, BlockPos>, Integer> CONTROL_QUEUE = new HashMap<Pair<UUID, BlockPos>, Integer>();
	public static long eventTickNumber;
	public static long eventTickNumberCheck;
	private static UUID pilotID;
	static int pp = 0;
	
	@SubscribeEvent
    public static void onWorldTick(TickEvent.LevelTickEvent event){
		eventTickNumber = event.level.getGameTime();
		
		
		// this is pretty much only clientside
		if(!CONTROL_QUEUE.isEmpty() && eventTickNumber > eventTickNumberCheck) {
			HashMap<Pair<UUID, BlockPos>, Integer> destroy = new HashMap<Pair<UUID,BlockPos>, Integer>();
			for(Entry<Pair<UUID, BlockPos>, Integer> entries : CONTROL_QUEUE.entrySet()) {
				if(entries.getKey().first != null)
				{Player player = event.level.getPlayerByUUID(entries.getKey().first);
				if(player != null) {
						RocketContraptionEntity rocket = ((RocketContraptionEntity)event.level.getEntity(entries.getValue()));
						if(rocket != null) {
							if(!rocket.getControllingPlayer().isEmpty()) {
								if(rocket.getControllingPlayer().get() == player.getUUID()) {
									destroy.put(entries.getKey(), entries.getValue());
									continue;
								}
							}
							
							rocket.handlePlayerInteraction(player, entries.getKey().second, Direction.NORTH, InteractionHand.MAIN_HAND);
							destroy.put(entries.getKey(), entries.getValue());
						}
					}
				}
			}
			for(Entry<Pair<UUID, BlockPos>, Integer> entries : destroy.entrySet()) {
				CONTROL_QUEUE.remove(entries.getKey(), entries.getValue());
			}			
		}
		if(event.level.isClientSide)
			return;
		
		if(ROCKETS.size() != 0) {
			pp++;
			for(int p = 0; p < ROCKETS.size(); p++) {
				if( pp % 800 == 0) {
				if(ROCKETS.get(p).level().dimension() != ROCKETS.get(p).destination && ROCKETS.get(p).getY() > 1750) {
					changeDim(ROCKETS.get(p), event.level);
					ROCKETS.remove(ROCKETS.get(p));
				}
				}
			}
		}
		
		if(eventTickNumber > eventTickNumberCheck) {
			//deleting contents of the queue
			if(!TICKET_QUEUE.isEmpty()) {
				List<Pair<Level, BlockPos>> DELETE_QUEUE = new ArrayList<>();
				for(Pair<Level,BlockPos> entries : TICKET_QUEUE) {
					if(event.level.dimension() == entries.first.dimension()) {
						if(entries.first.getBlockEntity(entries.second) instanceof RocketStationBlockEntity rsbe) {
							rsbe.container.setItem(0, new ItemStack(Blocks.AIR.asItem()));
							DELETE_QUEUE.add(entries);
						}
					}
				}
				for(Pair<Level,BlockPos> entries : DELETE_QUEUE) {
					TICKET_QUEUE.remove(entries);
				}
			}
		}
    	
    }
	
	public static void changeDim(RocketContraptionEntity entity, Level level) {
		if (entity == null)
			return;
		entity.startLanding();
		ResourceKey<Level> dest;
		if(entity.destination == null) {
			dest = NorthstarDimensions.MOON_DIM_KEY;
		}else {dest = entity.destination;}
		ServerLevel destLevel = entity.level().getServer().getLevel(dest);
		HashMap<Entity,Integer> seatMap = new HashMap<Entity,Integer>();
		UUID controller = null;
		Map<Entity, MutableInt> colliders = new HashMap<Entity, MutableInt>();
		for(Entity passengers : entity.entitiesInContraption) {
			if(passengers.level().getServer().getLevel(passengers.level().dimension()) != destLevel && !passengers.level().isClientSide) {
				if(passengers instanceof ServerPlayer) {
					if(!entity.getControllingPlayer().isEmpty()) {
						if(entity.getControllingPlayer().get() == passengers.getUUID())
							pilotID = passengers.getUUID();

					}
					if(destLevel == null) {
						destLevel = passengers.level().getServer().getLevel(passengers.level().dimension());
					}
					changePlayerDimension(destLevel, (ServerPlayer) passengers, new PortalForcer(destLevel), seatMap, entity.getContraption(), entity, controller);		
					continue;
				}
				changeDimensionCustom(destLevel, passengers, new PortalForcer(destLevel), seatMap, colliders, entity.getContraption(), entity, controller);
			}
		}
		changeDimensionCustom(destLevel, entity, new PortalForcer(destLevel), seatMap, colliders, entity.getContraption(), entity, controller);
		eventTickNumberCheck = eventTickNumber + 70;
		
	}
	
	public static Entity changeDimensionCustom(ServerLevel pDestination, Entity entity, net.minecraftforge.common.util.ITeleporter teleporter,
			HashMap<Entity,Integer> seatMap, Map<Entity, MutableInt> colliders,RocketContraption contrap, RocketContraptionEntity contrapEnt, UUID controller) {
	      if (!net.minecraftforge.common.ForgeHooks.onTravelToDimension(entity, pDestination.dimension())) return null;
	      if (entity.level() instanceof ServerLevel && !entity.isRemoved()) {
	    	  entity.level().getProfiler().push("changeDimension");
	    	  int seatNumber = -12345;
	    	  if(contrap.getSeatOf(entity.getUUID()) != null) {
	    		  Map<UUID, Integer> seatMapping = contrap.getSeatMapping();
	    		  for (Map.Entry<UUID, Integer> entry : seatMapping.entrySet()) {
	    			  if(entry.getKey() == entity.getUUID()) {
	    				  seatNumber = entry.getValue();
	    			  }
	    		  }
	    	  }	    	  
	    	  entity.level().getProfiler().push("reposition");

	    	  System.out.println(entity);

	    	  Entity transportedEntity = teleporter.placeEntity(entity, (ServerLevel) entity.level(), pDestination, entity.getYRot(), spawnPortal -> {
	            	
	            entity.level().getProfiler().popPush("reloading");
	            Entity newentity = entity.getType().create(pDestination);
	            System.out.println(newentity);
	            System.out.println(entity.getType());
	            
	            if (newentity != null) {
	            	newentity.restoreFrom(entity);
	            	newentity.moveTo(entity.position().x, entity.position().y, entity.position().z, entity.getYRot(), entity.getXRot());
	            	newentity.setDeltaMovement(entity.getDeltaMovement());
	               pDestination.addDuringTeleport(newentity);
	            }
	            
	            return newentity;
	            });
	    	  	if(entity instanceof RocketContraptionEntity rce) {
	    	  		
	    	  		for(Integer entint : seatMap.values()) {
	    	  			for(Entity ents : seatMap.keySet()) {
	    	  				if(seatMap.get(ents) == entint)
	    	  				((RocketContraptionEntity)transportedEntity).addSittingPassenger(ents, entint);;
	    	  			}
	    	  		}
	    	  		for(MutableInt entint : colliders.values()) {
	    	  			for(Entity ents : colliders.keySet()) {
	    	  				if(colliders.get(ents) == entint)
	    	  				((RocketContraptionEntity)transportedEntity).registerColliding(ents);
	    	  			}
	    	  		}
	    	  		if(controller == null) {
	    	  			((RocketContraptionEntity)transportedEntity).setControllingPlayer(controller);
	    	  		}
	    	  		((RocketContraptionEntity)transportedEntity).owner = rce.owner;
	    	  		((RocketContraptionEntity)transportedEntity).isUsingTicket = rce.isUsingTicket;
	    	  		((RocketContraptionEntity)transportedEntity).visualEngineCount = rce.visualEngineCount;
	    	  		((RocketContraptionEntity)transportedEntity).localControlsPos = rce.localControlsPos;
	    	  		((RocketContraptionEntity)transportedEntity).setControllingPlayer(controller);
					NorthstarPackets.getChannel().send(PacketDistributor.ALL.noArg(),
							new RocketControlPacket(pilotID, ((RocketContraptionEntity)transportedEntity).getId(), rce.localControlsPos));
	    	  		
	    	  		
	    	  	}
	    	  	if(seatNumber != -12345 ) {
	    	  		seatMap.put(transportedEntity, seatNumber);
	    	  	}
		    	if(contrapEnt.collidingEntities.containsKey(entity)) {
		    		colliders.put(transportedEntity, contrapEnt.collidingEntities.get(entity));
		    		// TRUCK NUTS!!!!!!
		    	}
	            
	            entity.setRemoved(Entity.RemovalReason.CHANGED_DIMENSION);
	            entity.level().getProfiler().pop();
	            ((ServerLevel)entity.level()).resetEmptyTime();
	            pDestination.resetEmptyTime();
	            entity.level().getProfiler().pop();
	            return transportedEntity;
	      } else {
	         return null;
	      }
	}
	public static Entity changePlayerDimension(ServerLevel pDestination, ServerPlayer entity, net.minecraftforge.common.util.ITeleporter teleporter, HashMap<Entity,Integer> seatMap, RocketContraption contrap, RocketContraptionEntity contrapEnt, UUID controller) {
	      if (!net.minecraftforge.common.ForgeHooks.onTravelToDimension(entity, pDestination.dimension())) return null;
	      ((ServerPlayer) entity).isChangingDimension();

	      ServerLevel serverlevel = entity.serverLevel();
	          LevelData leveldata = pDestination.getLevelData();
	          entity.connection.send(new ClientboundRespawnPacket(pDestination.dimensionTypeId(), pDestination.dimension(), BiomeManager.obfuscateSeed(pDestination.getSeed()), entity.gameMode.getGameModeForPlayer(), entity.gameMode.getPreviousGameModeForPlayer(), pDestination.isDebug(), pDestination.isFlat(), (byte)3, entity.getLastDeathLocation(), entity.getPortalCooldown()));
	          entity.connection.send(new ClientboundChangeDifficultyPacket(leveldata.getDifficulty(), leveldata.isDifficultyLocked()));
	          PlayerList playerlist = entity.server.getPlayerList();
	          int seatNumber = -12345;
	    	  if(contrap.getSeatOf(entity.getUUID()) != null) {
	    		  Map<UUID, Integer> seatMapping = contrap.getSeatMapping();
	    		  for (Map.Entry<UUID, Integer> entry : seatMapping.entrySet()) {
	    			  if(entry.getKey() == entity.getUUID()) {
	    				  seatNumber = entry.getValue();
	    			  }
	    		  }
	    	  }
	    	  if(contrapEnt.hasControllingPassenger() && contrapEnt.getControllingPlayer() != null) {
	    		  controller = contrapEnt.getControllingPlayer().get();
	    	  }
	          playerlist.sendPlayerPermissionLevel(entity);
	          pDestination.removePlayerImmediately(entity, Entity.RemovalReason.CHANGED_DIMENSION);
	          entity.revive();
	          PortalInfo portalinfo = new PortalInfo(entity.position(), entity.getDeltaMovement(), entity.getYRot(), entity.getXRot());
	             Entity e = teleporter.placeEntity(entity, serverlevel, pDestination, entity.getYRot(), spawnPortal -> {//Forge: Start vanilla logic
	             serverlevel.getProfiler().push("moving");
	             serverlevel.getProfiler().pop();
	             serverlevel.getProfiler().push("placing");
	             entity.setServerLevel(pDestination);
	             pDestination.addDuringPortalTeleport(entity);
	             entity.setXRot(portalinfo.xRot);
	             entity.setYRot(portalinfo.yRot);
	             entity.moveTo(portalinfo.pos.x, portalinfo.pos.y + 1, portalinfo.pos.z);
	             serverlevel.getProfiler().pop();
	             CriteriaTriggers.CHANGED_DIMENSION.trigger(entity, entity.level().dimension(), pDestination.dimension());
	             return entity;//forge: this is part of the ITeleporter patch
	             });//Forge: End vanilla logic
	             if (e != entity) throw new java.lang.IllegalArgumentException(String.format(java.util.Locale.ENGLISH, "Teleporter %s returned not the player entity but instead %s, expected PlayerEntity %s", teleporter, e, entity));
	             entity.connection.send(new ClientboundPlayerAbilitiesPacket(entity.getAbilities()));
	             playerlist.sendLevelInfo(entity, pDestination);
	             playerlist.sendAllPlayerInfo(entity);

	             for(MobEffectInstance mobeffectinstance : entity.getActiveEffects()) {
	                entity.connection.send(new ClientboundUpdateMobEffectPacket(entity.getId(), mobeffectinstance));
	             
	          }
	          if(seatNumber != -12345 ) {
	        	  seatMap.put(entity, seatNumber);
			  }
	          
	             
	          return entity;
	       
	}
	
	public static void deleteTicket(Level level, BlockPos pos) {
		TICKET_QUEUE.add(Pair.of(level, pos.above()));
		eventTickNumberCheck = eventTickNumber + 3;
	}
	
	
	public static boolean isInRocket(Entity entity) {
		for(RocketContraptionEntity rockets : ROCKETS) {
			if(rockets.entitiesInContraption.contains(entity)) {
				return true;
			}
		}
		return false;
	}
	
	public static void register() {
		System.out.println("Handling rockets for " + Northstar.MOD_ID);
	}

}
