package me.deemu.guilds.Commands;

import me.deemu.guilds.Guilds;
import me.deemu.guilds.GuildsManager.Manager;
import me.deemu.guilds.Utilities.Utility;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Guild extends Command {
    public Guild() {
        super("guild");
    }
    @Override
    public void execute(CommandSender sender, String[] args) {
        Guilds plugin = Guilds.getPlugin();
        if(sender instanceof ProxiedPlayer){
            ProxiedPlayer player = (ProxiedPlayer) sender;
                if(args.length == 0){
                    Utility.sendMessage(player, "&b----------------------------------------------");
                    Utility.sendMessage(player, "&aGuild Commands:");
                    Utility.sendMessage(player, "&e/guild accept &b- Accepts a guild invitation.");
                    Utility.sendMessage(player, "&e/guild chat &b- Send a chat message to your guild chat channel");
                    Utility.sendMessage(player, "&e/guild create name &b- Create a guild with the specified name");
                    Utility.sendMessage(player, "&e/guild demote &b- Demotes the player to the previous rank");
                    Utility.sendMessage(player, "&e/guild disband &b- Disbands the guild");
                    Utility.sendMessage(player, "&e/guild discord &b- Set or view the guild's discord link");
                    Utility.sendMessage(player, "&e/guild help &b- Prints the help message");
                    Utility.sendMessage(player, "&e/guild history &b- View the last 24 hours of guild events");
                    Utility.sendMessage(player, "&e/guild info &b-");
                    return;
                }
                if(args[0].equalsIgnoreCase("create")){
                    if(player.hasPermission("guilds.create")){
                        String guild = args[1];
                        Manager.createGuild(player, guild);
                    }else{
                        Utility.sendMessage(player, "&eYou can't create a guild with your current rank!");
                    }
                }
                if(args[0].equalsIgnoreCase("leave")){
                    String guild = plugin.getData().getString("data.player." + player.getUniqueId().toString() + ".guild-name");
                    Manager.removeFromGuild(player, guild);
                }
                if(args[0].equalsIgnoreCase("accept")){
                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[1]);
                    String guild = plugin.getData().getString("data.players." + player.getUniqueId().toString() + ".guild-name");
                    Manager.addToGuild(target, guild);
                }
                if(args[0].equalsIgnoreCase("join")){
                    Manager.sendGuildRequest(player, args[1]);
                }
                if(args[0].equalsIgnoreCase("chat")){
                    if(!(args[1] == null)){
                        String guild = plugin.getData().getString("data.player." + player.getUniqueId().toString() + ".guild-name");
                        Manager.sendGuildChatMessage(player, guild, args[1]);
                    }else{
                        player.sendMessage(new TextComponent("§cSpecify a message!"));
                    }
                }
        }else{
            sender.sendMessage(new TextComponent(ChatColor.RED + "You cannot use this command!"));
        }
    }
    public void getPlayer(String name){

    }
}
