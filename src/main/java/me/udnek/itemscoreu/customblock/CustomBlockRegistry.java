package me.udnek.itemscoreu.customblock;

import me.udnek.itemscoreu.utils.CustomRegistry;

import java.util.HashMap;
import java.util.Set;

public class CustomBlockRegistry extends CustomRegistry<CustomBlock> {

    private static final HashMap<String, CustomBlock> customBlocks = new HashMap<>();

    private static CustomBlockRegistry instance;
    private CustomBlockRegistry(){}
    public static CustomBlockRegistry getInstance() {
        if (instance == null) instance = new CustomBlockRegistry();
        return instance;

    }


    @Override
    public String getCategory() {
        return "Block";
    }

    @Override
    protected String getIdToLog(CustomBlock custom) {
        return custom.getId();
    }

    protected void put(CustomBlock customBlock){
        customBlocks.put(customBlock.getId(), customBlock);
    }

    protected static CustomBlock get(String id){
        return customBlocks.get(id);
    }
    protected static Set<String> getAllIds(){
        return customBlocks.keySet();
    }

}
