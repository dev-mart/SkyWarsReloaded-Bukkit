package net.devmart.skywarsreloaded.bukkit.wrapper.scheduler;

import net.devmart.skywarsreloaded.api.wrapper.scheduler.SWRunnable;
import net.devmart.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.devmart.skywarsreloaded.core.wrapper.scheduler.AbstractSWScheduler;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class BukkitSWScheduler extends AbstractSWScheduler {

    private final JavaPlugin bukkitPlugin;

    public BukkitSWScheduler(BukkitSkyWarsReloaded skywars) {
        super(skywars);
        this.bukkitPlugin = skywars.getBukkitPlugin();
    }

    @Override
    public <T> CompletableFuture<T> callSyncMethod(Supplier<T> supplier) {
        CompletableFuture<T> future = new CompletableFuture<>();
        bukkitPlugin.getServer().getScheduler().runTask(bukkitPlugin, () -> future.complete(supplier.get()));
        return future;
    }

    @Override
    public void runSync(Runnable runnable) {
        bukkitPlugin.getServer().getScheduler().runTask(bukkitPlugin, runnable);
    }

    @Override
    public void runAsync(Runnable runnable) {
        bukkitPlugin.getServer().getScheduler().runTaskAsynchronously(bukkitPlugin, runnable);
    }

    @Override
    public void runSyncLater(Runnable runnable, int ticks) {
        bukkitPlugin.getServer().getScheduler().runTaskLater(bukkitPlugin, runnable, ticks);
    }

    @Override
    public void runAsyncLater(Runnable runnable, int ticks) {
        bukkitPlugin.getServer().getScheduler().runTaskLaterAsynchronously(bukkitPlugin, runnable, ticks);
    }

    @Override
    public SWRunnable runSyncTimer(Runnable runnable, int ticks, int period) {
        BukkitTask task = bukkitPlugin.getServer().getScheduler().runTaskTimer(bukkitPlugin, runnable, ticks, period);
        final SWRunnable coreSWRunnable = this.createRunnable(runnable);
        coreSWRunnable.setTaskId(task.getTaskId());
        return coreSWRunnable;
    }

    @Override
    public SWRunnable runAsyncTimer(Runnable runnable, int ticks, int period) {
        BukkitTask task = bukkitPlugin.getServer().getScheduler().runTaskTimerAsynchronously(bukkitPlugin, runnable, ticks, period);
        final SWRunnable coreSWRunnable = this.createRunnable(runnable);
        coreSWRunnable.setTaskId(task.getTaskId());
        return coreSWRunnable;
    }

    @Override
    public SWRunnable createRunnable(Runnable runnable) {
        return new BukkitSWRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        };
    }

    @Override
    public void cancelAll() {
        bukkitPlugin.getServer().getScheduler().cancelTasks(bukkitPlugin);
    }
}
