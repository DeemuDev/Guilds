package me.deemu.guilds.GuildsManager;

import me.deemu.guilds.Guilds;
import me.deemu.guilds.Utilities.Utility;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Manager {
    Guilds plugin;


    public static void createGuild(ProxiedPlayer owner, String name) {
        Guilds plugin = Guilds.getPlugin();
        if (!(name.contains("-") || name.contains("!") || name.contains("?"))) {
            if (plugin.getData().getBoolean("data.players." + owner.getUniqueId().toString() + ".in-guild")) {
                Utility.sendMessage(owner, "&cYou are already in a guild!");
                return;
            }
            // role 1 = member
            // role 2 = owner
            List<String> members = new ArrayList<>();
            members.add(owner.getUniqueId().toString());
            plugin.getData().set("data.guilds." + name + ".name", name);
            plugin.getData().set("data.guilds." + name + ".owner", owner.getUniqueId().toString());
            plugin.getData().set("data.guilds." + name + ".members", members);

            Guilds.guild.put(owner.getUniqueId(), name);
            Guilds.role.put(owner.getUniqueId(), 2);

            plugin.getData().set("data.players." + owner.getUniqueId().toString() + ".name", owner.getName());
            plugin.getData().set("data.players." + owner.getUniqueId().toString() + ".in-guild", true);
            plugin.getData().set("data.players." + owner.getUniqueId().toString() + ".guild-name", Guilds.guild.get(owner.getUniqueId()));
            plugin.getData().set("data.players." + owner.getUniqueId().toString() + ".guild-role", Guilds.role.get(owner.getUniqueId()));
            plugin.saveData();
        } else {
            owner.sendMessage(new TextComponent("§cThe guild name is invalid!"));
        }
    }

    public static void sendGuildRequest(ProxiedPlayer fromPlayer, String toGuild) {
        Guilds plugin = Guilds.getPlugin();
        if (plugin.getData().getBoolean("data.players." + fromPlayer.getUniqueId().toString() + ".in-guild")) {
            fromPlayer.sendMessage(new TextComponent("§cYou are already in a guild! Leave your current one to join a new one."));
            return;
        }
        ProxiedPlayer owner = getGuildOwner(toGuild);
        owner.sendMessage(new TextComponent("§a" + fromPlayer.getName() + "§eWants to join your guild!"));
        owner.sendMessage(new TextComponent("§eIf you want to accept their request:"));
        TextComponent confirm = new TextComponent("§bClick here!");
        confirm.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to accept!").color(ChatColor.GREEN).create()));
        confirm.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/guild accept " + fromPlayer.getName()));
        owner.sendMessage(confirm);
    }

    public static void addToGuild(ProxiedPlayer player, String guild) {
        Guilds plugin = Guilds.getPlugin();
        if (plugin.getData().getBoolean("data.players." + player.getUniqueId().toString() + ".in-guild")) {
            player.sendMessage(new TextComponent("§cYou are already in a guild! Leave your current one to join a new one."));
            return;
        }
        Guilds.role.put(player.getUniqueId(), 1);
        Guilds.guild.put(player.getUniqueId(), guild);
        plugin.getData().set("data.players." + player.getUniqueId().toString() + ".in-guild", true);
        plugin.getData().set("data.players." + player.getUniqueId().toString() + ".guild-name", Guilds.guild.get(player.getUniqueId()));
        plugin.getData().set("data.players." + player.getUniqueId().toString() + ".guild-role", Guilds.role.get(player.getUniqueId()));

        List<String> members = new ArrayList<>();
        for(String rawMembers : plugin.getData().getStringList("data.guilds." + guild + ".members")){
            members.add(rawMembers);
        }
        members.add(player.getUniqueId().toString());
        plugin.getData().set("data.guilds." + guild + ".members", members);
        plugin.saveData();
    }

    public static ProxiedPlayer getGuildOwner(String guild) {
        Guilds plugin = Guilds.getPlugin();
        String uuid = plugin.getData().getString("data.guilds." + guild + ".owner");

        return ProxyServer.getInstance().getPlayer(UUID.fromString(uuid));
    }

    public static void removeFromGuild(ProxiedPlayer player, String guild) {
        Guilds plugin = Guilds.getPlugin();
        // checking if player is inside of guild
        // if yes, we check if they're a guild master
        // if yes we disband guild, if no we remove them from guild

        if (plugin.getData().getBoolean("data.players." + player.getUniqueId().toString() + ".in-guild")) {
            guild = plugin.getData().getString("data.players." + player.getUniqueId().toString() + ".guild-name");
            ProxiedPlayer owner = getGuildOwner(guild);
            if (plugin.getData().getInt("data.players." + player.getUniqueId().toString() + ".guild-role") == 2) {
                Guilds.role.put(player.getUniqueId(), 0);
                Guilds.guild.remove(player.getUniqueId());

                plugin.getData().set("data.guilds." + guild, null);
                plugin.getData().set("data.players." + player.getUniqueId().toString() + ".in-guild", false);
                plugin.getData().set("data.players." + player.getUniqueId().toString() + ".guild-name", null);
                plugin.getData().set("data.players." + player.getUniqueId().toString() + ".guild-role", 0);
                plugin.saveData();
                player.sendMessage(new TextComponent(ChatColor.RED + "You disbanded your guild."));
                return;
            }
            Guilds.role.put(player.getUniqueId(), 0);
            Guilds.guild.remove(player.getUniqueId());
            plugin.getData().set("data.guilds." + guild, null);
            plugin.getData().set("data.players." + player.getUniqueId().toString() + ".in-guild", false);
            plugin.getData().set("data.players." + player.getUniqueId().toString() + ".guild-name", null);
            plugin.getData().set("data.players." + player.getUniqueId().toString() + ".guild-role", 0);
            plugin.getData().set("data.guilds." + guild + ".name", guild);
            plugin.getData().set("data.guilds." + guild + ".owner", owner.getUniqueId().toString());
            List<String> members = new ArrayList<>();
            for(String rawMembers : plugin.getData().getStringList("data.guilds." + guild + ".members")){
                members.add(rawMembers);
            }
            members.remove(player.getUniqueId().toString());
            plugin.getData().set("data.guilds." + guild + ".members", members);
            plugin.saveData();
            player.sendMessage(new TextComponent(ChatColor.YELLOW + "You left your guild."));
            return;
        } else {
            player.sendMessage(new TextComponent(ChatColor.RED + "You are not in a guild!"));
        }
    }

    public static void setupPlayerData(ProxiedPlayer player) {
        // were checking if player is already in a guild or not.
        // if their data isn't already saved in config
        // we create a brand new data section for them.
        // contains: UUID, name, in-guild, guild-name, guild-role.

        Guilds plugin = Guilds.getPlugin();
        if (plugin.getData().getBoolean("data.players." + player.getUniqueId().toString() + ".in-guild")) return;

        Guilds.role.put(player.getUniqueId(), 0);
        Guilds.guild.put(player.getUniqueId(), null);
        plugin.getData().set("data.players." + player.getUniqueId().toString() + ".name", player.getName());
        plugin.getData().set("data.players." + player.getUniqueId().toString() + ".in-guild", false);
        plugin.getData().set("data.players." + player.getUniqueId().toString() + ".guild-name", Guilds.guild.get(player.getUniqueId()));
        plugin.getData().set("data.players." + player.getUniqueId().toString() + ".guild-role", Guilds.role.get(player.getUniqueId()));
        plugin.saveData();
    }

    public static void refreshPlayerData(ProxiedPlayer player) {
        // here were just refreshing their data when they join

        Guilds plugin = Guilds.getPlugin();
        if (plugin.getData().getBoolean("data.players." + player.getUniqueId().toString() + "in-guild")) {
            Guilds.guild.put(player.getUniqueId(), plugin.getData().getString("data.players." + player.getUniqueId().toString() + ".guild-name"));
            Guilds.role.put(player.getUniqueId(), plugin.getData().getInt("data.players." + player.getUniqueId().toString() + "guild-role"));
        }
        Guilds.guild.remove(player.getUniqueId());
    }

    public static void sendGuildChatMessage(ProxiedPlayer fromPlayer, String guild, String message){
        // Simple method, easy to understand.
        // More comments added soon.

        Guilds plugin = Guilds.getPlugin();
        for(String uuids : plugin.getData().getStringList("data.guilds." + guild + ".members")){
            ProxyServer.getInstance().getPlayer(UUID.fromString(uuids)).sendMessage(new TextComponent("§2Guild > §f" + fromPlayer.getDisplayName() + "§f:" + message));
        }
    }
    /*
    plugin.getData().set("data.guilds." + name + ".owner", owner.getUniqueId());
        plugin.getData().set("data.guilds." + name + ".members", "none");
     */
}
