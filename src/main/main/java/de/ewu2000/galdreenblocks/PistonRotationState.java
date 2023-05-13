package de.ewu2000.galdreenblocks;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.TechnicalPiston;

import java.util.LinkedList;
import java.util.List;

public class PistonRotationState {
    private Material matToPlace;
    private Material matOfModel;
    private String name;
    private TechnicalPiston.Type type;
    private boolean _short;
    private List<PistonRotationStateTransition> transitions = new LinkedList<>();

    public static List<PistonRotationState> allPistonRotationStates = new LinkedList<>();


    public Material getMatToPlace() {
        return matToPlace;
    }

    public Material getMatOfModel() {
        return matOfModel;
    }

    public List<PistonRotationStateTransition> getTransitions() {
        return transitions;
    }

    public String getName() {
        return name;
    }

    public TechnicalPiston.Type getType() {
        return type;
    }

    public boolean getShort() { return _short; }


    PistonRotationState(Material matToPlace, Material matOfModel, String name, TechnicalPiston.Type type, boolean _short) {
        this.matToPlace = matToPlace;
        this.matOfModel = matOfModel;
        this.name = name;
        this.type = type;
        this._short= _short;

    }

    public static BlockFace negateFace(BlockFace bf){
        switch (bf){
            case NORTH: return BlockFace.SOUTH;
            case UP: return  BlockFace.DOWN;
            case SOUTH: return  BlockFace.NORTH;
            case DOWN: return  BlockFace.UP;
            case EAST: return  BlockFace.WEST;
            case WEST: return BlockFace.EAST;
            default: return BlockFace.UP;
        }
    }

}
