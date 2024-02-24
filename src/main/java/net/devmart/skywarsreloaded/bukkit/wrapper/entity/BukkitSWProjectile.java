package net.devmart.skywarsreloaded.bukkit.wrapper.entity;

import net.devmart.skywarsreloaded.api.SkyWarsReloaded;
import net.devmart.skywarsreloaded.api.wrapper.entity.SWEntity;
import net.devmart.skywarsreloaded.api.wrapper.entity.SWPlayer;
import net.devmart.skywarsreloaded.api.wrapper.entity.SWProjectile;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.ProjectileSource;

public class BukkitSWProjectile extends BukkitSWEntity implements SWProjectile {

    protected Projectile projectile;

    public BukkitSWProjectile(SkyWarsReloaded skywars, Projectile projectile) {
        super(skywars, projectile);
        this.projectile = projectile;
    }

    @Override
    public SWEntity getShooter() {
        if (!(projectile.getShooter() instanceof Entity)) return null;

        return skywars.getEntityManager().getEntityByUUID(((Entity) projectile.getShooter()).getUniqueId());
    }

    @Override
    public void setShooter(SWEntity shooter) {
        if (!(projectile.getShooter() instanceof Entity)) return;

        projectile.setShooter((ProjectileSource) ((BukkitSWEntity) shooter).getEntity());
    }

    @Override
    public SWPlayer getOwner() {
        SWEntity shooter = getShooter();
        if (shooter instanceof SWPlayer) {
            return (SWPlayer) shooter;
        }
        return null;
    }

    @Override
    public void setOwner(SWPlayer player) {
        this.setShooter(player);
    }
}
