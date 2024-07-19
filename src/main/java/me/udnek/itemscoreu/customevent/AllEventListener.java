package me.udnek.itemscoreu.customevent;

import org.bukkit.event.Event;

public interface AllEventListener {
    default void onEvent(Event event){}
}
