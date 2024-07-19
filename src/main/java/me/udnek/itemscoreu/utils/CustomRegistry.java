package me.udnek.itemscoreu.utils;

import me.udnek.itemscoreu.customevent.AllEventListener;
import me.udnek.itemscoreu.customevent.AllEventManager;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class CustomRegistry<T extends PluginInitializable> {

    public T register(JavaPlugin plugin, T custom){
        if (plugin == null) throw new RuntimeException("Plugin can not be null!");
        custom.initialize(plugin);
        put(custom);
        logRegistered(custom);
        if (custom instanceof AllEventListener allEventListener) AllEventManager.addListener(allEventListener);
        return custom;
    }
    protected void logRegistered(T custom){
        LogUtils.pluginLog("("+getCategory()+") " + getIdToLog(custom));
    }
    public abstract String getCategory();
    protected abstract String getIdToLog(T custom);
    protected abstract void put(T custom);

}
