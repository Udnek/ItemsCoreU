package me.udnek.itemscoreu.nms.entry;

import me.udnek.itemscoreu.nms.Nms;
import me.udnek.itemscoreu.nms.NmsUtils;
import me.udnek.itemscoreu.utils.NMS.Reflex;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntries;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.craftbukkit.v1_21_R1.CraftLootTable;
import org.bukkit.loot.LootTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class CustomNmsLootEntry extends LootPoolSingletonContainer {

    public CustomNmsLootEntry(){this(1, 0);}
    public CustomNmsLootEntry(int weight, int quality){
        this(weight, quality, List.of(), List.of());
    }
    protected CustomNmsLootEntry(int weight, int quality, List<LootItemCondition> conditions, List<LootItemFunction> functions) {
        super(weight, quality, conditions, functions);
    }

    protected static @Nullable Pair<List<LootItemCondition>, List<LootItemFunction>> getConditionsAndFunctions(@NotNull LootTable lootTable, @NotNull Predicate<org.bukkit.inventory.ItemStack> predicate){
        LootPoolSingletonContainer foundContainer = NmsUtils.getSingletonContainerByItems(NmsUtils.toNmsLootTable(lootTable), new Predicate<net.minecraft.world.item.ItemStack>() {
            @Override
            public boolean test(net.minecraft.world.item.ItemStack itemStack) {
                return predicate.test(NmsUtils.toBukkitItemStack(itemStack));
            }
        });
        if (foundContainer == null) return null;
        List<LootItemCondition> conditions = (List<LootItemCondition>) Reflex.getFieldValue(foundContainer, "conditions");
        List<LootItemFunction> functions = (List<LootItemFunction>) Reflex.getFieldValue(foundContainer, "functions");
        return Pair.of(conditions, functions);
    }


    @Override
    protected void createItemStack(Consumer<ItemStack> consumer, LootContext lootContext) {
        org.bukkit.inventory.ItemStack itemStack = createItemStack(CraftLootTable.convertContext(lootContext));
        consumer.accept(NmsUtils.toNmsItemStack(itemStack));
    }
    public abstract @NotNull org.bukkit.inventory.ItemStack createItemStack(@NotNull org.bukkit.loot.LootContext lootContext);
    @Override
    public LootPoolEntryType getType() {
        return LootPoolEntries.ITEM;
    }
}
