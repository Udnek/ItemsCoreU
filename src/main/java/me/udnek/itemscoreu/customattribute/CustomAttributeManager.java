package me.udnek.itemscoreu.customattribute;

import me.udnek.itemscoreu.customevent.AllEventListener;
import me.udnek.itemscoreu.customevent.CustomEventManager;
import me.udnek.itemscoreu.utils.LogUtils;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomAttributeManager {
    public static CustomAttribute register(JavaPlugin plugin, CustomAttribute customAttribute){
        customAttribute.register(plugin);
        if (customAttribute instanceof AllEventListener allEventListener) CustomEventManager.addListener(allEventListener);
        LogUtils.pluginLog(customAttribute.getId() + " (Attribute)");
        return customAttribute;
    }

}
