package com.jtgreaves.actingcore.commands;

import com.jtgreaves.actingcore.Main;
import com.jtgreaves.actingcore.utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.getPluginCommand;
import static org.bukkit.Bukkit.getServer;

public class ToggleOneHitCorpse implements CommandExecutor {

    private Main plugin;

    public ToggleOneHitCorpse(Main plugin){
        this.plugin = plugin;
        getPluginCommand("toggleonehitcorpse").setExecutor(this);

    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player author = (Player) sender;

        if(plugin.getConfig().getBoolean("one-hit-corpse")) {
            author.sendMessage(utils.chat("&6&lCORE &8» &7I have &cdisabled &7one hit corpse!"));
            plugin.getConfig().set("one-hit-corpse", false);
        } else {
            author.sendMessage(utils.chat("&6&lCORE &8» &7I have &aenabled &7one hit corpse!"));
            plugin.getConfig().set("one-hit-corpse", true);
        }
        plugin.saveConfig();


        return true;
    }
}