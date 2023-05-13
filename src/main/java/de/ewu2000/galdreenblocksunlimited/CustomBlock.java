package de.ewu2000.galdreenblocksunlimited;

import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class CustomBlock {
    private BlockData goalData;
    private ArrayList<BlockData> placeData;

    public CustomBlock(BlockData goalData, ArrayList<BlockData> placeData){
        this.goalData = goalData;
        this.placeData = placeData;
    }

    public CustomBlock(BlockData goalData){
        this.goalData = goalData;
        this.placeData = new ArrayList<>();
    }

    public BlockData getGoalData() {
        return goalData;
    }

    public void setGoalData(BlockData goalData) {
        this.goalData = goalData;
    }

    public ArrayList<BlockData> getPlaceData() {
        return placeData;
    }

    public void setPlaceData(ArrayList<BlockData> placeData) {
        this.placeData = placeData;
    }
}
