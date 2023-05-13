package de.ewu2000.galdreenblocks;

import org.bukkit.Material;

import java.util.ArrayList;

public class PlateState {
    private String name;
    private Material material;
    private int power;

    public static ArrayList<PlateState> allPlateStates = new ArrayList<>();

    public PlateState(String name, Material material, int power){
        this.material = material;
        this.name = name;
        this.power = power;
    }
    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }

    public int getPower() {
        return power;
    }
}
