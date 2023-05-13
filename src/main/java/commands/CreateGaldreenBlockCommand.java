package commands;

import de.ewu2000.galdreenblocksunlimited.CustomBlock;
import de.ewu2000.galdreenblocksunlimited.CustomBlockCompound;
import de.ewu2000.galdreenblocksunlimited.CustomBlockCycle;
import de.ewu2000.galdreenblocksunlimited.GaldreenBlocksUnlimited;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CreateGaldreenBlockCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        // /createGaldreenBlock x y z name

        //Layout
        if (commandSender instanceof Player && ((Player) commandSender).getInventory().getItemInMainHand().getType() != Material.AIR) {
            if (args.length >= 4) {
                //read item name
                String itemName = "";
                for (int i = 3; i < args.length; i++) {
                    itemName += args[i];
                    if (i != args.length - 1) {
                        itemName += " ";
                    }
                }
                Location startLocation;
                try {
                    startLocation = new Location(((Player) commandSender).getWorld(), Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
                } catch (NumberFormatException e) {
                    return false;
                }

                //create Itemstack
                ItemStack is = ((Player) commandSender).getInventory().getItemInMainHand().clone();
                ItemMeta im = is.getItemMeta();
                im.displayName(Component.newline().content(itemName));
                is.setAmount(1);
                ArrayList<String> lore = new ArrayList<>();
                lore.add("Galdreen Block");
                im.setLore(lore);
                is.setItemMeta(im);


                CustomBlockCompound cbcmp = new CustomBlockCompound(is);
                Location cycleLocation = startLocation.clone();
                System.out.println("1.");
                while (cycleLocation.getBlock().getType() != Material.AIR) {
                    CustomBlockCycle cbc = new CustomBlockCycle();
                    System.out.println("  2.");
                    Location inCycleLocation = cycleLocation.clone();
                    while (inCycleLocation.getBlock().getType() != Material.AIR) {
                        CustomBlock cb = new CustomBlock(inCycleLocation.getBlock().getBlockData());
                        System.out.println("    3.");
                        Location inBlockDataLocation = inCycleLocation.clone();
                        inBlockDataLocation.add(2, 0, 0);
                        while (inBlockDataLocation.getBlock().getType() != Material.AIR) {
                            cb.getPlaceData().add(inBlockDataLocation.getBlock().getBlockData());
                            System.out.println("      4.");
                            inBlockDataLocation.add(2,0,0);
                        }
                        cbc.getCustomBlocks().add(cb);
                        inCycleLocation.add(0, 2, 0);
                        System.out.println(inCycleLocation.toString());
                    }
                    cbcmp.getBlockCyclesList().add(cbc);
                    cycleLocation.add(0, 0, 2);
                }
                GaldreenBlocksUnlimited.allCustomBlockCompounds.add(cbcmp);

            }

        }
        return false;
    }
}
