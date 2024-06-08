package me.udnek.itemscoreu.customitem;

import me.udnek.itemscoreu.customevent.CustomEvent;
import me.udnek.itemscoreu.customevent.CustomEventListener;
import me.udnek.itemscoreu.customevent.CustomEventManager;
import me.udnek.itemscoreu.utils.LogUtils;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Set;

public class CustomItemManager {

    private static final HashMap<String, CustomItem> customItemMap = new HashMap<String, CustomItem>();


    public static CustomItem registerItem(JavaPlugin plugin, CustomItem customItem){
        customItem.initialize(plugin);
        putItem(customItem);
        if (customItem instanceof CustomEventListener customEventListener) CustomEventManager.addListener(customEventListener);
        return customItem;
    }

    public static void registerRecipes(){
        customItemMap.values().forEach(CustomItem::registerRecipes);
    }

    private static void putItem(CustomItem customItem){
        if (!customItemMap.containsKey(customItem.getId())){
            customItemMap.put(customItem.getId(), customItem);
            LogUtils.pluginLog(customItem.getId() + " (Item)");
        }
    }

    protected static CustomItem get(String id){
        return customItemMap.get(id);
    }

    protected static Set<String> getAllIds(){
        return customItemMap.keySet();
    }

    //TODO REALISE
    public static void registerAdditionalRecipe(String id, Recipe...recipes){}

}
