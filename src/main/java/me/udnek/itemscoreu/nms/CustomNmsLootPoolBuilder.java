package me.udnek.itemscoreu.nms;

import com.google.common.base.Preconditions;
import me.udnek.itemscoreu.nms.entry.CustomNmsLootEntryBuilder;
import me.udnek.itemscoreu.utils.NMS.Reflex;
import net.minecraft.Util;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class CustomNmsLootPoolBuilder {

    protected Integer rolls = 1;
    protected int bonusRolls = 0;
    protected List<CustomNmsLootEntryBuilder> entries = new ArrayList<>();
    protected List<LootItemCondition> conditions;
    protected List<LootItemFunction> functions;

    public CustomNmsLootPoolBuilder(@NotNull CustomNmsLootEntryBuilder...entries){
        this.entries.addAll(Arrays.asList(entries));
    }
    public @NotNull CustomNmsLootPoolBuilder rolls(int rolls){
        this.rolls = rolls;
        return this;
    }
    public @NotNull CustomNmsLootPoolBuilder bonusRolls(int bonusRolls){
        this.bonusRolls = bonusRolls;
        return this;
    }

    protected LootPoolSingletonContainer getContainerByPredicate(@NotNull LootTable lootTable, Predicate<ItemStack> predicate){
        LootPoolSingletonContainer foundContainer = NmsUtils.getSingletonContainerByItems(NmsUtils.toNmsLootTable(lootTable), new Predicate<net.minecraft.world.item.ItemStack>() {
            @Override
            public boolean test(net.minecraft.world.item.ItemStack itemStack) {
                return predicate.test(NmsUtils.toBukkitItemStack(itemStack));
            }
        });

        return foundContainer;
    }
    public @NotNull CustomNmsLootPoolBuilder copyConditionsFrom(@NotNull LootTable lootTable, Predicate<ItemStack> predicate){
        LootPoolSingletonContainer foundContainer = getContainerByPredicate(lootTable, predicate);
        Preconditions.checkArgument(foundContainer != null, "Container not found!");
        this.conditions = (List<LootItemCondition>) Reflex.getFieldValue(foundContainer, "conditions");
        return this;
    }
    public @NotNull CustomNmsLootPoolBuilder copyFunctionsFrom(@NotNull LootTable lootTable, Predicate<ItemStack> predicate){
        LootPoolSingletonContainer foundContainer = getContainerByPredicate(lootTable, predicate);
        Preconditions.checkArgument(foundContainer != null, "Container not found!");
        this.functions = (List<LootItemFunction>) Reflex.getFieldValue(foundContainer, "functions");
        return this;
    }

    public void validate(){
        for (CustomNmsLootEntryBuilder container : entries) {
            Preconditions.checkArgument(container != null, "Container can not be null!");
        }
    }

    public LootPool create(){
        validate();

        LootPool.Builder builder = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(rolls))
                .setBonusRolls(ConstantValue.exactly(bonusRolls));
        LootPool lootPool = builder.build();
        if (conditions != null){
            Reflex.setFieldValue(lootPool, "conditions", conditions);
            Reflex.setFieldValue(lootPool, "compositeCondition", Util.allOf(conditions));
        }
        if (functions != null){
            Reflex.setFieldValue(lootPool, "functions", functions);
            Reflex.setFieldValue(lootPool, "compositeFunction", LootItemFunctions.compose(functions));
        }
        List<LootPoolEntryContainer> containers = new ArrayList<>();
        entries.forEach(b -> containers.add(b.build()));
        Reflex.setFieldValue(lootPool, "entries", containers);
        return lootPool;
    }


}
