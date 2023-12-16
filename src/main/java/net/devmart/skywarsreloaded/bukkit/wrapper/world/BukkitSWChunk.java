package net.devmart.skywarsreloaded.bukkit.wrapper.world;

import net.devmart.skywarsreloaded.api.wrapper.world.SWChunk;
import net.devmart.skywarsreloaded.api.wrapper.world.SWWorld;
import net.devmart.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import org.bukkit.Chunk;

public class BukkitSWChunk implements SWChunk {

    private final BukkitSkyWarsReloaded skywars;
    private final Chunk chunk;

    public BukkitSWChunk(BukkitSkyWarsReloaded skywars, Chunk chunk) {
        this.skywars = skywars;
        this.chunk = chunk;
    }

    @Override
    public boolean isLoaded() {
        return chunk.isLoaded();
    }

    @Override
    public void load() {
        chunk.load();
    }

    @Override
    public void unload() {
        chunk.unload();
    }

    @Override
    public SWWorld getWorld() {
        return skywars.getServer().getWorld(chunk.getWorld().getName());
    }

    public Chunk getChunk() {
        return this.chunk;
    }
}
