package events;

import de.ewu2000.galdreenblocks.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.*;
import org.bukkit.block.data.type.PistonHead;
import org.bukkit.block.data.type.TechnicalPiston;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;
import java.util.List;

public class PlaceEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent e){

        //prevent placing blocks behind Pistons

        BlockFace[] bfs1 = new BlockFace[]{BlockFace.EAST,BlockFace.NORTH,BlockFace.WEST,BlockFace.SOUTH,BlockFace.UP,BlockFace.DOWN};

        for(PistonRotationState pRS : PistonRotationState.allPistonRotationStates){
            for(BlockFace bf : bfs1){
                if(e.getBlock().getRelative(bf).getType() == pRS.getMatOfModel()){
                    if(((Directional) e.getBlock().getRelative(bf).getBlockData()).getFacing() == bf){
                        e.setCancelled(true);
                        e.getPlayer().sendMessage(ChatColor.RED + "[!]" + ChatColor.GRAY + " Der angrenzende Piston verhindert das platzieren eines Blockes!");
                        return;
                    }
                }
            }
        }

        //prevent placing blocks behind buttons
        BlockFace[] bfs = new BlockFace[]{BlockFace.EAST,BlockFace.NORTH,BlockFace.WEST,BlockFace.SOUTH};

        for(ButtonRotationState bRS : ButtonRotationState.allButtonRotationStates){

            for(BlockFace bf : bfs1){
                if(e.getBlock().getRelative(bf).getType() == bRS.getMaterial() && e.getBlock().getRelative(bf).getBlockData() instanceof FaceAttachable &&
                        ((FaceAttachable)e.getBlock().getRelative(bf).getBlockData()).getAttachedFace() == FaceAttachable.AttachedFace.WALL){
                    if(((Directional) e.getBlock().getRelative(bf).getBlockData()).getFacing() == bf){
                        e.setCancelled(true);
                        e.getPlayer().sendMessage(ChatColor.RED + "[!]" + ChatColor.GRAY + " Der angrenzenden Knopf verhindert das platzieren eines Blockes!");
                        return;
                    }
                }
                else if(e.getBlock().getRelative(BlockFace.DOWN).getType() == bRS.getMaterial() && e.getBlock().getRelative(BlockFace.DOWN).getBlockData() instanceof FaceAttachable &&
                        ((FaceAttachable)e.getBlock().getRelative(BlockFace.DOWN).getBlockData()).getAttachedFace() == FaceAttachable.AttachedFace.CEILING) {
                    e.setCancelled(true);
                    e.getPlayer().sendMessage(ChatColor.RED + "[!]" + ChatColor.GRAY + " Der angrenzenden Knopf verhindert das platzieren eines Blockes!");
                    return;
                }
                else if(e.getBlock().getRelative(BlockFace.UP).getType() == bRS.getMaterial() && e.getBlock().getRelative(BlockFace.UP).getBlockData() instanceof FaceAttachable &&
                        ((FaceAttachable)e.getBlock().getRelative(BlockFace.UP).getBlockData()).getAttachedFace() == FaceAttachable.AttachedFace.FLOOR) {
                    e.setCancelled(true);
                    e.getPlayer().sendMessage(ChatColor.RED + "[!]" + ChatColor.GRAY + " Der angrenzenden Knopf verhindert das platzieren eines Blockes!");
                    return;
                }

            }
        }
        //prevent placing blocks behind Plates
        for(PlateState ps : PlateState.allPlateStates){
            if(e.getBlock().getRelative(BlockFace.UP).getType() == ps.getMaterial()){
                e.setCancelled(true);
                e.getPlayer().sendMessage(ChatColor.RED + "[!]" + ChatColor.GRAY + " Die angrenzenden Druckplatte verhindert das platzieren eines Blockes!");
                return;
            }

        }


        //Buttons
        for (ButtonRotationState rs: ButtonRotationState.allButtonRotationStates) {
            List<String> expectedLore = new ArrayList<>();
            expectedLore.add("Dekoration");
            expectedLore.add(rs.getName());

            if(e.getItemInHand().getItemMeta().getLore() != null && e.getItemInHand().getItemMeta().getLore().equals(expectedLore)){

                if(e.getBlock().getBlockData() instanceof Directional && e.getBlock().getBlockData() instanceof  FaceAttachable){
                    Directional directional = (Directional) e.getBlock().getBlockData();
                    FaceAttachable attachable = (FaceAttachable) e.getBlock().getBlockData();

                    ButtonRotationStateTransition possible = rs.getTransitions().get(0);
                    for(ButtonRotationStateTransition rt: rs.getTransitions()){
                        if(rt.getFromBlockFace() == null || rt.getFromBlockFace() == directional.getFacing()){
                            if(rt.getFromAttachedFace() == null ||rt.getFromAttachedFace() == attachable.getAttachedFace()){
                               possible = rt;
                               break;
                            }
                        }
                    }
                    attachable.setAttachedFace(possible.getToAttachedFace());
                    e.getBlock().setBlockData(attachable);

                    directional = (Directional) e.getBlock().getBlockData();
                    directional.setFacing(possible.getToBlockFace());
                    e.getBlock().setBlockData(directional);
                }
                break;
            }
        }

        //Pistons
        for (PistonRotationState rs: PistonRotationState.allPistonRotationStates) {
            List<String> expectedLore = new ArrayList<>();
            expectedLore.add("Dekoration");
            expectedLore.add(rs.getName());

            if(e.getItemInHand().getItemMeta().getLore() != null && e.getItemInHand().getItemMeta().getLore().equals(expectedLore)){

                if(e.getBlock().getBlockData() instanceof Directional){
                    Directional directional = (Directional) e.getBlock().getBlockData();

                    PistonRotationStateTransition possible = rs.getTransitions().get(0);
                    for(PistonRotationStateTransition rt: rs.getTransitions()){
                        if(rt.getFromBlockFace() == null || rt.getFromBlockFace() == directional.getFacing()){
                            possible = rt;
                            break;
                        }
                    }

                    e.getBlock().setType(Material.PISTON_HEAD,false);

                    directional = (Directional) e.getBlock().getBlockData();
                    directional.setFacing(possible.getToBlockFace());
                    ((TechnicalPiston) directional).setType(rs.getType());
                    ((PistonHead) directional).setShort(rs.getShort());
                    e.getBlock().setBlockData(directional,false);

                }
                break;
            }
        }

        //PressurePlates
        for (PlateState ps: PlateState.allPlateStates) {
            List<String> expectedLore = new ArrayList<>();
            expectedLore.add("Dekoration");
            expectedLore.add(ps.getName());

            if(e.getItemInHand().getItemMeta().getLore() != null && e.getItemInHand().getItemMeta().getLore().equals(expectedLore)){
                e.getBlock().setBlockData(Bukkit.getServer().createBlockData( ps.getMaterial(), "[power=" + ps.getPower() + "]"),false );
            }

                break;

        }


    }

}