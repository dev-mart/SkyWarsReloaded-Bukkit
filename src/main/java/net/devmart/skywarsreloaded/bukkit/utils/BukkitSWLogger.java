package net.devmart.skywarsreloaded.bukkit.utils;

import net.devmart.skywarsreloaded.api.SkyWarsReloaded;
import net.devmart.skywarsreloaded.core.utils.AbstractSWLogger;

import java.util.logging.Logger;

public class BukkitSWLogger extends AbstractSWLogger {

    private final Logger bukkitLogger;

    public BukkitSWLogger(SkyWarsReloaded pluginIn, Logger bukkitLoggerIn, boolean isDebugModeActive) {
        super(pluginIn, isDebugModeActive);
        this.bukkitLogger = bukkitLoggerIn;
    }

    @Override
    public void info(String message) {
        this.bukkitLogger.info(message);
    }

    @Override
    public void warn(String message) {
        this.bukkitLogger.warning(message);
    }

    @Override
    public void error(String message) {
        this.bukkitLogger.severe(message);
    }

}
