package commands;

import de.ewu2000.galdreenblocksunlimited.GaldreenBlocksUnlimited;
import events.BlockCanBuildEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
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
import java.nio.charset.StandardCharsets;

public class AddBlacklistMaterial implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        // /addPlaceable x y z
        if(!(commandSender instanceof Player)){
            return false;
        }
        if(strings.length != 3){
            ((Player)commandSender).sendMessage(Component.newline().content("Blacklistitem not added! You must specify a location!"));
            return false;
        }
        Location loc;
        try{
            loc = new Location(((Player) commandSender).getWorld(), Integer.parseInt(strings[0]), Integer.parseInt(strings[1]), Integer.parseInt(strings[2]));
        }
        catch (NumberFormatException e){
            ((Player)commandSender).sendMessage(Component.newline().content("Blacklistitem not added! YOne of the arguments wasn't an integer"));
            return false;
        }
        Material mat = ((Player)commandSender).getWorld().getBlockData(loc).getMaterial();

        if(mat == Material.AIR){
            ((Player)commandSender).sendMessage(Component.newline().content("BlackListMaterial not added! No block at coords."));
            return false;
        }

        GaldreenBlocksUnlimited.blackListDebugStick.add(mat);

        //write to file
        File itemstackFile = new File(GaldreenBlocksUnlimited.dataFolder.getPath() + "/debugStick/" + mat.toString() + ".txt");
        try{
            itemstackFile.createNewFile();
            FileOutputStream oS = new FileOutputStream(itemstackFile);
            oS.write(mat.toString().getBytes(StandardCharsets.UTF_8));
            oS.close();
            ((Player)commandSender).sendMessage(Component.newline().content("Blacklistitem added successfully!"));
            return true;
        } catch (FileNotFoundException e){
            e.printStackTrace();
            return false;
        } catch  (IOException e){
            e.printStackTrace();
            return false;
        }

    }
}
