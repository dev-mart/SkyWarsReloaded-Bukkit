package net.devmart.skywarsreloaded.bukkit.wrapper.server;

import net.devmart.skywarsreloaded.api.SkyWarsReloaded;
import net.devmart.skywarsreloaded.api.utils.Item;
import net.devmart.skywarsreloaded.api.wrapper.entity.SWPlayer;
import net.devmart.skywarsreloaded.api.wrapper.server.SWInventory;
import net.devmart.skywarsreloaded.bukkit.utils.BukkitItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;
import java.util.stream.Collectors;

public class BukkitSWInventory implements SWInventory {

    private final SkyWarsReloaded plugin;
    private final Inventory inventory;
    private final String title;
    private final int size;

    public BukkitSWInventory(SkyWarsReloaded plugin, String title, int size) {
        this.plugin = plugin;
        this.title = title;
        this.size = size;
        inventory = Bukkit.getServer().createInventory(null, size, title);

        // todo add support for different inventory types in the future?
    }

    public BukkitSWInventory(SkyWarsReloaded plugin, String title, String inventoryType) {
        this.plugin = plugin;
        this.title = title;
        this.size = 0;
        inventory = Bukkit.getServer().createInventory(null, InventoryType.valueOf(inventoryType.toUpperCase()), title);
    }

    public BukkitSWInventory(SkyWarsReloaded plugin, Inventory bukkitInventory, String title) {
        this.plugin = plugin;
        this.inventory = bukkitInventory;
        this.title = title;
        this.size = bukkitInventory.getSize();
    }

    @Override
    public void clear() {
        inventory.clear();
    }

    @Override
    public void setItem(int slot, Item item) {
        inventory.setItem(slot, ((BukkitItem) item).getBukkitItem());
    }

    @Override
    public Item getItem(int slot) {
        return new BukkitItem(plugin, inventory.getItem(slot));
    }

    @Override
    public Item[] getContents() {
        final ItemStack[] bukkitContents = inventory.getContents();
        final Item[] contents = new Item[bukkitContents.length];

        for (int i = 0; i < bukkitContents.length; i++) {
            final ItemStack item = bukkitContents[i];
            contents[i] = item == null || item.getType() == Material.AIR ? null : new BukkitItem(plugin, item);
        }

        return contents;
    }

    @Override
    public void setContents(Item[] items) {
        final ItemStack[] contents = new ItemStack[inventory.getSize()];

        for (int i = 0; i < items.length; i++) {
            final Item item = items[i];
            contents[i] = item == null || item.getMaterial().equalsIgnoreCase("AIR") ? null : ((BukkitItem) item).getBukkitItem();
        }

        inventory.setContents(contents);
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    public Inventory getBukkitInventory() {
        return this.inventory;
    }

    @Override
    public List<SWPlayer> getViewers() {
        return this.inventory.getViewers().stream()
                .map(viewer -> plugin.getPlayerManager().getPlayerByUUID(viewer.getUniqueId()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isViewing(SWPlayer player) {
        return this.inventory.getViewers().stream().anyMatch(viewer -> viewer.getUniqueId().equals(player.getUuid()));
    }

    @Override
    public void clearArmor() {
        if (inventory.getType() == InventoryType.PLAYER) {
            ((PlayerInventory) inventory).setArmorContents(new ItemStack[4]);
        }
    }

    @Override
    public void setItemInOffHand(Item item) {
        if (inventory.getType() == InventoryType.PLAYER) {
            ((PlayerInventory) inventory).setItemInOffHand(((BukkitItem) item).getBukkitItem());
        }
    }

    @Override
    public int getHeldItemSlot() {
        if (inventory.getType() == InventoryType.PLAYER) {
            return ((PlayerInventory) inventory).getHeldItemSlot();
        }
        return 0;
    }
}
