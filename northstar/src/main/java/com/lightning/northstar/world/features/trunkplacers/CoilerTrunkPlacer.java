package com.lightning.northstar.world.features.trunkplacers;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

//

public class CoilerTrunkPlacer extends TrunkPlacer {
	   public static final Codec<CoilerTrunkPlacer> CODEC = RecordCodecBuilder.create((p_226236_) -> {
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
		      }))).apply(p_226236_, CoilerTrunkPlacer::new);
		   });
		   private final IntProvider extraBranchSteps;
		   private final float placeBranchPerLogProbability;
		   private final IntProvider extraBranchLength;
		   private final HolderSet<Block> canGrowThrough;
		   private final IntProvider spinFactor;

		   public CoilerTrunkPlacer(int int1, int int2, int int3, IntProvider int4, float int5, IntProvider int6, HolderSet<Block> int7, IntProvider spinfacto) {
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
			      int spinX = 0;
			      int spinZ = 0;
			      int spinZAdd = -1;
			      int spinXAdd = 1;
			      boolean spinOrder = false;

			      for(int i = 0; i < pFreeTreeHeight; ++i) {
			    	 // these are here to dictate the "movement" of the trunk,
			    	 // spin order exists to stop the trunk from looking very dumb
			    	 if (spinOrder == false) {
			    	 spinX = spinX + spinXAdd;
			    	 if (spinX >= 1) {spinXAdd = -1; spinOrder = true;}
			    	 if (spinX <= -1) {spinXAdd = 1; spinOrder = true;}}
			    	 
			    	 if (spinOrder == true){
			    	 spinZ = spinZ + spinZAdd;
			    	 if (spinZ >= 1) {spinZAdd = -1; spinOrder = false;}
			    	 if (spinZ <= -1) {spinZAdd = 1; spinOrder = false;}}
			    	 
			    	 int Xpos = pPos.getX() + spinX;
			    	 int Zpos = pPos.getZ() + spinZ;
			         int j = pPos.getY() + i;
			         
			         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(Xpos, j, Zpos), pConfig);
			         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(Xpos, j - 1, Zpos), pConfig);

			         if (i == pFreeTreeHeight - 1) {
				         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(Xpos + 1, j, Zpos), pConfig);
				         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(Xpos - 1, j, Zpos), pConfig);
				         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(Xpos, j, Zpos + 1), pConfig);
				         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos.set(Xpos, j, Zpos - 1), pConfig);
			            list.add(new FoliagePlacer.FoliageAttachment(blockpos$mutableblockpos.set(Xpos, j + 1, Zpos), 0, false));
			         }
			      }
			      BlockPos.MutableBlockPos blockpos$mutableblockpos2 = new BlockPos.MutableBlockPos();
			      int spinX2 = 0;
			      int spinZ2 = 0;
			      int spinZAdd2 = -1;
			      int spinXAdd2 =  1;
			      boolean spinOrder2 = false;

			      for(int i2 = 0; i2 < pFreeTreeHeight; ++i2) {
			    	 // these are here to dictate the "movement" of the trunk,
			    	 // spin order exists to stop the trunk from looking very dumb
			    	 if (spinOrder2 == false) {
			    	 spinX2 = spinX2 + spinXAdd2;
			    	 if (spinX2 >= 2) {spinXAdd2 = -1; spinOrder2 = true;}
			    	 if (spinX2 <= -2) {spinXAdd2 = 1; spinOrder2 = true;}}
			    	 
			    	 if (spinOrder2 == true){
			    	 spinZ2 = spinZ2 + spinZAdd2;
			    	 if (spinZ2 >= 2) {spinZAdd2 = -1; spinOrder2 = false;}
			    	 if (spinZ2 <= -2) {spinZAdd2 = 1; spinOrder2 = false;}}
			    	 
			    	 int Xpos = pPos.getX() + spinX2;
			    	 int Zpos = pPos.getZ() + spinZ2;
			         int j = pPos.getY() + i2;
			         
			         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos2.set(Xpos, j, Zpos), pConfig);
			         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos2.set(Xpos, j - 1, Zpos), pConfig);

			         if (i2 == pFreeTreeHeight - 1) {
				         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos2.set(Xpos + 1, j, Zpos), pConfig);
				         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos2.set(Xpos - 1, j, Zpos), pConfig);
				         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos2.set(Xpos, j, Zpos + 1), pConfig);
				         this.placeLog(pLevel, pBlockSetter, pRandom, blockpos$mutableblockpos2.set(Xpos, j, Zpos - 1), pConfig);
			            list.add(new FoliagePlacer.FoliageAttachment(blockpos$mutableblockpos2.set(Xpos, j + 1, Zpos), 0, false));
			         }
			      }
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
		   @Override
		   protected boolean validTreePos(LevelSimulatedReader pLevel, BlockPos pPos) {
		      return super.validTreePos(pLevel, pPos) || pLevel.isStateAtPosition(pPos, (p_226232_) -> {
		         return p_226232_.is(this.canGrowThrough);
		      });
		   }
		}