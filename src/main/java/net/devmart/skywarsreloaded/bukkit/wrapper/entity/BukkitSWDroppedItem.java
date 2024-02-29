package net.devmart.skywarsreloaded.bukkit.wrapper.entity;

import net.devmart.skywarsreloaded.api.wrapper.entity.SWDroppedItem;
import net.devmart.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.devmart.skywarsreloaded.bukkit.wrapper.BukkitItem;
import org.bukkit.entity.Item;

import java.util.UUID;

public class BukkitSWDroppedItem extends BukkitSWEntity implements SWDroppedItem {

    private final Item item;

    public BukkitSWDroppedItem(BukkitSkyWarsReloaded skywars, Item entity) {
        super(skywars, entity);
        this.item = entity;
    }

    @Override
    public net.devmart.skywarsreloaded.api.wrapper.Item getItem() {
        return new BukkitItem(skywars, item.getItemStack());
    }

    @Override
    public void setItem(net.devmart.skywarsreloaded.api.wrapper.Item newItem) {
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
