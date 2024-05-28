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


        //store block data of neighbours, if they are GaldreenBlocks and replace with barrier to prevent block updates
        BlockData[] storedNeighbours = new BlockData[6];
        boolean storedBlock = false;
        int i = 0;
        for(BlockFace bf : ReplaceTask.cartesian){
            CustomBlockCompound comp = GaldreenBlocksUnlimited.goalToCompound.get(event.getBlock().getRelative(bf).getBlockData().toString());
            if (comp != null) {
                if (comp.isUpdatedByOtherBlocks()) {
                    storedNeighbours[i] = event.getBlock().getRelative(bf).getBlockData();
                    event.getBlock().getRelative(bf).setType(Material.BARRIER, false);
                    storedBlock = true;
                }
            }
            i++;
        }

        //schedule task inorder to replace neighbouring blocks with correct ones again
        if(storedBlock){
            BukkitTask task = new ReplaceTask(plugin,storedNeighbours,event.getBlock().getWorld(),event.getBlock().getLocation()).runTaskLater(plugin, 1);
        }

        //place CustomBlock
        ItemStack inHand = event.getPlayer().getInventory().getItem(event.getHand()).clone();
        inHand.setAmount(1);
        BlockData goalState = GaldreenBlocksUnlimited.itemAndPlaceToGoal.get(inHand.toString() + event.getBlockPlaced().getBlockData().toString());
        if(goalState == null){
            String search = inHand.toString() + GaldreenBlocksUnlimited.server.createBlockData("minecraft:diamond_block").toString();
            goalState = GaldreenBlocksUnlimited.itemAndPlaceToGoal.get(search);
        }
        if(goalState == null){
            return;
        }
        event.getBlockPlaced().setBlockData(goalState,false);


    }

    public static boolean itemStacksEqual(ItemStack is1,ItemStack is2){
        ItemStack isc1 = is1.clone();
        ItemStack isc2 = is2.clone();
        isc1.setAmount(1);
        isc2.setAmount(1);
        return isc1.equals(isc2);
    }
}
