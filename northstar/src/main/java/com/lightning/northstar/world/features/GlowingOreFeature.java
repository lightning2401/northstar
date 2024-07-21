package com.lightning.northstar.world.features;

import java.util.function.Function;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;

public class GlowingOreFeature extends Feature<OreConfiguration> {
	public GlowingOreFeature(Codec<OreConfiguration> pCodec) {
		super(pCodec);
	}

	/**
	 * Places the given feature at the given location.
	 * During world generation, features are provided with a 3x3 region of chunks, centered on the chunk being generated,
	 * that they can safely generate into.
	 * @param pContext A context object with a reference to the level and the position the feature is being placed at
	 */
	public boolean place(FeaturePlaceContext<OreConfiguration> pContext) {
		OreConfiguration config = pContext.config();
		WorldGenLevel level = pContext.level();
		BlockPos origin = pContext.origin();
		RandomSource rando = pContext.random();
		int size = config.size;
		int amount = 0;
		
		for(int x = -rando.nextInt(0,2); x <= rando.nextInt(0,2); x++) {
			for(int y = -rando.nextInt(0,2); y <= rando.nextInt(0,2); y++) {
				for(int z = -rando.nextInt(0,2); z <= rando.nextInt(0,2); z++) {
					BlockPos newpos = origin.offset(x,y,z);
                    BlockState blockstate = level.getBlockState(newpos);
                    for(OreConfiguration.TargetBlockState target : config.targetStates) {	
                    	if(target.target.test(blockstate, rando) && amount <= size) {
                    		level.setBlock(newpos, target.state, z);
                    		amount++;
                    	}
					}
				}
			}
		}
		
		
		
		
		return true;
	}

}
