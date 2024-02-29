package net.devmart.skywarsreloaded.bukkit.unlockable.kits;

import net.devmart.skywarsreloaded.api.SkyWarsReloaded;
import net.devmart.skywarsreloaded.api.wrapper.entity.SWPlayer;
import net.devmart.skywarsreloaded.bukkit.wrapper.BukkitPotionEffect;
import net.devmart.skywarsreloaded.core.unlockable.kits.AbstractSWKit;

import java.util.HashSet;
import java.util.Set;

public class BukkitSWKit extends AbstractSWKit {

    protected Set<BukkitPotionEffect> effects;

    public BukkitSWKit(SkyWarsReloaded skywars, String id) {
        super(skywars, id);
        this.effects = new HashSet<>();
    }

    @Override
    public synchronized void loadData() {
        super.loadData();

        getEffects().forEach(s -> {
            try {
                effects.add(new BukkitPotionEffect(skywars, s));
            } catch (Exception ignored) {
            }
        });
    }

    @Override
    public void applyEffects(SWPlayer player) {
        effects.forEach(bukkitPotionEffect -> {
            if (bukkitPotionEffect != null) bukkitPotionEffect.applyToPlayer(player);
        });
    }

}
