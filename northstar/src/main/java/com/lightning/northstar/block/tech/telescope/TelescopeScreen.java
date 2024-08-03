package com.lightning.northstar.block.tech.telescope;

import java.util.List;

import com.google.common.collect.Lists;
import com.lightning.northstar.Northstar;
import com.lightning.northstar.NorthstarPackets;
import com.lightning.northstar.world.dimension.NorthstarDimensions;
import com.lightning.northstar.world.dimension.NorthstarPlanets;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.utility.Lang;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

@SuppressWarnings("unused")
public class TelescopeScreen extends AbstractContainerScreen<TelescopeMenu>{
    private static final ResourceLocation TELESCOPE_TEXTURE = new ResourceLocation(Northstar.MOD_ID, "textures/gui/telescope_gui.png");
    private static final ResourceLocation TELESCOPE_TEXTURE_SIDE = new ResourceLocation(Northstar.MOD_ID, "textures/gui/telescope_gui_side.png");
    private static final ResourceLocation MERCURY = new ResourceLocation(Northstar.MOD_ID, "textures/environment/mercury_far.png");
    private static final ResourceLocation VENUS = new ResourceLocation(Northstar.MOD_ID, "textures/environment/venus_far.png");
    private static final ResourceLocation EARTH = new ResourceLocation(Northstar.MOD_ID, "textures/environment/earth_far.png");
    private static final ResourceLocation MOON = new ResourceLocation(Northstar.MOD_ID, "textures/environment/moon_far.png");
    private static final ResourceLocation MARS = new ResourceLocation(Northstar.MOD_ID, "textures/environment/mars_far.png");
    private static final ResourceLocation PHOBOS_DEIMOS = new ResourceLocation(Northstar.MOD_ID, "textures/environment/phobos_and_deimos_far.png");
    private static final ResourceLocation CERES = new ResourceLocation(Northstar.MOD_ID, "textures/environment/ceres_far.png");
    private static final ResourceLocation JUPITER = new ResourceLocation(Northstar.MOD_ID, "textures/environment/jupiter_far.png");
    private static final ResourceLocation SATURN = new ResourceLocation(Northstar.MOD_ID, "textures/environment/saturn_far.png");
    private static final ResourceLocation URANUS = new ResourceLocation(Northstar.MOD_ID, "textures/environment/uranus_far.png");
    private static final ResourceLocation NEPTUNE = new ResourceLocation(Northstar.MOD_ID, "textures/environment/neptune_far.png");
    private static final ResourceLocation PLUTO = new ResourceLocation(Northstar.MOD_ID, "textures/environment/pluto_far.png");
    private static final ResourceLocation ERIS = new ResourceLocation(Northstar.MOD_ID, "textures/environment/eris_far.png");
    private static final ResourceLocation BACKGROUND = new ResourceLocation(Northstar.MOD_ID, "textures/environment/space_background.png");
    private static final ResourceLocation MOON_GLOW = new ResourceLocation("textures/environment/moon_phases.png");
    private static final ResourceLocation MOON_FLAT = new ResourceLocation(Northstar.MOD_ID, "textures/environment/moon_flat.png");
    
    private boolean isScrolling;
    private double scrollX = 450;
    private double scrollY = 450;
    
    private int minX = Integer.MAX_VALUE;
    private int minY = Integer.MAX_VALUE;
	private int maxX = Integer.MIN_VALUE;
    private int maxY = Integer.MIN_VALUE;
    private Level level;
    private Inventory inv;
    public String SelectedPlanet = null;
    

