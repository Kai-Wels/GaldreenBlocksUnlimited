package events;

import commands.AddPlaceable;
import de.ewu2000.galdreenblocksunlimited.CustomBlock;
import de.ewu2000.galdreenblocksunlimited.CustomBlockCompound;
import de.ewu2000.galdreenblocksunlimited.CustomBlockCycle;
import de.ewu2000.galdreenblocksunlimited.GaldreenBlocksUnlimited;
import org.apache.commons.lang3.ObjectUtils;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class BlockPlaceEvent implements Listener {

    public JavaPlugin plugin;

    public BlockPlaceEvent(JavaPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlace(org.bukkit.event.block.BlockPlaceEvent event){

        //store block data of neighbours, if they are GaldreenBlocks
        BlockData[] storedNeighbours = new BlockData[6];
        boolean storedBlock = false;
        int i = 0;
        for(BlockFace bf : ReplaceTask.cartesian){
            BlockData neighbourBD =  event.getBlock().getRelative(bf).getBlockData();
            secondloop:
            for(CustomBlockCompound cbcmp : GaldreenBlocksUnlimited.allCustomBlockCompounds){
                for (CustomBlockCycle cbc : cbcmp.getBlockCyclesList()){
                    for (CustomBlock cb : cbc.getCustomBlocks()){
                        if(neighbourBD.equals(cb.getGoalData()) &&  cbcmp.isUpdatedByOtherBlocks()){
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

        //place CustomBlock
        placeloop:
        for(CustomBlockCompound cbcmp : GaldreenBlocksUnlimited.allCustomBlockCompounds){
            System.out.println("=?");
            if (itemStacksEqual(event.getPlayer().getInventory().getItem(event.getHand()),cbcmp.getItemToUse())) {
                System.out.println("=");
                for (CustomBlockCycle cbc : cbcmp.getBlockCyclesList()){
                    for (CustomBlock cb : cbc.getCustomBlocks()){
                        for (BlockData bd : cb.getPlaceData()){
                            if (event.getBlockPlaced().getBlockData().equals(bd) || bd.getMaterial() == Material.DIAMOND_BLOCK){
                                event.getBlockPlaced().setBlockData(cb.getGoalData(),false);
                                break placeloop;
                            }
                        }
                    }
                }
            }
        }

        //schedule task inorder to replace neighbouring blocks with correct ones again
        if(storedBlock){
            BukkitTask task = new ReplaceTask(plugin,storedNeighbours,event.getBlock().getWorld(),event.getBlock().getLocation()).runTaskLater(plugin, 1);
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
