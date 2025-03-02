package me.udnek.itemscoreu.customsound;

import me.udnek.itemscoreu.customregistry.Registrable;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CustomSound extends Registrable {
    default void play(@NotNull Player player){
        play(null, player);
    }
    default void play(@NotNull Location location){
        play(location, null);
    }
    void play(@Nullable Location location, @Nullable Player player);
    void play(@Nullable Location location, @Nullable Player player, @NotNull SoundCategory category, float volume, float pitch);
}
