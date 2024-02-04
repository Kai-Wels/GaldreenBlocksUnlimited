package commands;

import de.ewu2000.galdreenblocksunlimited.CustomBlock;
import de.ewu2000.galdreenblocksunlimited.CustomBlockCompound;
import de.ewu2000.galdreenblocksunlimited.GaldreenBlocksUnlimited;
import net.kyori.adventure.text.Component;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class GiveGaldreenBlocksCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (commandSender instanceof Player){
            if (((Player)commandSender).getGameMode() == GameMode.CREATIVE || commandSender.isOp()){
                if (args.length >= 1){ //give specific item
                    PlainTextComponentSerializer serializer = PlainTextComponentSerializer.plainText();
                    for(CustomBlockCompound cbcmp: GaldreenBlocksUnlimited.goalToCompound.values()) {
                        if (cbcmp.getItemToUse().getItemMeta().hasDisplayName()) {
                            if (serializer.serialize(cbcmp.getItemToUse().displayName()).equalsIgnoreCase("[" + String.join(" ",args) + "]")) { // for some reason there are brackets around the itemname
                                ((Player) commandSender).getWorld().dropItem(((Player) commandSender).getLocation(), cbcmp.getItemToUse());
                                return true;
                            }
                        }

                    }
                    return false;
                }else if (args.length == 0){ //drop all items
                    Set<ItemStack> items = new HashSet<>();
                    for(CustomBlockCompound cbcmp: GaldreenBlocksUnlimited.goalToCompound.values()) {
                        if (cbcmp.getItemToUse().getItemMeta().hasDisplayName()){
                            items.add(cbcmp.getItemToUse());
                        }
                    }
                    for(ItemStack i : items){
                        ((Player) commandSender).getWorld().dropItem(((Player) commandSender).getLocation(), i);
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
