package me.udnek.itemscoreu.custominventory;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryHolder;

public interface CustomInventory extends InventoryHolder {
    default void open(Player player) {player.openInventory(getInventory());}
    default void onPlayerClicksItem(InventoryClickEvent event){}
    default void afterPlayerClicksItem(InventoryClickEvent event){}
    default void onPlayerDragsItem(InventoryDragEvent event){}
    default void onPlayerClosesInventory(InventoryCloseEvent event){}
    default void onPlayerOpensInventory(InventoryOpenEvent event){}
}

