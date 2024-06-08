package me.udnek.itemscoreu.customitem;

import me.udnek.itemscoreu.utils.SelfRegisteringListener;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrowableProjectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomItemListener extends SelfRegisteringListener {


    public CustomItemListener(JavaPlugin plugin) {
        super(plugin);
    }

    // TODO: 2/15/2024 FIX SO TWO ITEMS WONT FIRE AT ONE TICK
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        if (!event.getAction().isRightClick()) return;

        CustomItem customItem = CustomItem.get(event.getItem());
        if (customItem == null) return;
        if (customItem instanceof InteractableItem interactableItem){
            interactableItem.onRightClicks(event);
        }
    }
}














