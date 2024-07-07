package com.lightning.northstar.contraptions;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class RocketEntity extends Entity {

	public RocketEntity(EntityType<?> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}
	
	
	
	   public void tick() {
		   int i = 0;
		   i++;
		   if(i % 10 == 0) {
			   System.out.println("I AM A ROCKET AND I EXIST!!!!!!!!");
		   }
	   }
	
	
	@Override
	protected void defineSynchedData() {}

	@Override
	protected void readAdditionalSaveData(CompoundTag pCompound) {}

	@Override
	protected void addAdditionalSaveData(CompoundTag pCompound) {}

	@Override
	public Packet<?> getAddEntityPacket() {
		return new ClientboundAddEntityPacket(this);
	}
	
	public static EntityType.Builder<?> build(EntityType.Builder<?> builder) {
		@SuppressWarnings("unchecked")
		EntityType.Builder<RocketEntity> entityBuilder = (EntityType.Builder<RocketEntity>) builder;
		return entityBuilder;
	}

}
