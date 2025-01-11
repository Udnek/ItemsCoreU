package me.udnek.itemscoreu.customregistry;

import com.google.common.base.Preconditions;
import me.udnek.itemscoreu.util.LogUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.function.Consumer;

public class MappedCustomRegistry<T extends Registrable> extends AbstractRegistrable implements CustomRegistry<T>{

    protected HashMap<String, T> map = new HashMap<>();
    protected String rawId;

    public MappedCustomRegistry(@NotNull String rawId){
        this.rawId = rawId;
    }

    @Override
    public @NotNull String getRawId() {return rawId;}


    @Override
    public <V extends T> @NotNull V register(@NotNull Plugin plugin, @NotNull V custom) {
        custom.initialize(plugin);
        Preconditions.checkArgument(!map.containsKey(custom.getId()), "Registry already contains key " + custom.getId());
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
    public boolean contains(@Nullable String id) {
        return map.containsKey(id);
    }

    @Override
    public @NotNull Collection<String> getIds() {
        return map.keySet();
    }
    protected void logRegistered(T custom){
        LogUtils.pluginLog("("+ getId() +") " + custom.getId());
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
