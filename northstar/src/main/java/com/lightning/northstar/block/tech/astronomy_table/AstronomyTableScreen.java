package com.lightning.northstar.block.tech.astronomy_table;

import com.lightning.northstar.Northstar;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AstronomyTableScreen extends AbstractContainerScreen<AstronomyTableMenu> implements ContainerListener {
	   private static final ResourceLocation TABLE_LOCATION =  Northstar.asResource("textures/gui/astronomy_table.png");
	   private static final Component DIFFERENT_PLANETS_TEXT = Component.translatable("container.northstar.different_planets");
	   private static final Component CLOSE_DATA_TEXT = Component.translatable("container.northstar.close_data");

	public AstronomyTableScreen(AstronomyTableMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
		super(pMenu, pPlayerInventory, pTitle);
	}

	   protected void subInit() {
	   }

	   protected void init() {
	      super.init();
	      this.subInit();
	      this.menu.addSlotListener(this);
	   }

	   public void removed() {
	      super.removed();
	      this.menu.removeSlotListener(this);
	   }

	   public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
	      this.renderBackground(pPoseStack);
	      super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
	      RenderSystem.disableBlend();
	      this.renderFg(pPoseStack, pMouseX, pMouseY, pPartialTick);
	      this.renderTooltip(pPoseStack, pMouseX, pMouseY);
	   }

	   protected void renderFg(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {

	   }
	   
	   protected void renderLabels(PoseStack pPoseStack, int pX, int pY) {
		      RenderSystem.disableBlend();
		      super.renderLabels(pPoseStack, pX, pY);
		         int j = 8453920;
		         Component component = null;

		         j = 16736352;
		         if (this.menu.dataTooFar) {component = CLOSE_DATA_TEXT;}
		         if (this.menu.differentPlanets) {component = DIFFERENT_PLANETS_TEXT;}
		         if (component != null) {
		            int k = this.imageWidth - 8 - this.font.width(component) - 2;
		            fill(pPoseStack, k - 2, 67, this.imageWidth - 8, 79, 1325400064);
		            this.font.drawShadow(pPoseStack, component, (float)k, 69.0F, j);
		         }
		      
	   }

	   @SuppressWarnings("static-access")
	protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pX, int pY) {
	      RenderSystem.setShader(GameRenderer::getPositionTexShader);
	      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
	      RenderSystem.setShaderTexture(0, this.TABLE_LOCATION);
	      int i = (this.width - this.imageWidth) / 2;
	      int j = (this.height - this.imageHeight) / 2;
	      this.blit(pPoseStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
	      this.blit(pPoseStack, i + 59, j + 20, 0, this.imageHeight + (this.menu.getSlot(0).hasItem() ? 0 : 16), 110, 16);
	      if ((this.menu.getSlot(0).hasItem() || this.menu.getSlot(1).hasItem() || this.menu.getSlot(2).hasItem()) && !this.menu.getSlot(3).hasItem()) {
	         this.blit(pPoseStack, i + 99, j + 45, this.imageWidth, 0, 28, 21);
	      }

	   }

	   public void dataChanged(AbstractContainerMenu pContainerMenu, int pDataSlotIndex, int pValue) {
	   }

	   /**
	    * Sends the contents of an inventory slot to the client-side Container. This doesn't have to match the actual
	    * contents of that slot.
	    */
	   public void slotChanged(AbstractContainerMenu pContainerToSend, int pSlotInd, ItemStack pStack) {
	   }
}
