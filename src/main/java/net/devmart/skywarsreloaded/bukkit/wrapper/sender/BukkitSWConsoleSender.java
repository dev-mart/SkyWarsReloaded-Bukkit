package net.devmart.skywarsreloaded.bukkit.wrapper.sender;

import net.devmart.skywarsreloaded.core.wrapper.sender.AbstractSWConsoleSender;
import org.bukkit.command.ConsoleCommandSender;

public class BukkitSWConsoleSender extends AbstractSWConsoleSender {

    private final ConsoleCommandSender consoleSender;

    public BukkitSWConsoleSender(ConsoleCommandSender consoleSender) {
        this.consoleSender = consoleSender;
    }

    @Override
    public void sendMessage(String message) {
        consoleSender.sendMessage(message);
    }

}
