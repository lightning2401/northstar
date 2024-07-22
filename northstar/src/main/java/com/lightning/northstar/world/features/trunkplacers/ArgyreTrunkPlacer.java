package com.lightning.northstar.world.features.trunkplacers;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import com.google.common.collect.Lists;
import com.lightning.northstar.NorthstarTags.NorthstarBlockTags;
import com.lightning.northstar.world.features.configuration.AlienTreeConfig;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer.FoliageAttachment;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

//

public class ArgyreTrunkPlacer extends TrunkPlacer {
	   public static final Codec<ArgyreTrunkPlacer> CODEC = RecordCodecBuilder.create((p_226236_) -> {
		      return trunkPlacerParts(p_226236_).and(p_226236_.group(IntProvider.POSITIVE_CODEC.fieldOf("extra_branch_steps").forGetter((p_226242_) -> {
		         return p_226242_.extraBranchSteps;
		      }), Codec.floatRange(0.0F, 1.0F).fieldOf("place_branch_per_log_probability").forGetter((p_226240_) -> {
		         return p_226240_.placeBranchPerLogProbability;
		      }), IntProvider.NON_NEGATIVE_CODEC.fieldOf("extra_branch_length").forGetter((p_226238_) -> {
		         return p_226238_.extraBranchLength;
		      }), RegistryCodecs.homogeneousList(Registry.BLOCK_REGISTRY).fieldOf("can_grow_through").forGetter((p_226234_) -> {
		         return p_226234_.canGrowThrough;
		      }), IntProvider.NON_NEGATIVE_CODEC.fieldOf("size").forGetter((p_226238_) -> {
			         return p_226238_.spinFactor;
		      }))).apply(p_226236_, ArgyreTrunkPlacer::new);
		   });
		   private final IntProvider extraBranchSteps;
		   private final float placeBranchPerLogProbability;
		   private final IntProvider extraBranchLength;
		   private final HolderSet<Block> canGrowThrough;
		   private final IntProvider spinFactor;

		   public ArgyreTrunkPlacer(int int1, int int2, int int3, IntProvider int4, float int5, IntProvider int6, HolderSet<Block> int7, IntProvider spinfacto) {
		      super(int1, int2, int3);
		      this.extraBranchSteps = int4;
		      this.placeBranchPerLogProbability = int5;
		      this.extraBranchLength = int6;
		      this.canGrowThrough = int7;
		      this.spinFactor = UniformInt.of(1, 1);
		   }
		   protected TrunkPlacerType<?> type() {
		      return TrunkPlacerType.DARK_OAK_TRUNK_PLACER;
		   }
		   public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, RandomSource pRandom, int pFreeTreeHeight, BlockPos pPos, AlienTreeConfig treeconfiguration, BiConsumer<BlockPos, BlockState> glow_block) {
			      List<FoliagePlacer.FoliageAttachment> list = Lists.newArrayList();
			      BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
			      int spinX = 0;
			      int spinZ = 0;
			      int spinZAdd = -1;
			      int spinXAdd = 1;

			      for(int i = 0; i < pFreeTreeHeight; ++i) {
			    	 if (Math.random() > 0.97) {this.placeBranch(pLevel, pBlockSetter, pRandom, pFreeTreeHeight, pPos, treeconfiguration, spinX, spinZ, glow_block);}
			    	 if (Math.random() > 0.9){spinX += spinXAdd;}
			    	 if (Math.random() > 0.7){spinZ += spinZAdd;}
			    	 if (Math.random() < 0.3){spinX -= spinXAdd;}
			    	 if (Math.random() < 0.3){spinZ -= spinZAdd;}
			    	 int Xpos = pPos.getX() + spinX;
			    	 int Zpos = pPos.getZ() + spinZ;
			         int j = pPos.getY() + i;
			    	 double rando1 = Math.random();
			    	 double rando2 = Math.random();
			         int glowoffsetX = 1;
			         int glowoffsetZ = 1;
			         if (rando1 > 0.5) {glowoffsetX = 1;}else {glowoffsetX =-1;}
			         if (rando2 > 0.5) {glowoffsetZ = 1;}else {glowoffsetZ =-1;}
			         
			         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(Xpos, j, Zpos), treeconfiguration);
			         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(Xpos, j - 1, Zpos), treeconfiguration);
//			         if (this.spinFactor == UniformInt.of(-1,-1)) {
			         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(Xpos + 1, j, Zpos), treeconfiguration);
			         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(Xpos + 1, j - 1, Zpos), treeconfiguration);
			         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(Xpos, j, Zpos + 1), treeconfiguration);
			         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(Xpos, j - 1, Zpos + 1), treeconfiguration);
