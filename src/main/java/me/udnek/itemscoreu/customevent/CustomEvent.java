package me.udnek.itemscoreu.customevent;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public abstract class CustomEvent extends Event{

    protected static final HandlerList HANDLER_LIST = new HandlerList();
    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

}
