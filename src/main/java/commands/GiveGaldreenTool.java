package commands;

import de.ewu2000.galdreenblocksunlimited.CustomBlockCompound;
import de.ewu2000.galdreenblocksunlimited.GaldreenBlocksUnlimited;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GiveGaldreenTool implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(commandSender instanceof Player)) {
            return false;
        }
        Player player = (Player) commandSender;

        if (player.getGameMode() != GameMode.CREATIVE || !commandSender.isOp()) {
            player.sendMessage(GaldreenBlocksUnlimited.createChatMessage("Befehl kann nur als OP oder im Kreativ ausgeführt werden."));
            return true;
        }
        if(AddTool.tool == null){
            player.sendMessage(GaldreenBlocksUnlimited.createChatMessage("Es wurde noch kein Werkzeug definiert!"));
            return true;
        }
        player.getInventory().addItem(AddTool.tool);
        return true;
    }
}
