package me.udnek.itemscoreu.custominventory;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
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
    public abstract String getRawDisplayName();
    public abstract int getInventorySize();
    public void generateInventory(int size, Component title){
        inventory = Bukkit.createInventory(this, size, title);
    }
    public Component getDisplayName(){
        return Component.translatable(this.getRawDisplayName());
    }

    @NotNull
    @Override
    public Inventory getInventory() {return inventory;}

}
