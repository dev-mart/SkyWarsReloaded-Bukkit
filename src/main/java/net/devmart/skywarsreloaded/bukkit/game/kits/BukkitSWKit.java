package net.devmart.skywarsreloaded.bukkit.game.kits;

import net.devmart.skywarsreloaded.api.SkyWarsReloaded;
import net.devmart.skywarsreloaded.api.utils.Item;
import net.devmart.skywarsreloaded.api.wrapper.entity.SWPlayer;
import net.devmart.skywarsreloaded.api.wrapper.server.SWInventory;
import net.devmart.skywarsreloaded.bukkit.utils.BukkitEffect;
import net.devmart.skywarsreloaded.core.game.kits.AbstractSWKit;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BukkitSWKit extends AbstractSWKit {

    private Item helmet;
    private Item chestplate;
    private Item leggings;
    private Item boots;
    private Item offHand;
    private HashMap<Integer, Item> inventory;

    private List<BukkitEffect> effects;

    public BukkitSWKit(SkyWarsReloaded skywars, String id) {
        super(skywars, id);
    }

    @Override
    public synchronized void loadData() {
        super.loadData();
        updateItems();
    }

    public void updateItems() {
        this.helmet = getHelmet();
        this.chestplate = getChestplate();
        this.leggings = getLeggings();
        this.boots = getBoots();
        this.offHand = getOffHand();

        this.inventory = new HashMap<>(getContents());

        this.effects = new ArrayList<>();
        getEffects().forEach(s -> {
            try {
                effects.add(new BukkitEffect(skywars, s));
            } catch (Exception ignored) {
            }
        });
    }

    @Override
    public void giveToPlayer(SWPlayer swp) {
        final SWInventory playerInv = swp.getInventory();

        // clearing inventory and effects.
        playerInv.clear();
        for (PotionEffectType value : PotionEffectType.values()) {
            if (value != null) swp.removePotionEffect(value.getName());
        }

        swp.setHelmet(this.helmet);
        swp.setChestplate(this.chestplate);
        swp.setLeggings(this.leggings);
        swp.setBoots(this.boots);
        this.inventory.forEach(playerInv::setItem);

        if (skywars.getUtils().getServerVersion() >= 9) {
            playerInv.setItemInOffHand(this.offHand);
        }

        effects.forEach(bukkitEffect -> {
            if (bukkitEffect != null) bukkitEffect.applyToPlayer(swp);
        });
    }
}
