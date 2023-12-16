package net.devmart.skywarsreloaded.bukkit.command;

import net.devmart.skywarsreloaded.api.SkyWarsReloaded;
import net.devmart.skywarsreloaded.api.wrapper.sender.SWCommandSender;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BukkitSWCommandExecutor implements CommandExecutor, TabCompleter {

    protected SkyWarsReloaded skywars;

    public BukkitSWCommandExecutor(SkyWarsReloaded skywars) {
        this.skywars = skywars;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        SWCommandSender swSender;

        if (sender instanceof Player)
            swSender = skywars.getPlayerManager().getPlayerByUUID(((Player) sender).getUniqueId());
        else if (sender instanceof ConsoleCommandSender)
            swSender = skywars.getConsoleSender();
        else return true;

        this.skywars.getCommandManager().runCommand(swSender, command.getName(), args.length > 0 ? args[0] : "", args);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        SWCommandSender swSender;

        if (sender instanceof Player)
            // creating the player if not existing.
            swSender = skywars.getPlayerManager().getPlayerByUUID(((Player) sender).getUniqueId());
        else if (sender instanceof ConsoleCommandSender)
            swSender = skywars.getConsoleSender();
        else return new ArrayList<>();

        return this.skywars.getCommandManager().runTabCompletion(swSender, command.getName(), args.length > 0 ? args[0] : "", args);
    }

}
