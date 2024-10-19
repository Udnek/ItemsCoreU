package me.udnek.itemscoreu.nms.loot.entry;

import me.udnek.itemscoreu.nms.NmsUtils;
import me.udnek.itemscoreu.nms.loot.ItemStackCreator;
import me.udnek.itemscoreu.utils.NMS.Reflex;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.bukkit.loot.LootTable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class NmsCustomLootEntryBuilder implements NmsLootEntryContainer{

    protected int weight = 1;
    protected int quality = 0;
    protected List<LootItemFunction> functions = new ArrayList<>();
    protected List<LootItemCondition> conditions = new ArrayList<>();
    protected ItemStackCreator creator;

    public NmsCustomLootEntryBuilder(@NotNull ItemStackCreator creator){
        this.creator = creator;
    }

    public static @NotNull NmsCustomLootEntryBuilder fromVanilla(@NotNull LootTable lootTable, @NotNull Predicate<org.bukkit.inventory.ItemStack> predicate, @NotNull ItemStackCreator creator){
        NmsCustomLootEntryBuilder builder = new NmsCustomLootEntryBuilder(creator);
        return builder.copyConditionsFrom(lootTable, predicate).copyFunctionsFrom(lootTable, predicate);
    }

    public @NotNull NmsCustomLootEntryBuilder copyConditionsFrom(@NotNull LootTable lootTable, @NotNull Predicate<org.bukkit.inventory.ItemStack> predicate){
        LootPoolSingletonContainer foundContainer = NmsUtils.getSingletonContainerByItems(
                NmsUtils.toNmsLootTable(lootTable),
                itemStack -> predicate.test(NmsUtils.toBukkitItemStack(itemStack)));
        if (foundContainer == null) return this;
        List<LootItemCondition> conditions = (List<LootItemCondition>) Reflex.getFieldValue(foundContainer, "conditions");
        return conditions(conditions);
    }
    public @NotNull NmsCustomLootEntryBuilder copyFunctionsFrom(@NotNull LootTable lootTable, @NotNull Predicate<org.bukkit.inventory.ItemStack> predicate){
        LootPoolSingletonContainer foundContainer = NmsUtils.getSingletonContainerByItems(
                NmsUtils.toNmsLootTable(lootTable),
                itemStack -> predicate.test(NmsUtils.toBukkitItemStack(itemStack)));
        if (foundContainer == null) return this;
        List<LootItemFunction> functions = (List<LootItemFunction>) Reflex.getFieldValue(foundContainer, "functions");
        return functions(functions);
    }


    public @NotNull NmsCustomLootEntryBuilder weight(int weight){
        this.weight= weight;
        return this;
    }
    public @NotNull NmsCustomLootEntryBuilder quality(int quality){
        this.quality = quality;
        return this;
    }
    public @NotNull NmsCustomLootEntryBuilder creator(@NotNull ItemStackCreator creator){
        this.creator = creator;
        return this;
    }
    public @NotNull NmsCustomLootEntryBuilder functions(@NotNull List<LootItemFunction> functions){
        this.functions = functions;
        return this;
    }
    public @NotNull NmsCustomLootEntryBuilder conditions(@NotNull List<LootItemCondition> conditions){
        this.conditions = conditions;
        return this;
    }


    public @NotNull NmsCustomLootEntry build(){
        return new NmsCustomLootEntry(weight, quality, conditions, functions, creator);
    }

    @Override
    public @NotNull LootPoolEntryContainer get() {
        return build();
    }
}


















