package me.udnek.itemscoreu.customregistry;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface CustomRegistry<T extends Registrable> {
    @NotNull T register(@NotNull Plugin plugin, @NotNull T custom);
    @Nullable T get(@Nullable String id);
    @NotNull Collection<String> getIds();
    @NotNull Collection<T> getAll();
}
