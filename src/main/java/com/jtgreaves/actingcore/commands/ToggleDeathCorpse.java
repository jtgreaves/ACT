package com.jtgreaves.actingcore.commands;

import com.jtgreaves.actingcore.Main;
import com.jtgreaves.actingcore.utils.utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.getPluginCommand;

public class ToggleDeathCorpse implements CommandExecutor {

    private Main plugin;

    public ToggleDeathCorpse(Main plugin){
        this.plugin = plugin;
        getPluginCommand("toggledeathcorpse").setExecutor(this);

    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player author = (Player) sender;

        if(plugin.getConfig().getBoolean("death-corpse")) {
            author.sendMessage(utils.chat("&6&lCORE &8» &7I have &cdisabled &7death corpse!"));
            plugin.getConfig().set("death-corpse", false);
        } else {
            author.sendMessage(utils.chat("&6&lCORE &8» &7I have &aenabled &7death corpse!"));
            plugin.getConfig().set("death-corpse", true);
        }
        plugin.saveConfig();


        return true;
    }
}