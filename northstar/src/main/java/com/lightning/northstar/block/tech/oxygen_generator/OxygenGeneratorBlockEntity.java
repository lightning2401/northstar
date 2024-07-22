package com.lightning.northstar.block.tech.oxygen_generator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.lightning.northstar.NorthstarTags;
import com.lightning.northstar.fluids.NorthstarFluids;
import com.lightning.northstar.particle.OxyFlowParticleData;
import com.lightning.northstar.sound.NorthstarSounds;
import com.lightning.northstar.world.OxygenStuff;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.equipment.goggles.IHaveHoveringInformation;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import com.simibubi.create.infrastructure.config.AllConfigs;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

@SuppressWarnings("removal")
public class OxygenGeneratorBlockEntity extends KineticBlockEntity implements IHaveGoggleInformation, IHaveHoveringInformation{
	public int maxOxy;
	public int minOxy;
	public Set<BlockPos> OXYGEN_BLOBS = new HashSet<BlockPos>();
	SmartFluidTankBehaviour tank;
	private int audioTick = 0;

	public OxygenGeneratorBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
		super(typeIn, pos, state);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void tick() {
//	  System.out.println("big fart" + pBlockEntity.tickCount);
		addToGoggleTooltip(new ArrayList<Component>(), false);
		boolean hasOxy = (this.tank.getPrimaryHandler().getFluid().getFluid().isSame(NorthstarFluids.OXYGEN.get()) || 
				this.tank.getPrimaryHandler().getFluid().getFluid().is(NorthstarTags.NorthstarFluidTags.IS_OXY.tag))
				&& this.tank.getPrimaryHandler().getFluid().getAmount() >= minOxy;
		
		long i = level.getGameTime();
		maxOxy = (int) (Math.abs(this.speed) * 20);
	  
		if(Math.abs(this.speed) > 0 && !isOverStressed() && hasOxy) {
			BlockPos pos = getBlockPos();
			if (level.random.nextFloat() < AllConfigs.client().fanParticleDensity.get())
				level.addParticle(new OxyFlowParticleData(getBlockPos().offset(0, 1, 0)), pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 0, 0, 0);
			audioTick++;
			if(level.isClientSide) {
				if(audioTick % 13 == 0) {
					level.playLocalSound(pos.getX(),pos.getY(),pos.getZ(), NorthstarSounds.AIRFLOW.get(), SoundSource.BLOCKS, 0.5f, 0, false);
				}		
			}
		}
		if (i % 40L == 0L && Math.abs(this.speed) > 0 && !isOverStressed() && hasOxy) {
			if(level.isClientSide)
				return;
	      
		  //this is called every 40 ticks to check if any of the blocks have changed and changes the shape of the oxygen's influence accordingly
		  //its also really laggy and makes me want to cry
		  Set<BlockPos> newList = new HashSet<BlockPos>();
//		  System.out.println("SPREAD THE DISEASE SPREAD THE DISEASE");
		  if(level.getBlockState(getBlockPos().above()).is(NorthstarTags.NorthstarBlockTags.AIR_PASSES_THROUGH.tag))
			  newList.add(getBlockPos().above());
		  if(newList.size() < maxOxy) {		 
			  OxygenStuff.spreadOxy(level, newList, maxOxy);	  
		  }
		  this.drain(6);
		  
//		  System.out.println("Oxy amount: " + this.tank.getPrimaryHandler().getFluidAmount());
		  
		  
		  
		  // I desperately need to find out a more lag friendly way to spread oxygen,
		  // but for now the old laggy method will do
		  // so ye
		  
		  //nevermind I fixed it :]
		  // I mean it still causes some strain but thats inevitable
		  if(!newList.equals(OXYGEN_BLOBS)) {
			  OxygenStuff.removeSource(source, level, OXYGEN_BLOBS, newList);
			  OxygenStuff.oxygenSources.put(newList, level.dimension());
			  OXYGEN_BLOBS.clear();
			  OXYGEN_BLOBS = newList;
		  }
      }else if(i % 40L == 0L && (Math.abs(this.speed) == 0 || isOverStressed()) || !hasOxy){
    	  removeOxy(this, OXYGEN_BLOBS);
      }
	}	public void removeOxy(OxygenGeneratorBlockEntity entity, Set<BlockPos> newlist) {
		OxygenStuff.removeSource(source, level, entity.OXYGEN_BLOBS, newlist);
		entity.OXYGEN_BLOBS.clear();
	}
	
	
	public void drain(int amount) {
		this.tank.getPrimaryHandler().drain(amount, FluidAction.EXECUTE);
	}
	
	@Override
	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		Lang.translate("gui.goggles.kinetic_stats")
		.forGoggles(tooltip);
		addStressImpactStats(tooltip, calculateStressApplied());
	
		LangBuilder mb = Lang.translate("generic.unit.millibuckets");
		Lang.translate("gui.goggles.oxygen_sealer")
			.forGoggles(tooltip);
		FluidStack fluidStack = tank.getPrimaryHandler().getFluidInTank(0);
		if(!fluidStack.getFluid().getFluidType().isAir()) {
		Lang.fluidName(fluidStack)
			.style(ChatFormatting.GRAY)
			.forGoggles(tooltip);
		}else {
			Lang.translate("gui.goggles.empty")
			.style(ChatFormatting.GRAY)
			.forGoggles(tooltip);
		}
		Lang.builder()
		.add(Lang.number(fluidStack.getAmount())
			.add(mb)
			.style(ChatFormatting.GOLD))
		.text(ChatFormatting.GRAY, " / ")
		.add(Lang.number(tank.getPrimaryHandler().getTankCapacity(0))
			.add(mb)
			.style(ChatFormatting.DARK_GRAY))
		.forGoggles(tooltip, 1);
		Lang.translate("gui.goggles.blocks_filled")
		.style(ChatFormatting.GRAY)
		.forGoggles(tooltip);
		Lang.number(OXYGEN_BLOBS.size())
		.style(ChatFormatting.AQUA)
		.text(ChatFormatting.GRAY, " / ")
		.add(Lang.number(maxOxy)
				.style(ChatFormatting.DARK_GRAY))
		.forGoggles(tooltip, 1);
		return true;
	}

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
		tank = new SmartFluidTankBehaviour(SmartFluidTankBehaviour.INPUT, this, 1, 10000, true);
		behaviours.add(tank);
	}
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && side == getBlockState().getValue(OxygenGeneratorBlock.HORIZONTAL_FACING).getOpposite())
			return tank.getCapability()
				.cast();
		tank.getCapability().cast();
		return super.getCapability(cap, side);
	}
}
