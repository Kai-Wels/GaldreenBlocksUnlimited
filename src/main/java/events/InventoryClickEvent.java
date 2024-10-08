package events;

import de.ewu2000.galdreenblocksunlimited.CustomBlockCompound;
import de.ewu2000.galdreenblocksunlimited.GaldreenBlocksUnlimited;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;

public class InventoryClickEvent  implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onAnvil(org.bukkit.event.inventory.InventoryClickEvent event){
        if(event.getClickedInventory() == null || event.getClickedInventory().getType() == null || !event.getClickedInventory().getType().equals(InventoryType.ANVIL)){
            return;
        }
        if(event.getSlotType() != InventoryType.SlotType.RESULT) {
            return;
        }

        if(event.getCurrentItem() == null || event.getCurrentItem().getItemMeta() == null  || event.getCurrentItem().getItemMeta().hasLore() == false){
            return;
        }


        System.out.println(((TextComponent)event.getCurrentItem().getItemMeta().lore().get(0)).content());
        if(((TextComponent)event.getCurrentItem().getItemMeta().lore().get(0)).content().equals(((TextComponent) ((CustomBlockCompound)GaldreenBlocksUnlimited.goalToCompound.values().toArray()[0]).getItemToUse().getItemMeta().lore().get(0)).content())){
            event.setCancelled(true);
        }
    }
}
