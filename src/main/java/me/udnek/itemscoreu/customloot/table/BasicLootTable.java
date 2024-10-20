package me.udnek.itemscoreu.customloot.table;

import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.customloot.entry.LootTableEntry;
import me.udnek.itemscoreu.util.ItemUtils;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public abstract class BasicLootTable implements CustomLootTable{
    protected List<LootTableEntry> entries = new ArrayList<>();
    @Override
    public void addEntry(@NotNull LootTableEntry lootTableEntry) {
        entries.add(lootTableEntry);
    }
    @Override
    public void removeEntry(@NotNull LootTableEntry lootTableEntry) {
        entries.remove(lootTableEntry);
    }
    @Override
    public void removeItem(@NotNull ItemStack itemStack) {
        entries.removeIf((lootTableEntry ->
                ItemUtils.isSameIds(lootTableEntry.getItem(), itemStack))
        );
    }

    @Override
    public void replaceItem(@NotNull ItemStack oldItem, ItemStack newItem) {
        for (LootTableEntry entry : entries) {
            if (!ItemUtils.isSameIds(oldItem, entry.getItem())) continue;
            entry.replaceItem(newItem);
        }
    }

    @Override
    public @NotNull List<ItemStack> getAllItems() {
        List<ItemStack> stacks = new ArrayList<>();
        for (LootTableEntry entry : entries) {
            stacks.add(entry.getItem());
        }
        return stacks;
    }

    @Override
    public @NotNull Collection<ItemStack> populateLoot(@Nullable Random random, @NotNull LootContext lootContext) {
        List<ItemStack> stacks = new ArrayList<>();
        for (LootTableEntry entry : entries) {
            ItemStack roll = entry.roll(random, lootContext);
            if (roll == null) continue;
            stacks.add(roll);
        }
        return stacks;
    }

    @Override
    public void fillInventory(@NotNull Inventory inventory, @Nullable Random random, @NotNull LootContext lootContext) {
        // TODO: 7/24/2024 REALISE
    }

    @Override
    public boolean containsItem(ItemStack itemStack) {
        if (CustomItem.isCustom(itemStack)){
            CustomItem customItem = CustomItem.get(itemStack);
            for (ItemStack item : getAllItems()) {
                CustomItem lootCustomItem = CustomItem.get(item);
                if (lootCustomItem == customItem) return true;
            }
        }
        else {
            Material material = itemStack.getType();
            for (ItemStack item : getAllItems()) {
                if (CustomItem.isCustom(item)) continue;
                if (item.getType() == material) return true;
            }
        }
        return false;
    }

    @Override
    public void onContainerLootGeneratesEvent(LootGenerateEvent event) {
        // TODO: 7/24/2024 REMOVE WHEN FILL INVENTORY WORKS
        event.setLoot(populateLoot(new Random(), event.getLootContext()));
    }

    @Override
    public void onMobDeathEvent(EntityDeathEvent event) {
        List<ItemStack> drops = event.getDrops();
        drops.clear();
        LivingEntity entity = event.getEntity();
        LootContext.Builder builder = new LootContext.Builder(entity.getLocation());
        builder.lootedEntity(entity);
        builder.killer(entity.getKiller());
        builder.luck((float) entity.getKiller().getAttribute(Attribute.GENERIC_LUCK).getValue());
        drops.addAll(populateLoot(new Random(), builder.build()));
    }
}
