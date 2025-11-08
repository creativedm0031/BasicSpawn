package de.creativedm.basicspawn.commands;

import de.creativedm.basicspawn.BasicSpawn;
import de.creativedm.basicspawn.util.MessageUtil;
import de.creativedm.basicspawn.util.ParticleUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Command Handler für /spawn
 */
public class SpawnCommand implements CommandExecutor {

    private final BasicSpawn plugin;

    public SpawnCommand(BasicSpawn plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cDieser Befehl kann nur von Spielern ausgeführt werden!");
            return true;
        }

        Player player = (Player) sender;

        if (!plugin.getConfig().contains("spawn.world")) {
            String message = plugin.getConfig().getString("messages.no-spawn-set", "&cEs ist kein Spawn gesetzt. Melde dich bei unserem Team");
            MessageUtil.sendActionbar(player, message);
            return true;
        }

        Location spawnLocation = getSpawnLocation();
        
        if (spawnLocation == null) {
            MessageUtil.sendActionbar(player, "&cFehler beim Laden des Spawns!");
            return true;
        }

        boolean instantTeleport = args.length > 0 && args[0].equalsIgnoreCase("instant");
        
        if (instantTeleport) {
            if (!player.hasPermission("basicspawn.instant")) {
                String message = plugin.getConfig().getString("messages.no-permission", "&7Du hast keine &cRechte &7für den Befehl");
                MessageUtil.sendActionbar(player, message);
                return true;
            }
            
            player.teleport(spawnLocation);
            
            boolean teleportParticles = plugin.getConfig().getBoolean("effects.teleport-particles", true);
            boolean teleportSound = plugin.getConfig().getBoolean("effects.teleport-sound", true);
            
            if (teleportParticles) {
                ParticleUtil.playTeleportExplosion(spawnLocation);
            }
            if (teleportSound) {
                ParticleUtil.playTeleportSound(player);
            }
            
            String instantMessage = plugin.getConfig().getString("messages.teleport-instant", "&7Du wurdest &asofort&7 zum &bSpawn&7 teleportiert");
            MessageUtil.sendActionbar(player, instantMessage);
            
            return true;
        }

        int countdownSeconds = plugin.getConfig().getInt("countdown", 3);
        String countdownMessage = plugin.getConfig().getString("messages.teleport-countdown", "&7Du wirst in &3%time%&7 Sekunden zum &bSpawn&7 teleportiert");
        
        boolean countdownParticles = plugin.getConfig().getBoolean("effects.countdown-particles", true);
        boolean teleportParticles = plugin.getConfig().getBoolean("effects.teleport-particles", true);
        boolean teleportSound = plugin.getConfig().getBoolean("effects.teleport-sound", true);

        new BukkitRunnable() {
            int timeLeft = countdownSeconds;
            double particleHeight = 2.0;

            @Override
            public void run() {
                if (timeLeft <= 0) {
                    player.teleport(spawnLocation);
                    
                    if (teleportParticles) {
                        ParticleUtil.playTeleportExplosion(spawnLocation);
                    }
                    if (teleportSound) {
                        ParticleUtil.playTeleportSound(player);
                    }
                    
                    String successMessage = plugin.getConfig().getString("messages.teleport-success", "&7Du wurdest zum &bSpawn&7 teleportiert");
                    MessageUtil.sendActionbar(player, successMessage);
                    
                    cancel();
                    return;
                }

                String message = MessageUtil.replaceVariables(countdownMessage, player, timeLeft);
                MessageUtil.sendActionbar(player, message);
                
                if (countdownParticles) {
                    ParticleUtil.playCountdownParticles(player, particleHeight);
                    particleHeight -= (2.0 / (countdownSeconds * 20.0 / 4.0));
                    if (particleHeight < 0) particleHeight = 2.0;
                }

                timeLeft--;
            }
        }.runTaskTimer(plugin, 0L, 4L);

        return true;
    }

    /**
     * Lädt die Spawn Location aus der Config
     */
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
