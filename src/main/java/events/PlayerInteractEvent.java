package events;


import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import commands.AddTool;
import de.ewu2000.galdreenblocksunlimited.CustomBlockCompound;
import de.ewu2000.galdreenblocksunlimited.GaldreenBlocksUnlimited;
import me.angeschossen.lands.api.LandsIntegration;
import me.angeschossen.lands.api.flags.type.Flags;
import me.angeschossen.lands.api.flags.type.RoleFlag;
import me.angeschossen.lands.api.land.LandWorld;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;

import java.lang.reflect.Type;

public class PlayerInteractEvent implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInteract(org.bukkit.event.player.PlayerInteractEvent event) {
        //check if worldguard regions allow for building.
        RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
        com.sk89q.worldedit.util.Location loc = BukkitAdapter.adapt(event.getClickedBlock().getLocation());
        com.sk89q.worldedit.world.World world = BukkitAdapter.adapt(event.getPlayer().getWorld());
        boolean wgResult = query.testState(loc, WorldGuardPlugin.inst().wrapPlayer(event.getPlayer()), com.sk89q.worldguard.protection.flags.Flags.BUILD);
        boolean canBypass = WorldGuard.getInstance().getPlatform().getSessionManager().hasBypass(WorldGuardPlugin.inst().wrapPlayer(event.getPlayer()), world);

        LandsIntegration landsApi = LandsIntegration.of(GaldreenBlocksUnlimited.plugin);
        LandWorld landsWorld = landsApi.getWorld(event.getClickedBlock().getWorld());
        boolean bBuild = false;
        if (landsWorld != null) { // Lands is enabled in this world
            if (landsWorld.hasRoleFlag(event.getPlayer().getUniqueId(), event.getClickedBlock().getLocation(), Flags.BLOCK_BREAK)) {
                //if lands allows building
                bBuild = true;
            }
        }

        //check if towny allows building for this player
        //boolean bBuild = PlayerCacheUtil.getCachePermission(event.getPlayer(), event.getClickedBlock().getLocation(), event.getClickedBlock().getType(), TownyPermission.ActionType.BUILD);

        if(event.getHand() == EquipmentSlot.OFF_HAND){
            return;
        }
        if (!((wgResult && bBuild) || canBypass)) {
            return;
        }
        if (event.useInteractedBlock() == Event.Result.DENY) { //Not denied by plot plugin
            return;
        }
        if (event.useItemInHand() == Event.Result.DENY) {
            return;
        }

        if (event.getPlayer().getInventory().getItemInMainHand().isSimilar(AddTool.tool)) { //changeTool
            if (!(event.getAction().isRightClick() && event.getClickedBlock() != null && !event.getPlayer().isSneaking())) {
                return;
            }

            BlockData newGoal = GaldreenBlocksUnlimited.cycles.get(event.getClickedBlock().getBlockData().toString());
            if (newGoal == null) {
                return;
            }
            event.setCancelled(true);
            event.getClickedBlock().setBlockData(newGoal, false);

        }else if(event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.DEBUG_STICK)){ //debug stick blocking
            if(event.getPlayer().getGameMode().equals(GameMode.CREATIVE) || event.getPlayer().isOp()){
                return; // Don't annoy staff
            }
            CustomBlockCompound cbc = GaldreenBlocksUnlimited.goalToCompound.get(GaldreenBlocksUnlimited.removeCraftBlock(event.getClickedBlock().getBlockData().toString()));

            if(cbc != null){
                event.setCancelled(true); //disallow debugstick for named customblocks
                return;
            }
            if(GaldreenBlocksUnlimited.blackListDebugStick.contains(event.getClickedBlock().getType())){
                event.setCancelled(true); //disallow some blocks
                return;
            }
        }
    }
}

