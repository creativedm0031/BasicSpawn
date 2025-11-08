package de.creativedm.basicspawn.util;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * Utility-Klasse für Partikel und Sound-Effekte (1.13-1.21 kompatibel)
 */
public class ParticleUtil {

    /**
     * Spielt Spiralpartikel um den Spieler herum (für Countdown)
     * Weiße Sternen-Partikel von oben nach unten
     * 
     * @param player Der Spieler
     * @param height Aktuelle Höhe der Spirale (0.0 = unten, 2.0 = oben)
     */
    public static void playCountdownParticles(Player player, double height) {
        Location loc = player.getLocation();
        
        for (int i = 0; i < 8; i++) {
            double angle = (System.currentTimeMillis() / 200.0 + i * Math.PI / 4) % (2 * Math.PI);
            double radius = 1.0;
            
            double x = loc.getX() + radius * Math.cos(angle);
            double y = loc.getY() + height;
            double z = loc.getZ() + radius * Math.sin(angle);
            
            Location particleLoc = new Location(loc.getWorld(), x, y, z);
            
            try {
                loc.getWorld().spawnParticle(
                    Particle.VILLAGER_HAPPY, 
                    particleLoc, 
                    1, 
                    0, 0, 0, 
                    0
                );
            } catch (Exception e) {
            }
        }
    }

    /**
     * Spielt Explosions-Partikel am Boden beim Spawn
     * 
     * @param location Die Location am Spawn
     */
    public static void playTeleportExplosion(Location location) {
        Location groundLoc = location.clone();
        groundLoc.setY(location.getY() + 0.1);
        
        for (int i = 0; i < 20; i++) {
            double angle = 2 * Math.PI * i / 20;
            double radius = 1.5;
            
            double x = groundLoc.getX() + radius * Math.cos(angle);
            double z = groundLoc.getZ() + radius * Math.sin(angle);
            
            Location particleLoc = new Location(groundLoc.getWorld(), x, groundLoc.getY(), z);
            
            try {
                location.getWorld().spawnParticle(
                    Particle.CLOUD,
                    particleLoc,
                    3,
                    0.1, 0.1, 0.1,
                    0.05
                );
            } catch (Exception e) {
            }
        }
        
        try {
            location.getWorld().spawnParticle(
                Particle.EXPLOSION_LARGE,
                groundLoc,
                1,
                0, 0, 0,
                0
            );
        } catch (Exception e) {
        }
    }

    /**
     * Spielt Enderperlen-Teleport Sound
     * 
     * @param player Der Spieler
     */
    public static void playTeleportSound(Player player) {
        try {
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
        } catch (Exception e) {
        }
    }
}
