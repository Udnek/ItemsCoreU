package me.udnek.itemscoreu.customitem;

import me.udnek.itemscoreu.customcomponent.CustomComponentType;
import me.udnek.itemscoreu.util.SelfRegisteringListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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
        customItem.getComponentOrDefault(CustomComponentType.RIGHT_CLICKABLE_ITEM).onRightClick(customItem, event);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        CustomItem customItem = CustomItem.get(event.getItemInHand());
        if (customItem == null) return;
        if (!(customItem instanceof CustomBlockItem blockItem)) return;
        blockItem.onBlockPlace(event);
    }
}














