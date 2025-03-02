package me.udnek.itemscoreu.nms.loot.pool;

import me.udnek.itemscoreu.nms.loot.entry.NmsLootEntryContainer;
import me.udnek.itemscoreu.nms.loot.util.NmsConditioned;
import me.udnek.itemscoreu.nms.loot.util.NmsFunctioned;
import net.minecraft.world.level.storage.loot.LootPool;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public interface NmsLootPoolContainer extends Supplier<LootPool>, NmsConditioned, NmsFunctioned {
    void addEntry(@NotNull NmsLootEntryContainer entry);
    void removeEntry(int n);
    @NotNull NmsLootEntryContainer getEntry(int n);
}
