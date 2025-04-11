package me.udnek.itemscoreu.customloot.entry;

import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public interface LootTableEntry {
    @Nullable ItemStack roll(@Nullable Random random, @NotNull LootContext lootContext);
    @NotNull ItemStack getItem();
    void replaceItem(@NotNull ItemStack newItem);
}