//			         }
//			         if (this.spinFactor == UniformInt.of(1,1)) {
			         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(Xpos + 1, j, Zpos), treeconfiguration);
			         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(Xpos + 1, j - 1, Zpos), treeconfiguration);
			         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(Xpos - 1, j, Zpos), treeconfiguration);
			         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(Xpos - 1, j - 1, Zpos), treeconfiguration);
			         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(Xpos, j, Zpos + 1), treeconfiguration);
			         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(Xpos, j - 1, Zpos + 1), treeconfiguration);
			         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(Xpos, j, Zpos - 1), treeconfiguration);
			         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(Xpos, j - 1, Zpos - 1), treeconfiguration);
			    	 if (Math.random() > 0.92) {this.placeLog(pLevel, glow_block, pRandom, blockpos$mutableblockpos.set(Xpos + glowoffsetX, j, Zpos + glowoffsetZ), treeconfiguration);}
			    	 }

	//		      }
			      return list;
			   }
		   public void placeBranch(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, RandomSource pRandom, int pFreeTreeHeight, BlockPos pPos, AlienTreeConfig treeconfiguration, int oldspinX, int oldspinZ, BiConsumer<BlockPos, BlockState> glow_block) {
			      BlockPos.MutableBlockPos blockpos$mutableblockpos2 = new BlockPos.MutableBlockPos();
			      int spinX = 0;
			      int spinZ = 0;
			      int spinZAdd = -1;
			      int spinXAdd = 1;

			      for(int i = 0; i < pFreeTreeHeight;) {	
			    	 if (Math.random() > 0.3){i++;}
			    	 if (Math.random() > 0.6){spinX += spinXAdd;}
			    	 if (Math.random() > 0.6){spinZ += spinZAdd;}
			    	 if (Math.random() < 0.4){spinX -= spinXAdd;}
			    	 if (Math.random() < 0.4){spinZ -= spinZAdd;}
			    	 int Xpos = pPos.getX() + spinX;
			    	 int Zpos = pPos.getZ() + spinZ;
			         int j = pPos.getY() + i;
			    	 double rando1 = Math.random();
			    	 double rando2 = Math.random();
			         int glowoffsetX = 1;
			         int glowoffsetZ = 1;
			         if (rando1 > 0.5) {glowoffsetX = 1;}else {glowoffsetX =-1;}
			         if (rando2 > 0.5) {glowoffsetZ = 1;}else {glowoffsetZ =-1;}
			         
			         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos2.set(Xpos, j, Zpos), treeconfiguration);
			         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos2.set(Xpos, j - 1, Zpos), treeconfiguration);
//			         if (this.spinFactor == UniformInt.of(1,1)) {
			         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos2.set(Xpos + 1, j, Zpos), treeconfiguration);
			         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos2.set(Xpos + 1, j - 1, Zpos), treeconfiguration);
			         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos2.set(Xpos, j, Zpos + 1), treeconfiguration);
			         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos2.set(Xpos, j - 1, Zpos + 1), treeconfiguration);
			         if (Math.random() > 0.97) {this.placeLog(pLevel, glow_block, pRandom, blockpos$mutableblockpos2.set(Xpos + glowoffsetX, j, Zpos + glowoffsetZ), treeconfiguration);}
//			         }
			      }
		   }
		   protected boolean placeLog(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, RandomSource pRandom, BlockPos pPos, AlienTreeConfig treeconfiguration) {
			      return this.placeLog(pLevel, pBlockSetter, pRandom, pPos, treeconfiguration, Function.identity());
		   }
		   protected boolean placeLog(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, RandomSource pRandom, BlockPos pPos, AlienTreeConfig treeconfiguration, Function<Object, Object> function) {
			      if (this.validTreePos(pLevel, pPos)) {
			         pBlockSetter.accept(pPos, (BlockState) function.apply(treeconfiguration.trunkProvider.getState(pRandom, pPos)));
			         return true;
			      } else {
			         return false;
			      }
			   }
		   @Override
		   protected boolean validTreePos(LevelSimulatedReader pLevel, BlockPos pPos) {
			      return pLevel.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pPos).getY() > pPos.getY() && pLevel.isStateAtPosition(pPos, block -> {
			    	 return block.is(NorthstarBlockTags.ARGYRE_REPLACES.tag);
			      });
		   }
		@Override
		public List<FoliageAttachment> placeTrunk(LevelSimulatedReader pLevel,
				BiConsumer<BlockPos, BlockState> pBlockSetter, RandomSource pRandom, int pFreeTreeHeight, BlockPos pPos,
				TreeConfiguration pConfig) {
			return null;
		}
		}