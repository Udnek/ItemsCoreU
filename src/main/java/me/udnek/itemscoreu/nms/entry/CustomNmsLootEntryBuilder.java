package me.udnek.itemscoreu.nms.entry;

import me.udnek.itemscoreu.nms.NmsUtils;
import me.udnek.itemscoreu.utils.NMS.Reflex;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.bukkit.loot.LootTable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class CustomNmsLootEntryBuilder {

    protected int weight = 1;
    protected int quality = 0;
    protected List<LootItemFunction> functions = new ArrayList<>();
    protected List<LootItemCondition> conditions = new ArrayList<>();
    protected ItemStackCreator creator;

    public CustomNmsLootEntryBuilder(@NotNull ItemStackCreator creator){
        this.creator = creator;
    }

    public static @NotNull CustomNmsLootEntryBuilder fromVanilla(@NotNull LootTable lootTable, @NotNull Predicate<org.bukkit.inventory.ItemStack> predicate, @NotNull ItemStackCreator creator){
        CustomNmsLootEntryBuilder builder = new CustomNmsLootEntryBuilder(creator);
        return builder.copyConditionsFrom(lootTable, predicate).copyFunctionsFrom(lootTable, predicate);
    }

    public @NotNull CustomNmsLootEntryBuilder copyConditionsFrom(@NotNull LootTable lootTable, @NotNull Predicate<org.bukkit.inventory.ItemStack> predicate){
        LootPoolSingletonContainer foundContainer = NmsUtils.getSingletonContainerByItems(NmsUtils.toNmsLootTable(lootTable), new Predicate<net.minecraft.world.item.ItemStack>() {
            @Override
            public boolean test(net.minecraft.world.item.ItemStack itemStack) {
                return predicate.test(NmsUtils.toBukkitItemStack(itemStack));
            }
        });
        if (foundContainer == null) return this;
        List<LootItemCondition> conditions = (List<LootItemCondition>) Reflex.getFieldValue(foundContainer, "conditions");
        return conditions(conditions);
    }
    public @NotNull CustomNmsLootEntryBuilder copyFunctionsFrom(@NotNull LootTable lootTable, @NotNull Predicate<org.bukkit.inventory.ItemStack> predicate){
        LootPoolSingletonContainer foundContainer = NmsUtils.getSingletonContainerByItems(NmsUtils.toNmsLootTable(lootTable), new Predicate<net.minecraft.world.item.ItemStack>() {
            @Override
            public boolean test(net.minecraft.world.item.ItemStack itemStack) {
                return predicate.test(NmsUtils.toBukkitItemStack(itemStack));
            }
        });
        if (foundContainer == null) return this;
        List<LootItemFunction> functions = (List<LootItemFunction>) Reflex.getFieldValue(foundContainer, "functions");
        return functions(functions);
    }


    public @NotNull CustomNmsLootEntryBuilder weight(int weight){
        this.weight= weight;
        return this;
    }
    public @NotNull CustomNmsLootEntryBuilder quality(int quality){
        this.quality = quality;
        return this;
    }
    public @NotNull CustomNmsLootEntryBuilder creator(@NotNull ItemStackCreator creator){
        this.creator = creator;
        return this;
    }
    public @NotNull CustomNmsLootEntryBuilder functions(@NotNull List<LootItemFunction> functions){
        this.functions = functions;
        return this;
    }
    public @NotNull CustomNmsLootEntryBuilder conditions(@NotNull List<LootItemCondition> conditions){
        this.conditions = conditions;
        return this;
    }


    public @NotNull CustomNmsLootEntry build(){
        return new CustomNmsLootEntry(weight, quality, conditions, functions, creator);
    }
}


















