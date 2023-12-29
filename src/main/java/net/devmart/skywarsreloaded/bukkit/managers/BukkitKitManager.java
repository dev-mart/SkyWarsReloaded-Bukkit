package net.devmart.skywarsreloaded.bukkit.managers;

import net.devmart.skywarsreloaded.api.SkyWarsReloaded;
import net.devmart.skywarsreloaded.api.game.kits.SWKit;
import net.devmart.skywarsreloaded.bukkit.game.kits.BukkitSWKit;
import net.devmart.skywarsreloaded.core.manager.game.AbstractKitManager;

public class BukkitKitManager extends AbstractKitManager {

    public BukkitKitManager(SkyWarsReloaded skywars) {
        super(skywars);
    }

    @Override
    public SWKit initKit(String id) {
        return new BukkitSWKit(skywars, id);
    }
}