package events;

import net.md_5.bungee.api.chat.hover.content.Item;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.FaceAttachable;
import org.bukkit.block.data.type.Piston;
import org.bukkit.block.data.type.PistonHead;
import org.bukkit.block.data.type.TechnicalPiston;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.ewu2000.galdreenblocks.*;

import java.util.LinkedList;
import java.util.List;

public class BreakEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent e){
        boolean isDroped = false;


        //Buttons
        for (ButtonRotationState rs : ButtonRotationState.allButtonRotationStates) {
            if (e.getBlock().getType().equals(rs.getMaterial())) {
                //e.getPlayer().sendMessage("BreakEvent!");
                BlockData bd = e.getBlock().getBlockData();
                BlockFace bf = null;
                FaceAttachable.AttachedFace af = null;
                if (bd instanceof Directional && bd instanceof FaceAttachable) {
                    bf = ((Directional) bd).getFacing();
                    af = ((FaceAttachable) bd).getAttachedFace();
                    for (ButtonRotationStateTransition rt : rs.getTransitions()) {
                        if (/*rt.getToBlockFace() == null || */rt.getToBlockFace() == bf) {
                            if (/*rt.getToAttachedFace() == null || */rt.getToAttachedFace() == af) {
                                if (e.getPlayer().getGameMode() == GameMode.SURVIVAL && e.isDropItems()) {
                                    e.setDropItems(false);
                                    Material mat = rs.getMaterial();
                                    ItemStack is = new ItemStack(mat);
                                    ItemMeta im = is.getItemMeta();
                                    im.setDisplayName(rs.getName());
                                    List<String> lore = new LinkedList<>();
                                    lore.add("Dekoration");
                                    lore.add(rs.getName());
                                    im.setLore(lore);
                                    is.setItemMeta(im);

                                    e.getBlock().getWorld().dropItem(new Location(e.getBlock().getWorld(), e.getBlock().getX() + 0.5f, e.getBlock().getY() + 0.5f, e.getBlock().getZ() + 0.5f), is);
                                    isDroped = true;
                                }
                                checkForSurroundings(e,isDroped);
                                return;
                            }
                        }
                    }
                }
            }
        }
        //Pistons
        for (PistonRotationState rs : PistonRotationState.allPistonRotationStates) {
            if (e.getBlock().getType().equals(rs.getMatOfModel()) && ((TechnicalPiston) e.getBlock().getBlockData()).getType() == rs.getType() && ((PistonHead)e.getBlock().getBlockData()).isShort() == rs.getShort()) {
                //e.getPlayer().sendMessage("BreakEvent!");
                BlockData bd = e.getBlock().getBlockData();
                BlockFace bf = null;
                if (bd instanceof Directional) {
                    bf = ((Directional) bd).getFacing();
                    for (PistonRotationStateTransition rt : rs.getTransitions()) {
                        if (rt.getToBlockFace() == bf) {
                            if (e.getPlayer().getGameMode() == GameMode.SURVIVAL) {
                                if (e.getBlock().getRelative(PistonRotationState.negateFace(rt.getToBlockFace())).getType() == Material.PISTON || e.getBlock().getRelative(PistonRotationState.negateFace(rt.getToBlockFace())).getType() == Material.STICKY_PISTON) {
                                    if (((Piston) e.getBlock().getRelative(PistonRotationState.negateFace(rt.getToBlockFace())).getBlockData()).isExtended()) {
                                        checkForSurroundings(e,isDroped);
                                        return;
                                    }
                                }
                                Material mat = rs.getMatToPlace();
                                ItemStack is = new ItemStack(mat);
                                ItemMeta im = is.getItemMeta();
                                im.setDisplayName(rs.getName());
                                List<String> lore = new LinkedList<>();
                                lore.add("Dekoration");
                                lore.add(rs.getName());
                                im.setLore(lore);
                                is.setItemMeta(im);

                                e.getBlock().getWorld().dropItem(new Location(e.getBlock().getWorld(), e.getBlock().getX() + 0.5f, e.getBlock().getY() + 0.5f, e.getBlock().getZ() + 0.5f), is);
                                isDroped = true;
                            }
                            checkForSurroundings(e,isDroped);
                            return;
                        }

                    }
                }
            }
        }
        //Plates
        for (PlateState ps : PlateState.allPlateStates) {
            if (e.getBlock().getType().equals(ps.getMaterial())) {
                BlockData bd = e.getBlock().getBlockData();
                if(bd.equals(Bukkit.getServer().createBlockData( ps.getMaterial(), "[power=" + ps.getPower() + "]"))){
                    if (e.getPlayer().getGameMode() == GameMode.SURVIVAL && e.isDropItems()) {
                        e.setDropItems(false);
                        Material mat = ps.getMaterial();
                        ItemStack is = new ItemStack(mat);
                        ItemMeta im = is.getItemMeta();
                        im.setDisplayName(ps.getName());
                        List<String> lore = new LinkedList<>();
                        lore.add("Dekoration");
                        lore.add(ps.getName());
                        im.setLore(lore);
                        is.setItemMeta(im);

                        e.getBlock().getWorld().dropItem(new Location(e.getBlock().getWorld(), e.getBlock().getX() + 0.5f, e.getBlock().getY() + 0.5f, e.getBlock().getZ() + 0.5f), is);
                        isDroped = true;
                    }
                    checkForSurroundings(e, isDroped);
                    return;
                }
            }
        }
        checkForSurroundings(e,isDroped);

    }

    public void checkForSurroundings(BlockBreakEvent e, boolean isDroped) {
        //For all Blocks, that arent in RotationSates or Pistonheads
        BlockFace[] bfs = new BlockFace[]{BlockFace.EAST, BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH, BlockFace.UP, BlockFace.DOWN};

        for (ButtonRotationState rs : ButtonRotationState.allButtonRotationStates) {
            for (BlockFace bf : bfs) {
                if (e.getBlock().getRelative(bf).getType().equals(rs.getMaterial())) {
                    if (e.getPlayer().getGameMode() == GameMode.SURVIVAL) {
                        for (ItemStack is : e.getBlock().getDrops(e.getPlayer().getInventory().getItemInMainHand(), e.getPlayer())) {
                            if(isDroped == false){
                                e.getBlock().getWorld().dropItem(new Location(e.getBlock().getWorld(), e.getBlock().getX() + 0.5f, e.getBlock().getY() + 0.5f, e.getBlock().getZ() + 0.5f), is);
                                Container con = null;

                                switch (e.getBlock().getType()){
                                    case CHEST: con = (Container) e.getBlock().getState(); break;
                                    case BARREL: con = (Container)e.getBlock().getState(); break;
                                    case BREWING_STAND: con = (Container)e.getBlock().getState(); break;
                                    case FURNACE: con = (Container)e.getBlock().getState(); break;
                                    case SMOKER: con = (Container)e.getBlock().getState(); break;
                                    case BLAST_FURNACE: con = (Container)e.getBlock().getState(); break;
                                    case HOPPER: con = (Container)e.getBlock().getState(); break;
                                    case DISPENSER: con = (Container)e.getBlock().getState(); break;
                                    default: con = null;
                                }
                                if(con != null){
                                    for(ItemStack stack : con.getInventory().getContents()){
                                        if(stack != null){
                                            e.getBlock().getWorld().dropItem(new Location(e.getBlock().getWorld(), e.getBlock().getX() + 0.5f, e.getBlock().getY() + 0.5f, e.getBlock().getZ() + 0.5f),stack);
                                        }
                                    }
                                }
                                isDroped = true;
                            }
                        }
                    }

                    e.setCancelled(true);
                    e.getBlock().setType(Material.AIR, false);
                    return;
                }
            }
        }
        for (PistonRotationState rs : PistonRotationState.allPistonRotationStates) {
            for (BlockFace bf : bfs) {
                if (e.getBlock().getRelative(bf).getType().equals(rs.getMatOfModel())) {

                    if (e.getBlock().getType() == Material.PISTON || e.getBlock().getType() == Material.STICKY_PISTON) {
                        if (((Piston) e.getBlock().getBlockData()).isExtended()) {
                            if (((Piston) e.getBlock().getBlockData()).getFacing() == ((Directional) e.getBlock().getRelative(bf).getBlockData()).getFacing() && ((Piston) e.getBlock().getBlockData()).getFacing() == bf) {
                                return;
                            }
                        }
                    }

                    if (e.getPlayer().getGameMode() == GameMode.SURVIVAL) {
                        for (ItemStack is : e.getBlock().getDrops(e.getPlayer().getInventory().getItemInMainHand(), e.getPlayer())) {
                            if (isDroped == false) {
                                e.getBlock().getWorld().dropItem(new Location(e.getBlock().getWorld(), e.getBlock().getX() + 0.5f, e.getBlock().getY() + 0.5f, e.getBlock().getZ() + 0.5f), is);
                                Container con = null;

                                switch (e.getBlock().getType()){
                                    case CHEST: con = (Container) e.getBlock().getState(); break;
                                    case BARREL: con = (Container)e.getBlock().getState(); break;
                                    case BREWING_STAND: con = (Container)e.getBlock().getState(); break;
                                    case FURNACE: con = (Container)e.getBlock().getState(); break;
                                    case SMOKER: con = (Container)e.getBlock().getState(); break;
                                    case BLAST_FURNACE: con = (Container)e.getBlock().getState(); break;
                                    case HOPPER: con = (Container)e.getBlock().getState(); break;
                                    case DISPENSER: con = (Container)e.getBlock().getState(); break;
                                    default: con = null;
                                }
                                if(con != null){
                                    for(ItemStack stack : con.getInventory().getContents()){
                                        if(stack != null){
                                            e.getBlock().getWorld().dropItem(new Location(e.getBlock().getWorld(), e.getBlock().getX() + 0.5f, e.getBlock().getY() + 0.5f, e.getBlock().getZ() + 0.5f),stack);
                                        }
                                    }
                                }
                                isDroped = true;
                            }
                        }
                    }
                    e.setCancelled(true);
                    e.getBlock().setType(Material.AIR, false);
                    return;
                }
            }
        }
        for (PlateState ps : PlateState.allPlateStates) {

            if (e.getBlock().getRelative(BlockFace.UP).getType().equals(ps.getMaterial())) {

                if (e.getPlayer().getGameMode() == GameMode.SURVIVAL) {
                    for (ItemStack is : e.getBlock().getDrops(e.getPlayer().getInventory().getItemInMainHand(), e.getPlayer())) {
                        if (isDroped == false) {
                            e.getBlock().getWorld().dropItem(new Location(e.getBlock().getWorld(), e.getBlock().getX() + 0.5f, e.getBlock().getY() + 0.5f, e.getBlock().getZ() + 0.5f), is);
                            Container con = null;

                            switch (e.getBlock().getType()){
                                case CHEST: con = (Container) e.getBlock().getState(); break;
                                case BARREL: con = (Container)e.getBlock().getState(); break;
                                case BREWING_STAND: con = (Container)e.getBlock().getState(); break;
                                case FURNACE: con = (Container)e.getBlock().getState(); break;
                                case SMOKER: con = (Container)e.getBlock().getState(); break;
                                case BLAST_FURNACE: con = (Container)e.getBlock().getState(); break;
                                case HOPPER: con = (Container)e.getBlock().getState(); break;
                                case DISPENSER: con = (Container)e.getBlock().getState(); break;
                                default: con = null;
                            }
                            if(con != null){
                                for(ItemStack stack : con.getInventory().getContents()){
                                    if(stack != null){
                                        e.getBlock().getWorld().dropItem(new Location(e.getBlock().getWorld(), e.getBlock().getX() + 0.5f, e.getBlock().getY() + 0.5f, e.getBlock().getZ() + 0.5f),stack);
                                    }
                                }
                            }
                            isDroped = true;
                        }
                    }
                }
                e.setCancelled(true);
                e.getBlock().setType(Material.AIR, false);
                return;
            }

        }
    }


}

