package com.jtgreaves.actingcore.commands;

import com.jtgreaves.actingcore.Main;
import com.jtgreaves.actingcore.utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import static org.bukkit.Bukkit.getPluginCommand;
import static org.bukkit.Bukkit.getServer;

public class SettingsPanel implements CommandExecutor {

    private Main plugin;

    public SettingsPanel(Main plugin) {
        this.plugin = plugin;
        getPluginCommand("settings").setExecutor(this);

    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        if (!(sender instanceof Player)) {
            ConsoleCommandSender console = getServer().getConsoleSender();
            console.sendMessage("You may not use '/settings' command from console!");

        } else {
            loadHomepage(((Player) sender));

        }
        return false;

    }

    public static void loadHomepage(Player player) {
//        if (label.equalsIgnoreCase("settings")) {
        player.sendMessage(utils.chat("&cThis has not been fully implemented yet!"));

        Inventory gui = Bukkit.createInventory(player, 36, ChatColor.RED + "Homepage");

        ItemStack exit = new ItemStack(Material.BARRIER);
        ItemMeta exit_meta = exit.getItemMeta();
        exit_meta.setDisplayName(ChatColor.RED + "Exit");
        exit.setItemMeta(exit_meta);
        gui.setItem(35, exit);


        ItemStack worldIcon = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta worldIconMeta = worldIcon.getItemMeta();
        worldIconMeta.setDisplayName("§aWorlds");
        ((SkullMeta) worldIconMeta).setOwningPlayer(Bukkit.getOfflinePlayer("Dipicrylamine"));
        worldIcon.setItemMeta(worldIconMeta);
        gui.setItem(0, worldIcon);

        ItemStack settingsIcon = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta settingsIconMeta = settingsIcon.getItemMeta();
        settingsIconMeta.setDisplayName("§aSettings");
        ((SkullMeta) settingsIconMeta).setOwningPlayer(Bukkit.getOfflinePlayer("MHF_Chest"));
        settingsIcon.setItemMeta(settingsIconMeta);
        gui.setItem(1, settingsIcon);

        ItemStack usersIcon = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta usersIconmeta = usersIcon.getItemMeta();
        usersIconmeta.setDisplayName("§aUsers");
//                ((SkullMeta) usersIconmeta).setOwningPlayer(Bukkit.getOfflinePlayer("MHF_Chest"));
        usersIcon.setItemMeta(usersIconmeta);
        gui.setItem(2, usersIcon);

        player.openInventory(gui);
    }
}