package com.lightning.northstar.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;

import com.lightning.northstar.Northstar;
import com.lightning.northstar.NorthstarTags;
import com.lightning.northstar.particle.GlowstoneParticleData;
import com.lightning.northstar.particle.NorthstarParticles;
import com.lightning.northstar.world.dimension.NorthstarPlanets;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = Northstar.MOD_ID, bus = Bus.FORGE)
public class OxygenStuff {
	public static HashMap<BlockPos, Integer> oxygenBlocks = new HashMap<BlockPos, Integer>();
	public static HashMap<Set<BlockPos>,ResourceKey<Level>> oxygenSources = new HashMap<Set<BlockPos>,ResourceKey<Level>>();
	public static HashMap<Set<BlockPos>,ResourceKey<Level>> tickingQueue = new HashMap<Set<BlockPos>,ResourceKey<Level>>();
	public static List<LivingEntity> oxygenatedEntities = new ArrayList<LivingEntity>();
	public static int power = 2000;
	public static int maximumOxy = 2000;

	public static boolean hasOxygen(BlockPos pos, ResourceKey<Level> level) {
		if(!oxygenSources.containsValue(level)) {return false;}
    	for(Entry<Set<BlockPos>, ResourceKey<Level>> blocks:	oxygenSources.entrySet()) {
    		if(blocks.getValue() == level) {
    			if(blocks.getKey().contains(pos)) {
    				return true;
    			}
    		}
    	}
    	return false;
	}
	@SubscribeEvent
    public static void onWorldTick(TickEvent.LevelTickEvent event){
    	long t = event.level.getGameTime();
    	if(t % 40 == 0) {
    		for(Entry<Set<BlockPos>, ResourceKey<Level>> blocks:	oxygenSources.entrySet()) {
    			if(blocks.getValue() == event.level.dimension()) {
    				for(BlockPos pos : blocks.getKey()) {
    					event.level.addParticle(new GlowstoneParticleData(), pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, 0,0,0);
    				}
    			}
    			
    		}
    	}
    	
    }	
    public static Set<BlockPos> spreadOxy(Level level, Set<BlockPos> list, int maxSize) {
    	List<BlockPos> newBlocks = new ArrayList<BlockPos>();
    	newBlocks.addAll(list);
		for(int i = 0; i < newBlocks.size() && i < maxSize;i++) {
		    	BlockPos pos = newBlocks.get(i);
		    	for(Direction direction : Direction.values()) {
		    		BlockPos blockpos = pos.relative(direction);
		    		if(list.contains(blockpos)) {
		    			continue;
		    		}
		    		if(getIsAir(level.getBlockState(blockpos)) && list.size() < maxSize) 
		    		{list.add(blockpos); newBlocks.add(blockpos);}
		    	}
		}
		return list;   
    }

	public static void removeSource(BlockPos pos, Level level, Set<BlockPos> list, Set<BlockPos> newlist) {
		Set<BlockPos> templist = list;
		oxygenSources.remove(list, level.dimension());
		if(!level.isClientSide) {
			for(BlockPos block : templist) {
				if(!newlist.contains(block)) {
					if(!level.getBlockState(block).isAir()) {
						level.getBlockState(block).tick((ServerLevel) level, block, level.random);
					}
					level.setBlock(block, level.getBlockState(block).updateShape(Direction.NORTH, level.getBlockState(block), level, block, block), 2);
				}
			}
		}
	}
    
