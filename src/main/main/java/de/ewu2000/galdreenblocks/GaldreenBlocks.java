package de.ewu2000.galdreenblocks;


import events.BreakEvent;
import events.PlaceEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.FaceAttachable;
import org.bukkit.block.data.type.TechnicalPiston;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class GaldreenBlocks extends JavaPlugin {

    private static File customConfigFile;
    private static FileConfiguration customConfig;

    @Override
    public void onEnable(){



        getLogger().info("Plugin Loaded");
        getCommand("GaldreenBlocksGive").setExecutor(new GiveAllGaldreenBlocks());
        //getCommand("TestCommand").setExecutor(new TestCommand());

        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new PlaceEvent(),this);
        manager.registerEvents(new BreakEvent(),this);



        createCustomConfig();
        //buttons einelesen
        System.out.println("Buttons:");
        int i = 0;
        for (String rsString : customConfig.getConfigurationSection("buttons").getKeys(false)){
            System.out.print(rsString);
            System.out.print("-" + customConfig.getString("buttons." + rsString + ".name"));
            System.out.println("-" + customConfig.getString("buttons." + rsString + ".material"));
            try{
                ChatColor color = ChatColor.valueOf(customConfig.getString("buttons." + rsString + ".color"));
                ButtonRotationState.allButtonRotationStates.add(new ButtonRotationState(Material.getMaterial(customConfig.getString("buttons." + rsString + ".material")),  color + customConfig.getString("buttons." + rsString + ".name")));
            }
            catch(IllegalArgumentException e){
                throw new IllegalArgumentException("Color of object " + rsString + " doesn't exist!");
            }


            for(String rtString: customConfig.getConfigurationSection("buttons." + rsString + ".transitions").getKeys(false)){

                try{
                    BlockFace fromBF = customConfig.getString("buttons." + rsString + ".transitions." + rtString + ".fromBF")==null?null:BlockFace.valueOf(customConfig.getString("buttons." + rsString + ".transitions." + rtString + ".fromBF"));
                    BlockFace toBF = customConfig.getString("buttons." + rsString + ".transitions." + rtString + ".toBF")==null?null:BlockFace.valueOf(customConfig.getString("buttons." + rsString + ".transitions." + rtString + ".toBF"));
                    FaceAttachable.AttachedFace fromAF = customConfig.getString("buttons." + rsString + ".transitions." + rtString + ".fromAF")==null?null: FaceAttachable.AttachedFace.valueOf(customConfig.getString("buttons." + rsString + ".transitions." + rtString + ".fromAF"));
                    FaceAttachable.AttachedFace toAF = customConfig.getString("buttons." + rsString + ".transitions." + rtString + ".toAF")==null?null: FaceAttachable.AttachedFace.valueOf(customConfig.getString("buttons." + rsString + ".transitions." + rtString + ".toAF"));
                    ButtonRotationState.allButtonRotationStates.get(i).getTransitions().add(new ButtonRotationStateTransition(fromBF,toBF,fromAF,toAF));
                }
                catch (IllegalArgumentException e){
                    throw new IllegalArgumentException("Input mistake in buttons " + rsString + ", transition " + rtString + "!" );
                }
            }
            i++;
        }
        //Pistons einlesen
        System.out.println("Pistons:");
        i = 0;
        for (String rsString : customConfig.getConfigurationSection("pistons").getKeys(false)){
            System.out.print(rsString);
            System.out.print("-" + customConfig.getString("pistons." + rsString + ".name"));
            System.out.println("-" + customConfig.getString("pistons." + rsString + ".materialToPlace"));
            try{
                ChatColor color = ChatColor.valueOf(customConfig.getString("pistons." + rsString + ".color"));
                PistonRotationState.allPistonRotationStates.add(new PistonRotationState(Material.getMaterial(customConfig.getString("pistons." + rsString + ".materialToPlace")),Material.getMaterial(customConfig.getString("pistons." + rsString + ".materialOfModel")),  color + customConfig.getString("pistons." + rsString + ".name"), TechnicalPiston.Type.valueOf(customConfig.getString("pistons."+rsString+".type")),Boolean.valueOf(customConfig.getString("pistons."+rsString+".short"))));
            }
            catch(IllegalArgumentException e){
                throw new IllegalArgumentException("Input at piston " + rsString + " is wrong!");
            }


            for(String rtString: customConfig.getConfigurationSection("pistons." + rsString + ".transitions").getKeys(false)){

                try{
                    BlockFace fromBF = customConfig.getString("pistons." + rsString + ".transitions." + rtString + ".fromBF")==null?null:BlockFace.valueOf(customConfig.getString("pistons." + rsString + ".transitions." + rtString + ".fromBF"));
                    BlockFace toBF = customConfig.getString("pistons." + rsString + ".transitions." + rtString + ".toBF")==null?null:BlockFace.valueOf(customConfig.getString("pistons." + rsString + ".transitions." + rtString + ".toBF"));
                    PistonRotationState.allPistonRotationStates.get(i).getTransitions().add(new PistonRotationStateTransition(fromBF,toBF));
                }
                catch (IllegalArgumentException e){
                    throw new IllegalArgumentException("Input mistake in pistons " + rsString + ", transition " + rtString + "!" );
                }
            }
            i++;
        }
        System.out.println("Pressure Plates:");
        //PressurePlates einlesen
        for (String rsString : customConfig.getConfigurationSection("pressure_plates").getKeys(false)){
            System.out.print(rsString);
            System.out.print("-" + customConfig.getString("pressure_plates." + rsString + ".name"));
            System.out.println("-" + customConfig.getString("pressure_plates." + rsString + ".material"));
            try{
                ChatColor color = ChatColor.valueOf(customConfig.getString("pressure_plates." + rsString + ".color"));
                PlateState.allPlateStates.add(new PlateState(color + customConfig.getString("pressure_plates." + rsString + ".name"),Material.getMaterial(customConfig.getString("pressure_plates." + rsString + ".material")), Integer.parseInt(customConfig.getString("pressure_plates." + rsString + ".power"))));
            }
            catch(IllegalArgumentException e){
                throw new IllegalArgumentException("Input at plate " + rsString + " is wrong!");
            }
        }


        System.out.println();
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled");
    }

    public static FileConfiguration getCustomConfig() {
        return customConfig;
    }

    private void createCustomConfig() {
        customConfigFile = new File(getDataFolder(), "config.yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }

        customConfig= new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }


}

