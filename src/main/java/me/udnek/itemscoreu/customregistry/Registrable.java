package me.udnek.itemscoreu.customregistry;

import com.google.common.base.Preconditions;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public interface Registrable extends Keyed {
    void initialize(@NotNull Plugin plugin);
    default void afterInitialization(){}
    @NotNull String getId();

    @Override
    default @NotNull NamespacedKey getKey(){
        Preconditions.checkArgument(getId() != null, "Id is not present (not registered yet probably)");
        return NamespacedKey.fromString(getId());
    }
}
