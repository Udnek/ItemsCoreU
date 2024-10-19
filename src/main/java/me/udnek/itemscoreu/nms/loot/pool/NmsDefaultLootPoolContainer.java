package me.udnek.itemscoreu.nms.loot.pool;

import me.udnek.itemscoreu.nms.NmsUtils;
import me.udnek.itemscoreu.nms.loot.entry.NmsLootEntryContainer;
import me.udnek.itemscoreu.utils.NMS.Reflex;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import org.bukkit.loot.LootTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NmsDefaultLootPoolContainer implements NmsLootPoolContainer{

    @NotNull LootPool pool;

    public NmsDefaultLootPoolContainer(@NotNull LootPool pool){
        this.pool = pool;
    }

    public static @Nullable NmsDefaultLootPoolContainer fromVanilla(@NotNull LootTable lootTable, int n){
        if (n < 0) return null;
        List<LootPool> pools = NmsUtils.getPools(NmsUtils.toNmsLootTable(lootTable));
        if (n >= pools.size()) return null;
        return new NmsDefaultLootPoolContainer(pools.get(n));
    }

    public List<LootPoolEntryContainer> getEntries(){
        return (List<LootPoolEntryContainer>) Reflex.getFieldValue(pool, "entries");
    }

    @Override
    public void addEntry(@NotNull NmsLootEntryContainer entry) {
        List<LootPoolEntryContainer> newEntries = new ArrayList<>(getEntries());
        newEntries.add(entry.get());
        Reflex.setFieldValue(pool, "entries", newEntries);
    }

    @Override
    public @NotNull NmsLootEntryContainer getEntry(int n) {
        return NmsLootEntryContainer.from(getEntries().get(n));
    }

    @Override
    public @NotNull LootPool get() {
        return pool;
    }
}
