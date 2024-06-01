package me.udnek.itemscoreu.customentity;

import me.udnek.itemscoreu.utils.LogUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class CustomEntityManager {

    private static HashMap<String, CustomDumbTickingEntity> registeredEntites = new HashMap<>();


    public static CustomDumbTickingEntity register(JavaPlugin javaPlugin, CustomDumbTickingEntity customDumbTickingEntity){

        customDumbTickingEntity.register(javaPlugin);
        registeredEntites.put(customDumbTickingEntity.getId(), customDumbTickingEntity);
        LogUtils.pluginLog(customDumbTickingEntity.getId() + " (Entity)");
        return customDumbTickingEntity;

    }

    public static CustomDumbTickingEntity get(String id){

        return registeredEntites.getOrDefault(id, null);

    }

}
