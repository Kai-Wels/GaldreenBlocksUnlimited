package events;

import de.ewu2000.galdreenblocksunlimited.CustomBlock;
import de.ewu2000.galdreenblocksunlimited.CustomBlockCompound;
import de.ewu2000.galdreenblocksunlimited.CustomBlockCycle;
import de.ewu2000.galdreenblocksunlimited.GaldreenBlocksUnlimited;
import org.bukkit.event.Listener;

public class BlockPhysicsEvent implements Listener {


    public void onPhysic(BlockPhysicsEvent event){
        for(CustomBlockCompound cbcmp : GaldreenBlocksUnlimited.allCustomBlockCompounds){
            for (CustomBlockCycle cbc : cbcmp.getBlockCyclesList()){
                for (CustomBlock cb : cbc.getCustomBlocks()){
                    event.
                    if(event.getBlock().getBlockData().equals(cb.getGoalData())){
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }
    }
}
