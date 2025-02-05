package me.udnek.itemscoreu.customregistry;

import com.google.common.base.Preconditions;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Consumer;

public interface CustomRegistry<T extends Registrable> extends Registrable{
    @NotNull <V extends T>  V register(@NotNull Plugin plugin, @NotNull V custom);
    @Nullable T get(@Nullable String id);
    default @NotNull T getOrException(@NotNull String id){
        @Nullable T item = get(id);
        Preconditions.checkArgument(item != null, "No such item in registry: " + id);
        return item;
    }
    boolean contains(@Nullable String id);
    @NotNull Collection<String> getIds();
    @NotNull Collection<T> getAllByPlugin(@NotNull Plugin plugin);
    void getAll(@NotNull Consumer<T> consumer);
    @NotNull Collection<T> getAll();
}
