package me.udnek.itemscoreu.nms.loot.entry;

import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.CompositeEntryBase;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public interface NmsLootEntryContainer extends Supplier<LootPoolEntryContainer> {

    static @NotNull NmsLootEntryContainer from(@NotNull LootPoolEntryContainer entry){
        if (entry instanceof LootPoolSingletonContainer singleton) {
            return new NmsSingletonLootEntryContainer(singleton);
        }
        return new NmsCompositeLootEntryContainer((CompositeEntryBase) entry);
    }
}
