package me.udnek.itemscoreu.utils;

import me.udnek.itemscoreu.ItemsCoreU;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;

import java.lang.reflect.AccessFlag;
import java.lang.reflect.Field;

public class LogUtils {

    public static void log(String message){ Bukkit.getLogger().info(message);}
    public static void log(String message, TextColor color){ log(Component.text(message).color(color));}
    public static void log(Component message){ Bukkit.getConsoleSender().sendMessage(message);}
    public static void pluginLog(String message){
        ItemsCoreU.getInstance().getLogger().info(message);
    }

    public static void logDeclaredFields(Object object){
        Class<?> clazz = object.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            String message = field.toString();
            if (field.accessFlags().contains(AccessFlag.PRIVATE)){
                log(message, TextColor.color(1f, 0,0f));
            }
            else if(field.accessFlags().contains(AccessFlag.FINAL)){
                log(message, TextColor.color(1f, 1f, 0));
            }
            else {
                log(message);
            }
        }

    }
}
