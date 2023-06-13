package events;

import de.ewu2000.galdreenblocksunlimited.CustomBlock;
import de.ewu2000.galdreenblocksunlimited.CustomBlockCompound;
import de.ewu2000.galdreenblocksunlimited.CustomBlockCycle;
import de.ewu2000.galdreenblocksunlimited.GaldreenBlocksUnlimited;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;


public class BlockDestroyEvent implements Listener {



    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDestroy(com.destroystokyo.paper.event.block.BlockDestroyEvent event){



        for(CustomBlockCompound cbcmp : GaldreenBlocksUnlimited.allCustomBlockCompounds){
            for (CustomBlockCycle cbc : cbcmp.getBlockCyclesList()){
                for (CustomBlock cb : cbc.getCustomBlocks()){
                    if(event.getBlock().getBlockData().equals(cb.getGoalData())){
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }

        for( ItemStack item: BlockCanBuildEvent.alwaysPlaceable){
            if (item.getType().equals(event.getBlock().getType())) {
                event.setCancelled(true);
                return;
            }
        }



    }

}
