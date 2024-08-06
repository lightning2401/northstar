package com.lightning.northstar.entity.variants.renderers;

import com.lightning.northstar.Northstar;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FrozenZombieRenderer extends ZombieRenderer {
   private static final ResourceLocation TEXTURE_LOCATION = Northstar.asResource("textures/entity/frozen_zombie.png");

   public FrozenZombieRenderer(EntityRendererProvider.Context p_174180_) {
      super(p_174180_, ModelLayers.ZOMBIE, ModelLayers.ZOMBIE_INNER_ARMOR, ModelLayers.ZOMBIE_OUTER_ARMOR);
   }

   /**
    * Returns the location of an entity's texture.
    */
   public ResourceLocation getTextureLocation(Zombie pEntity) {
      return TEXTURE_LOCATION;
   }
}