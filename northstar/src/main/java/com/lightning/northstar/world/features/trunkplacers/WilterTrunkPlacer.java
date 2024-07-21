package com.lightning.northstar.world.features.trunkplacers;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

//

public class WilterTrunkPlacer extends TrunkPlacer {
	   public static final Codec<WilterTrunkPlacer> CODEC = RecordCodecBuilder.create((p_226236_) -> {
		      return trunkPlacerParts(p_226236_).and(p_226236_.group(IntProvider.POSITIVE_CODEC.fieldOf("extra_branch_steps").forGetter((p_226242_) -> {
		         return p_226242_.extraBranchSteps;
		      }), Codec.floatRange(0.0F, 1.0F).fieldOf("place_branch_per_log_probability").forGetter((p_226240_) -> {
		         return p_226240_.placeBranchPerLogProbability;
		      }), IntProvider.NON_NEGATIVE_CODEC.fieldOf("extra_branch_length").forGetter((p_226238_) -> {
		         return p_226238_.extraBranchLength;
		      }), RegistryCodecs.homogeneousList(Registry.BLOCK_REGISTRY).fieldOf("can_grow_through").forGetter((p_226234_) -> {
		         return p_226234_.canGrowThrough;
		      }), IntProvider.NON_NEGATIVE_CODEC.fieldOf("extra_branch_length").forGetter((p_226238_) -> {
			         return p_226238_.spinFactor;
		      }))).apply(p_226236_, WilterTrunkPlacer::new);
		   });
		   private final IntProvider extraBranchSteps;
		   private final float placeBranchPerLogProbability;
		   private final IntProvider extraBranchLength;
		   private final HolderSet<Block> canGrowThrough;
		   private final IntProvider spinFactor;
		   private int trunkXOffset;
		   private int trunkZOffset;
		   private int Xadd;
		   private int Zadd;
		   private BlockPos pos;
		   private Direction trunkDir;
		   
		   public WilterTrunkPlacer(int int1, int int2, int int3, IntProvider int4, float int5, IntProvider int6, HolderSet<Block> int7, IntProvider spinfacto) {
		      super(int1, int2, int3);
		      this.extraBranchSteps = int4;
		      this.placeBranchPerLogProbability = int5;
		      this.extraBranchLength = int6;
		      this.canGrowThrough = int7;
		      this.spinFactor = spinfacto;
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
			      System.out.println(pFreeTreeHeight);

			      if (trunkDir == Direction.EAST) {
			    	  Xadd = 1;
			    	  Zadd = 0;
			      }else if (trunkDir == Direction.WEST) {
			    	  Xadd = -1;
			    	  Zadd = 0;
			      }else if (trunkDir == Direction.NORTH) {
			    	  Xadd = 0;
			    	  Zadd = 1;
			      }else if (trunkDir == Direction.SOUTH) {
			    	  Xadd = 0;
			    	  Zadd = -1;
			      }
			      int curve_x = 0;
			      int curve_z = 0;
			      int x_offset = 0;
			      int z_offset = 0;
			      for(int i = 0; i < pFreeTreeHeight; ++i) {
				     int tree_y_dist = pFreeTreeHeight /2;
				     if (i<(pFreeTreeHeight / 3)) {
				    	 x_offset -= Xadd;
				    	 z_offset -= Zadd;
				     }
			    	 if (i > (pFreeTreeHeight / 2)) {
			    		  curve_x = (i - tree_y_dist) / 2;
			    		  curve_z = (i - tree_y_dist) / 2;
			    	 }
			    	 if (curve_x < -1)
			    	 {curve_x = 0;}
			    	 curve_x *= Xadd;
			    	 if (curve_z < -1)
			    	 {curve_z = 0;}
			    	 curve_z *= Zadd;
		//	    	 System.out.println(curve_x);
			    	 int Xpos = pPos.getX() + curve_x + x_offset;
			    	 int Zpos = pPos.getZ() + curve_z + z_offset;
			         int j = pPos.getY() + i;
			         
			         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(Xpos, j, Zpos), pConfig);
			         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(Xpos, j - 1, Zpos), pConfig);

