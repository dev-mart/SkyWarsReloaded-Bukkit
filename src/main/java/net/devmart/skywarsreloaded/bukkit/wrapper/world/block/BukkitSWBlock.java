package net.devmart.skywarsreloaded.bukkit.wrapper.world.block;

import net.devmart.skywarsreloaded.api.utils.SWCoord;
import net.devmart.skywarsreloaded.api.wrapper.world.SWChunk;
import net.devmart.skywarsreloaded.api.wrapper.world.block.SWBlock;
import net.devmart.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.devmart.skywarsreloaded.bukkit.wrapper.world.BukkitSWChunk;
import net.devmart.skywarsreloaded.core.utils.CoreSWCoord;
import org.bukkit.block.Block;

public class BukkitSWBlock implements SWBlock {

    protected final BukkitSkyWarsReloaded skywars;
    protected final Block block;

    public BukkitSWBlock(BukkitSkyWarsReloaded skywars, Block block) {
        this.skywars = skywars;
        this.block = block;
    }

    @Override
    public SWChunk getChunk() {
        return new BukkitSWChunk(skywars, block.getChunk());
    }

    @Override
    public SWCoord getCoord() {
        return new CoreSWCoord(skywars.getServer().getWorld(block.getWorld().getName()), block.getX(), block.getY(), block.getZ());
    }

    @Override
    public String getMaterial() {
        return this.block.getType().name();
    }
}
