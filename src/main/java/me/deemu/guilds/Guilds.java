package me.deemu.guilds;

import me.deemu.guilds.Commands.Guild;
import me.deemu.guilds.Commands.Lobby;
import me.deemu.guilds.GuildsManager.PlayerGuildUtility;
import me.deemu.guilds.Listeners.PlayerJoinListener;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.util.HashMap;
import java.util.UUID;

public class Guilds extends Plugin {
    private File file;
    private Configuration configuration;
    public static Guilds plugin;
    public static HashMap<UUID, String> guild = new HashMap<>();
    public static HashMap<UUID, Integer> role = new HashMap<>();
    public static final HashMap<ProxiedPlayer, PlayerGuildUtility> playerGuildUtilityMap = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        registerCommands();
        registerListeners();
        restorePlayerData();

        createDataFile();
        plugin = this;
        getLogger().info("Plugin has loaded!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Plugin unloaded!");
    }

    public static Guilds getPlugin() {
        return plugin;
    }

    public void registerCommands() {
        getProxy().getPluginManager().registerCommand(this, new Guild());
        getProxy().getPluginManager().registerCommand(this, new Lobby());
    }

    public void createDataFile() {
        file = new File(ProxyServer.getInstance().getPluginsFolder() + "/data.yml");

        try {
            if (!(file.exists())) {
                file.createNewFile();
            }
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);

            ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Configuration getData() {
        return configuration;
    }

    public void saveData() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void restorePlayerData(){
        for(ProxiedPlayer player : ProxyServer.getInstance().getPlayers()){
            int guildRole = this.getData().getInt("data.players." + player.getUniqueId().toString() + ".guild-role");
            String guildName = this.getData().getString("data.players." + player.getUniqueId().toString() + ".guild-name");
            boolean inGuild = this.getData().getBoolean("data.players." + player.getUniqueId().toString() + ".in-guild");
            role.put(player.getUniqueId(), guildRole);

            if(inGuild){
                guild.put(player.getUniqueId(), guildName);
            }else{
                return;
            }
        }
    }

/*
    @NotNull
    public void savePlayerData(ProxiedPlayer player) {
        getData().set("data.guilds." + guild.get(player.getUniqueId()) + ".role", role.get(player.getUniqueId()));
        saveData();
    }
 */

    public void registerListeners() {
        getProxy().getPluginManager().registerListener(this, new PlayerJoinListener());
    }
    // ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, file);

    public static PlayerGuildUtility getPlayerGuildUtility(ProxiedPlayer player){
        PlayerGuildUtility playerGuildUtility;
        if(!(playerGuildUtilityMap.containsKey(player))){
            playerGuildUtility = new PlayerGuildUtility(player);
            playerGuildUtilityMap.put(player, playerGuildUtility);

            return playerGuildUtility;
        }else{
            return playerGuildUtilityMap.get(player);
        }
    }
    public ProxyServer getThisProxy(){
        return getProxy();
    }
}
