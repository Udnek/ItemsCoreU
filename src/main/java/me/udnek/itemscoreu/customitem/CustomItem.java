package me.udnek.itemscoreu.customitem;

import me.udnek.itemscoreu.ItemsCoreU;
import me.udnek.itemscoreu.utils.PluginInitializable;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public interface CustomItem extends PluginInitializable {

    static final NamespacedKey PERSISTENT_DATA_CONTAINER_NAMESPACE = new NamespacedKey(ItemsCoreU.getInstance(), "item");

    ///////////////////////////////////////////////////////////////////////////
    // STATIC
    ///////////////////////////////////////////////////////////////////////////

    static String getId(ItemStack itemStack){
        if (itemStack == null) return null;
        if (!itemStack.hasItemMeta()) return null;
        return itemStack.getItemMeta().getPersistentDataContainer().get(PERSISTENT_DATA_CONTAINER_NAMESPACE, PersistentDataType.STRING);
    }
    static CustomItem get(ItemStack itemStack){return get(getId(itemStack));}
    static CustomItem get(String id){return CustomItemRegistry.get(id);}
    static boolean idExists(String id){return CustomItemRegistry.get(id) != null;}
    static boolean isCustom(ItemStack itemStack) {
        if (!itemStack.hasItemMeta()) return false;
        return itemStack.getItemMeta().getPersistentDataContainer().has(PERSISTENT_DATA_CONTAINER_NAMESPACE);
    }
    static Set<String> getAllIds(){
        return CustomItemRegistry.getAllIds();
    }
    static boolean isSameIds(ItemStack itemStack1, ItemStack itemStack2){
        CustomItem customItem1 = get(itemStack1);
        CustomItem customItem2 = get(itemStack2);
        if (customItem1 == null || customItem2 == null) return false;
        return customItem1 == customItem2;
    }
    default boolean isThisItem(ItemStack itemStack){
        return get(itemStack) == this;
    }

    ///////////////////////////////////////////////////////////////////////////
    // PROPERTIES
    ///////////////////////////////////////////////////////////////////////////
    @NotNull String getRawId();
    @NotNull String getId();
    ItemStack getItem();
    @NotNull List<Recipe> getRecipes();
    void registerRecipes();

    ///////////////////////////////////////////////////////////////////////////
    // EVENTS
    ///////////////////////////////////////////////////////////////////////////

    default ItemStack onPrepareCraft(PrepareItemCraftEvent event){
        return this.getItemFromCraftingMatrix(event.getRecipe().getResult(), event.getInventory().getMatrix(), event.getRecipe());
    }
    default ItemStack getItemFromCraftingMatrix(ItemStack result, ItemStack[] matrix, Recipe recipe){return result;}

}
