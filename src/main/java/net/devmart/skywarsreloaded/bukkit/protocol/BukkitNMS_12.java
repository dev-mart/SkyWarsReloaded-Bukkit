package net.devmart.skywarsreloaded.bukkit.protocol;

import net.devmart.skywarsreloaded.api.wrapper.entity.SWPlayer;
import net.devmart.skywarsreloaded.api.wrapper.world.SWChunkGenerator;
import net.devmart.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.devmart.skywarsreloaded.bukkit.wrapper.entity.BukkitSWPlayer;
import net.devmart.skywarsreloaded.bukkit.wrapper.world.BukkitSWChunkGenerator;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

public class BukkitNMS_12 extends BukkitNMS_9_11 {

    public BukkitNMS_12(BukkitSkyWarsReloaded plugin, String serverPackage) {
        super(plugin, serverPackage);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void initReflection() throws ClassNotFoundException, NoSuchFieldException, NoSuchMethodException {
        super.initReflection();

        // Classes
        this.chatMessageType = (Class<Enum>) Class.forName("net.minecraft.server." + serverVersion + ".ChatMessageType");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void sendActionbar(SWPlayer player, String message) {
        if (!(player instanceof BukkitSWPlayer)) return;
        BukkitSWPlayer bukkitSWPlayer = (BukkitSWPlayer) player;

        try {
            Object baseComponent = chatSerializer.invoke(null, "{\"text\": \"" + message + "\"}");
            Object packet = packetPlayOutChat.getDeclaredConstructor(chatBaseComponent, chatMessageType)
                    .newInstance(baseComponent, Enum.valueOf(chatMessageType, "CHAT"));

            Object nmsPlayer = getHandle.invoke(bukkitSWPlayer.getPlayer());
            Object connection = playerConnection.get(nmsPlayer);
            sendPacket.invoke(connection, packet);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                 InstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void sendJSONMessage(SWPlayer player, String message) {
        if (!(player instanceof BukkitSWPlayer)) return;
        BukkitSWPlayer bukkitSWPlayer = (BukkitSWPlayer) player;

        try {
            Object baseComponent = chatSerializer.invoke(null, message);
            Object packet = packetPlayOutChat.getDeclaredConstructor(chatBaseComponent, chatMessageType)
                    .newInstance(baseComponent, Enum.valueOf(chatMessageType, "SYSTEM"));

            Object nmsPlayer = getHandle.invoke(bukkitSWPlayer.getPlayer());
            Object connection = playerConnection.get(nmsPlayer);
            sendPacket.invoke(connection, packet);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                 InstantiationException e) {
            e.printStackTrace();
        }
    }

    @NotNull
    @Override
    public SWChunkGenerator getChunkGenerator() {
        return new BukkitSWChunkGenerator(
                new ChunkGenerator() {
                    @Override
                    public ChunkData generateChunkData(@NotNull World world, @NotNull Random random, int chunkX, int chunkZ, @NotNull BiomeGrid biome) {
                        ChunkData chunkData = createChunkData(world);
                        for (int x = 0; x < 16; x++) {
                            for (int z = 0; z < 16; z++) {
                                biome.setBiome(x, z, voidBiome);
                            }
                        }
                        return chunkData;
                    }
                });
    }
}
