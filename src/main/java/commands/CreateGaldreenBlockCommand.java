package commands;

import de.ewu2000.galdreenblocksunlimited.CustomBlock;
import de.ewu2000.galdreenblocksunlimited.CustomBlockCompound;
import de.ewu2000.galdreenblocksunlimited.CustomBlockCycle;
import de.ewu2000.galdreenblocksunlimited.GaldreenBlocksUnlimited;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.apache.maven.model.Plugin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.FileUtil;
import org.codehaus.plexus.util.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;

public class CreateGaldreenBlockCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        // /createGaldreenBlock x y z bool name

        //Layout
        if (commandSender instanceof Player && ((Player) commandSender).getInventory().getItemInMainHand().getType() != Material.AIR) {
            if (args.length >= 5) {
                //read item name
                String itemName = "";
                for (int i = 4; i < args.length; i++) {
                    itemName += args[i];
                    if (i != args.length - 1) {
                        itemName += " ";
                    }
                }
                Location startLocation;
                try {
                    startLocation = new Location(((Player) commandSender).getWorld(), Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
                } catch (NumberFormatException e) {
                    return false;
                }

                //create Itemstack
                ItemStack is = ((Player) commandSender).getInventory().getItemInMainHand().clone();
                if(args[3].equals("True")){
                    ItemMeta im = is.getItemMeta();
                    im.displayName(Component.newline().content(itemName));
                    is.setAmount(1);
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add("Galdreen Block");
                    im.setLore(lore);
                    is.setItemMeta(im);
                }else if (!args[3].equals("False")){
                    ((Player) commandSender).sendMessage("Error in boolean!");
                    return false;
                }



                int stop = 0;
                int maxStop = 300;
                CustomBlockCompound cbcmp = new CustomBlockCompound(is);
                Location cycleLocation = startLocation.clone();
                while (cycleLocation.getBlock().getType() != Material.AIR && stop < maxStop) {
                    CustomBlockCycle cbc = new CustomBlockCycle();
                    Location inCycleLocation = cycleLocation.clone();
                    while (inCycleLocation.getBlock().getType() != Material.AIR  && stop < maxStop) {
                        CustomBlock cb = new CustomBlock(inCycleLocation.getBlock().getBlockData());
                        Location inBlockDataLocation = inCycleLocation.clone();
                        inBlockDataLocation.add(2, 0, 0);
                        while (inBlockDataLocation.getBlock().getType() != Material.AIR  && stop < maxStop) {
                            cb.getPlaceData().add(inBlockDataLocation.getBlock().getBlockData());
                            inBlockDataLocation.add(2,0,0);
                            stop++;
                        }
                        cbc.getCustomBlocks().add(cb);
                        inCycleLocation.add(0, 2, 0);
                        System.out.println(inCycleLocation.toString());
                        stop++;
                    }
                    cbcmp.getBlockCyclesList().add(cbc);
                    cycleLocation.add(0, 0, 2);
                    stop++;
                }



                GaldreenBlocksUnlimited.allCustomBlockCompounds.add(cbcmp);

                //write to file
                File compFolder = new File(GaldreenBlocksUnlimited.dataFolder.getPath() + "/customBlocks/" + itemName);
                try{
                    if(compFolder.exists()){
                        FileUtils.deleteDirectory(compFolder);
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
                compFolder.mkdir();

                File itemstackFile = new File(compFolder.getPath() + "/item.txt");
                try{
                    itemstackFile.createNewFile();
                    FileOutputStream oS = new FileOutputStream(itemstackFile);
                    oS.write(cbcmp.getItemToUse().serializeAsBytes());
                    oS.close();
                } catch (FileNotFoundException e){
                    e.printStackTrace();
                    return false;
                } catch  (IOException e){
                    e.printStackTrace();
                    return false;
                }

                int cycleCount= 0;
                for (CustomBlockCycle cbc: cbcmp.getBlockCyclesList()){
                    File cycleFolder = new File(compFolder.getPath() + "/Cycle" +cycleCount);
                    cycleFolder.mkdir();



                    int blockCount = 0;
                    for(CustomBlock cb: cbc.getCustomBlocks()){
                        File blockFolder = new File(cycleFolder.getPath() + "/Block" + blockCount);
                        blockFolder.mkdir();

                        File goalBlockData = new File(blockFolder.getPath() + "/goal.txt");
                        try {
                            goalBlockData.createNewFile();
                            FileWriter fileWriter = new FileWriter(goalBlockData.getPath());
                            fileWriter.write(cb.getGoalData().getAsString());
                            fileWriter.close();

                        }catch (IOException e){
                            e.printStackTrace();
                            return false;
                        }
                        int placeCount = 0;
                        for(BlockData bd : cb.getPlaceData()){
                            File placeBlockData = new File(blockFolder.getPath() + "/place" + placeCount + ".txt");
                            try {
                                placeBlockData.createNewFile();
                                FileWriter fileWriter = new FileWriter(placeBlockData.getPath());
                                fileWriter.write(bd.getAsString());
                                fileWriter.close();

                            }catch (IOException e){
                                e.printStackTrace();
                                return false;
                            }
                            placeCount++;
                        }

                        blockCount++;
                    }


                    cycleCount++;
                }

                return true;

            }

        }




        return false;
    }
}
