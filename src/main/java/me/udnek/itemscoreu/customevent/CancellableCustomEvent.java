package me.udnek.itemscoreu.customevent;

import org.bukkit.event.Cancellable;

public abstract class CancellableCustomEvent extends CustomEvent implements Cancellable {

    protected boolean cancelled = false;

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}
