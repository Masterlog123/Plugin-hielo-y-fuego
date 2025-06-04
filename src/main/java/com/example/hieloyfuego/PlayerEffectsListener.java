package com.example.hieloyfuego;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

public class PlayerEffectsListener implements Listener {

    private final HieloYFuego plugin;
    private final Settings settings;
    private final Map<UUID, BukkitTask> fuegoTasks = new HashMap<>();

    public PlayerEffectsListener(HieloYFuego plugin, Settings settings) {
        this.plugin = plugin;
        this.settings = settings;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        checkPlayer(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        cancelTask(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        checkPlayer(event.getPlayer());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        if (world.getName().equals(settings.getHieloWorld())) {
            if (player.getLocation().getBlockY() < 30 || player.getLocation().getBlockY() > 80) {
                if (!hasRequiredArmor(player, settings.getHieloArmor())) {
                    applyHieloEffects(player);
                } else {
                    removeHieloEffects(player);
                }
            } else {
                removeHieloEffects(player);
            }
        }
    }

    private void checkPlayer(Player player) {
        cancelTask(player.getUniqueId());
        World world = player.getWorld();
        if (world.getName().equals(settings.getFuegoWorld())) {
            if (!hasRequiredArmor(player, settings.getFuegoArmor())) {
                BukkitTask task = Bukkit.getScheduler().runTaskTimer(plugin, () -> player.damage(1.0), 20L, 20L);
                fuegoTasks.put(player.getUniqueId(), task);
            }
        }
    }

    private void cancelTask(UUID id) {
        BukkitTask task = fuegoTasks.remove(id);
        if (task != null) {
            task.cancel();
        }
    }

    private boolean hasRequiredArmor(Player player, Map<Settings.ArmorSlot, Material> required) {
        ItemStack helmet = player.getInventory().getHelmet();
        ItemStack chest = player.getInventory().getChestplate();
        ItemStack legs = player.getInventory().getLeggings();
        ItemStack boots = player.getInventory().getBoots();

        if (!matches(required.get(Settings.ArmorSlot.HEAD), helmet)) return false;
        if (!matches(required.get(Settings.ArmorSlot.CHEST), chest)) return false;
        if (!matches(required.get(Settings.ArmorSlot.LEGS), legs)) return false;
        if (!matches(required.get(Settings.ArmorSlot.FEET), boots)) return false;
        return true;
    }

    private boolean matches(Material required, ItemStack stack) {
        if (required == null || required == Material.AIR) return true;
        return stack != null && stack.getType() == required;
    }

    private void applyHieloEffects(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 1, false, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 40, 1, false, false, false));
    }

    private void removeHieloEffects(Player player) {
        player.removePotionEffect(PotionEffectType.SLOW);
        player.removePotionEffect(PotionEffectType.SLOW_DIGGING);
    }
}
