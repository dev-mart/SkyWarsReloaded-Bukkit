package net.devmart.skywarsreloaded.bukkit.managers;

import net.devmart.skywarsreloaded.api.SkyWarsReloaded;
import net.devmart.skywarsreloaded.api.utils.SWCompletableFuture;
import net.devmart.skywarsreloaded.api.utils.scoreboards.SWBoard;
import net.devmart.skywarsreloaded.api.wrapper.entity.SWPlayer;
import net.devmart.skywarsreloaded.bukkit.game.BukkitSWScoreboard;
import net.devmart.skywarsreloaded.bukkit.wrapper.entity.BukkitSWPlayer;
import net.devmart.skywarsreloaded.core.manager.AbstractScoreboardManager;
import net.devmart.skywarsreloaded.core.utils.CoreSWCCompletableFuture;

public class BukkitScoreboardManager extends AbstractScoreboardManager {

    public BukkitScoreboardManager(SkyWarsReloaded plugin) {
        super(plugin);
    }

    @Override
    public SWCompletableFuture<SWBoard> createScoreboard(SWPlayer player, int lineCount) {
        final CoreSWCCompletableFuture<SWBoard> future = new CoreSWCCompletableFuture<>(plugin);
        plugin.getScheduler().runSync(() -> future.complete(new BukkitSWScoreboard(plugin, (BukkitSWPlayer) player, lineCount)));
        return future;
    }
}
