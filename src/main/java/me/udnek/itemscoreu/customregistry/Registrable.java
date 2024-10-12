package me.udnek.itemscoreu.customregistry;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public interface Registrable {
    void initialize(@NotNull Plugin plugin);
    default void afterInitialization(){}
    @NotNull String getId();
}
