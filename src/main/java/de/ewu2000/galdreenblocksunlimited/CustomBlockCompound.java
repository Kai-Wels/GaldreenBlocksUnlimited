package de.ewu2000.galdreenblocksunlimited;

import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.ArrayList;

public class CustomBlockCompound implements Serializable {

    private ItemStack itemToUse;

    private boolean updatedByOtherBlocks;


    public CustomBlockCompound(ItemStack itemToUse){
        this.itemToUse = itemToUse;
    }

    public CustomBlockCompound(){
        this.itemToUse = null;
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
