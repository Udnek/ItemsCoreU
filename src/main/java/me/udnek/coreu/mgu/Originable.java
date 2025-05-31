package me.udnek.coreu.mgu;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public interface Originable {

    static @NotNull Location setOrigin(@NotNull Location location, @NotNull Location origin){
        location.setWorld(origin.getWorld());
        location.add(origin.x(), origin.y(), origin.z());
        return location;
    }

    void setOrigin(@NotNull Location origin);
}
