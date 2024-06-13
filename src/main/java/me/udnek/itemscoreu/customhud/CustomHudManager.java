package me.udnek.itemscoreu.customhud;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class CustomHudManager {

    private static final HashMap<JavaPlugin, CustomHud> tickets = new HashMap<>();

    public static void addTicket(JavaPlugin plugin, CustomHud customHud){
        tickets.put(plugin, customHud);
    }

    public static void removeTicket(JavaPlugin plugin){
        tickets.remove(plugin);
    }

    public static HashMap<JavaPlugin, CustomHud> getAllTickets() {
        return tickets;
    }
}
