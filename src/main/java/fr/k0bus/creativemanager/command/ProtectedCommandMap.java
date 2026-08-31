package fr.k0bus.creativemanager.command;

import fr.k0bus.creativemanager.services.CommandBlacklist;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Wraps the server's real {@link CommandMap} so the commands blacklist/whitelist is enforced on
 * every command execution, not only on commands typed directly by the player.
 *
 * <p>{@code PlayerCommandPreprocessEvent} only fires when a player sends a command through chat.
 * Plugins that add command aliases (e.g. CommandAliases) commonly register their own alias as a
 * real command and then re-dispatch the underlying command themselves (via {@code
 * Bukkit.dispatchCommand} / {@code Player#performCommand}), which never fires that event. Every
 * one of those paths still ends up calling {@link CommandMap#dispatch}, so checking here catches
 * the command regardless of how its execution was triggered.
 */
public class ProtectedCommandMap implements CommandMap {

  private final CommandMap delegate;

  public ProtectedCommandMap(CommandMap delegate) {
    this.delegate = delegate;
  }

  @Override
  public void registerAll(String fallbackPrefix, List<Command> commands) {
    delegate.registerAll(fallbackPrefix, commands);
  }

  @Override
  public boolean register(String fallbackPrefix, Command command) {
    return delegate.register(fallbackPrefix, command);
  }

  @Override
  public boolean register(String label, String fallbackPrefix, Command command) {
    return delegate.register(label, fallbackPrefix, command);
  }

  @Override
  public boolean dispatch(CommandSender sender, String commandLine) throws CommandException {
    if (sender instanceof Player && CommandBlacklist.isBlocked((Player) sender, commandLine)) {
      CommandBlacklist.notifyBlocked((Player) sender);
      return true;
    }
    return delegate.dispatch(sender, commandLine);
  }

  @Override
  public void clearCommands() {
    delegate.clearCommands();
  }

  @Override
  public Command getCommand(String name) {
    return delegate.getCommand(name);
  }

  @Override
  public List<String> tabComplete(CommandSender sender, String cmdLine) {
    return delegate.tabComplete(sender, cmdLine);
  }

  @Override
  public List<String> tabComplete(CommandSender sender, String cmdLine, Location location) {
    return delegate.tabComplete(sender, cmdLine, location);
  }
}
