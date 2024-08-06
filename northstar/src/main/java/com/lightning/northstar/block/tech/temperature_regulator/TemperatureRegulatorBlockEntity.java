package com.lightning.northstar.block.tech.temperature_regulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lightning.northstar.particle.SnowflakeParticleData;
import com.lightning.northstar.world.TemperatureStuff;
import com.lightning.northstar.world.dimension.NorthstarPlanets;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.equipment.goggles.IHaveHoveringInformation;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.utility.Lang;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TemperatureRegulatorBlockEntity extends KineticBlockEntity implements IHaveGoggleInformation, IHaveHoveringInformation{
	private HashMap<BlockPos, Integer> TEMP_ZONES = new HashMap<BlockPos, Integer>();
	public int temp = 20;
	public int tempChange = 0;
	public int maxSize = 0;
	public boolean envFill = true;
	public boolean prevEnvFill = false;
	public int sizeX = 16;
	public int sizeY = 16;
	public int sizeZ = 16;
	public int offsetX = 0;
	public int offsetY = 0;
	public int offsetZ = 0;

	public TemperatureRegulatorBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
		super(typeIn, pos, state);
	}
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}
	
	@Override   
	public void tick() {
		super.tick();
		if(this.level.getGameTime() % 40 == 0) {
			maxSize = (int) (Math.abs(this.speed) * 20);
			if(Math.abs(this.speed) > 0 && !this.overStressed) 
			{addParticles(this.temp > NorthstarPlanets.getPlanetTemp(this.level.dimension()) && level.isClientSide, this.speed / 64);}
//			if(this.level.isClientSide)
//				return;
//			System.out.println(entity.temp);
//			System.out.println("size X: " + entity.sizeX);
//			System.out.println("size Y: " + entity.sizeY);
//			System.out.println("size Z: " + entity.sizeZ);
//			System.out.println("offset X: " + entity.offsetX);
//			System.out.println("offset Y: " + entity.offsetY);
//			System.out.println("offset Z: " + entity.offsetZ);
//			System.out.println("PLANET TEMP DIF: " + Math.abs(NorthstarPlanets.getPlanetTemp(this.level.dimension()) - this.temp) / 2);
			if(Math.abs(this.speed) > 0 && !this.overStressed) {
				if(!this.TEMP_ZONES.containsKey(this.getBlockPos())) 
				{this.TEMP_ZONES.put(this.getBlockPos().above(), this.temp);}
				
				if(!TemperatureStuff.temperatureSources.containsKey(this.TEMP_ZONES)) 
				{TemperatureStuff.temperatureSources.put(this.TEMP_ZONES,this.level.dimension());}
				
				if(this.envFill)
				{HashMap<BlockPos, Integer> newList = new HashMap<BlockPos, Integer>();
					 newList.put(getBlockPos().above(),temp);
				 TemperatureStuff.spreadTemp(this.level, newList, maxSize, this.temp);
				  if(!newList.equals(TEMP_ZONES)) {
					  HashMap<BlockPos, Integer> temptemp_zones = TEMP_ZONES;
					  if(temptemp_zones.get(worldPosition.above()) == newList.get(worldPosition.above())) {
						  temptemp_zones.keySet().removeAll(newList.keySet());
					  }
					  TemperatureStuff.removeSource(worldPosition, level, temptemp_zones, TEMP_ZONES);
					  TemperatureStuff.temperatureSources.put(newList, level.dimension());
					  TEMP_ZONES.clear();
					  TEMP_ZONES = newList;
				  }
				  this.prevEnvFill = envFill;
				  
				 }
				else {if(this.prevEnvFill) {removeTemp(this);this.prevEnvFill = false;}TemperatureStuff.markTemp(this.getBlockPos(), this.level, this.TEMP_ZONES, this.temp, this.sizeX, this.sizeY, this.sizeZ, this.offsetX, this.offsetY, this.offsetZ);}
			}
			else {
				removeTemp(this);
			}
//			System.out.println(entity.temp);

		}
    }
	
	public void removeTemp(TemperatureRegulatorBlockEntity entity) {
		TemperatureStuff.removeSource(worldPosition, level, null, TEMP_ZONES);
		entity.TEMP_ZONES.clear();
	}
	
	public void changeTemp(int i) {
		this.temp = i;
		this.temp = Mth.clamp(temp, -273, 1000);
//		this.removeTemp(this);
		this.tick();
//		System.out.println(this.temp);
	}
	public void changeSize(int xchange, int ychange, int zchange, int xOffsetChange, int yOffsetChange, int zOffsetChange, boolean envfi) {
		sizeX = xchange;
		sizeY = ychange;
		sizeZ = zchange;
		offsetX = xOffsetChange;
		offsetY = yOffsetChange;
		offsetZ = zOffsetChange;
		envFill = envfi;
	}
	
	public void addParticles(boolean isWarm, float spinMod) {
		RandomSource random = level.getRandom();
		BlockPos pos = getBlockPos();
		int test = random.nextInt(6);
//		System.out.println("help mEE");
		if(isWarm) {
			for(int i = 0; i < test;i++) {
	            double newX = pos.getX() + random.nextDouble();
	            double newY = pos.getY() + 0.7 + random.nextDouble();
	            double newZ = pos.getZ() + random.nextDouble();
				level.addParticle(ParticleTypes.FLAME, newX, newY, newZ,
			    random.nextFloat() * 0.05 * (random.nextBoolean() ? -1 : 1),
			    random.nextFloat() * 0.05,
			    random.nextFloat() * 0.05 * (random.nextBoolean() ? -1 : 1));
			}
		}else {
			for(int i = 0; i < test;i++) {
	            double newX = pos.getX() + random.nextDouble();
	            double newY = pos.getY() + 0.7 + random.nextDouble();
	            double newZ = pos.getZ() + random.nextDouble();
				level.addParticle(new SnowflakeParticleData(), newX, newY, newZ,
			    random.nextFloat() * 0.05 * (random.nextBoolean() ? -1 : 1) * spinMod,
			    random.nextFloat() * -0.08,
			    random.nextFloat() * 0.05 * (random.nextBoolean() ? -1 : 1) * spinMod);
			}
		}
	}
	
	@Override
	public boolean addToTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		Lang.translate("gui.goggles.temperature_regulator")
			.forGoggles(tooltip);
		Lang.translate("gui.goggles.blocks_filled")
		.style(ChatFormatting.GRAY)
		.forGoggles(tooltip);
		if(envFill) 
		{Lang.builder()
		.add(Lang.number(TEMP_ZONES.size())
			.style(ChatFormatting.AQUA))
		.text(ChatFormatting.GRAY, " / ")
		.add(Lang.number(maxSize)
			.style(ChatFormatting.DARK_GRAY))
		.forGoggles(tooltip, 1);}
		else
		{Lang.builder()
		.add(Lang.number(sizeX)
		.style(ChatFormatting.AQUA))
		.text(ChatFormatting.DARK_GRAY, " x ")
		.add(Lang.number(sizeY)
		.style(ChatFormatting.AQUA))
		.text(ChatFormatting.DARK_GRAY, " x ")
		.add(Lang.number(sizeZ)
		.style(ChatFormatting.AQUA))
		.forGoggles(tooltip, 1);}
		return true;
	}
	
	@Override
	public void write(CompoundTag tag, boolean clientPacket) {
		tag.putInt("sizeX", sizeX);
		tag.putInt("sizeY", sizeY);
		tag.putInt("sizeZ", sizeZ);
		
		tag.putInt("offsetX", offsetX);
		tag.putInt("offsetY", offsetY);
		tag.putInt("offsetZ", offsetZ);
		
		tag.putBoolean("envFill", envFill);
		
		tag.putInt("temp", this.temp);
		super.write(tag, clientPacket);
	}
	
	@Override
	protected void read(CompoundTag tag, boolean clientPacket) {		
		sizeX = tag.getInt("sizeX");
		sizeY = tag.getInt("sizeY");
		sizeZ = tag.getInt("sizeZ");
		
		offsetX = tag.getInt("offsetX");
		offsetY = tag.getInt("offsetY");
		offsetZ = tag.getInt("offsetZ");
		
		envFill = tag.getBoolean("envFill");
		
		temp = tag.getInt("temp");
		super.read(tag, clientPacket);
	}
}
