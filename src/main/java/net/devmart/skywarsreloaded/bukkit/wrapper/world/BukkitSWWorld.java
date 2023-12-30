package net.devmart.skywarsreloaded.bukkit.wrapper.world;

import net.devmart.skywarsreloaded.api.utils.Item;
import net.devmart.skywarsreloaded.api.utils.SWCoord;
import net.devmart.skywarsreloaded.api.wrapper.entity.SWPlayer;
import net.devmart.skywarsreloaded.api.wrapper.world.block.SWBlock;
import net.devmart.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.devmart.skywarsreloaded.bukkit.utils.BukkitItem;
import net.devmart.skywarsreloaded.bukkit.wrapper.world.block.BukkitSWBlock;
import net.devmart.skywarsreloaded.bukkit.wrapper.world.block.BukkitSWChest;
import net.devmart.skywarsreloaded.core.utils.CoreSWCoord;
import net.devmart.skywarsreloaded.core.wrapper.world.AbstractSWWorld;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;

import java.util.List;
import java.util.stream.Collectors;

public class BukkitSWWorld extends AbstractSWWorld {

    protected final BukkitSkyWarsReloaded skywars;
    protected final World bukkitWorld;
    protected final String name;

    public BukkitSWWorld(BukkitSkyWarsReloaded skywars, World world) {
        this.skywars = skywars;
        this.bukkitWorld = world;
        this.name = world.getName();
    }

    @Override
    public List<SWPlayer> getAllPlayers() {
        return this.bukkitWorld.getPlayers().stream().map(
                (bPlayer) -> this.skywars.getPlayerManager().getPlayerByUUID(bPlayer.getUniqueId())
        ).collect(Collectors.toList());
    }

    @Override
    public void setBlockAt(SWCoord location, Item item) {
        Block block = bukkitWorld.getBlockAt(location.x(), location.y(), location.z());
        if (item == null) block.setType(Material.AIR);
        else if (item instanceof BukkitItem) {
            block.setType(((BukkitItem) item).getBukkitItem().getType());
        } else {
            try {
                block.setType(Material.valueOf(item.getMaterial().toUpperCase()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void setBlockAt(SWCoord location, String blockName) {
        this.setBlockAt(location, blockName == null ? null : new BukkitItem(this.skywars, blockName));
    }

    @Override
    public SWBlock getBlockAt(int x, int y, int z) {
        final Block bukkitBlock = this.bukkitWorld.getBlockAt(x, y, z);
        BukkitSWBlock block = new BukkitSWBlock(skywars, bukkitBlock);

        if (block.getMaterial().contains("CHEST") && bukkitBlock.getState() instanceof Chest) {
            Chest chest = (Chest) bukkitBlock.getState();
            return new BukkitSWChest(this.skywars, chest);
        }

        return block;
    }

    @Override
    public SWBlock getBlockAt(SWCoord location) {
        return getBlockAt(location.x(), location.y(), location.z());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public SWCoord getDefaultSpawnLocation() {
        Location loc = this.bukkitWorld.getSpawnLocation();
        return new CoreSWCoord(this, loc.getX(), loc.getY(), loc.getZ());
    }

    @Override
    public void unload(boolean saveChunks) {
        this.skywars.getBukkitPlugin().getServer().unloadWorld(this.bukkitWorld, saveChunks);
    }

    @Override
    public boolean isLoaded() {
        return bukkitWorld != null;
    }

    @Override
    public void setKeepSpawnLoaded(boolean keepSpawnLoaded) {
        this.bukkitWorld.setKeepSpawnInMemory(keepSpawnLoaded);
    }

    @Override
    public long getTime() {
        return bukkitWorld.getTime();
    }

    @Override
    public void setTime(long time) {
        bukkitWorld.setTime(time);
    }

    @Override
    public void setRaining(boolean raining) {
        bukkitWorld.setStorm(raining);
    }

    @Override
    public boolean isRaining() {
        return bukkitWorld.hasStorm();
    }

    @Override
    public void setThundering(boolean thunder) {
        bukkitWorld.setThundering(thunder);
    }

    @Override
    public boolean isThundering() {
        return bukkitWorld.isThundering();
    }

    public World getBukkitWorld() {
        return bukkitWorld;
    }

}
