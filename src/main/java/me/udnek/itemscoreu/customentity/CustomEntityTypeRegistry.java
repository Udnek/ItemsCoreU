package me.udnek.itemscoreu.customentity;

import me.udnek.itemscoreu.utils.CustomRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Set;

public class CustomEntityTypeRegistry extends CustomRegistry<CustomEntityType> {

    private final HashMap<String, CustomEntityType> registered = new HashMap<>();

    private static CustomEntityTypeRegistry instance;
    private CustomEntityTypeRegistry(){}
    public static CustomEntityTypeRegistry getInstance() {
        if (instance == null) instance = new CustomEntityTypeRegistry();
        return instance;
    }

    @Override
    public String getCategory() {
        return "Entity";
    }
    @Override
    protected String getIdToLog(CustomEntityType custom) {
        return custom.getId();
    }
    @Override
    protected void put(CustomEntityType custom) {
        registered.put(custom.getId(), custom);
    }

    public @Nullable CustomEntityType get(String id){
        return registered.get(id);
    }
    protected Set<String> getAllIds(){
        return registered.keySet();
    }

}
