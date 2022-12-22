package net.devmart.skywarsreloaded.bukkit.managers;

import net.devmart.skywarsreloaded.api.SkyWarsReloaded;
import net.devmart.skywarsreloaded.api.manager.SWInventoryManager;
import net.devmart.skywarsreloaded.api.wrapper.server.SWInventory;
import net.devmart.skywarsreloaded.bukkit.wrapper.server.BukkitSWInventory;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

public class BukkitInventoryManager implements SWInventoryManager {

    private final SkyWarsReloaded plugin;
    private final HashMap<Inventory, SWInventory> inventories;

    public BukkitInventoryManager(SkyWarsReloaded plugin) {
        this.plugin = plugin;
        inventories = new HashMap<>();
    }


    @Override
    public void registerInventory(SWInventory inventory) {
        inventories.put(((BukkitSWInventory) inventory).getBukkitInventory(), inventory);
    }

    @Override
    public void unregisterInventory(SWInventory inventory) {
        inventories.remove(((BukkitSWInventory) inventory).getBukkitInventory());
    }

    @Override
    public void replaceInventory(SWInventory oldInventory, SWInventory newInventory) {
        unregisterInventory(oldInventory);
        registerInventory(newInventory);
    }

    public SWInventory getSWInventory(Inventory inv) {
        return this.inventories.get(inv);
    }
}
