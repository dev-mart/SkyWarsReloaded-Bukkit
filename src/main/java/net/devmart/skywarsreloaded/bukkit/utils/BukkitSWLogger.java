package net.devmart.skywarsreloaded.bukkit.utils;

import net.devmart.skywarsreloaded.api.SkyWarsReloaded;
import net.devmart.skywarsreloaded.core.utils.AbstractSWLogger;

import java.util.logging.Logger;

public class BukkitSWLogger extends AbstractSWLogger {

    protected final Logger bukkitLogger;

    public BukkitSWLogger(SkyWarsReloaded pluginIn, Logger bukkitLoggerIn, boolean isDebugModeActive) {
        super(pluginIn, isDebugModeActive);
        this.bukkitLogger = bukkitLoggerIn;
    }

    protected String formatMessage(String message, Object... args) {
        if (args == null || args.length == 0) return message;
        return String.format(message, args);
    }

    @Override
    public void info(String message, Object... args) {
        this.bukkitLogger.info(formatMessage(message, args));
    }

    @Override
    public void warn(String message, Object... args) {
        this.bukkitLogger.warning(formatMessage(message, args));
    }

    @Override
    public void error(String message, Object... args) {
        this.bukkitLogger.severe(formatMessage(message, args));
    }
}
