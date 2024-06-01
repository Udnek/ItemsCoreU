package me.udnek.itemscoreu.customitem;

import me.udnek.itemscoreu.utils.CustomItemUtils;
import me.udnek.itemscoreu.utils.LogUtils;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;

import static me.udnek.itemscoreu.utils.CustomItemUtils.putCustomItem;

public class CustomItemManager {

    public static CustomItem registerItem(JavaPlugin plugin, CustomItem customItem){
        customItem.initialize(plugin);
        putCustomItem(customItem);
        LogUtils.pluginLog(customItem.getId() + " (Item)");
        return customItem;
    }

    public static void registerRecipes(){
        for (String id : CustomItemUtils.getAllIds()) {
            CustomItemUtils.getFromId(id).registerRecipes();
        }
    }

    //TODO REALISE
    public static void registerAdditionalRecipe(String id, Recipe...recipes){
    }

}
