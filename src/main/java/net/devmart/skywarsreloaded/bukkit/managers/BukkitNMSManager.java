package net.devmart.skywarsreloaded.bukkit.managers;

import net.devmart.skywarsreloaded.api.manager.NMSManager;
import net.devmart.skywarsreloaded.api.protocol.NMS;
import net.devmart.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.devmart.skywarsreloaded.bukkit.protocol.*;
import org.bukkit.Bukkit;

public class BukkitNMSManager implements NMSManager {

    protected final BukkitSkyWarsReloaded skywars;
    private final NMS nms;

    public BukkitNMSManager(BukkitSkyWarsReloaded skywars) {
        this.skywars = skywars;

        int version = skywars.getUtils().getServerVersion();
        String serverPackage = Bukkit.getServer().getClass().getPackage().getName();

        if (version >= 16) {
            this.nms = new BukkitNMS_16_19(this.skywars, serverPackage);
        } else if (version >= 14) {
            this.nms = new BukkitNMS_14_15(this.skywars, serverPackage);
        } else if (version >= 13) {
            this.nms = new BukkitNMS_13(this.skywars, serverPackage);
        } else if (version >= 12) {
            this.nms = new BukkitNMS_12(this.skywars, serverPackage);
        } else if (version >= 9) {
            this.nms = new BukkitNMS_9_11(this.skywars, serverPackage);
        } else if (version >= 8) {
            this.nms = new BukkitNMS_8(this.skywars, serverPackage);
        } else {
            throw new IllegalStateException("Unsupported server version: " + version);
        }
    }

    public NMS getNMS() {
        return this.nms;
    }
}
