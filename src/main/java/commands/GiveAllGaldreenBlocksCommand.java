package commands;

import de.ewu2000.galdreenblocksunlimited.CustomBlock;
import de.ewu2000.galdreenblocksunlimited.CustomBlockCompound;
import de.ewu2000.galdreenblocksunlimited.GaldreenBlocksUnlimited;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GiveAllGaldreenBlocksCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (commandSender instanceof Player){
            if (((Player)commandSender).getGameMode() == GameMode.CREATIVE){
                for(CustomBlockCompound cbcmp: GaldreenBlocksUnlimited.allCustomBlockCompounds) {
                    ((Player) commandSender).getInventory().addItem(cbcmp.getItemToUse());
                }
            }
        }
        return false;
    }
}
