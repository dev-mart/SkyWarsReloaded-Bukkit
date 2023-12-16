package net.devmart.skywarsreloaded.bukkit.managers;

import net.devmart.skywarsreloaded.api.SkyWarsReloaded;
import net.devmart.skywarsreloaded.api.manager.EntityManager;
import net.devmart.skywarsreloaded.api.wrapper.entity.SWEntity;
import net.devmart.skywarsreloaded.api.wrapper.entity.SWPlayer;
import net.devmart.skywarsreloaded.bukkit.wrapper.entity.BukkitSWEntity;
import net.devmart.skywarsreloaded.bukkit.wrapper.entity.BukkitSWOwnedEntity;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;

import java.util.UUID;

public class BukkitEntityManager implements EntityManager {

    private final SkyWarsReloaded skywars;

    public BukkitEntityManager(SkyWarsReloaded skywars) {
        this.skywars = skywars;
    }

    @Override
    public SWEntity getEntityByUUID(UUID uuid) {
        Entity entity = Bukkit.getEntity(uuid);
        if (entity == null) return null;

        if (entity.getType() == EntityType.PLAYER) {
            return skywars.getPlayerManager().getPlayerByUUID(uuid);
        } else if (entity instanceof Projectile) {
            Projectile projectile = (Projectile) entity;

            SWPlayer owner = null;
            if (projectile.getShooter() instanceof Player) {
                owner = skywars.getPlayerManager().getPlayerByUUID(((Player) projectile.getShooter()).getUniqueId());
            }

            return new BukkitSWOwnedEntity(skywars, entity, owner);
        } else if (entity instanceof TNTPrimed) {
            TNTPrimed tnt = (TNTPrimed) entity;

            SWPlayer owner = null;
            if (tnt.getSource() instanceof Player) {
                owner = skywars.getPlayerManager().getPlayerByUUID(tnt.getSource().getUniqueId());
            }

            return new BukkitSWOwnedEntity(skywars, entity, owner);
        }

        return new BukkitSWEntity(skywars, entity);
    }
}
