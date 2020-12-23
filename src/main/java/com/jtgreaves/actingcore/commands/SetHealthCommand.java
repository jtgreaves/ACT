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

public class SetHealthCommand implements CommandExecutor {

    private Main plugin;

    public SetHealthCommand(Main plugin){
        this.plugin = plugin;
        getPluginCommand("sethealth").setExecutor(this);

    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player author = (Player) sender;
        String authordisplay = author.getDisplayName();

        if (!(sender instanceof Player)) {
            ConsoleCommandSender console = getServer().getConsoleSender();
            console.sendMessage("You may not use '/sethealth' command from console!");

        } else {
            if (label.equalsIgnoreCase("sethealth")){

                if (args.length == 0) {
                    author.sendMessage(utils.chat("&4&lERROR &8» &7Please select a player!"));


                } else {
                    //Player typed a player

                    Player target = Bukkit.getPlayerExact(args[0]);
                    double targetmaxhealth = target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue();
                    double targethealth;
                    try {
                        targethealth = Double.parseDouble(args[1]);
                    } catch (NumberFormatException exception) {
                        // Considering you are parsing the double inside an onCommand() method, you may want to consider telling the executor that his/her input is invalid, rather than printing the error to console
                        // Here you can tell the sender or whatever that their input is invalid
                        author.sendMessage(utils.chat("&4&lERROR &8» &e" + args[1] + "&7 could not used from console!"));
                        return false; // Returning will prevent code after this to be ran
                    }
                    // Code using double targethealth.
//
//                    if (target == author) {
//                        author.sendMessage(utils.chat("&4&lERROR &8» &7Select someone who isn't you!"));
//                        return false;
//                    }

                    if (target == null) {
                        //Target is not online!
                        author.sendMessage(utils.chat("&4&lERROR &8» &e" + args[0] + "&7 could not be found!"));
                        return false;
                    }
                    if (targethealth <= 0) {
                        author.sendMessage(utils.chat("&4&lERROR » &7You may not sent someone's health to 0 or less!"));
                    } if (targethealth > targetmaxhealth) {
                        author.sendMessage(utils.chat("&4&lERROR » &7The player has a maximum of &e" + targetmaxhealth + " &7hearts. Please set it to that value or less!"));
                    } else {
                        String targetdisplay = target.getDisplayName();
                                author.sendMessage(utils.chat("&6&lHEALTH &8» &7You've set &e" + targetdisplay + "&e's &7health to &e" + targethealth + "!"));
                                target.sendMessage(utils.chat("&6&lHEALTH &8» &e" + authordisplay + " &7set your health to " + targethealth));
                                target.setHealth(targethealth);
                    }
                }
            }
        }
        return false;
    }
}