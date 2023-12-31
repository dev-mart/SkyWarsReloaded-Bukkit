package net.devmart.skywarsreloaded.bukkit.utils.proxy;

import net.devmart.skywarsreloaded.api.listener.PluginMessageListener;
import net.devmart.skywarsreloaded.api.wrapper.entity.SWPlayer;
import net.devmart.skywarsreloaded.bukkit.BukkitSkyWarsReloadedPlugin;
import net.devmart.skywarsreloaded.bukkit.wrapper.entity.BukkitSWPlayer;
import net.devmart.skywarsreloaded.core.utils.proxy.AbstractPluginMessaging;
import org.bukkit.plugin.messaging.Messenger;

import java.util.Optional;

public class BungeeCordMessaging extends AbstractPluginMessaging {

    private final BukkitSkyWarsReloadedPlugin bukkitPlugin;
    private final Messenger messenger;

    public BungeeCordMessaging(BukkitSkyWarsReloadedPlugin bukkitPlugin) {
        this.bukkitPlugin = bukkitPlugin;
        this.messenger = this.bukkitPlugin.getServer().getMessenger();
    }

    @Override
    public void registerChannel(String channel) {
        this.messenger.registerOutgoingPluginChannel(this.bukkitPlugin, channel);
    }

    @Override
    public void unregisterChannel(String channel) {
        this.messenger.unregisterOutgoingPluginChannel(this.bukkitPlugin, channel);
    }

    @Override
    public void send(String channel, byte[] data) {
        this.bukkitPlugin.getServer().sendPluginMessage(this.bukkitPlugin, channel, data);
    }

    @Override
    public void sendToPlayer(SWPlayer player, String channel, byte[] data) {
        ((BukkitSWPlayer) player).getPlayer().sendPluginMessage(this.bukkitPlugin, channel, data);
    }

    @Override
    public void registerListener(String channel, PluginMessageListener listener) {
        super.registerListener(channel, listener);

        if (this.messenger.isIncomingChannelRegistered(this.bukkitPlugin, channel)) {
            return;
        }

        messenger.registerIncomingPluginChannel(this.bukkitPlugin, channel, (consumerChannel, player, message) -> {
            this.handlePluginMessage(consumerChannel, message);
        });
    }

    @Override
    public Optional<String> unregisterListener(PluginMessageListener listener) {
        Optional<String> channel = super.unregisterListener(listener);

        if (channel.isEmpty()) {
            return channel;
        }

        if (this.getListeners(channel.get()).isEmpty()) {
            this.messenger.unregisterIncomingPluginChannel(this.bukkitPlugin, this.bukkitPlugin.getName());
        }

        return channel;
    }
}
