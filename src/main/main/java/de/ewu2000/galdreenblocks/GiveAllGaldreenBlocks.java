package de.ewu2000.galdreenblocks;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.LinkedList;
import java.util.List;

public class GiveAllGaldreenBlocks implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        for(ButtonRotationState brs : ButtonRotationState.allButtonRotationStates){

            Material mat = brs.getMaterial();
            ItemStack is = new ItemStack(mat);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(brs.getName());
            List<String> lore = new LinkedList<>();
            lore.add("Dekoration");
            lore.add(brs.getName());
            im.setLore(lore);
            is.setItemMeta(im);
            ((Player)commandSender).getInventory().addItem(is);
        }

        for(PistonRotationState prs : PistonRotationState.allPistonRotationStates){

            Material mat = prs.getMatToPlace();
            ItemStack is = new ItemStack(mat);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(prs.getName());
            List<String> lore = new LinkedList<>();
            lore.add("Dekoration");
            lore.add(prs.getName());
            im.setLore(lore);
            is.setItemMeta(im);
            ((Player)commandSender).getInventory().addItem(is);
        }
        for(PlateState ps : PlateState.allPlateStates){

            Material mat = ps.getMaterial();
            ItemStack is = new ItemStack(mat);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(ps.getName());
            List<String> lore = new LinkedList<>();
            lore.add("Dekoration");
            lore.add(ps.getName());
            im.setLore(lore);
            is.setItemMeta(im);
            ((Player)commandSender).getInventory().addItem(is);
        }
        return false;
    }
}
