package de.ewu2000.galdreenblocks;

import org.bukkit.block.BlockFace;

public class PistonRotationStateTransition {
    private BlockFace fromBlockFace;
    private BlockFace toBlockFace;

    PistonRotationStateTransition(BlockFace fromBlockFace, BlockFace toBlockFace){
        this.fromBlockFace = fromBlockFace;
        this.toBlockFace = toBlockFace;
    }

    public BlockFace getFromBlockFace() {
        return fromBlockFace;
    }

    public BlockFace getToBlockFace() {
        return toBlockFace;
    }

    public String toString(){
        return "("+fromBlockFace+","+toBlockFace +")";
    }
}
