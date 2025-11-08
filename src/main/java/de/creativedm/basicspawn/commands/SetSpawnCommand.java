package de.creativedm.basicspawn.commands;

import de.creativedm.basicspawn.BasicSpawn;
import de.creativedm.basicspawn.util.MessageUtil;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command Handler für /setspawn
 */
public class SetSpawnCommand implements CommandExecutor {

    private final BasicSpawn plugin;

    public SetSpawnCommand(BasicSpawn plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cDieser Befehl kann nur von Spielern ausgeführt werden!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("basicspawn.edit")) {
            String message = plugin.getConfig().getString("messages.no-permission", "&7Du hast keine &cRechte &7für den Befehl");
            MessageUtil.sendActionbar(player, message);
            return true;
        }

        Location location = player.getLocation();

        plugin.getConfig().set("spawn.world", location.getWorld().getName());
        plugin.getConfig().set("spawn.x", location.getX());
        plugin.getConfig().set("spawn.y", location.getY());
        plugin.getConfig().set("spawn.z", location.getZ());
        plugin.getConfig().set("spawn.yaw", location.getYaw());
        plugin.getConfig().set("spawn.pitch", location.getPitch());
        
        plugin.saveConfig();

        String message = plugin.getConfig().getString("messages.spawn-set", "&7Der &bSpawn&7 wurde erfolgreich gesetzt");
        MessageUtil.sendActionbar(player, message);

        return true;
    }
}
