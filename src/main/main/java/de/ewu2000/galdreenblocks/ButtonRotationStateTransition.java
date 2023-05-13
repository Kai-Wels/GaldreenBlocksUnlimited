package de.ewu2000.galdreenblocks;

import org.bukkit.block.BlockFace;
import org.bukkit.block.data.FaceAttachable;

public class ButtonRotationStateTransition {
    private BlockFace fromBlockFace;
    private BlockFace toBlockFace;

    private FaceAttachable.AttachedFace fromAttachedFace;
    private FaceAttachable.AttachedFace toAttachedFace;

    ButtonRotationStateTransition(BlockFace fromBlockFace, BlockFace toBlockFace, FaceAttachable.AttachedFace fromAttachedFace, FaceAttachable.AttachedFace toAttachedFace){
        this.fromBlockFace = fromBlockFace;
        this.toBlockFace = toBlockFace;
        this.fromAttachedFace = fromAttachedFace;
        this.toAttachedFace = toAttachedFace;
    }

    public BlockFace getFromBlockFace() {
        return fromBlockFace;
    }

    public BlockFace getToBlockFace() {
        return toBlockFace;
    }

    public FaceAttachable.AttachedFace getFromAttachedFace() {
        return fromAttachedFace;
    }

    public FaceAttachable.AttachedFace getToAttachedFace() {
        return toAttachedFace;
    }

    public String toString(){
        return "("+fromBlockFace+","+toBlockFace +"," +fromAttachedFace + "," + toAttachedFace +")";
    }
}
