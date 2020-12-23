package com.jtgreaves.actingcore.listeners;


import com.jtgreaves.actingcore.Main;
import com.jtgreaves.actingcore.commands.SettingsPanel;
import com.jtgreaves.actingcore.utils.utils;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class onClick implements Listener {
    private final Main plugin;
    private final LuckPerms luckPerms;

    public onClick(Main plugin, LuckPerms luckPerms) {
        this.plugin = plugin;
        this.luckPerms = luckPerms;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void clickEvent(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) {
            return;
        }

        if (event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RED + "Exit")) {
            event.getWhoClicked().sendMessage(ChatColor.RED + "See you later!");
            event.getWhoClicked().closeInventory();
            return;
        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Back")) {
            if (event.getView().getTitle().equals(ChatColor.RED + "World Panel") || event.getView().getTitle().equals(ChatColor.RED + "Settings Panel") || event.getView().getTitle().equals(ChatColor.RED + "Users Panel")) {
                SettingsPanel.loadHomepage(Bukkit.getPlayer(event.getWhoClicked().getName()));
//                (Bukkit.getPlayer(event.getWhoClicked().getName()));
            } else if (event.getView().getTitle().startsWith(ChatColor.RED + "User Panel")) {
                loadUsersSettings(Bukkit.getPlayer(event.getWhoClicked().getName()));
            } else if (event.getView().getTitle().startsWith(ChatColor.RED + "User Rank Panel")) {
                loadUserSettings(Bukkit.getPlayer(event.getWhoClicked().getName()), Bukkit.getPlayer(event.getView().getTitle().split(" - ")[1]));
            }
            return;
        }

        if (event.getView().getTitle().equalsIgnoreCase(ChatColor.RED + "Homepage")) {
            event.setCancelled(true);

            if (event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Worlds")) {
                loadWorlds(Bukkit.getPlayer(event.getWhoClicked().getName()));
            } else if (event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Settings")) {
                loadSettings(Bukkit.getPlayer(event.getWhoClicked().getName()));
            } else if (event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Users")) {
                loadUsersSettings(Bukkit.getPlayer(event.getWhoClicked().getName()));
            }
//            event.getWhoClicked().sendMessage(event.getCurrentItem().getItemMeta().getDisplayName());


        } else if (event.getView().getTitle().equalsIgnoreCase(ChatColor.RED + "World Panel")) {
            event.setCancelled(true);

            if (event.getCurrentItem().getItemMeta().getDisplayName().startsWith(ChatColor.RED + "World")) {
                event.getWhoClicked().sendMessage(ChatColor.GREEN + "Loading the world '" + event.getCurrentItem().getItemMeta().getDisplayName().split(" - ")[1] + "'!");
                Bukkit.getPlayer(event.getWhoClicked().getName()).performCommand("mv load " + event.getCurrentItem().getItemMeta().getDisplayName().split(" - ")[1]);
                loadWorlds(Bukkit.getPlayer(event.getWhoClicked().getName()));
            } else if (event.getCurrentItem().getItemMeta().getDisplayName().startsWith(ChatColor.GREEN + "World")) {
                event.getWhoClicked().sendMessage(ChatColor.RED + "Unloading the world '" + event.getCurrentItem().getItemMeta().getDisplayName().split(" - ")[1] + "'!");
                Bukkit.getPlayer(event.getWhoClicked().getName()).performCommand("mv unload " + event.getCurrentItem().getItemMeta().getDisplayName().split(" - ")[1]);
                loadWorlds(Bukkit.getPlayer(event.getWhoClicked().getName()));
            }

        } else if (event.getView().getTitle().equalsIgnoreCase(ChatColor.RED + "Settings Panel")) {
            event.setCancelled(true);

            if (event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "One hit corpse")) {
                plugin.getConfig().set("one-hit-corpse", false);
                loadSettings(Bukkit.getPlayer(event.getWhoClicked().getName()));
            } else if (event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RED + "One hit corpse")) {
                plugin.getConfig().set("one-hit-corpse", true);
                loadSettings(Bukkit.getPlayer(event.getWhoClicked().getName()));
            } else if (event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Death corpse")) {
                plugin.getConfig().set("death-corpse", false);
                loadSettings(Bukkit.getPlayer(event.getWhoClicked().getName()));
            } else if (event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RED + "Death corpse")) {
                plugin.getConfig().set("death-corpse", true);
                loadSettings(Bukkit.getPlayer(event.getWhoClicked().getName()));
            }
        } else if (event.getView().getTitle().equalsIgnoreCase(ChatColor.RED + "Users Panel")) {
            event.setCancelled(true);

            if (event.getCurrentItem().getType().equals(Material.PLAYER_HEAD)) {
                ItemMeta itemMeta = event.getCurrentItem().getItemMeta();
                loadUserSettings(Bukkit.getPlayer(event.getWhoClicked().getName()), Bukkit.getPlayer(((SkullMeta) itemMeta).getOwningPlayer().getName()));
            }
        } else if (event.getView().getTitle().startsWith(ChatColor.RED + "User Panel")) {
            event.setCancelled(true);

            if (event.getCurrentItem().getItemMeta().getDisplayName().startsWith(utils.chat("&eRanks"))) {
                loadUserSettings_RANK(Bukkit.getPlayer(event.getWhoClicked().getName()), Bukkit.getPlayer(event.getView().getTitle().split(" - ")[1]));

            } else if (event.getCurrentItem().getItemMeta().getDisplayName().startsWith(ChatColor.RED + "Operator")) {
                Bukkit.getPlayer(event.getWhoClicked().getName()).performCommand("op " + event.getView().getTitle().split(" - ")[1]);
                loadUserSettings(Bukkit.getPlayer(event.getWhoClicked().getName()), Bukkit.getPlayer(event.getView().getTitle().split(" - ")[1]));
            } else if (event.getCurrentItem().getItemMeta().getDisplayName().startsWith(ChatColor.GREEN + "Operator")) {
                Bukkit.getPlayer(event.getWhoClicked().getName()).performCommand("deop " + event.getView().getTitle().split(" - ")[1]);
                loadUserSettings(Bukkit.getPlayer(event.getWhoClicked().getName()), Bukkit.getPlayer(event.getView().getTitle().split(" - ")[1]));
            }

        } else if (event.getView().getTitle().startsWith(ChatColor.RED + "User Rank Panel")) {
            event.setCancelled(true);

            if (event.getCurrentItem().getItemMeta().getDisplayName().startsWith(utils.chat("&4&lOWNER"))) {
                if (event.getCurrentItem().getItemMeta().getDisplayName().endsWith("True")) {
                    Bukkit.getPlayer(event.getWhoClicked().getName()).performCommand("lp user " + event.getView().getTitle().split(" - ")[1] + " group remove Owner");
                } else {
                    Bukkit.getPlayer(event.getWhoClicked().getName()).performCommand("lp user " + event.getView().getTitle().split(" - ")[1] + " group add Owner");
                }
            } else if (event.getCurrentItem().getItemMeta().getDisplayName().startsWith(utils.chat("&4&lYOUTUBER"))) {
                if (event.getCurrentItem().getItemMeta().getDisplayName().endsWith("True")) {
                    Bukkit.getPlayer(event.getWhoClicked().getName()).performCommand("lp user " + event.getView().getTitle().split(" - ")[1] + " group remove Youtuber");
                } else {
                    Bukkit.getPlayer(event.getWhoClicked().getName()).performCommand("lp user " + event.getView().getTitle().split(" - ")[1] + " group add Youtuber");
                }
            } else if (event.getCurrentItem().getItemMeta().getDisplayName().startsWith(utils.chat("&6&lACTOR"))) {
                if (event.getCurrentItem().getItemMeta().getDisplayName().endsWith("True")) {
                    Bukkit.getPlayer(event.getWhoClicked().getName()).performCommand("lp user " + event.getView().getTitle().split(" - ")[1] + " group remove Actor");
                } else {
                    Bukkit.getPlayer(event.getWhoClicked().getName()).performCommand("lp user " + event.getView().getTitle().split(" - ")[1] + " group add Actor");
                }
            } else if (event.getCurrentItem().getItemMeta().getDisplayName().startsWith(utils.chat("&e&lBUILDER"))) {
                if (event.getCurrentItem().getItemMeta().getDisplayName().endsWith("True")) {
                    Bukkit.getPlayer(event.getWhoClicked().getName()).performCommand("lp user " + event.getView().getTitle().split(" - ")[1] + " group remove Builder");
                } else {
                    Bukkit.getPlayer(event.getWhoClicked().getName()).performCommand("lp user " + event.getView().getTitle().split(" - ")[1] + " group add Builder");
                }

            } else if (event.getCurrentItem().getItemMeta().getDisplayName().startsWith(utils.chat("&7Click to reset"))) {
                Bukkit.getPlayer(event.getWhoClicked().getName()).performCommand("lp user " + event.getView().getTitle().split(" - ")[1] + " clear");
            } else if (event.getCurrentItem().getItemMeta().getDisplayName().startsWith(ChatColor.RED + "Operator")) {
                Bukkit.getPlayer(event.getWhoClicked().getName()).performCommand("op " + event.getView().getTitle().split(" - ")[1]);
            } else if (event.getCurrentItem().getItemMeta().getDisplayName().startsWith(ChatColor.GREEN + "Operator")) {
                Bukkit.getPlayer(event.getWhoClicked().getName()).performCommand("deop " + event.getView().getTitle().split(" - ")[1]);

            }

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                loadUserSettings_RANK(Bukkit.getPlayer(event.getWhoClicked().getName()), Bukkit.getPlayer(event.getView().getTitle().split(" - ")[1]));
            }, 5L);
        }
    }

    public void loadWorlds(Player player) {
//        Player player = (Player) event.getWhoClicked();
        Inventory gui = Bukkit.createInventory(player, 45, ChatColor.RED + "World Panel");

        ItemStack exit = new ItemStack(Material.BARRIER);
        ItemMeta exit_meta = exit.getItemMeta();
        exit_meta.setDisplayName(ChatColor.RED + "Exit");
        exit.setItemMeta(exit_meta);
        gui.setItem(44, exit);

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta back_meta = back.getItemMeta();
        back_meta.setDisplayName(ChatColor.YELLOW + "Back");
        back.setItemMeta(back_meta);
        gui.setItem(36, back);

        String[] worldNames = new String[Bukkit.getServer().getWorlds().size()];
        String[] addedNames = new String[0];

        int counter = 0;
        for (World w : Bukkit.getServer().getWorlds()) {
            worldNames[counter] = w.getName();
            counter++;
        }
        counter = 0;
        for (String s : worldNames) {
            ItemStack item = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
            ItemMeta item_meta = item.getItemMeta();
            item_meta.setDisplayName(ChatColor.GREEN + "World - " + s);
            item.setItemMeta(item_meta);
            gui.setItem(counter, item);
            counter++;

//                        player.sendMessage(String.valueOf(Arrays.asList(addedNames)));
            addedNames = Arrays.copyOf(addedNames, addedNames.length + 1);
            addedNames[addedNames.length - 1] = s;
            //                        System.out.println("World Names = " + s);
        }

        File[] worldContainerDirs = Bukkit.getWorldContainer().listFiles();
        for (File file : worldContainerDirs) {
            if (file.isDirectory()) {
                File levelFile = new File(file.getPath() + "/level.dat");
                if (levelFile.exists()) {
//                                player.sendMessage(String.valueOf(Arrays.asList(addedNames)));
                    if (!Arrays.asList(addedNames).contains(file.getName())) {
                        ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
                        ItemMeta item_meta = item.getItemMeta();
                        item_meta.setDisplayName(ChatColor.RED + "World - " + file.getName());
                        item.setItemMeta(item_meta);
                        gui.setItem(counter, item);

                        addedNames = Arrays.copyOf(addedNames, addedNames.length + 1);
                        addedNames[addedNames.length - 1] = file.getName();


                        counter++;
                    }
                }
            }
        }

        player.openInventory(gui);
    }

    public void loadUsersSettings(Player player) {
        Inventory gui = Bukkit.createInventory(player, 36, ChatColor.RED + "Users Panel");

        ItemStack exit = new ItemStack(Material.BARRIER);
        ItemMeta exit_meta = exit.getItemMeta();
        exit_meta.setDisplayName(ChatColor.RED + "Exit");
        exit.setItemMeta(exit_meta);
        gui.setItem(35, exit);

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta back_meta = back.getItemMeta();
        back_meta.setDisplayName(ChatColor.YELLOW + "Back");
        back.setItemMeta(back_meta);
        gui.setItem(27, back);

        int counter = 0;
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            ItemStack item = new ItemStack(Material.PLAYER_HEAD);
            ItemMeta item_meta = item.getItemMeta();
            ((SkullMeta) item_meta).setOwningPlayer(Bukkit.getPlayer(p.getUniqueId()));
            item_meta.setDisplayName(p.getPlayerListName());
            item.setItemMeta(item_meta);
            gui.setItem(counter, item);

            counter++;
        }
        player.openInventory(gui);

    }

    public void loadUserSettings(Player player, Player toModify) {
        Inventory gui = Bukkit.createInventory(player, 36, ChatColor.RED + "User Panel - " + toModify.getName());

        ItemStack exit = new ItemStack(Material.BARRIER);
        ItemMeta exit_meta = exit.getItemMeta();
        exit_meta.setDisplayName(ChatColor.RED + "Exit");
        exit.setItemMeta(exit_meta);
        gui.setItem(35, exit);

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta back_meta = back.getItemMeta();
        back_meta.setDisplayName(ChatColor.YELLOW + "Back");
        back.setItemMeta(back_meta);
        gui.setItem(27, back);

        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta item_meta = item.getItemMeta();
        ((SkullMeta) item_meta).setOwningPlayer(Bukkit.getPlayer(toModify.getUniqueId()));
        item_meta.setDisplayName(toModify.getPlayerListName());
        item.setItemMeta(item_meta);
        gui.setItem(4, item);

        ItemStack ranks = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta ranks_meta = ranks.getItemMeta();
        ranks_meta.setDisplayName(ChatColor.YELLOW + "Ranks");
        List<String> rankslore = new ArrayList<String>();
        rankslore.add(ChatColor.GRAY + "Click to edit.");
        if (toModify.isOp()) {
            rankslore.add(utils.chat("&eOperator &7- &aTrue."));
        } else {
            rankslore.add(utils.chat("&eOperator &7- &cFalse."));
        }
        rankslore.add(" ");

        isRole(toModify.getUniqueId(), "owner").thenAcceptAsync(result -> {
            if (result) {
                rankslore.add(utils.chat("&4&lOWNER &7- &aTrue."));
            } else {
                rankslore.add(utils.chat("&4&lOWNER &7- &cFalse."));
            }
        });

        isRole(toModify.getUniqueId(), "manager").thenAcceptAsync(result -> {
            if (result) {
                rankslore.add(utils.chat("&c&lMANAGER &7- &aTrue."));
            } else {
                rankslore.add(utils.chat("&c&lMANAGER &7- &cFalse."));
            }
        });

        isRole(toModify.getUniqueId(), "youtuber").thenAcceptAsync(result -> {
            if (result) {
                rankslore.add(utils.chat("&4&lYOUTUBER &7- &aTrue."));
            } else {
                rankslore.add(utils.chat("&4&lYOUTUBER &7- &cFalse."));
            }
        });

        isRole(toModify.getUniqueId(), "actor").thenAcceptAsync(result -> {
            if (result) {
                rankslore.add(utils.chat("&6&lACTOR &7- &aTrue."));
            } else {
                rankslore.add(utils.chat("&6&lACTOR &7- &cFalse."));
            }
        });

        isRole(toModify.getUniqueId(), "builder").thenAcceptAsync(result -> {
            if (result) {
                rankslore.add(utils.chat("&e&lBUILDER &7- &aTrue."));
            } else {
                rankslore.add(utils.chat("&e&lBUILDER &7- &cFalse."));
            }
        });

        isRole(toModify.getUniqueId(), "default").thenAcceptAsync(result -> {
            if (result) {
                rankslore.add(utils.chat("&7&lDEFAULT &7- &aTrue."));
            } else {
                rankslore.add(utils.chat("&7&lDEFAULT &7- &cFalse."));
            }
            ranks_meta.setLore(rankslore);
            ranks.setItemMeta(ranks_meta);
            gui.setItem(10, ranks);
        });
//
//        while (completed < 6) {
//        }
//        }


        ItemStack gamemode = new ItemStack(Material.GRASS_BLOCK);
        ItemMeta gamemode_meta = gamemode.getItemMeta();
        gamemode_meta.setDisplayName(ChatColor.YELLOW + "Gamemode");
        List<String> gamemodelore = new ArrayList<String>();
        gamemodelore.add(ChatColor.GRAY + "Current Gamemode - " + toModify.getGameMode());
        gamemode_meta.setLore(gamemodelore);
        gamemode.setItemMeta(gamemode_meta);
        gui.setItem(12, gamemode);

        ItemStack teleport = new ItemStack(Material.ENDER_PEARL);
        ItemMeta teleport_meta = teleport.getItemMeta();
        teleport_meta.setDisplayName(ChatColor.YELLOW + "Teleport");
        List<String> teleportlore = new ArrayList<String>();
        teleportlore.add(ChatColor.GRAY + "LMB - Teleport to the player.");
        teleportlore.add(ChatColor.GRAY + "RMB - Teleport the player to you.");
        teleport_meta.setLore(teleportlore);
        teleport.setItemMeta(teleport_meta);
        gui.setItem(14, teleport);

        ItemStack operator = new ItemStack(Material.NETHER_STAR);
        ItemMeta operator_meta = operator.getItemMeta();
        List<String> operatorlore = new ArrayList<String>();
        if (toModify.isOp()) {
            operator_meta.setDisplayName(ChatColor.GREEN + "Operator");
            operatorlore.add(ChatColor.GREEN + "Currently an operator.");
            operatorlore.add(" ");
            operatorlore.add(ChatColor.GRAY + "You cannot edit roles on an operator.");
            operatorlore.add(ChatColor.GRAY + "Click to toggle!");
            operator_meta.addEnchant(Enchantment.ARROW_INFINITE, 1, false);
        } else {
            operator_meta.setDisplayName(ChatColor.RED + "Operator");
            operatorlore.add(ChatColor.GRAY + "Currently not an operator.");
            operatorlore.add(ChatColor.GRAY + "Click to toggle!");
        }
        operator_meta.setLore(operatorlore);
        operator.setItemMeta(operator_meta);
        gui.setItem(16, operator);

        player.openInventory(gui);
    }

    public void loadUserSettings_RANK(Player player, Player toModify) {
        Inventory gui = Bukkit.createInventory(player, 36, ChatColor.RED + "User Rank Panel - " + toModify.getName());

        ItemStack exit = new ItemStack(Material.BARRIER);
        ItemMeta exit_meta = exit.getItemMeta();
        exit_meta.setDisplayName(ChatColor.RED + "Exit");
        exit.setItemMeta(exit_meta);
        gui.setItem(35, exit);

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta back_meta = back.getItemMeta();
        back_meta.setDisplayName(ChatColor.YELLOW + "Back");
        back.setItemMeta(back_meta);
        gui.setItem(27, back);

        ItemStack operator = new ItemStack(Material.NETHER_STAR);
        ItemMeta operator_meta = operator.getItemMeta();
        List<String> operatorlore = new ArrayList<String>();
        if (toModify.isOp()) {
            operator_meta.setDisplayName(ChatColor.GREEN + "Operator");
            operatorlore.add(ChatColor.GREEN + "Currently an operator.");
            operatorlore.add(" ");
            operatorlore.add(ChatColor.GRAY + "You cannot edit roles on an operator.");
            operatorlore.add(ChatColor.GRAY + "Click to toggle!");
            operator_meta.addEnchant(Enchantment.ARROW_INFINITE, 1, false);
        } else {
            operator_meta.setDisplayName(ChatColor.RED + "Operator");
            operatorlore.add(ChatColor.GRAY + "Currently not an operator.");
            operatorlore.add(ChatColor.GRAY + "Click to toggle!");
        }
        operator_meta.setLore(operatorlore);
        operator.setItemMeta(operator_meta);
        gui.setItem(8, operator);


        operator_meta.setLore(operatorlore);
        operator.setItemMeta(operator_meta);

        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta item_meta = item.getItemMeta();
        ((SkullMeta) item_meta).setOwningPlayer(Bukkit.getPlayer(toModify.getUniqueId()));
        item_meta.setDisplayName(toModify.getPlayerListName());
        item.setItemMeta(item_meta);
        gui.setItem(4, item);


//        Ranks Items
//        if (!toModify.isOp()) {
        isRole(toModify.getUniqueId(), "owner").thenAcceptAsync(result -> {
            if (result) {
                ItemStack owner = new ItemStack(Material.RED_CONCRETE);
                ItemMeta ownerItemMeta = exit.getItemMeta();
                ownerItemMeta.setDisplayName(utils.chat("&4&lOWNER &7- &eTrue"));
                ownerItemMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, false);
                owner.setItemMeta(ownerItemMeta);
                gui.setItem(12, owner);
            } else {
                ItemStack owner = new ItemStack(Material.RED_CONCRETE);
                ItemMeta ownerItemMeta = exit.getItemMeta();
                ownerItemMeta.setDisplayName(utils.chat("&4&lOWNER &7- &cFalse"));
                owner.setItemMeta(ownerItemMeta);
                gui.setItem(12, owner);
            }
        });


        isRole(toModify.getUniqueId(), "youtuber").thenAcceptAsync(result -> {
            if (result) {
                ItemStack youtuber = new ItemStack(Material.RED_CONCRETE_POWDER);
                ItemMeta youtuberItemMeta = youtuber.getItemMeta();
                youtuberItemMeta.setDisplayName(utils.chat("&4&lYOUTUBER &7- &aTrue"));
                youtuberItemMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, false);
                youtuber.setItemMeta(youtuberItemMeta);
                gui.setItem(14, youtuber);
            } else {
                ItemStack youtuber = new ItemStack(Material.RED_CONCRETE_POWDER);
                ItemMeta youtuberItemMeta = youtuber.getItemMeta();
                youtuberItemMeta.setDisplayName(utils.chat("&4&lYOUTUBER &7- &cFalse"));
                youtuber.setItemMeta(youtuberItemMeta);
                gui.setItem(14, youtuber);
            }
        });

        isRole(toModify.getUniqueId(), "actor").thenAcceptAsync(result -> {
            if (result) {
                ItemStack actor = new ItemStack(Material.ORANGE_CONCRETE);
                ItemMeta actorItemMeta = exit.getItemMeta();
                actorItemMeta.setDisplayName(utils.chat("&6&lACTOR &7- &aTrue"));
                actorItemMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, false);
                actor.setItemMeta(actorItemMeta);
                gui.setItem(20, actor);
            } else {
                ItemStack actor = new ItemStack(Material.ORANGE_CONCRETE);
                ItemMeta actorItemMeta = exit.getItemMeta();
                actorItemMeta.setDisplayName(utils.chat("&6&lACTOR &7- &cFalse"));
                actor.setItemMeta(actorItemMeta);
                gui.setItem(20, actor);
            }
        });

        isRole(toModify.getUniqueId(), "builder").thenAcceptAsync(result -> {
            if (result) {
                ItemStack builder = new ItemStack(Material.YELLOW_CONCRETE);
                ItemMeta builderItemMeta = exit.getItemMeta();
                builderItemMeta.setDisplayName(utils.chat("&e&lBUILDER &7- &aTrue"));
                builderItemMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, false);
                builder.setItemMeta(builderItemMeta);
                gui.setItem(22, builder);
            } else {
                ItemStack builder = new ItemStack(Material.YELLOW_CONCRETE);
                ItemMeta builderItemMeta = exit.getItemMeta();
                builderItemMeta.setDisplayName(utils.chat("&e&lBUILDER &7- &cFalse"));
                builder.setItemMeta(builderItemMeta);
                gui.setItem(22, builder);
            }
        });

        ItemStack guest = new ItemStack(Material.GRAY_CONCRETE);
        ItemMeta guestItemMeta = exit.getItemMeta();
        guestItemMeta.setDisplayName(ChatColor.GRAY + "Click to reset the user to default.");
        guest.setItemMeta(guestItemMeta);
        gui.setItem(24, guest);
