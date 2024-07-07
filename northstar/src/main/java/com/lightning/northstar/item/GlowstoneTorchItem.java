package com.lightning.northstar.item;

import java.util.Map;

import javax.annotation.Nullable;

import com.lightning.northstar.block.GlowstoneTorchWallBlock;
import com.lightning.northstar.block.NorthstarTechBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;

public class GlowstoneTorchItem extends BlockItem {
	   protected final Block wallBlock;

	   public GlowstoneTorchItem(GlowstoneTorchWallBlock pStandingBlock, Properties pProperties) {
	      super(pStandingBlock, pProperties);
	      this.wallBlock = NorthstarTechBlocks.GLOWSTONE_TORCH_WALL.get();
	   }

	   @Nullable
	   protected BlockState getPlacementState(BlockPlaceContext pContext) {
	      BlockState blockstate = this.wallBlock.getStateForPlacement(pContext);
	      BlockState blockstate1 = null;
	      LevelReader levelreader = pContext.getLevel();
	      BlockPos blockpos = pContext.getClickedPos();

	      for(Direction direction : pContext.getNearestLookingDirections()) {
	         if (direction != Direction.UP) {
	            BlockState blockstate2 = direction == Direction.DOWN ? this.getBlock().getStateForPlacement(pContext) : blockstate;
	            if (blockstate2 != null && blockstate2.canSurvive(levelreader, blockpos)) {
	               blockstate1 = blockstate2;
	               break;
	            }
	         }
	      }

	      return blockstate1 != null && levelreader.isUnobstructed(blockstate1, blockpos, CollisionContext.empty()) ? blockstate1 : null;
	   }

	   public void registerBlocks(Map<Block, Item> pBlockToItemMap, Item pItem) {
	      super.registerBlocks(pBlockToItemMap, pItem);
	      pBlockToItemMap.put(this.wallBlock, pItem);
	   }

	   public void removeFromBlockToItemMap(Map<Block, Item> blockToItemMap, Item itemIn) {
	      super.removeFromBlockToItemMap(blockToItemMap, itemIn);
	      blockToItemMap.remove(this.wallBlock);
	   }
	}
