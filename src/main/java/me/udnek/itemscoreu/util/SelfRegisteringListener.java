package me.udnek.itemscoreu.util;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class SelfRegisteringListener implements Listener {
    public SelfRegisteringListener(JavaPlugin plugin){
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
}
