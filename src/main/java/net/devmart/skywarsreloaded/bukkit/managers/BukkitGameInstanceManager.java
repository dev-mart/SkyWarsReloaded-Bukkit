package net.devmart.skywarsreloaded.bukkit.managers;

import net.devmart.skywarsreloaded.api.game.GameTemplate;
import net.devmart.skywarsreloaded.api.game.gameinstance.LocalGameInstance;
import net.devmart.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.devmart.skywarsreloaded.bukkit.game.BukkitLocalGameInstance;
import net.devmart.skywarsreloaded.core.manager.game.instance.CoreLocalGameInstanceManager;

import java.lang.ref.WeakReference;
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
        WeakReference<LocalGameInstance> weakRef = new WeakReference<>(localGameInstance);

        this.registerGameInstance(localGameInstance);

        if (skywars.getLogger().isDebugModeActive()) {
            skywars.getLogger().debug("Debug mode active! Checking for game instance references...");
            startCheckingForRefs(weakRef, "LocalGameInstance::" + localGameInstance.getWorldName());
        } else {
            weakRef = null;
        }
        return CompletableFuture.completedFuture(localGameInstance);
    }

    private void startCheckingForRefs(WeakReference weakRef, String name) {
        if (weakRef == null) return;

        skywars.getScheduler().runSyncTimer(() -> {
            Object obj = weakRef.get();
            if (obj == null) {
                skywars.getLogger().debug(String.format("Object \"%s\" has no references!", name));
            } else {
                skywars.getLogger().debug(String.format("Object \"%s\" still has %d references!", name, weakRef.get()));
            }
        }, 0, 20);
    }
}
