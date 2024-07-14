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
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

//

public class TestSaplingTrunkPlacer extends TrunkPlacer {
	   public static final Codec<TestSaplingTrunkPlacer> CODEC = RecordCodecBuilder.create((p_226236_) -> {
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
		      }))).apply(p_226236_, TestSaplingTrunkPlacer::new);
		   });
		   private final IntProvider extraBranchSteps;
		   private final float placeBranchPerLogProbability;
		   private final IntProvider extraBranchLength;
		   private final HolderSet<Block> canGrowThrough;
		   private final IntProvider spinFactor;

		   public TestSaplingTrunkPlacer(int int1, int int2, int int3, IntProvider int4, float int5, IntProvider int6, HolderSet<Block> int7, IntProvider spinfacto) {
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
		   public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, RandomSource pRandom, int pFreeTreeHeight, BlockPos pPos, TreeConfiguration treeconfiguration) {
			      List<FoliagePlacer.FoliageAttachment> list = Lists.newArrayList();
				  this.placeLog(pLevel, pBlockSetter, pRandom, pPos, treeconfiguration, Direction.UP);
			      this.placeBranch(pLevel, pBlockSetter, pRandom, 90 + pRandom.nextInt(1, 12), pPos.relative(Direction.NORTH), treeconfiguration, pRandom.nextFloat(), Direction.NORTH);
			      this.placeBranch(pLevel, pBlockSetter, pRandom, 90 + pRandom.nextInt(1, 12), pPos.relative(Direction.SOUTH), treeconfiguration, pRandom.nextFloat(), Direction.SOUTH);
			      this.placeBranch(pLevel, pBlockSetter, pRandom, 90 + pRandom.nextInt(1, 12), pPos.relative(Direction.EAST), treeconfiguration, pRandom.nextFloat(), Direction.EAST);
			      this.placeBranch(pLevel, pBlockSetter, pRandom, 90 + pRandom.nextInt(1, 12), pPos.relative(Direction.WEST), treeconfiguration, pRandom.nextFloat(), Direction.WEST);
			      this.placeOre(pLevel, pBlockSetter, pRandom, 90 + pRandom.nextInt(1, 12), pPos, treeconfiguration);
			      this.placeCap(pLevel, pBlockSetter, pRandom, 90 + pRandom.nextInt(1, 12), pPos, treeconfiguration);
//			      }
			      return list;
			   }
		   public List<FoliagePlacer.FoliageAttachment> placeBranch(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, RandomSource pRandom, int pFreeTreeHeight, 
				   BlockPos pPos, TreeConfiguration treeconfiguration, float bias, Direction dir) {
			   List<FoliagePlacer.FoliageAttachment> list = Lists.newArrayList();
			   BlockPos.MutableBlockPos mutable = pPos.mutable();

			   int yPos = pPos.getY();
			   for(int i = 0; i < pFreeTreeHeight;) {
				   if(pRandom.nextFloat() > 0.7 && i != 0) {
					   if(pRandom.nextFloat() < bias)
					   {mutable.move(dir.getClockWise());}
					   else
					   {mutable.move(dir.getCounterClockWise());}
				   }

				   if(pRandom.nextFloat() > 0.9 && i != 0) {
					   float newBias = pRandom.nextFloat();
					   while(0.49 > Math.abs(bias - newBias))
					   {newBias = pRandom.nextFloat();}
					   System.out.println("bias: " + bias + "     newBias: " + newBias);
					   System.out.println(Math.abs(bias - newBias));
					   this.placeSecondBranch(pLevel, pBlockSetter, pRandom, pFreeTreeHeight - 1, mutable, treeconfiguration, pRandom.nextInt(1,4), newBias, dir, pRandom.nextBoolean() ? dir.getClockWise() : dir.getCounterClockWise());
				   }
				   yPos = mutable.getY();
				   int Xpos = mutable.getX();
				   int Zpos = mutable.getZ();
				   boolean moveFlag = false;
				   if(pLevel.isStateAtPosition(mutable.below(), block -> {return block.isSolidRender((BlockGetter) pLevel, mutable.below());}) && pLevel.isStateAtPosition(mutable, block -> {return block.getMaterial().isReplaceable();})) {
					   yPos = mutable.getY();
					   moveFlag = true;
					   this.placeLog(pLevel, pBlockSetter, pRandom, mutable.set(Xpos, yPos, Zpos), treeconfiguration, dir);
				   }else if(pLevel.isStateAtPosition(mutable.below(), block -> {return block.getMaterial().isReplaceable();})) {
					   this.placeLog(pLevel, pBlockSetter, pRandom, new BlockPos(mutable.set(Xpos, yPos, Zpos)), treeconfiguration, Direction.DOWN);
					   mutable.move(Direction.DOWN);
				   }else if(pLevel.isStateAtPosition(mutable, block -> {return block.isSolidRender((BlockGetter) pLevel, mutable);}) && pLevel.isStateAtPosition(mutable.above(), block -> {return block.getMaterial().isReplaceable();})) {
					   mutable.move(0, 1, 0);
					   moveFlag = true;
					   yPos = mutable.getY();
					   this.placeLog(pLevel, pBlockSetter, pRandom, mutable.set(Xpos, yPos, Zpos), treeconfiguration, dir);
				   }else if(pLevel.isStateAtPosition(mutable.below().below(), block -> {return block.isSolidRender((BlockGetter) pLevel, mutable.below().below());}) && pLevel.isStateAtPosition(mutable.below(), block -> {return block.getMaterial().isReplaceable();})) {
					   mutable.move(0, -1, 0);
					   moveFlag = true;
					   yPos = mutable.getY();
					   this.placeLog(pLevel, pBlockSetter, pRandom, mutable.set(Xpos, yPos, Zpos), treeconfiguration, dir);
				   }else if(pLevel.isStateAtPosition(mutable.relative(dir), block -> {return block.isSolidRender((BlockGetter) pLevel, mutable.relative(dir));}) && pLevel.isStateAtPosition(mutable, block -> {return block.getMaterial().isReplaceable();})) {
					   this.placeLog(pLevel, pBlockSetter, pRandom, new BlockPos(mutable.set(Xpos, yPos, Zpos)), treeconfiguration, Direction.UP);
					   mutable.move(Direction.UP);
				   }else {
					   i += 999999999;
				   }
				   if((pLevel.isStateAtPosition(mutable.relative(dir).below(), block -> {return block.getMaterial().isReplaceable();}) 
				   || pLevel.isStateAtPosition(mutable.relative(dir), block -> {return block.getMaterial().isReplaceable();})
				   || pLevel.isStateAtPosition(mutable.relative(dir).above(), block -> {return block.getMaterial().isReplaceable();})) && moveFlag) {
					   mutable.move(dir);
				   }
				   i++;
			   }
			   return list;
		   }
		   
		   public List<FoliagePlacer.FoliageAttachment> placeSecondBranch(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, RandomSource pRandom, int pFreeTreeHeight, 
				   BlockPos pPos, TreeConfiguration treeconfiguration,int dist, float bias, Direction dir, Direction offshootDir) {
			   List<FoliagePlacer.FoliageAttachment> list = Lists.newArrayList();
			   BlockPos.MutableBlockPos mutable = pPos.mutable();

			   int yPos = pPos.getY();
			   for(int i = 0; i < pFreeTreeHeight;) {
				   Direction newDir = dir;
				   if(pRandom.nextFloat() > 0.7 && i > 3) {
					   if(pRandom.nextFloat() > bias)
					   {mutable.move(dir.getClockWise());}
					   else
					   {mutable.move(dir.getCounterClockWise());}
				   }
				   yPos = mutable.getY();
				   int Xpos = mutable.getX();
				   int Zpos = mutable.getZ();
				   boolean moveFlag = false;

				   if(i <= dist) {
					   newDir = offshootDir;
				   }
				   if(pLevel.isStateAtPosition(mutable.below(), block -> {return block.isSolidRender((BlockGetter) pLevel, mutable.below());}) && pLevel.isStateAtPosition(mutable, block -> {return block.getMaterial().isReplaceable();})) {
					   yPos = mutable.getY();
					   moveFlag = true;
					   this.placeLog(pLevel, pBlockSetter, pRandom, new BlockPos(mutable.set(Xpos, yPos, Zpos)), treeconfiguration, newDir);
				   }else if(pLevel.isStateAtPosition(mutable.below(), block -> {return block.getMaterial().isReplaceable();})) {
					   this.placeLog(pLevel, pBlockSetter, pRandom, new BlockPos(mutable.set(Xpos, yPos, Zpos)), treeconfiguration, Direction.DOWN);
					   mutable.move(Direction.DOWN);
				   }else if(pLevel.isStateAtPosition(mutable, block -> {return block.isSolidRender((BlockGetter) pLevel, mutable);}) && pLevel.isStateAtPosition(mutable.above(), block -> {return block.getMaterial().isReplaceable();})) {
					   mutable.move(0, 1, 0);
					   moveFlag = true;
					   yPos = mutable.getY();
					   this.placeLog(pLevel, pBlockSetter, pRandom, new BlockPos(mutable.set(Xpos, yPos, Zpos)), treeconfiguration, newDir);
				   }else if(pLevel.isStateAtPosition(mutable.below().below(), block -> {return block.isSolidRender((BlockGetter) pLevel, mutable.below().below());}) && pLevel.isStateAtPosition(mutable.below(), block -> {return block.getMaterial().isReplaceable();})) {
					   mutable.move(0, -1, 0);
					   moveFlag = true;
					   yPos = mutable.getY();
					   this.placeLog(pLevel, pBlockSetter, pRandom, new BlockPos(mutable.set(Xpos, yPos, Zpos)), treeconfiguration, newDir);
				   }else if(pLevel.isStateAtPosition(mutable.relative(dir), block -> {return block.isSolidRender((BlockGetter) pLevel, mutable.relative(dir));}) && pLevel.isStateAtPosition(mutable, block -> {return block.getMaterial().isReplaceable();})) {
					   this.placeLog(pLevel, pBlockSetter, pRandom, new BlockPos(mutable.set(Xpos, yPos, Zpos)), treeconfiguration, Direction.UP);
					   mutable.move(Direction.UP);
				   }else {
					   i += 999999999;
				   }
				   if(pLevel.isStateAtPosition(mutable.relative(dir).below(), block -> {return block.getMaterial().isReplaceable();}) 
				   || pLevel.isStateAtPosition(mutable.relative(dir), block -> {return block.getMaterial().isReplaceable();})
				   || pLevel.isStateAtPosition(mutable.relative(dir).above(), block -> {return block.getMaterial().isReplaceable();})) {				   
					   if(i <= dist && moveFlag) 
					   {mutable.move(offshootDir);}
					   else if (moveFlag) {mutable.move(dir);}
				   }
				   i++;
			   }
			   return list;
		   }
		   
		   public void placeOre(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, RandomSource pRandom, int pFreeTreeHeight, 
				   BlockPos pPos, TreeConfiguration treeconfiguration) {
			   
			   for(int x = -1; x <= 1; x++) {
				   for(int y = -1; y <= 1; y++) {
					   for(int z = -1; z <= 1; z++) {
						   pBlockSetter.accept(pPos.offset(x, y - 2, z), NorthstarBlocks.MERCURY_DEEP_TUNGSTEN_ORE.get().defaultBlockState());
					   }   
				   }   
			   }
		   }
		   
		   public void placeCap(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, RandomSource pRandom, int pFreeTreeHeight, 
				   BlockPos pPos, TreeConfiguration treeconfiguration) {
			   for(int x = -1; x <= 1; x++) {
					   for(int z = -1; z <= 1; z++) {
						   pBlockSetter.accept(pPos.offset(x, 1, z), Blocks.BLACKSTONE.defaultBlockState());
					   }   
			   }

			   pBlockSetter.accept(pPos.offset(2, 2, -1), Blocks.BLACKSTONE.defaultBlockState());
			   pBlockSetter.accept(pPos.offset(2, 2, 0), Blocks.BLACKSTONE.defaultBlockState());
			   pBlockSetter.accept(pPos.offset(2, 2, 1), Blocks.BLACKSTONE.defaultBlockState());

			   pBlockSetter.accept(pPos.offset(-1, 2, 2), Blocks.BLACKSTONE.defaultBlockState());
			   pBlockSetter.accept(pPos.offset(0, 2, 2), Blocks.BLACKSTONE.defaultBlockState());
			   pBlockSetter.accept(pPos.offset(1, 2, 2), Blocks.BLACKSTONE.defaultBlockState());
			   
			   pBlockSetter.accept(pPos.offset(-2, 2, -1), Blocks.BLACKSTONE.defaultBlockState());
			   pBlockSetter.accept(pPos.offset(-2, 2, 0), Blocks.BLACKSTONE.defaultBlockState());
			   pBlockSetter.accept(pPos.offset(-2, 2, 1), Blocks.BLACKSTONE.defaultBlockState());
			   
			   pBlockSetter.accept(pPos.offset(-1, 2, -2), Blocks.BLACKSTONE.defaultBlockState());
			   pBlockSetter.accept(pPos.offset(0, 2, -2), Blocks.BLACKSTONE.defaultBlockState());
			   pBlockSetter.accept(pPos.offset(1, 2, -2), Blocks.BLACKSTONE.defaultBlockState());

			   pBlockSetter.accept(pPos.offset(0,2,0), Blocks.SHROOMLIGHT.defaultBlockState());
			   pBlockSetter.accept(pPos.offset(0,3,0), Blocks.SHROOMLIGHT.defaultBlockState());
			   pBlockSetter.accept(pPos.offset(0,4,0), Blocks.SHROOMLIGHT.defaultBlockState());
		   }
		   
		   protected boolean placeLog(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, RandomSource pRandom, BlockPos pPos, TreeConfiguration treeconfiguration, Direction dir) {
			      return this.placeLog(pLevel, pBlockSetter, pRandom, pPos, treeconfiguration, Function.identity(), dir);
		   }
		   protected boolean placeLog(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, RandomSource pRandom, BlockPos pPos, TreeConfiguration treeconfiguration, Function<Object, Object> function, Direction dir) {
			      if (this.validTreePos(pLevel, pPos)) {
			         pBlockSetter.accept(pPos, (BlockState) function.apply(treeconfiguration.trunkProvider.getState(pRandom, pPos).setValue(RotatedPillarBlock.AXIS, dir.getAxis())));
			         return true;
			      } else {
			         return false;
			      }
			   }
		   protected boolean placeBlackstone(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, RandomSource pRandom, BlockPos pPos, TreeConfiguration treeconfiguration, Function<Object, Object> function, Direction dir) {
			   if (this.validTreePos(pLevel, pPos)) {
				   pBlockSetter.accept(pPos, Blocks.BLACKSTONE.defaultBlockState());
				   return true;
			   } else {
				   return false;
			   }
		   }
		   protected boolean placeShroomlight(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, RandomSource pRandom, BlockPos pPos, TreeConfiguration treeconfiguration, Function<Object, Object> function, Direction dir) {
			   if (this.validTreePos(pLevel, pPos)) {
				   pBlockSetter.accept(pPos, Blocks.SHROOMLIGHT.defaultBlockState());
				   return true;
			   } else {
				   return false;
			   }
		   }
		   @Override
		   protected boolean validTreePos(LevelSimulatedReader pLevel, BlockPos pPos) {
			   return pLevel.isStateAtPosition(pPos, block -> (block.isAir())) || pLevel.isStateAtPosition(pPos, block -> (block.getMaterial().isReplaceable()));
		   }
}