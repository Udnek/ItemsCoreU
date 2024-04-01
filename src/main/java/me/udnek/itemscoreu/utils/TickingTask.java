package me.udnek.itemscoreu.utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public abstract class TickingTask implements Runnable{

    protected BukkitTask task;

    public void start(JavaPlugin plugin){
        task = Bukkit.getScheduler().runTaskTimer(plugin, this, 0, getDelay());
    }

    public void stop(){
        if (task == null) return;
        if (task.isCancelled()) return;
        task.cancel();
    }

    public abstract int getDelay();

}
