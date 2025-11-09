package de.creativedm.basicspawn.util;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class MessageUtil {

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

    public static String colorize(String text) {
        if (text == null) return "";
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static String replaceVariables(String text, Player player, int countdown) {
        return text
            .replace("%player%", player.getName())
            .replace("%time%", String.valueOf(countdown));
    }
}
