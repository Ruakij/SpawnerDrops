package eu.railduction.ruakij.spawnerDrops;

import eu.railduction.ruakij.spawnerDrops.listener.OnBlockBreak;
import eu.railduction.ruakij.spawnerDrops.listener.OnBlockPlace;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {

    public static Plugin plugin;

    public static FileConfiguration config;
    public static FileConfiguration data;

    public void onEnable() {

        plugin = this;

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new OnBlockPlace(), this);
        pluginManager.registerEvents(new OnBlockBreak(), this);

        loadConfigs();

        getLogger().info("Plugin activated");
    }

    public void onDisable() {

        getLogger().info("Plugin deactivated");
    }

    public void loadConfigs() {
        this.saveDefaultConfig();
        config = this.getConfig();

        try{
            File dataFile = new File("plugins/SpawnerDrops/data.yml");
            dataFile.createNewFile();
            data = YamlConfiguration.loadConfiguration(
                    dataFile
            );
        }catch (Exception ex){
            getLogger().severe("Could not load/create data.yml!");
            ex.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    public static String serialiseLocation(Location loc){
        return loc.getWorld().getName()+";"+loc.getBlockX()+";"+loc.getBlockY()+";"+loc.getBlockZ();
    }
}
