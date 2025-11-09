package de.creativedm.basicspawn.listener;

import de.creativedm.basicspawn.BasicSpawn;
import de.creativedm.basicspawn.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerJoinListener implements Listener {

    private final BasicSpawn plugin;

    public PlayerJoinListener(BasicSpawn plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        if (!plugin.getConfig().getBoolean("teleport-on-join", true)) {
            return;
        }

        if (!plugin.getConfig().contains("spawn.world")) {
            if (player.hasPermission("basicspawn.edit")) {
                startAdminWarning(player);
            }
            return;
        }

        Location spawnLocation = getSpawnLocation();
        if (spawnLocation != null) {
            player.teleport(spawnLocation);
        }
    }

    private void startAdminWarning(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline() 
                    || plugin.getConfig().contains("spawn.world")
                    || !plugin.getConfig().getBoolean("teleport-on-join", true)) {
                    cancel();
                    return;
                }

                if (player.hasPermission("basicspawn.edit")) {
                    String warningMessage = plugin.getConfig().getString(
                        "messages.admin-no-spawn-warning", 
                        "&c&lWARNUNG: &cKein Spawn gesetzt! &7Nutze &e/setspawn"
                    );
                    MessageUtil.sendActionbar(player, warningMessage);
                }
            }
        }.runTaskTimer(plugin, 0L, 200L);
    }

    private Location getSpawnLocation() {
        try {
            String worldName = plugin.getConfig().getString("spawn.world");
            double x = plugin.getConfig().getDouble("spawn.x");
            double y = plugin.getConfig().getDouble("spawn.y");
            double z = plugin.getConfig().getDouble("spawn.z");
            float yaw = (float) plugin.getConfig().getDouble("spawn.yaw");
            float pitch = (float) plugin.getConfig().getDouble("spawn.pitch");

            return new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
        } catch (Exception e) {
            plugin.getLogger().severe("Fehler beim Laden der Spawn-Location: " + e.getMessage());
            return null;
        }
    }
}
