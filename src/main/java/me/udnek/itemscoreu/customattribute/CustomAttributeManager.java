package me.udnek.itemscoreu.customattribute;

import me.udnek.itemscoreu.utils.LogUtils;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomAttributeManager {
    public static CustomAttribute register(JavaPlugin plugin, CustomAttribute customAttribute){
        customAttribute.register(plugin);
        LogUtils.pluginLog(customAttribute.getId() + " (Attribute)");
        return customAttribute;
    }

}
