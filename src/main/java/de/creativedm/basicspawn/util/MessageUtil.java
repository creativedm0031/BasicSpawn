package de.creativedm.basicspawn.util;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Utility-Klasse für Nachrichten mit voller 1.8-1.21 Kompatibilität
 */
public class MessageUtil {

    /**
     * Sendet eine Actionbar-Nachricht an einen Spieler (funktioniert für 1.8-1.21)
     * 
     * @param player Der Spieler
     * @param message Die Nachricht (mit & Color Codes)
     */
    public static void sendActionbar(Player player, String message) {
        String colored = colorize(message);
        
        try {
            player.spigot().sendMessage(
                net.md_5.bungee.api.ChatMessageType.ACTION_BAR,
                net.md_5.bungee.api.chat.TextComponent.fromLegacyText(colored)
            );
        } catch (Exception | NoSuchMethodError e) {
            sendActionbarLegacy(player, colored);
        }
    }

    /**
     * Legacy Actionbar für 1.8-1.10 via Reflection
     */
    private static void sendActionbarLegacy(Player player, String message) {
        try {
            String version = player.getClass().getPackage().getName().split("\\.")[3];
            
            Class<?> chatComponentText = Class.forName("net.minecraft.server." + version + ".ChatComponentText");
            Class<?> packetPlayOutChat = Class.forName("net.minecraft.server." + version + ".PacketPlayOutChat");
            Class<?> iChatBaseComponent = Class.forName("net.minecraft.server." + version + ".IChatBaseComponent");
            
            Constructor<?> chatConstructor = chatComponentText.getConstructor(String.class);
            Object chatComponent = chatConstructor.newInstance(message);
            
            Constructor<?> packetConstructor = packetPlayOutChat.getConstructor(iChatBaseComponent, byte.class);
            Object packet = packetConstructor.newInstance(chatComponent, (byte) 2);
            
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            Method sendPacket = playerConnection.getClass().getMethod("sendPacket", Class.forName("net.minecraft.server." + version + ".Packet"));
            sendPacket.invoke(playerConnection, packet);
            
        } catch (Exception ex) {
            player.sendMessage(message);
        }
    }

    /**
     * Konvertiert & Color Codes zu § Minecraft Codes
     * 
     * @param text Text mit & Codes
     * @return Text mit § Codes
     */
    public static String colorize(String text) {
        if (text == null) return "";
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    /**
     * Ersetzt Variablen in einem Text
     * 
     * @param text Der Text
     * @param player Der Spieler (für %player%)
     * @param countdown Der Countdown (für %time%)
     * @return Text mit ersetzten Variablen
     */
    public static String replaceVariables(String text, Player player, int countdown) {
        return text
            .replace("%player%", player.getName())
            .replace("%time%", String.valueOf(countdown));
    }
}
