package net.devmart.skywarsreloaded.bukkit.managers;

import net.devmart.skywarsreloaded.api.wrapper.entity.SWEntity;
import net.devmart.skywarsreloaded.api.wrapper.entity.SWPlayer;
import net.devmart.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.devmart.skywarsreloaded.bukkit.wrapper.entity.BukkitSWEntity;
import net.devmart.skywarsreloaded.bukkit.wrapper.entity.BukkitSWPlayer;
import net.devmart.skywarsreloaded.core.manager.AbstractPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BukkitPlayerManager extends AbstractPlayerManager {

    public BukkitPlayerManager(BukkitSkyWarsReloaded skywars) {
        super(skywars);
    }

    @Override
    public SWPlayer createSWPlayerForPlatform(UUID uuid) {
        if (skywars.getServer().isPrimaryThread()) {
            Player player = Bukkit.getPlayer(uuid);
            return this.createSWPlayerForPlatform(player);
        }
        return new BukkitSWPlayer((BukkitSkyWarsReloaded) skywars, uuid, true);
    }

    public SWPlayer createSWPlayerForPlatform(Player player) {
        return new BukkitSWPlayer((BukkitSkyWarsReloaded) skywars, player, true);
    }

    @Override
    public SWEntity getEntityFromUUID(UUID uuid) {
        Entity entity = Bukkit.getEntity(uuid);
        if (entity == null) return null;

        return new BukkitSWEntity(skywars, entity);
    }

    @Override
    public void initAllPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            initPlayer(player.getUniqueId());
        }
    }
}
