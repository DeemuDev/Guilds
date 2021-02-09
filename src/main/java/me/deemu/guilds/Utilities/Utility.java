package me.deemu.guilds.Utilities;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;


public class Utility {
    public static void sendMessage(ProxiedPlayer player, String message){
        player.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', message)));
    }
}
