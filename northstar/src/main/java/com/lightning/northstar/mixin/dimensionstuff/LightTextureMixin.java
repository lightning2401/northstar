package com.lightning.northstar.mixin.dimensionstuff;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@Mixin(LightTexture.class)
public class LightTextureMixin {

}
