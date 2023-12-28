package net.devmart.skywarsreloaded.bukkit.utils;

import net.devmart.skywarsreloaded.api.SkyWarsReloaded;
import net.devmart.skywarsreloaded.api.wrapper.entity.SWPlayer;
import net.devmart.skywarsreloaded.bukkit.wrapper.entity.BukkitSWPlayer;
import net.devmart.skywarsreloaded.core.utils.AbstractEffect;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BukkitEffect extends AbstractEffect {

    private PotionEffect effect;

    public BukkitEffect(SkyWarsReloaded skywars, String input) {
        super(skywars, input);
        try {
            effect = new PotionEffect(PotionEffectType.getByKey(NamespacedKey.fromString(getType())), getDuration(), getStrength(), true, showParticles());
        } catch (Exception e) {
            skywars.getLogger().error(
                    "Failed to load bukkit effect from string %s. Using the default: %d. (%s)",
                    input, 1, e.getClass().getName() + ": " + e.getLocalizedMessage()
            );
        }
    }

    @Override
    public void applyToPlayer(SWPlayer player) {
        if (effect == null) return;
        Player p = ((BukkitSWPlayer) player).getPlayer();
        p.addPotionEffect(effect);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BukkitEffect)) return false;
        BukkitEffect other = (BukkitEffect) obj;
        return other.effect.equals(this.effect);
    }
}
