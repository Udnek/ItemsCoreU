package me.udnek.itemscoreu.nms.loot.table;

import me.udnek.itemscoreu.nms.loot.pool.NmsLootPoolContainer;
import me.udnek.itemscoreu.nms.loot.util.NmsFunctioned;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public interface NmsLootTableContainer extends Supplier<LootTable>, NmsFunctioned {
    void addPool(@NotNull NmsLootPoolContainer container);
    @NotNull NmsLootPoolContainer getPool(int n);
    void removePool(int n);
    @NotNull NmsLootTableContainer copy();


}
