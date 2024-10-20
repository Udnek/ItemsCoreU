package me.udnek.itemscoreu.customevent;

import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.util.LoreBuilder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomItemGeneratedEvent extends CustomEvent{
    CustomItem customItem;
    ItemStack itemStack;
    LoreBuilder loreBuilder;

    public CustomItemGeneratedEvent(@NotNull CustomItem customItem, @NotNull ItemStack itemStack, @Nullable LoreBuilder loreBuilder){
        this.customItem = customItem;
        this.itemStack = itemStack;
        if (loreBuilder == null) this.loreBuilder = new LoreBuilder();
        else this.loreBuilder = loreBuilder;
    }
    public @NotNull CustomItem getCustomItem() {
        return customItem;
    }
    public @NotNull ItemStack getItemStack() {
        return itemStack;
    }
    public @NotNull LoreBuilder getLoreBuilder() {
        return loreBuilder;
    }

    public void setItemStack(@NotNull ItemStack itemStack){
        this.itemStack = itemStack;
    }
}
