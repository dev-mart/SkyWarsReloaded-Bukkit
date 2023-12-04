package net.devmart.skywarsreloaded.bukkit.game.loader;

import net.devmart.skywarsreloaded.api.SkyWarsReloaded;
import net.devmart.skywarsreloaded.api.game.gameinstance.LocalGameInstance;
import net.devmart.skywarsreloaded.bukkit.game.BukkitLocalGameInstance;
import net.devmart.skywarsreloaded.core.game.loader.AbstractWorldLoader;
import org.bukkit.World;

public abstract class BukkitWorldLoader extends AbstractWorldLoader {

    public BukkitWorldLoader(SkyWarsReloaded plugin) {
        super(plugin);
    }

    @Override
    public void updateWorldBorder(LocalGameInstance gameInstance) {
        World world = ((BukkitLocalGameInstance) gameInstance).getBukkitWorld();
        if (world == null) return;

        world.getWorldBorder().setCenter(0, 0);
        world.getWorldBorder().setSize(gameInstance.getTemplate().getBorderRadius());
    }
}
