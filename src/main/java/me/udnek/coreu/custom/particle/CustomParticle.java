package me.udnek.coreu.custom.particle;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public interface CustomParticle {
    void play(@NotNull Location location);
    void stop();
}
