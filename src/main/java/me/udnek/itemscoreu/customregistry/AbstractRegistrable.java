package me.udnek.itemscoreu.customregistry;

import com.google.common.base.Preconditions;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractRegistrable implements Registrable{
    private String id;
    @Override
    public void initialize(@NotNull Plugin plugin) {
        Preconditions.checkArgument(id == null, "Registrable already initialized!");
        id = new NamespacedKey(plugin, getRawId()).asString();
    }

    public abstract @NotNull String getRawId();

    @Override
    public @NotNull String getId() {return id;}
}
