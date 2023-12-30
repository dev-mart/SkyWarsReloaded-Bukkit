package net.devmart.skywarsreloaded.bukkit.protocol;

import net.devmart.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;

public class BukkitNMS_9_11 extends BukkitNMS_8 {

    public BukkitNMS_9_11(BukkitSkyWarsReloaded skywars, String serverPackage) {
        super(skywars, serverPackage);
    }

    @Override
    public void initVersionedAPI() {
        // Versioned enums
        voidBiome = "VOID";
    }
}
