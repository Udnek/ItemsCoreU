package me.udnek.itemscoreu.util;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ComponentU {

    public static final TextColor NO_SHADOW_COLOR = TextColor.fromHexString("#4e5c24");
    public static final Key DEFAULT_FONT = NamespacedKey.minecraft("default");

    public static Component translatableWithInsertion(@NotNull  String key, @NotNull  Component ...insertion){
        return Component.translatable(key, List.of(insertion));
    }

    public static @NotNull List<Component> translate(@NotNull String ...keys){
        List<Component> components = new ArrayList<>();
        for (String key : keys) {
            components.add(Component.translatable(key));
        }
        return components;
    }

    public static @NotNull Component space(int n){
        return Component.translatable("space."+n);
    }

    public static @NotNull Component spaceDefaultFont(int n){
        return Component.translatable("space."+n).font(DEFAULT_FONT);
    }

    public static @NotNull Component textWithNoSpace(int offset, @NotNull  Component text, int size){
        if (size != 0) size+=1; // SHADOW COUNTS
        return ComponentU.space(offset).append(text).append(ComponentU.space(-size-offset));
    }
    public static @NotNull Component textWithNoSpace(@NotNull Component text, int size){
        if (size == 0) return text;
        size += 1; // SHADOW COUNTS
        return text.append(ComponentU.space(-size));
    }

    public static @NotNull Component textWithNoSpaceDefaultFont(int offset, @NotNull Component text, int size){
        if (size != 0) size+=1; // SHADOW COUNTS
        return ComponentU.spaceDefaultFont(offset).append(text).append(ComponentU.spaceDefaultFont(-size-offset));
    }
    public static @NotNull Component textWithNoSpaceDefaultFont(@NotNull Component text, int size){
        if (size == 0) return text;
        size += 1; // SHADOW COUNTS
        return text.append(ComponentU.spaceDefaultFont(-size));
    }
}