			         if (i < (pFreeTreeHeight / 3)) {
				         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(Xpos + 1, j, Zpos), pConfig);
				         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(Xpos - 1, j, Zpos), pConfig);
				         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(Xpos, j, Zpos + 1), pConfig);
				         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(Xpos, j, Zpos - 1), pConfig);
				         
				         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(Xpos + 1, j - 1, Zpos), pConfig);
				         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(Xpos - 1, j - 1, Zpos), pConfig);
				         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(Xpos, j - 1, Zpos + 1), pConfig);
				         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(Xpos, j - 1, Zpos - 1), pConfig);
				         
				         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(Xpos - Xadd, j + 1, Zpos - Zadd), pConfig);
				         
			         }
				      if (i == (pFreeTreeHeight - 1)) {
				    	  trunkXOffset = curve_x + x_offset;
				    	  trunkZOffset = curve_z + z_offset;
				      }
			      }
			      BlockPos.MutableBlockPos blockpos$mutableblockpos2 = new BlockPos.MutableBlockPos();
			      for(int i = 1; i < (pFreeTreeHeight/2); ++i) {
				    	 if(i % 2 == 0 && i > (pFreeTreeHeight / 3)) {
				    		 trunkXOffset += Xadd;
				    		 trunkZOffset += Zadd;
				    	 }else if(i < (pFreeTreeHeight / 3)) {
				    		 trunkXOffset += Xadd;
				    		 trunkZOffset += Zadd;
				    	 }
	    	 
					 	 int Xpos = pPos.getX() + trunkXOffset;
					   	 int Zpos = pPos.getZ() + trunkZOffset;
					   	 
					     int j = pPos.getY() + (pFreeTreeHeight - i);
					        
					     this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos2.set(Xpos, j, Zpos), pConfig);
					     this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos2.set(Xpos, j - 1, Zpos), pConfig);
					     if (i == ((pFreeTreeHeight / 2) - 1)) {
					            list.add(new FoliagePlacer.FoliageAttachment(blockpos$mutableblockpos.set(Xpos, j, Zpos), 0, false));
							    pos = new BlockPos(Xpos, j, Zpos);
							      BiConsumer<BlockPos, BlockState> biconsumer = (a, b) -> {
							    	  a = pos;
							    	  b = Blocks.SHROOMLIGHT.defaultBlockState();
							       };
							    this.placeLight(pLevel, biconsumer, pRandom, pFreeTreeHeight, pPos, pConfig);
					     }
					     if (i == 1) {
						     this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos2.set(Xpos - Xadd, j, Zpos - Zadd), pConfig);
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
		   
		   public List<FoliagePlacer.FoliageAttachment> placeLight(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, RandomSource pRandom, int pFreeTreeHeight, BlockPos pPos, TreeConfiguration pConfig) {
			      List<FoliagePlacer.FoliageAttachment> list = Lists.newArrayList();   
			      BlockPos.MutableBlockPos blockpos$mutableblockpos3 = new BlockPos.MutableBlockPos();
				  this.placeShroomLight(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos3.set(pPos.getX(), pPos.getY()+12, pPos.getZ()), pConfig);

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
			         pBlockSetter.accept(pPos, pPropertySetter.apply(pConfig.trunkProvider.getState(pRandom, pPos)));
			         return true;
			      } else {
			         return false;
			      }
			   }
		   @Override
		   protected boolean validTreePos(LevelSimulatedReader pLevel, BlockPos pPos) {
		      return super.validTreePos(pLevel, pPos) || pLevel.isStateAtPosition(pPos, (p_226232_) -> {
		         return p_226232_.is(this.canGrowThrough);
		      });
		   }
		}