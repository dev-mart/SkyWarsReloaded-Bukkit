package net.devmart.skywarsreloaded.bukkit.wrapper.sender;

import net.devmart.skywarsreloaded.api.SkyWarsReloaded;
import net.devmart.skywarsreloaded.core.wrapper.sender.AbstractSWConsoleSender;
import org.bukkit.command.ConsoleCommandSender;

public class BukkitSWConsoleSender extends AbstractSWConsoleSender {

    private final ConsoleCommandSender consoleSender;

    public BukkitSWConsoleSender(SkyWarsReloaded plugin, ConsoleCommandSender consoleSenderIn) {
        super(plugin);
        this.consoleSender = consoleSenderIn;
    }

    @Override
    public void sendMessage(String message) {
        consoleSender.sendMessage(message);
    }

}
