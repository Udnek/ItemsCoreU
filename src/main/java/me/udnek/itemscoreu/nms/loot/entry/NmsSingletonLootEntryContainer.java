package me.udnek.itemscoreu.nms.loot.entry;

import me.udnek.itemscoreu.nms.NmsUtils;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntry;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class NmsSingletonLootEntryContainer implements NmsLootEntryContainer{

    @NotNull LootPoolSingletonContainer entry;

    public NmsSingletonLootEntryContainer(@NotNull LootPoolSingletonContainer entry){
        this.entry = entry;
    }

    public static @Nullable NmsSingletonLootEntryContainer fromVanilla(@NotNull LootTable lootTable, Predicate<ItemStack> predicate){
        for (LootPoolSingletonContainer container : NmsUtils.getAllSingletonContainers(NmsUtils.toNmsLootTable(lootTable))) {
            LootPoolEntry entry = NmsUtils.getEntry(container);
            List<net.minecraft.world.item.ItemStack> loot = new ArrayList<>();
            NmsUtils.getPossibleLoot(entry, loot::add);
            for (net.minecraft.world.item.ItemStack stack : loot) {
                ItemStack itemStack = NmsUtils.toBukkitItemStack(stack);
                if (predicate.test(itemStack)) return new NmsSingletonLootEntryContainer(container);
            }
        }
        return null;
    }

    @Override
    public @NotNull LootPoolEntryContainer get() {
        return entry;
    }
}
