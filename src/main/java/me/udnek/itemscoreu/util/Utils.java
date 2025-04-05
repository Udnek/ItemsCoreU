package me.udnek.itemscoreu.util;

import me.udnek.itemscoreu.ItemsCoreU;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
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

    public static void sendBlockDamageForAllPlayers(@NotNull Location location, float startProgress) {
        sendBlockDamageForAllPlayers(location, startProgress, startProgress, 5);
    }

    public static void sendBlockDamageForAllPlayers(@NotNull Location location, float startProgress, float step, long tickRate) {
        location.getWorld().getPlayers().forEach(player ->
                        new BukkitRunnable() {
                            float progress = startProgress;
                            @Override
                            public void run() {
                                player.sendBlockDamage(location, progress, location.getBlock().hashCode());
                                progress -= step;
                                if (progress < 0 || progress > 1) cancel();
                            }
                        }.runTaskTimer(ItemsCoreU.getInstance(), 0, tickRate));

    }


    public static <T> void consumeIfNotNull(@Nullable T object, @NotNull Consumer<@NotNull T> consumer){
        if (object != null) consumer.accept(object);
    }
}
