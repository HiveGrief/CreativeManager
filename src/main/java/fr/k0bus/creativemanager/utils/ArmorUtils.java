package fr.k0bus.creativemanager.utils;

import org.bukkit.Material;

/** Armor related utility class. */
public class ArmorUtils {

  private ArmorUtils() {}

  /**
   * Whether the material would end up in an armor slot (helmet/chestplate/leggings/boots slot),
   * either by being placed there directly or by auto-equipping (right-click, shift-click).
   *
   * @param material the material.
   * @return True if yes, otherwise false.
   */
  public static boolean isArmorMaterial(Material material) {
    String name = material.name();
    return name.endsWith("_HELMET")
        || name.endsWith("_CHESTPLATE")
        || name.endsWith("_LEGGINGS")
        || name.endsWith("_BOOTS")
        || name.endsWith("_HEAD")
        || name.endsWith("_SKULL")
        || name.equals("ELYTRA")
        || name.equals("CARVED_PUMPKIN");
  }
}
