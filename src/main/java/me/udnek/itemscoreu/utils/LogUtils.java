package me.udnek.itemscoreu.utils;

import me.udnek.itemscoreu.ItemsCoreU;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;

public class LogUtils {

    public static void log(String message){ Bukkit.getLogger().info(message);}
    public static void log(Component message){ Bukkit.getLogger().info(message.toString());}
    public static void pluginLog(String message){
        ItemsCoreU.getInstance().getLogger().info(message);
    }
}
