package de.ewu2000.galdreenblocksunlimited;

import commands.CreateGaldreenBlockCommand;
import commands.GiveAllGaldreenBlocksCommand;
import events.BlockBreakEvent;
import events.BlockDestroyEvent;
import events.BlockPlaceEvent;
import events.PlayerInteractEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;

public final class GaldreenBlocksUnlimited extends JavaPlugin {

    public static ArrayList<CustomBlockCompound> allCustomBlockCompounds = new ArrayList<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.logger.log(Level.FINE,"Registering Events");
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new BlockDestroyEvent(),this);
        manager.registerEvents(new BlockPlaceEvent(),this);
        manager.registerEvents(new BlockBreakEvent(),this);
        manager.registerEvents(new PlayerInteractEvent(),this);

        this.logger.log(Level.FINE,"Registering Commands");
        getCommand("createGaldreenBlock").setExecutor(new CreateGaldreenBlockCommand());
        getCommand("giveAllGaldreenBlocks").setExecutor(new GiveAllGaldreenBlocksCommand());

        this.logger.log(Level.FINE,"Loading stored Galdreen Blocks");
        File file = this.getDataFolder();
        file.mkdir();

        


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        this.logger.log(Level.FINE,"Plugin stopped ...");
    }
}
