package net.devmart.skywarsreloaded.bukkit.wrapper.sender;

import net.devmart.skywarsreloaded.api.SkyWarsReloaded;
import net.devmart.skywarsreloaded.core.wrapper.sender.AbstractSWCommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class BukkitSWCommandSender extends AbstractSWCommandSender {

    private final ConsoleCommandSender sender;

    public BukkitSWCommandSender(SkyWarsReloaded plugin, ConsoleCommandSender sender) {
        super(plugin);
        this.sender = sender;
    }

    @Override
    public void sendMessage(String message) {
        sender.sendMessage(message);
    }

    @Override
    public boolean hasPermission(String permission) {
        return sender.hasPermission(permission);
    }

    @Override
    public String getName() {
        return sender.getName();
    }
}
