package me.udnek.itemscoreu.utils;

import me.udnek.itemscoreu.ItemsCoreU;
import me.udnek.itemscoreu.customitem.CustomItem;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.LootTables;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class CustomItemUtils {
    public static final NamespacedKey namespacedKey = new NamespacedKey(ItemsCoreU.getInstance(), "item");

    private static final HashMap<String, CustomItem> customItemMap = new HashMap<String, CustomItem>();

    public static void putCustomItem(CustomItem customItem){
         customItemMap.put(customItem.getId(), customItem);
    }

    public static boolean isCustomItem(ItemStack itemStack){
        return (itemStack.hasItemMeta() && itemStack.getItemMeta().getPersistentDataContainer().has(namespacedKey, PersistentDataType.STRING));
    }
    public static String getId(ItemStack itemStack){
        if (!itemStack.hasItemMeta()) return null;
        return (itemStack.getItemMeta().getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING));
    }
    public static boolean isIdExists(String id){
        return customItemMap.containsKey(id);
    }

    public static boolean isItemStackEqualsId(ItemStack itemStack, String id){
        if (!(isCustomItem(itemStack))) return false;
        return getId(itemStack).equals(id);
    }

    public static Set<String> getAllIds(){
        return customItemMap.keySet();
    }
    public static CustomItem getFromId(String id){
        return customItemMap.get(id);
    }
    public static CustomItem getFromItemStack(ItemStack itemStack){
        return customItemMap.get(getId(itemStack));
    }
    public static ItemStack getItemStackFromId(String id){
        return getFromId(id).getItem();
    }

    public static ItemStack getItemStackFromItemName(String id){
        if (isIdExists(id)){
            return getItemStackFromId(id);
        }
        return new ItemStack(Material.valueOf(id.toUpperCase()));
    }

    public static Boolean isItemNameExists(String id){
        if (isIdExists(id)){
            return true;
        }
        return Material.getMaterial(id.toUpperCase()) != null;
    }

    public static Boolean isSimilar(ItemStack i1, ItemStack i2){
        if (i1.isSimilar(i2)) return true;
        CustomItem ci1 = getFromItemStack(i1);
        CustomItem ci2 = getFromItemStack(i2);
        if (ci1 == null && ci2 == null) return (i1.getType() == i2.getType());
        if (ci1 != null && ci2 != null) return (Objects.equals(ci1.getId(), ci2.getId()));
        return false;
    }

/*    public static String toFullId(String shortId){
        String[] split = shortId.split(":", 2);
        String namespace = split[0];
        String id = split[1];

        return new NamespacedKey(ItemsCoreU.getInstance(), "item" + "/" + namespace + "/" + id).asString();
    }*/
}
