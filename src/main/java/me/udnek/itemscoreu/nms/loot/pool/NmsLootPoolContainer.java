package me.udnek.itemscoreu.nms.loot.pool;

import me.udnek.itemscoreu.nms.loot.entry.NmsCompositeLootEntryContainer;
import me.udnek.itemscoreu.nms.loot.entry.NmsLootEntryContainer;
import me.udnek.itemscoreu.nms.loot.entry.NmsSingletonLootEntryContainer;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.CompositeEntryBase;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface NmsLootPoolContainer extends Supplier<LootPool> {
    void addEntry(@NotNull NmsLootEntryContainer entry);
    @NotNull NmsLootEntryContainer getEntry(int n);
}
