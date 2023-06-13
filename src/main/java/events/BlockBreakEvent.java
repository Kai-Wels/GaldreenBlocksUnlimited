package events;

import de.ewu2000.galdreenblocksunlimited.CustomBlock;
import de.ewu2000.galdreenblocksunlimited.CustomBlockCompound;
import de.ewu2000.galdreenblocksunlimited.CustomBlockCycle;
import de.ewu2000.galdreenblocksunlimited.GaldreenBlocksUnlimited;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class BlockBreakEvent implements Listener {

    public JavaPlugin plugin;

    public BlockBreakEvent(JavaPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBreak(org.bukkit.event.block.BlockBreakEvent event){

        //store block data of neighbours, if they are GaldreenBlocks and replace with barrier to prevent block updates
        BlockData[] storedNeighbours = new BlockData[6];
        boolean storedBlock = false;
        int i = 0;
        for(BlockFace bf : ReplaceTask.cartesian){
            BlockData neighbourBD =  event.getBlock().getRelative(bf).getBlockData();
            secondloop:
            for(CustomBlockCompound cbcmp : GaldreenBlocksUnlimited.allCustomBlockCompounds){
                for (CustomBlockCycle cbc : cbcmp.getBlockCyclesList()){
                    for (CustomBlock cb : cbc.getCustomBlocks()){
                        if(neighbourBD.equals(cb.getGoalData()) && cbcmp.isUpdatedByOtherBlocks()){
                            storedNeighbours[i] = neighbourBD;
                            event.getBlock().getRelative(bf).setType(Material.BARRIER,false);
                            storedBlock = true;
                            break secondloop;
                        }
                    }
                }
            }
            i++;
        }

        //break behaviour
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE){
            firstloop:
            for(CustomBlockCompound cbcmp : GaldreenBlocksUnlimited.allCustomBlockCompounds){
                for (CustomBlockCycle cbc : cbcmp.getBlockCyclesList()){
                    for (CustomBlock cb : cbc.getCustomBlocks()){
                        if(event.getBlock().getBlockData().equals(cb.getGoalData())){
                            event.setCancelled(true);
                            event.getBlock().setType(Material.AIR,false);
                            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(),cbcmp.getItemToUse());
                            break firstloop;
                        }
                    }
                }
            }
        }

        //schedule task to replace blocks again
        if(storedBlock){
            BukkitTask task = new ReplaceTask(plugin,storedNeighbours,event.getBlock().getWorld(),event.getBlock().getLocation()).runTaskLater(plugin, 1);
        }
    }
}
