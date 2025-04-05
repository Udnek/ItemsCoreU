package me.udnek.itemscoreu.util;

import io.papermc.paper.math.Position;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

public class Utils {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US));

    public static @NotNull String roundToTwoDigits(double value){
        return DECIMAL_FORMAT.format(value);
    }

    public static void sendBlockDamageForAllPlayers(@NotNull Location location, float progress) {
        location.getWorld().getPlayers().forEach(player -> player.sendBlockDamage(location, progress));
    }

    public static void sendMultiBlockDamageForAllPlayers(@NotNull World world, @NotNull Map<? extends Position, BlockData> blockChanges) {
        world.getPlayers().forEach(player -> player.sendMultiBlockChange(blockChanges));
    }

    public static <T> void consumeIfNotNull(@Nullable T object, @NotNull Consumer<@NotNull T> consumer){
        if (object != null) consumer.accept(object);
    }
}
