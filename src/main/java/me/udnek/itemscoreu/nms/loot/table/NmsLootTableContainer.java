package me.udnek.itemscoreu.nms.loot.table;

import me.udnek.itemscoreu.nms.loot.pool.NmsLootPoolContainer;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public interface NmsLootTableContainer extends Supplier<LootTable> {
    void addLootPool(@NotNull NmsLootPoolContainer container);
}
