package de.creativedm.basicspawn;

import de.creativedm.basicspawn.commands.SetSpawnCommand;
import de.creativedm.basicspawn.commands.SpawnCommand;
import de.creativedm.basicspawn.listener.PlayerJoinListener;
import org.bukkit.plugin.java.JavaPlugin;

public class BasicSpawn extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("BasicSpawn v1.1 wurde aktiviert!");
        
        saveDefaultConfig();
        
        getCommand("setspawn").setExecutor(new SetSpawnCommand(this));
        getCommand("spawn").setExecutor(new SpawnCommand(this));
        
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("BasicSpawn v1.1 wurde deaktiviert!");
    }
}
