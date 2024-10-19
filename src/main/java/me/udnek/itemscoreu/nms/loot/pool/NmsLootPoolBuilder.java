package me.udnek.itemscoreu.nms.loot.pool;

import com.google.common.base.Preconditions;
import me.udnek.itemscoreu.nms.NmsUtils;
import me.udnek.itemscoreu.nms.loot.entry.NmsCompositeLootEntryContainer;
import me.udnek.itemscoreu.nms.loot.entry.NmsLootEntryContainer;
import me.udnek.itemscoreu.nms.loot.entry.NmsSingletonLootEntryContainer;
import me.udnek.itemscoreu.utils.NMS.Reflex;
import net.minecraft.Util;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.CompositeEntryBase;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class NmsLootPoolBuilder implements NmsLootPoolContainer{

    protected Integer rolls = 1;
    protected int bonusRolls = 0;
    protected List<LootPoolEntryContainer> entries = new ArrayList<>();
    protected List<LootItemCondition> conditions;
    protected List<LootItemFunction> functions;

    public NmsLootPoolBuilder(@NotNull NmsLootEntryContainer...entries){
        Arrays.asList(entries).forEach(nms -> this.entries.add(nms.get()));
    }

    public @NotNull NmsLootPoolBuilder rolls(int rolls){
        this.rolls = rolls; return this;
    }

    public @NotNull NmsLootPoolBuilder bonusRolls(int bonusRolls){
        this.bonusRolls = bonusRolls; return this;
    }

    @Override
    public @NotNull NmsLootEntryContainer getEntry(int n) {
        return NmsLootEntryContainer.from(entries.get(n));
    }

    @Override
    public void addEntry(@NotNull NmsLootEntryContainer entry) {entries.add(entry.get());}

    protected LootPoolSingletonContainer getContainerByPredicate(@NotNull LootTable lootTable, Predicate<ItemStack> predicate){
        LootPoolSingletonContainer foundContainer = NmsUtils.getSingletonContainerByItems(
                NmsUtils.toNmsLootTable(lootTable),
                itemStack -> predicate.test(NmsUtils.toBukkitItemStack(itemStack)));

        return foundContainer;
    }
    public @NotNull NmsLootPoolBuilder copyConditionsFrom(@NotNull LootTable lootTable, Predicate<ItemStack> predicate){
        LootPoolSingletonContainer foundContainer = getContainerByPredicate(lootTable, predicate);
        Preconditions.checkArgument(foundContainer != null, "Container not found!");
        this.conditions = (List<LootItemCondition>) Reflex.getFieldValue(foundContainer, "conditions");
        return this;
    }
    public @NotNull NmsLootPoolBuilder copyFunctionsFrom(@NotNull LootTable lootTable, Predicate<ItemStack> predicate){
        LootPoolSingletonContainer foundContainer = getContainerByPredicate(lootTable, predicate);
        Preconditions.checkArgument(foundContainer != null, "Container not found!");
        this.functions = (List<LootItemFunction>) Reflex.getFieldValue(foundContainer, "functions");
        return this;
    }

    public void validate(){
        for (LootPoolEntryContainer container : entries) {
            Preconditions.checkArgument(container != null, "Container can not be null!");
        }
    }

    public LootPool build(){
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
        Reflex.setFieldValue(lootPool, "entries", new ArrayList<>(entries));
        return lootPool;
    }



    @Override
    public @NotNull LootPool get() {
        return build();
    }
}