    public static List<BlockPos> spreadOxyNew(BlockPos pos, Level level, List<BlockPos> list, int maxSize) {
    	java.util.List<BlockPos> newblockslist = new ArrayList<BlockPos>();
    	boolean flag = false;
        BlockPos.MutableBlockPos blockpos = new BlockPos.MutableBlockPos();
        for(Direction direction : Direction.values()) {
        	blockpos.setWithOffset(pos, direction);
        	BlockPos newpos = new BlockPos(blockpos.getX(), blockpos.getY(), blockpos.getZ());
        	if(list.contains(blockpos))
        		continue;
        	if(getIsAir(level.getBlockState(newpos)) && list.size() < maxSize) 
        	{list.add(newpos); newblockslist.add(newpos);flag = true;}
        }
        if(flag) {
        	int i = 0;
        	while(newblockslist.size() != 0 || list.size() < maxSize) {
        		for(;newblockslist.get(i) == null || list.size() > maxSize; i++) {
        			BlockPos blockpos2 = newblockslist.get(i);
//        			System.out.println(blockpos2);
        			BlockPos.MutableBlockPos mutpos = blockpos2.mutable();
        			for(Direction direction : Direction.values()) {
                		mutpos.setWithOffset(pos, direction);
                		BlockPos newpos = new BlockPos(mutpos.getX(), mutpos.getY(), mutpos.getZ());
                		if(getIsAir(level.getBlockState(newpos)) && list.size() < maxSize && !list.contains(blockpos2)) 
                		{list.add(newpos); newblockslist.add(newpos);}
                	}
                	newblockslist.remove(blockpos2);
                	i--;
        		}
        	}
        }
		return list;   
    }
    public static List<BlockPos> spreadOxy2(Level level, List<BlockPos> list, int maxSize, List<BlockPos> parentList) {
    	java.util.List<BlockPos> newblockslist = new ArrayList<BlockPos>();
        BlockPos.MutableBlockPos blockpos = new BlockPos.MutableBlockPos();
        for(int i = 0; i < list.size() && list.size() < maxSize; i++) {
        	BlockPos iPos = list.get(i);
//        	System.out.println(list.size());
        	for(Direction direction : Direction.values()) {
        		blockpos.setWithOffset(iPos, direction);
        		BlockPos newpos = new BlockPos(blockpos.getX(), blockpos.getY(), blockpos.getZ());
        		if(list.contains(newpos) || parentList.contains(newpos))
        			continue;
        		if(getIsAir(level.getBlockState(newpos)) && list.size() < maxSize) 
        		{newblockslist.add(newpos);}
        	}
        }
        parentList.addAll(newblockslist);
		return newblockslist;
    }
	
    public static boolean getIsAir(BlockState state) {
        return state.is(NorthstarTags.NorthstarBlockTags.AIR_PASSES_THROUGH.tag) || !state.getFluidState().isEmpty();
     }
    
    
	public static ItemStack getOxy(LivingEntity entity) {
		for(ItemStack items : entity.getArmorSlots())
		{
			if (items.is(NorthstarTags.NorthstarItemTags.OXYGEN_SOURCES.tag)){
				return items;
			}
		}
		
		
		return new ItemStack(Items.AIR);
		
	}
	
	public static void register() {
		System.out.println("Checking for oxygen for " + Northstar.MOD_ID);
	}
	@SuppressWarnings("unused")
	@SubscribeEvent
	public static void onUpdateLivingEntity(LivingTickEvent event) {
		LivingEntity entity = event.getEntity();
		Level world = entity.level;
		boolean tick = world.getGameTime() % 20 == 0;
		if (world == null)
			return;
		if(tick) {
			boolean creativeFlag = false;
			if(entity instanceof Player) {
				creativeFlag = ((Player) entity).isCreative();
			}
			if(checkForAir(entity) || NorthstarPlanets.getPlanetOxy(world.dimension()) || creativeFlag) {
				return;
			}
			
			else{
//				System.out.println("clover would like to cry but they have no eyes");
				for(ItemStack armor : entity.getArmorSlots()) {
					if(armor.getTag() == null)
						continue;
					if(armor.getTag().contains("Oxygen") && (armor.is(NorthstarTags.NorthstarItemTags.OXYGEN_SOURCES.tag)) && armor.getTag().getInt("Oxygen") > 0) {
						depleteOxy(armor);
						if(!oxygenatedEntities.contains(entity)) {
							oxygenatedEntities.add(entity);
						}
						return;
					}
				}
			}
			if(oxygenatedEntities.contains(entity)) {
				oxygenatedEntities.remove(entity);
			}
		}
		
	}
    public static boolean checkForAir(LivingEntity entity) {
    	if(entity.level.isClientSide)
    		return false;
    		for(Entry<Set<BlockPos>, ResourceKey<Level>> blocks:	oxygenSources.entrySet()) {
    			if(blocks.getValue() == entity.level.dimension()) {
    				if(blocks.getKey().contains(entity.blockPosition()) && blocks.getKey().size() < maximumOxy) {
    					return true;
    				}
				}
    		}		
    		
    	
		return false;
	}
	public static void depleteOxy(ItemStack stack) {
		CompoundTag tag = stack.getTag();
		ListTag lore = new ListTag();
		int oxy = tag.getInt("Oxygen");
		int newOxy = Mth.clamp(oxy - 1, 0, 5000);
		lore.add(StringTag.valueOf(Component.Serializer.toJson(Component.literal( "Oxygen: " + newOxy + "mb").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY).withItalic(false))).toString())); 
		tag.remove("Oxygen");
		tag.putInt("Oxygen", newOxy);
		stack.getOrCreateTagElement("display").put("Lore", lore);
	}

}
