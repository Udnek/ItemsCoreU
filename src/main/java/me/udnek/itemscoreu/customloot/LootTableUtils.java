package me.udnek.itemscoreu.customloot;

import com.google.common.base.Preconditions;
import me.udnek.itemscoreu.ItemsCoreU;
import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.customloot.table.CustomLootTable;
import me.udnek.itemscoreu.nms.Nms;
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
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LootTableUtils implements Listener {
    public static final NamespacedKey CUSTOM_LOOT_TABLE = new NamespacedKey(ItemsCoreU.getInstance(), "custom_loot_table");

    private static final LootTableRegistry manager = LootTableRegistry.getInstance();

    public static List<ItemStack> getPossibleLoot(LootTable lootTable){
        if (lootTable instanceof CustomLootTable customLootTable){
            return customLootTable.getAllItems();
        }
        return Nms.get().getPossibleLoot(lootTable);
    }


    public static List<LootTable> getWhereItemOccurs(ItemStack itemStack){
        List<LootTable> lootTables = new ArrayList<>();
        for (CustomLootTable customLootTable : manager.getAllCustom()) {
            if (customLootTable.containsItem(itemStack)) lootTables.add(customLootTable);
        }
        if (CustomItem.isCustom(itemStack)) return lootTables;

        Nms nms = Nms.get();
        Material material = itemStack.getType();
        for (LootTable lootTable : manager.getAllVanillaRegistered()) {

            if (manager.isVanillaLootTableReplaced(lootTable)) continue;

            List<ItemStack> itemStacks = nms.getPossibleLoot(lootTable);

            for (ItemStack loot : itemStacks) {
                if (loot.getType() == material){
                    lootTables.add(lootTable);
                    break;
                }
            }
        }
        return lootTables;
    }

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
        return LootTableRegistry.getInstance().getLootTable(Nms.get().getDeathLootTable(mob).getKey());
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
        return (CustomLootTable) manager.getLootTable(NamespacedKey.fromString(id));
    }


    @EventHandler(priority = EventPriority.LOW)
    public void onLootGenerates(LootGenerateEvent event){
        // TODO: 8/19/2024 INCORRECT WAY, USE PERSISTENT DATA
        NamespacedKey id = event.getLootTable().getKey();
        LootTable lootTable = manager.getLootTable(id);
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
