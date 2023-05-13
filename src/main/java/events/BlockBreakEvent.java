package events;

import de.ewu2000.galdreenblocksunlimited.CustomBlock;
import de.ewu2000.galdreenblocksunlimited.CustomBlockCompound;
import de.ewu2000.galdreenblocksunlimited.CustomBlockCycle;
import de.ewu2000.galdreenblocksunlimited.GaldreenBlocksUnlimited;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BlockBreakEvent implements Listener {

    @EventHandler
    public void onBreak(org.bukkit.event.block.BlockBreakEvent event){
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE){
            for(CustomBlockCompound cbcmp : GaldreenBlocksUnlimited.allCustomBlockCompounds){
                for (CustomBlockCycle cbc : cbcmp.getBlockCyclesList()){
                    for (CustomBlock cb : cbc.getCustomBlocks()){
                        if(event.getBlock().getBlockData().equals(cb.getGoalData())){
                            event.setCancelled(true);
                            event.getBlock().setType(Material.AIR,false);
                            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(),cbcmp.getItemToUse());
                            return;
                        }
                    }
                }
            }
        }
    }
}
