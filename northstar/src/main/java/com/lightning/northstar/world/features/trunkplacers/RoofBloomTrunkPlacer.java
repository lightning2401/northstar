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
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
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

public class RoofBloomTrunkPlacer extends TrunkPlacer {
	   public static final Codec<RoofBloomTrunkPlacer> CODEC = RecordCodecBuilder.create((p_226236_) -> {
		      return trunkPlacerParts(p_226236_).and(p_226236_.group(IntProvider.POSITIVE_CODEC.fieldOf("extra_branch_steps").forGetter((p_226242_) -> {
		         return p_226242_.extraBranchSteps;
		      }), RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("can_grow_through").forGetter((p_226234_) -> {
		         return p_226234_.canGrowThrough;
		      }), BlockStateProvider.CODEC.fieldOf("cap_provider").forGetter((p_161248_) -> {
		          return p_161248_.capProvider;
		      }))).apply(p_226236_, RoofBloomTrunkPlacer::new);
		   });
	   		public final BlockStateProvider capProvider;
		   private final IntProvider extraBranchSteps;
		   private final HolderSet<Block> canGrowThrough;
		   private BlockPos pos;
		   private Direction trunkDir;
		   
		   public RoofBloomTrunkPlacer(int int1, int int2, int int3, IntProvider int4, HolderSet<Block> int7, BlockStateProvider cap) {
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
			      trunkDir = Direction.getRandom(pRandom);
			      for(Direction dir = Direction.getRandom(pRandom); trunkDir == Direction.UP || trunkDir == Direction.DOWN;) {
				   	dir = Direction.getRandom(pRandom);
				  	trunkDir = dir;
			      }

			      for(int i = 0; i < pFreeTreeHeight; ++i) {
	
		//	    	 System.out.println(curve_x);
			    	 int Xpos = pPos.getX();
			    	 int Zpos = pPos.getZ();
			         int j = pPos.getY() - i;
			         
			         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(Xpos, j, Zpos), pConfig);
			         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(Xpos, j - 1, Zpos), pConfig);		
			         
			      }
			      BlockPos.MutableBlockPos thing = new BlockPos.MutableBlockPos();
			      for(int x = -2; x < 3; x++) {
			    	  for(int z = -2; z < 3; z++) {
				    	  if(Math.abs(x) + Math.abs(z) < 3) {
				    		  if(Math.abs(x) + Math.abs(z) < 2) {
					    		  this.placeLog(pLevel, pBlockSetter, pRandom, thing.set(pPos.getX() + x, pPos.getY(), pPos.getZ() + z), pConfig);
				    		  }
				    	  }
			    	  }
			      }
	    		  this.placeCap(pLevel, pBlockSetter, pRandom, pPos, pFreeTreeHeight, pConfig);
			      BlockPos.MutableBlockPos blockpos$mutableblockpos2 = new BlockPos.MutableBlockPos();
			      for(int i = 1; i < (pFreeTreeHeight/2); ++i) {
					 	 int Xpos = pPos.getX();
					   	 int Zpos = pPos.getZ();
					   	 
					     int j = pPos.getY() + (pFreeTreeHeight - i);
					        
					     this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos2.set(Xpos, j, Zpos), pConfig);
					     this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos2.set(Xpos, j - 1, Zpos), pConfig);
					     if (i == ((pFreeTreeHeight / 2) - 1)) {
							    pos = new BlockPos(Xpos, j, Zpos);
							      BiConsumer<BlockPos, BlockState> biconsumer = (a, b) -> {
							    	  a = pos;
							    	  b = Blocks.SHROOMLIGHT.defaultBlockState();
							       };
							    this.placeLight(pLevel, biconsumer, pRandom, pFreeTreeHeight, pPos, pConfig);
					     }
					     if (i == 1) {
						     this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos2.set(Xpos, j, Zpos), pConfig);
					     }
			      }


