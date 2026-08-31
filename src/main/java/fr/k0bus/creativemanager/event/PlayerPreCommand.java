package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.CreativeManager;
import fr.k0bus.creativemanager.services.CommandBlacklist;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerPreCommand implements Listener {

  CreativeManager plugin;

  public PlayerPreCommand(CreativeManager plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
    String cmd = e.getMessage().substring(1);
    if (CommandBlacklist.isBlocked(e.getPlayer(), cmd)) {
      e.setCancelled(true);
      CommandBlacklist.notifyBlocked(e.getPlayer());
    }
  }
}
