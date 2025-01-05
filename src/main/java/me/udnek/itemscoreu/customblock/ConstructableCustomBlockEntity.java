package me.udnek.itemscoreu.customblock;

import org.bukkit.Bukkit;
import org.bukkit.block.TileState;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.NotNull;

public abstract class ConstructableCustomBlockEntity implements CustomBlockEntity{

    private TileState state;

    public int getTickDelay(){return 5;}

    @Override
    @MustBeInvokedByOverriders
    public void load(@NotNull TileState block) {
        this.state = block;
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
