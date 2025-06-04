package com.example.hieloyfuego;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class HieloYFuego extends JavaPlugin {

    private Settings settings;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        settings = new Settings(getConfig());
        getServer().getPluginManager().registerEvents(new PlayerEffectsListener(this, settings), this);
    }

    public Settings getSettings() {
        return settings;
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        settings = new Settings(getConfig());
    }
}
