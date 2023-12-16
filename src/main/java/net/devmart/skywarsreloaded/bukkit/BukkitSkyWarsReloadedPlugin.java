package net.devmart.skywarsreloaded.bukkit;

import net.devmart.skywarsreloaded.api.SkyWarsReloaded;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitSkyWarsReloadedPlugin extends JavaPlugin {

    /**
     * Do not put anything in this class other than the onEnable / onDisable methods.
     * Use {@link BukkitSkyWarsReloaded} instead.
     */

    private BukkitSkyWarsReloaded skywars;
    private boolean hasBeenEnabled;

    public BukkitSkyWarsReloadedPlugin() {
        skywars = new BukkitSkyWarsReloaded(this);
        hasBeenEnabled = false;
    }

    @Override
    public void onEnable() {
        skywars.onEnable();
        hasBeenEnabled = true;
    }

    @Override
    public void onDisable() {
        skywars.onDisable();
    }

    /**
     * Use this to override initializers in the {@link SkyWarsReloaded} implementation.
     * Only use this to make major changes to the plugin such as replacing certain parts of the plugin with your
     * own logic.
     * <br><br>
     * Note: You can't use this method after {@link #onEnable()} has been called on SkyWars. Doing so would
     * completely break everything.
     *
     * @param implementation The new {@link SkyWarsReloaded} implementation.
     */
    public void overrideSkyWarsImplementation(BukkitSkyWarsReloaded implementation) {
        if (!hasBeenEnabled) this.skywars = implementation;
        else
            throw new IllegalStateException("You can't change the SkyWars implementation after onEnable() was already called!");
    }

    public BukkitSkyWarsReloaded getSkywars() {
        return skywars;
    }
}
