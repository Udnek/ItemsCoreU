package me.udnek.itemscoreu.custominventory;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public interface CustomInventory extends InventoryHolder {
    default void open(Player player) {player.openInventory(getInventory());}
    default void onPlayerClicksItem(InventoryClickEvent event){}
    default void afterPlayerClicksItem(InventoryClickEvent event){}
    default void onPlayerDragsItem(InventoryDragEvent event){}
    default void onPlayerClosesInventory(InventoryCloseEvent event){}
    default void onPlayerOpensInventory(InventoryOpenEvent event){}

}

