package com.lightning.northstar.block.tech.temperature_regulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.lightning.northstar.Northstar;
import com.lightning.northstar.NorthstarPackets;
import com.lightning.northstar.world.TemperatureStuff;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.AbstractSimiScreen;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.gui.widget.Label;
import com.simibubi.create.foundation.gui.widget.ScrollInput;
import com.simibubi.create.foundation.gui.widget.SelectionScrollInput;
import com.simibubi.create.foundation.utility.Components;

import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class TemperatureRegulatorScreen extends AbstractSimiScreen {
    private final ResourceLocation TEMPERATURE_REGULATOR = new ResourceLocation(Northstar.MOD_ID, "textures/gui/temperature_regulator.png");
    protected int imageWidth = 204;
    protected int imageHeight = 166;
    private int sizeX;
    private int sizeY;
    private int sizeZ;
    private int offsetX;
    private int offsetY;
    private int offsetZ;
    private int temp;
	protected final List<Component> envOptions = new ArrayList<Component>();
	
    private boolean envFill;
    private TemperatureRegulatorBlockEntity blockEntity;

	public TemperatureRegulatorScreen(TemperatureRegulatorBlockEntity block) 
	{blockEntity = block;
	sizeX = block.sizeX;
	sizeY = block.sizeY;
	sizeZ = block.sizeZ;
	offsetX = block.offsetX;
	offsetY = block.offsetY;
	offsetZ = block.offsetZ;
	temp = block.temp;}

    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
    	super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    	temp = Mth.clamp(temp, -273, 1000);
    	RenderSystem.disableBlend();
    	this.renderVariables(pPoseStack, pMouseX, pMouseY, pPartialTick);
    	this.renderButtons(pPoseStack, pMouseX, pMouseY, pPartialTick);

//    	this.renderTooltip(pPoseStack, pMouseX, pMouseY);
    }  
    
	@Override
	protected void init() {
		super.init();
        sizeX = blockEntity.sizeX;
        sizeY = blockEntity.sizeY;
        sizeZ = blockEntity.sizeZ;
        offsetX = blockEntity.offsetX;
        offsetY = blockEntity.offsetY;
        offsetZ = blockEntity.offsetZ;
        envFill = blockEntity.envFill;
    	envOptions.add(Component.translatable("northstar.gui.temperature_regulator.box"));
    	envOptions.add(Component.translatable("northstar.gui.temperature_regulator.env_fill"));
    	this.initScrollStuff();
	}
    protected void renderButtons(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
    }
    
    protected void renderVariables(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
        int x = (width - (imageWidth + (imageWidth / 2))) / 2;
        int y = (height - (imageHeight + (imageHeight / 2))) / 2;
//    	this.font.draw(pPoseStack, Component.literal("Temperature: " + blockEntity.temp).withStyle(ChatFormatting.AQUA), x, y, 6944);
  //  	this.font.draw(pPoseStack, Component.literal("Offset: " + blockEntity.offsetX + ", " + blockEntity.offsetY + ", " + blockEntity.offsetZ).withStyle(ChatFormatting.AQUA), x, y + 20, 6944);
   //  	this.font.draw(pPoseStack, Component.literal("Size: " + blockEntity.sizeX + ", " + blockEntity.sizeY + ", " + blockEntity.sizeZ).withStyle(ChatFormatting.AQUA), x, y + 40, 6944);   	
    	this.font.draw(pPoseStack, Component.literal("C°").withStyle(ChatFormatting.WHITE), x + 213, y + 110, 6944);
//    	this.font.draw(pPoseStack, Component.literal("Fill Mode").withStyle(ChatFormatting.GRAY), x + 180, y + 85, 6944);
    }
    
    protected void initScrollStuff() {
        int x = (width - (imageWidth + (imageWidth / 2))) / 2;
        int y = (height - (imageHeight + (imageHeight / 2))) / 2;
        List<String> strings = new ArrayList<>();
        strings.add("X"); strings.add("Y"); strings.add("Z");
        Vector<Label> labels = new Vector<>(3);
        Vector<ScrollInput> inputs = new Vector<>(3);
        	temp = blockEntity.temp;
        	envFill = blockEntity.envFill;
        	
     	   IconButton confirm_change = new IconButton(x + imageWidth + 20, y + 105, AllIcons.I_CONFIRM);
    	   confirm_change.setToolTip(Component.literal("Confirm"));
    	   confirm_change.withCallback(() -> {removed(); onClose();});
    	   addRenderableWidget(confirm_change);
        
			Label labelX = new Label(x + 65 + 20, y + 97, Components.literal(Integer.toString(5))).withShadow();
			ScrollInput inputX = new ScrollInput(x + 56 + 20, y + 97, 18, 18)
				.withRange(1, TemperatureStuff.maxSize)
				.writingTo(labelX)
				.titled(Component.literal("Box Width"))
				.calling(state -> {
					blockEntity.sizeX = state;
					sizeX = state;
			    	while(blockEntity.offsetX > blockEntity.sizeX / 2) {blockEntity.offsetX--;}
			    	while(blockEntity.offsetY > blockEntity.sizeY / 2) {blockEntity.offsetY--;}
			    	while(blockEntity.offsetZ > blockEntity.sizeZ / 2) {blockEntity.offsetZ--;}
					labelX.x = x + 65 + 20 - font.width(labelX.text) / 2;});
			inputX.setState(blockEntity.sizeX);
			inputX.onChanged();
			inputs.add(inputX);
			labels.add(labelX);
			
			Label labelY = new Label(x + 65 + 44, y + 97, Components.immutableEmpty()).withShadow();
			ScrollInput inputY = new ScrollInput(x + 56 + 43, y + 97, 18, 18)
				.withRange(1, TemperatureStuff.maxSize)
				.writingTo(labelY)
				.titled(Component.literal("Box Height"))
				.calling(state -> {
					blockEntity.sizeY = state;
					sizeY = state;
			    	while(blockEntity.offsetX > blockEntity.sizeX / 2) {blockEntity.offsetX--;}
			    	while(blockEntity.offsetY > blockEntity.sizeY / 2) {blockEntity.offsetY--;}
			    	while(blockEntity.offsetZ > blockEntity.sizeZ / 2) {blockEntity.offsetZ--;}
					labelY.x = x + 65 + 43 - font.width(labelY.text) / 2;});
			inputY.setState(blockEntity.sizeY);
			inputY.onChanged();
			inputs.add(inputY);
			labels.add(labelY);
			
			Label labelZ = new Label(x + 65 + 67, y + 97, Components.immutableEmpty()).withShadow();
			ScrollInput inputZ = new ScrollInput(x + 56 + 67, y + 97, 18, 18)
				.withRange(1, TemperatureStuff.maxSize)
				.writingTo(labelZ)
				.titled(Component.literal("Box Length"))
				.calling(state -> {
					blockEntity.sizeZ = state;
					sizeZ = state;
			    	while(blockEntity.offsetX > blockEntity.sizeX / 2) {blockEntity.offsetX--;}
			    	while(blockEntity.offsetY > blockEntity.sizeY / 2) {blockEntity.offsetY--;}
			    	while(blockEntity.offsetZ > blockEntity.sizeZ / 2) {blockEntity.offsetZ--;}
					labelZ.x = x + 65 + 67 - font.width(labelZ.text) / 2;});
			inputZ.setState(blockEntity.sizeZ);
			inputZ.onChanged();
			inputs.add(inputZ);
			labels.add(labelZ);
		
		for(Label labels2 : labels) {addRenderableWidget(labels2);}
		for(ScrollInput inputs2 : inputs) {addRenderableWidget(inputs2);}
		
		Vector<Label> offsetlabels = new Vector<>(3);
		Vector<ScrollInput> offsetinputs = new Vector<>(3);
        
		Label offsetlabelX = new Label(x + 65 + 20, y + 123, Components.immutableEmpty()).withShadow();
		ScrollInput offsetinputX = new ScrollInput(x + 56 + 20, y + 123, 18, 18)
			.withRange(-blockEntity.sizeX / 2, blockEntity.sizeX / 2)
			.writingTo(offsetlabelX)
			.titled(Component.literal("Box X Offset"))
			.calling(state -> {
				blockEntity.offsetX = state;
				offsetX = state;
		    	while(blockEntity.offsetX > blockEntity.sizeX / 2) {blockEntity.offsetX--;}
		    	while(blockEntity.offsetY > blockEntity.sizeY / 2) {blockEntity.offsetY--;}
		    	while(blockEntity.offsetZ > blockEntity.sizeZ / 2) {blockEntity.offsetZ--;}
				offsetlabelX.x = x + 65 + 20 - font.width(offsetlabelX.text) / 2;});
		offsetinputX.setState(blockEntity.offsetX);
		offsetinputX.onChanged();
		offsetlabels.add(offsetlabelX);
		offsetinputs.add(offsetinputX);
		
		Label offsetlabelY = new Label(x + 65 + 44, y + 123, Components.immutableEmpty()).withShadow();
		ScrollInput offsetinputY = new ScrollInput(x + 56 + 44, y + 123, 18, 18)
			.withRange(-blockEntity.sizeY / 2, blockEntity.sizeX / 2)
			.writingTo(offsetlabelY)
			.titled(Component.literal("Box Y Offset"))
			.calling(state -> {
				blockEntity.offsetY = state;
				offsetY = state;
		    	while(blockEntity.offsetX > blockEntity.sizeX / 2) {blockEntity.offsetX--;}
		    	while(blockEntity.offsetY > blockEntity.sizeY / 2) {blockEntity.offsetY--;}
		    	while(blockEntity.offsetZ > blockEntity.sizeZ / 2) {blockEntity.offsetZ--;}
				offsetlabelY.x = x + 65 + 44 - font.width(offsetlabelY.text) / 2;});
		offsetinputY.setState(blockEntity.offsetY);
		offsetinputY.onChanged();
		offsetlabels.add(offsetlabelY);
		offsetinputs.add(offsetinputY);
		
		Label offsetlabelZ = new Label(x + 65 + 68, y + 123, Components.immutableEmpty()).withShadow();
		ScrollInput offsetinputZ = new ScrollInput(x + 56 + 68, y + 123, 18, 18)
			.withRange(-blockEntity.sizeZ / 2, blockEntity.sizeZ / 2)
			.writingTo(offsetlabelZ)
			.titled(Component.literal("Box Z Offset"))
			.calling(state -> {
				blockEntity.offsetZ = state;
				offsetZ = state;
		    	while(blockEntity.offsetX > blockEntity.sizeX / 2) {blockEntity.offsetX--;}
		    	while(blockEntity.offsetY > blockEntity.sizeY / 2) {blockEntity.offsetY--;}
		    	while(blockEntity.offsetZ > blockEntity.sizeZ / 2) {blockEntity.offsetZ--;}
				offsetlabelZ.x = x + 65 + 68 - font.width(offsetlabelZ.text) / 2;});
		offsetinputZ.setState(blockEntity.offsetZ);
		offsetinputZ.onChanged();
		offsetlabels.add(offsetlabelZ);
		offsetinputs.add(offsetinputZ);
		
		Label templabel = new Label(x + 200, y + 110, Components.immutableEmpty()).withShadow();
		ScrollInput tempinput = new ScrollInput(x + 186, y + 110, 24, 18)
			.withRange(-273, 1000)
			.writingTo(templabel)
			.titled(Component.literal("Temperature"))
			.calling(state -> {
				blockEntity.temp = state;
				temp = state;
				templabel.x = x + 200  - font.width(templabel.text) / 2;});
		tempinput.setState(blockEntity.temp);
		tempinput.onChanged();
		offsetlabels.add(templabel);
		offsetinputs.add(tempinput);
		
		Label envLabel = new Label(x + 200, y + 90, Components.immutableEmpty()).withShadow();
		ScrollInput envInput = new SelectionScrollInput(x + 186, y + 90, 24, 18)
			.forOptions(envOptions)
			.writingTo(envLabel)
			.titled(Component.literal("Fill Mode"))
			.calling(state -> {
				blockEntity.envFill = state == 1;
				envFill = state == 1;
				envLabel.x = x + 200  - font.width(envLabel.text) / 2;});
		envInput.setState(blockEntity.envFill ? 1 : 0);
		envInput.onChanged();
		offsetlabels.add(envLabel);
		offsetinputs.add(envInput);

		
		for(Label labels2 : offsetlabels) {addRenderableWidget(labels2);}
		for(ScrollInput inputs2 : offsetinputs) {addRenderableWidget(inputs2);}
		
		
		
		
    }
    
	@Override
	public void removed() {
		TemperatureRegulatorEditPacket packet = getConfigurationPacket();
		NorthstarPackets.getChannel().sendToServer(packet);
	}
	
	protected TemperatureRegulatorEditPacket getConfigurationPacket() {
		return new TemperatureRegulatorEditPacket(this.blockEntity.getBlockPos(), offsetX, offsetY, offsetZ, sizeX, sizeY, sizeZ, temp, envFill);
	}

	@Override
	protected void renderWindow(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
    	RenderSystem.setShader(GameRenderer::getPositionTexShader);
    	RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    	RenderSystem.setShaderTexture(0, this.TEMPERATURE_REGULATOR);
    	int i = (this.width - this.imageWidth) / 2;
    	int j = (this.height - this.imageHeight) / 2;
    	this.blit(ms, i, j, 0, 0, this.imageWidth, this.imageHeight);
	}
    
    
    

}
