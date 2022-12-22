package net.devmart.skywarsreloaded.bukkit.wrapper.world;

import net.devmart.skywarsreloaded.api.wrapper.world.SWChunkGenerator;
import org.bukkit.generator.ChunkGenerator;

public class BukkitSWChunkGenerator implements SWChunkGenerator {

    private final ChunkGenerator generator;

    public BukkitSWChunkGenerator(ChunkGenerator generator) {
        this.generator = generator;
    }

    public ChunkGenerator getGenerator() {
        return generator;
    }
}
