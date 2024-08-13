package me.udnek.itemscoreu.utils;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

import java.util.ArrayList;
import java.util.List;

public class ComponentU {

    public static final TextColor NO_SHADOW_COLOR = TextColor.fromHexString("#4e5c24");

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

    public static Component space(int n){
        return Component.translatable("space."+n);
    }

    public static Component textWithNoSpace(int offset, Component text, int size){
        size += 1; // SHADOW COUNTS
        return ComponentU.space(offset).append(text).append(ComponentU.space(-size-offset));
    }
    public static Component textWithNoSpace(Component text, int size){
        size += 1; // SHADOW COUNTS
        return text.append(ComponentU.space(-size));
    }
}
