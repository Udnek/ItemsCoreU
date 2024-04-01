package me.udnek.itemscoreu.custominventory;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public abstract class CustomInventory implements InventoryHolder {
    protected Inventory inventory = Bukkit.createInventory(this, this.getInventorySize(), this.getDisplayName());
    public String getRawDisplayName(){return "";}
    public abstract int getInventorySize();

    public Component getDisplayName(){
        return Component.translatable(this.getRawDisplayName());
    }

    public void open(Player player) {
        player.openInventory(this.inventory);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }


    public void onPlayerClicksItem(InventoryClickEvent event){}
    public void afterPlayerClicksItem(InventoryClickEvent event){}
    public void onPlayerDragsItem(InventoryDragEvent event){}
    public void onPlayerClosesInventory(InventoryCloseEvent event){}

}

