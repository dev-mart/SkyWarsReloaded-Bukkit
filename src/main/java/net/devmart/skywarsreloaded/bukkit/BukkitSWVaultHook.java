package net.devmart.skywarsreloaded.bukkit;

import net.devmart.skywarsreloaded.api.wrapper.entity.SWPlayer;
import net.devmart.skywarsreloaded.bukkit.wrapper.server.BukkitSWServer;
import net.devmart.skywarsreloaded.core.hook.AbstractSWVaultHook;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

public class BukkitSWVaultHook extends AbstractSWVaultHook {

    private final BukkitSWServer bukkitServer;

    public BukkitSWVaultHook(BukkitSkyWarsReloaded skywars) {
        super(skywars);
        this.bukkitServer = skywars.getServer();
    }

    @Override
    public boolean setupEconomy() {
        if (!bukkitServer.isPluginEnabled("Vault")) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = bukkitServer.getBukkitServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            skywars.getLogger().warn("Vault was found but no economy plugin was found. Economy features will be disabled.");
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    @Override
    public double getBalance(SWPlayer player) {
        final OfflinePlayer offlinePlayer = bukkitServer.getOfflinePlayer(player);
        return economy.getBalance(offlinePlayer);
    }

    @Override
    public boolean hasBalance(SWPlayer player, double amount) {
        final OfflinePlayer offlinePlayer = bukkitServer.getOfflinePlayer(player);
        return economy.has(offlinePlayer, amount);
    }

    @Override
    public boolean withdraw(SWPlayer player, double amount) {
        final OfflinePlayer offlinePlayer = bukkitServer.getOfflinePlayer(player);
        return economy.withdrawPlayer(offlinePlayer, amount).transactionSuccess();
    }

    @Override
    public boolean deposit(SWPlayer player, double amount) {
        final OfflinePlayer offlinePlayer = bukkitServer.getOfflinePlayer(player);
        return economy.depositPlayer(offlinePlayer, amount).transactionSuccess();
    }
}
