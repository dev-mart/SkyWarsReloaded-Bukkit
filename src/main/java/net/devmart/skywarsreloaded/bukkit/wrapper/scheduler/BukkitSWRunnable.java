package net.devmart.skywarsreloaded.bukkit.wrapper.scheduler;

import net.devmart.skywarsreloaded.core.wrapper.scheduler.CoreSWRunnable;
import org.bukkit.Bukkit;

public abstract class BukkitSWRunnable extends CoreSWRunnable {

    @Override
    public void cancel() {
        super.cancel();
        if (taskId != 0) Bukkit.getScheduler().cancelTask(taskId);
    }
}
