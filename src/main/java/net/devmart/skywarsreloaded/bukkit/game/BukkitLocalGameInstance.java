package net.devmart.skywarsreloaded.bukkit.game;

import net.devmart.skywarsreloaded.api.game.GameTemplate;
import net.devmart.skywarsreloaded.api.wrapper.world.SWWorld;
import net.devmart.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.devmart.skywarsreloaded.bukkit.wrapper.world.BukkitSWWorld;
import net.devmart.skywarsreloaded.core.game.gameinstance.AbstractLocalGameInstance;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.UUID;

public class BukkitLocalGameInstance extends AbstractLocalGameInstance {

    public BukkitLocalGameInstance(BukkitSkyWarsReloaded plugin, UUID id, GameTemplate gameData) {
        super(plugin, id, gameData);
    }

    public World getBukkitWorld() {
        return Bukkit.getWorld(this.getWorldName());
    }

    @Override
    public SWWorld getWorld() {
        return new BukkitSWWorld((BukkitSkyWarsReloaded) plugin, getBukkitWorld());
    }

    @Override
    public void makeReadyForEditing() {
        World world = getBukkitWorld();
        plugin.getWorldLoader().updateWorldBorder(this);
        // Place beacons for each player spawn point
        getTemplate().getTeamSpawnpoints().forEach(swCoords ->
                swCoords.forEach(swCoord ->
                        world.getBlockAt(swCoord.x(), swCoord.y(), swCoord.z()).setType(Material.BEACON)));
    }
}
