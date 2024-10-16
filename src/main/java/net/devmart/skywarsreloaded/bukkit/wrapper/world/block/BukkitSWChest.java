package net.devmart.skywarsreloaded.bukkit.wrapper.world.block;

import net.devmart.skywarsreloaded.api.wrapper.Item;
import net.devmart.skywarsreloaded.api.wrapper.world.block.SWChest;
import net.devmart.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.devmart.skywarsreloaded.bukkit.wrapper.BukkitItem;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

public class BukkitSWChest extends BukkitSWBlock implements SWChest {

    private final Chest chest;

    public BukkitSWChest(BukkitSkyWarsReloaded skywars, Chest chest) {
        super(skywars, chest.getBlock());
        this.chest = chest;
    }

    @Override
    public Item[] getContents() {
        final ItemStack[] bukkitContents = chest.getBlockInventory().getContents();
        final Item[] contents = new Item[bukkitContents.length];

        for (int i = 0; i < bukkitContents.length; i++) {
            contents[i] =bukkitContents[i] == null ? null : new BukkitItem(this.skywars, bukkitContents[i]);
        }

        return contents;
    }

    @Override
    public int getSize() {
        return chest.getBlockInventory().getSize();
    }

    @Override
    public void setContents(Item[] items) {
        ItemStack[] parsedItems = new ItemStack[items.length];

        for (int i = 0; i < items.length; i++) {
            Item item = items[i];
            if (item instanceof BukkitItem) parsedItems[i] = ((BukkitItem) item).getBukkitItem();
            else parsedItems[i] = null;
        }

        chest.getBlockInventory().setContents(parsedItems);
    }

    @Override
    public void setItem(int slot, Item item) {
        if (item instanceof BukkitItem) chest.getBlockInventory().setItem(slot, ((BukkitItem) item).getBukkitItem());
    }

    @Override
    public void clearContents() {
        chest.getBlockInventory().clear();
    }
}
