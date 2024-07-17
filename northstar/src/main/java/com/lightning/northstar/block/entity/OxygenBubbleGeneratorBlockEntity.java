package com.lightning.northstar.block.entity;

import java.util.List;

import com.google.common.collect.Lists;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class OxygenBubbleGeneratorBlockEntity extends BlockEntity implements IHaveGoggleInformation{
	public int tickCount;
	private final List<BlockPos> effectBlocks = Lists.newArrayList();

	public OxygenBubbleGeneratorBlockEntity(BlockPos pos, BlockState state) {
		super(NorthstarBlockEntityTypes.OXYGEN_BUBBLE_GENERATOR.get(), pos, state);
	}
	
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}
	
	public static void tick(Level pLevel, BlockPos pPos, BlockState state, OxygenBubbleGeneratorBlockEntity pBlockEntity) {
	  ++pBlockEntity.tickCount;
//	  System.out.println("big fart");
	  long i = pLevel.getGameTime();
	  List<BlockPos> list = pBlockEntity.effectBlocks;
	  applyEffects(pLevel, pPos, list);
      if (i % 40L == 0L) {
      pLevel.playSound((Player)null, pPos, SoundEvents.CONDUIT_AMBIENT, SoundSource.BLOCKS, 1.0F, 1.0F);}
      }
	
	private static void applyEffects(Level pLevel, BlockPos pPos, List<BlockPos> pPositions) {
	      int i = pPositions.size();
	      int j = i / 7 * 16;
	      int k = pPos.getX();
	      int l = pPos.getY();
	      int i1 = pPos.getZ();
	      AABB aabb = (new AABB((double)(k - 50), (double)(l - 50), (double)(i1 - 50), (double)(k + 50), (double)(l + 50), (double)(i1 + 50))).inflate((double)j).expandTowards(0.0D, (double)pLevel.getHeight(), 0.0D);
	      List<Player> list = pLevel.getEntitiesOfClass(Player.class, aabb);
	//      System.out.println(list);
	      if (!list.isEmpty()) {
	//	      System.out.println("biggus fartus");
	         for(Player player : list) {
	            
	               player.addEffect(new MobEffectInstance(MobEffects.CONDUIT_POWER, 260, 0, true, false));
	            
	         }

	      }
	}
}
