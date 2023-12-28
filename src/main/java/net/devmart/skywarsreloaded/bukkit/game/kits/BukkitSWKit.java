package net.devmart.skywarsreloaded.bukkit.game.kits;

import net.devmart.skywarsreloaded.api.SkyWarsReloaded;
import net.devmart.skywarsreloaded.api.wrapper.entity.SWPlayer;
import net.devmart.skywarsreloaded.bukkit.utils.BukkitEffect;
import net.devmart.skywarsreloaded.core.game.kits.AbstractSWKit;

import java.util.HashSet;
import java.util.Set;

public class BukkitSWKit extends AbstractSWKit {

    protected Set<BukkitEffect> effects;

    public BukkitSWKit(SkyWarsReloaded skywars, String id) {
        super(skywars, id);
        this.effects = new HashSet<>();
    }

    @Override
    public synchronized void loadData() {
        super.loadData();

        getEffects().forEach(s -> {
            try {
                effects.add(new BukkitEffect(skywars, s));
            } catch (Exception ignored) {
            }
        });
    }

    @Override
    public void applyEffects(SWPlayer player) {
        effects.forEach(bukkitEffect -> {
            if (bukkitEffect != null) bukkitEffect.applyToPlayer(player);
        });
    }

}
