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
import org.bukkit.inventory.ItemStack;
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
            String bd = GaldreenBlocksUnlimited.removeCraftBlock(event.getBlock().getRelative(bf).getBlockData().toString());
            CustomBlockCompound comp = GaldreenBlocksUnlimited.goalToCompound.get(bd);
            if (comp != null) {
                if (comp.isUpdatedByOtherBlocks()) {
                    storedNeighbours[i] = event.getBlock().getRelative(bf).getBlockData();
                    event.getBlock().getRelative(bf).setType(Material.BARRIER, false);
                    storedBlock = true;
                }
            }
            i++;
        }

        //schedule task to replace blocks again
        if(storedBlock){
            BukkitTask task = new ReplaceTask(plugin,storedNeighbours,event.getBlock().getWorld(),event.getBlock().getLocation()).runTaskLater(plugin, 1);
        }

        //break behaviour
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE){
            return;
        }

        String bd = GaldreenBlocksUnlimited.removeCraftBlock(event.getBlock().getBlockData().toString());
        CustomBlockCompound comp = GaldreenBlocksUnlimited.goalToCompound.get(bd);
        if (comp == null){
            return;
        }
        event.setCancelled(true);
        if(comp.isUpdatedByOtherBlocks() == false){
            for(ItemStack it : event.getBlock().getDrops(event.getPlayer().getInventory().getItemInMainHand(),event.getPlayer())){
                event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(),it);
            }
        }else{
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(),comp.getItemToUse());

        }
        event.getBlock().setType(Material.AIR,false);
    }
}
