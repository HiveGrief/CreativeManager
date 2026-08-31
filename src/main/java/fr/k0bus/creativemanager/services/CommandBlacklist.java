package fr.k0bus.creativemanager.services;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.settings.Protections;
import fr.k0bus.creativemanager.utils.CMUtils;
import fr.k0bus.creativemanager.utils.SearchUtils;
import java.util.List;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class CommandBlacklist {

  private CommandBlacklist() {}

  public static boolean isBlocked(Player player, String commandLine) {
    if (!CreativeManager.getSettings().getProtection(Protections.COMMANDS)) return false;
    if (!player.getGameMode().equals(GameMode.CREATIVE)) return false;
    if (player.hasPermission("creativemanager.bypass.blacklist.commands")) return false;
    String cmd = commandLine.toLowerCase();
    List<String> list = CreativeManager.getSettings().getCommandBL();
    boolean whitelist =
        CreativeManager.getSettings()
            .getConfiguration()
            .getString("list.mode.commands")
            .equals("whitelist");
    boolean inList = SearchUtils.inList(list, cmd);
    return whitelist != inList;
  }

  public static void notifyBlocked(Player player) {
    if (CreativeManager.getSettings().getConfiguration().getBoolean("send-player-messages"))
      CMUtils.sendMessage(player, "blacklist.commands");
  }
}
