package me.udnek.itemscoreu.customevent;

import com.google.common.base.Preconditions;
import me.udnek.itemscoreu.utils.LogUtils;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;

public class AllEventManager {

    private static final List<AllEventListener> eventListeners = new ArrayList<>();
    public static void addListener(AllEventListener allEventListener){
        Preconditions.checkArgument(!eventListeners.contains(allEventListener), allEventListener + " is already registered!");
        LogUtils.pluginLog(allEventListener.getClass().getName() + " (EventListener)");
        eventListeners.add(allEventListener);
    }
    public static void fire(Event event){
        eventListeners.forEach(eventListener -> eventListener.onEvent(event));
    }

}
