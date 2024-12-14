package me.udnek.itemscoreu.custominventory;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
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

    public void addItem(int slot, int amount){
        ItemStack item = getInventory().getItem(slot);
        if (item == null) return;
        getInventory().setItem(slot, item.add(amount));
    }
    public void takeItem(int slot, int amount){
        addItem(slot, -amount);
    }


    @NotNull
    @Override
    public Inventory getInventory() {return inventory;}
}
