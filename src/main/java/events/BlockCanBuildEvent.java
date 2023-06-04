package events;

import de.ewu2000.galdreenblocksunlimited.CustomBlockCompound;
import de.ewu2000.galdreenblocksunlimited.GaldreenBlocksUnlimited;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class BlockCanBuildEvent implements Listener {

    public static ArrayList<ItemStack> alwaysPlaceable = new ArrayList<>();

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBuild(org.bukkit.event.block.BlockCanBuildEvent event){

        ItemStack usedItem = null;
        //get ITem in used Hand
        if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.AIR){
            usedItem = event.getPlayer().getInventory().getItemInOffHand();
        }else{
            usedItem = event.getPlayer().getInventory().getItemInMainHand();
        }

        for(ItemStack is: alwaysPlaceable){
            if(BlockPlaceEvent.itemStacksEqual(usedItem,is)){
                event.setBuildable(true);
            }
        }

    }


}
