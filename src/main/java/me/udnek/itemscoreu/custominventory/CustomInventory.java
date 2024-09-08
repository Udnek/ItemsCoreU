package me.udnek.itemscoreu.custominventory;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CustomInventory extends InventoryHolder {
    static boolean isCustom(@NotNull Inventory inventory){
        return inventory.getHolder() instanceof CustomInventory;
    }
    static boolean isCustom(InventoryHolder holder){
        return holder instanceof CustomInventory;
    }
    static @Nullable CustomInventory get(@NotNull Inventory inventory){
        if (inventory.getHolder() instanceof CustomInventory customInventory){
            return customInventory;
        } return null;
    }
    static boolean isSame(CustomInventory customInventory, @NotNull Inventory inventory){
        return get(inventory) == customInventory;
    }
    default void open(Player player) {player.openInventory(getInventory());}
    default void onPlayerClicksItem(InventoryClickEvent event){}
    default void afterPlayerClicksItem(InventoryClickEvent event){}
    default void onPlayerDragsItem(InventoryDragEvent event){}
    default void onPlayerClosesInventory(InventoryCloseEvent event){}
    default void onPlayerOpensInventory(InventoryOpenEvent event){}
}

