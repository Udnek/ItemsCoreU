package me.udnek.coreu.custom.loot.table;

import me.udnek.coreu.custom.loot.entry.LootTableEntry;
import me.udnek.coreu.custom.registry.Registrable;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface CustomLootTable extends LootTable, Registrable {
    @Override
    default @NotNull String getId(){return getKey().asString();}

    void addEntry(@NotNull LootTableEntry lootTableEntry);
    void removeEntry(@NotNull LootTableEntry lootTableEntry);
    void removeItem(@NotNull ItemStack itemStack);
    void replaceItem(@NotNull ItemStack oldItem, ItemStack newItem);
    @NotNull List<ItemStack> getAllItems();
    boolean containsItem(ItemStack itemStack);
    void onContainerLootGeneratesEvent(LootGenerateEvent event);
    void onMobDeathEvent(EntityDeathEvent event);
}
