package me.udnek.itemscoreu.utils;

import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.Arrays;

public class DecorUtils {
    public static <T extends Component> Component constructTranslatable(String key, T ...values){
        return Component.translatable(key, new ArrayList<T>(Arrays.asList(values)));
    }
}
