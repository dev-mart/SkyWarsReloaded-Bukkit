package net.devmart.skywarsreloaded.bukkit.utils;

import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.world.World;
import me.clip.placeholderapi.PlaceholderAPI;
import net.devmart.skywarsreloaded.api.wrapper.sender.SWCommandSender;
import net.devmart.skywarsreloaded.api.wrapper.world.SWWorld;
import net.devmart.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.devmart.skywarsreloaded.bukkit.wrapper.entity.BukkitSWPlayer;
import net.devmart.skywarsreloaded.core.utils.AbstractPlatformUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class BukkitPlatformUtils extends AbstractPlatformUtils {

    protected final BukkitSkyWarsReloaded skywars;

    public BukkitPlatformUtils(BukkitSkyWarsReloaded skywars) {
        super(skywars);
        this.skywars = skywars;
    }

    @Override
    public String colorize(String arg0) {
        return ChatColor.translateAlternateColorCodes('&', arg0);
    }

    @Override
    public int getServerVersion() {
        return Integer.parseInt(Bukkit.getServer().getBukkitVersion().split("-")[0].split("\\.")[1]);
    }

    @Override
    public int getBuildLimit() {
        return Bukkit.getServer().getWorlds().get(0).getMaxHeight();
    }

    @Override
    public World getWorldEditWorld(String worldName) {
        org.bukkit.World world = Bukkit.getWorld(worldName);
        if (world == null) return null;
        return new BukkitWorld(world);
    }

    @Override
    public SWWorld getSWWorld(String worldName) {
        return skywars.getServer().getWorld(worldName);
    }

    @Override
    public String parsePlaceholders(String message, SWCommandSender player) {
        if (!(player instanceof BukkitSWPlayer) || !Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            return message;
        }

        return PlaceholderAPI.setPlaceholders(((BukkitSWPlayer) player).getPlayer(), message);
    }
}
