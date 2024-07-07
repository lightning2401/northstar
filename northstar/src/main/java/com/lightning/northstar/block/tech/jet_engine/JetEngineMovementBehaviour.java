package com.lightning.northstar.block.tech.jet_engine;

import com.lightning.northstar.contraptions.RocketContraption;
import com.lightning.northstar.contraptions.RocketContraptionEntity;
import com.lightning.northstar.particle.ColdAirParticleData;
import com.lightning.northstar.particle.RocketFlameLandingParticleData;
import com.lightning.northstar.particle.RocketFlameParticleData;
import com.lightning.northstar.particle.RocketSmokeLandingParticleData;
import com.lightning.northstar.particle.RocketSmokeParticleData;
import com.simibubi.create.content.contraptions.behaviour.MovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.foundation.utility.VecHelper;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class JetEngineMovementBehaviour implements MovementBehaviour {

	@Override
	public boolean renderAsNormalBlockEntity() {
		return false;
	}
	
	@Override
	public ItemStack canBeDisabledVia(MovementContext context) {
		return null;
	}

	@Override
	public void tick(MovementContext context) {
		if (!context.world.isClientSide())
			return;
		if(!context.state.getValue(JetEngineBlock.BOTTOM)) 
			return;
		if((context.contraption instanceof RocketContraption) && ((RocketContraptionEntity)context.contraption.entity).lift_vel > 0)
		{RandomSource r = context.world.getRandom();
		Vec3 c = context.position;
		Vec3 v = c.add(VecHelper.offsetRandomly(Vec3.ZERO, r, .125f).multiply(1, 0, 1));
		if(((RocketContraptionEntity)context.contraption.entity).blasting) 
			{context.world.addAlwaysVisibleParticle(new RocketFlameParticleData(), v.x, v.y, v.z, 0, 0, 0);
			context.world.addAlwaysVisibleParticle(new RocketSmokeParticleData(), v.x, v.y, v.z, 0, 0, 0);}
		else {context.world.addParticle(new ColdAirParticleData(), v.x, v.y, v.z, 0, 0, 0);}
		return;}
		if((context.contraption instanceof RocketContraption) && ((RocketContraptionEntity)context.contraption.entity).lift_vel < 0 && context.contraption.entity.getY() < context.contraption.entity.level.getMaxBuildHeight() + 200) 
		{RandomSource r = context.world.getRandom();
		Vec3 c = context.position;
		Vec3 v = c.add(VecHelper.offsetRandomly(Vec3.ZERO, r, .125f).multiply(1, 0, 1));
		if(((RocketContraptionEntity)context.contraption.entity).slowing) 
		{context.world.addAlwaysVisibleParticle(new RocketFlameLandingParticleData(), v.x, v.y - 2, v.z, 0, 0, 0);
		context.world.addAlwaysVisibleParticle(new RocketSmokeLandingParticleData(), v.x, v.y - 2, v.z, 0, 0, 0);} return;}
		if(!(context.contraption instanceof RocketContraption))
		{RandomSource r = context.world.getRandom();
		Vec3 c = context.position;
		Vec3 v = c.add(VecHelper.offsetRandomly(Vec3.ZERO, r, .125f).multiply(1, 0, 1));
		context.world.addParticle(new ColdAirParticleData(), v.x, v.y, v.z, 0, 0, 0);}
	}

}
