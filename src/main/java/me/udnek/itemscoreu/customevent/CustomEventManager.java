package me.udnek.itemscoreu.customevent;

import me.udnek.itemscoreu.customitem.CustomItem;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;

public class CustomEventManager {

    private static final List<CustomEventListener> eventListeners = new ArrayList<>();
    public static void addListener(CustomEventListener customEventListener){
        eventListeners.add(customEventListener);
    }
    public static void fire(CustomEvent event){
        eventListeners.forEach(eventListener -> eventListener.onCustomEvent(event));
    }
    public static void fire(Event event){
        eventListeners.forEach(eventListener -> eventListener.onBukkitEvent(event));
    }

}
