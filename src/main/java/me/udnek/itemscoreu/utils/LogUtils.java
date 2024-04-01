package me.udnek.itemscoreu.utils;

import me.udnek.itemscoreu.ItemsCoreU;
import org.bukkit.Bukkit;

public class LogUtils {

    public static void log(String message){
        Bukkit.getLogger().info(message);
    }

    public static void pluginLog(String message){
        ItemsCoreU.getInstance().getLogger().info(message);
    }
}
