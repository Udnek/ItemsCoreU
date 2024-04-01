package me.udnek.itemscoreu.utils;

import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TranslateUtils {

    public static List<Component> getTranslated(List<String> strings){
        List<Component> lore = new ArrayList<Component>();
        for (String string : strings) {
            lore.add(Component.translatable(string));
        }
        return lore;
    }

    public static List<Component> getTranslated(String ...strings) {
        return getTranslated(Arrays.asList(strings));
    }

    public static Component getTranslatedWith(String key, String value){
        return Component.translatable(key, Arrays.asList(Component.text(value)));
    }

    public static Component getTranslatedWith(String key, Component component){
        return Component.translatable(key, Arrays.asList(component));
    }

}
