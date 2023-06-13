package events;

import com.sk89q.worldguard.bukkit.listener.debounce.BlockPistonExtendKey;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ReplaceTask  extends BukkitRunnable {

    public static BlockFace[] cartesian = {BlockFace.EAST,BlockFace.WEST,BlockFace.SOUTH,BlockFace.NORTH, BlockFace.UP,BlockFace.DOWN};
    private final JavaPlugin plugin;
    BlockData[] storedNeighbourData;
    World world;
    Location loc;

    public ReplaceTask(JavaPlugin plugin, BlockData[] storedNeighbourData, World world, Location loc){
        this.plugin = plugin;
        this.storedNeighbourData = storedNeighbourData;
        this.world = world;
        this.loc = loc;

    }
    @Override
    public void run() {
        int i = 0;
        for(BlockFace bf : cartesian) {
            if(storedNeighbourData[i] != null){
                world.getBlockAt(loc).getRelative(bf).setBlockData(storedNeighbourData[i],false);
            }
            i++;
        }
    }
}
