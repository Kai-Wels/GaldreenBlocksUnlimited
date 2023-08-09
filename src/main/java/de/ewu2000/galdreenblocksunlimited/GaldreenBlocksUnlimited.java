package de.ewu2000.galdreenblocksunlimited;

import commands.*;
import events.*;
import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public final class GaldreenBlocksUnlimited extends JavaPlugin {
    public static ArrayList<CustomBlockCompound> allCustomBlockCompounds = new ArrayList<>();
    public static File dataFolder;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.logger.info("Registering Events");
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new BlockDestroyEvent(),this);
        manager.registerEvents(new BlockPlaceEvent(this),this);
        manager.registerEvents(new BlockBreakEvent(this),this);
        manager.registerEvents(new PlayerInteractEvent(),this);
        manager.registerEvents(new BlockCanBuildEvent(),this);

        this.logger.info("Registering Commands");
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
        this.logger.info("Loading Blocks");
        {


            //for every CustomBlock Compound
            List<File> subfolderlist = Arrays.asList(blockMainFolder.listFiles());
            subfolderlist.sort(Comparator.comparing(File::getName));
            for (File subfolder : subfolderlist) {
                this.logger.info("  - " + subfolder.getName());
                if (subfolder.isDirectory()) {
                    CustomBlockCompound cbcmp = new CustomBlockCompound();
                    //for every CustomBlockCyle
                    List<File> subsubfolderlist = Arrays.asList(subfolder.listFiles());
                    subsubfolderlist.sort(Comparator.comparing(File::getName));
                    for (File subsubFolder : subsubfolderlist) {
                        if (subsubFolder.isDirectory()) {
                            CustomBlockCycle cbc = new CustomBlockCycle();
                            CustomBlock[] blocks = new CustomBlock[subsubFolder.listFiles().length];
                            //for every CustomBlock
                            List<File> blockFolderList = Arrays.asList(subsubFolder.listFiles());
                            blockFolderList.sort(Comparator.comparing(File::getName));
                            for (File blockFolder : blockFolderList) {
                                int cbIndex = Integer.parseInt(blockFolder.getName().substring(5, blockFolder.getName().length()));
                                CustomBlock cb = new CustomBlock(getServer().createBlockData("minecraft:air"));
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
                                            cb = new CustomBlock(getServer().createBlockData(bdString));
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

                                cb.setPlaceData(new ArrayList<>(Arrays.asList(placeBlocks)));
                                blocks[cbIndex] = cb;

                            }
                            cbc.setCustomBlocks(new ArrayList<>(Arrays.asList(blocks)));
                            cbcmp.getBlockCyclesList().add(cbc);

                        } else if (subsubFolder.getName().equals("item.txt")) {
                            try {
                                InputStream is = new FileInputStream(subsubFolder);
                                byte[] cont = is.readAllBytes();
                                is.close();
                                ItemStack itemToUse = ItemStack.deserializeBytes(cont);
                                cbcmp.setItemToUse(itemToUse);

                                if(itemToUse.getItemMeta().lore() == null){
                                    cbcmp.setUpdatedByOtherBlocks(false);
                                }else{
                                    cbcmp.setUpdatedByOtherBlocks(true);
                                }
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
                    GaldreenBlocksUnlimited.allCustomBlockCompounds.add(cbcmp);
                }
            }
        }
        this.logger.info("Loading Placeables");
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
                        this.logger.info("  - " + itemToPlace.getType() + "  [" + placeableFile.getName()  + "]");
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

        this.logger.info("Loading Tools");
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
                        this.logger.info("  - loaded tool");
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
}
