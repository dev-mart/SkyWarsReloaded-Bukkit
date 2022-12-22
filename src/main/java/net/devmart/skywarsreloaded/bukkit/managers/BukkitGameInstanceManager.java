package net.devmart.skywarsreloaded.bukkit.managers;

import net.devmart.skywarsreloaded.api.game.GameTemplate;
import net.devmart.skywarsreloaded.api.game.gameinstance.LocalGameInstance;
import net.devmart.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.devmart.skywarsreloaded.bukkit.game.BukkitLocalGameInstance;
import net.devmart.skywarsreloaded.core.manager.gameinstance.CoreLocalGameInstanceManager;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class BukkitGameInstanceManager extends CoreLocalGameInstanceManager {

    public BukkitGameInstanceManager(BukkitSkyWarsReloaded plugin) {
        super(plugin);
    }

    @Override
    public CompletableFuture<LocalGameInstance> createGameWorld(GameTemplate data) {
        final List<LocalGameInstance> idleInstances = getIdleGameInstances();
        if (data != null && idleInstances.size() > 0) {
            final LocalGameInstance localGameInstance = idleInstances.get(0);
            localGameInstance.setTemplate(data);
        }

        LocalGameInstance localGameInstance = new BukkitLocalGameInstance((BukkitSkyWarsReloaded) plugin, UUID.randomUUID(), data);
        this.registerGameWorld(localGameInstance);
        return CompletableFuture.completedFuture(localGameInstance);
    }
}
