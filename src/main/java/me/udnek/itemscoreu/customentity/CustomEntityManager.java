package me.udnek.itemscoreu.customentity;

import me.udnek.itemscoreu.utils.CustomThingManager;
import me.udnek.itemscoreu.utils.LogUtils;

import java.util.HashMap;

// TODO: 7/18/2024 SET TO CUSTOM ENTITY NOT DUMB
public class CustomEntityManager extends CustomThingManager<CustomDumbTickingEntity> {

    private static final HashMap<String, CustomDumbTickingEntity> registeredEntities = new HashMap<>();

    private static CustomEntityManager instance;
    private CustomEntityManager(){}
    public static CustomEntityManager getInstance() {
        if (instance == null) instance = new CustomEntityManager();
        return instance;
    }

    @Override
    public String getCategory() {
        return "Entity";
    }

    @Override
    protected String getIdToLog(CustomDumbTickingEntity custom) {
        return custom.getId();
    }

    @Override
    protected void put(CustomDumbTickingEntity custom) {
        registeredEntities.put(custom.getId(), custom);
    }

    public static CustomDumbTickingEntity get(String id){
        return registeredEntities.getOrDefault(id, null);
    }

}
