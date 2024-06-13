package me.udnek.itemscoreu.customevent;

import org.bukkit.event.Event;

public interface AllEventListener {
    default void onCustomEvent(CustomEvent event){}
    default void onBukkitEvent(Event event){}

}
