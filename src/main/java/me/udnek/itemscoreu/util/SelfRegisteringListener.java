package me.udnek.itemscoreu.util;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public abstract class SelfRegisteringListener implements Listener {
    public SelfRegisteringListener(@NotNull Plugin plugin){
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
}
