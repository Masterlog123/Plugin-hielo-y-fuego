package com.example.oxygenplugin;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class OxygenPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();

        if (world.getName().equals(getConfig().getString("world.name")) && player.getLocation().getBlockY() < 14) {
            if (!player.getInventory().contains(Material.TURTLE_HELMET) || !player.getInventory().contains(Material.POTION)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 40, 1, false, false));
            }
        }
    }

    @EventHandler
    public void onHelmetEquip(PlayerArmorChangeEvent event) {
        Player player = event.getPlayer();
        if (event.getSlotType() == EquipmentSlot.HEAD) {
            if (event.getNewItem() != null && event.getNewItem().getType() == Material.TURTLE_HELMET) {
                if (!event.getNewItem().getItemMeta().hasDisplayName() || !event.getNewItem().getItemMeta().getDisplayName().equals("Casco de Oxígeno")) {
                    ItemStack helmet = event.getNewItem();
                    ItemMeta meta = helmet.getItemMeta();
                    List<String> lore = new ArrayList<>();
                    lore.add("Permite respirar en el espacio");
                    lore.add("Durabilidad: 100%");
                    meta.setLore(lore);
                    meta.setDisplayName("Casco de Oxígeno");
                    helmet.setItemMeta(meta);
                    helmet.setDurability((short)0);
                }
            }
        }
    }

    @EventHandler
    public void onItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (item.getType() == Material.POTION && item.hasItemMeta() && item.getItemMeta().hasLore() && item.getItemMeta().getLore().contains("Agua Quemada (25%)")) {
            int currentDurability = Integer.parseInt(item.getItemMeta().getLore().get(1).split(": ")[1].replace("%", ""));
            int newDurability = currentDurability - 25;
            if (newDurability <= 0) {
                player.getInventory().remove(item);
            } else {
                ItemMeta meta = item.getItemMeta();
                List<String> lore = new ArrayList<>();
                lore.add("Agua Quemada (" + newDurability + "%)");
                lore.add("Usar en casco de oxígeno para rellenar durabilidad");
                meta.setLore(lore);
                item.setItemMeta(meta);
            }
        }
    }
}
public class OxygenPlugin extends JavaPlugin {

  private Set<UUID> playersWithHelmet = new HashSet<>();

  private Map<UUID, Integer> playerWaterBottleDurability = new HashMap<>();

  private Map<UUID, Integer> playerHelmetDurability = new HashMap<>();

  private Map<UUID, Integer> playerHelmetTicks = new HashMap<>();

  private int helmetTicksDelay = 20;

  private int helmetMaxDurability = 100;

  @Override
  public void onEnable() {
    getServer().getPluginManager().registerEvents(new OxygenPluginListener(this), this);
    getCommand("oxygensettings").setExecutor(new OxygenSettingsCommand(this));
  }

  @Override
  public void onDisable() {
    saveConfig();
  }

  public Set<UUID> getPlayersWithHelmet() {
    return playersWithHelmet;
  }

  public Map<UUID, Integer> getPlayerWaterBottleDurability() {
    return playerWaterBottleDurability;
  }

  public Map<UUID, Integer> getPlayerHelmetDurability() {
    return playerHelmetDurability;
  }

  public Map<UUID, Integer> getPlayerHelmetTicks() {
    return playerHelmetTicks;
  }

  public int getHelmetTicksDelay() {
    return helmetTicksDelay;
  }

  public int getHelmetMaxDurability() {
    return helmetMaxDurability;
  }

  public World getPluginWorld() {
    String worldName = getConfig().getString("worldName");
    return getServer().getWorld(worldName);
  }

  public void setPluginWorld(World world) {
    getConfig().set("worldName", world.getName());
  }
}
// Evento que se dispara cuando un jugador mueve su inventario
@EventHandler
public void onInventoryMoveEvent(InventoryMoveItemEvent event) {
    ItemStack item = event.getItem();
    if (!(item.getItemMeta() instanceof Potion)) {
        return;

    Player player = (Player) event.getDestination().getHolder();
    World world = player.getWorld();
import org.bukkit.World;

    if (!world.equals(plugin.getServer().getWorld(world))) {
        return;

    ItemStack[] contents = player.getInventory().getContents();

    for (ItemStack item : contents) {


        if (waterBottle != null && waterBottle.getType().equals(Material.POTION) && waterBottle.getDurability() == 0x0) {
            break;
        }
    }

    if (waterBottle == null) {
        return;
    }

    if (waterBottle.getAmount() == 1) {
        PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
        int duration = getOxygenDuration(potionMeta);

        if (duration > 0) {
            ItemMeta meta = waterBottle.getItemMeta();
            PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(new NamespacedKey(plugin, "duration"), PersistentDataType.INTEGER, duration);
            waterBottle.setItemMeta(meta);

            if (duration <= 0) {
                waterBottle.setType(Material.GLASS_BOTTLE);
            }
        }
    }
}

        }
        if (waterBottleSlot != -1) {
            ItemStack waterBottle = player.getInventory().getItem(waterBottleSlot);
            if (waterBottle.getAmount() == 1) {
                if (waterBottle.getItemMeta().getDisplayName().equals(BURNT_WATER_BOTTLE_NAME)) {
                    int duration = waterBottle.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "duration"), PersistentDataType.INTEGER);
                    if (duration > 0) {
                        duration--;
                        ItemMeta meta = waterBottle.getItemMeta();
                        PersistentDataContainer container = meta.getPersistentDataContainer();
                        container.set(new NamespacedKey(plugin, "duration"), PersistentDataType.INTEGER, duration);
                        waterBottle.setItemMeta(meta);
                        if (duration <= 0) {
                            player.getInventory().setItem(waterBottleSlot, new ItemStack(Material.AIR));
                        }
                    }
                }
            }
        }
    }
}
public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Crear la receta del casco de oxígeno
        ItemStack helmet = new ItemStack(Material.TURTLE_HELMET);
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(this, "casco_oxigeno"), helmet);
        recipe.shape("DDD", "PGP", "III");
        recipe.setIngredient('D', Material.DIAMOND_BLOCK);
        recipe.setIngredient('P', Material.GLASS_PANE);
        recipe.setIngredient('G', Material.AIR);
        recipe.setIngredient('I', Material.IRON_BLOCK);
        getServer().addRecipe(recipe);
    }
}