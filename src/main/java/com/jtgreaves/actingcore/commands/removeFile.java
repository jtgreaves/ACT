package com.jtgreaves.actingcore.commands;

import com.jtgreaves.actingcore.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import com.jtgreaves.actingcore.utils.utils;

import java.io.File;

import static org.bukkit.Bukkit.getPluginCommand;

public class removeFile implements CommandExecutor {

    private Main plugin;

    public removeFile(Main plugin) {
        this.plugin = plugin;
        getPluginCommand("removeFile").setExecutor(this);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("acting.admin")) {

            if (args.length == 0) {
                sender.sendMessage(utils.chat("&4&lERROR &8» &7You must say what file or directory you would like to delete."));
                return false;
            }

            File file = new File(Bukkit.getWorldContainer().getPath() + args[0]);
            if (file.exists()) {
                delete(file);
                sender.sendMessage(utils.chat("&6&lCORE &8» &7Your file &e" + args[0] + "&7 has been deleted!"));

            } else {
                sender.sendMessage(utils.chat("&4&lERROR &8» &7Oops! I cannot find that file..."));
            }

        } else {
            sender.sendMessage(utils.chat("&4&lERROR &8» &7Insufficient permissions!"));
        }

        return true;
    }

    public void delete(File file) {
        if (file.isDirectory()) { // If its a folder, delete its contents
            for (File f : file.listFiles())
                delete(f);
        }
        file.delete(); // Delete the actual file
    }
}