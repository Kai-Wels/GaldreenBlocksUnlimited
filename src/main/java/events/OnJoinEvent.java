package events;

import de.ewu2000.galdreenblocksunlimited.GaldreenBlocksUnlimited;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.permissions.PermissionAttachment;

public class OnJoinEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onAnvil(org.bukkit.event.player.PlayerJoinEvent event){
        PermissionAttachment attachment = event.getPlayer().addAttachment(GaldreenBlocksUnlimited.plugin);
        attachment.setPermission("minecraft.debugstick.always",true);
    }
}
