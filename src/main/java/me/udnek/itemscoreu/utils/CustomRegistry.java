package me.udnek.itemscoreu.utils;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class CustomRegistry<T extends PluginInitializable> {

    public T register(JavaPlugin plugin, T custom){
        if (plugin == null) throw new RuntimeException("Plugin can not be null!");
        custom.initialize(plugin);
        put(custom);
        logRegistered(custom);
        if (custom instanceof Listener listener){
            Bukkit.getPluginManager().registerEvents(listener, plugin);
            LogUtils.pluginLog("(EventListener) " + listener.getClass().getName());
        }
        return custom;
    }
    protected void logRegistered(T custom){
        LogUtils.pluginLog("("+getCategory()+") " + getIdToLog(custom));
    }
    public abstract String getCategory();
    protected abstract String getIdToLog(T custom);
    protected abstract void put(T custom);

}