	public TelescopeScreen(TelescopeMenu pMenu, Inventory pPlayerInventory, Component pTitle) 
		{super(pMenu, pPlayerInventory, pTitle); inv = pPlayerInventory;}
	
	
	@Override
	protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        int x = imageWidth / 2 - 25;
        int y = this.titleLabelY - 43;
		this.font.draw(pPoseStack, this.title, x, y, 4210752);
	}
	
    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        int x = (width - (imageWidth + (imageWidth / 2))) / 2;
        int y = (height - (imageHeight + (imageHeight / 2))) / 2;
        
        pPoseStack.pushPose();
        pPoseStack.translate(0.0D, 0.0D, 950.0D);
        RenderSystem.enableDepthTest();
        RenderSystem.colorMask(false, false, false, false);
        fill(pPoseStack, 4680, 2260, -4680, -2260, -16777216);
        RenderSystem.colorMask(true, true, true, true);
        pPoseStack.translate(0.0D, 0.0D, -950.0D);
        RenderSystem.depthFunc(518);
        fill(pPoseStack, x + 255, y + 255, x + 1, y + 1   , -16777216);
        RenderSystem.depthFunc(515);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BACKGROUND);

        int i = Mth.floor(this.scrollX);
        int j = Mth.floor(this.scrollY);
        int k = i % 900;
        int l = j % 900;

        for(int i1 = -1; i1 <= 15; ++i1) {
           for(int j1 = -1; j1 <= 8; ++j1) {
              blit(pPoseStack, k + 900 * i1, l + 900 * j1, 0.0F, 0.0F, 900, 900, 900, 900);
           }
        }
    	
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TELESCOPE_TEXTURE);

        this.blit(pPoseStack, x, y, 0, 0, 255, 255);
    }    
    
    @SuppressWarnings("resource")
	public void renderPlanets(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
    	
    	ResourceKey<Level> player_dim = Minecraft.getInstance().player.getLevel().dimension();
    	if (player_dim != NorthstarDimensions.MARS_DIM_KEY) {
        int mars_x = (int) NorthstarPlanets.mars_x;
        int mars_y = (int) NorthstarPlanets.mars_y;
        pPoseStack.pushPose();
        pPoseStack.scale(0.05F, 0.05F, 0.05F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, MARS);
        this.blit(pPoseStack, (mars_x * 20) + (int)scrollX * 20, (mars_y * 20) + (int)scrollY * 20, 0, 0, 255, 255);
        pPoseStack.popPose();
        
        int pd_x = (int) NorthstarPlanets.pd_x;
        int pd_y = (int) NorthstarPlanets.pd_y;
        pPoseStack.pushPose();
        pPoseStack.scale(0.05F, 0.05F, 0.05F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, PHOBOS_DEIMOS);
        this.blit(pPoseStack, (pd_x * 20) + (int)scrollX * 20, (pd_y * 20) + (int)scrollY * 20, 0, 0, 255, 255);
        pPoseStack.popPose();}
        
    	if (player_dim != NorthstarDimensions.VENUS_DIM_KEY) {
        int venus_x = (int) NorthstarPlanets.venus_x;
        int venus_y = (int) NorthstarPlanets.venus_y;
        pPoseStack.pushPose();
        pPoseStack.scale(0.05F, 0.05F, 0.05F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, VENUS);
        this.blit(pPoseStack, ((venus_x * 20) + (int)scrollX * 20), ((venus_y * 20) + (int)scrollY * 20), 0, 0, 255, 255);
        pPoseStack.popPose();}

    	if (player_dim != NorthstarDimensions.MERCURY_DIM_KEY) {
        int mercury_x = (int) NorthstarPlanets.mercury_x;
        int mercury_y = (int) NorthstarPlanets.mercury_y;
        pPoseStack.pushPose();
        pPoseStack.scale(0.05F, 0.05F, 0.05F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, MERCURY);
        this.blit(pPoseStack, (mercury_x * 20) + (int)scrollX * 20, (mercury_y * 20) + (int)scrollY * 20, 0, 0, 255, 255);
        pPoseStack.popPose();
    	}
        
        int jupiter_x = (int) NorthstarPlanets.jupiter_x;
        int jupiter_y = (int) NorthstarPlanets.jupiter_y;
        pPoseStack.pushPose();
        pPoseStack.scale(0.05F, 0.05F, 0.05F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, JUPITER);
        this.blit(pPoseStack, (jupiter_x * 20) + (int)scrollX * 20, (jupiter_y * 20) + (int)scrollY * 20, 0, 0, 255, 255);
        pPoseStack.popPose();
        
        int saturn_x = (int) NorthstarPlanets.saturn_x;
        int saturn_y = (int) NorthstarPlanets.saturn_y;
        pPoseStack.pushPose();
        pPoseStack.scale(0.05F, 0.05F, 0.05F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, SATURN);
        this.blit(pPoseStack, (saturn_x * 20) + (int)scrollX * 20, (saturn_y * 20) + (int)scrollY * 20, 0, 0, 255, 255);
        pPoseStack.popPose();
        
        int uranus_x = (int) NorthstarPlanets.uranus_x;
        int uranus_y = (int) NorthstarPlanets.uranus_y;
        pPoseStack.pushPose();
        pPoseStack.scale(0.05F, 0.05F, 0.05F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, URANUS);
        this.blit(pPoseStack, (uranus_x * 20) + (int)scrollX * 20, (uranus_y * 20) + (int)scrollY * 20, 0, 0, 255, 255);
        pPoseStack.popPose();
        
        int neptune_x = (int) NorthstarPlanets.neptune_x;
        int neptune_y = (int) NorthstarPlanets.neptune_y;
        pPoseStack.pushPose();
        pPoseStack.scale(0.05F, 0.05F, 0.05F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, NEPTUNE);
        this.blit(pPoseStack, (neptune_x * 20) + (int)scrollX * 20, (neptune_y * 20) + (int)scrollY * 20, 0, 0, 255, 255);
        pPoseStack.popPose();
        
        int pluto_x = (int) NorthstarPlanets.pluto_x;
        int pluto_y = (int) NorthstarPlanets.pluto_x;
        pPoseStack.pushPose();
        pPoseStack.scale(0.05F, 0.05F, 0.05F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, PLUTO);
        this.blit(pPoseStack, (pluto_x * 20) + (int)scrollX * 20, (pluto_y * 20) + (int)scrollY * 20, 0, 0, 255, 255);
        pPoseStack.popPose();
        
        int eris_x = (int) NorthstarPlanets.eris_x;
        int eris_y = (int) NorthstarPlanets.eris_y;
        pPoseStack.pushPose();
        pPoseStack.scale(0.05F, 0.05F, 0.05F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, ERIS);
        this.blit(pPoseStack, (eris_x * 20) + (int)scrollX * 20, (eris_y * 20) + (int)scrollY * 20, 0, 0, 255, 255);
        pPoseStack.popPose();
        
    	if (player_dim != ClientLevel.OVERWORLD) {
    		
        int earth_x = (int) NorthstarPlanets.earth_x;
        int earth_y = (int) NorthstarPlanets.earth_y;
        pPoseStack.pushPose();
        pPoseStack.scale(0.05F, 0.05F, 0.05F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, EARTH);
        this.blit(pPoseStack, (earth_x * 20) + (int)scrollX * 20, (earth_y * 20) + (int)scrollY * 20, 0, 0, 255, 255);
        pPoseStack.popPose();
        int moon_x = (int) NorthstarPlanets.moon_x;
        int moon_y = (int) NorthstarPlanets.moon_y;
        pPoseStack.pushPose();
        pPoseStack.scale(0.05F, 0.05F, 0.05F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, MOON);
        this.blit(pPoseStack, (moon_x * 20) + (int)scrollX * 20, (moon_y * 20) + (int)scrollY * 20, 0, 0, 255, 255);
        pPoseStack.popPose();
    	}
        
        
    	if (player_dim == ClientLevel.OVERWORLD) {
        RenderSystem.enableBlend();
	 	RenderSystem.defaultBlendFunc();
	    RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        pPoseStack.pushPose();
        pPoseStack.scale(2F, 1F, 1F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, MOON_GLOW);
        int moon_phase = Minecraft.getInstance().level.getMoonPhase();
        int moon_uv_x = (moon_phase % 4) * 64;
        int moon_uv_y = (moon_phase / 4) * 128;
        this.blit(pPoseStack, ((int)NorthstarPlanets.earth_moon_x + (int)scrollX / 2) - 27, ((int)NorthstarPlanets.earth_moon_y + (int)scrollY) - 57, 0 + moon_uv_x, 0 + moon_uv_y, 64, 128);
        pPoseStack.popPose();
        RenderSystem.disableBlend();
        pPoseStack.scale(2F, 1F, 1F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, MOON_FLAT);
        this.blit(pPoseStack, ((int)NorthstarPlanets.earth_moon_x + (int)scrollX / 2) - 27, ((int)NorthstarPlanets.earth_moon_y + (int)scrollY) - 57, 0 + moon_uv_x, 0 + moon_uv_y, 64, 128);
        pPoseStack.popPose();}
    }
    
    
    public void renderPlanetTooltip(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
        int mars_x = (int) NorthstarPlanets.mars_x;
        int mars_y = (int) NorthstarPlanets.mars_y;
        if (Math.abs(mars_x - mouseX) < 15 && Math.abs(mars_y - mouseY) < 15) {
        	
        }
    }
    
    protected void renderFrame(PoseStack pPoseStack, int mouseX, int mouseY) {
        int x = ((width - (imageWidth + (imageWidth / 2))) / 2);
        int y = (height - (imageHeight + (imageHeight / 2))) / 2;
        int x2 = (int) (x - (imageWidth / 1.19));
    	
        
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TELESCOPE_TEXTURE);

        this.blit(pPoseStack, x, y, 0, 0, 255, 255);
        
        RenderSystem.depthFunc(518);
        pPoseStack.translate(0.0D, 0.0D, -950.0D);
        RenderSystem.colorMask(false, false, false, false);
        fill(pPoseStack, 4680, 2260, -4680, -2260, -16777216);
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.depthFunc(515);

        
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TELESCOPE_TEXTURE_SIDE);
        this.blit(pPoseStack, x2, y, 0, 0, 255, 255);     
        
        if (SelectedPlanet != null) 
        {this.font.draw(pPoseStack, getPlanetName(SelectedPlanet), (float) (x2 + (imageWidth / 1.6)), (float) (y + (imageHeight / 2.4)), 6944);
        this.font.draw(pPoseStack, "X: " + (int) NorthstarPlanets.getPlanetX(SelectedPlanet), (float) (x2 + (imageWidth / 1.6)), (float) (y + (imageHeight / 1.48)), 6944);
        this.font.draw(pPoseStack, "Y: " + (int) NorthstarPlanets.getPlanetY(SelectedPlanet), (float) (x2 + (imageWidth / 1.6)), (float) (y + (imageHeight / 1.36)), 6944);}
        
        
        
    }
    
	
    @Override
    public void render(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, mouseX, mouseY, delta);
        renderPlanets(pPoseStack, mouseX, mouseY, delta);
        renderFrame(pPoseStack, mouseX, mouseY);

        renderTooltip(pPoseStack, mouseX, mouseY);
        renderPlanetTooltips(pPoseStack, mouseX, mouseY);
        renderLabels(pPoseStack, mouseX, mouseY);
        renderSelectedPlanet(pPoseStack);
        renderButton(pPoseStack, mouseX, mouseY, delta);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        
    }
    
    public boolean paperCheck() {
    	boolean flag = false;
    
    	if (menu.inv != null) {
    	for(int p = 0; p < 36; p++) {
    		ItemStack items = inv.getItem(p);
    		Item item = items.getItem();
    		if (item == Items.PAPER) {
    			flag = true;}}
    }
		return flag;}
    
    
    
    public void renderButton(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
    	pPoseStack.scale(10, 10, 10);
        int x = ((width - (imageWidth + (imageWidth / 2))) / 2);
        int y = (height - (imageHeight + (imageHeight / 2))) / 2;
        IconButton printButton = new IconButton(x + imageWidth - 204, y + imageHeight - 1, AllIcons.I_ADD);
        printButton.withCallback(() -> {
        	if(SelectedPlanet != null) {
			NorthstarPackets.getChannel()
				.sendToServer(TelescopePrintPacket.print(menu.blockEntity.getBlockPos(), SelectedPlanet));
//			System.out.println("WE'VE BEEN CLICKED, SCATTER!!!!!");
			}
		});

        printButton.setToolTip(Component.translatable("container.northstar.paper_check"));
		printButton.render(pPoseStack, mouseX, mouseY, delta);
		printButton.renderToolTip(pPoseStack, mouseX, mouseY);
		if(paperCheck())
		{printButton.active = true;}else {printButton.active = false;}
		printButton.renderButton(pPoseStack, mouseX, mouseY, delta);
		if(printButton.isMouseOver(mouseX, mouseY)) {printButton.renderToolTip(pPoseStack, mouseX, mouseY);}
		addRenderableWidget(printButton);
		if ((Math.abs(x + imageWidth - 196 - mouseX) < 9 && Math.abs(y + imageHeight - 1 + 8 - mouseY) < 9)) {
			List<Component> list = Lists.newArrayList();
			RenderSystem.colorMask(true, true, true, true);
			list.add((Lang.translateDirect("northstar.gui.telescope.button_tooltip").withStyle(ChatFormatting.WHITE)));

		    this.renderComponentTooltip(pPoseStack, list, mouseX, mouseY);
		}
    }
    
    public void renderSelectedPlanet(PoseStack pPoseStack) {
        int x = ((width - (imageWidth + (imageWidth / 2))) / 2);
        int y = (height - (imageHeight + (imageHeight / 2))) / 2;
        int x2 = (int) (x - (imageWidth / 1.19));
        
        pPoseStack.scale(0.1F, 0.1F, 0.1F);
        if (SelectedPlanet != null)
        {RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, getPlanetSprite(SelectedPlanet));
        this.blit(pPoseStack, (int) (x2 + (imageWidth / 1.51)) * 10,  (int)(y + (imageHeight / 2.06)) * 10, 0, 0, 255, 255);}
    }
    
    
    
    @SuppressWarnings("resource")
	public void renderPlanetTooltips(PoseStack pPoseStack, int mouseX, int mouseY) {
        int x = ((width - (imageWidth + (imageWidth / 2))) / 2);
        int y = (height - (imageHeight + (imageHeight / 2))) / 2;
    	
    	ResourceKey<Level> player_dim = Minecraft.getInstance().player.getLevel().dimension();
        if ((Math.abs(NorthstarPlanets.mars_x + scrollX + 8 - mouseX) < 8 && Math.abs(NorthstarPlanets.mars_y + scrollY + 8 - mouseY) < 8) && player_dim != NorthstarDimensions.MARS_DIM_KEY) {
            List<Component> list = Lists.newArrayList();
            RenderSystem.colorMask(true, true, true, true);
            list.add((Component.translatable("planets.mars.name").withStyle(ChatFormatting.AQUA)));
            list.add((Component.translatable("planets.mars.type").withStyle(ChatFormatting.GRAY)));
            list.add((Component.translatable("planets.mars.grav").withStyle(ChatFormatting.GRAY)));
            list.add((Component.translatable("planets.mars.temp").withStyle(ChatFormatting.GRAY)));
            list.add((Component.translatable("planets.mars.atmosphere").withStyle(ChatFormatting.GRAY)));
            list.add((Component.literal("X:  " + String.valueOf((int)NorthstarPlanets.mars_x)).withStyle(ChatFormatting.WHITE)));
            list.add((Component.literal("Y:  " + String.valueOf((int)NorthstarPlanets.mars_y)).withStyle(ChatFormatting.WHITE)));
            
            this.renderComponentTooltip(pPoseStack, list, mouseX, mouseY);
        }else
        if ((Math.abs(NorthstarPlanets.earth_x + scrollX + 8 - mouseX) < 8 && Math.abs(NorthstarPlanets.earth_y + scrollY + 8 - mouseY) < 8) && player_dim != ClientLevel.OVERWORLD) {
            List<Component> list = Lists.newArrayList();
            RenderSystem.colorMask(true, true, true, true);
            list.add((Component.translatable("planets.earth.name").withStyle(ChatFormatting.AQUA)));
            list.add((Component.translatable("planets.earth.type").withStyle(ChatFormatting.GRAY)));
            list.add((Component.translatable("planets.earth.grav").withStyle(ChatFormatting.GRAY)));
            list.add((Component.translatable("planets.earth.temp").withStyle(ChatFormatting.GRAY)));
            list.add((Component.translatable("planets.earth.atmosphere").withStyle(ChatFormatting.GRAY)));
            list.add((Component.literal("X:  " + String.valueOf((int)NorthstarPlanets.earth_x)).withStyle(ChatFormatting.WHITE)));
            list.add((Component.literal("Y:  " + String.valueOf((int)NorthstarPlanets.earth_y)).withStyle(ChatFormatting.WHITE)));
               
            this.renderComponentTooltip(pPoseStack, list, mouseX, mouseY);
        }else        	
        if ((Math.abs(NorthstarPlanets.moon_x + scrollX + 8 - mouseX) < 8 && Math.abs(NorthstarPlanets.moon_y + scrollY + 8 - mouseY) < 8) && player_dim != ClientLevel.OVERWORLD && player_dim != NorthstarDimensions.MOON_DIM_KEY ) {
            List<Component> list = Lists.newArrayList();
            RenderSystem.colorMask(true, true, true, true);
            list.add((Component.translatable("planets.moon.name").withStyle(ChatFormatting.AQUA)));
            list.add((Component.translatable("planets.moon.type").withStyle(ChatFormatting.GRAY)));
            list.add((Component.translatable("planets.moon.grav").withStyle(ChatFormatting.GRAY)));
            list.add((Component.translatable("planets.moon.temp").withStyle(ChatFormatting.GRAY)));
            list.add((Component.translatable("planets.moon.atmosphere").withStyle(ChatFormatting.GRAY)));
            list.add((Component.literal("X:  " + String.valueOf((int)NorthstarPlanets.moon_x)).withStyle(ChatFormatting.WHITE)));
            list.add((Component.literal("Y:  " + String.valueOf((int)NorthstarPlanets.moon_y)).withStyle(ChatFormatting.WHITE)));
                   
            this.renderComponentTooltip(pPoseStack, list, mouseX, mouseY);
        }else      
        if ((Math.abs((NorthstarPlanets.pd_x) + scrollX + 5 - mouseX) < 5 && Math.abs((NorthstarPlanets.pd_y) + scrollY + 5 - mouseY)< 5) && player_dim != NorthstarDimensions.MARS_DIM_KEY) {
            List<Component> list = Lists.newArrayList();
            RenderSystem.colorMask(true, true, true, true);
            list.add((Component.translatable("planets.phobos_deimos.name").withStyle(ChatFormatting.AQUA)));
            list.add((Component.translatable("planets.phobos_deimos.type").withStyle(ChatFormatting.GRAY)));
            list.add((Component.translatable("planets.phobos_deimos.grav").withStyle(ChatFormatting.GRAY)));
            list.add((Component.translatable("planets.phobos_deimos.temp").withStyle(ChatFormatting.GRAY)));
            list.add((Component.translatable("planets.phobos_deimos.atmosphere").withStyle(ChatFormatting.GRAY)));
            list.add((Component.literal("X:  " + String.valueOf((int)NorthstarPlanets.pd_x)).withStyle(ChatFormatting.WHITE)));
            list.add((Component.literal("Y:  " + String.valueOf((int)NorthstarPlanets.pd_y)).withStyle(ChatFormatting.WHITE)));
                        
            this.renderComponentTooltip(pPoseStack, list, mouseX, mouseY);
        }else
        if ((Math.abs((NorthstarPlanets.venus_x) + scrollX + 8 - mouseX) < 8 && Math.abs((NorthstarPlanets.venus_y) + scrollY + 8 - mouseY)< 8) && player_dim != NorthstarDimensions.VENUS_DIM_KEY) {
            List<Component> list = Lists.newArrayList();
            RenderSystem.colorMask(true, true, true, true);
            list.add((Component.translatable("planets.venus.name").withStyle(ChatFormatting.AQUA)));
            list.add((Component.translatable("planets.venus.type").withStyle(ChatFormatting.GRAY)));
            list.add((Component.translatable("planets.venus.grav").withStyle(ChatFormatting.GRAY)));
            list.add((Component.translatable("planets.venus.temp").withStyle(ChatFormatting.GRAY)));
            list.add((Component.translatable("planets.venus.atmosphere").withStyle(ChatFormatting.GRAY)));
            list.add((Component.literal("X:  " + String.valueOf((int)NorthstarPlanets.venus_x)).withStyle(ChatFormatting.WHITE)));
            list.add((Component.literal("Y:  " + String.valueOf((int)NorthstarPlanets.venus_y)).withStyle(ChatFormatting.WHITE)));
            
            this.renderComponentTooltip(pPoseStack, list, mouseX, mouseY);
        }else
        if ((Math.abs(NorthstarPlanets.mercury_x + scrollX + 7 - mouseX) < 8 && Math.abs(NorthstarPlanets.mercury_y + scrollY + 7 - mouseY) < 8) && player_dim != NorthstarDimensions.MERCURY_DIM_KEY) {
            List<Component> list = Lists.newArrayList();
            RenderSystem.colorMask(true, true, true, true);
            list.add((Component.translatable("planets.mercury.name").withStyle(ChatFormatting.AQUA)));
            list.add((Component.translatable("planets.mercury.type").withStyle(ChatFormatting.GRAY)));
            list.add((Component.translatable("planets.mercury.grav").withStyle(ChatFormatting.GRAY)));
            list.add((Component.translatable("planets.mercury.temp").withStyle(ChatFormatting.GRAY)));
            list.add((Component.translatable("planets.mercury.atmosphere").withStyle(ChatFormatting.GRAY)));
            list.add((Component.literal("X:  " + String.valueOf((int)NorthstarPlanets.mercury_x)).withStyle(ChatFormatting.WHITE)));
            list.add((Component.literal("Y:  " + String.valueOf((int)NorthstarPlanets.mercury_y)).withStyle(ChatFormatting.WHITE)));
            
            this.renderComponentTooltip(pPoseStack, list, mouseX, mouseY);
        }else
        if ((Math.abs(NorthstarPlanets.earth_moon_x + scrollX - mouseX) < 24 && Math.abs(NorthstarPlanets.earth_moon_y + scrollY - mouseY) < 24) && player_dim == ClientLevel.OVERWORLD  && player_dim != NorthstarDimensions.MOON_DIM_KEY) {
            List<Component> list = Lists.newArrayList();
            RenderSystem.colorMask(true, true, true, true);
            list.add((Component.translatable("planets.moon.name").withStyle(ChatFormatting.AQUA)));
            list.add((Component.translatable("planets.moon.type").withStyle(ChatFormatting.GRAY)));
            list.add((Component.translatable("planets.moon.grav").withStyle(ChatFormatting.GRAY)));
            list.add((Component.translatable("planets.moon.temp").withStyle(ChatFormatting.GRAY)));
            list.add((Component.translatable("planets.moon.atmosphere").withStyle(ChatFormatting.GRAY)));
            list.add((Component.literal("X:  " + String.valueOf((int)NorthstarPlanets.earth_moon_x)).withStyle(ChatFormatting.WHITE)));
            list.add((Component.literal("Y:  " + String.valueOf((int)NorthstarPlanets.earth_moon_y)).withStyle(ChatFormatting.WHITE)));
            
            this.renderComponentTooltip(pPoseStack, list, mouseX, mouseY);
        }else
        if ((Math.abs(NorthstarPlanets.ceres_x + scrollX + 6 - mouseX) < 6 && Math.abs(NorthstarPlanets.ceres_y + scrollY + 6 - mouseY) < 6)) {
            List<Component> list = Lists.newArrayList();
            RenderSystem.colorMask(true, true, true, true);
            list.add((Component.translatable("planets.ceres.name").withStyle(ChatFormatting.AQUA)));
            list.add((Component.translatable("planets.ceres.type").withStyle(ChatFormatting.GRAY)));
            list.add((Component.translatable("planets.ceres.grav").withStyle(ChatFormatting.GRAY)));
            list.add((Component.translatable("planets.ceres.temp").withStyle(ChatFormatting.GRAY)));
            list.add((Component.translatable("planets.ceres.atmosphere").withStyle(ChatFormatting.GRAY)));
            list.add((Component.literal("X:  " + String.valueOf((int)NorthstarPlanets.ceres_x)).withStyle(ChatFormatting.WHITE)));
            list.add((Component.literal("Y:  " + String.valueOf((int)NorthstarPlanets.ceres_y)).withStyle(ChatFormatting.WHITE)));
                
            this.renderComponentTooltip(pPoseStack, list, mouseX, mouseY);
        }else
        if (Math.abs((NorthstarPlanets.jupiter_x) + scrollX + 12 - mouseX) < 12 && Math.abs((NorthstarPlanets.jupiter_y) + scrollY + 12 - mouseY)< 12) {
        	List<Component> list = Lists.newArrayList();
            RenderSystem.colorMask(true, true, true, true);
            list.add((Component.translatable("planets.jupiter.name").withStyle(ChatFormatting.AQUA)));
            list.add((Component.translatable("planets.jupiter.type").withStyle(ChatFormatting.GRAY)));
            list.add((Component.translatable("planets.jupiter.grav").withStyle(ChatFormatting.GRAY)));
            list.add((Component.translatable("planets.jupiter.temp").withStyle(ChatFormatting.GRAY)));
            list.add((Component.translatable("planets.jupiter.atmosphere").withStyle(ChatFormatting.GRAY)));
            list.add((Component.literal("X:  " + String.valueOf((int)NorthstarPlanets.jupiter_x)).withStyle(ChatFormatting.WHITE)));
            list.add((Component.literal("Y:  " + String.valueOf((int)NorthstarPlanets.jupiter_x)).withStyle(ChatFormatting.WHITE)));
                
            this.renderComponentTooltip(pPoseStack, list, mouseX, mouseY);
        }else
	    if (Math.abs((NorthstarPlanets.saturn_x) + scrollX + 8 - mouseX) < 8 && Math.abs((NorthstarPlanets.saturn_y) + scrollY + 8 - mouseY)< 8) {
	        List<Component> list = Lists.newArrayList();
	        RenderSystem.colorMask(true, true, true, true);
	        list.add((Component.translatable("planets.saturn.name").withStyle(ChatFormatting.AQUA)));
	        list.add((Component.translatable("planets.saturn.type").withStyle(ChatFormatting.GRAY)));
	        list.add((Component.translatable("planets.saturn.grav").withStyle(ChatFormatting.GRAY)));
	        list.add((Component.translatable("planets.saturn.temp").withStyle(ChatFormatting.GRAY)));
	        list.add((Component.translatable("planets.saturn.atmosphere").withStyle(ChatFormatting.GRAY)));
	        list.add((Component.literal("X:  " + String.valueOf((int)NorthstarPlanets.saturn_x)).withStyle(ChatFormatting.WHITE)));
	        list.add((Component.literal("Y:  " + String.valueOf((int)NorthstarPlanets.saturn_y)).withStyle(ChatFormatting.WHITE)));
	                    
	        this.renderComponentTooltip(pPoseStack, list, mouseX, mouseY);
	    }
	    else
		if (Math.abs((NorthstarPlanets.uranus_x) + scrollX + 8 - mouseX) < 8 && Math.abs((NorthstarPlanets.uranus_y) + scrollY + 8 - mouseY)< 8) {
		    List<Component> list = Lists.newArrayList();
		    RenderSystem.colorMask(true, true, true, true);
		    list.add((Component.translatable("planets.uranus.name").withStyle(ChatFormatting.AQUA)));
		    list.add((Component.translatable("planets.uranus.type").withStyle(ChatFormatting.GRAY)));
		    list.add((Component.translatable("planets.uranus.grav").withStyle(ChatFormatting.GRAY)));
		    list.add((Component.translatable("planets.uranus.temp").withStyle(ChatFormatting.GRAY)));
		    list.add((Component.translatable("planets.uranus.atmosphere").withStyle(ChatFormatting.GRAY)));
		    list.add((Component.literal("X:  " + String.valueOf((int)NorthstarPlanets.uranus_x)).withStyle(ChatFormatting.WHITE)));
		    list.add((Component.literal("Y:  " + String.valueOf((int)NorthstarPlanets.uranus_y)).withStyle(ChatFormatting.WHITE)));
		                    
		    this.renderComponentTooltip(pPoseStack, list, mouseX, mouseY);
		}else
	    if (Math.abs((NorthstarPlanets.neptune_x) + scrollX + 8 - mouseX) < 8 && Math.abs((NorthstarPlanets.neptune_y) + scrollY + 8 - mouseY)< 8) {
	        List<Component> list = Lists.newArrayList();
	        RenderSystem.colorMask(true, true, true, true);
	        list.add((Component.translatable("planets.neptune.name").withStyle(ChatFormatting.AQUA)));
	        list.add((Component.translatable("planets.neptune.type").withStyle(ChatFormatting.GRAY)));
	        list.add((Component.translatable("planets.neptune.grav").withStyle(ChatFormatting.GRAY)));
	        list.add((Component.translatable("planets.neptune.temp").withStyle(ChatFormatting.GRAY)));
	        list.add((Component.translatable("planets.neptune.atmosphere").withStyle(ChatFormatting.GRAY)));
	        list.add((Component.literal("X:  " + String.valueOf((int)NorthstarPlanets.neptune_x)).withStyle(ChatFormatting.WHITE)));
	        list.add((Component.literal("Y:  " + String.valueOf((int)NorthstarPlanets.neptune_y)).withStyle(ChatFormatting.WHITE)));
	                    
	        this.renderComponentTooltip(pPoseStack, list, mouseX, mouseY);
	    }else
		if (Math.abs((NorthstarPlanets.pluto_x) + scrollX + 6 - mouseX) < 6 && Math.abs((NorthstarPlanets.pluto_y) + scrollY + 6 - mouseY)< 6) {
		    List<Component> list = Lists.newArrayList();
		    RenderSystem.colorMask(true, true, true, true);
		    list.add((Component.translatable("planets.pluto.name").withStyle(ChatFormatting.AQUA)));
		    list.add((Component.translatable("planets.pluto.type").withStyle(ChatFormatting.GRAY)));
		    list.add((Component.translatable("planets.pluto.grav").withStyle(ChatFormatting.GRAY)));
		    list.add((Component.translatable("planets.pluto.temp").withStyle(ChatFormatting.GRAY)));
		    list.add((Component.translatable("planets.pluto.atmosphere").withStyle(ChatFormatting.GRAY)));
		    list.add((Component.literal("X:  " + String.valueOf((int)NorthstarPlanets.pluto_x)).withStyle(ChatFormatting.WHITE)));
		    list.add((Component.literal("Y:  " + String.valueOf((int)NorthstarPlanets.pluto_y)).withStyle(ChatFormatting.WHITE)));
		                    
		    this.renderComponentTooltip(pPoseStack, list, mouseX, mouseY);
		}else
		if (Math.abs((NorthstarPlanets.eris_x) + scrollX + 6 - mouseX) < 6 && Math.abs((NorthstarPlanets.eris_y) + scrollY + 6 - mouseY)< 6) {
		    List<Component> list = Lists.newArrayList();
		    RenderSystem.colorMask(true, true, true, true);
		    list.add((Component.translatable("planets.eris.name").withStyle(ChatFormatting.AQUA)));
		    list.add((Component.translatable("planets.eris.type").withStyle(ChatFormatting.GRAY)));
		    list.add((Component.translatable("planets.eris.grav").withStyle(ChatFormatting.GRAY)));
		    list.add((Component.translatable("planets.eris.temp").withStyle(ChatFormatting.GRAY)));
		    list.add((Component.translatable("planets.eris.atmosphere").withStyle(ChatFormatting.GRAY)));
		    list.add((Component.literal("X:  " + String.valueOf((int)NorthstarPlanets.eris_x)).withStyle(ChatFormatting.WHITE)));
		    list.add((Component.literal("Y:  " + String.valueOf((int)NorthstarPlanets.eris_y)).withStyle(ChatFormatting.WHITE)));
			                    
		    this.renderComponentTooltip(pPoseStack, list, mouseX, mouseY);
		}
    }
    
  

	public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        if (pButton != 0) {
           this.isScrolling = false;
           return false;
        } else {
           if (!this.isScrolling) {
              this.isScrolling = true;
           }
           scroll(pDragX, pDragY);
           return true;
        }
           
     }
    
    
    public boolean mouseClicked(double mouseX, double mouseY, int pButton) {
        if (pButton == 0 || pButton == 1) {
            if (Math.abs(NorthstarPlanets.mercury_x + scrollX + 7 - mouseX) < 8 && Math.abs(NorthstarPlanets.mercury_y + scrollY + 7 - mouseY) < 8 && Minecraft.getInstance().level.dimension() != NorthstarDimensions.MERCURY_DIM_KEY) {
            SelectedPlanet = "mercury";}      
            if (Math.abs((NorthstarPlanets.venus_x) + scrollX + 8 - mouseX) < 8 && Math.abs((NorthstarPlanets.venus_y) + scrollY + 8 - mouseY)< 8 && Minecraft.getInstance().level.dimension() != NorthstarDimensions.VENUS_DIM_KEY) {
            SelectedPlanet = "venus";}          
            if (Math.abs(NorthstarPlanets.earth_x + scrollX + 8 - mouseX) < 8 && Math.abs(NorthstarPlanets.earth_y + scrollY + 8 - mouseY) < 8 && Minecraft.getInstance().level.dimension() != Level.OVERWORLD) {
            SelectedPlanet = "earth";}
            if ((Math.abs(NorthstarPlanets.earth_moon_x + scrollX - mouseX) < 18 && Math.abs(NorthstarPlanets.earth_moon_y + scrollY - mouseY) < 18) && Minecraft.getInstance().level.dimension() == Level.OVERWORLD  && Minecraft.getInstance().level.dimension() != NorthstarDimensions.MOON_DIM_KEY ) {
            SelectedPlanet = "earth_moon";}
            if (Math.abs(NorthstarPlanets.moon_x + scrollX + 8 - mouseX) < 8 && Math.abs(NorthstarPlanets.moon_y + scrollY + 8 - mouseY) < 8 && Minecraft.getInstance().level.dimension() != Level.OVERWORLD && Minecraft.getInstance().level.dimension() != NorthstarDimensions.MOON_DIM_KEY  ) {
            SelectedPlanet = "moon";}
            if (Math.abs(NorthstarPlanets.mars_x + scrollX + 8 - mouseX) < 8 && Math.abs(NorthstarPlanets.mars_y + scrollY + 8 - mouseY) < 8 && Minecraft.getInstance().level.dimension() != NorthstarDimensions.MARS_DIM_KEY) {
            SelectedPlanet = "mars";}
            if (Math.abs(NorthstarPlanets.ceres_x + scrollX + 8 - mouseX) < 8 && Math.abs(NorthstarPlanets.ceres_y + scrollY + 8 - mouseY) < 8) {
            SelectedPlanet = "ceres";}
            if (Math.abs(NorthstarPlanets.jupiter_x + scrollX + 8 - mouseX) < 8 && Math.abs(NorthstarPlanets.jupiter_y + scrollY + 8 - mouseY) < 8) {
            SelectedPlanet = "jupiter";}
            if (Math.abs((NorthstarPlanets.saturn_x) + scrollX + 8 - mouseX) < 8 && Math.abs((NorthstarPlanets.saturn_y) + scrollY + 8 - mouseY)< 8) {
            SelectedPlanet = "saturn";}            
            if (Math.abs(NorthstarPlanets.uranus_x + scrollX + 7 - mouseX) < 8 && Math.abs(NorthstarPlanets.uranus_y + scrollY + 7 - mouseY) < 8) {
            SelectedPlanet = "uranus";}         
            if (Math.abs(NorthstarPlanets.neptune_x + scrollX + 7 - mouseX) < 8 && Math.abs(NorthstarPlanets.neptune_y + scrollY + 7 - mouseY) < 8) {
            SelectedPlanet = "neptune";}       
            if (Math.abs(NorthstarPlanets.pluto_x + scrollX + 7 - mouseX) < 8 && Math.abs(NorthstarPlanets.pluto_y + scrollY + 7 - mouseY) < 8) {
            SelectedPlanet = "pluto";}       
            if (Math.abs(NorthstarPlanets.eris_x + scrollX + 7 - mouseX) < 8 && Math.abs(NorthstarPlanets.eris_y + scrollY + 7 - mouseY) < 8) {
            SelectedPlanet = "eris";}       
         }
        if (pButton == 1) {
        }

        return super.mouseClicked(mouseX, mouseY, pButton);
     }
    
    
    
    public void scroll(double pDragX, double pDragY) {
            this.scrollX = Mth.clamp(this.scrollX + pDragX, 0, 900);
            this.scrollY = Mth.clamp(this.scrollY + pDragY, 0, 900);
     }
    
    
    
    public ResourceLocation getPlanetSprite(String planet) {
    	switch (planet) {
		case "mercury": {return MERCURY;}
		case "venus": {return VENUS;}
		case "earth": {return EARTH;}
		case "earth_moon": {return MOON;}
		case "moon": {return MOON;}
		case "mars": {return MARS;}
		case "ceres": {return CERES;}
		case "jupiter": {return JUPITER;}
		case "saturn": {return SATURN;}
		case "uranus": {return URANUS;}
		case "neptune": {return NEPTUNE;}
		case "pluto": {return PLUTO;}
		case "eris": {return ERIS;}


		default:return null;}
    	
    }
    
    public Component getPlanetName(String planet) {
    	return Component.translatable("planets." + planet + ".name");
    }
    
    
    
    
}
