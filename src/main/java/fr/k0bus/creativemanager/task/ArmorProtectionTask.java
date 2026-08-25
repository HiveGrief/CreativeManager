package fr.k0bus.creativemanager.task;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.settings.Protections;
import fr.k0bus.k0buscore.utils.ItemsUtils;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * Armor protection task class.
 *
 * <p>Continuously re-applies the configured "creative_armor" set on every Creative player, so
 * that any armor a player manages to get into an armor slot through a path not already covered
 * by the click / equip protections (dispensers, commands, other plugins, ...) gets replaced
 * again a moment later.
 */
public class ArmorProtectionTask {

  /** Enforcement interval, in ticks (1 second). */
  private static final long PERIOD_TICKS = 20L;

  private ArmorProtectionTask() {}

  /**
   * Run interval.
   *
   * @param plugin the plugin.
   * @return the task id.
   */
  public static int run(CreativeManager plugin) {
    return Bukkit.getScheduler()
        .scheduleSyncRepeatingTask(plugin, ArmorProtectionTask::tick, PERIOD_TICKS, PERIOD_TICKS);
  }

  private static void tick() {
    if (!CreativeManager.getSettings().getProtection(Protections.ARMOR)) return;
    ConfigurationSection cs =
        CreativeManager.getSettings().getConfiguration().getConfigurationSection("creative_armor");
    if (cs == null) return;

    for (Player p : Bukkit.getOnlinePlayers()) {
      if (!p.getGameMode().equals(GameMode.CREATIVE)) continue;
      if (p.hasPermission("creativemanager.bypass.armor")) continue;

      PlayerInventory inv = p.getInventory();
      enforceSlot(inv.getHelmet(), cs.getConfigurationSection("helmet"), p, inv::setHelmet);
      enforceSlot(
          inv.getChestplate(), cs.getConfigurationSection("chestplate"), p, inv::setChestplate);
      enforceSlot(inv.getLeggings(), cs.getConfigurationSection("leggings"), p, inv::setLeggings);
      enforceSlot(inv.getBoots(), cs.getConfigurationSection("boots"), p, inv::setBoots);
    }
  }

  /**
   * Reset a single armor slot to its configured item if it doesn't already match.
   *
   * @param current the item currently in the slot.
   * @param cs the configuration section describing the expected item.
   * @param p the player, used to resolve placeholders in the configured item.
   * @param setter the setter used to apply the expected item back on the slot.
   */
  private static void enforceSlot(
      ItemStack current, ConfigurationSection cs, Player p, Consumer<ItemStack> setter) {
    ItemStack expected = ItemsUtils.fromConfiguration(cs, p);
    if (current != null && current.isSimilar(expected)) return;
    if (current == null && expected.getType().equals(Material.AIR)) return;
    setter.accept(expected);
  }
}
