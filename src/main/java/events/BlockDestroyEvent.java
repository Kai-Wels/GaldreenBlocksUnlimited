package events;

import de.ewu2000.galdreenblocksunlimited.CustomBlock;
import de.ewu2000.galdreenblocksunlimited.CustomBlockCompound;
import de.ewu2000.galdreenblocksunlimited.CustomBlockCycle;
import de.ewu2000.galdreenblocksunlimited.GaldreenBlocksUnlimited;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


public class BlockDestroyEvent implements Listener {

    @EventHandler
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

    }

}
