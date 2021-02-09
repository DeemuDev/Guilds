package me.deemu.guilds.GuildsManager;

import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PlayerGuildUtility {
    private ProxiedPlayer owner;

    public PlayerGuildUtility(ProxiedPlayer owner) {
        this.owner = owner;
    }

    public ProxiedPlayer getOwner() {
        return owner;
    }
}
