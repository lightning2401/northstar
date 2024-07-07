package com.lightning.northstar.world.features.trunkplacers;

import java.util.List;
import java.util.function.BiConsumer;

import com.google.common.collect.Lists;
import com.lightning.northstar.world.features.configuration.AlienTreeConfig;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;

//

public class ArgyreCeilingTrunkPlacer extends ArgyreTrunkPlacer {

	   private final IntProvider extraBranchSteps;
	   private final float placeBranchPerLogProbability;
	   private final IntProvider extraBranchLength;
	   private final HolderSet<Block> canGrowThrough;
	   private final IntProvider spinFactor;
	   public ArgyreCeilingTrunkPlacer(int int1, int int2, int int3, IntProvider int4, float int5, IntProvider int6, HolderSet<Block> int7, IntProvider spinfacto) {
		      super(int1, int2, int3, spinfacto, int5, spinfacto, int7, spinfacto);
		      this.extraBranchSteps = int4;
		      this.placeBranchPerLogProbability = int5;
		      this.extraBranchLength = int6;
		      this.canGrowThrough = int7;
		      this.spinFactor = UniformInt.of(1, 1);
		   }
	   public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, RandomSource pRandom, int pFreeTreeHeight, BlockPos pPos, AlienTreeConfig treeconfiguration) {
		      List<FoliagePlacer.FoliageAttachment> list = Lists.newArrayList();
		      BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
		      int spinX = 0;
		      int spinZ = 0;
		      int spinZAdd = -1;
		      int spinXAdd = 1;

		      for(int i = 0; i < pFreeTreeHeight; ++i) {
		    	 if (Math.random() > 0.93) {this.placeBranch(pLevel, pBlockSetter, pRandom, pFreeTreeHeight, pPos, treeconfiguration, spinX, spinZ);}
		    	 
		    	 if (Math.random() > 0.9){spinX += spinXAdd;}
		    	 if (Math.random() > 0.7){spinZ += spinZAdd;}
		    	 if (Math.random() < 0.3){spinX -= spinXAdd;}
		    	 if (Math.random() < 0.3){spinZ -= spinZAdd;}
		    	 int Xpos = pPos.getX() + spinX;
		    	 int Zpos = pPos.getZ() + spinZ;
		         int j = pPos.getY() - i;
		         
		         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(Xpos, j, Zpos), treeconfiguration);
		         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(Xpos, j - 1, Zpos), treeconfiguration);
//		         if (this.spinFactor == UniformInt.of(-1,-1)) {
		         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(Xpos + 1, j, Zpos), treeconfiguration);
		         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(Xpos + 1, j - 1, Zpos), treeconfiguration);
		         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(Xpos, j, Zpos + 1), treeconfiguration);
		         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(Xpos, j - 1, Zpos + 1), treeconfiguration);
//		         }
//		         if (this.spinFactor == UniformInt.of(1,1)) {
		         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(Xpos + 1, j, Zpos), treeconfiguration);
		         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(Xpos + 1, j - 1, Zpos), treeconfiguration);
		         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(Xpos - 1, j, Zpos), treeconfiguration);
		         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(Xpos - 1, j - 1, Zpos), treeconfiguration);
		         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(Xpos, j, Zpos + 1), treeconfiguration);
		         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(Xpos, j - 1, Zpos + 1), treeconfiguration);
		         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(Xpos, j, Zpos - 1), treeconfiguration);
		         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(Xpos, j - 1, Zpos - 1), treeconfiguration);}
//		      }
		      return list;
		   }
	   public void placeBranch(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, RandomSource pRandom, int pFreeTreeHeight, BlockPos pPos, AlienTreeConfig treeconfiguration, int oldspinX, int oldspinZ) {
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
		         int j = pPos.getY() - i;
		         
		         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos2.set(Xpos, j, Zpos), treeconfiguration);
		         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos2.set(Xpos, j - 1, Zpos), treeconfiguration);
//		         if (this.spinFactor == UniformInt.of(1,1)) {
		         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos2.set(Xpos + 1, j, Zpos), treeconfiguration);
		         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos2.set(Xpos + 1, j - 1, Zpos), treeconfiguration);
		         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos2.set(Xpos, j, Zpos + 1), treeconfiguration);
		         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos2.set(Xpos, j - 1, Zpos + 1), treeconfiguration);
//		         }
		      }
	   }
}
