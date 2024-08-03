package com.lightning.northstar.block.tech.telescope;

import java.util.List;

import javax.annotation.Nullable;

import com.lightning.northstar.block.entity.NorthstarBlockEntityTypes;
import com.lightning.northstar.item.NorthstarItems;
import com.lightning.northstar.world.dimension.NorthstarPlanets;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class TelescopeBlockEntity extends SmartBlockEntity implements MenuProvider {

	protected final ContainerData data;
    public String SelectedPlanet = null;
	
	public TelescopeBlockEntity(BlockPos pPos, BlockState pBlockState) {
		super(NorthstarBlockEntityTypes.TELESCOPE.get(), pPos, pBlockState);
        this.data = new ContainerData() {

			@Override
			public int get(int pIndex) {
				return 0;
			}

			@Override
			public void set(int pIndex, int pValue) {
				return;
			}

			@Override
			public int getCount() {
				return 0;
			}};
		}
	

	
    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new TelescopeMenu(id, inventory, this, this.data);
    }
    
    public void print(String name, ServerPlayer player) {
    	SelectedPlanet = name;
    	boolean flag = false;
    	int paperslot = 0;
    	Inventory inv = player.getInventory();
    	if(name == null) {return;}
    	if (inv != null) {
    	for(int p = 0; p < 36; p++) {
    		ItemStack items = inv.getItem(p);
    		Item item = items.getItem();
    		if (item == Items.PAPER) {
    			flag = true;
    			paperslot = p;}}
    	System.out.println(flag);
    	if (flag == false) {return;}
    	ItemStack paper = inv.getItem(paperslot);
    	paper.setCount(paper.getCount() - 1);
    	inv.player.level().playSound((Player) null, inv.player.blockPosition(), SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
        ItemStack itemstack = new ItemStack(NorthstarItems.ASTRONOMICAL_READING.get(), 1);
        itemstack.setHoverName(Component.translatable("item.northstar.reading_" + name).setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE).withItalic(false)));
        CompoundTag tag = itemstack.getOrCreateTagElement("Planet");
        tag.putString("name", name);
        int x = (int) NorthstarPlanets.getPlanetX(name);
        int y = (int) NorthstarPlanets.getPlanetY(name);
        ListTag poob = new ListTag();
        poob.add(StringTag.valueOf(Component.Serializer.toJson(Component.literal( "X: " + x).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY).withItalic(false))).toString())); 
        poob.add(StringTag.valueOf(Component.Serializer.toJson(Component.literal( "Y: " + y).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY).withItalic(false))).toString())); 
        itemstack.getOrCreateTagElement("display").put("Lore", poob);
        CompoundTag tag1 = itemstack.getOrCreateTagElement("planetX");
        tag1.putInt("value", (int) x);
        CompoundTag tag2 = itemstack.getOrCreateTagElement("planetY");
        tag2.putInt("value", (int) y);
        level.addFreshEntity(new ItemEntity(level, player.getX(), player.getY(), player.getZ(), itemstack));}
    	System.out.println(inv);
    }
    
    public static void tick(Level level, BlockPos pos, BlockState state, TelescopeBlockEntity pEntity) {
        if(level.isClientSide()) {
            return;
        }
       }
    

	@Override
	public Component getDisplayName() {
        return Component.literal("Telescope");
	}



	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
		
	}
}
