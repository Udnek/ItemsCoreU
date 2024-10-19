package me.udnek.itemscoreu.nms.loot.table;

import me.udnek.itemscoreu.nms.loot.pool.NmsLootPoolContainer;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.NotNull;

public class NmsDefaultLootTableContainer implements NmsLootTableContainer{

    @NotNull LootTable lootTable;

    public NmsDefaultLootTableContainer(@NotNull LootTable lootTable){
        this.lootTable = lootTable;
    }

    public @NotNull NmsDefaultLootTableContainer copy(){
        return this;
    }

    @Override
    public @NotNull LootTable get() {return lootTable;}

    @Override
    public void addLootPool(@NotNull NmsLootPoolContainer container) {

    }
}
