package net.devmart.skywarsreloaded.bukkit.managers;

import net.devmart.skywarsreloaded.api.game.GameTemplate;
import net.devmart.skywarsreloaded.api.game.gameinstance.LocalGameInstance;
import net.devmart.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.devmart.skywarsreloaded.bukkit.game.BukkitLocalGameInstance;
import net.devmart.skywarsreloaded.core.manager.game.instance.CoreLocalGameInstanceManager;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class BukkitGameInstanceManager extends CoreLocalGameInstanceManager {

    public BukkitGameInstanceManager(BukkitSkyWarsReloaded skywars) {
        super(skywars);
    }

    @Override
    public CompletableFuture<LocalGameInstance> createGameInstance(GameTemplate data) {
        final List<LocalGameInstance> idleInstances = getIdleGameInstances();
        if (data != null && !idleInstances.isEmpty()) {
            final LocalGameInstance localGameInstance = idleInstances.get(0);
            localGameInstance.setTemplate(data);
        }

        LocalGameInstance localGameInstance = new BukkitLocalGameInstance((BukkitSkyWarsReloaded) skywars, UUID.randomUUID(), data);
        this.registerGameInstance(localGameInstance);
        return CompletableFuture.completedFuture(localGameInstance);
    }
}
