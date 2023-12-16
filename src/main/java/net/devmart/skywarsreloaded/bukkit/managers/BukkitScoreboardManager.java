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

    public BukkitScoreboardManager(SkyWarsReloaded skywars) {
        super(skywars);
    }

    @Override
    public SWCompletableFuture<SWBoard> createScoreboard(SWPlayer player, int lineCount) {
        final CoreSWCCompletableFuture<SWBoard> future = new CoreSWCCompletableFuture<>(skywars);
        skywars.getScheduler().runSync(() -> future.complete(new BukkitSWScoreboard(skywars, (BukkitSWPlayer) player, lineCount)));
        return future;
    }
}
