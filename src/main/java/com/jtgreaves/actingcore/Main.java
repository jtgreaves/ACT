package com.jtgreaves.actingcore;

import com.jtgreaves.actingcore.commands.*;
import com.jtgreaves.actingcore.listeners.DeathListeners;
import com.jtgreaves.actingcore.listeners.PvPListeners;
import com.jtgreaves.actingcore.listeners.onClick;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    FileConfiguration config = this.getConfig();
    private LuckPerms luckPerms;

    @Override
    public void onEnable() {
        this.luckPerms = getServer().getServicesManager().load(LuckPerms.class);

        config.addDefault("one-hit-corpse", false);
        config.addDefault("death-corpse", false);
        config.options().copyDefaults(true);
        saveConfig();

        // Plugin startup logic
        System.out.print("[A.C.T.] Loaded successfully!");
        //Import Commands
        new SetHealthCommand(this);
        new ToggleOneHitCorpse(this);
        new ToggleDeathCorpse(this);
        new SettingsPanel(this);
        new removeFile(this);

        // Import Listeners
        new PvPListeners(this);
        new DeathListeners(this);
        new onClick(this, this.luckPerms);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


}
