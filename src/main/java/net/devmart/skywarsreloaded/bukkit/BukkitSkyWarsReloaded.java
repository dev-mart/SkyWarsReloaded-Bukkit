package net.devmart.skywarsreloaded.bukkit;

import net.devmart.skywarsreloaded.api.utils.properties.ConfigProperties;
import net.devmart.skywarsreloaded.bukkit.command.BukkitSWCommandExecutor;
import net.devmart.skywarsreloaded.bukkit.game.loader.SchemWorldLoader;
import net.devmart.skywarsreloaded.bukkit.game.loader.SlimeWorldLoader;
import net.devmart.skywarsreloaded.bukkit.listener.BukkitSWEventListener;
import net.devmart.skywarsreloaded.bukkit.managers.*;
import net.devmart.skywarsreloaded.bukkit.utils.BukkitPlatformUtils;
import net.devmart.skywarsreloaded.bukkit.utils.BukkitSWLogger;
import net.devmart.skywarsreloaded.bukkit.utils.proxy.BungeeCordMessaging;
import net.devmart.skywarsreloaded.bukkit.wrapper.scheduler.BukkitSWScheduler;
import net.devmart.skywarsreloaded.bukkit.wrapper.sender.BukkitSWConsoleSender;
import net.devmart.skywarsreloaded.bukkit.wrapper.server.BukkitSWServer;
import net.devmart.skywarsreloaded.core.AbstractSkyWarsReloaded;
import net.devmart.skywarsreloaded.core.manager.CoreCageManager;
import net.devmart.skywarsreloaded.core.manager.CoreSWCommandManager;
import net.devmart.skywarsreloaded.core.manager.CoreUnlockablesManager;
import net.devmart.skywarsreloaded.core.manager.gameinstance.CoreRemoteGameInstanceManager;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class BukkitSkyWarsReloaded extends AbstractSkyWarsReloaded {

    private final BukkitSkyWarsReloadedPlugin plugin;

    public BukkitSkyWarsReloaded(BukkitSkyWarsReloadedPlugin plugin) {
        this.plugin = plugin;
    }

    // Internal Utils

    @Override
    public void initCommandManager() {
        setCommandManager(new CoreSWCommandManager(this));
    }

    @Override
    public void initCommands() {
        BukkitSWCommandExecutor ex = new BukkitSWCommandExecutor(this);

        PluginCommand plCmdSW = plugin.getCommand("skywars");
        if (plCmdSW != null) {
            plCmdSW.setExecutor(ex);
            plCmdSW.setTabCompleter(ex);
        }
        PluginCommand plCmdSWMap = plugin.getCommand("skywarsmap");
        if (plCmdSWMap != null) {
            plCmdSWMap.setExecutor(ex);
            plCmdSWMap.setTabCompleter(ex);
        }
        PluginCommand plCmdSWKit = plugin.getCommand("skywarskit");
        if (plCmdSWKit != null) {
            plCmdSWKit.setExecutor(ex);
            plCmdSWKit.setTabCompleter(ex);
        }
    }

    @Override
    public void registerDefaultHooks() {
        super.registerDefaultHooks();

        getHookManager().registerHook(new BukkitSWVaultHook(this));
    }

    @Override
    public void initConsoleSender() {
        setConsoleSender(new BukkitSWConsoleSender(this.getBukkitPlugin().getServer().getConsoleSender()));
    }

    @Override
    public void initPlatformEventListeners() {
        BukkitSWEventListener bukkitEventListener = new BukkitSWEventListener(this);
        this.plugin.getServer().getPluginManager().registerEvents(bukkitEventListener, this.plugin);
        setPlatformEventListener(bukkitEventListener);
    }

    @Override
    public void initGameInstanceManager() {
        if (getConfig().getBoolean(ConfigProperties.PROXY_ENABLED) &&
                getConfig().getString(ConfigProperties.PROXY_SERVER_TYPE).equalsIgnoreCase("GAME")) {
            setGameInstanceManager(new CoreRemoteGameInstanceManager(this));
        } else {
            setGameInstanceManager(new BukkitGameInstanceManager(this));
        }
    }

    @Override
    public void initKitManager() {
        setKitManager(new BukkitKitManager(this));
    }

    @Override
    public void initCageManager() {
        setCageManager(new CoreCageManager(this));
    }

    @Override
    protected void initUnlockablesManager() {
        setUnlockablesManager(new CoreUnlockablesManager(this));
    }

    @Override
    public void initLogger() {
        setLogger(new BukkitSWLogger(this, this.plugin.getLogger(), true));
    }

    @Override
    public void initNMSManager() throws IllegalStateException {
        setNMSManager(new BukkitNMSManager(this));
    }

    @Override
    protected void initPluginMessaging() {
        setPluginMessaging(new BungeeCordMessaging(this.plugin));
    }

    @Override
    public void initPlatformUtils() {
        setPlatformUtils(new BukkitPlatformUtils(this));
    }

    @Override
    public void initPlayerDataManager() {
        setPlayerDataManager(new BukkitSWPlayerDataManager(this));
    }

    @Override
    public void initPlayerManager() {
        setPlayerManager(new BukkitPlayerManager(this));
    }

    @Override
    protected void initScheduler() {
        setScheduler(new BukkitSWScheduler(this));
    }

    @Override
    protected void initEntityManager() {
        setEntityManager(new BukkitEntityManager(this));
    }

    @Override
    protected void initScoreboardManager() {
        setScoreboardManager(new BukkitScoreboardManager(this));
    }

    @Override
    protected void initItemManager() {
        setItemManager(new BukkitItemManager(this));
    }

    @Override
    protected void initInventoryManager() {
        setInventoryManager(new BukkitInventoryManager(this));
    }

    @Override
    public void initServer() {
        setServer(new BukkitSWServer(this));
    }

    @Override
    public void initWorldLoader() {
        if (getConfig().getBoolean(ConfigProperties.ENABLE_SLIME_WORLD_MANAGER, false)) {
            if (((Plugin) plugin).getServer().getPluginManager().isPluginEnabled("SlimeWorldManager")) {
                // SWM found! yay
                setWorldLoader(new SlimeWorldLoader(this));
                return;
            }
        }

        // Fallback to schematic loading
        setWorldLoader(new SchemWorldLoader(this));
    }

    @Override
    public void initYAMLManager() {
        setYAMLManager(new BukkitYAMLManager(this));
    }

    public BukkitSkyWarsReloadedPlugin getBukkitPlugin() {
        return this.plugin;
    }

    // Getters

    @Override
    public File getDataFolder() {
        return plugin.getDataFolder();
    }

    @Override
    public String getVersion() {
        return this.plugin.getDescription().getVersion();
    }

    @Override
    public String getMinecraftVersion() {
        return this.plugin.getServer().getVersion();
    }

    @Override
    public String getPlatformVersion() {
        // todo not sure if this is correct, TBD
        return this.plugin.getServer().getClass().getPackage().getImplementationVersion();
    }

    @Override
    public void disableSkyWars() {
        HandlerList.unregisterAll((BukkitSWEventListener) this.getPlatformEventListener());
        this.getScheduler().cancelAll();
    }

    @Override
    public BukkitSWServer getServer() {
        return (BukkitSWServer) super.getServer();
    }
}
