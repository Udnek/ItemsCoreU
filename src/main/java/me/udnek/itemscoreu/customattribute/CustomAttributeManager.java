package me.udnek.itemscoreu.customattribute;

import me.udnek.itemscoreu.customevent.AllEventListener;
import me.udnek.itemscoreu.customevent.CustomEventManager;
import me.udnek.itemscoreu.utils.CustomThingManager;
import me.udnek.itemscoreu.utils.LogUtils;
import org.bukkit.plugin.java.JavaPlugin;

// TODO: 7/18/2024 STORE IN MAP????
public class CustomAttributeManager extends CustomThingManager<CustomAttribute> {


    private static CustomAttributeManager instance;
    private CustomAttributeManager(){}
    public static CustomAttributeManager getInstance() {
        if (instance == null) instance = new CustomAttributeManager();
        return instance;

    }

    @Override
    public String getCategory() {
        return "Attribute";
    }

    @Override
    protected String getIdToLog(CustomAttribute custom) {
        return custom.getId();
    }

    @Override
    protected void put(CustomAttribute custom) {
    }

}
