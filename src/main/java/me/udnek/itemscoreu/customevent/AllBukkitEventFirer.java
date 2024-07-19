package me.udnek.itemscoreu.customevent;

import me.udnek.itemscoreu.utils.SelfRegisteringListener;
import org.bukkit.plugin.java.JavaPlugin;

public class AllBukkitEventFirer extends SelfRegisteringListener {
    public AllBukkitEventFirer(JavaPlugin plugin) {
        super(plugin);
    }

}
