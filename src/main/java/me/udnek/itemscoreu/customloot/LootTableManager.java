package me.udnek.itemscoreu.customloot;

import com.google.common.base.Preconditions;
import me.udnek.itemscoreu.ItemsCoreU;
import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.customloot.table.CustomLootTable;
import me.udnek.itemscoreu.nms.Nms;
import me.udnek.itemscoreu.utils.CustomRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.LootTables;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class LootTableManager extends CustomRegistry<CustomLootTable> implements Listener {

    private final List<String> vanillaReplaced = new ArrayList<>();
    private final HashMap<String, CustomLootTable> CustomLootTables = new HashMap<>();
    private static LootTableManager instance;
    private LootTableManager(){
        Bukkit.getPluginManager().registerEvents(this, ItemsCoreU.getInstance());
    }
    public static LootTableManager getInstance() {
        if (instance == null) instance = new LootTableManager();
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

    public @Nullable LootTable getLootTable(String id){
        LootTable lootTable = CustomLootTables.get(id);
        if (lootTable != null) return lootTable;
        lootTable = Nms.get().getLootTable(id);
        if (lootTable != LootTables.EMPTY.getLootTable()) return lootTable;
        return null;
    }

    public List<LootTable> getWhereItemOccurs(ItemStack itemStack){
        List<LootTable> lootTables = new ArrayList<>();
        for (CustomLootTable customLootTable : getAllCustom()) {
            if (customLootTable.containsItem(itemStack)) lootTables.add(customLootTable);
        }
        if (CustomItem.isCustom(itemStack)) return lootTables;

        Nms nms = Nms.get();
        Material material = itemStack.getType();
        for (LootTable lootTable : getAllVanillaRegistered()) {

            if (isVanillaLootTableReplaced(lootTable)) continue;

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

    public List<ItemStack> getPossibleLoot(LootTable lootTable){
        if (lootTable instanceof CustomLootTable customLootTable){
            return customLootTable.getAllItems();
        }
        return Nms.get().getPossibleLoot(lootTable);
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

    @EventHandler(priority = EventPriority.LOW)
    public void onLootGenerates(LootGenerateEvent event){
        String id = event.getLootTable().getKey().asString();
        LootTable lootTable = getLootTable(id);
        Preconditions.checkArgument(
                lootTable != null,
                "LootTable at "+ event.getLootContext().getLocation().getBlock() + " is null: " + event.getLootTable().getKey().asString());
        if (lootTable instanceof CustomLootTable customLootTable) {
            customLootTable.onLootGeneratesEvent(event);
        }
    }

    public void onEntityDeath(EntityDeathEvent event){
        LootTable lootTable = Nms.get().getDeathLootTable(event.getEntity());
        // TODO: 7/29/2024 CALL LOOTTABLE
    }
}
