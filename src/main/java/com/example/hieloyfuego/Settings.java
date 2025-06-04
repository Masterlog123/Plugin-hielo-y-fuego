package com.example.hieloyfuego;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.EnumMap;
import java.util.Map;

public class Settings {
    private final String hieloWorld;
    private final String fuegoWorld;
    private final Map<ArmorSlot, Material> hieloArmor = new EnumMap<>(ArmorSlot.class);
    private final Map<ArmorSlot, Material> fuegoArmor = new EnumMap<>(ArmorSlot.class);

    public Settings(FileConfiguration config) {
        hieloWorld = config.getString("mundos.hielo", "mundo_hielo");
        fuegoWorld = config.getString("mundos.fuego", "mundo_fuego");

        loadArmor(config, "equipamiento.hielo", hieloArmor);
        loadArmor(config, "equipamiento.fuego", fuegoArmor);
    }

    private void loadArmor(FileConfiguration config, String path, Map<ArmorSlot, Material> map) {
        ConfigurationSection sec = config.getConfigurationSection(path);
        if (sec == null) return;
        for (ArmorSlot slot : ArmorSlot.values()) {
            String value = sec.getString(slot.getKey(), "NONE");
            map.put(slot, parseMaterial(value));
        }
    }

    private Material parseMaterial(String value) {
        if (value == null) return Material.AIR;
        if (value.equalsIgnoreCase("NONE") || value.equals("-1")) {
            return Material.AIR;
        }
        try {
            return Material.valueOf(value);
        } catch (IllegalArgumentException ex) {
            return Material.AIR;
        }
    }

    public String getHieloWorld() {
        return hieloWorld;
    }

    public String getFuegoWorld() {
        return fuegoWorld;
    }

    public Map<ArmorSlot, Material> getHieloArmor() {
        return hieloArmor;
    }

    public Map<ArmorSlot, Material> getFuegoArmor() {
        return fuegoArmor;
    }

    public enum ArmorSlot {
        HEAD("head"),
        CHEST("chest"),
        LEGS("legs"),
        FEET("feet");

        private final String key;

        ArmorSlot(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }
}
