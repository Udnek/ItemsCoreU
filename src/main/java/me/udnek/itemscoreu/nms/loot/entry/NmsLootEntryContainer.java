package me.udnek.itemscoreu.nms.loot.entry;

import me.udnek.itemscoreu.nms.loot.util.NmsConditioned;
import net.minecraft.world.level.storage.loot.entries.CompositeEntryBase;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public interface NmsLootEntryContainer extends Supplier<LootPoolEntryContainer>, NmsConditioned {

    static @NotNull NmsLootEntryContainer from(@NotNull LootPoolEntryContainer entry){
        if (entry instanceof NestedLootTable nested)
            return new NmsNestedEntryContainer(nested);
        if (entry instanceof LootPoolSingletonContainer singleton)
            return new NmsSingletonEntryContainer(singleton);
        return new NmsCompositeEntryContainer((CompositeEntryBase) entry);
    }
}
