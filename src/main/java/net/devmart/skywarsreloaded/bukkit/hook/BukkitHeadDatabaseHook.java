package net.devmart.skywarsreloaded.bukkit.hook;

import me.arcaniax.hdb.api.HeadDatabaseAPI;
import net.devmart.skywarsreloaded.api.SkyWarsReloaded;
import net.devmart.skywarsreloaded.api.hook.SWHeadDatabaseHook;
import net.devmart.skywarsreloaded.api.utils.Item;
import net.devmart.skywarsreloaded.bukkit.utils.BukkitItem;
import net.devmart.skywarsreloaded.core.hook.AbstractSWHook;
import org.bukkit.inventory.ItemStack;

public class BukkitHeadDatabaseHook extends AbstractSWHook implements SWHeadDatabaseHook {

    protected HeadDatabaseAPI headDatabaseAPI;

    public BukkitHeadDatabaseHook(SkyWarsReloaded skywars) {
        super(skywars);
        this.headDatabaseAPI = null;
    }

    @Override
    public void enable() {
        if (!skywars.getServer().isPluginEnabled("HeadDatabase")) return;

        try {
            this.headDatabaseAPI = new HeadDatabaseAPI();
        } catch (Exception e) {
            skywars.getLogger().warn("Failed to enable HeadDatabase hook. HeadDatabase may not be installed or may not be enabled.");
            return;
        }

        skywars.getLogger().info("HeadDatabase hook has been enabled. You can now use HeadDatabase features such as custom heads.\n" +
                "You can use \"HDB-<head-id>\" as material for any item to get a custom head.");
        setEnabled(true);
    }

    @Override
    public Item getHeadFromId(String id) {
        ItemStack itemstack = this.headDatabaseAPI.getItemHead(id);

        if (itemstack == null) {
            return null;
        }

        return new BukkitItem(skywars, itemstack);
    }
}
