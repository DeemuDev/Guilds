package me.deemu.guilds.Listeners;

import me.deemu.guilds.Guilds;
import me.deemu.guilds.GuildsManager.Manager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import net.md_5.bungee.api.event.ServerConnectEvent;

public class PlayerJoinListener implements Listener {
    public Guilds plugin = Guilds.getPlugin();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerConnect(ServerConnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        Manager.setupPlayerData(player);
        Manager.refreshPlayerData(player);
    }
}
