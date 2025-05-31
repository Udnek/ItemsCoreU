package me.udnek.coreu.custom.loot.entry;

import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class SimpleLootEntry implements LootTableEntry {
    protected float chance;
    protected ItemStack itemStack;

    public SimpleLootEntry(@NotNull ItemStack itemStack, float chance){
        this.itemStack = itemStack;
        this.chance = chance;
    }

    @Override
    public @Nullable ItemStack roll(@Nullable Random random, @NotNull LootContext lootContext) {
        if (random == null) random = new Random();
        if (random.nextDouble() * lootContext.getLuck() > (1 - chance)) return itemStack.clone();
        return null;
    }

    @Override
    public @NotNull ItemStack getItem() {
        return itemStack.clone();
    }

    @Override
    public void replaceItem(@NotNull ItemStack newItem) {
        this.itemStack = newItem.clone();
    }
}
