package me.udnek.itemscoreu.custominventory;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public abstract class ConstructableCustomInventory implements CustomInventory{

    protected Inventory inventory;
    public ConstructableCustomInventory(){
        initialize();
    }
    protected void initialize(){
        generateInventory(getInventorySize(), getDisplayName());
    }
    public abstract Component getDisplayName();
    public abstract int getInventorySize();
    public void generateInventory(int size, Component title){
        inventory = Bukkit.createInventory(this, size, title);
    }

    @NotNull
    @Override
    public Inventory getInventory() {return inventory;}
}
