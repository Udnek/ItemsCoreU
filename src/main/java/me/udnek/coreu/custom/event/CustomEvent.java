package me.udnek.coreu.custom.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public abstract class CustomEvent extends Event{

    protected static final HandlerList HANDLER_LIST = new HandlerList();

    public static HandlerList getHandlerList(){return HANDLER_LIST;}
    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

}
