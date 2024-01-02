package net.devmart.skywarsreloaded.bukkit.game.loader;

import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.math.BlockVector3;
import io.papermc.lib.PaperLib;
import net.devmart.skywarsreloaded.api.game.GameTemplate;
import net.devmart.skywarsreloaded.api.game.gameinstance.LocalGameInstance;
import net.devmart.skywarsreloaded.api.utils.SWCoord;
import net.devmart.skywarsreloaded.api.utils.properties.FolderProperties;
import net.devmart.skywarsreloaded.api.utils.properties.InternalProperties;
import net.devmart.skywarsreloaded.api.utils.properties.RuntimeDataProperties;
import net.devmart.skywarsreloaded.api.wrapper.entity.SWPlayer;
import net.devmart.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.devmart.skywarsreloaded.bukkit.game.BukkitLocalGameInstance;
import net.devmart.skywarsreloaded.bukkit.wrapper.world.BukkitSWChunkGenerator;
import net.devmart.skywarsreloaded.core.utils.FileUtils;
import org.bukkit.*;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class SchemWorldLoader extends BukkitWorldLoader {

    public SchemWorldLoader(BukkitSkyWarsReloaded skywars) {
        super(skywars);
    }

    @Override
    public CompletableFuture<Boolean> generateWorldInstance(LocalGameInstance gameWorld) throws IllegalStateException, IllegalArgumentException {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        this.createEmptyWorld(gameWorld).thenRun(() -> skywars.getScheduler().runSync(
                () -> postWorldGenerateTask(gameWorld, future)
        ));
        return future;
    }

    protected void postWorldGenerateTask(LocalGameInstance gameWorld, CompletableFuture<Boolean> future) {
        boolean res = false;
        try {
            res = pasteTemplateSchematic(gameWorld).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        future.complete(res);
    }

    @Override
    public CompletableFuture<Void> createEmptyWorld(LocalGameInstance gameInstance) {
        WorldCreator creator = new WorldCreator(gameInstance.getWorldName());
        creator.generateStructures(false);
        creator.type(WorldType.FLAT);

        // Apply generator settings based on the MC version
        String generatorSettings = this.skywars.getNMSManager().getNMS().getVoidGeneratorSettings();
        creator.generatorSettings(generatorSettings);

        // Override world generator
        creator.generator(((BukkitSWChunkGenerator) this.skywars.getNMSManager()
                .getNMS()
                .getChunkGenerator(gameInstance.getTemplate().getBiome())
        ).getGenerator());

        World createdWorld = creator.createWorld();
        assert createdWorld != null;
        CompletableFuture<Void> future = new CompletableFuture<>();

        // This won't do anything in 1.11 or lower -> will be laggier
        Location sl = createdWorld.getSpawnLocation();
        World world = sl.getWorld();
        if (world == null) throw new IllegalStateException("How is the world null? We just made it!!");

        PaperLib.getChunkAtAsyncUrgently(world, sl.getBlockX() / 16, sl.getBlockZ() / 16, true)
                .thenAccept(chunk -> future.complete(null));
        return future;
    }

    /**
     * Pastes the schematic into the world.
     *
     * @param gameWorld GameWorld to paste into
     * @return CompletableFuture that yields true if the schematic existed, false otherwise
     */
    public CompletableFuture<Boolean> pasteTemplateSchematic(LocalGameInstance gameWorld) throws IllegalStateException, IllegalArgumentException {
        CompletableFuture<Boolean> futureFail = CompletableFuture.completedFuture(false);
        // todo: Later make this work with FAWE
        CompletableFuture<Boolean> futureOk = CompletableFuture.completedFuture(true);

        File schemFolder = new File(skywars.getDataFolder(), FolderProperties.WORLD_SCHEMATICS_FOLDER);
        String schemFileName = gameWorld.getTemplate().getName() + ".schem";

        File schemFile = new File(schemFolder, schemFileName);
        if (!schemFile.exists()) {
            return futureFail;
        }

        Clipboard clip = skywars.getSchematicManager().getSchematic(schemFolder, schemFileName);
        if (clip == null) {
            return futureFail; // todo throw error?
        }

        World world = ((BukkitLocalGameInstance) gameWorld).getBukkitWorld();
        if (world == null) {
            throw new IllegalStateException(String.format(
                    "GameWorld %s$ doesn't have a valid minecraft world. Check the console for other errors!",
                    gameWorld.getId()
            ));
        }

        // The returned EditSession is already auto-closed using a try-w/ statement inside pasteSchematic()
        //noinspection resource
        skywars.getSchematicManager().pasteSchematic(clip, new BukkitWorld(world), BlockVector3.at(0, 0, 0), true);
        return futureOk;
    }

    @Override
    public void deleteWorldInstance(LocalGameInstance gameWorld) {
        if (gameWorld.getScheduler() != null) gameWorld.getScheduler().end();

        World world = ((BukkitLocalGameInstance) gameWorld).getBukkitWorld();
        if (world == null) {
            return;
        }

        final SWCoord loc = skywars.getDataConfig().getCoord(RuntimeDataProperties.LOBBY_SPAWN);

        for (SWPlayer player : gameWorld.getWorld().getAllPlayers()) {
            player.teleport(loc);
        }

        Bukkit.unloadWorld(world, false);
        try {
            FileUtils.deleteDirectory(world.getWorldFolder());
        } catch (IOException e) {
            skywars.getLogger().error(String.format("Failed to delete world %s. (%s)", world.getName(), e.getClass().getName() + ": " + e.getLocalizedMessage()));
        }
    }

    @Override
    public void deleteMap(GameTemplate gameTemplate, boolean forceUnloadInstances) {
        File schemFolder = new File(skywars.getDataFolder(), FolderProperties.WORLD_SCHEMATICS_FOLDER);
        String schemFileName = gameTemplate.getName() + ".schem";

        File schemFile = new File(schemFolder, schemFileName);
        if (schemFile.exists()) {
            try {
                FileUtils.forceDelete(schemFile);
            } catch (IOException e) {
                skywars.getLogger().error(String.format("Failed to delete schematic file %s. (%s)", schemFileName, e.getClass().getName() + ": " + e.getLocalizedMessage()));
            }
        }
    }

    @Override
    public void createBasePlatform(LocalGameInstance gameInstance) {
        World world = ((BukkitLocalGameInstance) gameInstance).getBukkitWorld();
        if (world == null) return;

        world.getBlockAt(
                InternalProperties.MAP_CREATE_PLATFORM_X,
                InternalProperties.MAP_CREATE_PLATFORM_Y,
                InternalProperties.MAP_CREATE_PLATFORM_Z
        ).setType(Material.STONE);
    }

    @Override
    public CompletableFuture<Boolean> save(LocalGameInstance gameInstance) {
        if (gameInstance == null) return CompletableFuture.completedFuture(false);

        boolean successful = skywars.getSchematicManager().saveGameWorldToSchematic(
                gameInstance,
                skywars.getUtils().getWorldEditWorld(gameInstance.getWorldName())
        );
        return CompletableFuture.completedFuture(successful);
    }
}
