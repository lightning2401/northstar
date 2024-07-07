package com.lightning.northstar.block.tech.oxygen_concentrator;

import com.lightning.northstar.Northstar;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.ItemStack;

public class OxygenConcentratorScreen <T extends OxygenConcentratorMenu> extends AbstractContainerScreen<T> implements ContainerListener {
    private final ResourceLocation OXYGEN_CONCENTRATOR = new ResourceLocation(Northstar.MOD_ID, "textures/gui/rocket_station.png");

	public OxygenConcentratorScreen(T pMenu, Inventory pPlayerInventory, Component pTitle) {
		super(pMenu, pPlayerInventory, pTitle);
	}
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
    	this.renderBackground(pPoseStack);
    	super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    	RenderSystem.disableBlend();
    	this.renderBg(pPoseStack, pPartialTick, pMouseX, pMouseY);
    	this.renderText(pPoseStack, pPartialTick, pMouseX, pMouseY);
//    	this.renderOxy(pPoseStack, pPartialTick, pMouseX, pMouseY);
    	this.renderTooltip(pPoseStack, pMouseX, pMouseY);
    }  
    
    protected void renderFg(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {

    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
    	RenderSystem.setShader(GameRenderer::getPositionTexShader);
    	RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    	RenderSystem.setShaderTexture(0, this.OXYGEN_CONCENTRATOR);
    	int i = (this.width - this.imageWidth) / 2;
    	int j = (this.height - this.imageHeight) / 2;
    	this.blit(pPoseStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
    }
    protected void renderText(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        int x = (this.width - 252) / 2;
        int y = (this.height - 140) / 2;
		this.font.draw(pPoseStack, Component.literal("Oxygen: " + this.menu.blockEntity.tank.getPrimaryHandler().getFluidAmount() + "mb"), x + 120, y + 35, 4210752);
    	
    }
    

    protected void renderOxy(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        int x = (this.width - 252) / 2;
        int y = (this.height - 140) / 2;
        pPoseStack.pushPose();
        RenderSystem.applyModelViewMatrix();
        pPoseStack.translate(x, y, 0);
        pPoseStack.popPose();
        
        
        ResourceLocation resourcelocation = new ResourceLocation("create:textures/fluid/oxygen.png");
        RenderSystem.setShaderTexture(0, resourcelocation);
        
        RenderSystem.depthFunc(518);
        pPoseStack.translate(x, y, 0);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        pPoseStack.pushPose();
        RenderSystem.enableDepthTest();
        RenderSystem.colorMask(false, false, false, false);
        fill(pPoseStack, 4680, 2260, -4680, -2260, -16777216);
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.depthFunc(518);
        fill(pPoseStack, 60, 16, 0, 0, -16777216);
        RenderSystem.depthFunc(515);
        pPoseStack.popPose();
        for(int i1 = -1; i1 <= 7; ++i1) {
            for(int j1 = -1; j1 <= 3; ++j1) {
               blit(pPoseStack, 16 * i1, 16 * j1, 0.0F, 0.0F, 16, 16, 16, 16);
            }
         }
        pPoseStack.translate(-x, -y, 0);

    	
    }
	@Override
	public void slotChanged(AbstractContainerMenu pContainerToSend, int pDataSlotIndex, ItemStack pStack) {		
	}
	@Override
	public void dataChanged(AbstractContainerMenu pContainerMenu, int pDataSlotIndex, int pValue) {
	}    

}
