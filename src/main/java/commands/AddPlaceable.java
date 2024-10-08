package commands;

import de.ewu2000.galdreenblocksunlimited.GaldreenBlocksUnlimited;
import events.BlockCanBuildEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AddPlaceable implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        // /addPlaceable

        if(!(commandSender instanceof Player)) {
            return false;
        }
        Player player = (Player) commandSender;

        ItemStack itemInhand = player.getInventory().getItemInMainHand();

        if(itemInhand.getType().equals(Material.AIR)){
            player.sendMessage(GaldreenBlocksUnlimited.createChatMessage("Halte den gewünschten Block in der Hand."));
            return true;
        }

        BlockCanBuildEvent.alwaysPlaceable.add(itemInhand);

        //write to file
        File itemstackFile = new File(GaldreenBlocksUnlimited.dataFolder.getPath() + "/placeable/" + itemInhand.getType().toString() + itemInhand.hashCode()  + ".txt");
        try{
            itemstackFile.createNewFile();
            FileOutputStream oS = new FileOutputStream(itemstackFile);
            oS.write(itemInhand.serializeAsBytes());
            oS.close();
            player.sendMessage(GaldreenBlocksUnlimited.createChatMessage("Placeable added successfully!"));
            return true;
        } catch (FileNotFoundException e){
            e.printStackTrace();
            player.sendMessage(GaldreenBlocksUnlimited.createChatMessage("Ein Fehler ist aufgetreten: Datei nicht gefunden."));
            return true;
        } catch  (IOException e){
            e.printStackTrace();
            player.sendMessage(GaldreenBlocksUnlimited.createChatMessage("Ein Fehler ist aufgetreten: IOException"));
            return true;
        }
    }
}
