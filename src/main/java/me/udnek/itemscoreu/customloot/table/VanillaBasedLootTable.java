package me.udnek.itemscoreu.customloot.table;

import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.nms.Nms;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class VanillaBasedLootTable extends BasicLootTable {

    protected final LootTable vanillaLootTable;
    protected Set<Material> toRemoveItems;
    public VanillaBasedLootTable(LootTable vanilla){
        vanillaLootTable = vanilla;
    }
    @Override
    public void initialize(@NotNull JavaPlugin plugin) {}
    public LootTable getVanilla() {return vanillaLootTable;}
    @Override
    public void removeItem(@NotNull ItemStack itemStack) {
        super.removeItem(itemStack);
        if (CustomItem.isCustom(itemStack)) return;
        if (toRemoveItems == null) toRemoveItems = new HashSet<>();
        toRemoveItems.add(itemStack.getType());
    }

    protected void removeItems(Collection<ItemStack> stacks){
        stacks.removeIf(itemStack -> toRemoveItems.contains(itemStack.getType()));
    }
    @Override
    public @NotNull List<ItemStack> getAllItems() {
        List<ItemStack> possibleLoot = Nms.get().getPossibleLoot(vanillaLootTable);
        removeItems(possibleLoot);
        possibleLoot.addAll(super.getAllItems());
        return possibleLoot;
    }

    @Override
    public @NotNull Collection<ItemStack> populateLoot(@Nullable Random random, @NotNull LootContext lootContext) {
        Collection<ItemStack> stacks = vanillaLootTable.populateLoot(random, lootContext);
        removeItems(stacks);
        stacks.addAll(super.populateLoot(random, lootContext));
        return stacks;
    }

    @Override
    public void fillInventory(@NotNull Inventory inventory, @Nullable Random random, @NotNull LootContext lootContext) {
        // TODO: 7/24/2024 INJECT EXTRA
        vanillaLootTable.fillInventory(inventory, random, lootContext);

    }

    @Override
    public @NotNull NamespacedKey getKey() {
        return vanillaLootTable.getKey();
    }
}
