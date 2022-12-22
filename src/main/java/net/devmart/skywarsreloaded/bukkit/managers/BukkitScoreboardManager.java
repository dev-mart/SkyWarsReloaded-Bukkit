package net.devmart.skywarsreloaded.bukkit.managers;

import net.devmart.skywarsreloaded.api.SkyWarsReloaded;
import net.devmart.skywarsreloaded.api.utils.scoreboards.SWBoard;
import net.devmart.skywarsreloaded.api.wrapper.entity.SWPlayer;
import net.devmart.skywarsreloaded.bukkit.game.BukkitSWScoreboard;
import net.devmart.skywarsreloaded.bukkit.wrapper.player.BukkitSWPlayer;
import net.devmart.skywarsreloaded.core.manager.AbstractScoreboardManager;

public class BukkitScoreboardManager extends AbstractScoreboardManager {

    public BukkitScoreboardManager(SkyWarsReloaded plugin) {
        super(plugin);
    }

    @Override
    public SWBoard createScoreboard(SWPlayer player, int lineCount) {
        return new BukkitSWScoreboard(plugin, (BukkitSWPlayer) player, lineCount);
    }
}
