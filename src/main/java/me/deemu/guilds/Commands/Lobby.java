package me.deemu.guilds.Commands;

import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Command;

import java.net.ConnectException;

public class Lobby extends Command {
    public Lobby() {
        super("lobby");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if (player.getServer().getInfo().getName().equalsIgnoreCase("Lobby")) {
                player.sendMessage(new TextComponent(ChatColor.RED + "You are already connected to this server!"));
                return;
            }
            ServerInfo target = ProxyServer.getInstance().getServerInfo("Lobby");
            Callback cb = new Callback() {
                @Override
                public void done(Object result, Throwable error) {
                    player.sendMessage(new TextComponent(""));
                    player.sendMessage(new TextComponent(ChatColor.GREEN + "You were sent to the lobby."));
                }
            };
            player.connect(target, cb, ServerConnectEvent.Reason.COMMAND);
        } else {
            sender.sendMessage(new TextComponent(ChatColor.RED + "Only players can execute this!"));
        }
    }
}
