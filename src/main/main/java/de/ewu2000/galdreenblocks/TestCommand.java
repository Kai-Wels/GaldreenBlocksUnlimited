package de.ewu2000.galdreenblocks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        int x = 0;
        int y = 0;
        int z = 0;
        if(strings.length == 3){
            try{
                x = Integer.parseInt(strings[0]);
                y = Integer.parseInt(strings[1]);
                z = Integer.parseInt(strings[2]);

                Location l = new Location(((Player)commandSender).getWorld(),x,y,z);
                ((Player)commandSender).sendMessage(l.getBlock().getBlockData().toString());
                l.getBlock().setType(Material.PISTON,true);
                //l.getBlock().setBlockData(CraftBlockData.newData(Material.PISTON,"[extended=true,facing=west]"),false );
            }
            catch (NumberFormatException e){
                ((Player)commandSender).sendMessage("Es wurden nicht 3 Integer angegeben!");

            }
        }
        else{
            ((Player)commandSender).sendMessage("Es wurden nicht 3 Argumente abgegeben!");
        }
        return false;
    }
}
