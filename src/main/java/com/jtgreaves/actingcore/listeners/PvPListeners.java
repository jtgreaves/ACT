package com.jtgreaves.actingcore.listeners;


import com.jtgreaves.actingcore.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PvPListeners implements Listener {
    private Main plugin;

    public PvPListeners(Main plugin){
        this.plugin = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }


    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event)
    {
        if (event.getDamager() instanceof Player){
            if (plugin.getConfig().getBoolean("one-hit-corpse")) {
                Player victim = (Player) event.getEntity();
                Location deathLocation = victim.getLocation();

                victim.damage(100);

                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    victim.spigot().respawn();
                    victim.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,  100, 3, true)); //Add the potion effect.
                    victim.teleport(deathLocation);

                    victim.performCommand("lay");
                }, 15L);
            }
        }
}}