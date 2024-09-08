package me.udnek.itemscoreu.nms.entry;

import me.udnek.itemscoreu.customloot.entry.SimpleLootEntry;
import me.udnek.itemscoreu.nms.Nms;
import me.udnek.itemscoreu.nms.NmsUtils;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.Lootable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Predicate;

public class SimpleNmsEntry extends CustomNmsLootEntry{

    protected ItemStack itemStack;
    public SimpleNmsEntry(int weight, int quality, ItemStack itemStack) {
        super(weight, quality);
        this.itemStack = itemStack;
    }
    public SimpleNmsEntry(Pair<Integer, Integer> weightAndQuality, ItemStack itemStack) {
        super(weightAndQuality.getLeft(), weightAndQuality.getRight());
        this.itemStack = itemStack;
    }

    protected SimpleNmsEntry(int weight, int quality, List<LootItemCondition> conditions, List<LootItemFunction> functions, ItemStack itemStack) {
        super(weight, quality, conditions, functions);
        this.itemStack = itemStack;
    }

    public static CustomNmsLootEntry fromVanilla(@NotNull LootTable lootTable, Predicate<org.bukkit.inventory.ItemStack> predicate, ItemStack itemStack){
        Pair<List<LootItemCondition>, List<LootItemFunction>> conditionsAndFunctions = getConditionsAndFunctions(lootTable, predicate);
        Pair<Integer, Integer> weightAndQuality = Nms.get().getWeightAndQuality(lootTable, predicate);
        return new SimpleNmsEntry(
                weightAndQuality.getLeft(),
                weightAndQuality.getRight(),
                conditionsAndFunctions.getLeft(),
                conditionsAndFunctions.getRight(), itemStack);
    }

    @NotNull
    @Override
    public ItemStack createItemStack(@NotNull LootContext lootContext) {
        return itemStack;
    }
}
