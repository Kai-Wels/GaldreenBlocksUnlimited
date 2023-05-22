package events;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import de.ewu2000.galdreenblocksunlimited.CustomBlock;
import de.ewu2000.galdreenblocksunlimited.CustomBlockCompound;
import de.ewu2000.galdreenblocksunlimited.CustomBlockCycle;
import de.ewu2000.galdreenblocksunlimited.GaldreenBlocksUnlimited;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;

public class PlayerInteractEvent implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInteract(org.bukkit.event.player.PlayerInteractEvent event){

        //check if worldguard regions allow for building.
        RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
        com.sk89q.worldedit.util.Location loc = BukkitAdapter.adapt(event.getClickedBlock().getLocation());
        com.sk89q.worldedit.world.World world = BukkitAdapter.adapt(event.getPlayer().getWorld());
        boolean wgResult = query.testState(loc, WorldGuardPlugin.inst().wrapPlayer(event.getPlayer()), Flags.BUILD);
        boolean canBypass = WorldGuard.getInstance().getPlatform().getSessionManager().hasBypass(WorldGuardPlugin.inst().wrapPlayer(event.getPlayer()), world);

        if (wgResult ||canBypass){

            if ( event.getAction().isRightClick() && event.getClickedBlock() != null && !event.getPlayer().isSneaking()){
                if (event.useInteractedBlock() != Event.Result.DENY){ //Not denied by plot plugin
                    for(CustomBlockCompound cbcmp : GaldreenBlocksUnlimited.allCustomBlockCompounds){
                        for (CustomBlockCycle cbc : cbcmp.getBlockCyclesList()) {
                            int i = 0;
                            if (cbc.getCustomBlocks().size() > 1) {
                                for (CustomBlock cb : cbc.getCustomBlocks()) {
                                    if (event.getClickedBlock().getBlockData().equals(cb.getGoalData())) {
                                        event.setCancelled(true);
                                        if (event.getHand() == EquipmentSlot.HAND) {
                                            if (i == cbc.getCustomBlocks().size() - 1) {
                                                i = 0;
                                            } else {
                                                i++;
                                            }
                                            event.getClickedBlock().setBlockData(cbc.getCustomBlocks().get(i).getGoalData(), false);
                                        }
                                        return;
                                    } else {
                                        i++;
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}
