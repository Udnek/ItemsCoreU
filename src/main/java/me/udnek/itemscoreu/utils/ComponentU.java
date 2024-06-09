package me.udnek.itemscoreu.utils;

import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;

public class ComponentU {

    public static Component translatableWithInsertion(String key, Component ...insertion){
        return Component.translatable(key, List.of(insertion));
    }

    public static List<Component> translate(String ...keys){
        List<Component> components = new ArrayList<>();
        for (String key : keys) {
            components.add(Component.translatable(key));
        }
        return components;
    }
}