//        }

        player.openInventory(gui);
    }

    public void loadSettings(Player player) {
        Inventory gui = Bukkit.createInventory(player, 36, ChatColor.RED + "Settings Panel");

        ItemStack exit = new ItemStack(Material.BARRIER);
        ItemMeta exit_meta = exit.getItemMeta();
        exit_meta.setDisplayName(ChatColor.RED + "Exit");
        exit.setItemMeta(exit_meta);
        gui.setItem(35, exit);

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta back_meta = back.getItemMeta();
        back_meta.setDisplayName(ChatColor.YELLOW + "Back");
        back.setItemMeta(back_meta);
        gui.setItem(27, back);

//        Settings for one hit corpse
        if (plugin.getConfig().getBoolean("one-hit-corpse")) {
            ItemStack item = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
            ItemMeta item_meta = item.getItemMeta();
            item_meta.setDisplayName(ChatColor.GREEN + "One hit corpse");
            item.setItemMeta(item_meta);
            gui.setItem(0, item);
        } else {
            ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
            ItemMeta item_meta = item.getItemMeta();
            item_meta.setDisplayName(ChatColor.RED + "One hit corpse");
            item.setItemMeta(item_meta);
            gui.setItem(0, item);
        }

//        Settings for death corpse
        if (plugin.getConfig().getBoolean("death-corpse")) {
            ItemStack item = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
            ItemMeta item_meta = item.getItemMeta();
            item_meta.setDisplayName(ChatColor.GREEN + "Death corpse");
            item.setItemMeta(item_meta);
            gui.setItem(1, item);
        } else {
            ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
            ItemMeta item_meta = item.getItemMeta();
            item_meta.setDisplayName(ChatColor.RED + "Death corpse");
            item.setItemMeta(item_meta);
            gui.setItem(1, item);
        }

        player.openInventory(gui);
    }

    public CompletableFuture<Boolean> isRole(UUID who, String role) {
        return luckPerms.getUserManager().loadUser(who)
                .thenApplyAsync(user -> {
                    Collection<Group> inheritedGroups = user.getInheritedGroups(user.getQueryOptions());
                    return inheritedGroups.stream().anyMatch(g -> g.getName().equals(role));
                });
    }
}