package me.udnek.itemscoreu.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.function.Consumer;

public class Utils {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US));

    public static @NotNull String roundToTwoDigits(double value){
        return DECIMAL_FORMAT.format(value);
    }

    public static <T> void consumeIfNotNull(@Nullable T object, @NotNull Consumer<@NotNull T> consumer){
        if (object != null) consumer.accept(object);
    }
}
