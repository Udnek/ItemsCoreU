package me.udnek.itemscoreu.customitem;

import me.udnek.itemscoreu.customcomponent.CustomComponentType;
import me.udnek.itemscoreu.util.SelfRegisteringListener;
import me.udnek.itemscoreu.util.Utils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
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
        customItem.getComponents().getOrDefault(CustomComponentType.RIGHT_CLICKABLE_ITEM).onRightClick(customItem, event);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        CustomItem customItem = CustomItem.get(event.getItemInHand());
        if (customItem == null) return;
        if (!(customItem instanceof CustomBlockItem blockItem)) return;
        blockItem.onBlockPlace(event);
    }

    @EventHandler
    public void updateItemOnTake(InventoryClickEvent event){
        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null) return;
        if (clickedInventory.getType() == InventoryType.PLAYER) return;
        Utils.consumeIfNotNull(CustomItem.get(event.getCurrentItem()), customItem -> {
            event.setCurrentItem(customItem.update(event.getCurrentItem()));
        });
    }
    @EventHandler
    public void updateItemOnJoin(PlayerJoinEvent event){
        PlayerInventory inventory = event.getPlayer().getInventory();
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            int finalI = i;
            Utils.consumeIfNotNull(CustomItem.get(item), customItem -> {
                inventory.setItem(finalI, customItem.update(item));
            });
        }
    }
}














