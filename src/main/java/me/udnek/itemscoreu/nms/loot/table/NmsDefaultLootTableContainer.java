package me.udnek.itemscoreu.nms.loot.table;

import me.udnek.itemscoreu.nms.loot.pool.NmsDefaultLootPoolContainer;
import me.udnek.itemscoreu.nms.loot.pool.NmsLootPoolContainer;
import me.udnek.itemscoreu.nms.NmsContainer;
import me.udnek.itemscoreu.nms.loot.util.NmsFields;
import me.udnek.itemscoreu.nms.loot.util.NmsLootFunctionsContainer;
import me.udnek.itemscoreu.util.Reflex;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NmsDefaultLootTableContainer extends NmsContainer<LootTable> implements NmsLootTableContainer{


    public NmsDefaultLootTableContainer(@NotNull LootTable supply) {
        super(supply);
    }

    @Override
    public @NotNull NmsDefaultLootTableContainer copy(){
        LootTable newLootTable = Reflex.construct(
                Reflex.getFirstConstructor(LootTable.class),
                Reflex.getFieldValue(supply, "paramSet"),
                Reflex.getFieldValue(supply, "randomSequence"),
                new ArrayList<>((List<LootPool>) Reflex.getFieldValue(supply, NmsFields.POOLS)),
                new ArrayList<>((List<LootItemFunction>) Reflex.getFieldValue(supply, NmsFields.FUNCTIONS))
        );
        return new NmsDefaultLootTableContainer(newLootTable);
    }


    @Override
    public void addPool(@NotNull NmsLootPoolContainer container) {
        List<LootPool> pools = new ArrayList<>(Reflex.getFieldValue(supply, NmsFields.POOLS, List.class));
        pools.add(container.get());
        Reflex.setFieldValue(supply, NmsFields.POOLS, pools);
    }

    @Override
    public @NotNull NmsLootPoolContainer getPool(int n) {
        return new NmsDefaultLootPoolContainer(((List<LootPool>) Reflex.getFieldValue(supply, NmsFields.POOLS)).get(n));
    }

    @Override
    public void removePool(int n) {
        List<LootPool> pools = new ArrayList<>(Reflex.getFieldValue(supply, NmsFields.POOLS, List.class));
        pools.remove(n);
        Reflex.setFieldValue(supply, NmsFields.POOLS, pools);
    }

    @Override
    public @NotNull NmsLootFunctionsContainer getFunctions() {
        return (NmsLootFunctionsContainer) Reflex.getFieldValue(supply, NmsFields.FUNCTIONS);
    }


    @Override
    public void setFunctions(@NotNull NmsLootFunctionsContainer functions) {
        List<LootItemFunction> value = functions.get();
        Reflex.setFieldValue(supply, NmsFields.FUNCTIONS, value);
        Reflex.setFieldValue(supply, NmsFields.COMPOSITE_FUNCTIONS, LootItemFunctions.compose(value));
    }
}
