package me.udnek.itemscoreu.customevent;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class CustomEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    @Override
    public final @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
