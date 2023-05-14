package commands;

import de.ewu2000.galdreenblocksunlimited.GaldreenBlocksUnlimited;
import events.BlockCanBuildEvent;
import net.kyori.adventure.text.TextComponent;
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

        if(commandSender instanceof Player){
            ItemStack itemInhand = ((Player)commandSender).getInventory().getItemInMainHand();
            BlockCanBuildEvent.alwaysPlaceable.add(itemInhand);

            int id = (new File(GaldreenBlocksUnlimited.dataFolder.getPath() + "/placeable")).listFiles().length;
            //write to file
            File itemstackFile = new File(GaldreenBlocksUnlimited.dataFolder.getPath() + "/placeable/" + id  + ".txt");
            try{
                itemstackFile.createNewFile();
                FileOutputStream oS = new FileOutputStream(itemstackFile);
                oS.write(itemInhand.serializeAsBytes());
                oS.close();
            } catch (FileNotFoundException e){
                e.printStackTrace();
                return false;
            } catch  (IOException e){
                e.printStackTrace();
                return false;
            }
        }



        return false;
    }
}
