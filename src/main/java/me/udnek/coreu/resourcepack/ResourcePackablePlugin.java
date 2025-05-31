package me.udnek.coreu.resourcepack;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public interface ResourcePackablePlugin extends Plugin {
    default @NotNull VirtualResourcePack getResourcePack(){
        return new VirtualResourcePack(this);
    }
}
