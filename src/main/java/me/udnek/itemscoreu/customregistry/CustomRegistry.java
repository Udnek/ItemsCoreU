package me.udnek.itemscoreu.customregistry;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Consumer;

public interface CustomRegistry<T extends Registrable> {
    @NotNull T register(@NotNull Plugin plugin, @NotNull T custom);
    @Nullable T get(@Nullable String id);
    boolean contains(@Nullable String id);
    @NotNull Collection<String> getIds();
    void getAll(@NotNull Consumer<T> consumer);
    @NotNull Collection<T> getAll();
}
