package me.udnek.itemscoreu.customattribute;

import me.udnek.itemscoreu.ItemsCoreU;
import me.udnek.itemscoreu.utils.LogUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class CustomAttributeManager {

    //private static final HashMap<String, CustomAttributeType> attributeInstances = new HashMap<>();

    public static CustomAttributeType register(JavaPlugin plugin, CustomAttributeType customAttributeType){
        customAttributeType.register(plugin);
        //attributeInstances.put(customAttributeType.getClass().getName(), customAttributeType);
        LogUtils.pluginLog(customAttributeType.getId() + " (Attribute)");
        return customAttributeType;
    }

/*    protected static CustomAttributeType getInstance(CustomAttributeType customAttributeType){
        return attributeInstances.get(customAttributeType.getClass().getName());
    }*/
}
