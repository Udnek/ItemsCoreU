package me.udnek.itemscoreu.customevent;

import me.udnek.itemscoreu.utils.SelfRegisteringListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class AllBukkitEventListener extends SelfRegisteringListener {
    public AllBukkitEventListener(JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event){CustomEventManager.fire(event);}
    @EventHandler
    public void onShoot(EntityShootBowEvent event){CustomEventManager.fire(event);}
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event){CustomEventManager.fire(event);}
}
