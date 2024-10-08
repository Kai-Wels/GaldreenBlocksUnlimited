package commands;

import de.ewu2000.galdreenblocksunlimited.CustomBlock;
import de.ewu2000.galdreenblocksunlimited.CustomBlockCompound;
import de.ewu2000.galdreenblocksunlimited.GaldreenBlocksUnlimited;
import net.kyori.adventure.text.Component;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class GiveGaldreenBlocksCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        // Befehl hat form /givegaldreenblocks <optional:Blockname> wenn er von einem Spieler ausgeführt wird
        // Und sonst die Form /givegaldreenblocks <Spielername> <Anzahl> <optional:Blockname>

        int count = 1;
        Player player = null;
        String[] remArgs = null;

        if(commandSender instanceof Player){
            if(((Player)commandSender).getGameMode() != GameMode.CREATIVE && !commandSender.isOp()){
                ((Player)commandSender).sendMessage(GaldreenBlocksUnlimited.createChatMessage("Befehl kann nur als OP oder im Kreativ ausgeführt werden."));
                return true;
            }
            player = ((Player) commandSender);
            remArgs = args;
        }else{
            if(args.length < 2){
                commandSender.sendMessage("Wenn diese Befehl nicht von einem Spieler gesendet wird sind die ersten beiden Argumente ein Spielername und die Anzahl an items.");
                return false;
            }
            player = Bukkit.getPlayerExact(args[0]);
            if(player == null){
                commandSender.sendMessage("Dieser Spieler wurde nicht gefunden!");
                return false;
            }
            try {
                count = Integer.parseInt(args[1]);
            }catch (NumberFormatException e){
                commandSender.sendMessage("Die angegebene Anzahl ist keine Zahl.");
                return false;
            }
            remArgs = Arrays.copyOfRange(args, 2,args.length);
        }



        if (remArgs.length >= 1){ //give specific item
            PlainTextComponentSerializer serializer = PlainTextComponentSerializer.plainText();
            for(CustomBlockCompound cbcmp: GaldreenBlocksUnlimited.goalToCompound.values()) {
                if (cbcmp.getItemToUse().getItemMeta().hasDisplayName()) {
                    if (serializer.serialize(cbcmp.getItemToUse().displayName()).equalsIgnoreCase("[" + String.join(" ",remArgs) + "]")) { // for some reason there are brackets around the itemname
                        ItemStack is = cbcmp.getItemToUse();
                        is.setAmount(count);
                        player.getWorld().dropItem(player.getLocation(), is);
                        return true;
                    }
                }

            }
            commandSender.sendMessage(GaldreenBlocksUnlimited.createChatMessage("Block nicht gefunden!"));
            return true;
        }else if (remArgs.length == 0){ //drop all items
            Set<ItemStack> items = new HashSet<>();
            for(CustomBlockCompound cbcmp: GaldreenBlocksUnlimited.goalToCompound.values()) {
                if (cbcmp.getItemToUse().getItemMeta().hasDisplayName()){
                    items.add(cbcmp.getItemToUse());
                }
            }
            for(ItemStack i : items){
                player.getWorld().dropItem(player.getLocation(), i);
            }
            return true;
        }


        return false;
    }
}
