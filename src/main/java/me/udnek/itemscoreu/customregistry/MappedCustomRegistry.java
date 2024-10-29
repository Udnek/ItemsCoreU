package me.udnek.itemscoreu.customregistry;

import me.udnek.itemscoreu.util.LogUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.function.Consumer;

public class MappedCustomRegistry<T extends Registrable> implements CustomRegistry<T>{

    protected HashMap<String, T> map = new HashMap<>();
    protected String category;

    public MappedCustomRegistry(String category){
        this.category = category;
    }

    @Override
    public @NotNull T register(@NotNull Plugin plugin, @NotNull T custom){
        custom.initialize(plugin);
        map.put(custom.getId(), custom);
        logRegistered(custom);
        if (custom instanceof Listener listener){
            Bukkit.getPluginManager().registerEvents(listener, plugin);
            LogUtils.pluginLog("(EventListener) " + custom.getId() + " (" +listener.getClass().getName() + ")");
        }
        return custom;
    }
    @Override
    public T get(@Nullable String id) {
        return map.get(id);
    }

    @Override
    public @NotNull Collection<String> getIds() {
        return map.keySet();
    }
    protected void logRegistered(T custom){
        LogUtils.pluginLog("("+category+") " + custom.getId());
    }

    @Override
    public void getAll(@NotNull Consumer<T> consumer) {
        map.forEach((s, t) -> consumer.accept(t));
    }
    @Override
    public @NotNull Collection<T> getAll() {
        return map.values();
    }
}
