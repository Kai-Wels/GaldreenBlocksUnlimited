package de.ewu2000.galdreenblocksunlimited;

import java.util.ArrayList;

public class CustomBlockCycle {
    private ArrayList<CustomBlock> customBlocks;

    public CustomBlockCycle(ArrayList<CustomBlock> blockCycle){
        this.customBlocks = blockCycle;
    }

    public CustomBlockCycle(){
        this.customBlocks = new ArrayList<>();
    }

    public ArrayList<CustomBlock> getCustomBlocks() {
        return customBlocks;
    }

    public void setCustomBlocks(ArrayList<CustomBlock> customBlocks) {
        this.customBlocks = customBlocks;
    }
}
