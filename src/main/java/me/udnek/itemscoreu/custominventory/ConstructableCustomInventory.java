package me.udnek.itemscoreu.custominventory;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ConstructableCustomInventory implements CustomInventory{

    private @Nullable Inventory inventory;

    public abstract @Nullable Component getTitle();
    public abstract int getInventorySize();

    public @NotNull Inventory generateInventory(int size, @Nullable Component title){
        Inventory inventory;
        if (title == null) inventory = Bukkit.createInventory(this, size);
        else inventory = Bukkit.createInventory(this, size, title);
        return inventory;
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
    public Inventory getInventory() {
        if (inventory == null) inventory = generateInventory(getInventorySize(), getTitle());
        return inventory;
    }
}
