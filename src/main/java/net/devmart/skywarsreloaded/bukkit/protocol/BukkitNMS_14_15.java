package net.devmart.skywarsreloaded.bukkit.protocol;

import net.devmart.skywarsreloaded.api.wrapper.world.SWChunk;
import net.devmart.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.devmart.skywarsreloaded.bukkit.wrapper.world.BukkitSWChunk;

public class BukkitNMS_14_15 extends BukkitNMS_13 {

    public BukkitNMS_14_15(BukkitSkyWarsReloaded plugin, String serverPackage) {
        super(plugin, serverPackage);
    }

    @Override
    public void addPluginChunkTicket(SWChunk chunk) {
        ((BukkitSWChunk) chunk).getChunk().addPluginChunkTicket(plugin.getBukkitPlugin());
    }
}
