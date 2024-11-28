package me.udnek.itemscoreu.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.function.Consumer;

public class Utils {
    private Utils(){}


    public static @NotNull String roundToTwoDigits(double value){
        if (value % 1 == 0){
            return String.valueOf((int) value);
        }
        return new DecimalFormat("#.##").format(value);
    }

    public static <T> void consumeIfNotNull(@Nullable T object, @NotNull Consumer<@NotNull T> consumer){
        if (object != null) consumer.accept(object);
    }
}
