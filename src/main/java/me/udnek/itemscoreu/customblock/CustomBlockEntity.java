package me.udnek.itemscoreu.customblock;

import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.jetbrains.annotations.NotNull;

public interface CustomBlockEntity {
    void load(@NotNull TileState block);
    void unload();
    void tick();
    @NotNull TileState getState();
}