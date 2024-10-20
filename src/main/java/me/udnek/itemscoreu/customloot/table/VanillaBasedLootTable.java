package me.udnek.itemscoreu.customloot.table;

import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.nms.Nms;
import me.udnek.itemscoreu.util.ItemUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Deprecated
public class VanillaBasedLootTable extends BasicLootTable{

    protected final LootTable vanillaLootTable;
    protected Set<Material> toRemoveItems;
    protected HashMap<String, ItemStack> toReplaceItems;
    public VanillaBasedLootTable(LootTable vanilla){
        vanillaLootTable = vanilla;
    }
    @Override
    public void initialize(@NotNull Plugin plugin) {}

    public LootTable getVanilla() {return vanillaLootTable;}
    @Override
    public void removeItem(@NotNull ItemStack itemStack) {
        super.removeItem(itemStack);
        if (CustomItem.isCustom(itemStack)) return;
        if (toRemoveItems == null) toRemoveItems = new HashSet<>();
        toRemoveItems.add(itemStack.getType());
    }

    @Override
    public void replaceItem(@NotNull ItemStack oldItem, ItemStack newItem) {
        super.replaceItem(oldItem, newItem);
        String id = ItemUtils.getId(oldItem);
        if (toReplaceItems == null) toReplaceItems = new HashMap<>();
        toReplaceItems.put(id, newItem);
    }

    protected void removeItems(Collection<ItemStack> stacks){
        if (toRemoveItems == null) return;
        stacks.removeIf(itemStack -> toRemoveItems.contains(itemStack.getType()));
    }
    protected List<ItemStack> replaceItems(Collection<ItemStack> stacks){
        if (toReplaceItems == null) return new ArrayList<>(stacks);
        List<ItemStack> newList = new ArrayList<>();
        for (ItemStack itemStack : stacks) {
            for (Map.Entry<String, ItemStack> entry : toReplaceItems.entrySet()) {
                if (!ItemUtils.getId(itemStack).equals(entry.getKey())){
                    newList.add(itemStack);
                    continue;
                }
                newList.add(entry.getValue().asQuantity(itemStack.getAmount()));
            }
        }
        return newList;
    }
    @Override
    public @NotNull List<ItemStack> getAllItems() {
        List<ItemStack> possibleLoot = Nms.get().getPossibleLoot(vanillaLootTable);
        removeItems(possibleLoot);
        possibleLoot =  replaceItems(possibleLoot);
        possibleLoot.addAll(super.getAllItems());
        return possibleLoot;
    }

    @Override
    public @NotNull Collection<ItemStack> populateLoot(@Nullable Random random, @NotNull LootContext lootContext) {
        Collection<ItemStack> stacks = vanillaLootTable.populateLoot(random, lootContext);
        removeItems(stacks);
        stacks = replaceItems(stacks);
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
