package me.udnek.itemscoreu.customblock.block;

import me.udnek.itemscoreu.customblock.type.CustomBlockEntityType;
import org.bukkit.block.TileState;
import org.jetbrains.annotations.NotNull;

public interface CustomBlockEntity {
    void load(@NotNull TileState block);
    void unload();
    void tick();
    @NotNull TileState getState();
    @NotNull CustomBlockEntityType<?> getType();
}