package net.devmart.skywarsreloaded.bukkit.wrapper.entity;

import net.devmart.skywarsreloaded.api.SkyWarsReloaded;
import net.devmart.skywarsreloaded.api.wrapper.entity.SWDroppedItem;
import net.devmart.skywarsreloaded.bukkit.utils.BukkitItem;
import org.bukkit.entity.Item;

import java.util.UUID;

public class BukkitSWDroppedItem extends BukkitSWEntity implements SWDroppedItem {

    private final SkyWarsReloaded plugin;
    private final Item item;

    public BukkitSWDroppedItem(SkyWarsReloaded plugin, Item entity) {
        super(plugin, entity);
        this.plugin = plugin;
        this.item = entity;
    }

    @Override
    public net.devmart.skywarsreloaded.api.utils.Item getItem() {
        return new BukkitItem(plugin, item.getItemStack());
    }

    @Override
    public void setItem(net.devmart.skywarsreloaded.api.utils.Item newItem) {
        item.setItemStack(((BukkitItem) newItem).getBukkitItem());
    }

    @Override
    public int getPickupDelay() {
        return item.getPickupDelay();
    }

    @Override
    public void setPickupDelay(int delay) {
        item.setPickupDelay(delay);
    }

    @Override
    public void setUnlimitedLifetime(boolean unlimitedLifetime) {
        item.setUnlimitedLifetime(unlimitedLifetime);
    }

    @Override
    public boolean isUnlimitedLifetime() {
        return item.isUnlimitedLifetime();
    }

    @Override
    public UUID getOwner() {
        return item.getOwner();
    }

    @Override
    public void setOwner(UUID owner) {
        item.setOwner(owner);
    }

    @Override
    public UUID getThrower() {
        return item.getThrower();
    }

    @Override
    public void setThrower(UUID thrower) {
        item.setThrower(thrower);
    }
}
