package me.udnek.itemscoreu.utils;

import me.udnek.itemscoreu.ItemsCoreU;
import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.customloot.LootTableManager;
import me.udnek.itemscoreu.customloot.table.CustomLootTable;
import me.udnek.itemscoreu.customloot.table.VanillaBasedLootTable;
import me.udnek.itemscoreu.customrecipe.RecipeManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.loot.LootTable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VanillaItemDisabler extends SelfRegisteringListener{

    public static final EquipmentSlot[] ENTITY_SLOTS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND};
    private static VanillaItemDisabler instance;
    private final Set<Material> disabled = new HashSet<>();

    public static VanillaItemDisabler getInstance() {
        if (instance == null) instance = new VanillaItemDisabler();
        return instance;
    }
    private VanillaItemDisabler(){
        super(ItemsCoreU.getInstance());
    }

    public void disableItem(Material material){
        disabled.add(material);
    }

    public void runDisabler(){
        RecipeManager recipeManager = RecipeManager.getInstance();
        for (Material material : disabled) {

            ItemStack itemStack = new ItemStack(material);

            // recipe removal
            List<Recipe> recipes = recipeManager.getRecipesAsIngredient(itemStack);
            recipes.addAll(recipeManager.getRecipesAsResult(itemStack));
            for (Recipe recipe : recipes) {
                recipeManager.unregister(recipe);
            }

            LootTableManager lootTableManager = LootTableManager.getInstance();
            for (LootTable lootTable : lootTableManager.getWhereItemOccurs(itemStack)) {
                if (lootTable instanceof CustomLootTable customLootTable) {
                    customLootTable.removeItem(itemStack);
                    LogUtils.pluginLog("Custom lootTable was edited: " + customLootTable.getKey().asString());
                } else {
                    VanillaBasedLootTable vanillaBasedLootTable = new VanillaBasedLootTable(lootTable);
                    vanillaBasedLootTable.removeItem(itemStack);
                    lootTableManager.register(ItemsCoreU.getInstance(), vanillaBasedLootTable);
                    LogUtils.pluginLog("Vanilla lootTable was replaced!: " + lootTable.getKey().asString());
                }
            }

            LogUtils.pluginLog("Disabled: " + material);
        }
    }

    @EventHandler
    public void onSpawn(EntitySpawnEvent event){
        if (!(event.getEntity() instanceof LivingEntity livingEntity)) return;
        EntityEquipment equipment = livingEntity.getEquipment();
        if (equipment == null) return;
        for (EquipmentSlot slot : ENTITY_SLOTS) {
            ItemStack item = equipment.getItem(slot);
            if (isDisabled(item)) equipment.setItem(slot, null);
        }
    }

    @EventHandler
    public void onCreative(InventoryCreativeEvent event){
        ItemStack item = event.getCursor();
        if (!isDisabled(item)) return;
        event.setCancelled(true);
        event.getViewers().get(0)
                .sendMessage(Component.text("Item is disabled!").color(NamedTextColor.RED));
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(EntityDeathEvent event){
        List<ItemStack> toRemove = new ArrayList<>();
        for (ItemStack item : event.getDrops()) {
            if (isDisabled(item)) toRemove.add(item);
        }
        event.getDrops().removeAll(toRemove);
    }

    public static boolean isDisabled(ItemStack item){
        if (!isDisabled(item.getType())) return false;
        return !CustomItem.isCustom(item);
    }
    public static boolean isDisabled(Material material){
        return getInstance().disabled.contains(material);
    }
}
