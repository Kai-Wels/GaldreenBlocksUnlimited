package commands;

import de.ewu2000.galdreenblocksunlimited.GaldreenBlocksUnlimited;
import net.kyori.adventure.text.Component;
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
import java.util.ArrayList;

public class AddTool implements CommandExecutor {

    public static ItemStack tool = null;
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            ItemStack itemInhand = ((Player)sender).getInventory().getItemInMainHand();
            if (itemInhand == null){
                return false;
            }
            tool = itemInhand;
            //write to file
            File itemstackFile = new File(GaldreenBlocksUnlimited.dataFolder.getPath() + "/tools/changeTool.txt");
            try{
                itemstackFile.createNewFile();
                FileOutputStream oS = new FileOutputStream(itemstackFile, false);
                oS.write(itemInhand.serializeAsBytes());
                oS.close();
                ((Player)sender).sendMessage(Component.newline().content("Tool added successfully!"));
                return true;
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
