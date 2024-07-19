package me.udnek.itemscoreu.utils;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public interface PluginInitializable {
    void initialize(@NotNull JavaPlugin plugin);
}
