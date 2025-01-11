package me.udnek.itemscoreu.customblock.block;

import org.bukkit.Bukkit;
import org.bukkit.block.TileState;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.NotNull;

public abstract class ConstructableCustomBlockEntity<T extends TileState> implements CustomBlockEntity {

    private T state;

    public int getTickDelay(){return 5;}

    @Override
    @MustBeInvokedByOverriders
    public void load(@NotNull TileState block) {
        this.state = (T) block;
    }

    @Override
    @MustBeInvokedByOverriders
    public void unload() {}

    public abstract void delayedTick();

    @Override
    public final void tick() {
        if (Bukkit.getCurrentTick() % getTickDelay() == 0) delayedTick();
    }

    @Override
    public @NotNull TileState getState() {
        return state;
    }
}
