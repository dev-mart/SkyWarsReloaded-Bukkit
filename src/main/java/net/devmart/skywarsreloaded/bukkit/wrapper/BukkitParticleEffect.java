package net.devmart.skywarsreloaded.bukkit.wrapper;

import net.devmart.skywarsreloaded.api.SkyWarsReloaded;
import net.devmart.skywarsreloaded.core.wrapper.AbstractParticleEffect;
import org.bukkit.Particle;

public class BukkitParticleEffect extends AbstractParticleEffect {

    protected Particle particle;

    public BukkitParticleEffect(SkyWarsReloaded skywars, String input) {
        super(skywars, input);

        try {
            particle = Particle.valueOf(getType());
        } catch (Exception e) {
            particle = null;
            skywars.getLogger().error("Failed to load Bukkit particle effect from string '%s' because the type '%s' is not a valid Bukkit particle.", input, getType());
        }
    }

    public Particle getBukkitParticle() {
        return this.particle;
    }

}
