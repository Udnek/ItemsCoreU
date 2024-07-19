package me.udnek.itemscoreu.customloot.table;

import me.udnek.itemscoreu.customloot.entry.LootTableEntry;
import me.udnek.itemscoreu.utils.PluginInitializable;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface CustomLootTable extends LootTable, PluginInitializable {
    void addEntry(@NotNull LootTableEntry lootTableEntry);
    void removeEntry(@NotNull LootTableEntry lootTableEntry);
    void removeItem(@NotNull ItemStack itemStack);
    @NotNull List<ItemStack> getAllItems();
    void onLootGeneratesEvent(LootGenerateEvent event);
    boolean containsItem(ItemStack itemStack);
}
