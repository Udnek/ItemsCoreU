package me.udnek.itemscoreu.customitem;

import me.udnek.itemscoreu.utils.CustomRegistry;

import java.util.HashMap;
import java.util.Set;

public class CustomItemRegistry extends CustomRegistry<CustomItem> {

    private static final HashMap<String, CustomItem> customItemMap = new HashMap<>();
    public static void registerRecipes(){
        customItemMap.values().forEach(CustomItem::registerRecipes);
    }

    private static CustomItemRegistry instance;
    private CustomItemRegistry(){}
    public static CustomItemRegistry getInstance() {
        if (instance == null) instance = new CustomItemRegistry();
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
