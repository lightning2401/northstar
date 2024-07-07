package com.lightning.northstar.world.features.trunkplacers;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import com.google.common.collect.Lists;
import com.lightning.northstar.block.NorthstarBlocks;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

public class RoofTowerTrunkPlacer extends TrunkPlacer {
	   public static final Codec<RoofTowerTrunkPlacer> CODEC = RecordCodecBuilder.create((p_226236_) -> {
		      return trunkPlacerParts(p_226236_).and(p_226236_.group(IntProvider.POSITIVE_CODEC.fieldOf("extra_branch_steps").forGetter((p_226242_) -> {
		         return p_226242_.extraBranchSteps;
		      }), RegistryCodecs.homogeneousList(Registry.BLOCK_REGISTRY).fieldOf("can_grow_through").forGetter((p_226234_) -> {
		         return p_226234_.canGrowThrough;
		      }), BlockStateProvider.CODEC.fieldOf("cap_provider").forGetter((p_161248_) -> {
		          return p_161248_.capProvider;
		      }))).apply(p_226236_, RoofTowerTrunkPlacer::new);
		   });
	   		public final BlockStateProvider capProvider;
		   private final IntProvider extraBranchSteps;
		   private final HolderSet<Block> canGrowThrough;
		   
		   public RoofTowerTrunkPlacer(int int1, int int2, int int3, IntProvider int4, HolderSet<Block> int7, BlockStateProvider cap) {
		      super(int1, int2, int3);
		      this.extraBranchSteps = int4;
		      this.canGrowThrough = int7;
		      this.capProvider = cap;
		   }
		   protected TrunkPlacerType<?> type() {
		      return TrunkPlacerType.DARK_OAK_TRUNK_PLACER;
		   }
		   public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, RandomSource pRandom, int pFreeTreeHeight, BlockPos pPos, TreeConfiguration pConfig) {
			      List<FoliagePlacer.FoliageAttachment> list = Lists.newArrayList();
			      BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

			      for(int i = 0; i < pFreeTreeHeight; ++i) {
			    	 int Xpos = pPos.getX();
			    	 int Zpos = pPos.getZ();
			         int j = pPos.getY() - i;
			         
			         if(i > 1 && (i - 1) % 2 == 0 && i < 4) {
			        	 int randX = pRandom.nextBoolean() ? 1 : -1;
			        	 int randZ = pRandom.nextBoolean() ? 1 : -1;
			        	 this.placeTinyCap(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.offset(randX,0,randZ), pConfig);
			         }else if((i - 1) % 2 == 0 && i > 4) {
			        	 int randX = pRandom.nextBoolean() ? 1 : -1;
			        	 int randZ = pRandom.nextBoolean() ? 1 : -1;
			        	 this.placeSmallCap(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.offset(randX,0,randZ), pConfig);
			        	 
			         }
			         
			         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(Xpos, j, Zpos), pConfig);
			      }
	    		  this.placeBigCap(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos, pConfig);
			      return list;
			   }
		   public void placeColumn(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, RandomSource pRandom, BlockPos origin, int Xoff, int Zoff, int height, TreeConfiguration pConfig) {
			   BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
			   blockpos$mutableblockpos.setX(origin.getX());
			   blockpos$mutableblockpos.setZ(origin.getZ());
			   for(int i = 0; i < height; i++) {
				   this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(origin.getX() - Xoff, origin.getY() + i, origin.getZ() - Zoff), pConfig);
			   }
		   }
		   public void placeTinyCap(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, RandomSource pRandom, BlockPos origin, TreeConfiguration pConfig) {
			   BlockPos.MutableBlockPos thing = new BlockPos.MutableBlockPos();
			   this.placeCapBlock(pLevel, pBlockSetter, pRandom, thing.set(origin.getX(), origin.getY(), origin.getZ()), pConfig);
			   this.placeCapBlock(pLevel, pBlockSetter, pRandom, thing.set(origin.getX(), origin.getY(), origin.getZ() - 1), pConfig);
			   this.placeCapBlock(pLevel, pBlockSetter, pRandom, thing.set(origin.getX(), origin.getY(), origin.getZ() + 1), pConfig);
			   this.placeCapBlock(pLevel, pBlockSetter, pRandom, thing.set(origin.getX() - 1, origin.getY(), origin.getZ()), pConfig);
			   this.placeCapBlock(pLevel, pBlockSetter, pRandom, thing.set(origin.getX() + 1, origin.getY(), origin.getZ()), pConfig);

		   }
		   public void placeSmallCap(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, RandomSource pRandom, BlockPos origin, TreeConfiguration pConfig) {
			   BlockPos.MutableBlockPos thing = new BlockPos.MutableBlockPos();
			   this.placeCapBlock(pLevel, pBlockSetter, pRandom, thing.set(origin.getX(), origin.getY(), origin.getZ()), pConfig);
			   this.placeCapBlock(pLevel, pBlockSetter, pRandom, thing.set(origin.getX() + 1, origin.getY(), origin.getZ()), pConfig);
			   this.placeCapBlock(pLevel, pBlockSetter, pRandom, thing.set(origin.getX(), origin.getY(), origin.getZ() + 1), pConfig);
			   this.placeCapBlock(pLevel, pBlockSetter, pRandom, thing.set(origin.getX() + 1, origin.getY(), origin.getZ() + 1), pConfig);
			   
			   this.placeCapBlock(pLevel, pBlockSetter, pRandom, thing.set(origin.getX() - 1, origin.getY(), origin.getZ()), pConfig);
			   this.placeCapBlock(pLevel, pBlockSetter, pRandom, thing.set(origin.getX() - 1, origin.getY(), origin.getZ() + 1), pConfig);

			   this.placeCapBlock(pLevel, pBlockSetter, pRandom, thing.set(origin.getX() + 2, origin.getY(), origin.getZ()), pConfig);
			   this.placeCapBlock(pLevel, pBlockSetter, pRandom, thing.set(origin.getX() + 2, origin.getY(), origin.getZ() + 1), pConfig);
			   
			   this.placeCapBlock(pLevel, pBlockSetter, pRandom, thing.set(origin.getX(), origin.getY(), origin.getZ() + 2), pConfig);
			   this.placeCapBlock(pLevel, pBlockSetter, pRandom, thing.set(origin.getX() + 1, origin.getY(), origin.getZ() + 2), pConfig);

			   this.placeCapBlock(pLevel, pBlockSetter, pRandom, thing.set(origin.getX(), origin.getY(), origin.getZ() - 1), pConfig);
			   this.placeCapBlock(pLevel, pBlockSetter, pRandom, thing.set(origin.getX() + 1, origin.getY(), origin.getZ() - 1), pConfig);

		   }
		   
		   public void placeBigCap(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, RandomSource pRandom, BlockPos origin, TreeConfiguration pConfig) {
			   BlockPos.MutableBlockPos thing = new BlockPos.MutableBlockPos();
			   //for the head of the thing
			   for(int x = -2; x < 3; x++) {
				   for(int z = -2; z < 3; z++) {
					   if(Mth.abs(x) + Mth.abs(z) != 4) {
						   this.placeCapBlock(pLevel, pBlockSetter, pRandom, thing.set(origin.getX() + x, origin.getY() - 1, origin.getZ() + z), pConfig);
					   }
				   }
			   }

		   }

		   @Override
		   protected boolean placeLog(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, RandomSource pRandom, BlockPos pPos, TreeConfiguration pConfig) {
			      return this.placeLog(pLevel, pBlockSetter, pRandom, pPos, pConfig, Function.identity());
		   }
		   @Override
		   protected boolean placeLog(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, RandomSource pRandom, BlockPos pPos, TreeConfiguration pConfig, Function<BlockState, BlockState> pPropertySetter) {
			      if (this.validTreePos(pLevel, pPos)) {
			         pBlockSetter.accept(pPos, pPropertySetter.apply(pConfig.trunkProvider.getState(pRandom, pPos)));
			         return true;
			      } else {
			         return false;
			      }
			   }
		   
		   protected boolean placeShroomLight(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, RandomSource pRandom, BlockPos pPos, TreeConfiguration pConfig) {
			      return this.placeShroomLight2(pLevel, pBlockSetter, pRandom, pPos, pConfig, Function.identity());
		   }
		   protected boolean placeShroomLight2(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, RandomSource pRandom, BlockPos pPos, TreeConfiguration pConfig, Function<BlockState, BlockState> pPropertySetter) {
			      if (this.validTreePos(pLevel, pPos)) {
			         pBlockSetter.accept(pPos, pPropertySetter.apply(Blocks.SHROOMLIGHT.defaultBlockState()));
			         return true;
			      } else {
			         return false;
			      }
			   }
		   protected boolean placeCapBlock(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, RandomSource pRandom, BlockPos pPos, TreeConfiguration pConfig) {
			      return this.placeCapBlock2(pLevel, pBlockSetter, pRandom, pPos, pConfig, Function.identity());
		   }
		   protected boolean placeCapBlock2(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, RandomSource pRandom, BlockPos pPos, TreeConfiguration pConfig, Function<BlockState, BlockState> pPropertySetter) {
			      if (this.validTreePos(pLevel, pPos)) {
			         pBlockSetter.accept(pPos, pPropertySetter.apply(this.capProvider.getState(pRandom, pPos)));
			         return true;
			      } else {
			         return false;
			      }
			   }
		   @Override
		   protected boolean validTreePos(LevelSimulatedReader pLevel, BlockPos pPos) {
			   return pLevel.isStateAtPosition(pPos, (state) -> {
				   return state.is(Blocks.AIR) || state.is(NorthstarBlocks.TOWER_FUNGUS.get()) || state.is(NorthstarBlocks.VENUS_STONE.get()) || state.is(NorthstarBlocks.VENUS_DEEP_STONE.get());
			   });
		   }
		}