//			      for(int i2 = 0; i2 < pFreeTreeHeight; ++i2) {
//
//			    	 int Xpos = pPos.getX() + spinX2;
//			    	 int Zpos = pPos.getZ() + spinZ2;
//			         int j = pPos.getY() + i2;
//			         
//			         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos2.set(Xpos, j, Zpos), pConfig);
//			         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos2.set(Xpos, j - 1, Zpos), pConfig);
//
//			         if (i2 == pFreeTreeHeight - 1) {
//				         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos2.set(Xpos + 1, j, Zpos), pConfig);
//				         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos2.set(Xpos - 1, j, Zpos), pConfig);
//				         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos2.set(Xpos, j, Zpos + 1), pConfig);
//				         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos2.set(Xpos, j, Zpos - 1), pConfig);
//			            list.add(new FoliagePlacer.FoliageAttachment(blockpos$mutableblockpos2.set(Xpos, j + 1, Zpos), 0, false));
//			         }
//			      }
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
		   public void placeCap(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, RandomSource pRandom, BlockPos origin, int treeHeight, TreeConfiguration pConfig) {
			   // this is genuinely awful but I can't be bothered to find a better way to do this
			   BlockPos.MutableBlockPos thing = new BlockPos.MutableBlockPos();
			   
			   //first layer
			   this.placeCapBlock(pLevel, pBlockSetter, pRandom, thing.set(origin.getX(), origin.getY() - treeHeight, origin.getZ()), pConfig);
			   this.placeCapBlock(pLevel, pBlockSetter, pRandom, thing.set(origin.getX() + 1, origin.getY() - treeHeight, origin.getZ()), pConfig);
			   this.placeCapBlock(pLevel, pBlockSetter, pRandom, thing.set(origin.getX() + 1, origin.getY() - treeHeight, origin.getZ() + 1), pConfig);
			   this.placeCapBlock(pLevel, pBlockSetter, pRandom, thing.set(origin.getX() + 1, origin.getY() - treeHeight, origin.getZ() - 1), pConfig);
			   this.placeCapBlock(pLevel, pBlockSetter, pRandom, thing.set(origin.getX(), origin.getY() - treeHeight, origin.getZ() + 1), pConfig);
			   this.placeCapBlock(pLevel, pBlockSetter, pRandom, thing.set(origin.getX(), origin.getY() - treeHeight, origin.getZ() - 1), pConfig);
			   this.placeCapBlock(pLevel, pBlockSetter, pRandom, thing.set(origin.getX() - 1, origin.getY() - treeHeight, origin.getZ() + 1), pConfig);
			   this.placeCapBlock(pLevel, pBlockSetter, pRandom, thing.set(origin.getX() - 1, origin.getY() - treeHeight, origin.getZ()), pConfig);
			   this.placeCapBlock(pLevel, pBlockSetter, pRandom, thing.set(origin.getX() - 1, origin.getY() - treeHeight, origin.getZ() - 1), pConfig);
			   //second layer;
			   this.placeCapBlock(pLevel, pBlockSetter, pRandom, thing.set(origin.getX() + 1, origin.getY() - treeHeight - 1, origin.getZ() + 1), pConfig);
			   this.placeCapBlock(pLevel, pBlockSetter, pRandom, thing.set(origin.getX() + 1, origin.getY() - treeHeight - 1, origin.getZ() - 1), pConfig);
			   this.placeCapBlock(pLevel, pBlockSetter, pRandom, thing.set(origin.getX() - 1, origin.getY() - treeHeight - 1, origin.getZ() + 1), pConfig);
			   this.placeCapBlock(pLevel, pBlockSetter, pRandom, thing.set(origin.getX() - 1, origin.getY() - treeHeight - 1, origin.getZ() - 1), pConfig);
			   //outer "pedals"
			   this.placeCapBlock(pLevel, pBlockSetter, pRandom, thing.set(origin.getX() + 2, origin.getY() - treeHeight, origin.getZ()), pConfig);
			   this.placeCapBlock(pLevel, pBlockSetter, pRandom, thing.set(origin.getX() + 2, origin.getY() - treeHeight - 1, origin.getZ()), pConfig);
			   this.placeCapBlock(pLevel, pBlockSetter, pRandom, thing.set(origin.getX() - 2, origin.getY() - treeHeight, origin.getZ()), pConfig);
			   this.placeCapBlock(pLevel, pBlockSetter, pRandom, thing.set(origin.getX() - 2, origin.getY() - treeHeight - 1, origin.getZ()), pConfig);
			   this.placeCapBlock(pLevel, pBlockSetter, pRandom, thing.set(origin.getX(), origin.getY() - treeHeight, origin.getZ() + 2), pConfig);
			   this.placeCapBlock(pLevel, pBlockSetter, pRandom, thing.set(origin.getX(), origin.getY() - treeHeight - 1, origin.getZ() + 2), pConfig);
			   this.placeCapBlock(pLevel, pBlockSetter, pRandom, thing.set(origin.getX(), origin.getY() - treeHeight, origin.getZ() - 2), pConfig);
			   this.placeCapBlock(pLevel, pBlockSetter, pRandom, thing.set(origin.getX(), origin.getY() - treeHeight - 1, origin.getZ() - 2), pConfig);
			   //outer outer "pedals"
			   this.placeCapBlock(pLevel, pBlockSetter, pRandom, thing.set(origin.getX() + 3, origin.getY() - treeHeight - 1, origin.getZ()), pConfig);
			   this.placeCapBlock(pLevel, pBlockSetter, pRandom, thing.set(origin.getX() + 3, origin.getY() - treeHeight - 2, origin.getZ()), pConfig);
			   this.placeCapBlock(pLevel, pBlockSetter, pRandom, thing.set(origin.getX() - 3, origin.getY() - treeHeight - 1, origin.getZ()), pConfig);
			   this.placeCapBlock(pLevel, pBlockSetter, pRandom, thing.set(origin.getX() - 3, origin.getY() - treeHeight - 2, origin.getZ()), pConfig);
			   this.placeCapBlock(pLevel, pBlockSetter, pRandom, thing.set(origin.getX(), origin.getY() - treeHeight - 1, origin.getZ() + 3), pConfig);
			   this.placeCapBlock(pLevel, pBlockSetter, pRandom, thing.set(origin.getX(), origin.getY() - treeHeight - 2, origin.getZ() + 3), pConfig);
			   this.placeCapBlock(pLevel, pBlockSetter, pRandom, thing.set(origin.getX(), origin.getY() - treeHeight - 1, origin.getZ() - 3), pConfig);
			   this.placeCapBlock(pLevel, pBlockSetter, pRandom, thing.set(origin.getX(), origin.getY() - treeHeight - 2, origin.getZ() - 3), pConfig);
			   //placing the shroomlight
			   this.placeShroomLight(pLevel, pBlockSetter, pRandom, thing.set(origin.getX(), origin.getY() - treeHeight - 1, origin.getZ()), pConfig);
			   this.placeShroomLight(pLevel, pBlockSetter, pRandom, thing.set(origin.getX(), origin.getY() - treeHeight - 2, origin.getZ()), pConfig);
			   this.placeShroomLight(pLevel, pBlockSetter, pRandom, thing.set(origin.getX(), origin.getY() - treeHeight - 3, origin.getZ()), pConfig);
		   }
		   
		   public List<FoliagePlacer.FoliageAttachment> placeLight(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, RandomSource pRandom, int pFreeTreeHeight, BlockPos pPos, TreeConfiguration pConfig) {
			      List<FoliagePlacer.FoliageAttachment> list = Lists.newArrayList();   
			      BlockPos.MutableBlockPos blockpos$mutableblockpos3 = new BlockPos.MutableBlockPos();
				  this.placeShroomLight(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos3.set(pPos.getX(), pPos.getY()+12, pPos.getZ()), pConfig);
//				  System.out.println(this.placeShroomLight(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos3.set(pPos.getX(), pPos.getY()+12, pPos.getZ()), pConfig));
//				  System.out.println("YOOOOOOOOOOOOOOOO, BIOLUMINESCENCE");
//				  System.out.println(pPos.getX() + "X    " + (pPos.getY()+12) + "Y    " + pPos.getZ() + "Z" );

			      return list;
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
				   return state.is(Blocks.AIR) || state.is(NorthstarBlocks.BLOOM_FUNGUS.get()) || state.is(NorthstarBlocks.VENUS_STONE.get()) || state.is(NorthstarBlocks.VENUS_DEEP_STONE.get());
			   });
		   }
		}