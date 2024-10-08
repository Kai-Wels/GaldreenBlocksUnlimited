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
        // /addblacklistmaterial x y z
        if(!(commandSender instanceof Player)){
            return false;
        }
        Player player = (Player) commandSender;
        if(strings.length != 3){
            player.sendMessage(GaldreenBlocksUnlimited.createChatMessage("Nicht zur Blacklist hinzugefügt! Du musst eine Koordinate angeben!"));
            return false;
        }
        Location loc;
        try{
            loc = new Location(((Player) commandSender).getWorld(), Integer.parseInt(strings[0]), Integer.parseInt(strings[1]), Integer.parseInt(strings[2]));
        }
        catch (NumberFormatException e){
            player.sendMessage(GaldreenBlocksUnlimited.createChatMessage("Nicht zur Blacklist hinzugefügt! Eines der Argumente war keine Ganzzahl."));
            return false;
        }
        Material mat = player.getWorld().getBlockData(loc).getMaterial();

        if(mat == Material.AIR){
            player.sendMessage(GaldreenBlocksUnlimited.createChatMessage("Nicht zur Blacklist hinzugefügt! An der Koordinate war kein Block."));
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
            player.sendMessage(GaldreenBlocksUnlimited.createChatMessage("Block erfolgreich hinzugefügt!"));
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
