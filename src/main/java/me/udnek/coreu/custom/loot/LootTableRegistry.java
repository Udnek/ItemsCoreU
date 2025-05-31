package me.udnek.coreu.custom.loot;

import com.google.common.base.Preconditions;
import me.udnek.coreu.custom.loot.table.CustomLootTable;
import me.udnek.coreu.custom.registry.MappedCustomRegistry;
import me.udnek.coreu.nms.Nms;
import me.udnek.coreu.util.LogUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.loot.LootTable;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LootTableRegistry extends MappedCustomRegistry<CustomLootTable> {

    final List<String> vanillaReplaced = new ArrayList<>();

    public LootTableRegistry(@NotNull String rawId) {
        super(rawId);
    }

    @Override
    public <V extends CustomLootTable> @NotNull V register(@NotNull Plugin plugin, @NotNull V lootTable) {
        Preconditions.checkArgument(
                !map.containsKey(lootTable.getKey().asString()),
                "LootTable already registered: " + lootTable.getKey().asString() + "!");

        if (Nms.get().getRegisteredLootTableIds().contains(lootTable.getKey().asString())){
            vanillaReplaced.add(lootTable.getKey().asString());
        }

        lootTable.initialize(plugin);
        map.put(lootTable.getId(), lootTable);
        logRegistered(lootTable);
        if (lootTable instanceof Listener listener){
            Bukkit.getPluginManager().registerEvents(listener, plugin);
            LogUtils.pluginLog("(EventListener) " + listener.getClass().getName());
        }
        return lootTable;
    }

    boolean isVanillaLootTableReplaced(LootTable lootTable){
        return vanillaReplaced.contains(lootTable.getKey().asString());
    }


/*    public @Nullable LootTable getLootTable(@NotNull NamespacedKey key){
        LootTable lootTable = map.get(key.asString());
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
        return customLootTables.values();
    }
    public List<LootTable> getAllVanillaRegistered() {
        return Nms.get().getRegisteredLootTables();
    }
    public HashMap<String, CustomLootTable> getCustomLootTables() {
        return customLootTables;
    }*/
}
