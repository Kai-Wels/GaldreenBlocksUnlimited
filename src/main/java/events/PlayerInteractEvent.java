package events;

import de.ewu2000.galdreenblocksunlimited.CustomBlock;
import de.ewu2000.galdreenblocksunlimited.CustomBlockCompound;
import de.ewu2000.galdreenblocksunlimited.CustomBlockCycle;
import de.ewu2000.galdreenblocksunlimited.GaldreenBlocksUnlimited;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;

public class PlayerInteractEvent implements Listener {
    @EventHandler
    public void onInteract(org.bukkit.event.player.PlayerInteractEvent event){
        if ( event.getAction().isRightClick() && event.getClickedBlock() != null && !event.getPlayer().isSneaking()){
            if (event.useInteractedBlock() != Event.Result.DENY){ //Not denied by plot plugin
                for(CustomBlockCompound cbcmp : GaldreenBlocksUnlimited.allCustomBlockCompounds){
                    for (CustomBlockCycle cbc : cbcmp.getBlockCyclesList()){
                        int i = 0;
                        for (CustomBlock cb : cbc.getCustomBlocks()){
                            if(event.getClickedBlock().getBlockData().equals(cb.getGoalData())){
                                event.setCancelled(true);
                                if (event.getHand() == EquipmentSlot.HAND){
                                    if(i == cbc.getCustomBlocks().size() - 1){
                                        i = 0;
                                    }else{
                                        i++;
                                    }
                                    event.getClickedBlock().setBlockData(cbc.getCustomBlocks().get(i).getGoalData(),false);
                                }
                                return;
                            }else{
                                i++;
                            }
                        }
                    }
                }
            }
        }
    }
}
