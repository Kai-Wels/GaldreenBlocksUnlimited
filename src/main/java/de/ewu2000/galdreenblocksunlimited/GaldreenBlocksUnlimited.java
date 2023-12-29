package de.ewu2000.galdreenblocksunlimited;

import commands.*;
import events.*;
import it.unimi.dsi.fastutil.Hash;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.*;

public final class GaldreenBlocksUnlimited extends JavaPlugin {

    //rework structure
    public static HashMap<String,CustomBlockCompound> goalToCompound = new HashMap<>();
    public static HashMap<String,BlockData> itemAndPlaceToGoal = new HashMap<>();
    public static HashMap<String,BlockData> cycles = new HashMap<>();

    public static File dataFolder;

    public static Server server;

    @Override
    public void onEnable() {
        server = getServer();
        // Plugin startup logic
        this.getLogger().info("Registering Events");
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new BlockDestroyEvent(),this);
        manager.registerEvents(new BlockPlaceEvent(this),this);
        manager.registerEvents(new BlockBreakEvent(this),this);
        manager.registerEvents(new PlayerInteractEvent(),this);
        manager.registerEvents(new BlockCanBuildEvent(),this);

        this.getLogger().info("Registering Commands");
        getCommand("createGaldreenBlock").setExecutor(new CreateGaldreenBlockCommand());
        getCommand("giveGaldreenBlocks").setExecutor(new GiveGaldreenBlocksCommand());
        getCommand("giveGaldreenBlocks").setTabCompleter(new TabCompleterGiveGaldreenBlocks());
        getCommand("addPlaceable").setExecutor(new AddPlaceable());
        getCommand("addTool").setExecutor(new AddTool());
        getCommand("giveGaldreenTool").setExecutor(new GiveGaldreenTool());


        dataFolder = this.getDataFolder();
        dataFolder.mkdir();

