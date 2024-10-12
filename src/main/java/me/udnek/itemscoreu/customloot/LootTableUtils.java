package me.udnek.itemscoreu.customloot;

import com.google.common.base.Preconditions;
import me.udnek.itemscoreu.ItemsCoreU;
import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.customloot.table.CustomLootTable;
import me.udnek.itemscoreu.customregistry.CustomRegistries;
import me.udnek.itemscoreu.nms.Nms;
import me.udnek.itemscoreu.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.LootTables;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LootTableUtils implements Listener {
    public static final NamespacedKey CUSTOM_LOOT_TABLE = new NamespacedKey(ItemsCoreU.getInstance(), "custom_loot_table");;
    private static final LootTableRegistry registry = CustomRegistries.LOOT_TABLE;

    public static @Nullable LootTable getLootTable(@NotNull NamespacedKey key){
        return getLootTable(key.asString());
    }
    public static @Nullable LootTable getLootTable(@NotNull String key){
        LootTable lootTable = registry.get(key);
        if (lootTable != null) return lootTable;
        lootTable = Nms.get().getLootTable(key);
        if (lootTable != LootTables.EMPTY.getLootTable()) return lootTable;
        return null;
    }

    public static List<LootTable> getAll(){
        List<LootTable> all = new ArrayList<>(registry.getAll());
        for (LootTable lootTable : getAllVanillaRegistered()) {
            if (registry.isVanillaLootTableReplaced(lootTable)) continue;
            all.add(lootTable);
        }
        return all;
    }
    public static List<LootTable> getAllVanillaRegistered() {
        return Nms.get().getRegisteredLootTables();
    }

    ///////////////////////////////////////////////////////////////////////////
    // ITEM
    ///////////////////////////////////////////////////////////////////////////
    public static List<ItemStack> getPossibleLoot(LootTable lootTable){
        if (lootTable instanceof CustomLootTable customLootTable){
            return customLootTable.getAllItems();
        }
        return Nms.get().getPossibleLoot(lootTable);
    }
    public static List<LootTable> getWhereItemOccurs(ItemStack itemStack){
        List<LootTable> lootTables = new ArrayList<>();
        for (CustomLootTable customLootTable : registry.getAll()) {
            if (customLootTable.containsItem(itemStack)) lootTables.add(customLootTable);
        }
        //if (CustomItem.isCustom(itemStack)) return lootTables;

        Nms nms = Nms.get();
        Material material = itemStack.getType();
        for (LootTable lootTable : getAllVanillaRegistered()) {

            if (registry.isVanillaLootTableReplaced(lootTable)) continue;

            List<ItemStack> itemStacks = nms.getPossibleLoot(lootTable);

            for (ItemStack loot : itemStacks) {
                if (ItemUtils.isSameIds(loot, itemStack)){
                    lootTables.add(lootTable);
                    break;
                }
            }
        }
        return lootTables;
    }
    ///////////////////////////////////////////////////////////////////////////
    // MOB
    ///////////////////////////////////////////////////////////////////////////
    public static void setLootTable(@NotNull Mob mob, @Nullable LootTable lootTable){
        if (lootTable instanceof CustomLootTable customLootTable){
            setCustomLootTable(mob, customLootTable);
            Nms.get().setDeathLootTable(mob, null);
        } else {
            Nms.get().setDeathLootTable(mob, lootTable);
        }
    }
    public static @NotNull LootTable getLootTable(@NotNull Mob mob){
        LootTable lootTable = getCustomLootTable(mob);
        if (lootTable != null) return lootTable;
        return getLootTable(Nms.get().getDeathLootTable(mob).getKey());
    }
    private static void setCustomLootTable(@NotNull Mob entity, CustomLootTable lootTable){
        PersistentDataContainer container = entity.getPersistentDataContainer();
        if (lootTable == null){
            container.remove(CUSTOM_LOOT_TABLE);
            return;
        }
        container.set(CUSTOM_LOOT_TABLE, PersistentDataType.STRING, lootTable.getKey().asString());
    }
    private static @Nullable CustomLootTable getCustomLootTable(@NotNull Mob mob){
        String id = mob.getPersistentDataContainer().get(CUSTOM_LOOT_TABLE, PersistentDataType.STRING);
        if (id == null) return null;
        return (CustomLootTable) getLootTable(id);
    }
    ///////////////////////////////////////////////////////////////////////////
    // EVENT
    ///////////////////////////////////////////////////////////////////////////
    @EventHandler(priority = EventPriority.LOW)
    public void onLootGenerates(LootGenerateEvent event){
        // TODO: 8/19/2024 INCORRECT WAY, USE PERSISTENT DATA
        NamespacedKey id = event.getLootTable().getKey();
        LootTable lootTable = getLootTable(id);
        Preconditions.checkArgument(
                lootTable != null,
                "LootTable at "+ event.getLootContext().getLocation().getBlock() + " is null: " + event.getLootTable().getKey().asString());
        if (lootTable instanceof CustomLootTable customLootTable) {
            customLootTable.onContainerLootGeneratesEvent(event);
        }
    }
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event){
        // TODO: 8/19/2024 FIX EQUIPMENT DO NOT DROPS
        if (!(event.getEntity() instanceof Mob mob)) return;
        LootTable lootTable = getLootTable(mob);
        if (lootTable instanceof CustomLootTable customLootTable) {
            customLootTable.onMobDeathEvent(event);
        }
    }
}
