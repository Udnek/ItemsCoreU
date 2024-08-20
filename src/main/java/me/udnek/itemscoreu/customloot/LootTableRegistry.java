package me.udnek.itemscoreu.customloot;

import com.google.common.base.Preconditions;
import me.udnek.itemscoreu.ItemsCoreU;
import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.customloot.table.CustomLootTable;
import me.udnek.itemscoreu.nms.Nms;
import me.udnek.itemscoreu.utils.CustomRegistry;
import me.udnek.itemscoreu.utils.LogUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.LootTables;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class LootTableRegistry extends CustomRegistry<CustomLootTable> implements Listener {

    private final List<String> vanillaReplaced = new ArrayList<>();
    private final HashMap<String, CustomLootTable> CustomLootTables = new HashMap<>();
    private static LootTableRegistry instance;
    private LootTableRegistry(){
        Bukkit.getPluginManager().registerEvents(this, ItemsCoreU.getInstance());
    }
    public static LootTableRegistry getInstance() {
        if (instance == null) instance = new LootTableRegistry();
        return instance;
    }
    
    @Override
    public String getCategory() {
        return "CustomLootTable";
    }

    @Override
    protected String getIdToLog(CustomLootTable custom) {
        return custom.getKey().asString();
    }

    @Override
    protected void put(CustomLootTable lootTable) {
        Preconditions.checkArgument(
                !CustomLootTables.containsKey(lootTable.getKey().asString()),
                "LootTable already registered: " + lootTable.getKey().asString() + "!");
        if (Nms.get().getRegisteredLootTableIds().contains(lootTable.getKey().asString())){
            vanillaReplaced.add(lootTable.getKey().asString());
        }
        CustomLootTables.put(lootTable.getKey().asString(), lootTable);
    }

    public @Nullable LootTable getLootTable(@NotNull NamespacedKey key){
        LootTable lootTable = CustomLootTables.get(key.asString());
        if (lootTable != null) return lootTable;
        lootTable = Nms.get().getLootTable(key.asString());
        if (lootTable != LootTables.EMPTY.getLootTable()) return lootTable;
        return null;
    }

    public boolean isVanillaLootTableReplaced(LootTable lootTable){
        return vanillaReplaced.contains(lootTable.getKey().asString());
    }
    
    public List<LootTable> getAll(){
        List<LootTable> all = new ArrayList<>(getAllCustom());
        for (LootTable lootTable : getAllVanillaRegistered()) {
            if (isVanillaLootTableReplaced(lootTable)) continue;
            all.add(lootTable);
        }
        return all;
    }
    public Collection<CustomLootTable> getAllCustom(){
        return CustomLootTables.values();
    }
    public List<LootTable> getAllVanillaRegistered(){
        return Nms.get().getRegisteredLootTables();
    }
}
