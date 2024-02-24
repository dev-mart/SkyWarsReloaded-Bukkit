package net.devmart.skywarsreloaded.bukkit.wrapper;

import net.devmart.skywarsreloaded.api.SkyWarsReloaded;
import net.devmart.skywarsreloaded.api.wrapper.entity.SWPlayer;
import net.devmart.skywarsreloaded.bukkit.wrapper.entity.BukkitSWPlayer;
import net.devmart.skywarsreloaded.core.wrapper.AbstractPotionEffect;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BukkitPotionEffect extends AbstractPotionEffect {

    protected PotionEffect effect;

    public BukkitPotionEffect(SkyWarsReloaded skywars, PotionEffect bukkitEffect) {
        super(skywars, bukkitEffect.getType().getName(), bukkitEffect.getDuration(), bukkitEffect.getAmplifier(), bukkitEffect.hasParticles());
        this.effect = bukkitEffect;
    }

    public BukkitPotionEffect(SkyWarsReloaded skywars, String input) {
        super(skywars, input);
        try {
            effect = new PotionEffect(PotionEffectType.getByKey(NamespacedKey.fromString(getType())), getDuration(), getStrength(), true, showParticles());
        } catch (Exception e) {
            skywars.getLogger().error(
                    "Failed to load bukkit effect from string %s. Extracted values: type=%s, duration=%d, strength=%d (%s)",
                    input, getType(), getDuration(), getStrength(), e.getClass().getName() + ": " + e.getLocalizedMessage()
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
        if (!(obj instanceof BukkitPotionEffect)) return false;
        BukkitPotionEffect other = (BukkitPotionEffect) obj;
        return other.effect.equals(this.effect);
    }
}
