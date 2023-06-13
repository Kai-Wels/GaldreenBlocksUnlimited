package de.ewu2000.galdreenblocksunlimited;

import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.ArrayList;

public class CustomBlockCompound implements Serializable {

    private ArrayList<CustomBlockCycle> blockCyclesList;
    private ItemStack itemToUse;


    private boolean updatedByOtherBlocks;


    public CustomBlockCompound(ArrayList<CustomBlockCycle> blockCycles, ItemStack itemToUse){
        this.blockCyclesList = blockCycles;
        this.itemToUse = itemToUse;
    }

    public CustomBlockCompound(ItemStack itemToUse){
        this.itemToUse = itemToUse;
        this.blockCyclesList = new ArrayList<>();
    }

    public CustomBlockCompound(){
        this.itemToUse = null;
        this.blockCyclesList = new ArrayList<>();
    }

    public ArrayList<CustomBlockCycle> getBlockCyclesList() {
        return blockCyclesList;
    }

    public void setBlockCyclesList(ArrayList<CustomBlockCycle> blockCyclesList) {
        this.blockCyclesList = blockCyclesList;
    }

    public ItemStack getItemToUse() {
        return itemToUse;
    }

    public void setItemToUse(ItemStack itemToUse) {
        this.itemToUse = itemToUse;
    }

    public boolean isUpdatedByOtherBlocks() {
        return updatedByOtherBlocks;
    }

    public void setUpdatedByOtherBlocks(boolean updatedByOtherBlocks) {
        this.updatedByOtherBlocks = updatedByOtherBlocks;
    }
}