        File blockMainFolder = new File(dataFolder.getPath() + "/customBlocks");
        if (!blockMainFolder.exists()) {
            blockMainFolder.mkdir();
        }
        File placeableFolder = new File(dataFolder.getPath() + "/placeable");
        if (!placeableFolder.exists()) {
            placeableFolder.mkdir();
        }
        File toolsFolder = new File(dataFolder.getPath() + "/tools");
        if (!toolsFolder.exists()) {
            toolsFolder.mkdir();
        }
        this.getLogger().info("Loading Blocks");
        {


            //for every CustomBlock Compound
            List<File> subfolderlist = Arrays.asList(blockMainFolder.listFiles());
            subfolderlist.sort(Comparator.comparing(File::getName));
            for (File subfolder : subfolderlist) {
                this.getLogger().info("  - " + subfolder.getName());
                if (subfolder.isDirectory()) {
                    CustomBlockCompound cbcmp = new CustomBlockCompound();

                    try {
                        File itemFile = new File(subfolder, "item.txt");
                        InputStream is = new FileInputStream(itemFile);
                        byte[] cont = is.readAllBytes();
                        is.close();
                        ItemStack itemToUse = ItemStack.deserializeBytes(cont);
                        cbcmp.setItemToUse(itemToUse);

                        if(itemToUse.getItemMeta().lore() == null){
                            cbcmp.setUpdatedByOtherBlocks(false);
                        }else{
                            cbcmp.setUpdatedByOtherBlocks(true);
                        }
                        this.getLogger().info("Found Item without error");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                        return;
                    }

                    //for every CustomBlockCyle
                    List<File> subsubfolderlist = Arrays.asList(subfolder.listFiles());
                    subsubfolderlist.sort(Comparator.comparing(File::getName));
                    for (File subsubFolder : subsubfolderlist) {
                        if (subsubFolder.isDirectory()) {
                            ArrayList<BlockData> cylce = new ArrayList<>();
                            //for every CustomBlock
                            List<File> blockFolderList = Arrays.asList(subsubFolder.listFiles());
                            blockFolderList.sort(Comparator.comparing(File::getName));
                            for (File blockFolder : blockFolderList) {
                                BlockData goal = getServer().createBlockData("minecraft:air");
                                BlockData[] placeBlocks = new BlockData[blockFolder.listFiles().length - 1];
                                //for every Blockdata of a CustomBlock
                                List<File> blockFileList = Arrays.asList(blockFolder.listFiles());
                                blockFileList.sort(Comparator.comparing(File::getName));
                                for (File blockFile : blockFileList) {

                                    if (blockFile.isFile() && blockFile.getName().equals("goal.txt")) {
                                        try {
                                            InputStream is = new FileInputStream(blockFile);
                                            String bdString = "";
                                            int cont;
                                            while ((cont = is.read()) != -1) {
                                                bdString += (char) cont;
                                            }
                                            is.close();
                                            goalToCompound.put(bdString,cbcmp);
                                            goal = getServer().createBlockData(bdString);
                                            cylce.add(goal);
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                            return;
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                            return;
                                        } catch (OutOfMemoryError e) {
                                            e.printStackTrace();
                                            return;
                                        } catch (IllegalArgumentException e) {
                                            e.printStackTrace();
                                            return;
                                        }
                                    } else if (blockFile.isFile() && blockFile.getName().contains("place")) {

                                        try {
                                            int index = Integer.parseInt(blockFile.getName().substring(5, blockFile.getName().length() - 4));
                                            InputStream is = new FileInputStream(blockFile);
                                            String bdString = "";
                                            int cont;
                                            while ((cont = is.read()) != -1) {
                                                bdString += (char) cont;
                                            }
                                            is.close();
                                            placeBlocks[index] = getServer().createBlockData(bdString);
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                            return;
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                            return;
                                        } catch (OutOfMemoryError e) {
                                            e.printStackTrace();
                                            return;
                                        } catch (NumberFormatException e) {
                                            e.printStackTrace();
                                            return;
                                        } catch (IndexOutOfBoundsException e) {
                                            e.printStackTrace();
                                            return;
                                        }
                                    }
                                }
                                for(BlockData bd : placeBlocks){
                                    itemAndPlaceToGoal.put(cbcmp.getItemToUse().toString() + bd.toString(),goal);
                                }


                            }

                            this.getLogger().info("CycleCount:" + cylce.size());
                            BlockData lastData = cylce.get(cylce.size()-1);
                            for(BlockData bd : cylce){
                                cycles.put(lastData.toString(),bd);
                                lastData = bd;
                            }

                        }
                    }
                }
                this.getLogger().info(" -> Loaded gTC: " + goalToCompound.size() + ", iAPG:" + itemAndPlaceToGoal.size()+", cycles:" + cycles.size());
                this.getLogger().info(" -> gTC: " + goalToCompound.keySet());
            }
        }
        this.getLogger().info("Loading Placeables");
        {
            List<File> placeableFilelist = Arrays.asList(placeableFolder.listFiles());
            placeableFilelist.sort(Comparator.comparing(File::getName));
            for (File placeableFile : placeableFilelist) {
                if(placeableFile.isFile()){
                    try {
                        InputStream is = new FileInputStream(placeableFile);
                        byte[] cont = is.readAllBytes();
                        is.close();
                        ItemStack itemToPlace = ItemStack.deserializeBytes(cont);
                        BlockCanBuildEvent.alwaysPlaceable.add(itemToPlace);
                        this.getLogger().info("  - " + itemToPlace.getType() + "  [" + placeableFile.getName()  + "]");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        }

        this.getLogger().info("Loading Tools");
        {
            List<File> toolsList = Arrays.asList(toolsFolder.listFiles());
            toolsList.sort(Comparator.comparing(File::getName));
            for ( File f : toolsList){
                if (f.getName().equals("changeTool.txt")){
                    try {
                        InputStream is = new FileInputStream(f);
                        byte[] cont = is.readAllBytes();
                        is.close();
                        ItemStack tool = ItemStack.deserializeBytes(cont);
                        AddTool.tool = tool;
                        this.getLogger().info("  - loaded tool");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    public static String removeCraftBlock(String blockData){
        return blockData.substring(15,blockData.length()-1);
    }
}
