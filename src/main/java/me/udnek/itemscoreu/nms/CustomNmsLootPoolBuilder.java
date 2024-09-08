package me.udnek.itemscoreu.nms;

import com.google.common.base.Preconditions;
import me.udnek.itemscoreu.nms.entry.CustomNmsLootEntry;
import me.udnek.itemscoreu.utils.LogUtils;
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

    protected Integer rolls;
    protected int bonusRolls;
    protected List<LootPoolEntryContainer> containers = new ArrayList<>();
    protected List<LootItemCondition> conditions;
    protected List<LootItemFunction> functions;

    public CustomNmsLootPoolBuilder(@NotNull CustomNmsLootEntry ...entries){
        containers.addAll(Arrays.asList(entries));
    }
    public CustomNmsLootPoolBuilder rolls(int rolls){
        this.rolls = rolls;
        return this;
    }
    public CustomNmsLootPoolBuilder bonusRolls(int bonusRolls){
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
    public CustomNmsLootPoolBuilder copyConditionsFrom(@NotNull LootTable lootTable, Predicate<ItemStack> predicate){
        LootPoolSingletonContainer foundContainer = getContainerByPredicate(lootTable, predicate);
        Preconditions.checkArgument(foundContainer != null, "Container not found!");
        this.conditions = (List<LootItemCondition>) Reflex.getFieldValue(foundContainer, "conditions");
        return this;
    }
    public CustomNmsLootPoolBuilder copyFunctionsFrom(@NotNull LootTable lootTable, Predicate<ItemStack> predicate){
        LootPoolSingletonContainer foundContainer = getContainerByPredicate(lootTable, predicate);
        Preconditions.checkArgument(foundContainer != null, "Container not found!");
        this.functions = (List<LootItemFunction>) Reflex.getFieldValue(foundContainer, "functions");
        return this;
    }

    public void validate(){
        Preconditions.checkArgument(rolls != null, "Rolls must be specified");
        for (LootPoolEntryContainer container : containers) {
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
        Reflex.setFieldValue(lootPool, "entries", containers);
        return lootPool;
    }


}
