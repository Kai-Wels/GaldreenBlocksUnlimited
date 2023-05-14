package events;

import de.ewu2000.galdreenblocksunlimited.CustomBlock;
import de.ewu2000.galdreenblocksunlimited.CustomBlockCompound;
import de.ewu2000.galdreenblocksunlimited.CustomBlockCycle;
import de.ewu2000.galdreenblocksunlimited.GaldreenBlocksUnlimited;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class BlockPlaceEvent implements Listener {
    @EventHandler
    public void onPlace(org.bukkit.event.block.BlockPlaceEvent event){
        for(CustomBlockCompound cbcmp : GaldreenBlocksUnlimited.allCustomBlockCompounds){
            if (itemStacksEqual(event.getPlayer().getInventory().getItem(event.getHand()),cbcmp.getItemToUse())) {
                for (CustomBlockCycle cbc : cbcmp.getBlockCyclesList()){
                    for (CustomBlock cb : cbc.getCustomBlocks()){
                        for (BlockData bd : cb.getPlaceData()){
                            if (bd.getMaterial() == Material.DIAMOND_BLOCK || event.getBlockPlaced().getBlockData().equals(bd)){
                                event.getBlockPlaced().setBlockData(cb.getGoalData(),false);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    public static boolean itemStacksEqual(ItemStack is1,ItemStack is2){
        ItemStack isc1 = is1.clone();
        ItemStack isc2 = is2.clone();
        isc1.setAmount(1);
        isc2.setAmount(1);
        return isc1.equals(isc2);
    }
}
