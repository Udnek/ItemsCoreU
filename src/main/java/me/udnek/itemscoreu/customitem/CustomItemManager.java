package me.udnek.itemscoreu.customitem;

import me.udnek.itemscoreu.customevent.AllEventListener;
import me.udnek.itemscoreu.customevent.CustomEventManager;
import me.udnek.itemscoreu.utils.CustomThingManager;
import me.udnek.itemscoreu.utils.LogUtils;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Set;

public class CustomItemManager extends CustomThingManager<CustomItem> {

    private static final HashMap<String, CustomItem> customItemMap = new HashMap<>();
    public static void registerRecipes(){
        customItemMap.values().forEach(CustomItem::registerRecipes);
    }

    private static CustomItemManager instance;
    private CustomItemManager(){}
    public static CustomItemManager getInstance() {
        if (instance == null) instance = new CustomItemManager();
        return instance;
    }

    protected void put(CustomItem customItem){
        customItemMap.put(customItem.getId(), customItem);
    }
    @Override
    public String getCategory() {
        return "Item";
    }
    @Override
    protected String getIdToLog(CustomItem custom) {
        return custom.getId();
    }

    protected static CustomItem get(String id){
        return customItemMap.get(id);
    }
    protected static Set<String> getAllIds(){
        return customItemMap.keySet();
    }

}
